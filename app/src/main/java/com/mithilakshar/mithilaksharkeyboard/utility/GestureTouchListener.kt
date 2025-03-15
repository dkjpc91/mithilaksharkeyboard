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

class GestureTouchListener(private val context: Context, private val view: View) : GestureDetector.SimpleOnGestureListener(), View.OnTouchListener {
    private val handler = Handler(Looper.getMainLooper())
    private val longPressTimeout = ViewConfiguration.getLongPressTimeout() + 2000
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

    // Runnable for long press detection
    private val longPressRunnable = Runnable {
        val dialog = LongPressMenuDialog(context, view, onDelete = {
            Toast.makeText(context, "संसोधन आइटम डिलीट भ गेल ", Toast.LENGTH_SHORT).show()
            val parentViewGroup = view.parent as? ViewGroup
            parentViewGroup?.removeView(view)
        })
        dialog.show()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (v == null || event == null) return false

        currentView = v

        // Pass the touch events to the GestureDetector and ScaleGestureDetector
        gestureDetector.onTouchEvent(event)
        scaleGestureDetector.onTouchEvent(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                handleActionDown(event, v)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                handleActionMove(event, v)
                return true
            }
            MotionEvent.ACTION_UP -> {
                handleActionUp(v)
            }
        }
        return false
    }

    private fun handleActionDown(event: MotionEvent, v: View) {
        if (isNearEdge(event.x, event.y, v)) {
            // Start resizing if near the edge
            initialWidth = v.width
            initialHeight = v.height
            initialTouchX = event.rawX
            initialTouchY = event.rawY
            isResizing = true
        } else {
            // Start moving the view
            xDelta = event.rawX - v.x
            yDelta = event.rawY - v.y
            isResizing = false
        }
        // Start long press detection
  /*      handler.postDelayed(longPressRunnable, longPressTimeout.toLong())*/

    }

    private fun handleActionMove(event: MotionEvent, v: View) {
        if (isResizing) {
            // Resize the view
            val newWidth = (initialWidth + (event.rawX - initialTouchX)).toInt()
            val newHeight = (initialHeight + (event.rawY - initialTouchY)).toInt()

            // Prevent resizing below a minimum size
            val minWidth = 50
            val minHeight = 50
            if (newWidth >= minWidth && newHeight >= minHeight) {
                animateResize(v, newWidth, newHeight)
            }
        } else {
            // Move the view
            v.animate()
                .x(event.rawX - xDelta)
                .y(event.rawY - yDelta)
                .setDuration(0)
                .start()
        }
    }

    private fun handleActionUp(v: View) {
        // Remove the long press callback and stop resizing
        v.requestFocus()
        handler.removeCallbacks(longPressRunnable)
        isResizing = false
    }

    override fun onLongPress(e: MotionEvent) {
        super.onLongPress(e)
        Toast.makeText(context, "अहाँ चयनित वस्तु केँ घसकाबि अथवा ओकर आकार बदलि सकैत छी", Toast.LENGTH_SHORT).show();

        if (view is EditText) {
            Toast.makeText(context, "openkey", Toast.LENGTH_SHORT).show()
           /* openKeyboard(context, view)*/
        }
    }

    private fun isNearEdge(x: Float, y: Float, view: View): Boolean {
        val rightEdge = view.width.toFloat()
        val bottomEdge = view.height.toFloat()
        return (x >= rightEdge - edgeThreshold && y >= bottomEdge - edgeThreshold)
    }

    private fun animateResize(view: View, newWidth: Int, newHeight: Int) {
        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.duration = 200 // Duration of the animation
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

            val scaleFactor = detector.scaleFactor
            val deltaX = detector.currentSpanX - detector.previousSpanX
            val deltaY = detector.currentSpanY - detector.previousSpanY

            // Horizontal scaling
            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                scaleViewHorizontally(view, scaleFactor)
            }
            // Vertical scaling
            else if (Math.abs(deltaY) > Math.abs(deltaX)) {
                scaleViewVertically(view, scaleFactor)
            }
            // Diagonal scaling
            else {
                scaleViewDiagonally(view, scaleFactor)
            }

            return true
        }

        private fun scaleViewHorizontally(view: View, scaleFactor: Float) {
            val newScaleX = view.scaleX * scaleFactor
            if (newScaleX in 0.5f..3.0f) {
                view.scaleX = newScaleX
                Log.d("ScaleGestureListener", "Scaling view horizontally to scaleX: ${view.scaleX}")
            }
        }

        private fun scaleViewVertically(view: View, scaleFactor: Float) {
            val newScaleY = view.scaleY * scaleFactor
            if (newScaleY in 0.5f..3.0f) {
                view.scaleY = newScaleY
                Log.d("ScaleGestureListener", "Scaling view vertically to scaleY: ${view.scaleY}")
            }
        }

        private fun scaleViewDiagonally(view: View, scaleFactor: Float) {
            val newScaleX = view.scaleX * scaleFactor
            val newScaleY = view.scaleY * scaleFactor
            if (newScaleX in 0.5f..3.0f && newScaleY in 0.5f..3.0f) {
                view.scaleX = newScaleX
                view.scaleY = newScaleY
                Log.d("ScaleGestureListener", "Scaling view diagonally to scaleX: ${view.scaleX}, scaleY: ${view.scaleY}")
            }
        }
    }

    // Open the keyboard when an EditText is touched
    fun openKeyboard(context: Context, editText: EditText) {
        editText.requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }
}
