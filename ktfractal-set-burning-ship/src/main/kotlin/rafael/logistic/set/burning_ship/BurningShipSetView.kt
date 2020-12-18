package rafael.logistic.set.burning_ship

import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.valueToString
import rafael.logistic.set.SetView
import tornadofx.App

class BurningShipSetApp: App(BurningShipSetView::class, Styles::class)

class BurningShipSetView : SetView("Burning Ship Set", "BurningShipSet", BurningShipSetGenerator()) {

    override val spinnerComponents = emptyArray<SpinnerConfigurations>()

    override fun getImageName(iterations: Int): String = "burning-ship" +
            ".XMin=${spnXMin.valueToString()}" +
            ".XMax=${spnXMax.valueToString()}" +
            ".YMin=${spnYMin.valueToString()}" +
            ".YMax=${spnYMax.valueToString()}" +
            ".Iterations_Dot=${iterations}"

}
