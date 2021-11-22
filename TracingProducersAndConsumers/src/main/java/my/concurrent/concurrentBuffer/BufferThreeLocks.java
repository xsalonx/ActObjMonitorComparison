package my.concurrent.concurrentBuffer;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BufferThreeLocks extends Buffer {

    final ReentrantLock lockOfProducers = new ReentrantLock(true);
    final ReentrantLock lockOfConsumers = new ReentrantLock(true);

    final Condition finalAccessCondition = lock.newCondition();


    public BufferThreeLocks(int size) {
        super(size);
    }

    @Override
    public void produce(int[] data) {
        lockOfProducers.lock();

        int size = data.length;
        try {
            lock.lock();
            while (cannotPut(size))
                finalAccessCondition.await();

            putData(data);
            finalAccessCondition.signal();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
            lockOfProducers.unlock();
        }
    }

    @Override
    public int[] consume(int size) {
        lockOfConsumers.lock();

        int[] ret = new int[size];
        try {

            lock.lock();
            while (cannotTake(size))
                finalAccessCondition.await();

            takeData(ret);
            finalAccessCondition.signal();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
            lockOfConsumers.unlock();
        }

        return ret;
    }

    @Override
    public void produce(int[] data, int index) {
        lockOfProducers.lock();

        tracer.logProducerAccessingLock(index);
        int size = data.length;
        try {

            lock.lock();
            tracer.logProducerAccessingLock(index);
            while (cannotPut(size)) {
                finalAccessCondition.await();
                tracer.logProducerAccessingLock(index);
            }

            putData(data);
            tracer.logProducerCompletingTask(index);

            finalAccessCondition.signal();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
            lockOfProducers.unlock();
        }
    }

    @Override
    public int[] consume(int size, int index) {
        lockOfConsumers.lock();

        tracer.logConsumerAccessingLock(index);

        int[] ret = new int[size];
        try {

            lock.lock();
            tracer.logConsumerAccessingLock(index);
            while (cannotTake(size)) {
                finalAccessCondition.await();
                tracer.logConsumerAccessingLock(index);
            }

            takeData(ret);
            tracer.logConsumerCompletingTask(index);

            finalAccessCondition.signal();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
            lockOfConsumers.unlock();
        }

        return ret;
    }
}
