package ru.bulat.frontend;

import ru.bulat.interfaces.StartInterface;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

public class Settings extends JFrame implements StartInterface {
    private static JComboBox<String> jFont;
    private static JComboBox<String> jStyle;
    private static JComboBox<String> jColor;
    private static JTextField jSize;


    private Settings() {
        initComponents();
    }

    private void initComponents() {
        addWindowListener(this);

        setTitle("Settings");
        setResizable(false);
        setContentPane(new Settings.BgPanel2());


        jFont = new JComboBox<>();
        jStyle = new JComboBox<>();
        JLabel labelFont = new JLabel();
        JLabel labelStyle = new JLabel();
        JButton enter = new JButton();
        jSize = new JTextField();
        JLabel labelSize = new JLabel();
        jColor = new JComboBox<>();
        JLabel labelColor = new JLabel();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        jFont.setModel(new DefaultComboBoxModel<>(new String[] { "Times New Roman", "Georgia", "Arial", "Courier" }));

        jStyle.setModel(new DefaultComboBoxModel<>(new String[] { "Font.PLAIN", "Font.BOLD", "Font.ITALIC" }));

        labelFont.setText("Font:");

        labelStyle.setText("Style:");

        enter.setText("Enter");
        enter.addActionListener(e -> {
            try {
                String font = (String) jFont.getSelectedItem();
                int style = jStyle.getSelectedIndex();
                int size = Integer.parseInt(jSize.getText().trim());
                if (size < 1 || size > 30){
                    JOptionPane.showMessageDialog(Settings.this, "The font size can be from 1 to 30.", "Error", JOptionPane.ERROR_MESSAGE);
                }else{
                    String color = (String) jColor.getSelectedItem();
                    JOptionPane.showMessageDialog(Settings.this,
                            "You have changed the settings!");
                    assert color != null;
                    Chat.createNewFont(font, style, size, color);
                }
            }catch (NullPointerException | NumberFormatException ignored){

            }
        });

        jSize.setFont(new Font("Times New Roman", Font.PLAIN, 14));

        labelSize.setText("Font size:");

        jColor.setModel(new DefaultComboBoxModel<>(new String[] { "Black", "Blue", "Red", "Green", "Yellow" }));

        labelColor.setText("Color:");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(188, 188, 188)
                                .addComponent(enter)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(125, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(labelSize)
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                .addComponent(labelFont)
                                                .addComponent(labelStyle))
                                        .addComponent(labelColor))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(7, 7, 7)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(jStyle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jFont, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jSize)
                                                        .addComponent(jColor, 0, 67, Short.MAX_VALUE))))
                                .addGap(123, 123, 123))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap(60, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelFont)
                                        .addComponent(jFont, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelStyle)
                                        .addComponent(jStyle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelSize)
                                        .addComponent(jSize, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jColor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(labelColor))
                                .addGap(33, 33, 33)
                                .addComponent(enter)
                                .addGap(42, 42, 42))
        );

        pack();
    }

    static void goToSettings(){
        CustomizeItemsJavaFX.start(Settings.class);
        EventQueue.invokeLater(() -> new Settings().setVisible(true));
    }

    @Override
    public void windowClosing(WindowEvent e) {
        Object[] options = { "Yes", "No!" };
        int n = JOptionPane
                .showOptionDialog(e.getWindow(), "Close a window?",
                        "Confirmation", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, options,
                        options[0]);
        if (n == 0) {
            e.getWindow().setVisible(false);
        }else{
            e.getWindow().setVisible(true);
        }
    }

    class BgPanel2 extends JPanel {
        public void paintComponent(Graphics g) {
            Image im = null;
            try {
                im = ImageIO.read(new File("src/main/resources/img/background/" +
                        "The-best-top-desktop-yellow-wallpapers-yellow-wallpaper-yellow-background-hd-8.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            g.drawImage(im, 0, 0, null);
        }
    }
}
