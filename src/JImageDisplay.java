import javax.swing.JComponent;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
* Fractals painting widget
*/
public class JImageDisplay extends JComponent {
    private BufferedImage bufi;

    JImageDisplay(int width, int heigth) {
        bufi = new BufferedImage(width, heigth, BufferedImage.TYPE_INT_RGB);
        setPreferredSize(new Dimension(width, heigth));
    }

    /** Image painting method  **/
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bufi, 0, 0, bufi.getWidth(), bufi.getHeight(), null);
    }

    /** Cleaning image method  **/
    public void clearImage() {
        for (int y = 0; y < bufi.getHeight(); ++y)
            for (int x = 0; x < bufi.getHeight(); ++y)
                bufi.setRGB(x, y, 0);
    }

    /** Pixel repaint method  **/
    public void drawPixel(int x, int y, int rgbColor) {
        bufi.setRGB(x, y, rgbColor);
    }
}
