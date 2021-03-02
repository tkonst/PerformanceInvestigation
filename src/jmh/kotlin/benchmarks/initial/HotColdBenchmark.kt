package benchmarks.initial

import initial.PrimeCalculator
import org.openjdk.jmh.annotations.*
import java.util.concurrent.*

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 2)
@Measurement(iterations = 5)
open class HotColdBenchmark {

    @State(Scope.Benchmark)
    open class NormalState {
        lateinit var service: ExecutorService
        @Setup(Level.Trial)
        fun up() {
            service = Executors.newCachedThreadPool()
        }

        @TearDown(Level.Trial)
        fun down() {
            service.shutdown()
        }
    }

    @State(Scope.Benchmark)
    open class LaggingState : NormalState() {
        @Setup(Level.Invocation)
        @Throws(InterruptedException::class)
        fun lag() {
            TimeUnit.MILLISECONDS.sleep(SLEEP_TIME.toLong())
        }

        companion object {
            val SLEEP_TIME = 1000
        }
    }

    @Benchmark
    @Throws(ExecutionException::class, InterruptedException::class)
    fun measureHot(e: NormalState, calculator: Proxy): List<Int> {
        return e.service.submit(Task(calculator)).get()
    }

    @Benchmark
    @Throws(ExecutionException::class, InterruptedException::class)
    fun measureCold(e: LaggingState, calculator: Proxy): List<Int> {
        return e.service.submit(Task(calculator)).get()
    }

    @State(Scope.Thread)
    open class Proxy {
        fun doWork(): List<Int>{
            return PrimeCalculator.getPrimes()
        }
    }

    class Task(private val instance: Proxy) : Callable<List<Int>> {
        override fun call(): List<Int> {
            return instance.doWork()
        }
    }
}
