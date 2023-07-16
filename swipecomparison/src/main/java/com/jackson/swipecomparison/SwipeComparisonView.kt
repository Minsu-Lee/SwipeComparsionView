package com.jackson.swipecomparison

import android.annotation.SuppressLint
import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import com.jackson.swipecomparison.utils.Utils
import com.jackson.swipecomparison.utils.ViewUtils

class SwipeComparisonView: ConstraintLayout, View.OnTouchListener {

    private var widthPx: Int = 0
    private var leftMargin = 0

    private var afterDrawableRes: Int = 0
    private var beforeDrawableRes: Int = 0
    private var thumbDrawableRes: Int = 0
    private var thumbWidth: Int = 0
    private var thumbHeight: Int = 0
    private var centerLineWidth: Int = 0
    private var centerLineColor: Int = 0

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
            setBackgroundColor(centerLineColor)
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
                R.styleable.SwipeComparisonView,
                defStyleAttr,
                defStyleRes
            )

            try {
                afterDrawableRes = typedArray.getResourceId(
                    R.styleable.SwipeComparisonView_afterDrawableRes,
                    R.drawable.ic_android_blue
                )
                beforeDrawableRes = typedArray.getResourceId(
                    R.styleable.SwipeComparisonView_beforeDrawableRes,
                    R.drawable.ic_android_black
                )
                thumbDrawableRes = typedArray.getResourceId(
                    R.styleable.SwipeComparisonView_thumbDrawableRes,
                    R.drawable.img_swiper_thumb
                )
                thumbWidth = typedArray.getDimensionPixelSize(
                    R.styleable.SwipeComparisonView_thumbWidth,
                    Utils.dp2px(context, 56).toInt()
                )
                thumbHeight = typedArray.getDimensionPixelSize(
                    R.styleable.SwipeComparisonView_thumbHeight,
                    thumbWidth
                )
                centerLineWidth = typedArray.getDimensionPixelSize(
                    R.styleable.SwipeComparisonView_centerLineWidth,
                    Utils.dp2px(context, 2).toInt()
                )
                centerLineColor = typedArray.getColor(
                    R.styleable.SwipeComparisonView_centerLineColor,
                    ContextCompat.getColor(context, R.color.default_center_line_color)
                )

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

        if (widthPx != _width) {
            leftMargin = ViewUtils.calcTotalMarginStarts(this)
            widthPx = _width
            initViewSize(widthPx)
        }
    }

    /**
     * 전체 뷰 크기가 변경될 때에만 호출됩니다.
     * - width or height 변경 시
     */
    private fun initViewSize(widthPx: Int) {
        val centerValue = widthPx / 2
        llBefore.updateLayoutParams {
            this.width = centerValue
        }

        swiperThumb.x = (centerValue - (thumbWidth / 2)).toFloat()
        val centerLineWidth = centerLine.width
        centerLine.x = centerValue.toFloat() - centerLineWidth.div(2)

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
                    val centerValue = targetPosition + thumbWidth.div(2)
                    val centerLineWidth = centerLine.width
                    centerLine.x = centerValue - centerLineWidth.div(2)

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