package asserts

import org.junit.jupiter.api.Assertions
import org.openjdk.jmh.annotations.Mode.*
import org.openjdk.jmh.results.RunResult

object BenchmarkAsserts {

    fun assertOutputs(results: Collection<RunResult>, expValue: Double) {
        results.forEach {
            it.benchmarkResults.forEach {
                val mode = it.params.mode
                val score = it.primaryResult.score
                val methodName = it.primaryResult.label
                if (mode == SampleTime || mode == AverageTime || mode == SingleShotTime) {
                    Assertions.assertTrue(score < expValue,
                            "Benchmark $methodName score = $score is higher than $expValue ${it.scoreUnit}. Too low performance!")
                }
            }
        }
    }
}
