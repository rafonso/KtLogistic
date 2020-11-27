package rafael.logistic.core.fx

import javafx.geometry.Pos
import javafx.scene.layout.BorderStrokeStyle
import javafx.scene.paint.Color
import javafx.scene.text.Font
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val pnlControls by cssclass("controls")
        val title by cssclass()
        val mouseRealPosNode by csselement("MouseRealPosNode")
        val spnDouble by cssclass("spnDouble")
        val spnIterations by cssid("spnIterations")
        val spnInteger by cssclass("spnInteger")
        val lblStatus by cssclass("status")
        val pnlBottom by cssclass("bottom")
        val defaultInsets by cssclass("defaultInsets")
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
//            prefWidth = 100.px
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
            textInput {
                alignment = Pos.CENTER_RIGHT
            }
        }
        spinner and spnInteger {
            prefWidth = 60.px
            textInput {
                alignment = Pos.CENTER_RIGHT
            }
        }
        label and lblStatus {
            prefWidth = 200.px
        }
        pnlBottom {
            hgap = 5.px
            vgap = 5.px
            borderWidth += box(1.px)
            borderStyle = multi(BorderStrokeStyle.DASHED)
            // style="-fx-border-width: 1px; -fx-border-style: dashed;" vgap="5.0"
        }
        defaultInsets {
            padding = box(5.px)
        }
        pnlControls {
            vgap = 5.px
        }
    }
}
