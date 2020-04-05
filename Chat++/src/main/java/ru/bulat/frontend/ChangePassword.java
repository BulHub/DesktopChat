package ru.bulat.frontend;

import ru.bulat.data.DatabaseConnection;
import ru.bulat.interfaces.WindowListenerNotExit;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

class ChangePassword extends JFrame implements WindowListenerNotExit {
    private JPasswordField fieldRePassword;
    private JPasswordField fieldPassword;
    private static JTextField fieldNickname;
    private static String jNickname;
    private static String jEmail;

    private ChangePassword() {
        initComponents();
    }

    private void initComponents() {
        addWindowListener(this);
        setTitle("Change Password");
        setContentPane(new ChangePassword.BgPanel3());
        setResizable(false);
        JLabel labelNickname = new JLabel();
        JLabel labelEmail = new JLabel();
        fieldNickname = new JTextField();
        JButton change = new JButton();
        JLabel labelTitle = new JLabel();
        JLabel labelPassword = new JLabel();
        JLabel labelRePassword = new JLabel();
        fieldRePassword = new JPasswordField();
        fieldPassword = new JPasswordField();
        JTextField fieldEmail = new JTextField();

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        labelNickname.setFont(new java.awt.Font("Times New Roman", Font.PLAIN, 16)); // NOI18N
        labelNickname.setText("  Nickname:");

        labelEmail.setFont(new java.awt.Font("Times New Roman", Font.PLAIN, 16)); // NOI18N
        labelEmail.setText("Email:");

        fieldNickname.setEditable(false);
        fieldNickname.setBackground(new Color(255, 255, 51));

        change.setFont(new java.awt.Font("Times New Roman", Font.BOLD | Font.ITALIC, 14));
        change.setText("Change");
        change.addActionListener(evt -> {
            StringBuilder builder = new StringBuilder();
            if (!(fieldPassword.getText()).equals(fieldRePassword.getText())) builder.append("Passwords do not match.\n");
            if (fieldPassword.getText().length() < 6 || fieldPassword.getText().length() > 20) builder.append("Password must be between 6 and 20 characters.");
            if (builder.toString().equals("")){
                DatabaseConnection.changePassword(fieldPassword.getText(), fieldNickname.getText().trim());
                JOptionPane.showMessageDialog(ChangePassword.this, "The password was successfully changed!");
            }else{
                JOptionPane.showMessageDialog(ChangePassword.this, builder, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        labelTitle.setFont(new Font("Georgia", Font.BOLD, 18));
        labelTitle.setText("Change Password");

        labelPassword.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        labelPassword.setText("Password:");

        labelRePassword.setFont(new java.awt.Font("Times New Roman", Font.PLAIN, 16));
        labelRePassword.setText("Re-password:");

        fieldEmail.setEditable(false);
        fieldEmail.setBackground(new Color(255, 255, 51));

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(159, 159, 159)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(labelEmail)
                                        .addComponent(labelPassword)
                                        .addComponent(labelRePassword)
                                        .addComponent(labelNickname))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(fieldNickname, GroupLayout.PREFERRED_SIZE, 179, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(fieldEmail)
                                        .addComponent(fieldPassword)
                                        .addComponent(fieldRePassword)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(43, 43, 43)
                                                .addComponent(change)))
                                .addContainerGap(159, Short.MAX_VALUE))
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(labelTitle)
                                .addGap(190, 190, 190))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(74, 74, 74)
                                .addComponent(labelTitle)
                                .addGap(29, 29, 29)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(fieldNickname, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(labelNickname))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelEmail)
                                        .addComponent(fieldEmail, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelPassword)
                                        .addComponent(fieldPassword, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelRePassword)
                                        .addComponent(fieldRePassword, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(change, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(78, 78, 78))
        );
        labelNickname.getAccessibleContext().setAccessibleName("label_email");
        labelNickname.getAccessibleContext().setAccessibleDescription("");
        labelEmail.getAccessibleContext().setAccessibleName("label_password");
        fieldNickname.setText(jNickname);
        fieldEmail.setText(jEmail);
        pack();
    }

    static void goToChangePassword(String nickname) {
        CustomizeItemsJavaFX.start(ChangePassword.class);
        EventQueue.invokeLater(() -> new ChangePassword().setVisible(true));
        jNickname = nickname;
        jEmail = DatabaseConnection.gettingEmailByNickname(nickname);
    }

    class BgPanel3 extends JPanel {
        public void paintComponent(Graphics g) {
            Image im = null;
            try {
                im = ImageIO.read(new File("src/main/resources/img/background/orange.jpg"));
            } catch (IOException e) {
                Logger.getLogger("Pictures not found!");
            }
            g.drawImage(im, 0, 0, null);
        }
    }
}

