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
 * Para acionar é necessário adicionar a propriedade da JVM "timer", com a linha `-Dtimer=c` ou `-Dtimer=s`.
 *
 * Se o valor de `timer` for `c`, cada linha terá o instante de início do ciclo (Quando , e as colunas com cada status o tempo gasto nele
 * em milisegundos. Por exemplo:
 * ```
2020-12-10T15:58:50.810908800 	CA:   93	PP:   12	PC:   34	PD:    0	PF:    9
2020-12-10T15:58:50.958514    	CA:  608	PP:    0	PC:   48	PD:   36	PF:    0
2020-12-10T15:58:51.650667700 	CA: 4906	PP:    0	PC:    8	PD:    8	PF:    0
 * ```
 * Onde:
 *  - CA = [GenerationStatus.CALCULATING]
 *  - PP = [GenerationStatus.PLOTTING_PREPARING]
 *  - PC = [GenerationStatus.PLOTTING_CONVERT]
 *  - PD = [GenerationStatus.PLOTTING_DRAW]
 *  - PF = [GenerationStatus.PLOTTING_FINALIZING]
 *
 *  Se o valor de `timer` for `s`, cada linha terá apenas os tempos gasto em cada status, na mesma ordem exibida acima:
 *  ```
  48	   5	   9	   1	   2
 515	   0	  23	  30	   0
1845	   1	   6	  10	   0
 *  ```
 *  Esse formato permite copiar esse valores e colar numa planilha.
 */
class GenerationStatusChronometerListener private constructor(private val processor: (Instant, Instant?, GenerationStatus, GenerationStatus) -> Unit) :
    ChangeListener<GenerationStatus> {

    companion object {

        @ExperimentalTime
        private fun printComplete(now: Instant, prior: Instant?, priorStatus: GenerationStatus, newStatus: GenerationStatus) {
            if (priorStatus == GenerationStatus.IDLE) {
                val localNow = now.toLocalDateTime(TimeZone.currentSystemDefault())
                print("%02d:%02d:%02d.%03d".format(localNow.hour, localNow.minute, localNow.second, (localNow.nanosecond / 1_000_000)))
                if(newStatus == GenerationStatus.PLOTTING_PREPARING) {
                    print("\t  :     ") // Deveria ser o CA
                }
            } else if (prior != null) {
                print("\t%s: %4.0f".format(priorStatus.code, (now - prior).inMilliseconds))
            }
        }

        @ExperimentalTime
        private fun printSintetic(now: Instant, prior: Instant?, priorStatus: GenerationStatus, newStatus: GenerationStatus) {
            if (priorStatus == GenerationStatus.IDLE) {
                if(newStatus == GenerationStatus.PLOTTING_PREPARING) {
                    print("    \t") // Deveria ser o CA
                }
            } else if (prior != null) {
                print("%4.0f\t".format((now - prior).inMilliseconds))
            }
        }

        @ExperimentalTime
        fun bind(generationStatusProperty: ReadOnlyObjectProperty<GenerationStatus>) {
            if (System.getProperty("timer") == null) {
                return
            }

            val processador = when (System.getProperty("timer")) {
                "c" -> ::printComplete
                "s" -> ::printSintetic
                else -> error("'timer' deve ter valor 'c' (Completo) ou 's' (Sintético)")
            }

            generationStatusProperty.addListener(GenerationStatusChronometerListener(processador))
        }
    }

    private var priorTime: Instant? = null

    override fun changed(
        observable: ObservableValue<out GenerationStatus>?,
        oldValue: GenerationStatus?,
        newValue: GenerationStatus?
    ) {
        val now = Clock.System.now()

        processor(now, priorTime, oldValue!!, newValue!!)
        if (newValue == GenerationStatus.IDLE) {
            println()
        }
//        Throwable("${LocalTime.now()}: ${oldValue.code${newValue!!.code}").printStackTrace()

        priorTime = if (newValue == GenerationStatus.IDLE) null else now
    }
}
