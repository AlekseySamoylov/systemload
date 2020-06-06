package com.alekseysamoylov.systemload

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class MemoryConsumer {
  private val log = LoggerFactory.getLogger(this::class.java)
  private val list = mutableListOf<ByteArray>()
  private val entrySizeBytes = 500

  fun consume(amout: Int) {
    list.clear()
    repeat(amout) {
      list.add(ByteArray(entrySizeBytes))
    }
    log.info("List bytes stored in memory size={}", list.size)
  }

  fun size(): Int = list.size
}
