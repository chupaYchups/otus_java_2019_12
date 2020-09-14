package ru.chupaYchups;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class OrderedThreadOutput {

    private static Logger logger = LoggerFactory.getLogger(OrderedThreadOutput.class);

    private ReentrantLock lock = new ReentrantLock(true);
    private Condition notAllOnStartCondition = lock.newCondition();

    private int counterOnStart = 0;

    public static void main(String[] args) {
        OrderedThreadOutput orderedThreadOutput = new OrderedThreadOutput();
        orderedThreadOutput.doIt();
    }

    public void doIt() {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Runnable runnable = () -> {
            try {
                loop();
            } catch (InterruptedException e) {
                logger.error("Exception in thread " + Thread.currentThread().getName() , e);
                Thread.currentThread().interrupt();
            }
        };
        executor.execute(runnable);
        executor.execute(runnable);
        executor.shutdown();
    }

    private void doTheOutput(int number) throws InterruptedException {
        lock.lock();
        try {
            if (counterOnStart < 1) {
                counterOnStart++;
                notAllOnStartCondition.await();
            } else {
                notAllOnStartCondition.signal();
            }
            logger.info(String.format("%" + number + "d",  number));
            sleep(300);
        } finally {
            lock.unlock();
        }
    }

    private void loop() throws InterruptedException {
        while (!Thread.currentThread().isInterrupted()) {
            for (int i = 1; i <= 10; i++) {
                doTheOutput(i);
            }
            for (int i = 9; i > 1; i--) {
                doTheOutput(i);
            }
        }
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
