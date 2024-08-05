package View;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component("crm")
@Scope("prototype")
public class crmWindow extends JFrame {
    private JTable resultTable;
    private JScrollPane scrollPane;
    private static AnnotationConfigApplicationContext context;

    public void setContext(AnnotationConfigApplicationContext context) {
        crmWindow.context = context;
        createWindow();
    }

    public void createWindow() {
        setLocationRelativeTo(null);
        setTitle("Главное окно");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(null); // Устанавливаем null layout

        // Получаем размеры экрана
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        // Устанавливаем размеры панелей в зависимости от размеров экрана
        int leftPanelWidth = screenWidth / 2;
        int rightPanelWidth = screenWidth / 2;

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.LIGHT_GRAY);
        leftPanel.setLayout(null);
        leftPanel.setBounds(0, 0, leftPanelWidth, screenHeight);

        String[] columnNames = {"ФИО", "Статус лица", "Вероятность соответствия"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        resultTable = new JTable(tableModel);
        scrollPane = new JScrollPane(resultTable);
        scrollPane.setBounds(0, 0, leftPanelWidth, screenHeight);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        leftPanel.add(scrollPane);
        add(leftPanel);

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(null);
        rightPanel.setBounds(leftPanelWidth, 0, rightPanelWidth, screenHeight);


        JButton uploadButton = new JButton("Загрузить excel файл");
        uploadButton.setBounds(100, 10, 200, 30);
        uploadButton.addActionListener(new UploadButtonManager());
        rightPanel.add(uploadButton);

        JButton addClient = new JButton("Добавить клиента");
        addClient.setBounds(10, 50, 185, 30);
        rightPanel.add(addClient);

        JButton addProducts = new JButton("Добавить продукт");
        addProducts.setBounds(200, 50, 180, 30);
        addProducts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                context.getBean("productForm", addProduct.class);
            }
        });
        rightPanel.add(addProducts);

        JButton productList = new JButton("Список продуктов");
        productList.setBounds(10, 90, 185, 30);
        productList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                context.getBean("productList", productList.class);
            }
        });
        rightPanel.add(productList);

        JButton getResult = new JButton("Получить результат");
        getResult.setBounds(200, 90, 180, 30);
        getResult.addActionListener(new GetResultButtonManager());
        rightPanel.add(getResult);

        styleButton(getResult);
        styleButton(uploadButton);
        styleButton(addClient);
        styleButton(addProducts);
        styleButton(productList);
        add(rightPanel);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int newWidth = getWidth() / 2;
                leftPanel.setBounds(0, 0, newWidth, getHeight());
                rightPanel.setBounds(newWidth, 0, newWidth, getHeight());
                scrollPane.setBounds(0, 0, newWidth, getHeight()); // Обновляем размеры scrollPane
                revalidate();
                repaint();
            }
        });
        setVisible(true); // Изменено с super.setVisible(true) на setVisible(true)
    }

    class CreatePartnerWindow implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Логика добавления партнера
        }
    }

    class UploadButtonManager implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter xlsFilter = new FileNameExtensionFilter("Excel Files (*.xls)", "xls");
            FileNameExtensionFilter xlsxFilter = new FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx");
            FileNameExtensionFilter csvFilter = new FileNameExtensionFilter("CSV Files (*.csv)", "csv");
            fileChooser.addChoosableFileFilter(xlsFilter);
            fileChooser.addChoosableFileFilter(xlsxFilter);
            fileChooser.addChoosableFileFilter(csvFilter);
            fileChooser.setFileFilter(csvFilter);
            int result = fileChooser.showOpenDialog(crmWindow.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                List<String[]> data = readExcelFile(selectedFile);
                for (String[] row : data) {
                    System.out.println(String.join(", ", row)); // Печать строки
                }
            }
        }

        private List<String[]> readExcelFile(File file) {
            List<String[]> data = new ArrayList<>();
            try (Workbook workbook = new XSSFWorkbook(file)) {
                Sheet sheet = workbook.getSheetAt(0); // Получаем первый лист
                for (Row row : sheet) {
                    String[] rowData = new String[row.getPhysicalNumberOfCells()];
                    for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
                        Cell cell = row.getCell(i);
                        if (cell != null) {
                            rowData[i] = cell.toString(); // Преобразуем ячейку в строку
                        } else {
                            rowData[i] = ""; // Пустая ячейка
                        }
                    }
                    data.add(rowData);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidFormatException e) {
                throw new RuntimeException(e);
            }
            return data;
        }
    }

    class GetResultButtonManager implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Логика обработки
        }
    }

    public static void styleButton(JButton button) {
        button.setBorder(new LineBorder(Color.BLACK, 1));
    }
}