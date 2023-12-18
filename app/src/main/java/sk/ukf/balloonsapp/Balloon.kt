package sk.ukf.balloonsapp

class Balloon(
    private val x: Float,
    private var y: Float,
    private val color: Int,
    private val speed: Int
) {
    fun flyUP() {
        y -= speed
    }

    fun getX(): Float = x
    fun getY(): Float = y
    fun getColor(): Int = color
}
