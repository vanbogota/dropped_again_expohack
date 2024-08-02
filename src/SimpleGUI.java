import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimpleGUI extends JFrame {

    JTextField firstName_field, lastName_field, email_field;
    public SimpleGUI(){
        super("Форма клиента");
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setSize(400,500);

        Container container = super.getContentPane();
        container.setLayout(new GridLayout(5,2,10,10));

        JLabel firstName = new JLabel("<html>" +
                "<span style='color: green; font-size: 14px;'>Отправить</span>" +
                "</html>" );
        firstName_field = new JTextField("",1);
        JLabel lastName = new JLabel("Enter name:");
        lastName_field = new JTextField("",1);
        JLabel email = new JLabel("Email:");
        email_field = new JTextField("@",1);

        container.add(firstName);
        container.add(firstName_field);
        container.add(lastName);
        container.add(lastName_field);
        container.add(email);
        container.add(email_field);

        JButton send_button = new JButton(
                "<html>" +
                "<span style='color: green; font-size: 14px;'>Отправить</span>" +
                "</html>" );

        container.add(send_button);

        send_button.addActionListener(new SendForm());
    }

    class SendForm implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            String firstName = firstName_field.getText();
            String lastName = lastName_field.getText();
            String email = email_field.getText();

            JOptionPane.showMessageDialog(null, "", "Форма отправлена", JOptionPane.PLAIN_MESSAGE);

        }
    }
}
