@file:Suppress("LeakingThis")

package rafael.logistic.map.fx.view

import javafx.beans.binding.Bindings
import javafx.beans.property.DoubleProperty
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.scene.chart.NumberAxis
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.layout.Region
import rafael.logistic.core.fx.DoubleSpinner
import rafael.logistic.core.fx.oneProperty
import rafael.logistic.core.fx.valueToString
import rafael.logistic.core.fx.view.ViewBase
import rafael.logistic.core.generation.BiDouble
import rafael.logistic.core.generation.IterationGenerator
import rafael.logistic.map.fx.iterationchart.IterationChartBi
import rafael.logistic.map.fx.mapchart.MapChartBi

abstract class ViewBi<G : IterationGenerator<*, BiDouble, *>>(title: String, fxmlFile: String, generator: G) :
        ViewBase<BiDouble, G, MapChartBi>(title, fxmlFile, generator) {

    // @formatter:off
    private         val spnX0               :   DoubleSpinner     by  fxid()
    protected       val spnY0               :   DoubleSpinner     by  fxid()

    private         val xIterationsChart    :   IterationChartBi    by  fxid()
    private         val yIterationsChart    :   IterationChartBi    by  fxid()

    protected       val xIterationsYAxis    =   (xIterationsChart.yAxis as NumberAxis)
    protected       val yIterationsYAxis    =   (yIterationsChart.yAxis as NumberAxis)

    protected open  val maxDelta            =   0.1
    protected open  val iniX0Spinner        =   0.0
    protected open  val minX0Spinner        =   (chart.xAxis as NumberAxis).lowerBound
    protected open  val maxX0Spinner        =   (chart.xAxis as NumberAxis).upperBound
    protected open  val iniY0Spinner        =   0.0
    protected open  val minY0Spinner        =   (chart.yAxis as NumberAxis).lowerBound
    protected open  val maxY0Spinner        =   (chart.yAxis as NumberAxis).upperBound

    private         val deltaX0Property     =   oneProperty()
    private         val x0ValueFactory      =   SpinnerValueFactory.DoubleSpinnerValueFactory(minX0Spinner, maxX0Spinner, iniX0Spinner, maxDelta)
    protected       val x0Property          :   ReadOnlyObjectProperty<Double>   =   spnX0.valueProperty()

    private         val deltaY0Property     =   oneProperty()
    private         val y0ValueFactory      =   SpinnerValueFactory.DoubleSpinnerValueFactory(minY0Spinner, maxY0Spinner, iniY0Spinner, maxDelta)
    protected       val y0Property          :   ReadOnlyObjectProperty<Double>   =   spnY0.valueProperty()

    override        val spinnersChartProperties =   emptyArray<Pair<DoubleSpinner, DoubleProperty>>()

    // @formatter:on

    override fun initializeControls() {
        spnX0.configureSpinner(x0ValueFactory, deltaX0Property)
        spnY0.configureSpinner(y0ValueFactory, deltaY0Property)
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
