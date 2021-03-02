package benchmarks.optimized

import optimized.PrimeCalculatorOptimized
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole

@Warmup(iterations = 1)
@Measurement(iterations = 3)
@BenchmarkMode(Mode.AverageTime)
open class AverageTimeBenchmarkNew {

    @Benchmark
    fun measureAverage(bh: Blackhole) {
        bh.consume(PrimeCalculatorOptimized.getPrimes())
    }

}
