package View.reccomendationsWindow;

import DAO.DBOperationsForRecommendation;
import DAO.DBoperationsForClient;
import Model.clientModel;
import Model.productModel;
import View.BlackBoxAI.getProbability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

@Component("recommendationWindow")
@Scope("prototype")
public class reccomendationWindow extends JFrame {
    // Поля для клиента
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
    // Поля для продукта
    private JTextField productNameField;
    private JTextField productDescriptionField;
    private JTextField productPriceField;
    private JTextField productPromotionsField;
    // Метки для продукта
    private JLabel productNameLabel;
    private JLabel productDescriptionLabel;
    private JLabel productPriceLabel;
    private JLabel productPromotionsLabel;
    // Кнопки
    private JButton editButton;
    private JButton deleteButton;
    private int idReccomendation;
    private DBoperationsForClient dBoperationsForClient;
    private DBOperationsForRecommendation dbOperationsForRecommendation;
    private ApplicationContext context;
    @Autowired
    public void setDbOperationsForRecommendation(DBOperationsForRecommendation dbOperationsForRecommendation) {
        this.dbOperationsForRecommendation = dbOperationsForRecommendation;
    }
    @Autowired
    public void setdBoperationsForClient(DBoperationsForClient dBoperationsForClient) {
        this.dBoperationsForClient = dBoperationsForClient;
    }

    public void setProductClientId(productModel product, clientModel client, int id,ApplicationContext context) {
        createWindow(product, client,id,context);
    }

