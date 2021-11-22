import my.concurrent.concurrentBuffer.Buffer;
import my.concurrent.methodRequests.MethodRequest;
import my.concurrent.rest.Servant;
import pseudoCond.PseudoCond;
import timeMeasure.TimeMeter;
import tracing.Tracer;
import workers.*;
import workers.actions.ActiveObjectActions;
import workers.actions.ResourceActionsAbstraction;
import workers.actions.SynchronizedBufferAction;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * implementation of producers and consumers problem with two and four conditions with tracing threads' work
 * @author Łukasz Dubiel
 * */


public class Main {

    static private int producersNumb;
    static private int consumersNumb;
    static private int bufferSize;

    static private int lowerBound;
    static private int upperBound;

    static private int alterPoint = Integer.MAX_VALUE;
    static private TimeMeter timeMeter;

    static private ResourceActionsAbstraction resourceActionsAbstraction;

    public static void main(String[] args) {

        System.out.println(Arrays.toString(args));

        producersNumb = Integer.parseInt(args[1]);
        consumersNumb = Integer.parseInt(args[2]);
        bufferSize = Integer.parseInt(args[3]);
        lowerBound = 1;
        upperBound = bufferSize / 2;

        int optNumbCoeff = Integer.parseInt(args[4]);
        int complReqOptNumbCoeff = Integer.parseInt(args[5]);

        int actionCostCoeff = Integer.parseInt(args[6]);
        int secondsOfMeasuring = Integer.parseInt(args[7]);

        Tracer tracer = new Tracer(producersNumb, consumersNumb);

        if (args[0].equals("buffer")) {
            Buffer.setTracer(tracer);
            Buffer.setActionCostCoeff(actionCostCoeff);
            resourceActionsAbstraction = new SynchronizedBufferAction(bufferSize, lowerBound, upperBound, optNumbCoeff, complReqOptNumbCoeff);
        }
        else if (args[0].equals("actObject")) {
            MethodRequest.setTracer(tracer);
            Servant.setActionCostCoeff(actionCostCoeff);
            resourceActionsAbstraction = new ActiveObjectActions(bufferSize, lowerBound, upperBound, optNumbCoeff, complReqOptNumbCoeff);
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Worker[] producers = initProducers();
        Worker[] consumers = initConsumers();


        Thread[] producersThreads = declareWorkersThreads(producers);
        Thread[] consumersThreads = declareWorkersThreads(consumers);

        timeMeter = new TimeMeter(producersThreads, consumersThreads);
        setTimeMeter(producers);
        setTimeMeter(consumers);


        Thread[] threads = Stream.of(producersThreads, consumersThreads).flatMap(Stream::of).toArray(Thread[]::new);
        startThreads(threads);

        PseudoCond pseudoCond = Worker.getPseudoCond();
        sleep(1000 * secondsOfMeasuring);

        pseudoCond.stop = true;
        sleep(100);
        System.out.println(timeMeter.toStringTimes().replaceAll("\u001B\\[[;\\d]*m", ""));
        System.out.println(tracer.toString().replaceAll("\u001B\\[[;\\d]*m", ""));
        System.exit(0);

    }



    private static Worker[] initProducers() {
        Worker[] workers = new Worker[producersNumb];
        for (int i = 0; i < producersNumb; i++)
            workers[i] = new Producer(i, resourceActionsAbstraction);
        return workers;
    }

    private static Worker[] initConsumers() {
        Worker[] workers = new Worker[consumersNumb];
        for (int i = 0; i < consumersNumb; i++)
            workers[i] = new Consumer(i, resourceActionsAbstraction);
        return workers;
    }

    private static void setTimeMeter(Worker[] workers) {
        for (Worker w : workers)
            w.setTimeMeter(timeMeter);
    }

    private static Thread[] declareWorkersThreads(Worker[] workers) {
        Thread[] workersThreads = new Thread[workers.length];
        for (int i = 0; i < workers.length; i++) {
            workersThreads[i] = new Thread(workers[i]);
        }
        return workersThreads;
    }

    private static void startThreads(Thread[] threads) {
        for (Thread th : threads)
            th.start();
    }

    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignore) {
        }
    }

}
