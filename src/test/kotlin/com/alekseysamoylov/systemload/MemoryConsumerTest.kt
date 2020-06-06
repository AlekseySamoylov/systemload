package com.alekseysamoylov.systemload

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Test


internal class MemoryConsumerTest {

  private lateinit var memoryConsumer: MemoryConsumer

  @BeforeEach
  fun setup() {
    memoryConsumer = MemoryConsumer()
  }

  @Test
  fun shouldFillListByValues() {
    // Given
    val amount = 200

    // When
    memoryConsumer.consume(amount)

    // Then
    assertThat(memoryConsumer.size()).isEqualTo(amount)
  }

  @Test
  fun shouldReFillListByValues() {
    // Given
    shouldFillListByValues()
    val amount = 100

    // When
    memoryConsumer.consume(amount)

    // Then
    assertThat(memoryConsumer.size()).isEqualTo(amount)
  }
}
