package ru.bulat.frontend;

import ru.bulat.ejection.PlaceholderTextField;
import ru.bulat.interfaces.PlaceholderInterface;
import ru.bulat.interfaces.WindowListenerExit;
import ru.bulat.data.DatabaseConnection;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration extends JFrame implements ActionListener, WindowListenerExit, PlaceholderInterface {
    private static final String placeholderForEmail = "(Enter your email)";
    private static final String placeholderForNickname = "(Enter a nickname)";
    private static final String placeholderForPassword = "Password here!";
    private static HashMap<String, String> descriptionOfAllErrors = new HashMap<>();
    private static StringBuilder error = new StringBuilder();
    private static boolean check;
    private JPasswordField fieldRePassword;
    private JPasswordField fieldPassword;
    private JTextField fieldEmail;
    private JTextField fieldNickname;


    private Registration() {
        initComponents();
    }


    private void initComponents() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        fillingInAllErrors();

        addWindowListener(this);
        setResizable(false);
        setTitle("Registration");

        setContentPane(new BgPanel1());

        JLabel jLabel1 = new JLabel();
        fieldNickname = new JTextField();
        JButton jButton3 = new JButton();
        JButton jButton2 = new JButton();
        fieldRePassword = new JPasswordField();
        fieldPassword = new JPasswordField();
        fieldEmail = new JTextField();

        fieldEmail.setText(placeholderForEmail);
        fieldNickname.setText("(Enter a nickname)");
        fieldRePassword.setText(placeholderForPassword);
        fieldPassword.setText(placeholderForPassword);
        fieldEmail.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                textEmailFieldFocusGained(e, fieldEmail);
            }

            @Override
            public void focusLost(FocusEvent e) {
                textEmailFieldFocusLost(e, fieldEmail);
            }
        });
        fieldNickname.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                textNicknameFieldFocusGained(e, fieldNickname);
            }

            @Override
            public void focusLost(FocusEvent e) {
                textNicknameFieldFocusLost(e, fieldNickname);
            }
        });

        fieldPassword.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                textPasswordFocusGained(e, fieldPassword);
            }

            @Override
            public void focusLost(FocusEvent e) {
                textPasswordFocusLost(e, fieldPassword);
            }
        });

        fieldRePassword.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                PlaceholderTextField.textPasswordFocusGained(e, fieldRePassword);
            }

            @Override
            public void focusLost(FocusEvent e) {
                PlaceholderTextField.textPasswordFocusLost(e, fieldRePassword);
            }
        });


        jLabel1.setFont(new Font("Times New Roman", Font.PLAIN, 16)); // NOI18N

        fieldNickname.setToolTipText("");

        jButton3.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 16)); // NOI18N
        jButton3.setText("Enter");
        jButton3.addActionListener(this);

        jButton2.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 16)); // NOI18N
        jButton2.setText("Entrance");
        jButton2.addActionListener(this);


        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(206, 206, 206)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(fieldNickname, GroupLayout.PREFERRED_SIZE, 179, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel1))
                                        .addComponent(jButton3)
                                        .addComponent(fieldPassword, GroupLayout.PREFERRED_SIZE, 179, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(fieldEmail, GroupLayout.PREFERRED_SIZE, 179, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                .addComponent(jButton2)
                                                .addComponent(fieldRePassword, GroupLayout.PREFERRED_SIZE, 179, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(209, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(88, 88, 88)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(fieldNickname, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel1))
                                .addGap(18, 18, 18)
                                .addComponent(fieldEmail, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(fieldPassword, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(fieldRePassword, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton2))
                                .addGap(88, 88, 88))
        );

        jLabel1.getAccessibleContext().setAccessibleName("label_email");
        jLabel1.getAccessibleContext().setAccessibleDescription("");

        pack();
    }

    private static void nicknameChecking(String nickname) {
        if (nickname.length() < 4 || nickname.length() > 20 || nickname.equals(placeholderForNickname)) {
            error.append(descriptionOfAllErrors.get("Invalid nickname"));
            check = false;
        }
        int x = DatabaseConnection.nickIsMatchCheck(nickname);
        if (x != -1){
            error.append(descriptionOfAllErrors.get("Matching nicknames"));
            check = false;
        }
    }

    private static void emailChecking(String email) {
        Pattern patternEmail = Pattern.compile("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@" +
                "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcherEmail = patternEmail.matcher(email);
        if (!matcherEmail.matches()) {
            error.append(descriptionOfAllErrors.get("Incorrectly email"));
            check = false;
        }
        int id = DatabaseConnection.emailVerification(email);
        if (id != -1){
            error.append(descriptionOfAllErrors.get("Invalid email"));
            check = false;
        }
    }

    private static void passwordChecking(String password, String rePassword) {
        if (!password.equals(rePassword)) {
            error.append(descriptionOfAllErrors.get("Mismatched passwords"));
            check = false;
        }
        if (!(password.length() > 6 && password.length() < 20) || password.equals(placeholderForPassword)) {
            error.append(descriptionOfAllErrors.get("Invalid password"));
            check = false;
        }
    }

    private static void fillingInAllErrors() {
        descriptionOfAllErrors.put("Registered email", "This email is already registered \n");
        descriptionOfAllErrors.put("Incorrectly email", "Email is entered incorrectly \n");
        descriptionOfAllErrors.put("Mismatched passwords", "Passwords do not match \n");
        descriptionOfAllErrors.put("Invalid password", "Password must be between 6 and 20 characters \n");
        descriptionOfAllErrors.put("Invalid nickname", "Nickname must be between 4 and 20 characters \n");
        descriptionOfAllErrors.put("Matching nicknames", "This nick is already busy \n");
        descriptionOfAllErrors.put("Invalid email", "This email is already busy \n");
    }

    static void goToRegistration() {
        CustomizeItemsJavaFX.start(Registration.class);
        EventQueue.invokeLater(() -> new Registration().setVisible(true));
    }


    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getActionCommand().equals("Enter")) {
            error.setLength(0);
            check = true;
            nicknameChecking(fieldNickname.getText().trim());
            emailChecking(fieldEmail.getText().trim());
            passwordChecking(fieldPassword.getText(), fieldRePassword.getText());
            if (check) {
                int id = DatabaseConnection.writeNewNickname(fieldNickname.getText().trim());
                DatabaseConnection.writeToDatabaseNewUser(fieldEmail.getText().trim(), fieldPassword.getText(), fieldNickname.getText().trim(), id);
                JOptionPane.showMessageDialog(Registration.this, "Congratulations you are registered in the chat!");
                setVisible(false);
                Chat.goToChat(fieldNickname.getText().trim());
            } else {
                JOptionPane.showMessageDialog(Registration.this, error, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (evt.getActionCommand().equals("Entrance")) {
            setVisible(false);
            Entrance.main(new String[0]);
        }
    }


    class BgPanel1 extends JPanel {
        public void paintComponent(Graphics g) {
            Image im = null;
            try {
                im = ImageIO.read(new File("src/main/resources/img/background/26002834_Rick_And_Morty_Cast_b.jpg"));
            } catch (IOException ex) {
                Logger.getLogger(Registration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            g.drawImage(im, 0, 0, null);
        }
    }
}
