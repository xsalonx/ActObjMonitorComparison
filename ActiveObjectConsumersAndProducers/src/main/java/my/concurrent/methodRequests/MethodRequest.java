package my.concurrent.methodRequests;

import tracing.Tracer;

public abstract class MethodRequest {
    int index;
    static Tracer tracer = null;
    MethodRequest(int index) {
        this.index = index;
    }

    static public void setTracer(Tracer tracer) {
        MethodRequest.tracer = tracer;
    }

    public abstract boolean guard();
    public abstract void execute();
    public abstract String getType();
}
