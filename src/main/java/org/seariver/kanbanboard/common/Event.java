package org.seariver.kanbanboard.common;

public abstract class Event {

    protected Exception exception;
    protected long startTime;
    protected long stopTime;

    public abstract String getOrigin();

    public abstract String toJson();

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public void startTimer() {
        startTime = System.nanoTime();
    }

    public void stopTimer() {
        stopTime = System.nanoTime();
    }

    public long getElapsedTimeInNano() {
        return stopTime - startTime;
    }

    public long getElapsedTimeInMilli() {
        return getElapsedTimeInNano() / 1_000_000L;
    }

    public boolean isSuccess() {
        return exception == null;
    }

    public boolean hasError() {
        return !isSuccess();
    }
}
