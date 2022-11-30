package com.martafoderaro.smellycat.ui.main

import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.model.Breed

object TestBreedsProvider {
    fun aBreedsList() = listOf(
        Breed(
            id = "id1",
            name = "breed name",
            origin = "origin",
            description = "description",
            temperament = "temperament",
            wikiUrl = "url"
        ),
        Breed(
            id = "id2",
            name = "breed name2",
            origin = "origin2",
            description = "description2",
            temperament = "temperament2",
            wikiUrl = "url2"
        ),
        Breed(
            id = "id3",
            name = "breed name3",
            origin = "origin3",
            description = "description3",
            temperament = "temperament3",
            wikiUrl = "url3"
        )
    )
}