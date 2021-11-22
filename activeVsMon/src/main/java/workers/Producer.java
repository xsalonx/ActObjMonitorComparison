package workers;

import workers.actions.ResourceActionsAbstraction;

public class Producer extends Worker {

    private final ResourceActionsAbstraction resourceActionsAbstraction;

    public Producer(int index, ResourceActionsAbstraction resourceActionsAbstraction) {
        super(index);
        this.resourceActionsAbstraction = resourceActionsAbstraction;
    }


    @Override
    public void run() {
        float start, end;

        while (!pseudoCond.end) {
            if (!pseudoCond.stop) {

                start = System.nanoTime();

                resourceActionsAbstraction.produceAction(index);

                end = System.nanoTime();
                timeMeter.logProducerTime(index, end - start);

            } else {
                pseudoCond.wait_();
            }
        }
    }

}
