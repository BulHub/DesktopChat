package ru.bulat.ejection;

import ru.bulat.data.DatabaseConnection;
import ru.bulat.interfaces.WindowListenerExit;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Ban extends JFrame implements WindowListenerExit {
    private Image image;
    private static final Map<Integer, String> imgBans = new HashMap<>() {{
        put(0, "src/main/resources/img/bans/1.jpg");
        put(1, "src/main/resources/img/bans/2.jpg");
        put(2, "src/main/resources/img/bans/3.jpeg");
        put(3, "src/main/resources/img/bans/4.png");
        put(4, "src/main/resources/img/bans/5.jpg");
        put(5, "src/main/resources/img/bans/6.jpeg");
        put(6, "src/main/resources/img/bans/7.jpeg");
    }};

    private Ban() {
        setTitle("Ban");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        try {
            Random rand = new Random();
            int random = rand.nextInt(7);
            BufferedImage img = ImageIO.read(new File(imgBans.get(random)));
            image = img;
            setSize(img.getWidth(), img.getHeight());
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(Ban.this,
                "You are banned!",
                "Ban",
                JOptionPane.ERROR_MESSAGE);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
//                try {
//                    String shutdownCmd = "shutdown -r";
//                    Runtime.getRuntime().exec(shutdownCmd);
//                } catch (IOException e) {
//                    Logger.getLogger("User reboot failed!");
//                }
                dispose();
                System.exit(0);
            }
        });
    }

    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, null);
    }


    public static void goToBan(String nickname) {
        DatabaseConnection.deletingUser(nickname);
        new Ban();
    }
}