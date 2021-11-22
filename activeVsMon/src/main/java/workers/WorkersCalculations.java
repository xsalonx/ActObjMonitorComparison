package workers;

import java.util.Arrays;
import java.util.Random;

public class WorkersCalculations {

    static private final Random random = new Random();
    static private int coeffFromN(int n) {
        return random.nextInt(n/2) + n/2;
    }

    static public void operationsRequiringData(int n, int[] fGet_) {
        double[] fGet = new double[fGet_.length];
        Arrays.setAll(fGet, (i) -> (double) fGet_[i]);

        double[] tmp = new double[fGet.length];

        for (int i=0; i<coeffFromN(n); i++) {
            for (int j=0; j<fGet.length; j++)
                tmp[j] = fGet[j] + Math.sin(tmp[j] + Math.exp(tmp[j]));
        }
    }
    static public void operations(int n) {
        double tmp = 0;
        for (int i=0; i<coeffFromN(n); i++) {
            tmp = Math.sin(tmp + Math.exp(tmp));
        }
    }
}
