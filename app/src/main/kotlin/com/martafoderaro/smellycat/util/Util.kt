package com.martafoderaro.smellycat.com.martafoderaro.smellycat.util

import timber.log.Timber
import java.io.IOException

fun isConnected(): Boolean {
    val command = "ping -c 1 google.com"
    var connected = false
    try {
        connected = Runtime.getRuntime().exec(command).waitFor() == 0
    } catch (e: InterruptedException) {
        Timber.d(e)
    } catch (e: IOException) {
        Timber.d(e)
    }
    return connected
}