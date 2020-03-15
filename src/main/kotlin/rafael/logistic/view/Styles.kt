package rafael.logistic.view

import javafx.geometry.Pos
import javafx.scene.paint.Color
import javafx.scene.text.Font
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val title by cssclass()
        val mouseRealPosNode by csselement("MouseRealPosNode")
    }

    init {
        spinner and focused {
            textInput {
                backgroundColor += Color.KHAKI
            }
        }
        chartPlotBackground {
            backgroundColor += Color.WHITE
        }
        label and title {
            alignment = Pos.CENTER_RIGHT
            minWidth = 75.px
        }
        mouseRealPosNode {
            font = Font.font("Consolas", 10.0)
            insets(5.0)
            prefWidth = 200.px
            maxWidth = 200.px
        }
    }
}
