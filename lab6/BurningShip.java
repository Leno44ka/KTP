import java.awt.geom.Rectangle2D;

/**
 * TODO: Подкласс FractalGeneration. BurningShip
 * Реализует фрактал BurningShip
 */
public class BurningShip extends FractalGenerator {

    /**
     * Константа для максимума итераций
     */
    public static final int MAX_ITERATIONS = 2000;

    /**
     * Метод позволяет генератору фракталов указать, какая часть
     * комплексной плоскости наиболее подходит для фрактала.
     * Ему передается объект прямоугольника, и метод изменяет
     * поля прямоугольника, чтобы показать правильный начальный диапазон для фрактала.
     * Начальный диапазон x = -2, y = -2,5 width=height=4.
     */
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.y = -2.5;
        range.width = 4;
        range.height = 4;
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
         * Считаем An = (abs[Re(An-1)] + i(abs[img(An-1)]))^2
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
            double imaginaryPartNew = 2 * Math.abs(realPart) * Math.abs(imaginaryPart) + y;
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
     *
     * @return "Burning Ship"
     */
    public String toString() {
        return "Burning Ship";
    }
}

