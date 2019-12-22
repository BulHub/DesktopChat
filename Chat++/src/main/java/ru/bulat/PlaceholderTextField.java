package ru.bulat;

import javax.swing.*;
import java.awt.event.FocusEvent;

public class PlaceholderTextField{
    private static final String placeholderForPassword = "Password here!";

    public static void textFieldFocusGained(FocusEvent evt, JTextField textField, String placeholder) {
        if(textField.getText().trim().toLowerCase().equals(placeholder.toLowerCase())){
            textField.setText("");
        }
    }

    public static void textFieldFocusLost(FocusEvent evt, JTextField textField, String placeholder){
        if(textField.getText().trim().equals("") || textField.getText().trim().toLowerCase().equals(placeholder.toLowerCase())){
            textField.setText(placeholder);
        }
    }

    public static void textPasswordFocusGained(FocusEvent evt, JPasswordField passwordField) {
        if(String.valueOf(passwordField.getPassword()).trim().equals(placeholderForPassword)) {
            passwordField.setText("");
        }
    }

    public static void textPasswordFocusLost(FocusEvent evt, JPasswordField passwordField){
        if(String.valueOf(passwordField.getPassword()).trim().equals("")) {
            passwordField.setText(placeholderForPassword);
        }
    }
}
