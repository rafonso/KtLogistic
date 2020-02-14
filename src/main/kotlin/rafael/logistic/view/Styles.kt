package rafael.logistic.view

import javafx.scene.text.FontWeight
import tornadofx.Stylesheet
import tornadofx.box
import tornadofx.cssclass
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val heading by cssclass()
        val parable by cssclass()
        val series0 by cssclass()
    }

    init {
        label and heading {
            padding = box(10.px)
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
        }
        parable {
            stroke = c("violet")
            strokeWidth  = 2.px
        }
        series0 {
            stroke = c("gold")
            strokeWidth  = 2.px
        }
    }
}