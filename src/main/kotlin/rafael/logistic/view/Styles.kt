package rafael.logistic.view

import javafx.scene.paint.Color
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
    }

    init {
        spinner and focused {
            textInput {
                backgroundColor += Color.KHAKI
            }
        }
    }
}