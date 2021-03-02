package functional.optimized

import optimized.PrimeCalculatorOptimized
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class PrimeCalculatorTestNew {

    @ParameterizedTest(name = "#{index} - Test with value: {0}")
    @ValueSource(ints = [2, 13, 199_999])
    fun primesPositiveTest(value: Int) {
        val result = PrimeCalculatorOptimized.getPrimes()
        assertTrue(result.contains(value),
                "$value is a prime, but not found in resulting list")
    }

    @ParameterizedTest
    @ValueSource(ints = [-1, -13, 0, 1, 4, 121, 200_000])
    fun primesNegativeTest(value: Int) {
        val result = PrimeCalculatorOptimized.getPrimes()
        assertFalse(result.contains(value),
                "$value is NOT a prime, but was found in resulting list")
    }

}
