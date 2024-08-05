package DAO;

import config.DatasLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import Model.partnerModel;

@Component("PartnerOperations")
public class DBoperationsForPartners {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public DBoperationsForPartners(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public void insertINDB(partnerModel partnerModel){
        if(!existOrNo(partnerModel)){
        }
        try {
            String SQL = "INSERT INTO partners(name, description, address, date_of_foundation,password) VALUES(?, ?, ?, ?, ?)";
            jdbcTemplate.update(SQL, partnerModel.getName(), partnerModel.getDescription(), partnerModel.getAddress(),
                     partnerModel.getDateOfFoundations(),partnerModel.getPassword());
            Integer id = jdbcTemplate.queryForObject("SELECT id FROM partners WHERE name = ?",
                    new Object[]{partnerModel.getName()}, Integer.class);

            if (id != null) {
                partnerModel.setId(id);
                insertInProperties(partnerModel);
            } else {
                System.out.println("Пользователь с логином " + partnerModel.getName() + " не найден.");
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
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
        DatasLoader datasLoader = new DatasLoader("src/resources/datas.properties");
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
    public void insertInProperties(partnerModel partnerModel) {
        DatasLoader datasLoader = new DatasLoader("src/resources/datas.properties");
        datasLoader.setUserName(partnerModel.getName());
        datasLoader.setUserPassword(partnerModel.getPassword());
        datasLoader.setUserId(partnerModel.getId());
        datasLoader.save("src/resources/datas.properties"); // Сохраняем изменения
    }
}
