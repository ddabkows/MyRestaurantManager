package restaurant;

public class Table {
    private boolean isOpen = true;

    public boolean isOpen() {return this.isOpen;}

    public void open() {this.isOpen = true;}

    public void close() {this.isOpen = false;}
}
