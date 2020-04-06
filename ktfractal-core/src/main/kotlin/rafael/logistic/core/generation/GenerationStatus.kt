package rafael.logistic.core.generation

enum class GenerationStatus(val code: String) {
    IDLE                ("ID"),
    CALCULATING         ("CA"),
    PLOTTING_PREPARING  ("PP"),
    PLOTTING_CONVERT    ("PC"),
    PLOTTING_DRAW       ("PD"),
    PLOTTING_FINALIZING ("PF")
}
