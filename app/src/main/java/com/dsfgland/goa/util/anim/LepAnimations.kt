package com.dsfgland.goa.util.anim

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import androidx.core.animation.doOnEnd

fun show(view: View, onEnd: () -> Unit) {
    AnimatorSet().apply {
        playTogether(
            ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f),
            ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f)
        )
        duration = 500
        doOnEnd { onEnd() }
        start()
    }
}