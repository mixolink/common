package com.amituofo.common.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.amituofo.common.api.StepProgressListener;
import com.amituofo.common.type.ReadProgressEvent;

/** Samples native copies; arbitrary listeners never run on the sampler or under a state lock. */
final class FileTransferProgress implements AutoCloseable {
    private static final ScheduledThreadPoolExecutor SAMPLER = new ScheduledThreadPoolExecutor(1, runnable -> {
        Thread thread = new Thread(runnable, "File-Transfer-Progress");
        thread.setDaemon(true);
        return thread;
    });
    private static final ExecutorService CALLBACKS = Executors.newCachedThreadPool(runnable -> {
        Thread thread = new Thread(runnable, "File-Transfer-Callback");
        thread.setDaemon(true);
        return thread;
    });
    static { SAMPLER.setRemoveOnCancelPolicy(true); }

    interface Operation { void run(FileTransferProgress progress) throws IOException; }

    private Path target;
    private BasicFileAttributes originalTarget;
    private final StepProgressListener listener;
    private final long totalBytes;
    private ScheduledFuture<?> sampling;
    private long observedBytes;
    private long deliveredBytes;
    private boolean closed;
    private boolean successful;
    private boolean endSent;
    private boolean listenerFailed;
    private boolean targetChanged;
    private boolean drainScheduled;

    private FileTransferProgress(Path source, Path target, StepProgressListener listener) {
        this.listener = listener;
        BasicFileAttributes sourceAttributes = attributes(source);
        this.totalBytes = sourceAttributes == null ? -1 : sourceAttributes.size();
        observe(target);
        if (sourceAttributes != null && sourceAttributes.isRegularFile() && !Files.isSymbolicLink(source)) {
            sampling = SAMPLER.scheduleWithFixedDelay(this::sample, 2, 2, TimeUnit.SECONDS);
        }
    }

    static void run(Path source, Path target, StepProgressListener listener, Operation operation) throws IOException {
        if (listener == null) {
            operation.run(null);
            return;
        }
        try (FileTransferProgress progress = new FileTransferProgress(source, target, listener)) {
            operation.run(progress);
            progress.complete();
        }
    }

    void observe(Path target) {
        BasicFileAttributes initial = attributes(target);
        synchronized (this) {
            this.target = target;
            this.originalTarget = initial;
            this.targetChanged = initial == null || initial.size() == 0;
        }
    }

    private void sample() {
        Path sampledTarget;
        synchronized (this) {
            if (closed || listenerFailed || totalBytes <= 0) return;
            sampledTarget = target;
        }
        BasicFileAttributes current = attributes(sampledTarget);
        synchronized (this) {
            if (closed || listenerFailed || !sampledTarget.equals(target) || current == null || !current.isRegularFile()) return;
            if (!targetChanged) {
                targetChanged = !Objects.equals(originalTarget.fileKey(), current.fileKey())
                        || current.size() < originalTarget.size()
                        || !originalTarget.lastModifiedTime().equals(current.lastModifiedTime())
                        || !originalTarget.creationTime().equals(current.creationTime());
            }
            if (targetChanged) {
                observedBytes = Math.max(observedBytes, Math.min(current.size(), totalBytes - 1));
                scheduleDrain();
            }
        }
    }

    private void complete() {
        BasicFileAttributes current = totalBytes < 0 ? attributes(target) : null;
        synchronized (this) {
            closed = true;
            successful = true;
            if (sampling != null) sampling.cancel(false);
            observedBytes = Math.max(observedBytes, totalBytes >= 0 ? totalBytes : current == null ? observedBytes : current.size());
            scheduleDrain();
        }
    }

    /** Called only under the state lock; at most one callback runner exists per transfer. */
    private void scheduleDrain() {
        if (!drainScheduled && !listenerFailed) {
            drainScheduled = true;
            CALLBACKS.execute(this::drain);
        }
    }

    private void drain() {
        while (true) {
            ReadProgressEvent event;
            int step;
            synchronized (this) {
                if (listenerFailed || (closed && !successful)) {
                    drainScheduled = false;
                    return;
                }
                if (observedBytes > deliveredBytes) {
                    step = (int) Math.min(observedBytes - deliveredBytes, Integer.MAX_VALUE);
                    deliveredBytes += step;
                    event = ReadProgressEvent.BYTE_READING_EVENT;
                } else if (successful && !endSent) {
                    endSent = true;
                    event = ReadProgressEvent.BYTE_READ_END_EVENT;
                    step = 0;
                } else {
                    drainScheduled = false;
                    return;
                }
            }
            try {
                listener.progressChanged(event, step);
            } catch (RuntimeException e) {
                synchronized (this) {
                    listenerFailed = true;
                    drainScheduled = false;
                }
                return;
            }
        }
    }

    @Override
    public synchronized void close() {
        closed = true;
        if (sampling != null) sampling.cancel(false);
    }

    private static BasicFileAttributes attributes(Path path) {
        try { return Files.readAttributes(path, BasicFileAttributes.class); }
        catch (IOException | SecurityException e) { return null; }
    }
}
