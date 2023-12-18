package sk.ukf.balloonsapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

class GameCanvas (context: Context) : View(context) {

    private val paint = Paint()
    private lateinit var background: Bitmap
    private val balloons = mutableListOf<Balloon>()
    private var score = 0
    private var clicks = 0
    private var screenX = 0f
    private var screenY = 0f
    private val colors = intArrayOf(Color.BLUE, Color.YELLOW, Color.GREEN, Color.RED, Color.WHITE)

    init {
        setBackground()
    }

    fun startMyAnimation(speed: Long) {
        val animator = object : Runnable {
            override fun run() {
                update()
                invalidate()
                postDelayed(this, speed)
            }
        }
        post(animator)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(background, 0f, 0f, paint)

        paint.color = Color.WHITE
        paint.textSize = 75f
        canvas.drawText("Skóre: $score", 10f, 85f, paint)
        canvas.drawText("Počet klikov: $clicks", 10f, 170f, paint)

        for (balloon in balloons) {
            paint.color = balloon.getColor()
            canvas.drawCircle(balloon.getX(), balloon.getY(), 60f, paint)
        }
    }

    fun update() {
        if (balloons.size < 20 && Random.nextDouble() > 0.9) {
            val balloon = Balloon(
                (70 + Random.nextDouble() * (screenX - 140)).toFloat(),
                screenY,
                colors[Random.nextInt(colors.size)],
                (1 + Random.nextDouble() * 10).toInt()
            )
            balloons.add(balloon)
        }

        if (balloons.isNotEmpty()) {
            val iterator = balloons.listIterator(balloons.size)
            while (iterator.hasPrevious()) {
                val balloon = iterator.previous()
                balloon.flyUP()

                if (balloon.getY() < 20)
                    iterator.remove()
            }
        }
    }

    private fun setBackground() {
        val originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.background)

        val screenWidth = resources.displayMetrics.widthPixels
        val screenHeight = resources.displayMetrics.heightPixels

        background = Bitmap.createScaledBitmap(originalBitmap, screenWidth, screenHeight, true)
        screenX = screenWidth.toFloat()
        screenY = screenHeight.toFloat()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val x = event.x
            val y = event.y

            val iterator = balloons.listIterator(balloons.size)
            while (iterator.hasPrevious()) {
                val balloon = iterator.previous()
                if (isClose(x, y, balloon.getX(), balloon.getY())) {
                    iterator.remove()
                    invalidate()
                    score++
                }
            }
            clicks++
        }
        return true
    }

    private fun isClose(x: Float, y: Float, x2: Float, y2: Float): Boolean {
        return (sqrt((x - x2).toDouble().pow(2.0) + (y - y2).toDouble().pow(2.0))) < 70
    }
}