package rafael.logistic.maps.gaussian

import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import rafael.logistic.generator.EndingEvent
import rafael.logistic.generator.IterationEvent
import rafael.logistic.generator.RunningEvent
import rafael.logistic.generator.StartingEvent
import rafael.logistic.view.IteractionChart
import rafael.logistic.view.ViewBase
import rafael.logistic.view.configureActions
import java.time.Duration
import java.time.Instant

private const val MAX_DELTA = 0.1

class GaussianView : ViewBase<Double, GaussianGenerator>("Gaussian Equation", "Gaussian", GaussianGenerator()) {

    // @formatter:off
    private  val spnAlpha           :   Spinner<Double>   by fxid()
    private  val spnBeta            :   Spinner<Double>   by fxid()
    private  val spnX0              :   Spinner<Double>   by fxid()

    private  val gaussianChart      :   GaussianChart     by fxid()
    private  val iteractionsChart   :   IteractionChart   by fxid()

    private val deltaAlphaProperty  =   SimpleIntegerProperty(this, "deltaAlpha"    , 1     )
    private val alphaValueFactory   =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 10.0, 1.0, MAX_DELTA)

    private val deltaBetaProperty   =   SimpleIntegerProperty(this, "deltaBeta"    , 1     )
    private val betaValueFactory    =   SpinnerValueFactory.DoubleSpinnerValueFactory(-1.0, 0.0, 0.0, MAX_DELTA)

    private val deltaX0Property     =   SimpleIntegerProperty(this, "deltaX0"   , 1     )
    private val x0ValueFactory      =   SpinnerValueFactory.DoubleSpinnerValueFactory(-1.0, 1.0, 0.0, MAX_DELTA)

    private var t0: Instant?            =   null
    // @formatter:on

    private fun dataGenerated(event: IterationEvent<Double>) {
        when (event) {
            is StartingEvent -> t0 = event.instant
            is RunningEvent  -> {

            }
            is EndingEvent   -> {
                val t1 = Instant.now()
                val deltaT = Duration.between(t0, t1)
            }
        }
    }

    override fun initializeControls() {
        spnAlpha.configureActions(alphaValueFactory, deltaAlphaProperty, this::loadData)
        spnBeta.configureActions(betaValueFactory, deltaBetaProperty, this::loadData)
        spnX0.configureActions(x0ValueFactory, deltaX0Property, this::loadData)
    }

    override fun initializeCharts() {
        gaussianChart.bind(logisticData) {
            when(it) {
                is GaussianChart -> {
                    it.alphaProperty.bind(spnAlpha.valueProperty())
                    it.betaProperty.bind(spnBeta.valueProperty())
                }
            }
        }
        iteractionsChart.bind(spnIteractions.valueProperty(), logisticData)
    }

    override fun refreshData(generator: GaussianGenerator, iterations: Int): List<Double> =
            generator.generate(spnX0.value, spnAlpha.value, spnBeta.value, iterations)

    override fun initializeAdditional() {
        generator.addStatusListener(this::dataGenerated)
    }
}
