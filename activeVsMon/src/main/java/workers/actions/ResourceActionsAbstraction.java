package workers.actions;

import java.util.Random;

abstract public class ResourceActionsAbstraction {

    private final int lowerBound;
    private final int upperBound;
    private final Random random = new Random();
    static final int dataBound = 10;

    final int optNumbCoeff;
    final int complReqOptNumbCoeff;

    public ResourceActionsAbstraction(int lowerBound, int upperBound, int optNumbCoeff, int complReqOptNumbCoeff) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.optNumbCoeff = optNumbCoeff;
        this.complReqOptNumbCoeff = complReqOptNumbCoeff;
    }

    int getRandSize() {
        return random.nextInt(upperBound - lowerBound + 1) + lowerBound;
    }

    int[] genRandData() {
        Random random = new Random();
        int[] data = new int[getRandSize()];
        for (int i = 0; i < data.length; i++) {
            data[i] = random.nextInt(dataBound);
        }
        return data;
    }

    public abstract void produceAction(int index);
    public abstract void consumeAction(int index);
}
