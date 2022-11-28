package pl.pk.evolutionarycomputation.util;

public final class Commons {

  private Commons() {
  }

  public static boolean isRangeIncorrect(double value, int minimumValue, int maximumValue) {
    return value < minimumValue || value > maximumValue;
  }
}
