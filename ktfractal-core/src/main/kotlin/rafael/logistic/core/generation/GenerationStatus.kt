package rafael.logistic.core.generation

enum class GenerationStatus(val code: String) {
    /**
     * O sistema está aguardando.
     */
    IDLE                ("ID"),
    /**
     * O sistema está calculando os valores.
     */
    CALCULATING         ("CA"),
    PLOTTING_PREPARING  ("PP"),
    PLOTTING_CONVERT    ("PC"),
    PLOTTING_DRAW       ("PD"),
    PLOTTING_FINALIZING ("PF")
}
