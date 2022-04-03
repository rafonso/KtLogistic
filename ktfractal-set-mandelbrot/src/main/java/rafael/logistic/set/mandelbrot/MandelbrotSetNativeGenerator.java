package rafael.logistic.set.mandelbrot;

import rafael.logistic.set.SetGenerator;
import rafael.logistic.set.SetParameter;

public class MandelbrotSetNativeGenerator extends SetGenerator {

    static {
        System.loadLibrary("MandelbrotSet");
    }

    private native int checkMandelbrotConvergence(double zx, double zy, double x, double y, int interactions);

    @Override
    protected int verify(double x, double y, SetParameter parameter, int interactions) {
        return checkMandelbrotConvergence(0.0, 0.0, x, y, interactions);
    }


    @Override
    protected double nextX(double zx, double zy, double cx, double cy) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected double nextY(double zx, double zy, double cx, double cy) {
        throw new UnsupportedOperationException();
    }
}
