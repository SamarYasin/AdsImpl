package com.example.adsimpl.adManager

import android.os.CountDownTimer
import javax.inject.Inject

class AdTimer @Inject constructor() {

    private var timer: CountDownTimer? = null
    private var duration: Long = 0L
    private var onTick: ((Long) -> Unit)? = null
    private var onFinish: (() -> Unit)? = null

    fun startTimer(
        duration: Long,
        onTick: (Long) -> Unit,
        onFinish: () -> Unit
    ) {
        this.duration = duration
        this.onTick = onTick
        this.onFinish = onFinish
        timer?.cancel()
        timer = object : CountDownTimer(duration, 30000) {
            override fun onTick(millisUntilFinished: Long) {
                onTick(millisUntilFinished / 1000)
            }

            override fun onFinish() {
                onFinish()
            }
        }.also { it.start() }
    }

    fun resetTimer() {
        timer?.cancel()
        if (duration > 0 && onTick != null && onFinish != null) {
            startTimer(duration, onTick!!, onFinish!!)
        }
    }

    fun stopTimer() {
        timer?.cancel()
        timer = null
    }

}