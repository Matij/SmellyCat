package com.martafoderaro.smellycat.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.graphics.Color
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.repository.BreedRepository
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.ui.main.MainScreenState
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.ui.main.MainViewModel
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.util.ConnectionStatusProvider
import com.martafoderaro.smellycat.core.CoroutineDispatcherProvider
import com.martafoderaro.smellycat.data.datasources.network.ResultWrapper
import com.martafoderaro.smellycat.ui.main.TestBreedImageProvider.breedImages
import com.martafoderaro.smellycat.ui.main.TestBreedsProvider.aBreedsList
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel
    private val breedRepository: BreedRepository = mockk(relaxed = true)
    private val connectionStatusProvider: ConnectionStatusProvider = mockk(relaxed = true)
    private val dispatcher = Dispatchers.Unconfined
    private val coroutineDispatchers = CoroutineDispatcherProvider(
        main = dispatcher, io = dispatcher, default = dispatcher
    )

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @Test
    fun `given device is offline and no empty local data, when requesting breeds, show empty state`() {
        every { connectionStatusProvider.isConnected() } returns false
        coEvery { breedRepository.breeds() } returns ResultWrapper.Success(emptyList())

        initViewModel()

        val expectedState = MainScreenState.initial().copy(isLoading = false)
        assertEquals(expected = expectedState, actual = viewModel.state.value)
    }

    @Test
    fun `given device is online and no empty local data, when requesting breeds, show data`() {
        val testBreeds = aBreedsList()
        every { connectionStatusProvider.isConnected() } returns true
        coEvery { breedRepository.breeds() } returns ResultWrapper.Success(testBreeds)

        initViewModel()

        val expectedState = MainScreenState.initial()
        assertEquals(
            expected = expectedState.copy(isLoading = false, breeds = testBreeds, connectionIndicatorColor = Color.Green),
            actual = viewModel.state.value
        )
    }

    @Test
    fun `given device is online, when breed is selected, show selected breed images`() {
        val testBreeds = aBreedsList()
        val testBreedIndex = testBreeds.size - 1
        val selectedBreed = testBreeds[testBreedIndex]
        val selectedBreedId = selectedBreed.id
        val breedImages = breedImages(breedId = selectedBreedId)

        every { connectionStatusProvider.isConnected() } returns true
        coEvery { breedRepository.breeds() } returns ResultWrapper.Success(testBreeds)
        coEvery { breedRepository.breed(breedId = any()) } returns selectedBreed
        coEvery {
            breedRepository.searchBreedImages(breedId = any())
        } returns ResultWrapper.Success(breedImages)

        initViewModel()
        viewModel.onBreedSelected(breedId = selectedBreedId)

        val expectedState = MainScreenState.initial()
        assertEquals(
            expected = expectedState.copy(
                isLoading = false,
                breeds = testBreeds,
                images = breedImages,
                selectedBreed = selectedBreed,
                connectionIndicatorColor = Color.Green
            ),
            actual = viewModel.state.value
        )
    }

    @After
    fun clean() {
        Dispatchers.resetMain()
    }

    private fun initViewModel() {
        viewModel = MainViewModel(
            breedRepository = breedRepository,
            connectionStatusProvider = connectionStatusProvider,
            dispatchers = coroutineDispatchers,
        )
    }
}