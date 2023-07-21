package you.thiago.koalaloadinglibrary

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.thiago.koalaloadinglibrary.R
import kotlinx.coroutines.launch

/**
 * Created by Thiago You on 2023/07.
 */
class KoalaLoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : RelativeLayout(context, attrs) {
    
    private var operatingAnim: Animation? = null
    
    private val content: View by lazy { findViewById(R.id.content) }
    private val leaf: ImageView by lazy { findViewById(R.id.leaf) }
    private val graduallyTextView: GraduallyTextView by lazy { findViewById(R.id.graduallyTextView) }
    
    private var viewText: String? = null
    
    init {
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        val view = View.inflate(context, R.layout.koala_loading_simple, null)

        addView(view, params)
        setupView()
    }

    fun launch() {
        if (content.isVisible) {
            stop()
        } else {
            start()
        }
    }
    
    fun start() {
        content.visibility = View.VISIBLE
        launchAnimation()
    }
    
    fun stop() {
        content.visibility = View.GONE
        stopAnimation()
    }

    fun setText(labelText: String?) {
        viewText = labelText

        if (!TextUtils.isEmpty(viewText)) {
            graduallyTextView.setText(viewText)
        }
    }

    fun setTextColor(@ColorRes colorRes: Int) {
        graduallyTextView.setTextColor(ContextCompat.getColor(context, colorRes))
    }
    
    private fun setupView() {
        operatingAnim = RotateAnimation(
            360f, 0f, Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )

        operatingAnim?.repeatCount = Animation.INFINITE
        operatingAnim?.duration = 2000

        val lin = LinearInterpolator()
        operatingAnim?.interpolator = lin
        
        if (!TextUtils.isEmpty(viewText)) {
            graduallyTextView.setText(viewText)
        }
    }
    
    private fun startAnimation() {
        leaf.animation = operatingAnim
        graduallyTextView.startLoading()
    }
    
    private fun stopAnimation() {
        leaf.clearAnimation()
        graduallyTextView.stopLoading()
    }
    
    private fun launchAnimation() {
        findViewTreeLifecycleOwner()?.apply {
            if (content.isVisible) {
                lifecycleScope.launch {
                    lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                        startAnimation()
                    }
                }
    
                lifecycleScope.launch {
                    lifecycle.repeatOnLifecycle(Lifecycle.State.DESTROYED) {
                        stopAnimation()
                    }
                }
            }
        }
    }
}