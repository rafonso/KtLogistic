package rafael.experimental.template

import javafx.scene.control.CheckBox
import rafael.logistic.core.fx.*
import rafael.logistic.core.fx.mapchart.MouseRealPosNode
import rafael.logistic.core.generation.BiDouble
import rafael.logistic.map.fx.view.ViewBi
import tornadofx.App
import tornadofx.disableWhen
import tornadofx.onChange

class TemplateApp: App(TemplateView::class, Styles::class)

class TemplateView : ViewBi<TemplateGenerator>("Template", "Template", TemplateGenerator()) {

    // @formatter:off
    private val chbPinY             :   CheckBox            by  fxid()

    private val txtMouseRealPos     :   MouseRealPosNode    by fxid()

    private val spnXMin             :   DoubleSpinner     by fxid()
    private val spnXMax             :   DoubleSpinner     by fxid()
    private val spnYMin             :   DoubleSpinner     by fxid()
    private val spnYMax             :   DoubleSpinner     by fxid()

    override    val spinnerComponents   = emptyArray<SpinnerConfigurations>()

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

        configureMinMaxSpinners(LimitsSpinnersConfiguration(spnXMin, spnXMax, chart.xMin, chart.xMax), this::loadData)
        configureMinMaxSpinners(LimitsSpinnersConfiguration(spnYMin, spnYMax, chart.yMin, chart.yMax), this::loadData)
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

    override fun refreshData(generator: TemplateGenerator, iterations: Int): List<BiDouble> =
            generator.generate(BiDouble(x0Property.value, y0Property.value), super.minX0Spinner, super.maxX0Spinner, iterations)

    override fun initializeAdditional() {
        txtMouseRealPos.bind(chart)
    }

    override fun getImageName1(): String = "template" +
            ".XMin=${spnXMin.valueToString()}.XMax=${spnXMax.valueToString()}" +
            ".YMin=${spnYMin.valueToString()}.YMax=${spnYMax.valueToString()}"

}
