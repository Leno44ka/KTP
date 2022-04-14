import java.awt.geom.Rectangle2D;

/**
 * TODO: Подкласс FractalGeneration(Mandelbrot реализация)
 * Реализует подсчет фрактала Mandelbrot
 */
public class Mandelbrot extends FractalGenerator {

    /**
     * Константа максимального кол-ва итераций
     */
    public static final int MAX_ITERATIONS = 2000;

    /**
     * Метод позволяет генератору фракталов указать, какая часть
     * комплексной плоскости наиболее подходит для фрактала.
     * Ему передается объект прямоугольника, и метод изменяет
     * поля прямоугольника, чтобы показать правильный начальный диапазон для фрактала.
     * Эта реализация устанавливает начальный диапазон (-2 - 1.5i) - (1 + 1.5i)
     */
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.y = -1.5;
        range.width = 3;
        range.height = 3;
    }

    /**
     * Метод реализцует итерацию для фрактала.
     * Берет два double для действительной чпсти и мнимые части комплексного числа
     * и возвращает кол-во итераций для соответствующей координаты.
     */
    public int numIterations(double x, double y) {
        int iteration = 0;
        double realPart = 0;
        double imaginaryPart = 0;

        /**
         * Счиатем по формуле An = An - 1^2 + c, где
         * @param realPart - действительная часть числа
         * @param imaginaryPart - мнимая часть числа
         * A0 = 0
         * c - соответствующий x и y (данные для точки)
         *
         * Итерация до тех пор, пока A^2 > 4 (абсолютное значение A больше чем 2)
         * или максимальное число итераций не достагнуто
         */
        while (iteration < MAX_ITERATIONS && realPart * realPart + imaginaryPart * imaginaryPart < 4) {
            double realPartNew = realPart * realPart - imaginaryPart * imaginaryPart + x;
            double imaginaryPartNew = 2 * realPart * imaginaryPart + y;
            realPart = realPartNew;
            imaginaryPart = imaginaryPartNew;
            iteration++;
        }

        /**
         * Если максимальное кол-во итераций достигнуто,
         * то возвращаем -1
         */
        if (iteration == MAX_ITERATIONS) {
            return -1;
        }


        return iteration;


    }

    /**
     * Наш toString() для возврата имени фрактала
     * @return "Mandelbrot"
     */
    public String toString() {
        return "Mandelbrot";
    }

}