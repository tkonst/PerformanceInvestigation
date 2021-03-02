package benchmarks.initial

import initial.PrimeCalculator
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole

@Warmup(iterations = 1)
@Measurement(iterations = 3)
@BenchmarkMode(Mode.AverageTime)
open class AverageTimeBenchmark {

    @Throws(InterruptedException::class)
    @Benchmark
    fun measureAverage(bh: Blackhole) {
        bh.consume(PrimeCalculator.getPrimes())
    }
}
