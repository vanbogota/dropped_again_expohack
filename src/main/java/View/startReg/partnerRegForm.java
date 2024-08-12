package View.startReg;

import DAO.DBoperationsForPartners;
import Model.partnerModel;
import View.mainWindow.crmWindow;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

@Component("regForm")
@Scope("prototype")
public class partnerRegForm extends JFrame {


    private static AnnotationConfigApplicationContext context;

    public void setContext(AnnotationConfigApplicationContext context) {
        partnerRegForm.context = context;
        mainWindow();
    }

    private final AtomicBoolean draw = new AtomicBoolean(true);

    private CountDownLatch latch;

    private DBoperationsForPartners operations;

    @Autowired
    public void setOperations(DBoperationsForPartners operations) {
        this.operations = operations;
    }

    public void mainWindow() {
        getPropertiesAndLogin autoLogin = new getPropertiesAndLogin(operations, draw);
        autoLogin.start();
        try {
            autoLogin.join();
            if (!draw.get()) {
                crmWindow crmWindow = context.getBean("crm", crmWindow.class);
                crmWindow.setContext(context);
                return;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        setSize(500, 525); // Увеличиваем высоту окна для нового поля
        setLayout(null);
        setTitle("Регистрация");
        setResizable(false);
        setLocationRelativeTo(null);
        // Установка цвета
        Color labelColor = new Color(0, 0, 0); // Черный цвет для меток

        // Поле для имени
        JLabel nameLabel = new JLabel("Наим.:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setBounds(50, 50, 100, 30); // Изменено
        nameLabel.setForeground(labelColor);
        add(nameLabel);
        JTextField nameField = new JTextField();
        nameField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        nameField.setBounds(150, 50, 250, 30); // Изменено
        add(nameField);

        // Поле для пароля
        JLabel passwordLabel = new JLabel("Пароль:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 16));
        passwordLabel.setBounds(50, 100, 100, 30); // Изменено
        passwordLabel.setForeground(labelColor);
        add(passwordLabel);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        passwordField.setBounds(150, 100, 250, 30); // Изменено
        add(passwordField);

        // Поле для описания
        JLabel descriptionLabel = new JLabel("Описание:");
        descriptionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        descriptionLabel.setBounds(50, 150, 100, 30); // Изменено
        descriptionLabel.setForeground(labelColor);
        add(descriptionLabel);
        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        descriptionArea.setBounds(150, 150, 250, 100); // Изменено
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        add(descriptionArea);

        // Поле для адреса
        JLabel addressLabel = new JLabel("Адрес:");
        addressLabel.setFont(new Font("Arial", Font.BOLD, 16));
        addressLabel.setBounds(50, 260, 100, 30); // Изменено
        addressLabel.setForeground(labelColor);
        add(addressLabel);
        JTextField addressField = new JTextField();
        addressField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        addressField.setBounds(150, 260, 250, 30); // Изменено
        add(addressField);

        // Поле для даты основания
        JLabel dateLabel = new JLabel("Дата основания:");
        dateLabel.setFont(new Font("Arial", Font.BOLD, 14));
        dateLabel.setBounds(30, 310, 150, 30); // Изменено
        dateLabel.setForeground(labelColor);
        add(dateLabel);
        JTextField dateField = new JTextField();
        dateField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        dateField.setBounds(150, 310, 250, 30); // Изменено
        add(dateField);

        // Поле "Вопрос о нас *"
        JLabel questionLabel = new JLabel("Вопрос о вас:");
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        questionLabel.setBounds(35, 360, 150, 30);
        questionLabel.setForeground(labelColor);
        questionLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showQuestionDialog(); // Вызов метода для отображения всплывающего окна
            }
        });
        add(questionLabel);
        JTextField questionField = new JTextField();
        questionField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        questionField.setBounds(150, 360, 250, 30);
        add(questionField);

        // Кнопка "Отправить!"
        JButton submitButton = new JButton("Отправить!");
        submitButton.setBounds(175, 410, 150, 40); // Изменено
        submitButton.setBackground(Color.WHITE); // Белый цвет кнопки
        submitButton.setForeground(labelColor); // Темно-голубой цвет текста
        add(submitButton);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                partnerModel partner = new partnerModel();
                partner.setName(nameField.getText()); // Получаем текст из поля имени
                partner.setDescription(descriptionArea.getText()); // Получаем текст из области описания
                partner.setAddress(addressField.getText()); // Получаем текст из поля адреса
                partner.setDateOfFoundations(dateField.getText()); // Получаем текст из поля даты основания
                partner.setPassword(new String(passwordField.getPassword())); // Получаем текст из поля пароля
                partner.setQuestion(questionField.getText()); // Получаем текст из поля вопроса
                callDBInsert dbInsertThread = new callDBInsert(partner, operations);
                dbInsertThread.start();
                try {
                    dbInsertThread.join();
                    dispose();
                } catch (InterruptedException exc) {
                    exc.printStackTrace();
                }
                crmWindow crmWindow = context.getBean("crm", crmWindow.class);
                crmWindow.setContext(context);
            }
        });
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void showQuestionDialog() {
        // Создаем текст для всплывающего окна
        String message = "Введите ваш вопрос о своей компании ,который позволял бы менеджеру задавать его клиентам";
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = optionPane.createDialog("Вопрос о вас");

        // Создаем кнопку "Продолжить"
        JButton continueButton = new JButton("Продолжить");
        continueButton.addActionListener(e -> dialog.dispose()); // Закрываем диалог при нажатии

        optionPane.setOptions(new Object[]{continueButton}); // Устанавливаем кнопку в качестве опции
        dialog.setVisible(true);
    }

    class callDBInsert extends Thread {
        private final partnerModel partner;
        private final DBoperationsForPartners dBoperationsForPartners;
        public callDBInsert(partnerModel partner, DBoperationsForPartners dBoperationsForPartners) {
            this.partner = partner;
            this.dBoperationsForPartners = dBoperationsForPartners;
        }

        @Override
        public void run() {
            if (!dBoperationsForPartners.existOrNo(partner)) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, "Пользователь с таким наименование уже присутствует!", "Ошибка 1372", JOptionPane.ERROR_MESSAGE);
                });
            } else {
                dBoperationsForPartners.insertINDB(partner);
            }
        }
    }

    class getPropertiesAndLogin extends Thread {
        private final DBoperationsForPartners dBoperationsForPartners;
        private final AtomicBoolean draw; // Ссылка на AtomicBoolean

        public getPropertiesAndLogin(DBoperationsForPartners dBoperationsForPartners, AtomicBoolean draw) {
            this.dBoperationsForPartners = dBoperationsForPartners;
            this.draw = draw;
        }

        @Override
        public void run() {
            if (dBoperationsForPartners.accessToLoginAUTO()) {
                draw.set(false); // Устанавливаем значение draw
                return;
            }
            draw.set(true);
        }
    }
}