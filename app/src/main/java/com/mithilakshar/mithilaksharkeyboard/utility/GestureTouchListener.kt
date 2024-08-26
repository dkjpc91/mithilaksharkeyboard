package com.mithilakshar.mithilaksharkeyboard.utility

import android.animation.ValueAnimator
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast

class GestureTouchListener(private val context: Context,private val view: View) : GestureDetector.SimpleOnGestureListener(), View.OnTouchListener {
    private val handler = Handler(Looper.getMainLooper())
    private val longPressTimeout = ViewConfiguration.getLongPressTimeout() + 1000
    private var xDelta = 0f
    private var yDelta = 0f
    private var initialTouchX = 0f
    private var initialTouchY = 0f
    private var initialWidth = 0
    private var initialHeight = 0
    private var isResizing = false
    private val edgeThreshold = 50

    private var currentView: View? = null
    private val gestureDetector = GestureDetector(context, this)
    private val scaleGestureDetector = ScaleGestureDetector(context, ScaleGestureListener())
    private val longPressRunnable = Runnable {
        val dialog = LongPressMenuDialog(context, view, onDelete = {
            Toast.makeText(context, "View Deleted", Toast.LENGTH_SHORT).show()
            val parentViewGroup = view.parent as? ViewGroup
            parentViewGroup?.removeView(view)
        })
        dialog.show()

        Toast.makeText(context, "Long press detected", Toast.LENGTH_SHORT).show()
    }
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (v == null || event == null) return false

        currentView = v

        // Pass the touch events to the GestureDetector and ScaleGestureDetector
        gestureDetector.onTouchEvent(event)
        scaleGestureDetector.onTouchEvent(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (isNearEdge(event.x, event.y, v)) {
                    initialWidth = v.width
                    initialHeight = v.height
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                    isResizing = true
                } else {
                    xDelta = event.rawX - v.x
                    yDelta = event.rawY - v.y
                    isResizing = false
                }
                handler.postDelayed(longPressRunnable, longPressTimeout.toLong())
                return true
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
                handler.removeCallbacks(longPressRunnable)
                isResizing = false
            }
        }
        return false
    }

    override fun onLongPress(e: MotionEvent) {
        super.onLongPress(e)
        Toast.makeText(context, "reached here", Toast.LENGTH_SHORT).show()
        if(view is EditText){
            Toast.makeText(context, "openkey", Toast.LENGTH_SHORT).show()
            openKeyboard(context, view)
        }


    }

    private fun isNearEdge(x: Float, y: Float, view: View): Boolean {
        val rightEdge = view.width.toFloat()
        val bottomEdge = view.height.toFloat()

        return (x >= rightEdge - edgeThreshold && y >= bottomEdge - edgeThreshold)
    }

    private fun animateResize(view: View, newWidth: Int, newHeight: Int) {
        // Animate width and height together for a more fluid transition
        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.duration = 200 // Duration of the animation in milliseconds
        animator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Float
            val layoutParams = view.layoutParams
            layoutParams.width = (view.width + (newWidth - view.width) * progress).toInt()
            layoutParams.height = (view.height + (newHeight - view.height) * progress).toInt()
            view.layoutParams = layoutParams
        }
        animator.start()
    }

    private inner class ScaleGestureListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val view = currentView ?: return false

            // Get the scale factor
            val scaleFactor = detector.scaleFactor

            // Calculate new width and height
            val newWidth = (view.width * scaleFactor).toInt()
            val newHeight = (view.height * scaleFactor).toInt()

            // Prevent resizing below a minimum size
            val minSize = 50
            if (newWidth >= minSize && newHeight >= minSize) {
                Log.d("GestureTouchListener", "Scaling view to width: $newWidth, height: $newHeight")
                animateResize(view, newWidth, newHeight)
            }

            return true
        }
    }

    fun openKeyboard(context: Context, editText: EditText) {
        editText.requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }
}
