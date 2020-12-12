package rafael.logistic.core.fx.mapchart

import javafx.scene.paint.Color

/**
 * Contém as informações dos pixels a serem usadas em [CanvasChart].
 *
 * @property xChart Posição no eixo X (da esquerda para a direita)
 * @property yChart Posição no eixo Y (de cima para baixo)
 * @property color Cor na posição em questão.
 */
class PixelInfo(val xChart: Int, val yChart: Int, val color: Color) {

    /**
     * Compara com outro [PixelInfo] usando [xChart] e [yChart].
     *
     * @param other outro (supostamente) [PixelInfo]
     * @return `true` se ambos tiverem o mesmo [xChart] e [yChart].
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PixelInfo

        if (xChart != other.xChart) return false
        if (yChart != other.yChart) return false

        return true
    }

    /**
     * Gera o hashCore a partir de [xChart] e [yChart].
     *
     * @return hashCore gerado a partir de [xChart] e [yChart].
     */
    override fun hashCode(): Int {
        var result = xChart
        result = 31 * result + yChart
        return result
    }

    override fun toString(): String {
        return "[$xChart, $yChart, $color]"
    }

}