package com.example.fileioproject.persention

import com.example.fileioproject.model.Person

data class MainState(
    val isSaving: Boolean = false,
    val isLoading: Boolean = false,
    val items: List<Person> = emptyList()
)
