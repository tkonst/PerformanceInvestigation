package benchmarks.initial

import initial.PrimeCalculator
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole

@Warmup(iterations = 2)
@Measurement(iterations = 2)
@BenchmarkMode(Mode.SingleShotTime)
@Fork(value = 1)
open class SingleShotTimeBenchmark {

    @Throws(InterruptedException::class)
    @Benchmark
    fun measureSingleShot(bh: Blackhole) {
        bh.consume(PrimeCalculator.getPrimes())
    }
}
