package com.example.fileioproject.model

import kotlinx.serialization.Serializable


@Serializable
data class Person(
    val id: Int,
    val name: String
)