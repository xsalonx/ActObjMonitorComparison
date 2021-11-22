package timeMeasure;

import tracing.MeasuresFormatter;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.IntStream;


public class TimeMeter extends MeasuresFormatter {


    public final ArrayList<Float>[] producersCompletionTimes;
    public final ArrayList<Float>[] consumersCompletionTimes;

    private final Thread[] producersThreads;
    private final Thread[] consumersThreads;

    ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();

    public TimeMeter(Thread[] producersThreads, Thread[] consumersThreads) {
        super();
        int producersNumb = producersThreads.length;
        int consumersNumb = consumersThreads.length;

        producersCompletionTimes = new ArrayList[producersNumb];
        for (int i=0; i<producersNumb; i++) {
            producersCompletionTimes[i] = new ArrayList<>();
        }
        consumersCompletionTimes = new ArrayList[consumersNumb];
        for (int i=0; i<consumersNumb; i++) {
            consumersCompletionTimes[i] = new ArrayList<>();
        }
        this.producersThreads = producersThreads;
        this.consumersThreads = consumersThreads;
    }

    public void logProducerTime(int index, float time) {
        producersCompletionTimes[index].add(time);
    }
    public void logConsumerTime(int index, float time) {
        consumersCompletionTimes[index].add(time);
    }


    public String toStringTimes() {
        StringBuilder stringBuilder = new StringBuilder();

        float[] producersTimes = avgTimePerWorker(producersCompletionTimes);
        float[] consumersTimes = avgTimePerWorker(consumersCompletionTimes);


        int[] producersMeasures = new int[producersTimes.length];
        int[] consumersMeasures = new int[consumersTimes.length];

        for (int i=0; i< producersCompletionTimes.length; i++)
            producersMeasures[i] = producersCompletionTimes[i].size();
        for (int i=0; i< consumersTimes.length; i++)
            consumersMeasures[i] = consumersCompletionTimes[i].size();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        int n = Math.max(producersTimes.length, consumersTimes.length);

        String firstLine = getOnLineOfToString(IntStream.rangeClosed(0, n - 1).toArray(), "worker index");

        stringBuilder
                .append(dtf.format(now)).append('\n')

                .append(firstLine)
                .append("\n\n")

                .append("\u001B[32m").append(getOnLineOfToString(producersTimes, "producers [micro]:")).append("\u001B[0m")
                .append('\n')
                .append("\u001B[34m").append(getRowOfCPUTimes(producersThreads, "cpu t [mega clocks]:")).append("\u001B[0m")
                .append('\n')
                .append(getOnLineOfToString(producersMeasures, "number of meas.:"))
                .append("\n\n")

                .append("\u001B[32m").append(getOnLineOfToString(consumersTimes, "consumers [micro]:")).append("\u001B[0m")
                .append('\n')
                .append("\u001B[34m").append(getRowOfCPUTimes(consumersThreads, "cpu t [mega clocks]:")).append("\u001B[0m")
                .append('\n')
                .append(getOnLineOfToString(consumersMeasures, "number of meas.:"))
                .append('\n')

                .append("-".repeat(linesLength));

        return stringBuilder.toString();
    }

    private float[] avgTimePerWorker(ArrayList<Float>[] arr) {
        int length = arr.length;
        float[] avg = new float[length];
        for (int i=0; i<length; i++) {
            for (Float t : arr[i]) {
                avg[i] += t;
            }
            avg[i] /= arr[i].size();
            avg[i] /= 1000; // nano to micro
        }
        return avg;
    }

    private String getRowOfCPUTimes(Thread[] arr, String rowTitle) {
        float[] cpuT = new float[arr.length];
        for (int i=0; i<arr.length; i++) {
            cpuT[i] = ((float)mxBean.getThreadCpuTime(arr[i].getId())) / 1000000;
        }
        return getOnLineOfToString(cpuT, rowTitle);

    }


}
