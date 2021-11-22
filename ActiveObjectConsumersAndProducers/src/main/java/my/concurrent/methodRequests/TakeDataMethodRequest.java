package my.concurrent.methodRequests;

import my.concurrent.rest.Future;
import my.concurrent.rest.Proxy;
import my.concurrent.rest.Servant;


public class TakeDataMethodRequest extends MethodRequest {
    private final int size;
    private final Servant servant;
    private final Future<int[]> future;

    public TakeDataMethodRequest(int index, Future<int[]> future, Servant servant, int size) {
        super(index);
        this.future = future;
        this.servant = servant;
        this.size = size;
    }

    @Override
    public boolean guard() {
        tracer.logConsumerAccessingLock(index);
        return servant.getCurrentSize() >= size;
    }

    @Override
    public void execute() {
        future.setResult(servant.takeData(size));
        tracer.logConsumerCompletingTask(index);
    }

    @Override
    public String getType() {
        return Proxy.reqTypes.TAKE.name();
    }
}
