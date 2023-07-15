package com.jackson.swipecomparsion.utils

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics

object Utils {

    fun deviceSize(context: Context): Pair<Int, Int> {
        val metrics: DisplayMetrics = context.resources.displayMetrics
        val screenWidth = metrics.widthPixels
        val screenHeight = metrics.heightPixels
        return screenWidth to screenHeight
    }

    fun deviceWidth(context: Context): Int = deviceSize(context).first

    fun deviceHeight(context: Context): Int = deviceSize(context).second

    fun dp2px(context: Context, dp: Int): Float = dp2px(context, dp.toFloat())

    fun dp2px(context: Context, dp: Float): Float {
        val resources: Resources = context.resources
        val metrics: DisplayMetrics = resources.displayMetrics
        return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun px2dp(context: Context, px: Int): Float = px2dp(context, px.toFloat())

    fun px2dp(context: Context, px: Float): Float {
        val resources: Resources = context.resources
        val metrics: DisplayMetrics = resources.displayMetrics
        return px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}