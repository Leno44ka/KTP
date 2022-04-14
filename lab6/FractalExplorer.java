import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Класс соединяет разные части реализации фрактала
 * путем создания и демонстрации Swing GUI и обработки
 * событий, связаных с взаимодействием с пользователем
 */
public class FractalExplorer {
    //Поля для кнопок save, reset, combo box
    private JButton saveButton;
    private JButton resetButton;
    private JComboBox myComboBox;

    // Оставшееся кол-во строк для отрисовки
    private int rowsRemaining;

    //Кол-во пикселей исходя из длины и ширины дисплея
    private int displaySize;

    //Объект для обновления отображения в разных
    //методах в процессе вычисления фрактала.
    private JImageDisplay display;

    //Объект, использующий базовый класс FractalGeneration на будущее
    private FractalGenerator fractal;

    //Диапазон комплексной плоскости, которая выводится на экран.
    private Rectangle2D.Double range;

    //Констуктор для инициализации диапазона и объектов FractalGeneration
    public FractalExplorer(int size) {

        //Размер дисплея
        displaySize = size;

        //Объекты FractalGeneration
        fractal = new Mandelbrot();
        range = new Rectangle2D.Double();
        fractal.getInitialRange(range);
        display = new JImageDisplay(displaySize, displaySize);

    }

    /**
     * Метод инициализирует Swing GUI с JFrame содержащим
     * объеты JImageDisplay и кнопку для ресета дисплея
     */
    public void createAndShowGUI() {

        //Устанавливаем дисплей для использования java.awt.BorderLayout
        display.setLayout(new BorderLayout());
        JFrame myFrame = new JFrame("Fractal Explorer");

        //Добавляем объект ImageDisplay
        myFrame.add(display, BorderLayout.CENTER);


        // reset кнопка.
        resetButton = new JButton("Reset");
        ButtonHandler resetHandler = new ButtonHandler();
        resetButton.addActionListener(resetHandler);

        //Экземпляр MouseHandler
        MouseHandler click = new MouseHandler();
        display.addMouseListener(click);

        //Exit кнопка
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Установка ComboBox
        myComboBox = new JComboBox();

        //Добавляем фракталы в ComboBox
        FractalGenerator mandelbrotFractal = new Mandelbrot();
        myComboBox.addItem(mandelbrotFractal);
        FractalGenerator tricornFractal = new Tricorn();
        myComboBox.addItem(tricornFractal);
        FractalGenerator burningShipFractal = new BurningShip();
        myComboBox.addItem(burningShipFractal);

        // Экземпляр ButtonHandler ComboBox
        ButtonHandler fractalChoose = new ButtonHandler();
        myComboBox.addActionListener(fractalChoose);

        /**
         * Создается объект JPanel, JLaber, JComboBox
         * Добавляется в окне сверху
         */
        JPanel myPanel = new JPanel();
        JLabel myLabel = new JLabel("Fractal - ");
        myPanel.add(myLabel);
        myPanel.add(myComboBox);
        myFrame.add(myPanel, BorderLayout.NORTH);

        // Создаем кнопку Save и устанавливаем внизу
        // рядом с Reset
        saveButton = new JButton("Save");
        JPanel myBottomPanel = new JPanel();
        myBottomPanel.add(saveButton);
        myBottomPanel.add(resetButton);
        myFrame.add(myBottomPanel, BorderLayout.SOUTH);

        // Экземпляр ButtonHandler для Save
        ButtonHandler saveHandler = new ButtonHandler();
        saveButton.addActionListener(saveHandler);

        //Делаем все видимым и запрет изменения размера окна
        myFrame.pack();
        myFrame.setVisible(true);
        myFrame.setResizable(false);
    }

    /**
     * Отображения фрактала. Этот метод зацикливается
     * через каждый пиксель на дисплее и вычисляет количество
     * итераций для соответствующих координат во фрактале
     * Если количество итераций равно -1, установи цвет пикселя.
     * в черный. В противном случае выбери значение в зависимости от количества итераций.
     * Когда все пиксели нарисованы, JImageDisplay обновит цвет для каждого пикселя
     * и если нужно перекрасит
     */

    private void drawFractal() {
        // Отключаем все UI, пока рисуем
        enableUI(false);

        // ОСтавшиеся строки = текущее кол-во строк
        rowsRemaining = displaySize;

        // Проходимся по всем строкам и рисуем
        for (int x = 0; x < displaySize; x++) {
            FractalWorker drawRow = new FractalWorker(x);
            drawRow.execute();
        }

    }

    /**
     * Отключаем или включаем все кнопаки и ComboBox на основе входного boolean значения.
     */
    private void enableUI(boolean val) {
        myComboBox.setEnabled(val);
        resetButton.setEnabled(val);
        saveButton.setEnabled(val);
    }

    /**
     * Внутренний класс для обработки события ActionListener
     */
    private class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Узнаем комманду
            String command = e.getActionCommand();

            // Если она - ComboBox, то получем фрактал, который выбрали
            // и отображаем
            if (e.getSource() instanceof JComboBox) {
                JComboBox mySource = (JComboBox) e.getSource();
                fractal = (FractalGenerator) mySource.getSelectedItem();
                fractal.getInitialRange(range);
                drawFractal();

            }

