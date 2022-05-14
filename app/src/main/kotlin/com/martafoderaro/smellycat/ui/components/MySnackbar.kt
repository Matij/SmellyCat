package com.martafoderaro.smellycat.com.martafoderaro.smellycat.ui.components

import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun MySnackbar(message: String) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = scope, block = {
        snackbarHostState.showSnackbar(message = message)
    })
    SnackbarHost(hostState = snackbarHostState)
}