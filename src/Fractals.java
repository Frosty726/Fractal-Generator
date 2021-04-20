import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import javax.swing.filechooser.*;
import javax.imageio.*;

public class Fractals {

    /** Size of display **/
    private int dispSize;

    /** Displaying image of fractal  **/
    private JImageDisplay image;

    /** Using generator of fractal  **/
    private FractalGenerator generator;

    /** Sets range of displaying complex area **/
    private Rectangle2D.Double range;

    /** Application frame **/
    private JFrame frame;

    /** Choosing fractal menu **/
    private JComboBox menu;

    /** Buttons **/
    private JButton resetDispButton;
    private JButton saveImageButton;

    /** Variable for multithread handling **/
    private int linesRemaining;

    public Fractals(int displaySize) {

        dispSize = displaySize;
        range = new Rectangle2D.Double(-2, -1.5, 3, 3);
        image = new JImageDisplay(displaySize, displaySize);
        generator = new Mandelbrot();

        frame = new JFrame("Mandelbrot Fractal Explorer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        resetDispButton = new JButton("Reset Display");
        saveImageButton = new JButton("Save Image");

        menu = new JComboBox();
        menu.addItem(generator);
        menu.addItem(new Tricorn());
        menu.addItem(new BurningShip());
    }


    /**
     * Action handler class
     * */
    private class ActionHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            /** case: action source is "Reset Display" Button **/
            if (e.getSource() == resetDispButton) {

                generator.getInitialRange(range);
                drawFractal();

            /** case: action source is "Save Image" Button **/
            } else if (e.getSource() == saveImageButton) {

                JFileChooser fChooser = new JFileChooser();
                FileFilter filter = new FileNameExtensionFilter("PNG Images", "png");
                fChooser.setFileFilter(filter);
                fChooser.setAcceptAllFileFilterUsed(false);

                if (fChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    File file = fChooser.getSelectedFile();

                    try {
                        ImageIO.write(image.getBufi(), "png",
                                new File(file.getAbsolutePath() + ".png"));
                    }
                    catch (IOException exception) {
                        JOptionPane.showMessageDialog(frame, exception.getMessage(),
                                "Unable to save image", JOptionPane.ERROR_MESSAGE);
                    }
                }

            /** case: action source is fractal choosing menu **/
            } else if (e.getSource() == menu) {

                generator = (FractalGenerator) menu.getSelectedItem();
                generator.getInitialRange(range);

                drawFractal();
            }
        }
    }


    /**
     * Class for implementing multithreading feature.
     * One thread estimating one line of fractal.
     * */
    private class FractalWorker extends SwingWorker<Object, Object> {

        /** Coordinate of estimating line **/
        private int y;

        /** Estimated RGB colors of pixels in line **/
        private int[] line;

        public FractalWorker(int y) {
            this.y = y;
        }

        /** Estimating color process **/
        @Override
        protected Object doInBackground() {

            line = new int[dispSize];

            /** Temporary support variables **/
            double xCoord = 0;
            double yCoord = 0;
            int numIters = 0;
            float hue = 0;

            for (int x = 0; x < dispSize; ++x) {

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
                line[x] = Color.HSBtoRGB(hue, 1f, 1f);
            }

            return null;
        }

        /** Drawing pixels with estimated colors **/
        @Override
        protected void done() {
            for (int x = 0; x < dispSize; ++x)
                image.drawPixel(x, y, line[x]);

            image.repaint(0, 0, y, dispSize, 1);

            --linesRemaining;
            if (linesRemaining == 0)
                enableUI(true);
        }
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

        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());

        ActionHandler actHand = new ActionHandler();

        image.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (linesRemaining > 0)
                    return;

                double clickedX = FractalGenerator.getCoord(range.x, range.x + range.width,
                        dispSize, e.getX());
                double clickedY = FractalGenerator.getCoord(range.y, range.y + range.height,
                        dispSize, e.getY());

                generator.recenterAndZoomRange(range, clickedX, clickedY, 0.5);
                drawFractal();
            }
        });

        contentPane.add(image, BorderLayout.CENTER);

        /** Bottom panel construction **/
        JPanel bottomPanel = new JPanel();

        resetDispButton.addActionListener(actHand);
        saveImageButton.addActionListener(actHand);

        bottomPanel.add(saveImageButton);
        bottomPanel.add(resetDispButton);

        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        /** Top panel construction **/
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Fractal:"));
        topPanel.add(menu);

        menu.addActionListener(actHand);

        contentPane.add(topPanel, BorderLayout.NORTH);

        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    /**
     * Fractal drawing method
     * */
    private void drawFractal() {
        enableUI(false);
        linesRemaining = dispSize;

        FractalWorker fWorker;

        for (int y = 0; y < dispSize; ++y) {
            fWorker = new FractalWorker(y);
            fWorker.execute();
        }
    }

    /**
     * Handling access to UI method
     * */
    private void enableUI(boolean val) {
        saveImageButton.setEnabled(val);
        resetDispButton.setEnabled(val);
        menu.setEnabled(val);
    }
}
