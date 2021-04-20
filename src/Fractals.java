import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Fractals {

    /** Size of display **/
    private int dispSize;

    /** Displaying image of fractal  **/
    private JImageDisplay image;

    /** Using generator of fractal  **/
    private FractalGenerator generator;

    /** Sets range of displaying complex area **/
    private Rectangle2D.Double range;

    /** Choosing fractal menu **/
    private JComboBox menu;

    public Fractals(int displaySize) {

        dispSize = displaySize;
        range = new Rectangle2D.Double(-2, -1.5, 3, 3);
        image = new JImageDisplay(displaySize, displaySize);
        generator = new Mandelbrot();

        menu = new JComboBox();
        menu.addItem(generator);
        menu.addItem(new Tricorn());
        menu.addItem(new BurningShip());
    }


    /** Main method **/
    public static void main(String[] args) {
        Fractals fractals = new Fractals(512);

        fractals.initGUI();
        fractals.drawFractal();
    }

    /**
     * Initialising GUI method
     * */
    private void initGUI() {

        JFrame frame = new JFrame("Mandelbrot Fractal Explorer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = frame.getContentPane();

        contentPane.setLayout(new BorderLayout());

        image.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                double clickedX = FractalGenerator.getCoord(range.x, range.x + range.width,
                        dispSize, e.getX());
                double clickedY = FractalGenerator.getCoord(range.y, range.y + range.height,
                        dispSize, e.getY());

                generator.recenterAndZoomRange(range, clickedX, clickedY, 0.5);
                drawFractal();
            }
        });

        contentPane.add(image, BorderLayout.CENTER);

        JButton resDispButton = new JButton("Reset Display");
        resDispButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generator.getInitialRange(range);
                drawFractal();
            }
        });

        contentPane.add(resDispButton, BorderLayout.SOUTH);

        /** Top panel construction **/
        JPanel panel = new JPanel();
        panel.add(new JLabel("Fractal:"));
        panel.add(menu);

        menu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generator = (FractalGenerator) menu.getSelectedItem();
                generator.getInitialRange(range);

                drawFractal();
            }
        });

        contentPane.add(panel, BorderLayout.NORTH);

        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    /**
     * Fractal drawing method
     * */
    private void drawFractal() {

        /** Temporary support variables **/
        double xCoord = 0;
        double yCoord = 0;
        int numIters = 0;
        int colorRGB = 0;
        float hue = 0;

        for (int x = 0; x < dispSize; ++x)
            for (int y = 0; y < dispSize; ++y) {

                xCoord = FractalGenerator.getCoord(range.x, range.x + range.width,
                        dispSize, x);
                yCoord = FractalGenerator.getCoord(range.y, range.y + range.height,
                        dispSize, y);

                numIters = generator.numIterations(xCoord, yCoord);

                if (numIters == -1) {
                    image.drawPixel(x, y, 0);
                    continue;
                }

                hue = 0.7f + (float) numIters / 200f;
                colorRGB = Color.HSBtoRGB(hue, 1f, 1f);

                image.drawPixel(x, y, colorRGB);
            }

        image.repaint();
    }
}
