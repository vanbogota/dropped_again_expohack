package View.mainWindow;

import DAO.DBoperationsForPartners;
import Model.partnerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

@Component("salesWindow")
@Scope("prototype")
public class salesWindow extends JFrame {
    private DBoperationsForPartners operations;
    private JTable table;
    private MyTableModel tableModel;
    private ApplicationContext context;

    public void setContext(ApplicationContext context) {
        this.context = context;
        createWindow();
    }

    @Autowired
    public void setOperations(DBoperationsForPartners operations) {
        this.operations = operations;
    }

    public void createWindow() {
        setTitle("Вопросы от партнеров");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 400);
        setLocationRelativeTo(null);

        String[] columnNames = {"id", "Наименование", "Описание", "Адрес", "Вопрос"};
        tableModel = new MyTableModel(columnNames, 0); // Используем наш кастомный модель
        table = new JTable(tableModel);

        // Установка ширины столбцов
        table.getColumnModel().getColumn(4).setPreferredWidth(300); // Вопрос - самое широкое
        table.getColumnModel().getColumn(1).setPreferredWidth(150); // Наименование
        table.getColumnModel().getColumn(2).setPreferredWidth(200); // Описание
        table.getColumnModel().getColumn(3).setPreferredWidth(150); // Адрес
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        int id = (int) table.getValueAt(row, 0);
                        promotionWindow promotions=context.getBean("promotions",promotionWindow.class);
                        promotions.setId(id);
                    }
                }
            }
        });

        // Добавление таблицы в JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Запускаем загрузку данных из базы данных
        new LoadDataFromDatabase(operations).execute();

        setVisible(true);
    }

    private class MyTableModel extends DefaultTableModel {
        public MyTableModel(Object[] columnNames, int rowCount) {
            super(columnNames, rowCount);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Ячейки не редактируемы
        }
    }

    private class LoadDataFromDatabase extends SwingWorker<List<partnerModel>, partnerModel> {
        private DBoperationsForPartners operations;

        public LoadDataFromDatabase(DBoperationsForPartners operations) {
            this.operations = operations;
        }

        @Override
        protected List<partnerModel> doInBackground() {
            return operations.findAll(); // Загружаем данные в фоновом режиме
        }

        @Override
        protected void process(List<partnerModel> chunks) {
            for (partnerModel entity : chunks) {
                tableModel.addRow(new Object[]{entity.getId(), entity.getName(), entity.getDescription(), entity.getAddress(), entity.getQuestion()});
            }
        }

        @Override
        protected void done() {
            try {
                // Получаем результат
                List<partnerModel> list = get();
                for (partnerModel entity : list) {
                    tableModel.addRow(new Object[]{entity.getId(), entity.getName(), entity.getDescription(), entity.getAddress(), entity.getQuestion()});
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}