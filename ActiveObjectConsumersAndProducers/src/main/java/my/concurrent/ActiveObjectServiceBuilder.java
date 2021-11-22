package my.concurrent;

import my.concurrent.rest.ActivationQueue;
import my.concurrent.rest.Proxy;
import my.concurrent.rest.Scheduler;
import my.concurrent.rest.Servant;

public class ActiveObjectServiceBuilder {

    private final Servant servant;
    private final Scheduler scheduler;
    private final Proxy proxy;
    private final ActivationQueue activationQueue;

    public ActiveObjectServiceBuilder(int bufferSize) {
        servant = new Servant(bufferSize);
        scheduler = new Scheduler();
        proxy = new Proxy(servant, scheduler);
        activationQueue = scheduler.getTasksQueue();
    }

    public Proxy getProxy() {
        return proxy;
    }
}
