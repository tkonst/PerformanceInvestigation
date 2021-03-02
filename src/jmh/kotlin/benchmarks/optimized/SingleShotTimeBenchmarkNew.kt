package benchmarks.optimized

import optimized.PrimeCalculatorOptimized
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole

@Warmup(iterations = 2)
@Measurement(iterations = 2)
@BenchmarkMode(Mode.SingleShotTime)
@Fork(value = 1)
open class SingleShotTimeBenchmarkNew {
    @Throws(InterruptedException::class)
    @Benchmark
    fun measureSingleShot(bh: Blackhole) {
        bh.consume(PrimeCalculatorOptimized.getPrimes())
    }
}
