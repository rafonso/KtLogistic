package rafael.logistic.set;

import rafael.logistic.core.generation.BiDouble;
import rafael.logistic.core.generation.IterationGenerator;

import java.util.Arrays;
import java.util.List;

public abstract class SetGenerator implements IterationGenerator<BiDouble, SetInfo, SetParameter> {

    private SetInfo[] setInfos = {};

    private SetInfo colToSetInfo(SetParameter parameter, int interactions, Integer row, Integer col, double y) {
        final double x = parameter.xValues[col];
        final int iterationsToDiverge = this.verify(x, y, parameter, interactions);

        return new SetInfo(col, row, x, y, iterationsToDiverge);
    }

    private boolean diverges(double nextZX, double nextZY) {
        return nextZX * nextZX + nextZY * nextZY > 4.0D;
    }

    /**
     * Check convergence
     *
     * @param zx               valor da coordenada `x` do ponto corrente.
     * @param zy               valor da coordenada `y` do ponto corrente.
     * @param cx               valor da coordenada `x` da constante.
     * @param cy               valor da coordenada `y` da constante.
     * @param convergenceSteps Número máximo de iterações
     * @return Número de Iterações para a série divergir ou `0` se ela não o fez até atingir [convergenceSteps].
     */
    protected final int checkConvergence(final double zx, final double zy, final double cx, final double cy, final int convergenceSteps) {
        double zzx = zx;
        double zzy = zy;

        for (int iteration = 1; iteration < convergenceSteps; iteration++) {
            double nextZX = this.nextX(zzx, zzy, cx, cy);
            double nextZY = this.nextY(zzx, zzy, cx, cy);

            if (this.diverges(nextZX, nextZY)) {
                return iteration;
            }

            zzy = nextZY;
            zzx = nextZX;
        }

        return 0;
    }

    /**
     * Verifica quanta iterações são necessárias para a série divirga em um determinado ponto.
     *
     * @param x            coordenada x do ponto
     * @param y            coordenada y do ponto
     * @param parameter    Parâmetros da série
     * @param interactions Quantidade máxima de Iterações.
     * @return o número de iterações necessárias para que a série divirga ou `null` se ela não divergir ao se atingir [interactions]
     */
    protected abstract int verify(double x, double y, SetParameter parameter, int interactions);

    protected abstract double nextX(double zx, double zy, double cx, double cy);

    protected abstract double nextY(double zx, double zy, double cx, double cy);

    @SuppressWarnings("NullableProblems")
    @Override
    public List<SetInfo> generate(BiDouble z0, final SetParameter parameter, final int interactions) {
        if (parameter.width * parameter.height != setInfos.length) {
            setInfos = new SetInfo[parameter.width * parameter.height];
        }

        parameter.rows
                .parallelStream()
                .forEach(row -> {
                    final double y = parameter.yValues[parameter.rows.size() - row - 1];

                    parameter.cols.parallelStream().forEach(col -> setInfos[parameter.width * row + col] = colToSetInfo(parameter, interactions, row, col, y));
                });

        return Arrays.asList(setInfos);
    }

}
