package rafael.logistic.map.fx.view

import javafx.beans.binding.Bindings
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.scene.chart.NumberAxis
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.input.KeyCode
import javafx.scene.layout.Region
import rafael.logistic.map.fx.iterationchart.IterationChartDouble
import rafael.logistic.core.fx.oneProperty
import rafael.logistic.core.fx.valueToString
import rafael.logistic.core.fx.view.ViewBase
import rafael.logistic.core.generation.IterationGenerator
import rafael.logistic.map.fx.mapchart.MapChartDouble

abstract class ViewDouble<G : IterationGenerator<*, Double, *>, C : MapChartDouble>(title: String, fxmlFile: String, generator: G) :
        ViewBase<Double, G, C>(title, fxmlFile, generator) {

    protected val maxDelta = 0.1

    private val iniX0Spinner = 0.5

    // @formatter:off
    private     val iterationsChart     :   IterationChartDouble by fxid()

    private     val spnX0               :   Spinner<Double> by fxid()
    private     val deltaX0Property     =   oneProperty()
    private     val x0ValueFactory      =   SpinnerValueFactory.DoubleSpinnerValueFactory(
            (chart.xAxis as NumberAxis).lowerBound, (chart.xAxis as NumberAxis).upperBound, iniX0Spinner, maxDelta)
    protected   val x0Property:ReadOnlyObjectProperty<Double>   =   spnX0.valueProperty()

    // @formatter:on

    override fun initializeControls() {
        spnX0.configureSpinner(x0ValueFactory, deltaX0Property)
    }

    override fun initializeCharts() {
        val chartParent = chart.parent as Region
        chart.prefWidthProperty().bind(Bindings.min(chartParent.heightProperty(), chartParent.widthProperty()))
        iterationsChart.bind(spnIterations.valueProperty(), logisticData)
//        chart.square.xProperty.asObject().bindBidirectional(x0ValueFactory.valueProperty()) // as Property<Number>)
        super.root.setOnKeyPressed { event ->
            if (event.isControlDown && event.code == KeyCode.S) {
                exportImage()
            }
        }
    }

    override fun getImageName(): String =
        "${getImageName1()}.X0=${spnX0.valueToString()}.Iterations=${spnIterations.value}"


    protected abstract fun getImageName1(): String

}