    public void createWindow(productModel product, clientModel client, int id, ApplicationContext context) {
        this.context=context;
        idReccomendation=id;
        setTitle("Рекомендации");
        setSize(900, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null); // Устанавливаем layout в null для абсолютного позиционирования
        createClientFields(client);
        createProductFields(product);
        createButtons();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeComponents();
            }
        });
        setVisible(true);
    }

    private void createClientFields(clientModel client) {
        int padding = 10;
        // Имя клиента
        JLabel clientNameLabel = new JLabel("Имя клиента:");
        clientNameLabel.setBounds(10, 10, 150, 25);
        add(clientNameLabel);
        clientNameField = new JTextField(client.getClientName());
        clientNameField.setBounds(160, 10, 200 - padding, 25);
        clientNameField.setBorder(new LineBorder(Color.GRAY, 1)); // Добавляем границу
        add(clientNameField);
        // Дата рождения
        JLabel birthDateLabel = new JLabel("Дата рождения:");
        birthDateLabel.setBounds(10, 40, 150, 25);
        add(birthDateLabel);
        birthDateField = new JTextField(client.getBirthDate());
        birthDateField.setBounds(160, 40, 200 - padding, 25);
        birthDateField.setBorder(new LineBorder(Color.GRAY, 1)); // Добавляем границу
        add(birthDateField);
        // Пол клиента
        JLabel genderLabel = new JLabel("Пол клиента:");
        genderLabel.setBounds(10, 70, 150, 25);
        add(genderLabel);
        genderComboBox = new JComboBox<>(new String[]{"Мужчина", "Женщина"});
        genderComboBox.setSelectedItem(client.getGender());
        genderComboBox.setBounds(160, 70, 200 - padding, 25);
        add(genderComboBox);
        // Тип клиента
        JLabel clientTypeLabel = new JLabel("Тип клиента:");
        clientTypeLabel.setBounds(10, 100, 150, 25);
        add(clientTypeLabel);
        clientTypeComboBox = new JComboBox<>(new String[]{"Физическое лицо", "Юридическое лицо"});
        clientTypeComboBox.setSelectedItem(client.getClientType());
        clientTypeComboBox.setBounds(160, 100, 200 - padding, 25);
        add(clientTypeComboBox);
        // Уровень дохода
        JLabel incomeLabel = new JLabel("Уровень дохода:");
        incomeLabel.setBounds(10, 130, 150, 25);
        add(incomeLabel);
        incomeComboBox = new JComboBox<>(new String[]{"Низкий", "Средний", "Высокий"});
        incomeComboBox.setSelectedItem(client.getIncome());
        incomeComboBox.setBounds(160, 130, 200 - padding, 25);
        add(incomeComboBox);
        // Номер мобильного телефона
        JLabel mobilePhoneLabel = new JLabel("Номер мобильного телефона:");
        mobilePhoneLabel.setBounds(10, 160, 200, 25);
        add(mobilePhoneLabel);
        mobilePhoneField = new JTextField(client.getMobile_phone());
        mobilePhoneField.setBounds(210, 160, 150 - padding, 25);
        mobilePhoneField.setBorder(new LineBorder(Color.GRAY, 1)); // Добавляем границу
        add(mobilePhoneField);
        // Электронная почта
        JLabel emailLabel = new JLabel("Электронная почта:");
        emailLabel.setBounds(10, 190, 150, 25);
        add(emailLabel);
        emailField = new JTextField(client.getEmail());
        emailField.setBounds(160, 190, 200 - padding, 25);
        emailField.setBorder(new LineBorder(Color.GRAY, 1)); // Добавляем границу
        add(emailField);
        // Физический адрес
        JLabel addressLabel = new JLabel("Физический адрес:");
        addressLabel.setBounds(10, 220, 150, 25);
        add(addressLabel);
        addressField = new JTextField(client.getAddress());
        addressField.setBounds(160, 220, 200 - padding, 25);
        addressField.setBorder(new LineBorder(Color.GRAY, 1)); // Добавляем границу
        add(addressField);
        // Уровень дохода (цифры)
        JLabel workplaceIncomeLabel = new JLabel("Уровень дохода (цифры):");
        workplaceIncomeLabel.setBounds(10, 250, 200, 25);
        add(workplaceIncomeLabel);
        workplaceIncomeField = new JTextField(client.getWorkplace_income_amount());
        workplaceIncomeField.setBounds(210, 250, 150 - padding, 25);
        workplaceIncomeField.setBorder(new LineBorder(Color.GRAY, 1)); // Добавляем границу
        add(workplaceIncomeField);
        // История взаимодействий
        JLabel communicationHistoryLabel = new JLabel("История взаимодействий:");
        communicationHistoryLabel.setBounds(10, 280, 200, 25);
        add(communicationHistoryLabel);
        communicationHistoryArea = new JTextArea(client.getCommunication_history());
        communicationHistoryArea.setLineWrap(true);
        communicationHistoryArea.setWrapStyleWord(true);
        communicationHistoryArea.setBounds(10, 310, 350 - padding, 50);
        communicationHistoryArea.setBorder(new LineBorder(Color.GRAY, 1)); // Добавляем границу
        add(communicationHistoryArea);
        // Интересы клиента
        JLabel interestsLabel = new JLabel("Интересы клиента:");
        interestsLabel.setBounds(10, 370, 150, 25);
        add(interestsLabel);
        interestsArea = new JTextArea(client.getInterests());
        interestsArea.setLineWrap(true);
        interestsArea.setWrapStyleWord(true);
        interestsArea.setBounds(10, 400, 350 - padding, 50);
        interestsArea.setBorder(new LineBorder(Color.GRAY, 1)); // Добавляем границу
        add(interestsArea);
        // Предпочтения клиента
        JLabel preferencesLabel = new JLabel("Предпочтения клиента:");
        preferencesLabel.setBounds(10, 460, 200, 25);
        add(preferencesLabel);
        preferencesArea = new JTextArea(client.getPreferences());
        preferencesArea.setLineWrap(true);
        preferencesArea.setWrapStyleWord(true);
        preferencesArea.setBounds(10, 490, 350 - padding, 50);
        preferencesArea.setBorder(new LineBorder(Color.GRAY, 1)); // Добавляем границу
        add(preferencesArea);
        // Дата регистрации
        JLabel registrationDateLabel = new JLabel("Дата регистрации:");
        registrationDateLabel.setBounds(10, 550, 150, 25);
        add(registrationDateLabel);
        registrationDateField = new JTextField(client.getRegistration_date());
        registrationDateField.setBounds(160, 550, 200 - padding, 25);
        registrationDateField.setBorder(new LineBorder(Color.GRAY, 1)); // Добавляем границу
        add(registrationDateField);
        // Статус клиента
        JLabel statusLabel = new JLabel("Статус клиента:");
        statusLabel.setBounds(10, 580, 150, 25);
        add(statusLabel);
        statusComboBox = new JComboBox<>(new String[]{"Активный", "Неактивный", "Потенциальный"});
        statusComboBox.setSelectedItem(client.getStatus());
        statusComboBox.setBounds(160, 580, 200 - padding, 25);
        add(statusComboBox);
        // Диалог клиента
        JLabel dialogueLabel = new JLabel("Диалог клиента:");
        dialogueLabel.setBounds(10, 610, 150, 25);
        add(dialogueLabel);
        dialogueArea = new JTextArea(client.getDialogue());
        dialogueArea.setLineWrap(true);
        dialogueArea.setWrapStyleWord(true);
        dialogueArea.setBounds(10, 640, 350 - padding, 50);
        dialogueArea.setBorder(new LineBorder(Color.GRAY, 1)); // Добавляем границу
        add(dialogueArea);
    }

    private void createProductFields(productModel product) {
        int padding = 10;
        // Метки для продукта
        productNameLabel = new JLabel("Название продукта:");
        productNameLabel.setBounds(410, 10, 150, 25);
        add(productNameLabel);
        productNameField = new JTextField(product.getName());
        productNameField.setBounds(570, 10, 200 - padding, 25);
        productNameField.setBorder(new LineBorder(Color.GRAY, 1)); // Добавляем границу
        add(productNameField);
        productDescriptionLabel = new JLabel("Описание:");
        productDescriptionLabel.setBounds(410, 40, 150, 25);
        add(productDescriptionLabel);
        productDescriptionField = new JTextField(product.getDescriptions());
        productDescriptionField.setBounds(570, 40, 200 - padding, 25);
        productDescriptionField.setBorder(new LineBorder(Color.GRAY, 1)); // Добавляем границу
        add(productDescriptionField);
        productPriceLabel = new JLabel("Цена:");
        productPriceLabel.setBounds(410, 70, 150, 25);
        add(productPriceLabel);
        productPriceField = new JTextField(product.getPrice());
        productPriceField.setBounds(570, 70, 200 - padding, 25);
        productPriceField.setBorder(new LineBorder(Color.GRAY, 1)); // Добавляем границу
        add(productPriceField);
        productPromotionsLabel = new JLabel("Акции:");
        productPromotionsLabel.setBounds(410, 100, 150, 25);
        add(productPromotionsLabel);
        productPromotionsField = new JTextField(product.getPromotions());
        productPromotionsField.setBounds(570, 100, 200 - padding, 25);
        productPromotionsField.setBorder(new LineBorder(Color.GRAY, 1)); // Добавляем границу
        add(productPromotionsField);
    }

    private void createButtons() {
        editButton = new JButton("Изменить");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int clientId=dbOperationsForRecommendation.getClientByRecId(idReccomendation);
                clientModel updatedClient=new clientModel();
                updatedClient.setClientName(clientNameField.getText());
                updatedClient.setBirthDate(birthDateField.getText());
                updatedClient.setGender((String) genderComboBox.getSelectedItem());
                updatedClient.setClientType((String) clientTypeComboBox.getSelectedItem());
                updatedClient.setIncome((String) incomeComboBox.getSelectedItem());
                updatedClient.setMobile_phone(mobilePhoneField.getText());
                updatedClient.setEmail(emailField.getText());
                updatedClient.setAddress(addressField.getText());
                updatedClient.setWorkplace_income_amount(workplaceIncomeField.getText());
                updatedClient.setCommunication_history(communicationHistoryArea.getText());
                updatedClient.setInterests(interestsArea.getText());
                updatedClient.setPreferences(preferencesArea.getText());
                updatedClient.setRegistration_date(registrationDateField.getText());
                updatedClient.setStatus((String) statusComboBox.getSelectedItem());
                updatedClient.setDialogue(dialogueArea.getText());
                updatedClient.setId(clientId);
                dBoperationsForClient.editClient(updatedClient,clientId);
                dbOperationsForRecommendation.deleteAllWritesWhereClientId(clientId);
                getProbability probability = context.getBean("blackBoxAsk", getProbability.class);
                probability.createWindow(updatedClient);
                dispose();
            }
        });
        deleteButton = new JButton("Удалить");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dbOperationsForRecommendation.deleteRecWhereId(idReccomendation);
                dispose();
            }
        });
        // Устанавливаем начальные размеры и позиции кнопок
        int buttonWidth = 100;
        int buttonHeight = 25;

        // Устанавливаем фиксированные координаты для кнопок
        editButton.setBounds(680, 520, buttonWidth, buttonHeight);
        deleteButton.setBounds(680, 550, buttonWidth, buttonHeight);

        add(editButton);
        add(deleteButton);
    }

    private void resizeComponents() {
        int width = getWidth();
        int height = getHeight();

        // Пропорционально изменяем размеры полей
        int fieldHeight = Math.max(25, height / 30); // Минимальная высота 25, максимальная высота зависит от высоты окна
        int textAreaHeight = Math.max(50, height / 15); // Минимальная высота 50, максимальная высота зависит от высоты окна
        int padding = 10;

        // Изменяем размеры полей для клиента
        int clientFieldWidth = width - 600 - padding * 3; // Учитываем ширину секции продукта и отступы
        clientNameField.setSize(clientFieldWidth, fieldHeight);
        birthDateField.setSize(clientFieldWidth, fieldHeight);
        genderComboBox.setSize(clientFieldWidth, fieldHeight);
        clientTypeComboBox.setSize(clientFieldWidth, fieldHeight);
        incomeComboBox.setSize(clientFieldWidth, fieldHeight);
        mobilePhoneField.setSize(clientFieldWidth, fieldHeight);
        emailField.setSize(clientFieldWidth, fieldHeight);
        addressField.setSize(clientFieldWidth, fieldHeight);
        workplaceIncomeField.setSize(clientFieldWidth, fieldHeight);
        communicationHistoryArea.setSize(clientFieldWidth, textAreaHeight);
        interestsArea.setSize(clientFieldWidth, textAreaHeight);
        preferencesArea.setSize(clientFieldWidth, textAreaHeight);
        registrationDateField.setSize(clientFieldWidth, fieldHeight);
        statusComboBox.setSize(clientFieldWidth, fieldHeight);
        dialogueArea.setSize(clientFieldWidth, textAreaHeight);

        // Изменяем размеры и положение полей для продукта
        int productX = width - 300; // Фиксированное положение по оси X для полей продукта
        int productFieldWidth = 200 - padding;
        productNameField.setSize(productFieldWidth, fieldHeight);
        productDescriptionField.setSize(productFieldWidth, fieldHeight);
        productPriceField.setSize(productFieldWidth, fieldHeight);
        productPromotionsField.setSize(productFieldWidth, fieldHeight);

        // Устанавливаем фиксированные координаты по оси X для полей и меток продукта
        productNameField.setLocation(productX, 10);
        productNameField.setEditable(false);
        productDescriptionField.setLocation(productX, 40);
        productDescriptionField.setEditable(false);
        productPriceField.setLocation(productX, 70);
        productPriceField.setEditable(false);
        productPromotionsField.setLocation(productX, 100);
        productPromotionsField.setEditable(false);
        productNameLabel.setLocation(productX - 150, 10);
        productDescriptionLabel.setLocation(productX - 150, 40);
        productPriceLabel.setLocation(productX - 150, 70);
        productPromotionsLabel.setLocation(productX - 150, 100);

        // Изменяем размеры и положение кнопок
        int buttonWidth = 150;
        int buttonHeight = 40;
        int buttonX = width - buttonWidth - 35; // Положение кнопок по оси X
        int buttonY = height - buttonHeight - 100; // Положение кнопок по оси Y

        // Убедитесь, что кнопки находятся в пределах видимой области
        editButton.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
        deleteButton.setBounds(buttonX, buttonY + buttonHeight + 10, buttonWidth, buttonHeight);
    }
}