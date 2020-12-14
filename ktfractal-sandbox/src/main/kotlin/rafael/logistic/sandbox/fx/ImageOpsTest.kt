package rafael.logistic.sandbox.fx

import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.effect.DropShadow
import javafx.scene.image.PixelFormat
import javafx.scene.paint.Color
import javafx.stage.Stage


class ImageOpsTest : Application() {
    private val imageData = ByteArray(IMAGE_WIDTH * IMAGE_HEIGHT * 3)

    // Drawing Surface (Canvas)
    private var gc: GraphicsContext? = null
    private var canvas: Canvas? = null
    private var root: Group? = null
    override fun start(primaryStage: Stage) {
        primaryStage.title = "PixelWriter Test"
        root = Group()
        canvas = Canvas(200.0, 200.0)
        canvas!!.translateX = 100.0
        canvas!!.translateY = 100.0
        gc = canvas!!.graphicsContext2D
        createImageData()
        drawImageData()
        primaryStage.scene = Scene(root, 400.0, 400.0)
        primaryStage.show()
    }

    private fun createImageData() {
        val c = Color.GREEN
        var i = 0
        for (y in 0 until IMAGE_HEIGHT) {
            for (x in 0 until IMAGE_WIDTH) {
                imageData[i + 0] = (c.red    * 255).toInt().toByte()
                imageData[i + 1] = (c.green  * 255).toInt().toByte()
                imageData[i + 2] = (c.blue   * 255).toInt().toByte()
                i += 3
            }
        }



/*
        var i = 0
        for (y in 0 until IMAGE_HEIGHT) {
            val r = y * 255 / IMAGE_HEIGHT
            for (x in 0 until IMAGE_WIDTH) {
                val g = x * 255 / IMAGE_WIDTH
                imageData[i] = r.toByte()
                imageData[i + 1] = g.toByte()
                i += 3
            }
        }

 */
    }

    private fun drawImageData() {
        var on = true
        val pixelWriter = gc!!.pixelWriter
        val pixelFormat = PixelFormat.getByteRgbInstance()
        var y = 50
        while (y < 150) {
            var x = 50
            while (x < 150) {
                if (on) {
                    pixelWriter.setPixels(
                        x, y, IMAGE_WIDTH,
                        IMAGE_HEIGHT,
                        pixelFormat, imageData,
                        0, IMAGE_WIDTH * 3
                    )
                }
                on = !on
                x += IMAGE_WIDTH
            }
            on = !on
            y += IMAGE_HEIGHT
        }

        // Add drop shadow effect
        gc!!.applyEffect(DropShadow(20.0, 20.0, 20.0, Color.GRAY))
        root!!.children.add(canvas)
    }

    companion object {
        // Image Data
        private const val IMAGE_WIDTH = 10
        private const val IMAGE_HEIGHT = 1
        @JvmStatic
        fun main(args: Array<String>) {
            launch(ImageOpsTest::class.java)
        }
    }
}