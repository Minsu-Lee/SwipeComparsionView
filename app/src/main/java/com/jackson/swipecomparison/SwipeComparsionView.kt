package com.jackson.swipecomparison

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.AttributeSet
import android.util.Log

class SwipeComparsionView: ConstraintLayout {

    private var widthPx: Int = 0
    private var heightPx: Int = 0

    companion object {
        const val TAG = "SwipeComparsionView"
    }

    constructor(context: Context) : super(context) { initView(context) }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { initView(context, attrs) }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context, attrs, defStyleAttr)
    }

    private fun initView(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
    ) {

    }

    /**
     * - MeasureSpec.AT_MOST : wrap_content 에 매핑되며 뷰 내부의 크기에 따라 크기가 달라집니다.
     * - MeasureSpec.EXACTLY : fill_parent, match_parent 로 외부에서 미리 크기가 지정되었다.
     * - MeasureSpec.UNSPECIFIED : Mode 가 설정되지 않았을 경우. 소스상에서 직접 넣었을 때 주로 불립니다.
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val (widthMode, width) = getWidthSize(widthMeasureSpec)
        val (heightMode, height) = getHeightSize(heightMeasureSpec)
        val widthDp = Utils.px2dp(context, width).toInt()
        val heightDp = Utils.px2dp(context, height).toInt()
        val widthModeStr = getModeStr(widthMode)
        val heightModeStr = getModeStr(heightMode)
        setMeasuredDimension(width, height)

        val status = if (widthPx != width || heightPx != height ) {
            widthPx = width
            heightPx = height
            " [ change value ]"
        } else ""

        Log.e(
            TAG,
            "onMeasure$status\nwidth: $width px ( $widthDp dp, $widthModeStr mode )\nheight: $height px ( $heightDp dp, $heightModeStr mode )"
        )
    }

    private fun getModeStr(mode: Int): String {
        return when (mode) {
            MeasureSpec.EXACTLY -> "EXACTLY"
            MeasureSpec.AT_MOST -> "AT_MOST"
            else -> "UNSPECIFIED"
        }
    }

    private fun getSize(measureSpec: Int, mostValue: Int): Pair<Int, Int> {
        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)
        val value = when (mode) {
            MeasureSpec.EXACTLY -> size
            MeasureSpec.AT_MOST -> mostValue.coerceAtMost(size)
            else -> measureSpec
        }

        return mode to value
    }

    private fun getWidthSize(measureSpec: Int): Pair<Int, Int> {
        val mostValue = paddingLeft + paddingRight + suggestedMinimumWidth
        return getSize(measureSpec, mostValue)
    }

    private fun getHeightSize(measureSpec: Int): Pair<Int, Int> {
        val mostValue = paddingTop + paddingBottom + suggestedMinimumHeight
        return getSize(measureSpec, mostValue)
    }
}