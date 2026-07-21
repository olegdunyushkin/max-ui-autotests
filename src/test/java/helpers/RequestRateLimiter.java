package helpers;

import config.ProjectConfig;

import java.time.Duration;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public final class RequestRateLimiter {

    private static final ReentrantLock LOCK = new ReentrantLock(true);
    private static long nextRequestTime;

    private RequestRateLimiter() {
    }

    public static void waitBeforeRequest() {
        long interval = ProjectConfig.requestInterval();
        if (interval <= 0) {
            return;
        }

        LOCK.lock();
        try {
            waitUntilRequestIsAllowed();
            nextRequestTime = System.nanoTime() + Duration.ofMillis(interval).toNanos();
        } finally {
            LOCK.unlock();
        }
    }

    private static void waitUntilRequestIsAllowed() {
        long remainingTime;
        while ((remainingTime = nextRequestTime - System.nanoTime()) > 0) {
            LockSupport.parkNanos(remainingTime);
            if (Thread.currentThread().isInterrupted()) {
                throw new IllegalStateException("Ожидание перед запросом было прервано");
            }
        }
    }
}
