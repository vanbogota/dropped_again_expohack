package View.clientWindows;

import DAO.DBoperationsForClient;
import Model.clientModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

@Component("clientExist")
@Scope("prototype")
public class AlsoWasClients extends JFrame {
    private String birthDate;
    private String name;
    private ApplicationContext context;
    private DBoperationsForClient DAO;

    @Autowired
    public void setDAO(DBoperationsForClient DAO) {
        this.DAO = DAO;
    }

    public void setNameAndBirthDate(String birthDate, String name) {
        this.birthDate = birthDate;
        this.name = name;
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
        createWindow();
    }

    private void createWindow() {
        setTitle("Существующие клиенты");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Создаем заголовки столбцов
        String[] columnNames = {"ID", "Имя", "Дата рождения", "Номер телефона", "Адрес", "Почта"};

        // Создаем модель таблицы
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Все ячейки будут неизменяемыми
            }
        };

        // Получаем список клиентов из DAO
        List<clientModel> clients = DAO.listOfExistsClients(name, birthDate);
        // Добавляем данные клиентов в модель таблицы
        for (clientModel client : clients) {
            Object[] rowData = {
                    client.getId(), // Добавляем ID клиента
                    client.getClientName(),
                    client.getBirthDate(),
                    client.getMobile_phone(),
                    client.getAddress(),
                    client.getEmail()
            };
            tableModel.addRow(rowData);
        }

        // Создаем таблицу с моделью
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Добавляем слушатель для обработки двойного клика
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) { // Проверяем, был ли двойной клик
                    int row = table.getSelectedRow(); // Получаем выбранную строку
                    if (row != -1) { // Убедимся, что строка выбрана
                        Long id = (Long) tableModel.getValueAt(row, 0); // Получаем ID из первой колонки
                        // Показываем диалоговое окно с вопросом о изменении параметров клиента
                        int response = JOptionPane.showConfirmDialog(
                                null,
                                "Желаете изменить параметры данного клиента?",
                                "Подтверждение",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE
                        );
                        if (response == JOptionPane.YES_OPTION) {
                            clientModel clientModel = DAO.getClientById(id);
                            clientEditForm editForm=context.getBean("clientEditForm",clientEditForm.class);
                            editForm.setContextAndIdAndStartValues(context, Math.toIntExact(id),clientModel);
                        }
                    }
                }
            }
        });

        // Добавляем кнопку для закрытия окна
        JButton closeButton = new JButton("Закрыть");
        closeButton.addActionListener(e -> dispose());
        add(closeButton, BorderLayout.SOUTH);
        setVisible(true);
    }
}