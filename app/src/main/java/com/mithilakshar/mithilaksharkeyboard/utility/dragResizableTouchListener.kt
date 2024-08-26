package com.mithilakshar.mithilaksharkeyboard.utility

import android.graphics.Matrix
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.ImageView

class DragResizableTouchListener(private val imageView: ImageView) : View.OnTouchListener {

    private val gestureDetector: GestureDetector
    private val scaleGestureDetector: ScaleGestureDetector
    private val matrix: Matrix = Matrix()
    private var isScaling = false
    private var isDragging = false
    private var lastX = 0f
    private var lastY = 0f

    init {
        gestureDetector = GestureDetector(imageView.context, GestureListener())
        scaleGestureDetector = ScaleGestureDetector(imageView.context, ScaleGestureListener())
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (v == null || event == null) return false

        // Handle scaling
        scaleGestureDetector.onTouchEvent(event)

        // Handle dragging
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = event.rawX
                lastY = event.rawY
                isDragging = !isScaling
            }
            MotionEvent.ACTION_MOVE -> {
                if (isDragging) {
                    val dx = event.rawX - lastX
                    val dy = event.rawY - lastY
                    v.translationX += dx
                    v.translationY += dy
                    lastX = event.rawX
                    lastY = event.rawY
                }
            }
            MotionEvent.ACTION_UP -> {
                isDragging = false
            }
        }

        gestureDetector.onTouchEvent(event)

        if (isScaling) {
            imageView.imageMatrix = matrix
        }

        return true
    }




    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            e?.let {
                // Scale up by 50% on double-tap
                matrix.postScale(1.5f, 1.5f, it.x, it.y)
                imageView.imageMatrix = matrix
                return true
            }
            return false
        }
    }
    private inner class ScaleGestureListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            detector?.let {
                // Scale the matrix based on the scale factor
                matrix.postScale(it.scaleFactor, it.scaleFactor, it.focusX, it.focusY)
                imageView.imageMatrix = matrix
                return true
            }
            return false
        }

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            // Optionally handle the beginning of a scale gesture
            isScaling = true
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector) {
            // Optionally handle the end of a scale gesture
            isScaling = false
        }
    }

}