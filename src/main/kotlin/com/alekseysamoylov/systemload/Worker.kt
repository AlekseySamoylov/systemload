package com.alekseysamoylov.systemload


class Worker(private val work: () -> Unit): Runnable {
  @Volatile
  private var continueRun = true
  override fun run() {
    while (continueRun) {
      work()
    }
  }

  fun stop() {
    continueRun = false
  }
}
