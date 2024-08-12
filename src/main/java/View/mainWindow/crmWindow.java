package View.mainWindow;

import DAO.DBOperationsForRecommendation;
import DAO.DBoperationsForClient;
import Model.clientModel;
import Model.clientModelToRecomendations;
import Model.productModel;
import View.BlackBoxAI.getProbability;
import View.BlackBoxAI.getProbabilityToNewProducts;
import View.clientWindows.clientForm;
import View.productWindows.addProduct;
import View.productWindows.productList;
import View.reccomendationsWindow.reccomendationWindow;
import config.DatasLoader;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component("crm")
@Scope("prototype")
public class crmWindow extends JFrame {
    private DatasLoader datasLoader = new DatasLoader("src/main/java/config/datas.properties");
    private JTable resultTable;
    private JScrollPane scrollPane;
    private DefaultTableModel tableModel;
    private static AnnotationConfigApplicationContext context;
    private boolean isDarkTheme = false; // Переменная для отслеживания текущей темы

    public void setContext(AnnotationConfigApplicationContext context) {
        crmWindow.context = context;
        createWindow();
    }

    private DBoperationsForClient DAOclient;
    @Autowired
    public void setDAO(DBoperationsForClient DAO) {
        this.DAOclient = DAO;
    }

    private DBOperationsForRecommendation DAO;
    @Autowired
    public void setDAO(DBOperationsForRecommendation DAO) {
        this.DAO = DAO;
    }

    public void refreshAlDatasInRec() {
        tableModel.setRowCount(0);
        List<clientModelToRecomendations> data = DAO.getAllWhereId(Integer.parseInt(datasLoader.getUserId()));
        for (clientModelToRecomendations client : data) {
            clientModel client2 = DAO.getClientById(client.getClient_ID());
            Object[] rowData = {client.getReccomendationID(), client2.getClientName(), client2.getClientType(), client.getProbability()};
            tableModel.addRow(rowData);
        }
    }

