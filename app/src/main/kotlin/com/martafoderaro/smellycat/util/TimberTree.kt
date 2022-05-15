package com.martafoderaro.smellycat.com.martafoderaro.smellycat.util

import timber.log.Timber

class TimberTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        //TODO integrate wth Crashlytics/Sentry/Sematext/others...
    }
}