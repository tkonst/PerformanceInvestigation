package optimized;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PrimeCalculatorOptimized {
    public static final int maxPrime = 200_000;
    private static final List<Integer> numbersUpToMax;

    static {
        numbersUpToMax = IntStream.range(2, maxPrime).boxed().collect(Collectors.toList());
    }

    public static void main(String[] args) {
        for (Integer prime : getPrimes()) {
            System.out.print(prime + "\n");
        }
    }

    public static List<Integer> getPrimes() {
        return numbersUpToMax.parallelStream().filter(PrimeCalculatorOptimized::isPrime).collect(Collectors.toList());
    }

    public static boolean isPrime(Integer candidate) {
        for (int i : numbersUpToMax.subList(0, candidate - 2)) {
            if (candidate % i == 0) {
                return false;
            }
        }
        return true;
    }
}
