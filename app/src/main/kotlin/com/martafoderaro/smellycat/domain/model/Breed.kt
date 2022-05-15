package com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.model

data class Breed(
    val id: String,
    val name: String,
    val origin: String,
    val description: String,
    val temperament: String,
    val wikiUrl: String?,
)