package asserts

import org.junit.jupiter.api.Assertions
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.results.RunResult
import performance.initial.BenchmarkTests

class BenchmarkAsserts {

    companion object {

        fun assertOutputs(results: Collection<RunResult>, expValue: Double) {
            for (r in results) {
                for (rr in r.benchmarkResults) {
                    val mode = rr.params.mode
                    val score = rr.primaryResult.score
                    val methodName = rr.primaryResult.label
                    if (mode == Mode.SampleTime || mode == Mode.AverageTime || mode == Mode.SingleShotTime) {
                        Assertions.assertTrue(score < expValue,
                                "Benchmark " + methodName + " score = " + score + " is higher than " + expValue +
                                        " " + rr.scoreUnit + ". Too slow performance!")
                    }
                }
            }
        }

        fun assertOutputsHot(results: Collection<RunResult>, expValue: Double) {
            for (r in results) {
                for (rr in r.benchmarkResults) {
                    val score = rr.primaryResult.score
                    val methodName = rr.primaryResult.label
                    Assertions.assertTrue(score < expValue,
                            "Benchmark " + methodName + " score = " + score + " is higher than " + expValue +
                                    " " + rr.scoreUnit + ". Too slow performance!")
                }
            }
        }

        fun assertOutputsCold(results: Collection<RunResult>, expValue: Double) {
            for (r in results) {
                for (rr in r.benchmarkResults) {
                    val score = rr.primaryResult.score
                    val methodName = rr.primaryResult.label
                    Assertions.assertTrue(score < expValue,
                            "Benchmark " + methodName + " score = $score is higher than $expValue " +
                                    rr.scoreUnit + ". Too low performance!")
                }
            }
        }
    }
}
