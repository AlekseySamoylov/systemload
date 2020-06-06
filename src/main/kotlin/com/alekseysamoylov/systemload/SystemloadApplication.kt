package com.alekseysamoylov.systemload

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SystemloadApplication

fun main(args: Array<String>) {
  runApplication<SystemloadApplication>(*args)
}
