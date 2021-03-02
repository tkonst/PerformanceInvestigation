package benchmarks.optimized

import optimized.PrimeCalculatorOptimized
import org.openjdk.jmh.annotations.*
import java.util.concurrent.*

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 2)
@Measurement(iterations = 5)
open class HotColdBenchmarkNew {
    @State(Scope.Benchmark)
    open class NormalState {
        var service: ExecutorService? = null
        @Setup(Level.Trial)
        fun up() {
            service = Executors.newCachedThreadPool()
        }

        @TearDown(Level.Trial)
        fun down() {
            service!!.shutdown()
        }
    }

    @State(Scope.Benchmark)
    open class LaggingState : NormalState() {
        @Setup(Level.Invocation)
        @Throws(InterruptedException::class)
        fun pauseBetweenIterations() {
            TimeUnit.MILLISECONDS.sleep(SLEEP_TIME.toLong())
        }

        companion object {
            const val SLEEP_TIME = 1000
        }
    }

    @Benchmark
    @Throws(ExecutionException::class, InterruptedException::class)
    fun measureHot(e: NormalState, calculator: Proxy): List<Int> {
        return e.service!!.submit(Task(calculator)).get()
    }

    @Benchmark
    @Throws(ExecutionException::class, InterruptedException::class)
    fun measureCold(e: LaggingState, calculator: Proxy): List<Int> {
        return e.service!!.submit(Task(calculator)).get()
    }

    @State(Scope.Thread)
    open class Proxy {
        fun doWork(): List<Int>{
            return PrimeCalculatorOptimized.getPrimes()
        }
    }

    open class Task : Callable<List<Int>> {
        val instance : Proxy

        constructor(instance: Proxy) {
            this.instance = instance;
        }
        override fun call(): List<Int> {
            return instance.doWork()
        }
    }
}