            // Если источник - Reset, ресетим дисплей и рисуем фрактал
            else if (command.equals("Reset")) {
                fractal.getInitialRange(range);
                drawFractal();
            }
            // Если источник - Save, сохраняем фотку
            else if (command.equals("Save")) {

                // Юзер выбирает файл для сохранения фото
                JFileChooser myFileChooser = new JFileChooser();

                // Сохранение PNG
                FileFilter extensionFilter = new FileNameExtensionFilter("PNG Images", "png");
                myFileChooser.setFileFilter(extensionFilter);

                // Проверка на то, что сохраняется(выбрано) PDF
                myFileChooser.setAcceptAllFileFilterUsed(false);

                // Окошко Сохранения файла для выбора места, где его сохранить
                int userSelection = myFileChooser.showSaveDialog(display);

                // Если выбраное одобрено, то продолжаем дальше
                if (userSelection == JFileChooser.APPROVE_OPTION) {

                    // Берем файл и его имя
                    java.io.File file = myFileChooser.getSelectedFile();
                    String file_name = file.toString();

                    // Пытаемся его сохранить на диск
                    try {
                        BufferedImage displayImage = display.getImage();
                        javax.imageio.ImageIO.write(displayImage, "png", file);
                    }
                    //Ловим исключения и выводим ошибку в окошке
                    catch (Exception exception) {
                        JOptionPane.showMessageDialog(display, exception.getMessage(), "Cannot Save Image", JOptionPane.ERROR_MESSAGE);
                    }
                }
                // Все ок, выходим
                else return;
            }
        }
    }

    /**
     * Внутренний класс для обработки события MouseListener от дисплея
     */
    private class MouseHandler extends MouseAdapter {
        /**
         Когда обработчик получает событие щелчка мыши, он сопоставляет пиксельные
         * координаты щелчка с областью фрактала, который обрабатывается
         *  отображается, а затем вызывает повторный
         * метод с координатами, по которым был сделан щелчок, и масштабом 0,5.
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            // Если есть еще строки, то выходим.
            if (rowsRemaining != 0) {
                return;
            }
            /** x координата дисплея нажатия кнопки */
            int x = e.getX();
            double xCoord = fractal.getCoord(range.x,
                    range.x + range.width, displaySize, x);

            /** y координата дисплея нажатия кнопки */
            int y = e.getY();
            double yCoord = fractal.getCoord(range.y,
                    range.y + range.height, displaySize, y);

            /**
             * Вызов recternAndZoomRange() с данными координатами
             */
            fractal.recenterAndZoomRange(range, xCoord, yCoord, 0.5);

            /**
             * Перерисовка фрактала
             */
            drawFractal();
        }
    }

    /**
     * Просчет цветов для каждой строки фрактала
     */
    private class FractalWorker extends SwingWorker<Object, Object> {
        int yCoordinate;

        // Массив для запоминания просчитанных значение RGB
        // для каждого пикселя в строке
        int[] computedRGBValues;


        // Конструктор берет строку и хранит ее
        private FractalWorker(int row) {
            yCoordinate = row;
        }

        // Считает все значения RGB всем пикселям в строке(в одной) и хранит их
        protected Object doInBackground() {

            computedRGBValues = new int[displaySize];

            // По всем пикселям в строке
            for (int i = 0; i < computedRGBValues.length; i++) {

                //Находим соотв х и у в области фрактала
                double xCoord = fractal.getCoord(range.x, range.x + range.width, displaySize, i);
                double yCoord = fractal.getCoord(range.y, range.y + range.height, displaySize, yCoordinate);

                // Кол-во итераций для этих координат
                int iteration = fractal.numIterations(xCoord, yCoord);

                // Если -1, то вернем 0(черный)
                if (iteration == -1) {
                    computedRGBValues[i] = 0;
                } else {
                    // Выбираем оттенок в зависимости от значения итерации
                    float hue = 0.7f + (float) iteration / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);

                    // Добавляем в массив
                    computedRGBValues[i] = rgbColor;
                }
            }
            return null;

        }

        /**
         * Рисуем пиксели текущей строки и обновляем эту строчку
         */
        protected void done() {
            // Перебирать массив данных строк, рисуя в пикселях
            // которые были вычислены в doInBackground(). Перерисовать строку
            // Проходим по массиву, перерисовывам строку
            for (int i = 0; i < computedRGBValues.length; i++) {
                display.drawPixel(i, yCoordinate, computedRGBValues[i]);
            }
            display.repaint(0, 0, yCoordinate, displaySize, 1);

            // Уменьшая кол-во строк, если все отработало хорошо (будет 0 строк), то мы включаем UI
            rowsRemaining--;
            if (rowsRemaining == 0) {
                enableUI(true);
            }
        }
    }


    public static void main(String[] args) {
        FractalExplorer displayExplorer = new FractalExplorer(600);
        displayExplorer.createAndShowGUI();
        displayExplorer.drawFractal();
    }
}