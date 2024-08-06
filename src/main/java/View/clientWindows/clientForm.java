package View.clientWindows;

import DAO.DBoperationsForClient;
import View.BlackBoxAI.getProbability;
import Model.clientModel;
import View.mainWindow.salesWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

@Component("clientADD")
@Scope("prototype")
public class clientForm extends JFrame {
    private JTextField clientNameField;
    private JTextField birthDateField;
    private JComboBox<String> genderComboBox;
    private JComboBox<String> clientTypeComboBox;
    private JComboBox<String> incomeComboBox;
    private JTextField mobilePhoneField;
    private JTextField emailField;
    private JTextField addressField;
    private JTextField workplaceIncomeField;
    private JTextArea communicationHistoryArea;
    private JTextArea interestsArea;
    private JTextArea preferencesArea;
    private JTextField registrationDateField;
    private JComboBox<String> statusComboBox;
    private JTextArea dialogueArea;
    private JButton submitButton;
    private JButton clientQuestionsButton;
    private ApplicationContext context;

    private DBoperationsForClient DAO;
    @Autowired
    public void setDAO(DBoperationsForClient DAO) {
        this.DAO = DAO;
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
        createWindow();
    }

    public void createWindow() {
        setTitle("Форма ввода данных клиента");
        setSize(400, 900);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        setLayout(null); // Устанавливаем layout в null для абсолютного позиционирования

        int padding = 10;

        JLabel clientNameLabel = new JLabel("Имя клиента:");
        clientNameLabel.setBounds(10, 10, 150, 25);
        add(clientNameLabel);
        clientNameField = new JTextField();
        clientNameField.setBounds(160, 10, 200 - padding, 25); // Уменьшаем ширину на отступ
        clientNameField.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Добавляем границу
        add(clientNameField);

        JLabel birthDateLabel = new JLabel("Дата рождения:");
        birthDateLabel.setBounds(10, 40, 150, 25);
        add(birthDateLabel);
        birthDateField = new JTextField();
        birthDateField.setBounds(160, 40, 200 - padding, 25);
        birthDateField.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Добавляем границу
        add(birthDateField);

        JLabel genderLabel = new JLabel("Пол клиента:");
        genderLabel.setBounds(10, 70, 150, 25);
        add(genderLabel);
        genderComboBox = new JComboBox<>(new String[]{"Мужчина", "Женщина"});
        genderComboBox.setBounds(160, 70, 200 - padding, 25);
        add(genderComboBox);

        JLabel clientTypeLabel = new JLabel("Тип клиента:");
        clientTypeLabel.setBounds(10, 100, 150, 25);
        add(clientTypeLabel);
        clientTypeComboBox = new JComboBox<>(new String[]{"Физическое лицо", "Юридическое лицо"});
        clientTypeComboBox.setBounds(160, 100, 200 - padding, 25);
        add(clientTypeComboBox);

        JLabel incomeLabel = new JLabel("Уровень дохода:");
        incomeLabel.setBounds(10, 130, 150, 25);
        add(incomeLabel);
        incomeComboBox = new JComboBox<>(new String[]{"Низкий", "Средний", "Высокий"});
        incomeComboBox.setBounds(160, 130, 200 - padding, 25);
        add(incomeComboBox);

        JLabel mobilePhoneLabel = new JLabel("Номер мобильного телефона:");
        mobilePhoneLabel.setBounds(10, 160, 200, 25);
        add(mobilePhoneLabel);
        mobilePhoneField = new JTextField();
        mobilePhoneField.setBounds(210, 160, 150 - padding, 25);
        mobilePhoneField.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Добавляем границу
        add(mobilePhoneField);

        JLabel emailLabel = new JLabel("Электронная почта:");
        emailLabel.setBounds(10, 190, 150, 25);
        add(emailLabel);
        emailField = new JTextField();
        emailField.setBounds(160, 190, 200 - padding, 25);
        emailField.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Добавляем границу
        add(emailField);

        JLabel addressLabel = new JLabel("Физический адрес:");
        addressLabel.setBounds(10, 220, 150, 25);
        add(addressLabel);
        addressField = new JTextField();
        addressField.setBounds(160, 220, 200 - padding, 25);
        addressField.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Добавляем границу
        add(addressField);

        JLabel workplaceIncomeLabel = new JLabel("Уровень дохода(цифры):");
        workplaceIncomeLabel.setBounds(10, 250, 200, 25);
        add(workplaceIncomeLabel);
        workplaceIncomeField = new JTextField();
        workplaceIncomeField.setBounds(210, 250, 150 - padding, 25);
        workplaceIncomeField.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Добавляем границу
        add(workplaceIncomeField);

        JLabel communicationHistoryLabel = new JLabel("История взаимодействий:");
        communicationHistoryLabel.setBounds(10, 280, 200, 25);
        add(communicationHistoryLabel);
        communicationHistoryArea = new JTextArea();
        communicationHistoryArea.setBounds(10, 310, 350 - padding, 50);
        communicationHistoryArea.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Добавляем границу
        add(communicationHistoryArea);

        JLabel interestsLabel = new JLabel("Интересы клиента:");
        interestsLabel.setBounds(10, 370, 150, 25);
        add(interestsLabel);
        interestsArea = new JTextArea();
        interestsArea.setBounds(10, 400, 350 - padding, 50);
        interestsArea.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Добавляем границу
        add(interestsArea);

        JLabel preferencesLabel = new JLabel("Предпочтения клиента:");
        preferencesLabel.setBounds(10, 460, 200, 25);
        add(preferencesLabel);
        preferencesArea = new JTextArea();
        preferencesArea.setBounds(10, 490, 350 - padding, 50);
        preferencesArea.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Добавляем границу
        add(preferencesArea);

        JLabel registrationDateLabel = new JLabel("Дата регистрации:");
        registrationDateLabel.setBounds(10, 550, 150, 25);
        add(registrationDateLabel);
        registrationDateField = new JTextField();
        registrationDateField.setBounds(160, 550, 200 - padding, 25);
        registrationDateField.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Добавляем границу
        add(registrationDateField);

        JLabel statusLabel = new JLabel("Статус клиента:");
        statusLabel.setBounds(10, 580, 150, 25);
        add(statusLabel);
        statusComboBox = new JComboBox<>(new String[]{"Активный", "Неактивный", "Потенциальный"});
        statusComboBox.setBounds(160, 580, 200 - padding, 25);
        add(statusComboBox);

        JLabel dialogueLabel = new JLabel("Диалог клиента:");
        dialogueLabel.setBounds(10, 610, 150, 25);
        add(dialogueLabel);
        dialogueArea = new JTextArea();
        dialogueArea.setBounds(10, 640, 350 - padding, 50);
        dialogueArea.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Добавляем границу
        add(dialogueArea);

        submitButton = new JButton("Отправить");
        submitButton.setBounds(100, 700, 220, 40);
        add(submitButton);
        clientQuestionsButton = new JButton("Вопросы для клиента");
        clientQuestionsButton.setBounds(100, 750, 220, 40);
        clientQuestionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salesWindow salesWindow=context.getBean("salesWindow", salesWindow.class);
                salesWindow.setContext(context);
            }
        });
        add(clientQuestionsButton);

        // Обработчик события нажатия кнопки
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientModel  client=new clientModel();
                client.setClientName(clientNameField.getText());
                client.setBirthDate(birthDateField.getText());
                client.setGender((String) genderComboBox.getSelectedItem());
                client.setClientType((String) clientTypeComboBox.getSelectedItem());
                client.setIncome((String) incomeComboBox.getSelectedItem());
                client.setMobile_phone(mobilePhoneField.getText());
                client.setEmail(emailField.getText());
                client.setAddress(addressField.getText());
                client.setWorkplace_income_amount(workplaceIncomeField.getText());
                client.setCommunication_history(communicationHistoryArea.getText());
                client.setInterests(interestsArea.getText());
                client.setPreferences(preferencesArea.getText());
                client.setRegistration_date(registrationDateField.getText());
                client.setStatus((String) statusComboBox.getSelectedItem());
                client.setDialogue(dialogueArea.getText());

                if(DAO.addClientToDB(client)){
                    getProbability probability=context.getBean("blackBoxAsk",getProbability.class);
                    probability.createWindow(client);
                }else{
                    AlsoWasClients alsoWasClients=context.getBean("clientExist",AlsoWasClients.class);
                    alsoWasClients.setNameAndBirthDate(birthDateField.getText(),clientNameField.getText());
                    alsoWasClients.setContext(context);
                }
                dispose();
            }
        });

        // Обработчик изменения размера окна
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeComponents();
            }
        });
    }

    private void resizeComponents() {
        int width = getWidth();
        int height = getHeight();
        // Пропорционально изменяем размеры полей
        int fieldHeight = 25;
        int textAreaHeight = 50;

        // Уменьшаем ширину на отступ
        int padding = 10;

        clientNameField.setSize(width - 200 - padding, fieldHeight);
        birthDateField.setSize(width - 200 - padding, fieldHeight);
        genderComboBox.setSize(width - 200 - padding, fieldHeight);
        clientTypeComboBox.setSize(width - 200 - padding, fieldHeight);
        incomeComboBox.setSize(width - 200 - padding, fieldHeight);
        mobilePhoneField.setSize(width - 200 - padding, fieldHeight);
        emailField.setSize(width - 200 - padding, fieldHeight);
        addressField.setSize(width - 200 - padding, fieldHeight);
        workplaceIncomeField.setSize(width - 200 - padding, fieldHeight);
        communicationHistoryArea.setSize(width - 20 - padding, textAreaHeight);
        interestsArea.setSize(width - 20 - padding, textAreaHeight);
        preferencesArea.setSize(width - 20 - padding, textAreaHeight);
        registrationDateField.setSize(width - 200 - padding, fieldHeight);
        statusComboBox.setSize(width - 200 - padding, fieldHeight);
        dialogueArea.setSize(width - 20 - padding, textAreaHeight);

        // Обновляем размеры кнопок
        submitButton.setBounds((width - 250) / 2, height - 80, 100, 30);
        clientQuestionsButton.setBounds((width - 250) / 2 + 110, height - 80, 150, 30);
    }
}