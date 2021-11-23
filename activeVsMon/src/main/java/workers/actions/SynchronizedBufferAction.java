package workers.actions;

import my.concurrent.concurrentBuffer.Buffer;
import my.concurrent.concurrentBuffer.BufferThreeLocks;
import workers.WorkersCalculations;

public class SynchronizedBufferAction extends ResourceActionsAbstraction {


    private final Buffer buffer;

    public SynchronizedBufferAction(int bufferSize, int lowerBound, int upperBound, int optNumbCoeff, int complReqOptNumbCoeff) {
        super(lowerBound,  upperBound, optNumbCoeff, complReqOptNumbCoeff);

        this.buffer = new BufferThreeLocks(bufferSize);
    }

    @Override
    public void produceAction(int index) {
        buffer.produce(genRandData(), index);
        WorkersCalculations.operations(optNumbCoeff/10 + 1);
    }

    @Override
    public void consumeAction(int index) {
            int[] data = buffer.consume(getRandSize(), index);
            WorkersCalculations.operations(optNumbCoeff);
            WorkersCalculations.operationsRequiringData(complReqOptNumbCoeff, data);

    }


}
