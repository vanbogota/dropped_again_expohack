package View;

import DAO.DBoperationsForProducts;
import Model.productModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

@Component("productList")
@Scope("prototype")
public class productList extends JFrame {
    private DBoperationsForProducts operations;
    @Autowired
    public void setOperations(DBoperationsForProducts operations) {
        this.operations = operations;
    }

    @PostConstruct
    public void createWindow() {
        setTitle("Мое приложение");
        setSize(720, 435);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null); // Устанавливаем layout в null

        JButton deleteButton = new JButton("Удалить");
        JButton editButton = new JButton("Редактировать");
        add(deleteButton);
        add(editButton);

        String[] columnNames = {"ID","Имя", "Описание", "Цена", "Акции / скидки"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 50, 560, 300); // Устанавливаем размеры JScrollPane
        List<productModel> products = operations.getAllProducts();

        // Добавляем данные в модель таблицы
        for (productModel product : products) {
            Object[] row = {product.getProductId(),product.getName(), product.getDescriptions(), product.getPrice(), product.getPromotions()};
            tableModel.addRow(row);
        }
        add(scrollPane);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = getWidth();
                int height = getHeight();

                // Устанавливаем размеры и позиции кнопок в зависимости от размера окна
                int buttonWidth = width / 6; // ширина кнопки - 1/6 ширины окна
                int buttonHeight = height / 15; // высота кнопки - 1/15 высоты окна
                int buttonSpacing = 10; // отступ между кнопками и краем окна

                deleteButton.setBounds(buttonSpacing, buttonSpacing, buttonWidth, buttonHeight);
                editButton.setBounds(buttonSpacing + buttonWidth + buttonSpacing, buttonSpacing, buttonWidth, buttonHeight);

                // Устанавливаем размеры JScrollPane
                scrollPane.setBounds(buttonSpacing, buttonSpacing + buttonHeight + buttonSpacing, width - 20, height - buttonHeight - 70);
            }
        });
        setVisible(true);
    }

}
