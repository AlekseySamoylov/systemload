package com.alekseysamoylov.systemload

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.DataInputStream
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.TimeUnit
import javax.annotation.PreDestroy
import kotlin.random.Random


@Component
class NetworkConsumer {

  private val log = LoggerFactory.getLogger(this::class.java)
  private val workers = mutableListOf<Worker>()
  private var serverSocketWorker: Worker? = null
  private var serverSocket: ServerSocket? = null
  fun consume(byteArraySizeToWrite: Int, ipAddress: String, port: Int) {
    stopWorkers()
    serverSocket?.close()
    serverSocket = ServerSocket(port)
    val ssocket: Socket = serverSocket!!.accept()
    serverSocketWorker = Worker {
      try {
        val dis = DataInputStream(ssocket.getInputStream())
        val bytes = dis.readAllBytes()
        log.info("Bytes read {}", bytes.size)
      } catch (ex: Exception) {
        log.warn("Network err", ex)
      }
    }
    Thread(serverSocketWorker).start()
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
    workers.clear()
    serverSocketWorker?.stop()
    serverSocketWorker = null
  }

  fun size(): Int = workers.size

  private fun stopWorkers() {
    workers.forEach { it.stop() }
  }
}
