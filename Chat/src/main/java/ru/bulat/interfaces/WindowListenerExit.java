package ru.bulat.interfaces;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public interface WindowListenerExit extends WindowListener {

    @Override
    default void windowOpened(WindowEvent e) {

    }

    @Override
    default void windowClosing(WindowEvent e) {
        String[] options = { "Yes", "No!" };
        int n = JOptionPane
                .showOptionDialog(e.getWindow(), "Close a window?",
                        "Confirmation", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, options,
                        options[0]);
        if (n == 0) {
            e.getWindow().setVisible(false);
            System.exit(0);
        }else{
            e.getWindow().setVisible(true);
        }
    }

    @Override
    default void windowClosed(WindowEvent e) {

    }

    @Override
    default void windowIconified(WindowEvent e) {

    }

    @Override
    default void windowDeiconified(WindowEvent e) {

    }

    @Override
    default void windowActivated(WindowEvent e) {

    }

    @Override
    default void windowDeactivated(WindowEvent e) {

    }

}
