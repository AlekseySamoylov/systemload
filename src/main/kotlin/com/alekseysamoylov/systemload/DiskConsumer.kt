package com.alekseysamoylov.systemload

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.TimeUnit
import javax.annotation.PreDestroy
import kotlin.random.Random

@Component
class DiskConsumer {
  private val log = LoggerFactory.getLogger(this::class.java)

  private val workers = mutableListOf<Worker>()
  fun consume(byteArraySizeToWrite: Int) {
    stopWorkers()
    workers.clear()
    val arrayToWriteToFile = Random.nextBytes(ByteArray(byteArraySizeToWrite))
    val worker = Worker {
      try {
      val file = File("tempFile")
      FileOutputStream(file).use { os ->
        os.write(arrayToWriteToFile)
        TimeUnit.MILLISECONDS.sleep(500)
        log.info("Write finished {}", file.exists())
      }
      val fileContent = Files.readAllBytes(Path.of(file.toURI()))
      log.info("Read finished {}", fileContent.size)
      file.delete()
      } catch (ex: Exception) {
        log.warn("Disk consumer error", ex)
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
