package com.alekseysamoylov.systemload

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit
import javax.annotation.PreDestroy
import kotlin.math.tan

@Component
class CpuConsumer {
  private val log = LoggerFactory.getLogger(this::class.java)
  private val workers = mutableListOf<Worker>()

  fun consume(workersNumber: Int) {
    stopWorkers()
    workers.clear()
    repeat(workersNumber) {
      val worker = Worker {
        tan(123456789.123456789)
        TimeUnit.MILLISECONDS.sleep(100)
      }
      workers.add(worker)
      Thread(worker).start()
      log.info("Workers added")
    }
  }

  @PreDestroy
  fun teardown() {
    stopWorkers()
  }

  fun size(): Int = workers.size

  private fun stopWorkers() {
    workers.forEach { it.stop() }
  }
}
