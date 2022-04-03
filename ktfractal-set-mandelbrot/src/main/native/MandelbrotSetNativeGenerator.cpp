#include "pch.h"
#include "rafael_logistic_set_mandelbrot_MandelbrotSetNativeGenerator.h"

JNIEXPORT jint JNICALL Java_rafael_logistic_set_mandelbrot_MandelbrotSetNativeGenerator_checkMandelbrotConvergence
  (JNIEnv *env, jobject thiz, jdouble zx, jdouble zy, jdouble cx, jdouble cy, jint convergenceSteps) {
        double zzx = zx;
        double zzy = zy;
        double nextZX;
        double nextZY;

        for (int iteration = 1; iteration < convergenceSteps; iteration++) {
//        (zzx: Double, zzy: Double, cx: Double, cy: Double) = zx * zx - zy * zy + cx
//            nextZX = this.nextX(zzx, zzy, cx, cy);
            nextZX = zzx * zzx - zzy * zzy + cx;
//            (zzx: Double, zzy: Double, cx: Double, cy: Double) = 2 * zx * zy + cy
//            nextZY = this.nextY(zzx, zzy, cx, cy);
            nextZY = 2 * zzx * zzy + cy;

            if (nextZX * nextZX + nextZY * nextZY > 4.0) {
                return iteration;
            }

            zzy = nextZY;
            zzx = nextZX;
        }

        return 0;

  }