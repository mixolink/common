package com.amituofo.common.kit.retry;

import com.amituofo.common.type.HandleFeedback;

public class ReturnValue<T> {
	public final static ReturnValue<Void> FAILED = new ReturnValue<Void>(HandleFeedback.failed, null);
	public final static ReturnValue<Void> SUCCEED = new ReturnValue<Void>(HandleFeedback.succeed, null);
	public final static ReturnValue<Void> IGNORED = new ReturnValue<Void>(HandleFeedback.ignored, null);
	public final static ReturnValue<Void> INTERRUPTED = new ReturnValue<Void>(HandleFeedback.interrupted, null);

	private HandleFeedback feedback;
	private Exception exception = null;
	private T data = null;

	private ReturnValue(HandleFeedback feedback, T data) {
		super();
		this.feedback = feedback;
		this.data = data;
	}

	public ReturnValue(HandleFeedback feedback, T data, Exception exception) {
		super();
		this.feedback = feedback;
		this.data = data;
		this.exception = exception;
	}

	public HandleFeedback getResult() {
		return feedback;
	}

	public T getReturnData() {
		return data;
	}

	public boolean isFailed() {
		return feedback == HandleFeedback.failed;
	}

	public boolean isSucceed() {
		return feedback == HandleFeedback.succeed;
	}

	public boolean isIgnored() {
		return feedback == HandleFeedback.ignored;
	}

	public boolean isInterrupted() {
		return feedback == HandleFeedback.interrupted;
	}

	public static ReturnValue<Void> failed() {
		return FAILED;
	}

	public static ReturnValue<Void> succeed() {
		return SUCCEED;
	}

	public static ReturnValue<Void> ignored() {
		return IGNORED;
	}

	public static ReturnValue<Void> interrupted() {
		return INTERRUPTED;
	}

	public Exception getException() {
		return exception;
	}

	public static <T> ReturnValue<T> failed(T data) {
		return new ReturnValue<T>(HandleFeedback.failed, data);
	}

	public static <T> ReturnValue<T> failed(T data, Exception exception) {
		return new ReturnValue<T>(HandleFeedback.failed, data, exception);
	}

	public static <T> ReturnValue<T> succeed(T data) {
		return new ReturnValue<T>(HandleFeedback.succeed, data);
	}

	public static <T> ReturnValue<T> ignored(T data) {
		return new ReturnValue<T>(HandleFeedback.ignored, data);
	}

	public static <T> ReturnValue<T> interrupted(T data) {
		return new ReturnValue<T>(HandleFeedback.interrupted, data);
	}

	public static ReturnValue<Boolean> build(Boolean true_ok_false_faliled) {
		return new ReturnValue<Boolean>(true_ok_false_faliled ? HandleFeedback.succeed : HandleFeedback.failed, true_ok_false_faliled);
	}
}
