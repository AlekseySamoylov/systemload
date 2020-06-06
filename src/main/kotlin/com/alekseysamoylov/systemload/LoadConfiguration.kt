package com.alekseysamoylov.systemload


data class LoadConfiguration(
    val cpu: Int,
    val memory: Int,
    val disk: Int,
    val network: Int,
    val ipAddress: String,
    val port: Int
)
