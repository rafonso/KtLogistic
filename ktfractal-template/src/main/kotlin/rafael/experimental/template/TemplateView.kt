package rafael.experimental.template

import javafx.beans.property.ReadOnlyObjectProperty
import javafx.scene.control.CheckBox
import rafael.logistic.core.fx.*
import rafael.logistic.core.fx.mapchart.MouseRealPosNode
import rafael.logistic.core.generation.BiDouble
import rafael.logistic.map.fx.view.ViewBi
import tornadofx.App
import tornadofx.disableWhen
import tornadofx.onChange

class TemplateApp : App(TemplateView::class, Styles::class)

class TemplateView : ViewBi<TemplateGenerator>("Template", "Template", TemplateGenerator()) {

    // @formatter:off
    private val chbPinY             :   CheckBox            by  fxid()

    private val txtMouseRealPos     :   MouseRealPosNode    by fxid()

    private val spnXMin             :   DoubleSpinner     by fxid()
    private val spnXMax             :   DoubleSpinner     by fxid()
    private val spnYMin             :   DoubleSpinner     by fxid()
    private val spnYMax             :   DoubleSpinner     by fxid()

    override    val spinnerComponents   = emptyArray<SpinnerConfigurations>()

    override val spinnersChartProperties    =   arrayOf(
        Pair(spnXMin, chart.xMinProperty),
        Pair(spnXMax, chart.xMaxProperty),
        Pair(spnYMin, chart.yMinProperty),
        Pair(spnYMax, chart.yMaxProperty),
    )

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

    override fun initializeCharts(iterationsProperty: ReadOnlyObjectProperty<Int>) {
        super.initializeCharts(iterationsProperty)

        chart.xAxis.widthProperty().onChange { loadData() }
        chart.yAxis.widthProperty().onChange { loadData() }
    }

    override fun refreshData(generator: TemplateGenerator, iterations: Int): List<BiDouble> =
        generator.generate(
            BiDouble(x0Property.value, y0Property.value),
            super.minX0Spinner,
            super.maxX0Spinner,
            iterations
        )

    override fun initializeAdditional() {
        txtMouseRealPos.bind(chart)
    }

    override fun getImageName1(): String = "template" +
            ".XMin=${spnXMin.valueToString()}.XMax=${spnXMax.valueToString()}" +
            ".YMin=${spnYMin.valueToString()}.YMax=${spnYMax.valueToString()}"

}
