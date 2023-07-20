package you.thiago.koalaloadingview

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.thiago.catloadingview.R
import you.thiago.koalaloadinglibrary.KoalaLoadingView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.button).setOnClickListener {
            KoalaLoadingView().show(supportFragmentManager, "")
        }
    }
}