package com.alekseysamoylov.systemload

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.OutputStream
import java.net.Socket
import java.util.concurrent.TimeUnit
import javax.annotation.PreDestroy
import kotlin.random.Random


@Component
class NetworkConsumer {

  private val log = LoggerFactory.getLogger(this::class.java)
  private val workers = mutableListOf<Worker>()
  fun consume(byteArraySizeToWrite: Int, ipAddress: String, port: Int) {
    stopWorkers()
    workers.clear()
    val arrayToWrite = Random.nextBytes(ByteArray(byteArraySizeToWrite))
    val worker = Worker {
      Socket(ipAddress, port).use { socket ->
        val socketOutputStream = socket.getOutputStream()
        socketOutputStream.write(arrayToWrite)
        TimeUnit.MILLISECONDS.sleep(500)
        log.info("Work cycle finished")
      }
    }
    workers.add(worker)
    Thread(worker).start()
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