    public void createWindow() {
        setLocationRelativeTo(null);
        setTitle("Главное окно");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(null);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int leftPanelWidth = screenWidth / 2;
        int rightPanelWidth = screenWidth / 2;

        // Левая панель
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.LIGHT_GRAY);
        leftPanel.setLayout(null);
        leftPanel.setBounds(0, 0, leftPanelWidth, screenHeight);
        String[] columnNames = {"ID", "ФИО", "Статус лица", "Вероятность соответствия"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resultTable = new JTable(tableModel);
        scrollPane = new JScrollPane(resultTable);
        resultTable.getColumnModel().getColumn(0).setPreferredWidth(30); // ID
        resultTable.getColumnModel().getColumn(1).setPreferredWidth(150); // ФИО
        resultTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Статус лица
        resultTable.getColumnModel().getColumn(3).setPreferredWidth(200); // Вероятность соответствия
        resultTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Проверяем, что клик был двойным
                    int row = resultTable.getSelectedRow(); // Получаем выбранную строку
                    if (row != -1) { // Проверяем, что строка выбрана
                        Integer idValue = (Integer) tableModel.getValueAt(row, 0);
                        clientModelToRecomendations recomendations = DAO.getReccomendationById(idValue);
                        productModel product = DAO.getProductById(recomendations.getProduct_ID());
                        clientModel client = DAO.getClientById(recomendations.getClient_ID());
                        reccomendationWindow recWindow = context.getBean("recommendationWindow", reccomendationWindow.class);
                        recWindow.setProductClientId(product, client, idValue, context);
                    }
                }
            }
        });
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        scrollPane.setBounds(0, 0, leftPanelWidth, screenHeight - 10);
        leftPanel.add(scrollPane);
        add(leftPanel);

        // Правая панель
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(null);
        rightPanel.setBounds(leftPanelWidth, 0, rightPanelWidth, screenHeight);
        JLabel idLabel = new JLabel("ID= " + datasLoader.getUserId());
        idLabel.setBounds(10, 10, rightPanelWidth - 20, 30); // Позиция и размер JLabel
        rightPanel.add(idLabel);

        // Кнопка загрузки Excel файла
        JButton uploadButton = new JButton("Загрузить excel файл");
        uploadButton.setBounds((rightPanelWidth / 2) - (rightPanelWidth / 4), 30, rightPanelWidth / 2, 30); // Центрируем кнопку
        uploadButton.addActionListener(new UploadButtonManager());
        rightPanel.add(uploadButton);

        // Кнопка добавления клиента
        JButton addClient = new JButton("Добавить клиента");
        addClient.setBounds(10, 90, rightPanelWidth / 2 - 15, 30);
        addClient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientForm clientForm = context.getBean("clientADD", clientForm.class);
                clientForm.setContext(context);
            }
        });
        rightPanel.add(addClient);

        // Кнопка добавления продукта
        JButton addProducts = new JButton("Добавить продукт");
        addProducts.setBounds(rightPanelWidth / 2 + 5, 70, rightPanelWidth / 2 - 15, 30);
        addProducts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                context.getBean("productForm", addProduct.class);
            }
        });
        rightPanel.add(addProducts);

        // Кнопка списка продуктов
        JButton list = new JButton("Список продуктов");
        list.setBounds(10, 110, rightPanelWidth - 20, 30);
        list.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                productList productList = context.getBean("productList", View.productWindows.productList.class);
                productList.setContext(context);
            }
        });
        rightPanel.add(list);

        // Кнопка лидогенерации
        JButton getResult = new JButton("Лидогенерация");
        getResult.setBounds(10, 150, rightPanelWidth - 20, 30);
        getResult.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshAlDatasInRec(); // Обновляем данные
            }
        });
        rightPanel.add(getResult);

        // Добавление кнопки "E-mail рассылка"
        JButton emailButton = new JButton("E-mail рассылка");
        emailButton.setBounds(10, 190, rightPanelWidth - 20, 30);
        emailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = resultTable.getSelectedRow(); // Получаем выбранную строку
                if (row != -1) { // Проверяем, что строка выбранаё
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    JOptionPane.showMessageDialog(crmWindow.this, "E-mail рассылка инициирована.");
                } else {
                    JOptionPane.showMessageDialog(crmWindow.this, "Похоже вы не выбрали человека,которому хотели бы выслать рассылку.");
                }
            }
        });
        rightPanel.add(emailButton);

        // Добавление кнопки "Получить рекомендацию по продуктам"
        JButton getRecommendationButton = new JButton("Получить рекомендацию по интересам пользователей");
        getRecommendationButton.setBounds(10, 230, rightPanelWidth - 20, 30);
        getRecommendationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> {
                    getProbabilityToNewProducts newProducts=context.getBean("blackBoxAsk2", getProbabilityToNewProducts.class);
                    newProducts.setContext(context);
                }).start();
            }
        });
        rightPanel.add(getRecommendationButton);

        // Кнопка переключения темы
        JButton themeToggleButton = new JButton("Темная тема");
        themeToggleButton.setBounds(10, 290, rightPanelWidth - 20, 30);
        themeToggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleTheme(themeToggleButton);
            }
        });
        rightPanel.add(themeToggleButton);
        styleButton(themeToggleButton);
        styleButton(getRecommendationButton);
        styleButton(getResult);
        styleButton(uploadButton);
        styleButton(addClient);
        styleButton(addProducts);
        styleButton(list);
        styleButton(emailButton); // Стиль для новой кнопки
        add(rightPanel);

        // Обработка изменения размера окна
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int newWidth = getWidth() / 2;
                leftPanel.setBounds(0, 0, newWidth, getHeight());
                rightPanel.setBounds(newWidth, 0, newWidth, getHeight());
                scrollPane.setBounds(0, 0, newWidth, getHeight());
                uploadButton.setBounds((newWidth / 2) - (newWidth / 4), 50, newWidth / 2, 30); // Центрируем кнопку
                addClient.setBounds(10, 90, newWidth / 2 - 15, 30);
                addProducts.setBounds(newWidth / 2 + 5, 90, newWidth / 2 - 15, 30);
                list.setBounds(10, 130, newWidth - 20, 30);
                getResult.setBounds(10, 170, newWidth - 20, 30);
                emailButton.setBounds(10, 210, newWidth - 20, 30); // Позиция для кнопки E-mail рассылки
                getRecommendationButton.setBounds(10, 250, newWidth - 20, 30); // Позиция для новой кнопки
                themeToggleButton.setBounds(10, 290, newWidth - 20, 30); // Позиция для кнопки переключения темы
                revalidate();
                repaint();
            }
        });
        setVisible(true);
    }

    private void toggleTheme(JButton themeToggleButton) {
        if (isDarkTheme) {
            setLightTheme();
            themeToggleButton.setText("Темная тема"); // Меняем текст кнопки на "Темная тема"
            themeToggleButton.setBackground(Color.LIGHT_GRAY); // Устанавливаем светлый фон для кнопки
            themeToggleButton.setForeground(Color.BLACK); // Устанавливаем черный текст для кнопки
        } else {
            setDarkTheme();
            themeToggleButton.setText("Светлая тема"); // Меняем текст кнопки на "Светлая тема"
            themeToggleButton.setBackground(Color.DARK_GRAY); // Устанавливаем темный фон для кнопки
            themeToggleButton.setForeground(Color.WHITE); // Устанавливаем белый текст для кнопки
        }
        isDarkTheme = !isDarkTheme; // Переключаем состояние темы
    }
    private void setDarkTheme() {
        getContentPane().setBackground(Color.DARK_GRAY);
        resultTable.setBackground(Color.DARK_GRAY);
        resultTable.setForeground(Color.WHITE);
        resultTable.getTableHeader().setForeground(Color.WHITE); // Цвет текста заголовков
        resultTable.getTableHeader().setBackground(Color.DARK_GRAY); // Цвет фона заголовков
        resultTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer(Color.WHITE, Color.DARK_GRAY));
        for (java.awt.Component component : getContentPane().getComponents()) {
            if (component instanceof JPanel) {
                component.setBackground(Color.DARK_GRAY);
                for (java.awt.Component subComponent : ((JPanel) component).getComponents()) {
                    setComponentColors(subComponent, Color.WHITE, Color.DARK_GRAY);
                }
            }
        }
        resultTable.repaint(); // Обновляем таблицу
    }

    private void setLightTheme() {
        getContentPane().setBackground(Color.WHITE); // Устанавливаем цвет фона окна на белый
        resultTable.setBackground(Color.WHITE); // Устанавливаем цвет фона таблицы на белый
        resultTable.setForeground(Color.BLACK); // Устанавливаем цвет текста таблицы на черный
        resultTable.getTableHeader().setForeground(Color.BLACK); // Цвет текста заголовков
        resultTable.getTableHeader().setBackground(Color.WHITE); // Цвет фона заголовков
        resultTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer(Color.BLACK, Color.WHITE)); // Устанавливаем рендерер для таблицы
        for (java.awt.Component component : getContentPane().getComponents()) {
            if (component instanceof JPanel) {
                component.setBackground(Color.WHITE); // Устанавливаем цвет фона панелей на белый
                for (java.awt.Component subComponent : ((JPanel) component).getComponents()) {
                    setComponentColors(subComponent, Color.BLACK, Color.WHITE); // Устанавливаем цвета для всех компонентов
                }
            }
        }
        resultTable.repaint(); // Обновляем таблицу
    }

    private void setComponentColors(java.awt.Component component, Color textColor, Color backgroundColor) {
        if (component instanceof JButton) {
            component.setBackground(Color.WHITE); // Устанавливаем цвет фона кнопки на белый
            component.setForeground(textColor);
        } else if (component instanceof JLabel) {
            component.setForeground(textColor);
        } else if (component instanceof JTextField) {
            component.setBackground(Color.WHITE); // Устанавливаем цвет фона текстового поля на белый
            component.setForeground(textColor);
        }
        // Добавьте другие компоненты по мере необходимости
    }

    class CustomTableCellRenderer extends DefaultTableCellRenderer {
        private Color textColor;
        private Color backgroundColor;

        public CustomTableCellRenderer(Color textColor, Color backgroundColor) {
            this.textColor = textColor;
            this.backgroundColor = backgroundColor;
        }

        @Override
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            java.awt.Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            component.setForeground(textColor);
            component.setBackground(backgroundColor);
            return component;
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
                    String str = String.join("///delitel", row);
                    String[] masElements = str.split("///delitel");
                    clientModel clientModel = new clientModel();
                    clientModel.setClientName(masElements[0].trim());
                    clientModel.setBirthDate(masElements[1].trim());
                    clientModel.setGender(masElements[2].trim());
                    clientModel.setClientType(masElements[3].trim());
                    clientModel.setIncome(masElements[4].trim());
                    clientModel.setMobile_phone(masElements[5].trim());
                    clientModel.setEmail(masElements[6].trim());
                    clientModel.setAddress(masElements[7].trim());
                    clientModel.setWorkplace_income_amount(masElements[8].trim());
                    clientModel.setCommunication_history(masElements[9].trim());
                    clientModel.setInterests(masElements[10].trim());
                    clientModel.setPreferences(masElements[11].trim());
                    clientModel.setRegistration_date(masElements[12].trim());
                    clientModel.setStatus(masElements[13].trim());
                    clientModel.setDialogue(masElements[14].trim());
                    //--------Добавление в БД и составление рекомендаций--------
                    DAOclient.addClientToDB(clientModel);
                    getProbability probability = context.getBean("blackBoxAsk", getProbability.class);
                    probability.createWindow(clientModel);
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

    public static void styleButton(JButton button) {
        button.setBorder(new LineBorder(Color.BLACK, 1));
        button.setBackground(Color.LIGHT_GRAY); // Цвет фона кнопки
        button.setForeground(Color.BLACK); // Цвет текста кнопки
    }
}