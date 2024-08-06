package View.productWindows;

import DAO.DBoperationsForProducts;
import Model.productModel;
import config.DatasLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

@Component("productForm")
@Scope("prototype")
public class addProduct extends JFrame {
    private DBoperationsForProducts operations;
    @Autowired
    public void setOperations(DBoperationsForProducts operations) {
        this.operations = operations;
    }
    private JTable table;
    private DefaultTableModel model;

    @PostConstruct
    public void createWindow() {
        setTitle("Добавить продукт");
        setSize(600, 200);
        setLayout(new BorderLayout());

        model = new DefaultTableModel();
        model.addColumn("Наим.");
        model.addColumn("Описание");
        model.addColumn("Цена");
        model.addColumn("Акции/Скидка");
        model.addColumn("Наличие акций/скидок");

        table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }

            @Override
            public TableCellEditor getCellEditor(int row, int column) {
                if (column == 4) {
                    return new DefaultCellEditor(new JComboBox<>(new String[]{"true", "false"}));
                }
                return super.getCellEditor(row, column);
            }

            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                if (column == 4) {
                    return new DefaultTableCellRenderer();
                }
                return super.getCellRenderer(row, column);
            }
        };
        table.setBorder(new LineBorder(Color.BLACK));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JButton addButton = new JButton("Добавить продукт");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.addRow(new Object[]{"", "", "", "", false});
            }
        });
        add(addButton, BorderLayout.NORTH);

        JButton saveButton = new JButton("Добавить в базу данных");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < model.getRowCount(); i++) {
                    productModel product = new productModel();
                    DatasLoader datasLoader = new DatasLoader("src/resources/datas.properties");
                    String name = (String) model.getValueAt(i, 0);
                    String description = (String) model.getValueAt(i, 1);
                    String price = (String) model.getValueAt(i, 2);
                    String promotions = "";
                    long partnerId = Long.parseLong(String.valueOf(datasLoader.getUserId()));

                    // Измените здесь
                    String hasPromotionString = (String) model.getValueAt(i, 4);
                    boolean hasPromotion = Boolean.parseBoolean(hasPromotionString);

                    if (hasPromotion) {
                        promotions = (String) model.getValueAt(i, 3);
                    } else {
                        promotions = "Данный продукт не имеет активных акций";
                    }
                    product.setName(name);
                    product.setDescriptions(description);
                    product.setPrice(price);
                    product.setPromotions(promotions);
                    product.setPartnerId(partnerId);
                    operations.addProductToDB(product);
                }
                dispose();
            }
        });
        add(saveButton, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
