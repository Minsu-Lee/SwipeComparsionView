package com.jackson.swipecomparsion.utils

import android.view.View
import androidx.core.view.marginEnd
import androidx.core.view.marginStart

object ViewUtils {

    fun calcTotalMarginStarts(view: View?): Int {
        return if (view != null) {
            val marginStart = (view as? View)?.marginStart ?: 0
            marginStart + calcTotalMarginStarts(view.parent as? View)
        } else {
            0
        }
    }

    fun calcTotalMarginEnd(view: View?): Int {
        return if (view != null) {
            val marginEnd = (view as? View)?.marginEnd ?: 0
            marginEnd + calcTotalMarginEnd(view.parent as? View)
        } else {
            0
        }
    }
}