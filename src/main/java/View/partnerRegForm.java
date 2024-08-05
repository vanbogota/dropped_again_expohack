package View;

import DAO.DBoperationsForPartners;
import Model.partnerModel;
import org.apache.batik.swing.JSVGCanvas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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

        setSize(500, 475); // Увеличиваем высоту окна для нового поля
        setLayout(null);
        setTitle("Регистрация партнера");
        setResizable(false);
        setLocationRelativeTo(null);
        // Установка цвета
        Color labelColor = new Color(0, 0, 0); // Черный цвет для меток

        // Поле для имени
        JLabel nameLabel = new JLabel("Наим.:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setBounds(50, 80, 100, 30);
        nameLabel.setForeground(labelColor);
        add(nameLabel);
        JTextField nameField = new JTextField();
        nameField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        nameField.setBounds(150, 80, 250, 30);
        add(nameField);

        // Поле для пароля
        JLabel passwordLabel = new JLabel("Пароль:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 16));
        passwordLabel.setBounds(50, 130, 100, 30);
        passwordLabel.setForeground(labelColor);
        add(passwordLabel);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        passwordField.setBounds(150, 130, 250, 30);
        add(passwordField);

        // Поле для описания
        JLabel descriptionLabel = new JLabel("Описание:");
        descriptionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        descriptionLabel.setBounds(50, 180, 100, 30);
        descriptionLabel.setForeground(labelColor);
        add(descriptionLabel);
        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        descriptionArea.setBounds(150, 180, 250, 100);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        add(descriptionArea);

        // Поле для адреса
        JLabel addressLabel = new JLabel("Адрес:");
        addressLabel.setFont(new Font("Arial", Font.BOLD, 16));
        addressLabel.setBounds(50, 300, 100, 30);
        addressLabel.setForeground(labelColor);
        add(addressLabel);
        JTextField addressField = new JTextField();
        addressField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        addressField.setBounds(150, 300, 250, 30);
        add(addressField);

        // Поле для даты основания
        JLabel dateLabel = new JLabel("Дата основания:");
        dateLabel.setFont(new Font("Arial", Font.BOLD, 14));
        dateLabel.setBounds(30, 350, 150, 30);
        dateLabel.setForeground(labelColor);
        add(dateLabel);
        JTextField dateField = new JTextField();
        dateField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        dateField.setBounds(150, 350, 250, 30);
        add(dateField);

        // Кнопка "Отправить!"
        JButton submitButton = new JButton("Отправить!");
        submitButton.setBounds(175, 400, 150, 40);
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
                callDBInsert dbInsertThread = new callDBInsert(partner, operations);
                dbInsertThread.start();
                try {
                    dbInsertThread.join();
                    dispose();
                } catch (InterruptedException exc) {
                    exc.printStackTrace();
                }
            }
        });

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
                crmWindow crmWindow = context.getBean("crm", crmWindow.class);
                crmWindow.setContext(context);
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