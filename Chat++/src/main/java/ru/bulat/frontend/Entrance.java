package ru.bulat.frontend;

import ru.bulat.interfaces.PlaceholderInterface;
import ru.bulat.interfaces.StartInterface;
import ru.bulat.PlaceholderTextField;
import ru.bulat.data.DatabaseConnection;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class Entrance extends JFrame implements ActionListener, StartInterface, PlaceholderInterface {
    private static final String placeholderForEmail = "(Enter your email)";
    private static final String placeholderForPassword = "Password here!";
    private JPasswordField jPasswordField1;
    private JTextField jTextField2;


    private Entrance() {
        initComponents();
    }

    private void initComponents() {

        setResizable(false);
        setTitle("Entrance");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setContentPane(new BgPanel());

        jPasswordField1 = new JPasswordField();
        jTextField2 = new JTextField();
        JButton buttonRegistration = new JButton();
        JButton buttonEnter = new JButton();


        jTextField2.setText("(Enter your email)");
        jTextField2.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                PlaceholderTextField.textFieldFocusGained(e, jTextField2, placeholderForEmail);
            }

            @Override
            public void focusLost(FocusEvent e) {
                PlaceholderTextField.textFieldFocusLost(e, jTextField2, placeholderForEmail);
            }
        });

        jPasswordField1.setText(placeholderForPassword);
        jPasswordField1.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                textEmailFieldFocusGained(e, jPasswordField1);
            }

            @Override
            public void focusLost(FocusEvent e) {
                PlaceholderTextField.textPasswordFocusLost(e, jPasswordField1);
            }
        });
        addWindowListener(this);

        setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        buttonRegistration.setFont(new java.awt.Font("Times New Roman", Font.BOLD | Font.ITALIC, 16)); // NOI18N
        buttonRegistration.setText("Enter");
        buttonRegistration.addActionListener(this);

        buttonEnter.setFont(new java.awt.Font("Times New Roman", Font.BOLD | Font.ITALIC, 16)); // NOI18N
        buttonEnter.setText("Registration");
        buttonEnter.addActionListener(this);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(208, 208, 208)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(buttonRegistration)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(buttonEnter))
                                        .addComponent(jPasswordField1)
                                        .addComponent(jTextField2))
                                .addContainerGap(191, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(138, 138, 138)
                                .addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPasswordField1, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(buttonEnter, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(buttonRegistration, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(139, Short.MAX_VALUE))
        );

        jPasswordField1.getAccessibleContext().setAccessibleName("password");

        pack();
    }

    
    public static void main(String[] args) {
        CustomizeItemsJavaFX.start(Entrance.class);
        EventQueue.invokeLater(() -> new Entrance().setVisible(true));
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getActionCommand().equals("Enter")) {
            String nickname = DatabaseConnection.userVerification(jTextField2.getText().trim(), jPasswordField1.getText().trim());
            if (nickname != null){
                JOptionPane.showMessageDialog(Entrance.this, "Congratulations you are in the chat!");
                setVisible(false);
                Chat.goToChat(nickname);
            }
            else
                JOptionPane.showMessageDialog(Entrance.this, "Wrong login or password!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        if (evt.getActionCommand().equals("Registration")) {
            setVisible(false);
            Registration.goToRegistration();
        }
    }

    class BgPanel extends JPanel {
        public void paintComponent(Graphics g) {
            Image im = null;
            try {
                im = ImageIO.read(new File("src/main/resources/img/background/RickandMorty-Header.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            g.drawImage(im, 0, 0, null);
        }
    }
}
