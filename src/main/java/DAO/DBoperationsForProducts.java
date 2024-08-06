package DAO;

import Model.productModel;
import org.apache.commons.math3.stat.descriptive.summary.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@PropertySource("classpath:datas.properties")
public class DBoperationsForProducts {
    private final Environment environment;
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public DBoperationsForProducts(Environment environment, JdbcTemplate jdbcTemplate) {
        this.environment = environment;
        this.jdbcTemplate = jdbcTemplate;
    }
    public void addProductToDB(productModel product){
        String SQL="INSERT INTO products(name,description,price,promotions,partner_id) VALUES(?,?,?,?,?)";
        jdbcTemplate.update(SQL,product.getName(),product.getDescriptions(),product.getPrice(),product.getPromotions(),product.getPartnerId());
    }
    public List<productModel> getAllProducts() {
        String sql = "SELECT * FROM products WHERE partner_id = ?";
        Long partnerId = Long.valueOf(environment.getProperty("user.id"));
        List<productModel> products = jdbcTemplate.query(sql, new Object[]{partnerId}, new BeanPropertyRowMapper<>(productModel.class));

        // Установка описания вручную для каждого продукта
        for (productModel product : products) {
            String sql2 = "SELECT description FROM products WHERE product_id = ?";
            String description = jdbcTemplate.queryForObject(sql2, new Object[]{product.getProductId()}, String.class);
            product.setDescriptions(description); // Установите нужное описание
        }
        return products;
    }
    public String getName(int id) {
        String sql = "SELECT name FROM partners WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, String.class);
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
