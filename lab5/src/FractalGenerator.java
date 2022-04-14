import java.awt.geom.Rectangle2D;


/**
 * TODO: Генерация фрактала
 * Класс предоставляет интерфейс и операции для
 * генерации фракталов
 */
public abstract class FractalGenerator {

    /**
     * Метод приминает координату и преобразует ее
     * в значение двойной точности(double) в определенном диапазоне.
     * Используем это для преобразования пикселей в числа для большей точности
     * для фракталов
     *
     * @param rangeMin - минимальное значение диапазона
     * @param rangeMax - максимальное значение диапазона
     * @param size     - размер измерения, от которого происходит координата пикселя.
     * Например, это может быть ширина изображения или высота изображения.
     * @param coord    - координата, для которой вычисляется значение двойной точности.
     *                 Координата должна находиться в диапазоне [0, размер].
     */
    public static double getCoord(double rangeMin, double rangeMax, int size, int coord) {
        //Отлавливаем ошибки в программах во время исполнения
        assert size > 0;
        assert coord >= 0 && coord < size;

        double range = rangeMax - rangeMin;
        return rangeMin + (range * (double) coord / (double) size);
    }

    /**
     * Устанавливает указанный прямоугольник, чтобы он содержал начальный диапазон, подходящий для
     * генерируемый фрактал.
     */
    public abstract void getInitialRange(Rectangle2D.Double range);

    /**
     * Обновляет текущий диапазон, чтобы он был центрирован по указанным координатам,
     * для увеличения или уменьшения с указанным коэффициентом масштабирования.
     */

    public void recenterAndZoomRange(Rectangle2D.Double range, double centerX, double centerY, double scale) {

        double newWidth = range.width * scale;
        double newHeight = range.height * scale;

        range.x = centerX - newWidth / 2;
        range.y = centerY - newHeight / 2;
        range.width = newWidth;
        range.height = newHeight;
    }

    /**
     * Дается коодината X и Y
     * Возвращает кол-во инераций до момента как фрактал покидает
     * (заходит за границы) область для этой точки
     * Если точка не исчезает до конца предела итераций,
     * то выводим -1
     */
    public abstract int numIterations(double x, double y);
}
