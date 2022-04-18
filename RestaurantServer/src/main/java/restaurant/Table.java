package restaurant;

import java.util.concurrent.locks.ReentrantLock;

public class Table {
    ReentrantLock tableLock = new ReentrantLock();
    private boolean isOpen = false;
    private int peopleCount;

    public boolean isOpen() {return this.isOpen;}

    public boolean open(int peopleCountToSet) {
        if (tableLock.isLocked()) {
            return false;
        } else if (peopleCountToSet == 0 && this.isOpen) {
            return true;
        } else if (peopleCountToSet > 0 && !this.isOpen) {
            tableLock.lock();
            try {
                this.isOpen = true;
                peopleCount = peopleCountToSet;
            } finally {
                tableLock.unlock();
            }
            return true;
        }
        return false;
    }

    public void close() {this.isOpen = false;}
    public int getPeopleCount() {return this.peopleCount;}
    public void setPeopleCount(int peopleCountToSet) {this.peopleCount = peopleCountToSet;}
}
