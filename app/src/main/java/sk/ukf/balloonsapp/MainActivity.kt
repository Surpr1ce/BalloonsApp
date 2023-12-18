package sk.ukf.balloonsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val canvas = GameCanvas(this)
        setContentView(canvas)
        canvas.startMyAnimation(30)
    }
}