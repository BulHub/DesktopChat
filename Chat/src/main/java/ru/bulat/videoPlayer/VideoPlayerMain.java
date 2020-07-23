package ru.bulat.videoPlayer;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import ru.bulat.frontend.CustomizeItemsJavaFX;
import ru.bulat.interfaces.WindowListenerNotExit;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.windows.Win32FullScreenStrategy;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

public class VideoPlayerMain extends JFrame implements WindowListenerNotExit {
    private EmbeddedMediaPlayer emp;

    private VideoPlayerMain() {
        JFrame frame = new JFrame();
        frame.addWindowListener(this);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        JButton button = new JButton("Select File");
        ActionListener listener = event -> {
            JFileChooser chooser = new JFileChooser(".");
            int status = chooser.showOpenDialog(VideoPlayerMain.this);
            if (status == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                try {
                    load(file.getPath());
                } catch (Exception e) {
                    System.err.println("Try again: " + e);
                }
            }
        };
        button.addActionListener(listener);
        frame.getContentPane().add(button, BorderLayout.NORTH);
        frame.pack();
        frame.show();
    }

    private void load(String path) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        f.setVisible(true);
        f.setBounds(100, 100, 1000, 600);
        Canvas c = new Canvas();
        c.setBackground(Color.black);

        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        p.add(c);
        f.add(p);

        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "C:\\Program Files/VideoLAN/VLC");
        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
        MediaPlayerFactory mpf = new MediaPlayerFactory();
        emp = mpf.newEmbeddedMediaPlayer(new Win32FullScreenStrategy(f));
        emp.setVideoSurface(mpf.newVideoSurface(c));
        emp.setEnableMouseInputHandling(false);
        emp.setEnableKeyInputHandling(false);
        emp.prepareMedia(path);
        f.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                emp.pause();
                String[] options = { "Yes", "No!" };
                int n = JOptionPane
                        .showOptionDialog(e.getWindow(), "Close a window?",
                                "Confirmation", JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE, null, options,
                                options[0]);
                if (n == 0) {
                    e.getWindow().setVisible(false);
                }else{
                    e.getWindow().setVisible(true);
                    emp.play();
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createControllerMenu());
        f.setJMenuBar(menuBar);
        emp.play();
    }

    private JMenu createControllerMenu() {
        JMenu controller = new JMenu("Controller");
        JMenuItem play = new JMenuItem("Play");
        JMenuItem pause = new JMenuItem("Pause");
        JMenuItem stop = new JMenuItem("Stop");
        play.addActionListener(e -> emp.play());
        pause.addActionListener(e -> emp.pause());
        stop.addActionListener(e -> emp.stop());
        controller.add(play);
        controller.add(stop);
        controller.add(pause);
        return controller;
    }

    public static void goToVideoPlayer(){
        CustomizeItemsJavaFX.start(VideoPlayerMain.class);
        EventQueue.invokeLater(() -> new VideoPlayerMain().setVisible(true));
    }
}