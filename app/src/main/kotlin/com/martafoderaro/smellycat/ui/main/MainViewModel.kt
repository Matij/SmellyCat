package com.martafoderaro.smellycat.com.martafoderaro.smellycat.ui.main

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.martafoderaro.smellycat.base.BaseViewModel
import com.martafoderaro.smellycat.base.Reducer
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.repository.BreedRepository
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.util.ConnectionStatusProvider
import com.martafoderaro.smellycat.core.CoroutineDispatcherProvider
import com.martafoderaro.smellycat.data.datasources.network.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class MainViewModel @Inject constructor(
    private val breedRepository: BreedRepository,
    private val connectionStatusProvider: ConnectionStatusProvider,
    private val dispatchers: CoroutineDispatcherProvider,
): BaseViewModel<MainScreenState, MainScreenUiEvent>() {

    enum class ImageOrderType {
        RANDOM, ASC, DESC
    }

    private val reducer = MainReducer(MainScreenState.initial())

    override val state: StateFlow<MainScreenState>
        get() = reducer.state

    init {
        loadBreeds()
        viewModelScope.launch(dispatchers.main) { observeConnectionState() }
    }

    fun onBreedSelected(breedId: String) {
        sendEvent(MainScreenUiEvent.ShowLoadingState(isLoading = true))
        loadBreed(breedId)
        getBreedImages(breedId = breedId)
    }

    private fun loadBreed(breedId: String) = viewModelScope.launch(dispatchers.default) {
        try {
            val data = breedRepository.breed(breedId = breedId)
            sendEvent(MainScreenUiEvent.UpdateSelectedBreed(breed = data))
        } catch (e: Throwable) {
            Timber.e(wrapErrorMessage(e))
            sendEvent(MainScreenUiEvent.ShowError(
                message = e.message ?: "${MainViewModel::class.qualifiedName}: Unknown error."))
        }
    }

    private fun loadBreeds() = viewModelScope.launch(dispatchers.default) {
        when (val data = breedRepository.breeds()) {
            is ResultWrapper.Success -> sendEvent(MainScreenUiEvent.ShowBreeds(breeds = data.value))
            is ResultWrapper.GenericError -> handleError(data.error?.message)
            is ResultWrapper.NetworkError -> handleError(data.message)
        }
    }

    private suspend fun observeConnectionState() {
        viewModelScope.launchPeriodicAsync(1000L) {
            val newConnectedState = connectionStatusProvider.isConnected()
            sendEvent(MainScreenUiEvent.UpdateConnectionIndicator(newConnectedState))
        }.await()
    }

    private fun getBreedImages(breedId: String) = viewModelScope.launch(dispatchers.default) {
        //TODO update UI layer to dynamically determine page/limit/order parameters
        when (val data = breedRepository.searchBreedImages(breedId = breedId)) {
            is ResultWrapper.Success -> {
                sendEvent(MainScreenUiEvent.ShowBreedImages(images = data.value))
                sendEvent(MainScreenUiEvent.ShowLoadingState(isLoading = false))
            }
            is ResultWrapper.GenericError -> handleError(errorMessage = data.error?.message)
            is ResultWrapper.NetworkError -> handleError(errorMessage = data.message)
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
                is MainScreenUiEvent.ShowLoadingState -> {
                    setState(oldState.copy(isLoading = event.isLoading))
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
            }
        }
    }

    private fun handleError(errorMessage: String?) {
        val message = errorMessage ?: wrapErrorMessage(Throwable("Unknown error"))
        Timber.e(message)
        sendEvent(MainScreenUiEvent.ShowError(message = message))
    }

    private fun wrapErrorMessage(error: Throwable) = "${MainViewModel::class.qualifiedName}: $error."
}
