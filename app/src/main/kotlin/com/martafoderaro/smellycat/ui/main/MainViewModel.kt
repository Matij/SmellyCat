package com.martafoderaro.smellycat.com.martafoderaro.smellycat.ui.main

import androidx.lifecycle.viewModelScope
import com.martafoderaro.smellycat.base.BaseViewModel
import com.martafoderaro.smellycat.base.Reducer
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.usecase.GetBreedImagesParameters
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.usecase.IGetBreedImagesCase
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.usecase.IGetBreedsUseCase
import com.martafoderaro.smellycat.core.CoroutineDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@FlowPreview
class MainViewModel @Inject constructor(
    private val getBreedsUseCase: IGetBreedsUseCase,
    private val getBreedImagesUseCase: IGetBreedImagesCase,
    private val dispatchers: CoroutineDispatchers,
): BaseViewModel<MainScreenState, MainScreenUiEvent>() {

    enum class ImageOrderType {
        RANDOM, ASC, DESC
    }

    private val reducer = MainReducer(MainScreenState.initial())

    override val state: Flow<MainScreenState>
        get() = reducer.state

    init {
        viewModelScope.launch(dispatchers.io) {
            try {
                val data = getBreedsUseCase.invoke(Unit)
                sendEvent(MainScreenUiEvent.ShowBreeds(breeds = data.last()))
            } catch (e: Throwable) {
                sendEvent(MainScreenUiEvent.ShowError(
                    message = e.message ?: "${MainViewModel::class.qualifiedName}: Unknown error."))
            }
        }
    }

    fun onBreedSelected(breedId: String) {
        getBreedImages(breedId = breedId)
    }

    private fun getBreedImages(breedId: String) = viewModelScope.launch(dispatchers.io) {
        val parameters = GetBreedImagesParameters(breedId = breedId, page = 2, limit = 10, order = ImageOrderType.RANDOM.name)
        try {
            val data = getBreedImagesUseCase.invoke(parameters = parameters)
            sendEvent(MainScreenUiEvent.ShowBreedImages(images = data.last()))
        } catch (e: Throwable) {
            sendEvent(MainScreenUiEvent.ShowError(
                message = e.message ?: "${MainViewModel::class.qualifiedName}: Unknown error."))
        }
    }

    private fun sendEvent(event: MainScreenUiEvent) {
        reducer.sendEvent(event)
    }

    private class MainReducer(initial: MainScreenState) : Reducer<MainScreenState, MainScreenUiEvent>(initial) {
        override fun reduce(oldState: MainScreenState, event: MainScreenUiEvent) {
            when (event) {
                is MainScreenUiEvent.ShowBreeds -> {
                    setState(oldState.copy(isLoading = false, breeds = event.breeds))
                }
                is MainScreenUiEvent.ShowBreedImages -> {
                    setState(oldState.copy(isLoading = false, images = event.images))
                }
                is MainScreenUiEvent.ShowError -> {
                    setState(oldState.copy(isLoading = false, images = emptyList(), errorMessage = event.message))
                }
                else -> {
                    // do nothing
                }
            }
        }
    }
}
