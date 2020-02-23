package rafael.logistic.maps.baker

import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import rafael.logistic.generator.BiPoint
import rafael.logistic.view.IteractionChartBi
import rafael.logistic.view.MapChartBi
import rafael.logistic.view.ViewBase
import rafael.logistic.view.configureActions

private const val MAX_DELTA = 0.1

private const val MAX_X = 1.0

class BakerView : ViewBase<BiPoint, BakerGenerator>("Baker", "Baker", BakerGenerator()) {

    // @formatter:off
    private  val spnX0              : Spinner<Double>   by fxid()
    private  val spnY0              : Spinner<Double>   by fxid()

    private  val chart              : MapChartBi        by fxid()
    private  val xIterationsChart   : IteractionChartBi by fxid()
    private  val yIterationsChart   : IteractionChartBi by fxid()

    private val deltaX0Property         =   SimpleIntegerProperty(this, "deltaX0"   , 1     )
    private val x0ValueFactory          =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, MAX_X, 0.0, MAX_DELTA)

    private val deltaY0Property         =   SimpleIntegerProperty(this, "deltaY0"   , 1     )
    private val y0ValueFactory          =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, MAX_X, 0.0, MAX_DELTA)
    // @formatter:on

    override fun initializeControls() {
        spnX0.configureActions(x0ValueFactory, deltaX0Property, this::loadData)
        spnY0.configureActions(y0ValueFactory, deltaY0Property, this::loadData)
    }

    override fun initializeCharts() {
        chart.bind(logisticData)
        xIterationsChart.bind(spnIteractions.valueProperty(), logisticData, IteractionChartBi.extractorX)
        yIterationsChart.bind(spnIteractions.valueProperty(), logisticData, IteractionChartBi.extractorY)
    }

    override fun refreshData(generator: BakerGenerator, iterations: Int): List<BiPoint> =
            generator.generate(BiPoint(spnX0.value, spnY0.value), iterations)

}
