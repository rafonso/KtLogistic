package rafael.logistic.view

import javafx.beans.property.ReadOnlyObjectProperty
import javafx.scene.chart.NumberAxis
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import rafael.logistic.generator.BiPoint
import rafael.logistic.generator.IterationGenerator
import tornadofx.*

abstract class ViewBi<G : IterationGenerator<BiPoint, *>>(title: String, fxmlFile: String, generator: G) :
        ViewBase<BiPoint, G>(title, fxmlFile, generator) {

    // @formatter:off
    private     val spnX0              :   Spinner<Double>      by fxid()
    private     val spnY0              :   Spinner<Double>      by fxid()

    protected   val chart              :   MapChartBi           by fxid()
    private     val xIterationsChart   :   IteractionChartBi    by fxid()
    private     val yIterationsChart   :   IteractionChartBi    by fxid()

    protected open val maxDelta     = 0.1
    protected open val iniX0Spinner = 0.0
    protected open val minX0Spinner = (chart.xAxis as NumberAxis).lowerBound
    protected open val maxX0Spinner = (chart.xAxis as NumberAxis).upperBound
    protected open val iniY0Spinner = 0.0
    protected open val minY0Spinner = (chart.yAxis as NumberAxis).lowerBound
    protected open val maxY0Spinner = (chart.yAxis as NumberAxis).upperBound

    private     val deltaX0Property     =   1.toProperty()
    private     val x0ValueFactory      =   SpinnerValueFactory.DoubleSpinnerValueFactory(minX0Spinner, maxX0Spinner, iniX0Spinner, maxDelta)
    protected   val x0Property:ReadOnlyObjectProperty<Double>   =   spnX0.valueProperty()

    private     val deltaY0Property      =   1.toProperty()
    private     val y0ValueFactory       =   SpinnerValueFactory.DoubleSpinnerValueFactory(minY0Spinner, maxY0Spinner, iniY0Spinner, maxDelta)
    protected   val y0Property:ReadOnlyObjectProperty<Double>   =   spnY0.valueProperty()
    // @formatter:on


    override fun initializeControls() {
        spnX0.configureActions(x0ValueFactory, deltaX0Property, this::loadData)
        spnY0.configureActions(y0ValueFactory, deltaY0Property, this::loadData)
        initializeControlsBi()
    }

    override fun initializeCharts() {
        chart.bind(logisticData)
        xIterationsChart.bind(spnIteractions.valueProperty(), logisticData, IteractionChartBi.extractorX)
        yIterationsChart.bind(spnIteractions.valueProperty(), logisticData, IteractionChartBi.extractorY)
    }

    protected open fun initializeControlsBi() {

    }



}
