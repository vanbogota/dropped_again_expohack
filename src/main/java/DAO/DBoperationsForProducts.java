package DAO;

import Model.productModel;
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
        return jdbcTemplate.query(sql, new Object[]{partnerId}, new BeanPropertyRowMapper<>(productModel.class));
    }
}
