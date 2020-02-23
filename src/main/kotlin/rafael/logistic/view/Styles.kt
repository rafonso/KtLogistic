package rafael.logistic.view

import javafx.scene.paint.Color
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
//        val point0 by cssclass()
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
    }
}
