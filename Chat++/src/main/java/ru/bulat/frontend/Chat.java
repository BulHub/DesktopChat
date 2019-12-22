package ru.bulat.frontend;

import ru.bulat.ejection.Ban;
import ru.bulat.interfaces.StartInterface;
import ru.bulat.musicPlayer.SwingAudioPlayer;
import ru.bulat.network.TCPConnection;
import ru.bulat.network.TCPConnectionListener;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Chat extends JFrame implements StartInterface, ActionListener, TCPConnectionListener {
    private static final String IP_ADDRESS = "127.0.0.1";
    private static final int PORT = 8000;
    private static final String regexOfMats = "\\w{0,5}[хx]([хx\\s\\!@#\\$%\\^&*+-\\|\\/]{0,6})[уy]([уy\\s\\!@#\\$%\\^&*+-\\|\\/]{0,6})[ёiлeеюийя]\\w{0,7}|\\w{0,6}[пp]([пp\\s\\!@#\\$%\\^&*+-\\|\\/]{0,6})[iие]([iие\\s\\!@#\\$%\\^&*+-\\|\\/]{0,6})[3зс]([3зс\\s\\!@#\\$%\\^&*+-\\|\\/]{0,6})[дd]\\w{0,10}|[сcs][уy]([уy\\!@#\\$%\\^&*+-\\|\\/]{0,6})[4чkк]\\w{1,3}|\\w{0,4}[bб]([bб\\s\\!@#\\$%\\^&*+-\\|\\/]{0,6})[lл]([lл\\s\\!@#\\$%\\^&*+-\\|\\/]{0,6})[yя]\\w{0,10}|\\w{0,8}[её][bб][лске@eыиаa][наи@йвл]\\w{0,8}|\\w{0,4}[еe]([еe\\s\\!@#\\$%\\^&*+-\\|\\/]{0,6})[бb]([бb\\s\\!@#\\$%\\^&*+-\\|\\/]{0,6})[uу]([uу\\s\\!@#\\$%\\^&*+-\\|\\/]{0,6})[н4ч]\\w{0,4}|\\w{0,4}[еeё]([еeё\\s\\!@#\\$%\\^&*+-\\|\\/]{0,6})[бb]([бb\\s\\!@#\\$%\\^&*+-\\|\\/]{0,6})[нn]([нn\\s\\!@#\\$%\\^&*+-\\|\\/]{0,6})[уy]\\w{0,4}|\\w{0,4}[еe]([еe\\s\\!@#\\$%\\^&*+-\\|\\/]{0,6})[бb]([бb\\s\\!@#\\$%\\^&*+-\\|\\/]{0,6})[оoаa@]([оoаa@\\s\\!@#\\$%\\^&*+-\\|\\/]{0,6})[тnнt]\\w{0,4}|\\w{0,10}[ё]([ё\\!@#\\$%\\^&*+-\\|\\/]{0,6})[б]\\w{0,6}|\\w{0,4}[pп]([pп\\s\\!@#\\$%\\^&*+-\\|\\/]{0,6})[иeеi]([иeеi\\s\\!@#\\$%\\^&*+-\\|\\/]{0,6})[дd]([дd\\s\\!@#\\$%\\^&*+-\\|\\/]{0,6})[oоаa@еeиi]([oоаa@еeиi\\s\\!@#\\$%\\^&*+-\\|\\/]{0,6})[рr]\\w{0,12}|\\w{0,6}[cсs][уu][kк][aа]";
    private static final String censorship = "[вырезано цензурой]";
    private static final Pattern pattern = Pattern.compile(regexOfMats);
    private static JTextArea log;
    private static JTextField fieldInput;
    private TCPConnection connection;
    private int warning;
    private static String nickname;

    private Chat() {
        initComponents();
        try {
            connection = new TCPConnection(this, IP_ADDRESS, PORT);
        } catch (IOException ex) {
            printMessage("Database exception: " + ex);
        }
    }

    private void initComponents() {
        setTitle("Chat");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);
        addWindowListener(this);
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createMusicMenu());
        menuBar.add(createVideoMenu());
        setJMenuBar(menuBar);

        fieldInput = new JTextField();
        JLabel jLabel2 = new JLabel();
        JScrollPane jScrollPane = new JScrollPane();
        log = new JTextArea();

        setResizable(false);
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);
        log.setWrapStyleWord(true);
        log.setLineWrap(true);
        DefaultCaret caret = (DefaultCaret) log.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        log.setEditable(false);

        fieldInput.addActionListener(this::messageActionPerformed);

        fieldInput.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        fieldInput.addActionListener(this);

        jLabel2.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        jLabel2.setText("Your nickname: " + nickname);

        log.setColumns(20);
        log.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        log.setRows(5);
        jScrollPane.setViewportView(log);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jScrollPane, GroupLayout.PREFERRED_SIZE, 755, GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel2, GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                                                .addGap(249, 249, 249))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(fieldInput, GroupLayout.PREFERRED_SIZE, 755, GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane, GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(fieldInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(19, 19, 19))
        );
        pack();
    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        printMessage("Database ready ...");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {
        printMessage(value);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        printMessage("Database close!");
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception ex) {
        printMessage("Database exception: " + ex);
    }

    public static void main(String[] args) {
        CustomizeItemsJavaFX.start(Chat.class);
        EventQueue.invokeLater(() -> new Chat().setVisible(true));
    }

    @Override
    public void actionPerformed(ActionEvent evt) {

    }

    private JMenu createFileMenu() {
        JMenu file = new JMenu("File");
        JMenuItem open = new JMenuItem("About program",
                new ImageIcon("src\\main\\resources\\img\\menu\\open.png"));
        ExitAction exitAction = new ExitAction();
        JMenuItem exit = new JMenuItem(exitAction);
        JMenuItem settings = new JMenuItem("Settings");
        settings.addActionListener(e -> Settings.goToSettings());
        exit.setIcon(new ImageIcon("src\\main\\resources\\img\\menu\\exit.png"));
        file.add(open);
        file.add(settings);
        file.addSeparator();
        file.add(exit);
        open.addActionListener(arg0 -> JOptionPane.showMessageDialog(Chat.this,
                "(Something about my application)"));
        return file;
    }

    private JMenu createMusicMenu() {
        JMenu music = new JMenu("Music");
        JMenuItem on = new JMenuItem("Turn on the music");
        on.addActionListener(e -> SwingAudioPlayer.goToAudioPlayer());
        music.add(on);
        return music;
    }

    private JMenu createVideoMenu(){
        JMenu video = new JMenu("Video");
        JMenuItem on = new JMenuItem("Turn on the video");
        on.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        video.add(on);
        return video;
    }

    public static class ExitAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        ExitAction() {
            putValue(NAME, "Выход");
        }

        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }

    }

    static void goToChat(String name) {
        nickname = name;
        CustomizeItemsJavaFX.start(Chat.class);
        EventQueue.invokeLater(() -> new Chat().setVisible(true));
    }

    private void messageActionPerformed(ActionEvent e) {
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("hh:mm:ss");
        String message = fieldInput.getText();
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            message = censorship;
            warning++;
            message += "\nПользователю " + nickname + " выдано " + warning + "/3 предупреждение!";
            if (warning == 3) {
                setVisible(false);
                Ban.goToBan();
            }
        }
        if (message.trim().equals("")) return;
        fieldInput.setText(null);
        connection.sendString("(" + formatForDateNow.format(dateNow) + ")" + " " + nickname + ": " + message);
    }

    private synchronized void printMessage(final String message) {
        SwingUtilities.invokeLater(() -> {
            log.append(message + "\n");
            log.setCaretPosition(log.getDocument().getLength());
        });
    }

    static void createNewFont(String font, int style, int size, String color) {
        Font newFont = new Font(font, style, size);
        log.setFont(newFont);
        fieldInput.setFont(newFont);
        if (color.equals("Blue")) {
            fieldInput.setForeground(Color.BLUE);
            log.setForeground(Color.BLUE);
        }
        if (color.equals("Red")) {
            fieldInput.setForeground(Color.RED);
            log.setForeground(Color.RED);
        }
        if (color.equals("Black")) {
            fieldInput.setForeground(Color.BLACK);
            log.setForeground(Color.BLACK);
        }
        if (color.equals("Yellow")) {
            fieldInput.setForeground(Color.YELLOW);
            log.setForeground(Color.YELLOW);
        }
        if (color.equals("Green")) {
            fieldInput.setForeground(Color.GREEN);
            log.setForeground(Color.GREEN);
        }
    }
}
