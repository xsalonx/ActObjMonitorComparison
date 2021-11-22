package my.concurrent.concurrentBuffer;
import tracing.*;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Buffer {

    final int[] buffer;

    int currentSize = 0;
    int putIndex = 0;
    int takeIndex = 0;

    private static int actionCostCoeff = 0;
    private void addCost(int size) {
        int tmp = 0;
        for (int i=0; i<size * actionCostCoeff; i++) {
            tmp += 1;
            tmp %= 10;
        }
    }

    public static void setActionCostCoeff(int actionCostCoeff) {
        Buffer.actionCostCoeff = actionCostCoeff;
    }

    final ReentrantLock lock = new ReentrantLock(true);
    static Tracer tracer;


    Buffer(int size) {
        buffer = new int[size];
    }

    public static Tracer getTracer() {return tracer;}
    public static void setTracer(Tracer tracer) {
        Buffer.tracer = tracer;
    }

    void putData(int[] data) {
        for (int i=0; i<data.length; i++)
            buffer[(putIndex + i) % buffer.length] = data[i];
        putIndex = (putIndex + data.length) % buffer.length;
        currentSize += data.length;
        addCost(data.length);
    }

    void takeData(int[] ret) {
        int size = ret.length;
        for (int i=0; i<size; i++)
            ret[i] = buffer[(takeIndex + i) % buffer.length];
        takeIndex = (takeIndex + size) % buffer.length;
        currentSize -= size;
        addCost(size);
    }

    public String toStringBufferState() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('[');
        if (putIndex < takeIndex || currentSize == buffer.length) {
            stringBuilder.append("+".repeat(putIndex));
            stringBuilder.append(".".repeat(takeIndex - putIndex));
            stringBuilder.append("+".repeat(buffer.length - takeIndex));
        } else {
            stringBuilder.append(".".repeat(takeIndex));
            stringBuilder.append("+".repeat(putIndex - takeIndex));
            stringBuilder.append(".".repeat(buffer.length - putIndex));
        }
        stringBuilder.append(']').append("  current size:").append(currentSize);

        return stringBuilder.toString();
    }

    public void printBufferState() {
        System.out.println(toStringBufferState());
    }
    void printBufferState(int change) {
        System.out.println(toStringBufferState() + " change:" + change);
    }


    // TODO
    boolean cannotPut(int size) {
        return (currentSize + size > buffer.length);
    }
    boolean cannotTake(int size) {
        return (currentSize - size < 0);
    }

    public abstract void produce(int[] data);
    public abstract int[] consume(int size);
    public abstract void produce(int[] data, int index);
    public abstract int[] consume(int size, int index);

}
