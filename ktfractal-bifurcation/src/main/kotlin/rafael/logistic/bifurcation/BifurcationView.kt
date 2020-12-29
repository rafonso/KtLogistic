package rafael.logistic.bifurcation

import javafx.beans.property.ReadOnlyIntegerProperty
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.scene.control.Label
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.layout.Region
import rafael.logistic.core.fx.mapchart.MouseRealPosNode
import rafael.logistic.core.fx.spinners.DoubleSpinner
import rafael.logistic.core.fx.spinners.IntSpinner
import rafael.logistic.core.fx.spinners.LimitsSpinnersConfiguration
import rafael.logistic.core.fx.spinners.configureMinMaxSpinners
import rafael.logistic.core.fx.view.ViewBase
import rafael.logistic.core.generation.GenerationStatus
import tornadofx.*

abstract class BifurcationView<G : BifurcationGenerator<*>> protected constructor(
    title: String,
    fxmlFile: String,
    generator: G
) :
    ViewBase<RData, G, BifurcationCanvas>(title, fxmlFile, generator) {

    // @formatter:off

    private val spnSkip                         : IntSpinner        by fxid()
    private val skipValueFactory                = SpinnerValueFactory.IntegerSpinnerValueFactory(0, 60, 0, 1)

    private val spnPixelsSeparation             : IntSpinner        by fxid()
    private val pixelsSeparationValueFactory    =
        SpinnerValueFactory.ListSpinnerValueFactory(observableListOf(0, 1, 2, 4, 10, 50, 100))
    private val pixelsSeparation                : Int
        get() = spnPixelsSeparation.value

    private val lblPosMouse                     : MouseRealPosNode  by fxid()

    private val lblStatus                       : Label             by fxid()

    protected abstract val spnX0Axis            : DoubleSpinner

    protected abstract val xAxisConfiguration   : LimitsSpinnersConfiguration

    protected abstract val yAxisConfiguration   : LimitsSpinnersConfiguration

    override            val spinnersChartProperties by lazy { arrayOf(
        Pair(spnX0Axis                , chart.x0Property),
        Pair(xAxisConfiguration.spnMin, chart.xMinProperty),
        Pair(xAxisConfiguration.spnMax, chart.xMaxProperty),
        Pair(yAxisConfiguration.spnMin, chart.yMinProperty),
        Pair(yAxisConfiguration.spnMax, chart.yMaxProperty),
    ) }

    private             val firstIterationProperty  =
            ReadOnlyIntegerProperty.readOnlyIntegerProperty(super.iterationsProperty) *
            ReadOnlyIntegerProperty.readOnlyIntegerProperty(spnSkip.valueProperty()) / 100

    // @formatter:on

    protected abstract fun getParametersName(iterations: Int): String

    override fun getImageName(iterations: Int) = getParametersName(iterations) +
            (if (spnSkip.value > 0) ".Skip=${spnSkip.value}pct" else "") +
            (if (pixelsSeparation > 0) ".PxnSep=${pixelsSeparation}" else "")

    override fun initializeControls() {
        spnSkip.configureActions(skipValueFactory, this::loadData)

        configureMinMaxSpinners(xAxisConfiguration, this::loadData)
        configureMinMaxSpinners(yAxisConfiguration) { this.loadData(false) }

        pixelsSeparationValueFactory.value = 0
        spnPixelsSeparation.configureActions(pixelsSeparationValueFactory) {
            chart.pixelsSeparationProperty.value = spnPixelsSeparation.value + 1
            this.loadData()
        }
    }

    override fun initializeAdditional() {
        lblPosMouse.bind(chart)
        super.generationStatusProperty().onChange {
            runLater {
                it?.let { status ->
                    lblStatus.text = if (status == GenerationStatus.IDLE) "" else status.toString()
                }
            }
        }
    }

    override fun initializeCharts(iterationsProperty: ReadOnlyObjectProperty<Int>) {
        val chartParent = chart.parent as Region
        chart.widthProperty().bind(chartParent.widthProperty())
        chart.widthProperty().onChange { loadData() }
        chart.heightProperty().bind(chartParent.heightProperty())
        chart.heightProperty().onChange { loadData() }

        chart.iterationsProperty.bind(iterationsProperty)
        chart.firstIterationProperty.bind(firstIterationProperty)
    }

    protected abstract fun refreshData(generator: G, iterations: Int, stepsForR: Int, firstIteration: Int): List<RData>

    override fun refreshData(generator: G, iterations: Int): List<RData> =
        if (chart.width > 0) refreshData(
            generator,
            iterations,
            super.chart.widthProperty().value.toInt() / (pixelsSeparation + 1),
            firstIterationProperty.value
        )
        else emptyList()

}
