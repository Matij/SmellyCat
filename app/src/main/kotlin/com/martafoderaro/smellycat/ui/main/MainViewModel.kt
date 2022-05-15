package com.martafoderaro.smellycat.com.martafoderaro.smellycat.ui.main

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.martafoderaro.smellycat.base.BaseViewModel
import com.martafoderaro.smellycat.base.Reducer
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.usecase.GetBreedImagesParameters
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.usecase.GetBreedParameters
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.usecase.IGetBreedImagesCase
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.usecase.IGetBreedsUseCase
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.usecase.IGetBreedUseCase
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.util.isConnected
import com.martafoderaro.smellycat.core.CoroutineDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@Suppress("DeferredResultUnused")
@HiltViewModel
@FlowPreview
class MainViewModel @Inject constructor(
    private val getBreedsUseCase: IGetBreedsUseCase,
    private val getBreedUseCase: IGetBreedUseCase,
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
        loadBreeds()
        observeConnectionState()
    }

    fun onBreedSelected(breedId: String) {
        loadBreed(breedId)
        getBreedImages(breedId = breedId)
    }

    private fun loadBreed(breedId: String) = viewModelScope.launch(dispatchers.io) {
        try {
            val data = getBreedUseCase.invoke(GetBreedParameters(breedId = breedId)).last()
            sendEvent(MainScreenUiEvent.UpdateSelectedBreed(breed = data))
        } catch (e: Throwable) {
            Timber.e(wrapErrorMessage(e))
            sendEvent(MainScreenUiEvent.ShowError(
                message = e.message ?: "${MainViewModel::class.qualifiedName}: Unknown error."))
        }
    }

    private fun loadBreeds() = viewModelScope.launch(dispatchers.io) {
        try {
            val data = getBreedsUseCase.invoke(Unit).last()
            sendEvent(MainScreenUiEvent.ShowBreeds(breeds = data))
        } catch (e: Throwable) {
            Timber.e(wrapErrorMessage(e))
            sendEvent(MainScreenUiEvent.ShowError(
                message = e.message ?: "${MainViewModel::class.qualifiedName}: Unknown error."))
        }
    }

    private fun observeConnectionState() {
        viewModelScope.launchPeriodicAsync(1000L) {
            val newConnectedState = isConnected()
            sendEvent(MainScreenUiEvent.UpdateConnectionIndicator(newConnectedState))
        }
    }

    private fun getBreedImages(breedId: String) = viewModelScope.launch(dispatchers.io) {
        //TODO update UI layer to dynamically determine page/limit/order parameters
        val parameters = GetBreedImagesParameters(breedId = breedId, page = 10, limit = 100, order = ImageOrderType.RANDOM.name)
        try {
            val data = getBreedImagesUseCase.invoke(parameters = parameters).last()
            sendEvent(MainScreenUiEvent.ShowBreedImages(images = data))
        } catch (e: Throwable) {
            Timber.e(wrapErrorMessage(e))
            sendEvent(MainScreenUiEvent.ShowError(message = e.message ?: wrapErrorMessage(Throwable("Unknown error"))))
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
                is MainScreenUiEvent.UpdateSelectedBreed -> {
                    setState(oldState.copy(isLoading = false, selectedBreed = event.breed))
                }
                is MainScreenUiEvent.ShowError -> {
                    setState(oldState.copy(isLoading = false, images = emptyList(), errorMessage = event.message))
                }
                is MainScreenUiEvent.UpdateConnectionIndicator -> {
                    val connected = event.connected
                    setState(oldState.copy(
                        connectionIndicatorColor = if (connected) Color.Green else Color.Red))
                }
                else -> {
                    // do nothing
                    Timber.d("MainReducer - invoked reduce else branch.")
                }
            }
        }
    }

    private fun wrapErrorMessage(error: Throwable) = "${MainViewModel::class.qualifiedName}: $error."
}
