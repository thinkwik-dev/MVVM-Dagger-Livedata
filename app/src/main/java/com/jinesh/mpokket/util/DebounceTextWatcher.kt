package com.jinesh.mpokket.util

import android.text.Editable
import android.text.TextWatcher
import java.util.*


abstract class DebounceTextWatcher : TextWatcher {

    private var timer = Timer()
    private val DELAY: Long = 400 // milliseconds
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}


    override fun afterTextChanged(s: Editable) {
        timer.cancel()
        timer = Timer()
        timer.schedule(
            object : TimerTask() {
                override fun run() {
                    onDone()
                }
            },
            DELAY
        )
    }

    abstract fun onDone()


}
