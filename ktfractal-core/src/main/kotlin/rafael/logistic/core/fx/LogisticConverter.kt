package rafael.logistic.core.fx

import javafx.util.StringConverter

/**
 * Conversor que converte um Double numa string com uma quantidade predeterminada de casas decimais.
 *
 * @constructor Cria uma nova instância do Conversor.
 *
 * @param size Quantidade de casas decimais.
 */
class LogisticConverter(size: Int) : StringConverter<Double>() {

    private val format = "%.${size}f"

    override fun toString(value: Double?): String = if (value != null) format.format(value) else ""

    override fun fromString(string: String?): Double? = string?.toDoubleOrNull()

}

@Suppress("UNCHECKED_CAST")
val CONVERTER_2 = LogisticConverter(2) as StringConverter<Number>
