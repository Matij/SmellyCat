package com.martafoderaro.smellycat.ui.main

import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.model.BreedImage

object TestBreedImageProvider {
    fun breedImages(breedId: String) = listOf(
        BreedImage(
            id = "imageId1_$breedId",
            url = "imageUrl1_$breedId"
        ),
        BreedImage(
            id = "imageId2_$breedId",
            url = "imageUrl2_$breedId"
        ),
        BreedImage(
            id = "imageId3_$breedId",
            url = "imageUrl3_$breedId"
        )
    )
}