package com.jackson.swipecomparison

import android.annotation.SuppressLint
import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.updateLayoutParams
import com.jackson.swipecomparison.utils.Utils
import com.jackson.swipecomparison.utils.ViewUtils

class SwipeComparsionView: ConstraintLayout, View.OnTouchListener {

    private var widthPx: Int = 0
    private var heightPx: Int = 0
    private var leftMargin = 0
    private var rightMargin = 0

    private var afterDrawableRes = R.drawable.ic_android_blue
    private var beforeDrawableRes = R.drawable.ic_android_black
    private var thumbDrawableRes = R.drawable.img_swiper_thumb
    private var thumbWidth = Utils.dp2px(context, 56).toInt()
    private var thumbHeight = Utils.dp2px(context, 56).toInt()
    private var centerLineWidth = Utils.dp2px(context, 2).toInt()
    private var centerLineColor = R.color.default_center_line_color

    private val ivAfter: ImageView by lazy {
        ImageView(context).apply {
            id = R.id.iv_after
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 0).apply {
                startToStart = getParentId()
                topToTop = getParentId()
                endToEnd = getParentId()
                bottomToBottom = getParentId()
            }
            setImageResource(afterDrawableRes)
        }
    }
    private val llBefore: LinearLayout by lazy {
        LinearLayout(context).apply {
            id = R.id.ll_after
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, 0).apply {
                startToStart = R.id.iv_after
                topToTop = R.id.iv_after
                bottomToBottom = R.id.iv_after
            }
        }
    }
    private val ivBefore: ImageView by lazy {
        ImageView(context).apply {
            id = R.id.iv_before
            layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            setImageResource(beforeDrawableRes)
        }
    }

    private val swiperThumb: ImageView by lazy {
        ImageView(context).apply {
            id = R.id.iv_swiper_thumb
            layoutParams = LayoutParams(thumbWidth, thumbHeight).apply {
                topToTop = R.id.iv_after
                bottomToBottom = R.id.iv_after
            }
            setImageResource(thumbDrawableRes)
        }
    }

    private val centerLine: View by lazy {
        View(context).apply {
            id = R.id.v_center_line
            layoutParams = LayoutParams(centerLineWidth, 0).apply {
                startToStart = R.id.iv_swiper_thumb
                topToTop = R.id.iv_after
                endToEnd = R.id.iv_swiper_thumb
                bottomToBottom = R.id.iv_after
            }
            setBackgroundResource(centerLineColor)
        }
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

    private fun getParentId(): Int {
        return id
    }

    private fun initView(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
    ) {
        initAttributes(context, attrs, defStyleAttr, defStyleRes)
        applyAttributes()
    }

    private fun initAttributes(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
    ) {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.SwipeComparsionView,
                defStyleAttr,
                defStyleRes
            )

            try {

            } finally {
                typedArray.recycle()
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun applyAttributes() {
        // default parent id
        if (getParentId() < 0) {
            id = R.id.siv_parent_id
        }
        // before imageView
        addView(ivAfter)
        // after imageView
        llBefore.addView(ivBefore)
        addView(llBefore)

        // Center Line
        addView(centerLine)
        // Thumb
        swiperThumb.setOnTouchListener(this)
        addView(swiperThumb)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val _width = right - left
        val _height = bottom - top

        if (widthPx != _width || heightPx != _height) {
            leftMargin = ViewUtils.calcTotalMarginStarts(this)
            rightMargin = ViewUtils.calcTotalMarginEnd(this)
            widthPx = _width
            heightPx = _height
            initViewSize(widthPx, heightPx)
        }
    }

    /**
     * 전체 뷰 크기가 변경될 때에만 호출됩니다.
     * - width or height 변경 시
     */
    private fun initViewSize(widthPx: Int, heightPx: Int) {
        val centerValue = widthPx / 2
        llBefore.updateLayoutParams {
            this.width = centerValue
        }

        swiperThumb.x = (centerValue - (thumbWidth / 2)).toFloat()
        centerLine.x = centerValue.toFloat()

        ivBefore.updateLayoutParams<LinearLayout.LayoutParams> {
            this.width = widthPx
        }
    }

    private var oldXvalue = 0f
    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        return if (view != null && event != null) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> oldXvalue = event.x
                MotionEvent.ACTION_MOVE -> {
                    val thumbWidth = view.width
                    val x = (event.rawX - oldXvalue) - leftMargin

                    // 화면 밖의 영역으로 이동하지 않게 제한
                    val targetPosition = when {
                        x >= (widthPx - thumbWidth) -> (widthPx - thumbWidth).toFloat()
                        x > 0 -> x
                        else -> 0f
                    }

                    // Thumb
                    view.x = targetPosition

                    // Center Line
                    val centerValue = targetPosition + (thumbWidth / 2)
                    centerLine.x = centerValue

                    // before image parent layout
                    llBefore.updateLayoutParams<LayoutParams> {
                        this.width = centerValue.toInt()
                    }
                }
            }
            true
        } else false
    }
}