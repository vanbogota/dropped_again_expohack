package DAO;

import config.DatasLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import Model.partnerModel;

import java.util.List;

@Component("PartnerOperations")
public class DBoperationsForPartners {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public DBoperationsForPartners(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public boolean insertINDB(partnerModel partnerModel){
        DatasLoader datasLoader = new DatasLoader("src/main/java/config/datas.properties");
        try {
            String SQL = "INSERT INTO partners(name, description, address, date_of_foundation,password,questionAboutUs) VALUES(?, ?, ?, ?, ?,?)";
            jdbcTemplate.update(SQL, partnerModel.getName(), partnerModel.getDescription(), partnerModel.getAddress(),
                     partnerModel.getDateOfFoundations(),partnerModel.getPassword(),partnerModel.getQuestion());
            Integer id = jdbcTemplate.queryForObject("SELECT id FROM partners WHERE name = ?",
                    new Object[]{partnerModel.getName()}, Integer.class);

            if (id != null) {
                datasLoader.setUserId(id);
                datasLoader.setUserName(partnerModel.getName());
                datasLoader.setUserPassword(partnerModel.getPassword());
                datasLoader.save("src/main/java/config/datas.properties");
                partnerModel.setId(id);
            } else {
                System.out.println("Пользователь с логином " + partnerModel.getName() + " не найден.");
            }
        } catch (DataAccessException  e) {
            e.printStackTrace();
        }
        return true;
    }
    public List<partnerModel> findAll() {
        String sql = "SELECT * FROM partners"; // Замените на ваш запрос
        List<partnerModel> partners = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(partnerModel.class));
        for (partnerModel partner : partners) {
            String sql2 = "SELECT questionAboutUs FROM partners WHERE id = ?";
            String question = jdbcTemplate.queryForObject(sql2, new Object[]{partner.getId()}, String.class);
            partner.setQuestion(question);
             }

        return partners;
    }
    public boolean existOrNo(partnerModel partnerModel){
        String SQL = "SELECT id FROM partners WHERE name = ?";
        try {
            jdbcTemplate.queryForObject(SQL, new Object[]{partnerModel.getName()}, Integer.class);
            return false;
        } catch (EmptyResultDataAccessException e) {
            return true;
        } catch (DataAccessException e) {
            return false;
        }
    }
    public boolean accessToLoginAUTO(){
        DatasLoader datasLoader = new DatasLoader("src/main/java/config/datas.properties");
        String SQL = "SELECT id FROM partners WHERE name = ? AND password = ?";
        try {
            Integer id = jdbcTemplate.queryForObject(SQL, new Object[]{datasLoader.getUserName(), datasLoader.getUserPassword()}, Integer.class);
            return id != null; // Если id не null, значит, пользователь найден
        } catch (EmptyResultDataAccessException e) {
            return false; // Пользователь не найден
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }
}
