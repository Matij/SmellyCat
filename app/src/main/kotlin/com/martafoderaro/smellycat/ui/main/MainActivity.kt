package com.martafoderaro.smellycat.com.martafoderaro.smellycat.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.martafoderaro.smellycat.R
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.ui.components.Dropdown
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.ui.components.ImagePager
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.ui.components.MySnackbar
import com.martafoderaro.smellycat.ui.components.ContentWithProgress
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalPagerApi
@AndroidEntryPoint
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
class MainActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }

    @Preview
    @Composable
    private fun CatBreedPreview() {
        MainScreen(viewModel())
    }

    @Composable
    private fun MainScreen(
        viewModel: MainViewModel = viewModel(),
        scaffoldState: ScaffoldState = rememberScaffoldState()
    ) {
        val state by viewModel.state.collectAsState(MainScreenState.initial())

        Scaffold(
            topBar = {
                TopAppBar(title = {
                    Text(text = stringResource(id = R.string.main_screen_title))
                })
            },
            scaffoldState = scaffoldState
        ) {
            when {
                state.isLoading -> ContentWithProgress()
                state.breeds.isNotEmpty() -> MainScreenContent(
                    state = state,
                    onBreedSelected = { selectedBreedId ->
                        viewModel.onBreedSelected(breedId = selectedBreedId)
                    }
                )
                state.errorMessage != null -> {
                    MainScreenEmpty()
                    MySnackbar(state.errorMessage!!)
                }
                state.breeds.isEmpty() || state.images.isEmpty() -> {
                    MainScreenEmpty()
                }
            }
        }
    }

    @Composable
    private fun MainScreenContent(
        state: MainScreenState,
        onBreedSelected: (String) -> Unit = {},
    ) {
        val scrollState = rememberScrollState()

        Column(modifier = Modifier.verticalScroll(scrollState)) {
            Dropdown(
                items = state.breeds,
                onItemSelected = onBreedSelected
            )

            Spacer(modifier = Modifier.height(8.dp))

            ImagePager(
                images = state.images,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    @Composable
    private fun MainScreenEmpty() {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painterResource(R.drawable.empty_state_illustration),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.width(300.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.empty_state_label),
                textAlign = TextAlign.Center,
                fontSize = 14.sp
            )
        }
    }
}