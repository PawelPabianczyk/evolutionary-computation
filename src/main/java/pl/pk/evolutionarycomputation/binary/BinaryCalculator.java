package pl.pk.evolutionarycomputation.binary;

public class BinaryCalculator {

    private BinaryCalculator() {
    }

    public static double getLengthOfChromosome(double a, double b) {
        double m = log2((b - a) * Math.pow(10, 6)) + log2(1);
        return Math.ceil(m);
    }

    private static double log2(double logNumber) {
        return Math.log(logNumber) / Math.log(2);
    }

    public static double decodeBinaryNumber(double a, double b, double m, double decimal) {
        double x = a + decimal * (b - a) / (Math.pow(2, m) - 1);
        return Math.round(x * Math.pow(10, 5)) / Math.pow(10, 5);

    }

    public static double getDecimal(String binaryNumber) {
        return Integer.parseInt(binaryNumber, 2);
    }

    public static double getDecimal(byte[] binaryNumber, double m) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < m; i++) {
            if (binaryNumber[i] == 1)
                sb.append("1");
            else sb.append("0");
        }

        return getDecimal(sb.toString());
    }
}
