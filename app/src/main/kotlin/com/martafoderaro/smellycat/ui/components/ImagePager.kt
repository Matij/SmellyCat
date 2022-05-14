package com.martafoderaro.smellycat.com.martafoderaro.smellycat.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.model.BreedImage

@ExperimentalPagerApi
@Composable
fun ImagePager(images: List<BreedImage>, modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState()
    HorizontalPager(count = images.size, state = pagerState, modifier = modifier) { index ->
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            val currentImage = images[index]
            SubcomposeAsyncImage(
                model = currentImage.url,
                contentDescription = currentImage.id
            ) {
                val state = painter.state
                if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                    CircularProgressIndicator()
                } else {
                    SubcomposeAsyncImageContent()
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Image ${currentImage.id}", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}