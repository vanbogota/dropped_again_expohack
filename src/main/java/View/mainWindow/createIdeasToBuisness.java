package View.mainWindow;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component("ideasToBuisness")
@Scope("prototype")
public class createIdeasToBuisness extends JFrame {
    private JList<String> ideasList; // Список для отображения идей
    private DefaultListModel<String> listModel; // Модель списка для управления содержимым
    private String ideasString;

    public void setIdeasString(String ideasString) {
        this.ideasString = ideasString;
        createWindow();
    }

    public void createWindow() throws HeadlessException {
        setTitle("Идеи для бизнеса");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Центрируем окно на экране

        // Инициализация модели списка и списка
        listModel = new DefaultListModel<>();
        ideasList = new JList<>(listModel);
        ideasList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Разрешаем выбор только одной идеи

        // Разбиваем строку с идеями на массив и добавляем в модель списка
        String[] ideasArray = ideasString.split("\n"); // Предполагаем, что идеи разделены новой строкой
        for (String idea : ideasArray) {
            listModel.addElement(idea.trim()); // Удаляем лишние пробелы
        }

        // Создаем панель прокрутки для списка
        JScrollPane scrollPane = new JScrollPane(ideasList);
        scrollPane.setPreferredSize(new Dimension(380, 200)); // Устанавливаем размер панели прокрутки

        // Создаем кнопки

        // Обработчики событий для кнопок

        // Создаем панель для кнопок
        JPanel buttonPanel = new JPanel();
        // Добавляем компоненты в окно
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);

    }
}