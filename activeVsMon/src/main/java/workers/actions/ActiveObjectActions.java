package workers.actions;

import my.concurrent.ActiveObjectServiceBuilder;
import my.concurrent.rest.Future;
import my.concurrent.rest.Proxy;
import workers.WorkersCalculations;

public class ActiveObjectActions extends ResourceActionsAbstraction {


    private final ActiveObjectServiceBuilder activeObjectServiceBuilder;
    private final Proxy proxy;

    public ActiveObjectActions(int bufferSize, int lowerBound, int upperBound, int optNumbCoeff, int complReqOptNumbCoeff) {
        super(lowerBound,  upperBound, optNumbCoeff, complReqOptNumbCoeff);

        activeObjectServiceBuilder = new ActiveObjectServiceBuilder(bufferSize);
        proxy = activeObjectServiceBuilder.getProxy();

    }

    @Override
    public void produceAction(int index) {
        proxy.putData(genRandData(), index);
        WorkersCalculations.operations(optNumbCoeff/10 + 1);
    }

    @Override
    public void consumeAction(int index) {
        Future<int[]> future = proxy.takeData(getRandSize(), index);
        WorkersCalculations.operations(optNumbCoeff);
        WorkersCalculations.operationsRequiringData(complReqOptNumbCoeff, future.get());
    }

}
