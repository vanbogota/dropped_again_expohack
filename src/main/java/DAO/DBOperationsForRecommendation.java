package DAO;

import Model.clientModel;
import Model.clientModelToRecomendations;
import Model.productModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class DBOperationsForRecommendation {
    private JdbcTemplate operations;
    @Autowired
    public DBOperationsForRecommendation(JdbcTemplate operations) {
        this.operations = operations;
    }
    public List<clientModelToRecomendations> getAllWhereId(int id){
        String sql = "SELECT * FROM recommendations WHERE reccomendedto=?";
        List<clientModelToRecomendations> list = operations.query(sql, new Object[]{id}, new RowMapper<clientModelToRecomendations>() {
            @Override
            public clientModelToRecomendations mapRow(ResultSet rs, int rowNum) throws SQLException {
                clientModelToRecomendations client = new clientModelToRecomendations();
                client.setReccomendationID(rs.getInt("recommendationsid"));
                client.setClient_ID(rs.getInt("clientid"));
                client.setProbability(rs.getString("probability"));
                client.setProduct_ID(rs.getInt("productid"));
                client.setRecomendedTo(rs.getInt("reccomendedto"));
                return client;
            }
        });
        return list;
    }
    public void addToReccomendations(clientModel client, String probability, productModel productModel, int reccomendedTo) {
        int clientId = (int) client.getId();
        int productId = (int) productModel.getProductId();
        operations.update("INSERT INTO recommendations (productId, clientId, probability, reccomendedTo) VALUES (?, ?, ?, ?)",
                productId, clientId, probability, reccomendedTo);
    }

    public productModel getProductById(int id) {
        String sql = "SELECT * FROM products WHERE product_id = ?";
        return operations.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            productModel product = new productModel();
            product.setProductId(rs.getInt("product_id"));
            product.setPartnerId(rs.getInt("partner_id"));
            product.setDescriptions(rs.getString("description")); // Пример поля
            product.setName(rs.getString("name")); // Пример поля
            product.setPrice(rs.getString("price")); // Пример поля
            product.setPromotions(rs.getString("promotions"));
            return product;
        });
    }
    public clientModel getClientById(int id) {
        String sql = "SELECT * FROM clients WHERE client_id = ?";
        return operations.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
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
        });
    }
    public void deleteAllWritesWhereClientId(int id) {
        String sql = "DELETE FROM recommendations WHERE clientId = ?";
        operations.update(sql, id);
    }
    public void deleteRecWhereId(int id) {
        String sql = "DELETE FROM recommendations WHERE recommendationsid = ?";
        operations.update(sql, id);
    }
    public int getClientByRecId(int id) {
        String sql = "SELECT clientId FROM recommendations WHERE recommendationsid = ?";

        // Используем queryForObject для получения clientId
        return operations.queryForObject(sql, new Object[]{id}, Integer.class);
    }

    public void deleteAllRECwithId(long id){
        String sql = "DELETE FROM recommendations WHERE productid = ?";
        operations.update(sql, id);
    }
    public clientModelToRecomendations getReccomendationById(int id){
            String sql = "SELECT * FROM recommendations WHERE recommendationsid = ?"; // Замените "reccomendations" на имя вашей таблицы

            return operations.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
                clientModelToRecomendations client = new clientModelToRecomendations();
                client.setReccomendationID(id);
                client.setClient_ID(rs.getInt("clientid"));
                client.setProbability(rs.getString("probability"));
                client.setProduct_ID(rs.getInt("productid"));
                client.setRecomendedTo(rs.getInt("reccomendedto"));
                return client;
            });
        }
}
