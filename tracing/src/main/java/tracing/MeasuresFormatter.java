package tracing;


public class MeasuresFormatter {

    public static final int leftPadding = 30;
    public static final int spaceBetweenCells = 2;
    public static int linesLength = 250;

    public static int cellWidth = 13;


    public void MeasureFormatter() {}


    static public String getOnLineOfToString(int[] arr, String rowTitle) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(rowTitle).append(" ".repeat(leftPadding - rowTitle.length()));
        for (int a : arr) {
            stringBuilder.append(" ".repeat(cellWidth - digitNumb(a) + spaceBetweenCells));
            stringBuilder.append(a);
        }
        return stringBuilder.toString();
    }
    static public String getOnLineOfToString(float[] arr, String rowTitle) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(rowTitle).append(" ".repeat(leftPadding - rowTitle.length()));
        for (float a : arr) {
            String formattedFloat = String.format("%.02f", a);
            stringBuilder.append(" ".repeat(Math.max(cellWidth - formattedFloat.length() + spaceBetweenCells, 1)));
            stringBuilder.append(formattedFloat);
        }
        return stringBuilder.toString();
    }
    static public int digitNumb(long n) {
        return (int) (Math.floor(Math.log10(Math.max(1, n))) + 1);
    }

}
