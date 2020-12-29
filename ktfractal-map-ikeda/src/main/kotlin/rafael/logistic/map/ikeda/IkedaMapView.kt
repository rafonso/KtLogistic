package rafael.logistic.map.ikeda

import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.spinners.DoubleSpinner
import rafael.logistic.core.fx.spinners.valueToString
import rafael.logistic.core.generation.BiDouble
import rafael.logistic.map.fx.view.ViewBi
import tornadofx.*

private const val U_CHANGE_SCALE_VALUE = 0.9

private enum class PlotBounds(
    val xLowerBound: Double,
    val xUpperBound: Double,
    val yLowerBound: Double,
    val yUpperBound: Double
) {
    ZOOM_IN(-2.0, +2.0, -2.0, +2.0),
    ZOOM_OUT(-11.0, +11.0, -11.0, +11.0);

    companion object {
        fun valueOf(u: Double) = if (u < U_CHANGE_SCALE_VALUE) ZOOM_IN else ZOOM_OUT
    }

}

class IkedaMapApp : App(IkedaMapView::class, Styles::class)

class IkedaMapView : ViewBi<IkedaMapGenerator>("Ikeda Map", "IkedaMap", IkedaMapGenerator()) {

    // @formatter:off

    private     val spnU                :   DoubleSpinner by fxid()

    override    val spinnerComponents   =   arrayOf(SpinnerConfigurations(spnU, U_MIN, U_MAX, (U_MIN + U_MAX) / 2))

    // @formatter:on

    private fun changeScale(newU: Double) {
        val plotBounds = PlotBounds.valueOf(newU)

        super.chart.xMinProperty.value = plotBounds.xLowerBound
        super.chart.xMaxProperty.value = plotBounds.xUpperBound
        super.chart.yMinProperty.value = plotBounds.yLowerBound
        super.chart.yMaxProperty.value = plotBounds.yUpperBound

        super.xIterationsYAxis.lowerBound = plotBounds.xLowerBound
        super.xIterationsYAxis.upperBound = plotBounds.xUpperBound
        super.yIterationsYAxis.lowerBound = plotBounds.yLowerBound
        super.yIterationsYAxis.upperBound = plotBounds.yUpperBound
    }

    override fun refreshData(generator: IkedaMapGenerator, iterations: Int): List<BiDouble> =
        generator.generate(BiDouble(x0Property.value, y0Property.value), spnU.value, iterations)

    override fun initializeControlsBi() {
        spnU.valueProperty().addListener { _, oldU, newU ->
            if(oldU != null) {
                val goingToZoomOut = oldU < U_CHANGE_SCALE_VALUE && newU >= U_CHANGE_SCALE_VALUE
                val goingToZoomInn = oldU >= U_CHANGE_SCALE_VALUE && newU < U_CHANGE_SCALE_VALUE
                if (goingToZoomOut || goingToZoomInn) {
                    changeScale(newU)
                }
            }
        }
    }

    override fun getImageName1(): String = "ikeda.U=${spnU.valueToString()}"

}
