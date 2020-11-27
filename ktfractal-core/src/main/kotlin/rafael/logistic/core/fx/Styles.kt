package rafael.logistic.core.fx

import javafx.geometry.Pos
import javafx.scene.paint.Color
import javafx.scene.text.Font
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val title by cssclass()
        val mouseRealPosNode by csselement("MouseRealPosNode")
        val spnDouble by cssclass("spnDouble")
        val spnIterations by cssid("spnIterations")
        val spnInteger by cssclass("spnInteger")
        val lblStatus by cssclass("status")
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
            padding = box(0.px, 5.px, 0.px, 0.px)
        }
        mouseRealPosNode {
            font = Font.font("Consolas", 10.0)
            insets(5.0)
            prefWidth = 200.px
            maxWidth = 200.px
        }
        spinner and spnDouble {
            prefWidth = 120.px
        }
        spinner and spnIterations {
            prefWidth = 100.px
        }
        spinner and spnInteger {
            prefWidth = 60.px
        }
        label and lblStatus {
            prefWidth = 200.px
        }
    }
}
