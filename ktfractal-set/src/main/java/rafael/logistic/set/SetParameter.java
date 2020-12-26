package rafael.logistic.set;

import rafael.logistic.core.generation.IterationParameter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SetParameter implements IterationParameter {
    public final double deltaX;
    public final double deltaY;

    public final double[] xValues;

    public final List<Integer> cols;

    public final double[] yValues;

    public final List<Integer> rows;
    public final double cX;
    public final double cY;
    public final double xMin;
    public final double xMax;
    public final int width;
    public final double yMin;
    public final double yMax;
    public final int height;

    public SetParameter(double cX, double cY, double xMin, double xMax, int width, double yMin, double yMax, int height) {
        this.cX = cX;
        this.cY = cY;
        this.xMin = xMin;
        this.xMax = xMax;
        this.width = width;
        this.yMin = yMin;
        this.yMax = yMax;
        this.height = height;
        this.deltaX = (this.xMax - this.xMin) / (double) this.width;
        this.deltaY = (this.yMax - this.yMin) / (double) this.height;

        this.cols = IntStream.range(0, this.width).boxed().collect(Collectors.toList());
        this.xValues = cols.stream().mapToDouble(x -> x * deltaX + xMin).toArray();

        this.rows = IntStream.range(0, this.height).boxed().collect(Collectors.toList());
        this.yValues = rows.stream().mapToDouble(x -> x * this.deltaY + this.yMin).toArray();
    }

}
