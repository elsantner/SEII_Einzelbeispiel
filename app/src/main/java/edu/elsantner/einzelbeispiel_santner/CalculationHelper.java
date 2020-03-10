package edu.elsantner.einzelbeispiel_santner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public abstract class CalculationHelper {
    private static List<Integer> singleDigitPrimes = Arrays.asList(2, 3, 5, 7);

    public static String sortAndFilterPrimes(String mnr) {
        StringBuilder result = new StringBuilder();
        List<Integer> digits = new ArrayList<>();
        for (char digitChar: mnr.toCharArray()) {
            digits.add(Integer.decode(String.valueOf(digitChar)));
        }
        digits.sort(Comparator.<Integer>reverseOrder());
        for (Integer digit : digits) {
            if (!isPrime(digit)) {
                result.append(digit);
            }
        }
        return result.toString();
    }

    public static boolean isPrime(int number) {
        return singleDigitPrimes.contains(number);
    }
}
