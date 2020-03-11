package rafael.logistic.view

import javafx.geometry.Pos
import javafx.scene.paint.Color
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val title by cssclass()
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
    }
}
