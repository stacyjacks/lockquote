package com.kurmakaeva.anastasia.lockquote.network

import com.kurmakaeva.anastasia.lockquote.R

enum class NetworkResult(val colorResId: Int, val messageResId: Int) {
    DISCONNECTED(R.color.whiteColor, R.string.messageDisconnected),
    CONNECTED(R.color.colorAccent, R.string.messageDisconnected)
}