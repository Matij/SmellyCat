package com.martafoderaro.smellycat.com.martafoderaro.smellycat.ui.main

import androidx.compose.runtime.Immutable
import com.martafoderaro.smellycat.base.UiEvent
import com.martafoderaro.smellycat.base.UiState
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.model.Breed
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.model.BreedImage

@Immutable
sealed class MainScreenUiEvent : UiEvent {
    data class ShowBreeds(val breeds: List<Breed>): MainScreenUiEvent()
    data class ShowBreedImages(val images: List<BreedImage>): MainScreenUiEvent()
    data class ShowError(val message: String): MainScreenUiEvent()
}

@Immutable
data class MainScreenState(
    val isLoading: Boolean,
    val breeds: List<Breed>,
    val images: List<BreedImage>,
    val errorMessage: String? = null,
): UiState {
    companion object {
        fun initial() = MainScreenState(
            isLoading = true,
            breeds = emptyList(),
            images = emptyList(),
        )
    }

    override fun toString(): String {
        return "MainScreenState(isLoading=$isLoading, breeds=$breeds, images=$images, errorMessage=$errorMessage)"
    }


}