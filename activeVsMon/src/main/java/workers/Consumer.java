package workers;

import workers.actions.ResourceActionsAbstraction;

public class Consumer extends Worker {

    private final ResourceActionsAbstraction resourceActionsAbstraction;

    public Consumer(int index, ResourceActionsAbstraction resourceActionsAbstraction) {
        super(index);
        this.resourceActionsAbstraction = resourceActionsAbstraction;
    }

    @Override
    public void run() {
        float start, end;

        while (!pseudoCond.end) {
            if (!pseudoCond.stop) {

                start = System.nanoTime();

                resourceActionsAbstraction.consumeAction(index);

                end = System.nanoTime();
                timeMeter.logConsumerTime(index, end - start);

            } else {
                pseudoCond.wait_();
            }
        }
    }


}
