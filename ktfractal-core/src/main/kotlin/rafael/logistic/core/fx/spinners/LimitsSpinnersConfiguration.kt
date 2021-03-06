package rafael.logistic.core.fx.spinners

import javafx.scene.control.SpinnerValueFactory

/**
 * Classe auxiliar para a configuração de [DoubleSpinner]s vinculados, onde um [DoubleSpinner] define um valor
 * menor e um segundo o maior. A configuração é feita de forma que o valor do primeiro nunca é maior que o segundo.
 *
 * @property spnMin Spinner que define o valor menor
 * @property spnMax Spinner que define o valor maior
 * @property min Menor valor permitido
 * @property max Maior valor permitido
 * @property initialDecimalPlaces Número inicial de casas decimais dos Spinners.
 */
data class LimitsSpinnersConfiguration(
    val spnMin: DoubleSpinner,
    val spnMax: DoubleSpinner,
    val min: Double,
    val max: Double,
    val initialDecimalPlaces: Int = 1
) {

    val minValueFactory = SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, min, 0.1)

    val maxValueFactory = SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, max, 0.1)

}