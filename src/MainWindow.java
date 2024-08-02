import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class MainWindow extends JFrame {

    public MainWindow(){

            // Создаем главное окно
            super("Главное окно");
            super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            super.setSize(800, 600);
            super.setLayout(new BorderLayout());



            // Создаем боковую панель
            JPanel leftPanel = new JPanel();
            leftPanel.setBackground(Color.LIGHT_GRAY);
            leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

            String[] columnNames = {"ФИО", "Статус лица", "Вероятность соответствия"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
            tableModel.addRow(new Object[]{"Данные 1-1", "Данные 1-2", "Данные 1-3"});
            tableModel.addRow(new Object[]{"Данные 2-1", "Данные 2-2", "Данные 2-3"});
            tableModel.addRow(new Object[]{"Данные 3-1", "Данные 3-2", "Данные 3-3"});
            tableModel.addRow(new Object[]{"Данные 4-1", "Данные 4-2", "Данные 4-3"});
            tableModel.addRow(new Object[]{"Данные 5-1", "Данные 5-2", "Данные 5-3"});

            JTable resultTable = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(resultTable);
            leftPanel.add(scrollPane);

            // Добавляем боковую панель в левую часть окна
            super.add(leftPanel, BorderLayout.WEST);

            // Создаем основное содержимое
            JPanel rightPanel = new JPanel();
            rightPanel.setBackground(Color.WHITE);
            super.add(rightPanel, BorderLayout.EAST);

            super.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    int newWidth = getWidth() / 2;
                    int newHeight = getHeight();
                    leftPanel.setPreferredSize(new Dimension(newWidth, newHeight));
                    rightPanel.setPreferredSize(new Dimension(newWidth, newHeight));
                    revalidate();
                }
            });

            super.setVisible(true);

    }


}
