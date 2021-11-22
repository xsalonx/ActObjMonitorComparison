package my.concurrent.rest;

import my.concurrent.methodRequests.MethodRequest;
import my.concurrent.methodRequests.PutDataMethodRequest;
import my.concurrent.methodRequests.TakeDataMethodRequest;


public class Proxy {
    private final Scheduler scheduler;
    private final Servant servant;

    public enum reqTypes {
        PUT, TAKE
    };

    public Proxy(Servant servant, Scheduler scheduler) {
        this.scheduler = scheduler;
        this.servant = servant;
    }

    public Future<int[]> putData(int[] data, int index) {
        Future<int[]> future = new Future<>();
        MethodRequest mr = new PutDataMethodRequest(index, future, servant, data);
        scheduler.enqueue(mr);
        return future;
    }

    public Future<int[]> takeData(int size, int index) {
        Future<int[]> future = new Future<>();
        MethodRequest mr = new TakeDataMethodRequest(index, future, servant, size);
        scheduler.enqueue(mr);
        return future;
    }

}
