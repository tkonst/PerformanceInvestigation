package performance.optimized

import asserts.BenchmarkAsserts.assertOutputs
import benchmarks.optimized.AverageTimeBenchmarkNew
import benchmarks.optimized.HotColdBenchmarkNew
import benchmarks.optimized.SingleShotTimeBenchmarkNew
import org.junit.jupiter.api.Test
import org.openjdk.jmh.annotations.Threads
import org.openjdk.jmh.runner.Runner
import org.openjdk.jmh.runner.RunnerException
import org.openjdk.jmh.runner.options.OptionsBuilder

open class BenchmarkTestsNew {

    companion object {
        const val EXPECTED_TIME_SINGLE_SHOT = 1.4
        const val EXPECTED_AVERAGE_TIME = 1.3
        const val EXPECTED_AVERAGE_TIME_COLD_MODE = 1.1
        const val EXPECTED_AVERAGE_TIME_HOT_MODE = 1.3
        const val EXPECTED_AVERAGE_TIME_MAX_THREADS = 13.0
    }

    @Test
    @Throws(RunnerException::class)
    fun testSingleTimeNoWarmUp() {
        val opt = OptionsBuilder()
                .include(SingleShotTimeBenchmarkNew::class.java.canonicalName)
                .jvmArgs("-Xms2G", "-Xmx2G")
                .forks(5)
                .measurementIterations(1)
                .warmupIterations(0)
                .shouldFailOnError(true)
                .build()
        val results = Runner(opt).run()
        assertOutputs(results, EXPECTED_TIME_SINGLE_SHOT)
    }

    @Test
    @Throws(RunnerException::class)
    fun testAverageTime() {
        val opt = OptionsBuilder()
                .include(AverageTimeBenchmarkNew::class.java.canonicalName)
                .jvmArgs("-Xms2G", "-Xmx2G")
                .shouldFailOnError(true)
                .build()
        val results = Runner(opt).run()
        assertOutputs(results, EXPECTED_AVERAGE_TIME)
    }

    @Test
    @Throws(RunnerException::class)
    fun testSingleShotMaxThreads() {
        val opt = OptionsBuilder()
                .include(SingleShotTimeBenchmarkNew::class.java.canonicalName)
                .jvmArgs("-Xms2G", "-Xmx2G")
                .forks(7)
                .threads(Threads.MAX)
                .measurementIterations(5)
                .warmupIterations(3)
                .shouldFailOnError(true)
                .build()
        val results = Runner(opt).run()
        assertOutputs(results, EXPECTED_AVERAGE_TIME_MAX_THREADS)
    }

    @Test
    @Throws(RunnerException::class)
    fun testHotMode() {
        val opt = OptionsBuilder()
                .include(HotColdBenchmarkNew::class.java.canonicalName + ".measureHot")
                .jvmArgs("-Xms2G", "-Xmx2G")
                .syncIterations(false)
                .shouldFailOnError(true)
                .build()
        val results = Runner(opt).run()
        assertOutputs(results, EXPECTED_AVERAGE_TIME_HOT_MODE)
    }

    @Test
    @Throws(RunnerException::class)
    fun testColdMode() {
        val opt = OptionsBuilder()
                .include(HotColdBenchmarkNew::class.java.canonicalName + ".measureCold")
                .jvmArgs("-Xms2G", "-Xmx2G")
                .forks(3)
                .syncIterations(false)
                .shouldFailOnError(true)
                .build()
        val results = Runner(opt).run()
        assertOutputs(results, EXPECTED_AVERAGE_TIME_COLD_MODE)
    }

    @Test
    @Throws(RunnerException::class)
    fun testMemoryConsumptionLessThen1G() {
        val opt = OptionsBuilder()
                .include(SingleShotTimeBenchmarkNew::class.java.canonicalName)
                .jvmArgs("-Xms1G", "-Xmx1G")
                .shouldDoGC(true)
                .shouldFailOnError(true)
                .build()
        val results = Runner(opt).run()
        assertOutputs(results, EXPECTED_TIME_SINGLE_SHOT)
    }
}
