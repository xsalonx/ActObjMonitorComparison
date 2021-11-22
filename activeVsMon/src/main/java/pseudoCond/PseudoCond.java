package pseudoCond;

public class PseudoCond {
    public boolean end = false;
    public boolean stop = false;

    public PseudoCond() {}

    synchronized public void signallAll_() {
        notifyAll();
    }

    synchronized public void wait_() {
        try {
            wait();
        } catch (InterruptedException ignore) {}
    }

}
