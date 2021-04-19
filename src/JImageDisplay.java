import javax.swing.JComponent;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
*   Виджет для прорисовки фракталов
*/
public class JImageDisplay extends JComponent {
    private BufferedImage bufi;

    JImageDisplay(int width, int heigth) {
        bufi = new BufferedImage(width, heigth, BufferedImage.TYPE_INT_RGB);
        setPreferredSize(new Dimension(width, heigth));
    }

    /** Прорисовка изображения  **/
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bufi, 0, 0, bufi.getWidth(), bufi.getHeight(), null);
    }

    /** Очистка изображения  **/
    public void clearImage() {
        for (int y = 0; y < bufi.getHeight(); ++y)
            for (int x = 0; x < bufi.getHeight(); ++y)
                bufi.setRGB(x, y, 0);
    }

    /** Перерисовка пикселя  **/
    public void drawPixel(int x, int y, int rgbColor) {
        bufi.setRGB(x, y, rgbColor);
    }
}
