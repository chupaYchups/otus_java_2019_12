package ru.chupaYchups;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class OrderedThreadOutput {

    private static Logger logger = LoggerFactory.getLogger(OrderedThreadOutput.class);

    private ReentrantLock printLock = new ReentrantLock();
    private Condition ifLastCondition = printLock.newCondition();

    public static final String THREAD_FIRST_NAME = "thread-1";
    public static final String THREAD_SECOND_NAME = "thread-2";

    private String lastThreadName = THREAD_SECOND_NAME;

    public static void main(String[] args) {
        OrderedThreadOutput orderedThreadOutput = new OrderedThreadOutput();
        orderedThreadOutput.doIt();
    }

    public void doIt() {
        Thread threadFirst = new Thread(this::start);
        threadFirst.setName(THREAD_FIRST_NAME);
        Thread threadSecond = new Thread(this::start);
        threadSecond.setName(THREAD_SECOND_NAME);
        threadFirst.start();
        threadSecond.start();
    }

    private void start() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                for (int i = 1; i <= 10; i++) {
                    printNumber(i);
                }
                for (int i = 9; i > 1; i--) {
                    printNumber(i);
                }
            }
        } catch (InterruptedException exc) {
            Thread.currentThread().interrupt();
            logger.error(Thread.currentThread().getName() + " interrupted", exc);
        }
    }

    private void printNumber(int number) throws InterruptedException {
        printLock.lock();
        try {
            String currentThreadName = Thread.currentThread().getName();
            if (lastThreadName.equals(currentThreadName)) {
                ifLastCondition.await();
            }
            logger.info(String.format("%" + number + "d",  number));
            sleep(300);
            lastThreadName = currentThreadName;
            ifLastCondition.signalAll();
        } finally {
            printLock.unlock();
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
