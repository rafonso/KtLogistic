package rafael.logistic.view

import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.layout.BorderPane
import tornadofx.*

abstract class ViewBase(title: String, fxmlFile: String): View(title) {

    // @formatter:off
    override val root               : BorderPane        by fxml("/$fxmlFile.fxml")
    private  val spnIteractions     : Spinner<Int>      by fxid()
    private val iteractionsValueFactory =   SpinnerValueFactory.IntegerSpinnerValueFactory(50, 2000, 100, 50)
    // @formatter:on


}
