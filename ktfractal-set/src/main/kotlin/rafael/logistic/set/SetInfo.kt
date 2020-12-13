package rafael.logistic.set

/**
 * Informações de um Set em uma determinada posição
 *
 * @property col Coluna no [Canvas][javafx.scene.canvas.Canvas], do lado esquerdo ao lado direito.
 * @property row Linha no [Canvas][javafx.scene.canvas.Canvas], do lado superior ao lado inferior.
 * @property x posição real, em coordenadas cartesianas.
 * @property y posição real, em coordenadas cartesianas.
 * @property iterationsToDiverge Quantidade de Iterações que a equaçãpo leva para divergir ou `null` se não divergir.
 */
data class SetInfo(val col: Int, val row: Int, val x: Double, val y: Double, val iterationsToDiverge: Int?) {

    /**
     * Se há convergência ([iterationsToDiverge] é `null`) ou não.
     */
    val converges = (iterationsToDiverge == null)

}
