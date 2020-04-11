package rafael.logistic.core.generation

import javafx.beans.property.ReadOnlyObjectProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import java.time.Duration
import java.time.LocalTime

/**
 * Cronometra os tempos gastos em cada [GenerationStatus].
 * Para acionar é necessário adicionar a propriedade da JVM "timer", com a linha `-Dtimer`.
 */
class GenerationStatusChronometerListener private constructor() : ChangeListener<GenerationStatus> {

    companion object {
        fun bind(generationStatusProperty: ReadOnlyObjectProperty<GenerationStatus>) {
            if (System.getProperty("timer") != null) {
                generationStatusProperty.addListener(GenerationStatusChronometerListener())
            }
        }
    }

    private var priorTime: LocalTime? = null

    override fun changed(
        observable: ObservableValue<out GenerationStatus>?,
        oldValue: GenerationStatus?,
        newValue: GenerationStatus?
    ) {
        val now = LocalTime.now()

        if (oldValue == GenerationStatus.IDLE) {
            print(now)
        } else if (priorTime != null) { //  && (newValue != GenerationStatus.IDLE)) {
            print("\t%4d".format(Duration.between(priorTime, now).toMillis()))
//            print("\t${oldValue?.code}: %4d".format(Duration.between(priorTime, now).toMillis()))
        }
        if (newValue == GenerationStatus.IDLE) {
            println()
        }

        priorTime = if (newValue == GenerationStatus.IDLE) null else now
    }
}
