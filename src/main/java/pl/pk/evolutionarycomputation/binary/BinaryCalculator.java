package pl.pk.evolutionarycomputation.binary;

public class BinaryCalculator {

    private BinaryCalculator() {
    }

    public static int getLengthOfChromosome(double minimumValue, double maximumValue) {
        double lengthOfChromosome = log2((maximumValue - minimumValue) * Math.pow(10, 6)) + log2(1);
        return (int) Math.ceil(lengthOfChromosome);
    }

    private static double log2(double logNumber) {
        return Math.log(logNumber) / Math.log(2);
    }

    public static double decodeBinaryNumber(double minimumValue, double maximumValue, double lengthOfChromosome, double decimal) {
        double x = minimumValue + decimal * (maximumValue - minimumValue) / (Math.pow(2, lengthOfChromosome) - 1);
        return Math.round(x * Math.pow(10, 5)) / Math.pow(10, 5);

    }

    public static double getDecimal(String binaryNumber) {
        return Integer.parseInt(binaryNumber, 2);
    }

    public static double getDecimal(byte[] binaryNumber) {

        StringBuilder sb = new StringBuilder();

        for (byte b : binaryNumber) {
            if (b == 1)
                sb.append("1");
            else sb.append("0");
        }

        return getDecimal(sb.toString());
    }
}
