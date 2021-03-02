package performance.initial

import asserts.BenchmarkAsserts.Companion.assertOutputs
import asserts.BenchmarkAsserts.Companion.assertOutputsCold
import asserts.BenchmarkAsserts.Companion.assertOutputsHot
import benchmarks.initial.AverageTimeBenchmark
import benchmarks.initial.HotColdBenchmark
import benchmarks.initial.SingleShotTimeBenchmark
import org.junit.jupiter.api.Test
import org.openjdk.jmh.runner.Runner
import org.openjdk.jmh.runner.RunnerException
import org.openjdk.jmh.runner.options.OptionsBuilder
import org.openjdk.jmh.runner.options.VerboseMode

open class BenchmarkTests {

    companion object {
        const val EXPECTED_TIME_SINGLE_SHOT = 17.0
        const val EXPECTED_AVERAGE_TIME = 15.0
        const val EXPECTED_AVERAGE_TIME_COLD_MODE = 13.5
        const val EXPECTED_AVERAGE_TIME_HOT_MODE = 16.0
    }

    @Test
    @Throws(RunnerException::class)
    fun testSingleTimeNoWarmUp() {
        val opt = OptionsBuilder()
                .include(SingleShotTimeBenchmark::class.java.canonicalName)
                .jvmArgs("-Xms2G", "-Xmx2G")
                .forks(5)
                .measurementIterations(1)
                .warmupIterations(0)
                .verbosity(VerboseMode.EXTRA)
                .shouldFailOnError(true)
                .build()
        val results = Runner(opt).run()
        assertOutputs(results, EXPECTED_TIME_SINGLE_SHOT)
    }

    @Test
    @Throws(RunnerException::class)
    fun testAverageTime() {
        val opt = OptionsBuilder()
                .include(AverageTimeBenchmark::class.java.canonicalName)
                .jvmArgs("-Xms3G", "-Xmx3G")
                .shouldFailOnError(true)
                .build()
        val results = Runner(opt).run()
        assertOutputs(results, EXPECTED_AVERAGE_TIME)
    }

    @Test
    @Throws(RunnerException::class)
    fun test15TimesSingleShot() {
        val opt = OptionsBuilder()
                .include(SingleShotTimeBenchmark::class.java.canonicalName)
                .jvmArgs("-Xms2G", "-Xmx2G")
                .forks(15)
                .warmupIterations(3)
                .verbosity(VerboseMode.EXTRA)
                .shouldFailOnError(true)
                .build()
        val results = Runner(opt).run()
        assertOutputs(results, EXPECTED_TIME_SINGLE_SHOT)
    }

    @Test
    @Throws(RunnerException::class)
    fun testHotMode() {
        val opt = OptionsBuilder()
                .include(HotColdBenchmark::class.java.canonicalName + ".measureHot")
                .jvmArgs("-Xms2G", "-Xmx2G")
                .forks(3)
                .syncIterations(false)
                .shouldFailOnError(true)
                .build()
        val results = Runner(opt).run()
        assertOutputsHot(results, EXPECTED_AVERAGE_TIME_HOT_MODE)
    }

    @Test
    @Throws(RunnerException::class)
    fun testColdMode() {
        val opt = OptionsBuilder()
                .include(HotColdBenchmark::class.java.canonicalName + ".measureCold")
                .jvmArgs("-Xms2G", "-Xmx2G")
                .syncIterations(false)
                .shouldFailOnError(true)
                .build()
        val results = Runner(opt).run()
        assertOutputsCold(results, EXPECTED_AVERAGE_TIME_COLD_MODE)
    }

    @Test
    @Throws(RunnerException::class)
    fun testMemoryConsumptionLessThen1G() {
        val opt = OptionsBuilder()
                .include(SingleShotTimeBenchmark::class.java.canonicalName)
                .jvmArgs("-Xms1G", "-Xmx1G")
                .shouldDoGC(true)
                .shouldFailOnError(true)
                .build()
        Thread.sleep(5000)
        val results = Runner(opt).run()
        assertOutputs(results, EXPECTED_TIME_SINGLE_SHOT)
    }
}
