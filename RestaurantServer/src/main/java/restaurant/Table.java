package restaurant;

import java.util.concurrent.locks.ReentrantLock;

public class Table {
    ReentrantLock tableLock = new ReentrantLock();
    private boolean isOpen = false;

    public boolean isOpen() {return this.isOpen;}

    public boolean open() {
        if (this.isOpen) {
            return false;
        } else if (tableLock.isLocked()) {
            return false;
        } else {
            tableLock.lock();
            try {
                this.isOpen = true;
            } finally {
                tableLock.unlock();
            }
            return true;
        }
    }

    public void close() {this.isOpen = false;}
}
