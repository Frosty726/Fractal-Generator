import java.awt.geom.Rectangle2D;

/**
 * Tricorn fractal generation class
 *
 * [Z{i} = (!Z{i-1})^2 + C]
 * C = x + iy, where x, y - coordinates of pixel.
 * Z{i} - complex number, Z{0} = 0
 * if Z = a + ib, then !Z = a - ib
 *
 * Initial range is [-2 - 2i; 2 + 2i]
 * */
public class Tricorn extends FractalGenerator {
    public static final int MAX_ITERATIONS = 2000;

    @Override
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.y = -2;
        range.width = 4;
        range.height = 4;
    }

    @Override
    public int numIterations(double x, double y) {

        double xRes = 0;
        double yRes = 0;
        double newXRes = 0;

        for (int i = 0; i < MAX_ITERATIONS; ++i) {
            if (xRes * xRes + yRes * yRes > 4)
                return i;

            newXRes = xRes * xRes - yRes * yRes + x;
            yRes = - 2 * xRes * yRes + y;
            xRes = newXRes;
        }

        return -1;
    }

    public String toString() {
        return "Tricorn";
    }
}
