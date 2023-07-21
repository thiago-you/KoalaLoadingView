package you.thiago.koalaloadinglibrary

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.RelativeLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.thiago.koalaloadinglibrary.R

/**
 * Created by Thiago You on 2023/07.
 */
class KoalaLoadingDialog : BaseDialogFragment() {
    
    private lateinit var operatingAnim: Animation
    private lateinit var graduallyTextView: GraduallyTextView
    private lateinit var background: ConstraintLayout
    private lateinit var leaf: View
    
    private var viewText: String? = null
    private var color = 0
    private var mainDialog: Dialog? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mainDialog = Dialog(requireActivity(), R.style.cart_dialog).apply {
            setContentView(R.layout.koala_loading_dialog)
            window?.setGravity(Gravity.CENTER)
        }

        operatingAnim = RotateAnimation(
                360f, 0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        )

        operatingAnim.repeatCount = Animation.INFINITE
        operatingAnim.duration = 2000
        
        val lin = LinearInterpolator()
        operatingAnim.interpolator = lin
        
        mainDialog?.window?.decorView?.let { view ->
            background = view.findViewById(R.id.background)

            if (color != 0) {
                val unwrappedDrawable = AppCompatResources.getDrawable(view.context, R.drawable.background)

                unwrappedDrawable?.let {
                    val wrappedDrawable = DrawableCompat.wrap(it)
                    DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(view.context, color))
                    background.background = wrappedDrawable
                }
            }
            
            leaf = view.findViewById(R.id.leaf)
            graduallyTextView = view.findViewById<View>(R.id.graduallyTextView) as GraduallyTextView

            if (!TextUtils.isEmpty(viewText)) {
                graduallyTextView.setText(viewText)
            }
        }

        return mainDialog!!
    }

    override fun onResume() {
        leaf.animation = operatingAnim
        graduallyTextView.startLoading()
        super.onResume()
    }

    override fun onPause() {
        leaf.clearAnimation()
        graduallyTextView.stopLoading()
        super.onPause()
    }

    override fun onDestroyView() {
        if (mainDialog?.isShowing == true) {
            mainDialog?.dismiss()
            mainDialog = null
        }
        super.onDestroyView()
    }

    fun setText(labelText: String?) {
        viewText = labelText
    }

    fun setClickCancelAble(flag: Boolean) {
        this.isCancelable = flag
    }

    fun setBackgroundColor(color: Int) {
        this.color = color
    }
}