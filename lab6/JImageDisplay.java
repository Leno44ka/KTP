import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * TODO: Отображение фракталов
 * Класс позволяет нам отображать фракталы.
 * производный от javax.swing.JComponent.
 */
class JImageDisplay extends JComponent {
    /**
     * BufferedImage управляет
     * изображением, содержимое которого можно записать.
     */
    private BufferedImage displayImage;

    /**
     * Получение изображения (для реализации из другого класса)
     */
    public BufferedImage getImage() {
        return displayImage;
    }

    /**
     * Конструктор принимает int ширину и высоту.
     * Создает экземпляр класса BufferedImage,
     * новое изображение с этой шириной и высотой
     * цвета типа TYPE_INT_RGB
     */
    public JImageDisplay(int width, int height) {
        displayImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        //Вызов родительского класса
        //Инкапсулирует ширину и высоту компонента в один объект.
        Dimension imageDimension = new Dimension(width, height);
        //Устанавливает предпочтительный размер этого компонента.
        super.setPreferredSize(imageDimension);

    }

    /**
     * Вызываем paintComponent родителя для верного отображения.
     * Рисуем изображение компонента
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(displayImage, 0, 0, displayImage.getWidth(), displayImage.getHeight(), null);
    }

    /**
     * Метод clearImage (), который устанавливает все пиксели
     * изображения в черный цвет
     */
    public void clearImage() {
        int[] blankArray = new int[getWidth() * getHeight()];
        displayImage.setRGB(0, 0, getWidth(), getHeight(), blankArray, 0, 1);
    }

    /**
     * Устанавливает пиксель в определенный цвет
     */
    public void drawPixel(int x, int y, int rgbColor) {
        displayImage.setRGB(x, y, rgbColor);
    }
}
