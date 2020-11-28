package rafael.logistic.core.generation

import javafx.beans.property.ReadOnlyObjectProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

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

    private var priorTime: Instant? = null

    @ExperimentalTime
    override fun changed(
        observable: ObservableValue<out GenerationStatus>?,
        oldValue: GenerationStatus?,
        newValue: GenerationStatus?
    ) {
        val now = Clock.System.now()

        if (oldValue == GenerationStatus.IDLE) {
            print("%-30s".format(now.toLocalDateTime(TimeZone.currentSystemDefault())))
        } else if (priorTime != null) { //  && (newValue != GenerationStatus.IDLE)) {
            print("\t%s: %4.0f".format(oldValue?.code ?: "??", (now - priorTime!!).inMilliseconds))
        }
        if (newValue == GenerationStatus.IDLE) {
            println()
        }

        priorTime = if (newValue == GenerationStatus.IDLE) null else now
    }
}
