package test;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class CancelableThreadPoolDemo {

    // 1. 使用 AtomicBoolean 作为手动停止的开关
    private static final AtomicBoolean isRunning = new AtomicBoolean(true);

    public static void main(String[] args) throws InterruptedException {
        // 创建线程池：2个线程，10个队列空间，CallerRunsPolicy 策略
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2, 2,
                0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(10),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        // 模拟一个外部线程，在 3 秒后手动终止任务
        new Thread(() -> {
            try {
                Thread.sleep(3000); // 等待 3 秒
                System.err.println("\n>>> [用户点击了停止按钮] <<<\n");
                stopTasks(executor);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        System.out.println("--- 开始添加 4000 个任务 ---");

        for (int i = 1; i <= 4000; i++) {
            // 2. 每次循环前检查开关：如果已停止，直接退出循环
            if (!isRunning.get()) {
                break;
            }

            final int taskId = i;
            try {
                System.out.println(Thread.currentThread().getName() + " 正在提交任务: " + taskId);
                
                executor.execute(() -> {
                    try {
                        // 模拟文件上传耗时
                        Thread.sleep(1000);
                        System.out.println(Thread.currentThread().getName() + " [OK] 完成: " + taskId);
                    } catch (InterruptedException e) {
                        // 响应中断，用于 shutdownNow 时的立即退出
                        System.out.println(Thread.currentThread().getName() + " [!] 任务 " + taskId + " 被中断停止");
                    }
                });
            } catch (RejectedExecutionException e) {
                // 如果 executor 已经关闭，execute 可能会抛出此异常
                break;
            }
        }

        System.out.println("--- 主线程循环结束，等待存量任务收尾 ---");
        
        // 等待所有任务真正结束
        if (executor.awaitTermination(5, TimeUnit.SECONDS)) {
            System.out.println("--- 流程彻底结束 ---");
        }
    }

    // 手动终止的逻辑
    public static void stopTasks(ThreadPoolExecutor executor) {
        // A. 停止主线程的 for 循环继续提交
        isRunning.set(false);
        
        // B. 停止线程池接受新任务，并尝试中断正在执行的任务
        executor.shutdownNow(); 
        
        System.out.println("终止指令已发出...");
    }
}