package rafael.logistic.maps.tinkerbell

import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import rafael.logistic.generator.BiPoint
import rafael.logistic.view.IteractionChartBi
import rafael.logistic.view.MapChartBi
import rafael.logistic.view.ViewBase
import rafael.logistic.view.configureActions

private const val MAX_DELTA = 0.1

private const val MAX_X = 1.5

class TinkerbellView : ViewBase<BiPoint, TinkerbellGenerator>("Tinkerbell", "Tinkerbell", TinkerbellGenerator()) {

    // @formatter:off
    private  val spnA               :   Spinner<Double>   by fxid()
    private  val spnB               :   Spinner<Double>   by fxid()
    private  val spnC               :   Spinner<Double>   by fxid()
    private  val spnD               :   Spinner<Double>   by fxid()
    private  val spnX0              :   Spinner<Double>   by fxid()
    private  val spnY0              :   Spinner<Double>   by fxid()

    private  val chart              :   MapChartBi        by fxid()
    private  val xIterationsChart   :   IteractionChartBi by fxid()
    private  val yIterationsChart   :   IteractionChartBi by fxid()

    private val deltaAProperty      =   SimpleIntegerProperty(this, "deltaAlpha"    , 1     )
    private val aValueFactory       =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1.0, 0.5, MAX_DELTA)

    private val deltaBProperty      =   SimpleIntegerProperty(this, "deltaBeta"    , 1     )
    private val bValueFactory       =   SpinnerValueFactory.DoubleSpinnerValueFactory(-1.0, 1.0, 0.6, MAX_DELTA)

    private val deltaCProperty      =   SimpleIntegerProperty(this, "deltaC"    , 1     )
    private val cValueFactory       =   SpinnerValueFactory.DoubleSpinnerValueFactory(-4.0, 4.0, 2.0, MAX_DELTA)

    private val deltaDProperty      =   SimpleIntegerProperty(this, "deltaD"    , 1     )
    private val dValueFactory       =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1.0, 0.5, MAX_DELTA)

    private val deltaX0Property     =   SimpleIntegerProperty(this, "deltaX0"   , 1     )
    private val x0ValueFactory      =   SpinnerValueFactory.DoubleSpinnerValueFactory(-MAX_X, MAX_X, 0.0, MAX_DELTA)

    private val deltaY0Property     =   SimpleIntegerProperty(this, "deltaY0"   , 1     )
    private val y0ValueFactory      =   SpinnerValueFactory.DoubleSpinnerValueFactory(- 2 * MAX_X / 3, 2 * MAX_X / 3 , 0.0, MAX_DELTA)

    // @formatter:on

    override fun initializeControls() {
        spnA.configureActions(aValueFactory, deltaAProperty, this::loadData)
        spnB.configureActions(bValueFactory, deltaBProperty, this::loadData)
        spnC.configureActions(cValueFactory, deltaCProperty, this::loadData)
        spnD.configureActions(dValueFactory, deltaDProperty, this::loadData)
        spnX0.configureActions(x0ValueFactory, deltaX0Property, this::loadData)
        spnY0.configureActions(y0ValueFactory, deltaY0Property, this::loadData)
    }

    override fun initializeCharts() {
        chart.bind(logisticData)
        xIterationsChart.bind(spnIteractions.valueProperty(), logisticData, IteractionChartBi.extractorX)
        yIterationsChart.bind(spnIteractions.valueProperty(), logisticData, IteractionChartBi.extractorY)
    }

    override fun refreshData(generator: TinkerbellGenerator, iterations: Int): List<BiPoint> =
            generator.generate(BiPoint(spnX0.value, spnY0.value), spnA.value, spnB.value, spnC.value, spnD.value, iterations)

}
