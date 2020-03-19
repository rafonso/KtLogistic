package rafael.logistic.view

import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import java.time.Duration
import java.time.LocalTime

class GenerationStatusChronometerListener : ChangeListener<GenerationStatus> {

    private var priorTime: LocalTime? = null

    override fun changed(observable: ObservableValue<out GenerationStatus>?, oldValue: GenerationStatus?, newValue: GenerationStatus?) {
        val now = LocalTime.now()

        if (oldValue == GenerationStatus.IDLE) {
            print(now)
        } else if (priorTime != null && (newValue!!.ordinal - oldValue!!.ordinal == 1)) {
            print("\t%4d".format(Duration.between(priorTime, now).toMillis()))
        }
        if (newValue == GenerationStatus.IDLE) {
            println()
        }

        priorTime = if (newValue == GenerationStatus.IDLE) null else now
    }
}