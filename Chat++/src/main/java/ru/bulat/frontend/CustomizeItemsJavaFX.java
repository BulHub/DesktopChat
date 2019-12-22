package ru.bulat.frontend;

import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomizeItemsJavaFX {

    public static void start(Class nameClass){
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(nameClass.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
