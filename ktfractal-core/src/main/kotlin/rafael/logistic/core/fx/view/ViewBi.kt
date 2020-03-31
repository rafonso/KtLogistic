package rafael.logistic.core.fx.view

import javafx.beans.binding.Bindings
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.scene.chart.NumberAxis
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.layout.Region
import rafael.logistic.core.fx.configureActions
import rafael.logistic.core.fx.iterationchart.IterationChartBi
import rafael.logistic.core.fx.mapchart.MapChartBi
import rafael.logistic.core.fx.valueToString
import rafael.logistic.core.generation.BiDouble
import rafael.logistic.core.generation.IterationGenerator
import tornadofx.toProperty

abstract class ViewBi<G : IterationGenerator<*, BiDouble, *>>(title: String, fxmlFile: String, generator: G) :
        ViewBase<BiDouble, G, MapChartBi>(title, fxmlFile, generator) {

    // @formatter:off
    private         val spnX0               :   Spinner<Double>     by  fxid()
    protected       val spnY0               :   Spinner<Double>     by  fxid()

    private         val xIterationsChart    :   IterationChartBi    by  fxid()
    private         val yIterationsChart    :   IterationChartBi    by  fxid()

    protected open  val maxDelta            =   0.1
    protected open  val iniX0Spinner        =   0.0
    protected open  val minX0Spinner        =   (chart.xAxis as NumberAxis).lowerBound
    protected open  val maxX0Spinner        =   (chart.xAxis as NumberAxis).upperBound
    protected open  val iniY0Spinner        =   0.0
    protected open  val minY0Spinner        =   (chart.yAxis as NumberAxis).lowerBound
    protected open  val maxY0Spinner        =   (chart.yAxis as NumberAxis).upperBound

    private         val deltaX0Property     =   1.toProperty()
    private         val x0ValueFactory      =   SpinnerValueFactory.DoubleSpinnerValueFactory(minX0Spinner, maxX0Spinner, iniX0Spinner, maxDelta)
    protected       val x0Property:ReadOnlyObjectProperty<Double>   =   spnX0.valueProperty()

    private         val deltaY0Property     =   1.toProperty()
    private         val y0ValueFactory      =   SpinnerValueFactory.DoubleSpinnerValueFactory(minY0Spinner, maxY0Spinner, iniY0Spinner, maxDelta)
    protected       val y0Property:ReadOnlyObjectProperty<Double>   =   spnY0.valueProperty()
    // @formatter:on

    override fun initializeControls() {
        spnX0.configureActions(x0ValueFactory, deltaX0Property, this::loadData)
        spnY0.configureActions(y0ValueFactory, deltaY0Property, this::loadData)
        initializeControlsBi()
    }

    override fun initializeCharts() {
        val chartParent = chart.parent as Region
        chart.prefWidthProperty().bind(Bindings.min(chartParent.heightProperty(), chartParent.widthProperty()))
        xIterationsChart.bind(spnIterations.valueProperty(), logisticData, IterationChartBi.extractorX)
        yIterationsChart.bind(spnIterations.valueProperty(), logisticData, IterationChartBi.extractorY)
    }

    protected open fun initializeControlsBi() {

    }

    override fun getImageName(): String =
        "${getImageName1()}.X0=${spnX0.valueToString()}.Y0=${spnX0.valueToString()}.Iterations=${spnIterations.value}"

    protected abstract fun getImageName1(): String

}
