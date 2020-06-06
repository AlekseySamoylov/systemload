package com.alekseysamoylov.systemload

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api")
class MainController(
    private val memoryConsumer: MemoryConsumer,
    private val cpuConsumer: CpuConsumer,
    private val diskConsumer: DiskConsumer,
    private val networkConsumer: NetworkConsumer
) {
  private val log = LoggerFactory.getLogger(this::class.java)

  @PostMapping("/")
  fun setLoad(@RequestBody loadConfiguration: LoadConfiguration) {
    log.info("LoadConfiguration={}", loadConfiguration)
    memoryConsumer.consume(loadConfiguration.memory)
    cpuConsumer.consume(loadConfiguration.cpu)
    diskConsumer.consume(loadConfiguration.disk)
    networkConsumer.consume(loadConfiguration.network, loadConfiguration.ipAddress, loadConfiguration.port)
  }
}
