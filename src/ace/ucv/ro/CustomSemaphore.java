package ace.ucv.ro;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CustomSemaphore {

    private Integer nrPermits;
    private Lock lock;
    private Condition zeroPermits;

    public CustomSemaphore(Integer nrPermits, Boolean fair) {
        this.nrPermits = nrPermits;
        this.lock = new ReentrantLock(fair);
        this.zeroPermits = lock.newCondition();
    }

    public void acquire() {
        lock.lock();
        while (nrPermits == 0) {
            try {
                zeroPermits.wait();
            } catch (InterruptedException ie) {
                System.out.println(ie.getMessage());
            }
        }
        nrPermits--;
        lock.unlock();
    }

    public void release() {
        lock.lock();
        nrPermits++;
        zeroPermits.notify();
        lock.unlock();
    }
}
