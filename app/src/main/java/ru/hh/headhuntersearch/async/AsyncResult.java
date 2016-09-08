package ru.hh.headhuntersearch.async;

public class AsyncResult<D> {
    private Exception exception;
    private D data;

    public AsyncResult(Exception exception) {
        this.exception = exception;
    }

    public AsyncResult(D data) {
        this.data = data;
    }

    public Exception getException() {
        return exception;
    }

    public D getData() {
        return data;
    }
}