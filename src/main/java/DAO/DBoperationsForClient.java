package DAO;

import Model.clientModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.RowMapper;
import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@PropertySource("classpath:datas.properties")
public class DBoperationsForClient {
    private final Environment environment;
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public DBoperationsForClient(Environment environment, JdbcTemplate jdbcTemplate) {
        this.environment = environment;
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean addClientToDB(clientModel client){
        if (alsoWas(client.getClientName(), client.getBirthDate())) {
            int response = JOptionPane.showConfirmDialog(
                    null,
                    "Вероятнее всего, такой клиент уже существует. Перепроверьте его данные или спросите его. Хотите продолжить создание нового клиента?(Да-создать нового клиента,Нет-увидеть список клиентов с такими же данными)",
                    "Подтверждение",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (response == JOptionPane.NO_OPTION) {
                return false;
            }
        }
        jdbcTemplate.update("INSERT INTO clients (client_name, birthdate, gender, c_type, income, mobile_phone, email, address, workplace_income_amount, communication_history, interests, preferences, registration_date, status, dialogue) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                client.getClientName(), client.getBirthDate(), client.getGender(), client.getClientType(), client.getIncome(), client.getMobile_phone(), client.getEmail(), client.getAddress(), client.getWorkplace_income_amount(), client.getCommunication_history(),
                client.getInterests(), client.getPreferences(), client.getRegistration_date(), client.getStatus(), client.getDialogue());

        return true;
    }
    public boolean alsoWas(String name, String birthdate) {
        String sql = "SELECT COUNT(*) FROM clients WHERE client_name = ? AND birthdate = ?";

        // Выполняем запрос и получаем количество найденных записей
        int count = jdbcTemplate.queryForObject(sql, new Object[]{name, birthdate}, Integer.class);

        // Если количество найденных записей больше 0, значит клиент существует
        return count > 0;
    }
    public List<clientModel> listOfExistsClients(String name,String birthdate){
        String sql = "SELECT * FROM clients WHERE client_name = ? AND birthdate = ?";
        List<clientModel> list = jdbcTemplate.query(sql, new Object[]{name, birthdate}, new RowMapper<clientModel>() {
            @Override
            public clientModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                clientModel client = new clientModel();
                client.setId(rs.getLong("client_id"));
                client.setClientName(rs.getString("client_name"));
                client.setBirthDate(rs.getString("birthdate"));
                client.setGender(rs.getString("gender"));
                client.setClientType(rs.getString("c_type"));
                client.setIncome(rs.getString("income"));
                client.setMobile_phone(rs.getString("mobile_phone"));
                client.setEmail(rs.getString("email"));
                client.setAddress(rs.getString("address"));
                client.setWorkplace_income_amount(rs.getString("workplace_income_amount"));
                client.setCommunication_history(rs.getString("communication_history"));
                client.setInterests(rs.getString("interests"));
                client.setPreferences(rs.getString("preferences"));
                client.setRegistration_date(rs.getString("registration_date"));
                client.setStatus(rs.getString("status"));
                client.setDialogue(rs.getString("dialogue"));
                return client;
            }
        });

        return list;
    }
    public clientModel getClientById(long id) {
        String sql = "SELECT * FROM clients WHERE client_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new RowMapper<clientModel>() {
            @Override
            public clientModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                clientModel client = new clientModel();
                client.setId(rs.getLong("client_id"));
                client.setClientName(rs.getString("client_name"));
                client.setBirthDate(rs.getString("birthdate"));
                client.setGender(rs.getString("gender"));
                client.setClientType(rs.getString("c_type"));
                client.setIncome(rs.getString("income"));
                client.setMobile_phone(rs.getString("mobile_phone"));
                client.setEmail(rs.getString("email"));
                client.setAddress(rs.getString("address"));
                client.setWorkplace_income_amount(rs.getString("workplace_income_amount"));
                client.setCommunication_history(rs.getString("communication_history"));
                client.setInterests(rs.getString("interests"));
                client.setPreferences(rs.getString("preferences"));
                client.setRegistration_date(rs.getString("registration_date"));
                client.setStatus(rs.getString("status"));
                client.setDialogue(rs.getString("dialogue"));
                return client;
            }
        });
    }
    public void editClient(clientModel client, long id) {
        String sql = "UPDATE clients SET " +
                "client_name = ?, " +
                "birthdate = ?, " +
                "gender = ?, " +
                "c_type = ?, " +
                "income = ?, " +
                "mobile_phone = ?, " +
                "email = ?, " +
                "address = ?, " +
                "workplace_income_amount = ?, " +
                "communication_history = ?, " +
                "interests = ?, " +
                "preferences = ?, " +
                "registration_date = ?, " +
                "status = ?, " +
                "dialogue = ? " +
                "WHERE client_id = ?";

        jdbcTemplate.update(sql,
                client.getClientName(),
                client.getBirthDate(),
                client.getGender(),
                client.getClientType(),
                client.getIncome(),
                client.getMobile_phone(),
                client.getEmail(),
                client.getAddress(),
                client.getWorkplace_income_amount(),
                client.getCommunication_history(),
                client.getInterests(),
                client.getPreferences(),
                client.getRegistration_date(),
                client.getStatus(),
                client.getDialogue(),
                id // Указываем id клиента, которого нужно обновить
        );
    }
}
