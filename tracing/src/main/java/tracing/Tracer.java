package tracing;

import java.util.Arrays;
import java.util.stream.IntStream;


public class Tracer extends MeasuresFormatter {

    public final int[] producersAccessingLockTimes;
    public final int[] producersCompletingTaskTimes;

    public final int[] consumersAccessingLockTimes;
    public final int[] consumersCompletingTaskTimes;


    public Tracer(int producersNumb, int consumersNumb) {
        producersAccessingLockTimes = new int[producersNumb];
        producersCompletingTaskTimes = new int[producersNumb];


        consumersAccessingLockTimes = new int[consumersNumb];
        consumersCompletingTaskTimes = new int[consumersNumb];
    }

    public void logProducerAccessingLock(int index) {
        producersAccessingLockTimes[index] ++;
    }
    public void logProducerCompletingTask(int index) {
        producersCompletingTaskTimes[index] ++;
    }

    public void logConsumerAccessingLock(int index) {
        consumersAccessingLockTimes[index] ++;
    }
    public void logConsumerCompletingTask(int index) {
        consumersCompletingTaskTimes[index] ++;
    }


//    private int calcCellWidth() {
//        int max1 = Arrays.stream(producersAccessingLockTimes).max().getAsInt();
//        int max2 = Arrays.stream(producersCompletingTaskTimes).max().getAsInt();
//
//        int max3 = Arrays.stream(consumersAccessingLockTimes).max().getAsInt();
//        int max4 = Arrays.stream(consumersCompletingTaskTimes).max().getAsInt();
//
//        int max = Math.max(Math.max(max1, max2), Math.max(max3, max4));
//        int cellWidth = digitNumb(max);
//
//        return cellWidth;
//    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        int n = Math.max(producersAccessingLockTimes.length, consumersAccessingLockTimes.length);
        String indexesLine = getOnLineOfToString(IntStream.rangeClosed(0, n-1).toArray(), "worker index:");
        stringBuilder.append(indexesLine).append('\n');


        stringBuilder
                .append(toStringAccessAndCompletionData(producersAccessingLockTimes, producersCompletingTaskTimes, "producer")).append("\n")
                .append(toStringAccessAndCompletionData(consumersAccessingLockTimes, consumersCompletingTaskTimes, "consumer")).append('\n')
                .append(getTopDownBorder());

        return stringBuilder.toString();
    }


    String toStringAccessAndCompletionData(int[] locksAccessTime, int[] completion, String role) {
        return '\n'  +
                getOnLineOfToString(locksAccessTime, role + " lock accessing times") +
                '\n' +
                getOnLineOfToString(completion, role + " completed tasks") +
                '\n' +
                (getOnLineOfToString(
                        getRatio(completion, locksAccessTime), "ratios")) +
                '\n';
    }

    String getTopDownBorder() {
        return  "_".repeat(linesLength) +
                '\n';
    }

    private float[] getRatio(int[] nominators, int[] denominators) {
        assert nominators.length == denominators.length;
        float[] ratios = new float[nominators.length];
        for (int i =0; i<nominators.length; i++) {
            ratios[i] = (float) nominators[i] / (float) denominators[i];
        }
        return ratios;
    }

}
