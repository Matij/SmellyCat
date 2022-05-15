package com.martafoderaro.smellycat.com.martafoderaro.smellycat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.martafoderaro.smellycat.R
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.model.BreedImage
import kotlin.math.absoluteValue

private val maxOffset = 70.dp

@ExperimentalPagerApi
@Composable
fun ImagePager(images: List<BreedImage>, modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState()
    val currentIndex = pagerState.currentPage
    val currentPageOffset = pagerState.currentPageOffset
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalPager(count = images.size, state = pagerState, modifier = modifier) { index ->
            val offset = maxOffset * when (index) {
                currentIndex -> {
                    currentPageOffset.absoluteValue
                }
                currentIndex - 1 -> {
                    1 + currentPageOffset.coerceAtMost(0f)
                }
                currentIndex + 1 -> {
                    1 - currentPageOffset.coerceAtLeast(0f)
                }
                else -> {
                    1f
                }
            }

            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                val currentImage = images[index]
                SubcomposeAsyncImage(
                    model = currentImage.url,
                    contentDescription = currentImage.id,
                    contentScale = ContentScale.Inside,
                    alignment = Alignment.TopCenter,
                    modifier = Modifier
                        .offset(y = offset)
                        .height(300.dp)
                ) {
                    val state = painter.state
                    if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                        CircularProgressIndicator()
                    } else {
                        SubcomposeAsyncImageContent()
                    }
                }
                Text(
                    text = stringResource(id = R.string.image_label).format(currentImage.id),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.padding(8.dp))

        DotsIndicator(
            totalDots = images.size,
            selectedIndex = pagerState.currentPage,
            selectedColor = Color.Blue,
            unSelectedColor = Color.LightGray,
        )
    }
}

@Composable
fun DotsIndicator(
    totalDots : Int,
    selectedIndex : Int,
    selectedColor: Color,
    unSelectedColor: Color,
){

    LazyRow(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 32.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {

        items(totalDots) { index ->
            if (index == selectedIndex) {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(selectedColor)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(unSelectedColor)
                )
            }

            if (index != totalDots - 1) {
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            }
        }
    }
}