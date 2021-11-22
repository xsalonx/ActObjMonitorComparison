package my.concurrent.methodRequests;

import my.concurrent.rest.Future;
import my.concurrent.rest.Proxy;
import my.concurrent.rest.Servant;

public class PutDataMethodRequest extends MethodRequest {

    private final int[] data;
    private final Servant servant;
    private final Future<int[]> future;

    public PutDataMethodRequest(int index, Future<int[]> future, Servant servant, int[] data) {
        super(index);
        this.future = future;
        this.servant = servant;
        this.data = data;
    }

    @Override
    public boolean guard() {
        tracer.logProducerAccessingLock(index);
        return data.length <= servant.getLeftSpace();
    }

    @Override
    public void execute() {
        servant.putData(data);
        future.setResult(null);
        tracer.logProducerCompletingTask(index);
    }

    @Override
    public String getType() {
        return Proxy.reqTypes.PUT.name();
    }
}
