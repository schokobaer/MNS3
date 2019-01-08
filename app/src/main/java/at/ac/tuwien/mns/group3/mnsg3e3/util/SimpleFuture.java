package at.ac.tuwien.mns.group3.mnsg3e3.util;

import java.util.concurrent.*;

public class SimpleFuture<T> implements Future<T> {

    private final CountDownLatch latch = new CountDownLatch(1);
    private boolean canceled;
    private T result;

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (isDone() || canceled) {
            return false;
        }
        canceled = true;
        put(null);
        return true;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public boolean isDone() {
        return latch.getCount() == 0;
    }

    @Override
    public T get() throws ExecutionException, InterruptedException {
        latch.await();
        return result;
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
        if (latch.await(timeout, unit)) {
            return result;
        } else {
            this.canceled = true;
            throw new TimeoutException();
        }
    }

    public void put(T result) {
        if (isDone() || canceled) {
            return;
        }
        this.result = result;
        latch.countDown();
    }
}
