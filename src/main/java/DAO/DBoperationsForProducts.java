package DAO;

import Model.productModel;
import config.DatasLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class DBoperationsForProducts {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public DBoperationsForProducts(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public void addProductToDB(productModel product){
        String SQL="INSERT INTO products(name,description,price,promotions,partner_id) VALUES(?,?,?,?,?)";
        jdbcTemplate.update(SQL,product.getName(),product.getDescriptions(),product.getPrice(),product.getPromotions(),product.getPartnerId());
    }
    
    public List<productModel> getAllProductsWhereId() {
        DatasLoader datasLoader=new DatasLoader("src/main/java/config/datas.properties");
        String sql = "SELECT * FROM products WHERE partner_id = ?";
        long partnerId = Long.parseLong(datasLoader.getUserId());
        List<productModel> products = jdbcTemplate.query(sql, new Object[]{partnerId}, new BeanPropertyRowMapper<>(productModel.class));

        // Установка описания вручную для каждого продукта
        for (productModel product : products) {
            String sql2 = "SELECT description FROM products WHERE product_id = ?";
            String description = jdbcTemplate.queryForObject(sql2, new Object[]{product.getProductId()}, String.class);
            product.setDescriptions(description); // Установите нужное описание
        }
        return products;
    }
    public List<productModel> allProducts() {
        String sql = "SELECT * FROM products ";
        List<productModel> products = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(productModel.class));
        for (productModel product : products) {
            String sql2 = "SELECT description FROM products WHERE product_id = ?";
            String description = jdbcTemplate.queryForObject(sql2, new Object[]{product.getProductId()}, String.class);
            product.setDescriptions(description); // Установите нужное описание
        }
        return products;
    }
    public List<productModel> allProducts(int id) {
        String sql = "SELECT * FROM products WHERE partner_id=?";
        // Передаем id в запрос
        List<productModel> products = jdbcTemplate.query(sql, new Object[]{id}, new BeanPropertyRowMapper<>(productModel.class));

        for (productModel product : products) {
            String sql2 = "SELECT description FROM products WHERE product_id = ?";
            // Извлекаем описание для каждого продукта
            String description = jdbcTemplate.queryForObject(sql2, new Object[]{product.getProductId()}, String.class);
            product.setDescriptions(description); // Устанавливаем описание
        }

        return products;
    }
    public String getName(int id) {
        String sql = "SELECT name FROM partners WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, String.class);
    }
    public void deleteWhereId(long id){
        String sql = "DELETE FROM products WHERE product_id = ?";
        jdbcTemplate.update(sql, id);
    }
    public List<productModel> productList(int id) {
        String sqlWithPromotions = "SELECT * FROM products WHERE promotions != 'Данный продукт не имеет активных акций' AND partner_id = ?";
        String sqlWithoutPromotions = "SELECT * FROM products WHERE promotions = 'Данный продукт не имеет активных акций' AND partner_id = ?";

        // Получаем продукты с активными акциями
        List<productModel> productsWithPromotions = jdbcTemplate.query(sqlWithPromotions, new Object[]{id}, new BeanPropertyRowMapper<>(productModel.class));
        for (productModel product : productsWithPromotions) {
            String description = getDescriptionFromDatabase((int) product.getProductId());
            product.setDescriptions(description); // Устанавливаем полученное описание
        }
        // Получаем продукты без активных акций
        List<productModel> productsWithoutPromotions = jdbcTemplate.query(sqlWithoutPromotions, new Object[]{id}, new BeanPropertyRowMapper<>(productModel.class));
        for (productModel product : productsWithoutPromotions) {
            String description = getDescriptionFromDatabase((int) product.getProductId());
            product.setDescriptions(description); // Устанавливаем полученное описание
        }
        // Объединяем списки
        productsWithPromotions.addAll(productsWithoutPromotions);

        return productsWithPromotions;
    }
    private String getDescriptionFromDatabase(int productId) {
        String sql = "SELECT description FROM products WHERE product_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{productId}, String.class);
    }
}
