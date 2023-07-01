package rafael.logistic.core.fx.mapchart

import javafx.scene.input.MouseEvent
import rafael.logistic.core.generation.BiDouble

// Usado apenas para noMouseRealPos
private val noMouseEvent = MouseEvent(
    null,
    Double.NaN,
    Double.NaN,
    Double.NaN,
    Double.NaN,
    null,
    0,
    false,
    false,
    false,
    false,
    false,
    false,
    false,
    false,
    false,
    false,
    null
)

/**
 * Encapsula um [MouseEvent], indicando a posição real que onde o mouse se encontra.
 *
 * @property x Valor x da posição real do mouse.
 * @property y Valor y da posição real do mouse.
 * @property mouseEvent [MouseEvent] encapsulado.
 */
data class MouseRealPos(val x: Double, val y: Double, val mouseEvent: MouseEvent) {

    companion object {

        /**
         * Usado para indicar que o mouse se encontra fora do gráfico
         */
        val noMouseRealPos = MouseRealPos(Double.NaN, Double.NaN, noMouseEvent)

        /**
         * Verifica se o cursor do mouse se encontra dentro do componente.
         *
         * @param width Largura do componente
         * @param height Altura do componente
         * @return `true` se o cursor está dentro do componente e se [Tipo de Evento][MouseEvent.eventType] não for
         * [MouseEvent.MOUSE_EXITED] nem [MouseEvent.MOUSE_EXITED_TARGET].
         */
        fun MouseEvent.isInside(width: Double, height: Double): Boolean =
            (this.eventType != MouseEvent.MOUSE_EXITED) &&
                    (this.eventType != MouseEvent.MOUSE_EXITED_TARGET) &&
                    (x in (0.0)..width) && (y in (0.0)..height)

    }

    /**
     * Indica se o mouse se encontra dentro do gráfico.
     *
     */
    val isValid = mouseEvent !== noMouseEvent

    val biDouble: BiDouble by lazy { BiDouble(x, y) }

}