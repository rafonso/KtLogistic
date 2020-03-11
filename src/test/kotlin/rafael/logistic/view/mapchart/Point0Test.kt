package rafael.logistic.view.mapchart

import javafx.beans.property.DoubleProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tornadofx.*

internal class Point0Test {

    @org.junit.jupiter.api.BeforeEach
    fun setUp() {
    }

    private fun createPoint0() = Point0({ xChart -> 2 * xChart }, { xChart -> xChart / 2 }, { yChart -> 3 * yChart }, { yReal -> yReal / 3 })

    private fun assertPoint0(p0: Point0, expectedXChart: Double, expectedXReal: Double, expectedYChart: Double, expectedYReal: Double) {
        assertEquals(expectedXChart, p0.xChart)
        assertEquals(expectedXReal, p0.xReal)
        assertEquals(expectedYChart, p0.yChart)
        assertEquals(expectedYReal, p0.yReal)
    }

    private fun assertListener(prop: DoubleProperty, oldValue: Number?, newValue: Number?, listener: ListenerFiller) {
        assertSame(prop, listener.observable)
        assertEquals(oldValue, listener.oldValue)
        assertEquals(newValue, listener.newValue)
    }

    private fun assertListenerNotCalled(listener: ListenerFiller) {
        assertNull(listener.observable)
        assertNull(listener.oldValue)
        assertNull(listener.newValue)
    }

    @Test
    fun `Creating empty Point0`() {
        val p0 = createPoint0()

        assertPoint0(p0, 0.0, 0.0, 0.0, 0.0)
    }

    @Test
    fun `Setting x Chart`() {
        val xChartListener = ListenerFiller()
        val xRealListener = ListenerFiller()
        val yChartListener = ListenerFiller()
        val yRealListener = ListenerFiller()

        val p0 = createPoint0().apply {
            xChartProperty.addListener(xChartListener)
            xRealProperty.addListener(xRealListener)
            yChartProperty.addListener(yChartListener)
            yRealProperty.addListener(yRealListener)
        }

        p0.xChart = 10.0

        assertPoint0(p0, 10.0, 20.0, 0.0, 0.0)
        assertListener(p0.xChartProperty, 0.0, 10.0, xChartListener)
        assertListener(p0.xRealProperty, 0.0, 20.0, xRealListener)
        assertListenerNotCalled(yChartListener)
        assertListenerNotCalled(yRealListener)
    }

    @Test
    fun `Setting x Real`() {
        val xChartListener = ListenerFiller()
        val xRealListener = ListenerFiller()
        val yChartListener = ListenerFiller()
        val yRealListener = ListenerFiller()

        val p0 = createPoint0().apply {
            xChartProperty.addListener(xChartListener)
            xRealProperty.addListener(xRealListener)
            yChartProperty.addListener(yChartListener)
            yRealProperty.addListener(yRealListener)
        }

        p0.xReal = 10.0

        assertPoint0(p0, 5.0, 10.0, 0.0, 0.0)
        assertListener(p0.xChartProperty, 0.0, 5.0, xChartListener)
        assertListener(p0.xRealProperty, 0.0, 10.0, xRealListener)
        assertListenerNotCalled(yChartListener)
        assertListenerNotCalled(yRealListener)
    }


    @Test
    fun `Setting y Chart`() {
        val xChartListener = ListenerFiller()
        val xRealListener = ListenerFiller()
        val yChartListener = ListenerFiller()
        val yRealListener = ListenerFiller()

        val p0 = createPoint0().apply {
            xChartProperty.addListener(xChartListener)
            xRealProperty.addListener(xRealListener)
            yChartProperty.addListener(yChartListener)
            yRealProperty.addListener(yRealListener)
        }

        p0.yChart = 10.0

        assertPoint0(p0, 0.0, 0.0, 10.0, 30.0)
        assertListenerNotCalled(xChartListener)
        assertListenerNotCalled(xRealListener)
        assertListener(p0.yChartProperty, 0.0, 10.0, yChartListener)
        assertListener(p0.yRealProperty, 0.0, 30.0, yRealListener)
    }

    @Test
    fun `Setting y Real`() {
        val xChartListener = ListenerFiller()
        val xRealListener = ListenerFiller()
        val yChartListener = ListenerFiller()
        val yRealListener = ListenerFiller()

        val p0 = createPoint0().apply {
            xChartProperty.addListener(xChartListener)
            xRealProperty.addListener(xRealListener)
            yChartProperty.addListener(yChartListener)
            yRealProperty.addListener(yRealListener)
        }

        p0.yReal = 60.0

        assertPoint0(p0, 0.0, 0.0, 20.0, 60.0)
        assertListenerNotCalled(xChartListener)
        assertListenerNotCalled(xRealListener)
        assertListener(p0.yChartProperty, 0.0, 20.0, yChartListener)
        assertListener(p0.yRealProperty, 0.0, 60.0, yRealListener)
    }


    @Test
    fun `Double binding x Real`() {
        val xChartListener = ListenerFiller()
        val xRealListener = ListenerFiller()
        val p0 = createPoint0().apply {
            xChartProperty.addListener(xChartListener)
            xRealProperty.addListener(xRealListener)
        }

        val xReal = (0.0).toProperty()
        p0.xRealProperty.bindBidirectional(xReal)

        assertEquals(0.0, p0.xReal)
        assertEquals(0.0, p0.xChart)
        assertEquals(0.0, xReal.value)

        p0.xReal = 20.0
        assertEquals(20.0, p0.xReal)
        assertEquals(10.0, p0.xChart)
        assertEquals(p0.xReal, xReal.value)
        assertListener(p0.xChartProperty, 0.0, 10.0, xChartListener)
        assertListener(p0.xRealProperty, 0.0, 20.0, xRealListener)

        xReal.value = 100.0
        assertEquals(100.0, p0.xReal)
        assertEquals(50.0, p0.xChart)
        assertEquals(p0.xReal, xReal.value)
        assertListener(p0.xChartProperty, 10.0, 50.0, xChartListener)
        assertListener(p0.xRealProperty, 20.0, 100.0, xRealListener)

    }


}

internal class ListenerFiller : ChangeListener<Number> {

    var observable: ObservableValue<out Number>? = null
    var oldValue: Number? = null
    var newValue: Number? = null

    override fun changed(observable: ObservableValue<out Number>?, oldValue: Number?, newValue: Number?) {
        this.observable = observable
        this.oldValue = oldValue
        this.newValue = newValue
    }

}