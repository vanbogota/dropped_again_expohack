package View.mainWindow;

import DAO.DBoperationsForPartners;
import DAO.DBoperationsForProducts;
import Model.productModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Component("promotions")
@Scope("prototype")
public class promotionWindow extends JFrame {
    private DBoperationsForProducts DAO;
    private int id;

    @Autowired
    public void setDAO(DBoperationsForProducts DAO) {
        this.DAO = DAO;
    }

    public void setId(int id) {
        this.id = id;
        createWindow();
    }

    public void createWindow() {
        String partnerName = DAO.getName(id);
        setTitle("Таблица товаров со скидкой - " + partnerName);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);

        String[] columnNames = {"ID", "Наименование", "Описание", "Цена", "Акция"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Получаем список продуктов из DAO
        List<productModel> products = DAO.productList(id); // Получаем список продуктов из DAO

        // Заполняем модель данными из списка продуктов
        for (productModel product : products) {
            Object[] rowData = {
                    product.getProductId(),          // Метод getProductId() для получения ID продукта
                    product.getName(),               // Метод getName() для получения имени продукта
                    product.getDescriptions(),       // Метод getDescriptions() для получения описания продукта
                    product.getPrice(),              // Метод getPrice() для получения цены продукта
                    product.getPromotions()          // Метод getPromotions() для получения акций продукта
            };
            model.addRow(rowData); // Добавляем строку в модель
        }

        setVisible(true);
    }
}
