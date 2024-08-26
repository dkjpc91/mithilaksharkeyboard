package com.mithilakshar.mithilaksharkeyboard.utility

import android.animation.ValueAnimator
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

class ResizableTouchListener : View.OnTouchListener {

    private var xDelta = 0f
    private var yDelta = 0f
    private var initialTouchX = 0f
    private var initialTouchY = 0f
    private var initialWidth = 0
    private var initialHeight = 0
    private var isResizing = false
    private val edgeThreshold = 50 // Threshold for detecting edge

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (v == null || event == null) return false

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (isNearEdge(event.x, event.y, v)) {
                    initialWidth = v.width
                    initialHeight = v.height
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                    isResizing = true
                    return true
                } else {
                    xDelta = event.rawX - v.x
                    yDelta = event.rawY - v.y
                    isResizing = false
                    return true
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (isResizing) {
                    val newWidth = (initialWidth + (event.rawX - initialTouchX)).toInt()
                    val newHeight = (initialHeight + (event.rawY - initialTouchY)).toInt()

                    // Prevent resizing below a minimum size
                    val minWidth = 50
                    val minHeight = 50
                    if (newWidth >= minWidth && newHeight >= minHeight) {
                        animateResize(v, newWidth, newHeight)
                    }
                } else {
                    v.animate()
                        .x(event.rawX - xDelta)
                        .y(event.rawY - yDelta)
                        .setDuration(0)
                        .start()
                }
                return true
            }
            MotionEvent.ACTION_UP -> {
                v.requestFocus()
                isResizing = false
            }
        }
        return false
    }

    private fun animateResize(view: View, newWidth: Int, newHeight: Int) {
        // Animate width
        val widthAnimator = ValueAnimator.ofInt(view.width, newWidth)
        widthAnimator.addUpdateListener { animation ->
            val layoutParams = view.layoutParams
            layoutParams.width = animation.animatedValue as Int
            view.layoutParams = layoutParams
        }
        widthAnimator.duration = 200 // Duration of the animation in milliseconds
        widthAnimator.start()

        // Animate height
        val heightAnimator = ValueAnimator.ofInt(view.height, newHeight)
        heightAnimator.addUpdateListener { animation ->
            val layoutParams = view.layoutParams
            layoutParams.height = animation.animatedValue as Int
            view.layoutParams = layoutParams
        }
        heightAnimator.duration = 200 // Duration of the animation in milliseconds
        heightAnimator.start()
    }

    private fun isNearEdge(x: Float, y: Float, view: View): Boolean {
        val rightEdge = view.width.toFloat()
        val bottomEdge = view.height.toFloat()

        return (x >= rightEdge - edgeThreshold && y >= bottomEdge - edgeThreshold)
    }
}
