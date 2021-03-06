import java.awt.geom.Rectangle2D;

public class Mandelbrot extends FractalGenerator {

    public static final int MAX_ITERATIONS = 2000;

    @Override
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.y = -1.5;
        range.width = 3;
        range.height = 3;
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
            yRes = 2 * xRes * yRes + y;
            xRes = newXRes;
        }

        return -1;
    }
}
