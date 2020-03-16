package rafael.logistic.view.experimental.template

import javafx.geometry.Point2D
import javafx.scene.control.CheckBox
import javafx.scene.control.Spinner
import rafael.logistic.view.addCopyCapacity
import rafael.logistic.view.configureMinMaxSpinners
import rafael.logistic.view.doubleSpinnerValueFactory
import rafael.logistic.view.mapchart.MouseRealPosNode
import rafael.logistic.view.view.ViewBi
import tornadofx.*

class TemplateView : ViewBi<TemplateGenerator>("Template", "Template", TemplateGenerator()) {

    // @formatter:off
    private val chbPinY             :   CheckBox            by  fxid()

    private val txtMouseRealPos     :   MouseRealPosNode    by fxid()

    private val spnXMin             :   Spinner<Double>     by fxid()
    private val xMinValueFactory    =   doubleSpinnerValueFactory(chart.xMin, chart.xMax, chart.xMin, 0.1)

    private val spnXMax             :   Spinner<Double>     by fxid()
    private val xMaxValueFactory    =   doubleSpinnerValueFactory(chart.xMin, chart.xMax, chart.xMax, 0.1)

    private val spnYMin             :   Spinner<Double>     by fxid()
    private val yMinValueFactory    =   doubleSpinnerValueFactory(chart.yMin, chart.yMax, chart.yMin, 0.1)

    private val spnYMax             :   Spinner<Double>     by fxid()
    private val yMaxValueFactory    =   doubleSpinnerValueFactory(chart.yMin, chart.yMax, chart.yMax, 0.1)

    private     val deltaXLimitProperty =   1.toProperty()
    private     val deltaXStepProperty  =   (0.1).toProperty()

    private     val deltaYLimitProperty =   1.toProperty()
    private     val deltaYStepProperty  =   (0.1).toProperty()

    // @formatter:on

    override fun initializeControlsBi() {
        spnY0.disableWhen { chbPinY.selectedProperty() }
        spnY0.disableProperty().onChange { disable ->
            if (disable) {
                spnY0.valueFactory.value = 0.0
            } else {
                spnY0.requestFocus()
            }
        }

        configureMinMaxSpinners(spnXMin, xMinValueFactory, spnXMax, xMaxValueFactory,
                deltaXLimitProperty, deltaXStepProperty, this::loadData)
        configureMinMaxSpinners(spnYMin, yMinValueFactory, spnYMax, yMaxValueFactory,
                deltaYLimitProperty, deltaYStepProperty, this::loadData)

        spnXMin.addCopyCapacity()
        spnXMax.addCopyCapacity()
        spnYMin.addCopyCapacity()
        spnYMax.addCopyCapacity()
    }

    override fun initializeCharts() {
        super.initializeCharts()

        chart.xAxis.widthProperty().onChange { loadData() }
        chart.xMinProperty.bind(spnXMin.valueProperty())
        chart.xMaxProperty.bind(spnXMax.valueProperty())

        chart.yAxis.widthProperty().onChange { loadData() }
        chart.yMinProperty.bind(spnYMin.valueProperty())
        chart.yMaxProperty.bind(spnYMax.valueProperty())
    }

    override fun refreshData(generator: TemplateGenerator, iterations: Int): List<Point2D> =
            generator.generate(Point2D(x0Property.value, y0Property.value), super.minX0Spinner, super.maxX0Spinner, iterations)

    override fun initializeAdditional() {
        txtMouseRealPos.bind(chart)
    }

}
