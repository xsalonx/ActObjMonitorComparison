package workers;

import pseudoCond.PseudoCond;
import timeMeasure.TimeMeter;

public abstract class Worker implements Runnable {

    static private int dataBound = 10;
    static final PseudoCond pseudoCond = new PseudoCond();

    final int index;

    TimeMeter timeMeter;

    Worker(int index) {
        this.index = index;
    }

    @Override
    abstract public void run();

    public static PseudoCond getPseudoCond() {
        return pseudoCond;
    }

    public void setTimeMeter(TimeMeter timeMeter) {
        this.timeMeter = timeMeter;
    }
}
