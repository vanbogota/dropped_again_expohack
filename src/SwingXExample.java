import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwingXExample extends  JXFrame {

    JXTextField firstName_field, lastName_field, email_field;
    SwingXExample(){
        super("Форма клиента");
        super.setDefaultCloseOperation(JXFrame.EXIT_ON_CLOSE);
        super.setSize(400,500);

        Container container = super.getContentPane();
        container.setLayout(new GridLayout(5,2,10,10));

        JLabel firstName = new JLabel("Введите имя:");
        firstName_field = new JXTextField("");
        JLabel lastName = new JLabel("Enter name:");
        lastName_field = new JXTextField("");
        JLabel email = new JLabel("Email:");
        email_field = new JXTextField("@");

        container.add(firstName);
        container.add(firstName_field);
        container.add(lastName);
        container.add(lastName_field);
        container.add(email);
        container.add(email_field);

        JXButton send_button = new JXButton("Отправить");
        container.add(send_button);

        send_button.addActionListener(new SendXForm());
    }

    class SendXForm implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String firstName = firstName_field.getText();
            String lastName = lastName_field.getText();
            String email = email_field.getText();

            JOptionPane.showMessageDialog(null, "", "Форма отправлена", JOptionPane.PLAIN_MESSAGE);

        }
    }
}
