package com.martafoderaro.smellycat.com.martafoderaro.smellycat.ui.main

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.martafoderaro.smellycat.base.UiEvent
import com.martafoderaro.smellycat.base.UiState
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.model.Breed
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.model.BreedImage

@Immutable
sealed class MainScreenUiEvent : UiEvent {
    data class ShowBreeds(val breeds: List<Breed>): MainScreenUiEvent()
    data class ShowBreedImages(val images: List<BreedImage>): MainScreenUiEvent()
    data class UpdateSelectedBreed(val breed: Breed): MainScreenUiEvent()
    data class ShowError(val message: String): MainScreenUiEvent()
    data class UpdateConnectionIndicator(val connected: Boolean): MainScreenUiEvent()
}

@Immutable
data class MainScreenState(
    val isLoading: Boolean,
    val selectedBreed: Breed?,
    val breeds: List<Breed>,
    val images: List<BreedImage>,
    val connectionIndicatorColor: Color,
    val errorMessage: String?,
): UiState {
    companion object {
        fun initial() = MainScreenState(
            isLoading = true,
            selectedBreed = null,
            breeds = emptyList(),
            images = emptyList(),
            connectionIndicatorColor = Color.Red,
            errorMessage  = null
        )
    }

    override fun toString(): String {
        return "MainScreenState(isLoading=$isLoading, breeds=$breeds, images=$images, connectionIndicatorColor=$connectionIndicatorColor, errorMessage=$errorMessage)"
    }


}