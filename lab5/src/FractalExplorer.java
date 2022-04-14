import java.awt.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.geom.Rectangle2D;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * TODO: Соединяющий класс
 * Класс соединяет разные части реализации фрактала
 * путем создания и демонстрации Swing GUI и обработки
 * событий, связаных с взаимодействием с пользователем
 */
public class FractalExplorer {
    //Поля для кнопок save, reset, combo box
    private JButton saveButton;
    private JButton resetButton;
    private JComboBox myComboBox;

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
        JFrame myframe = new JFrame("Fractal Explorer");

        //Добавляем объект ImageDisplay
        myframe.add(display, BorderLayout.CENTER);

        //Кнопка ресета
        JButton resetButton = new JButton("Reset");
        ResetHandler handler = new ResetHandler();
        resetButton.addActionListener(handler);
        myframe.add(resetButton, BorderLayout.SOUTH);

        //Экземпляр MouseHandler
        MouseHandler click = new MouseHandler();
        display.addMouseListener(click);

        //Exit кнопка
        myframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
        ResetHandler fractalChoose = new ResetHandler();
        myComboBox.addActionListener(fractalChoose);

        /**
         * Создается объект JPanel, JLaber, JComboBox
         * Добавляется в окне сверху
         */
        JPanel myPanel = new JPanel();
        JLabel myLabel = new JLabel("Fractal - ");
        myPanel.add(myLabel);
        myPanel.add(myComboBox);
        myframe.add(myPanel, BorderLayout.NORTH);

        // Создаем кнопку Save и устанавливаем внизу
        // рядом с Reset
        saveButton = new JButton("Save");
        JPanel myBottomPanel = new JPanel();
        myBottomPanel.add(saveButton);
        myBottomPanel.add(resetButton);
        myframe.add(myBottomPanel, BorderLayout.SOUTH);

        // Экземпляр ButtonHandler для Save
        ResetHandler saveHandler = new ResetHandler();
        saveButton.addActionListener(saveHandler);

        //Делаем все видимым и запрет изменения размера окна
        myframe.pack();
        myframe.setVisible(true);
        myframe.setResizable(false);
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
        /** Проходимся по всем пикселям на дисплее */
        for (int x = 0; x < displaySize; x++) {
            for (int y = 0; y < displaySize; y++) {

                /** Находим подходящие координаты  в области фрактала */
                double xCoord = fractal.getCoord(range.x, range.x + range.width, displaySize, x);
                double yCoord = fractal.getCoord(range.y, range.y + range.height, displaySize, y);

                /**
                 * Считаем кол-во итераций для координат в области фрактала
                 */
                int iteration = fractal.numIterations(xCoord, yCoord);

                /**
                 * Кол-во -1, перекрашиваем в черный
                 */
                if (iteration == -1) {
                    display.drawPixel(x, y, 0);
                } else {

                    /**
                     * Выбор оттенка на основе кол-ва итераций
                     */
                    float hue = 0.7f + (float) iteration / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);

                    /** Обновляем цвет */
                    display.drawPixel(x, y, rgbColor);
                }

            }
        }

        /**
         * Все нарисовали, перекрашиваем JImageDisplay в соотв с текущим изображением
         */
        display.repaint();
    }

    /**
     * Внутренний класс для обработки события ActionListener с помощью кнопки ресет
     */
    private class ResetHandler implements ActionListener {

        /**
         * Обработчик сбрасывает диапазон до начального диапазона, заданного
         * генератором, а затем рисует фрактал.
         */
        public void actionPerformed(ActionEvent e) {
                // Узначем комманду
                String command = e.getActionCommand();

                // Если она - ComboBox, то получем фрактак, который выбрали
                // и отображаем
                if (e.getSource() instanceof JComboBox) {
                    JComboBox mySource = (JComboBox) e.getSource();
                    fractal = (FractalGenerator) mySource.getSelectedItem();
                    fractal.getInitialRange(range);
                    drawFractal();

                }
                // If the source is the reset button, reset the display and draw
                // the fractal.
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
         * Когда обработчик получает событие щелчка мыши, он отображает пиксельные
         * координаты щелчка в область фрактала, который
         * отображается, а затем вызывает recenterAndZoomRange()
         * метод с координатами, которые были нажаты.
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            /** x координата дисплея нажатия кнопки */
            int x = e.getX();
            double xCoord = fractal.getCoord(range.x, range.x + range.width, displaySize, x);

            /** y координата дисплея нажатия кнопки */
            int y = e.getY();
            double yCoord = fractal.getCoord(range.y, range.y + range.height, displaySize, y);

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

    public static void main(String[] args) {
        FractalExplorer displayExplorer = new FractalExplorer(600);
        displayExplorer.createAndShowGUI();
        displayExplorer.drawFractal();
    }
}