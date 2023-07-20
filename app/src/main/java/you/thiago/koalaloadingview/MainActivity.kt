package you.thiago.koalaloadingview

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.thiago.catloadingview.R
import you.thiago.koalaloadinglibrary.KoalaLoadingDialog
import you.thiago.koalaloadinglibrary.KoalaLoadingView

class MainActivity : AppCompatActivity() {
    
    private val koalaView: KoalaLoadingView by lazy {
        findViewById(R.id.koala_view)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.button).setOnClickListener {
            KoalaLoadingDialog().show(supportFragmentManager, "")
        }

        findViewById<View>(R.id.button2).setOnClickListener {
            koalaView.launch()
        }
    }
}