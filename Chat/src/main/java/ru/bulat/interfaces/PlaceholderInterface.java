package ru.bulat.interfaces;

import javax.swing.*;
import java.awt.event.FocusEvent;

public interface PlaceholderInterface{
    String placeholderForPassword = "Password here!";
    String placeholderForEmail = "(Enter your email)";
    String placeholderForNickname = "(Enter a nickname)";

    default void textEmailFieldFocusGained(FocusEvent evt, JTextField textField) {
        if(textField.getText().trim().toLowerCase().equals(placeholderForEmail.toLowerCase()))textField.setText("");
    }

    default void textEmailFieldFocusLost(FocusEvent evt, JTextField textField){
        if(textField.getText().trim().equals("") || textField.getText().trim().toLowerCase().equals(placeholderForEmail.toLowerCase()))textField.setText(placeholderForEmail);
    }

    default void textNicknameFieldFocusGained(FocusEvent evt, JTextField textField) {
        if(textField.getText().trim().toLowerCase().equals(placeholderForNickname.toLowerCase()))textField.setText("");
    }

    default void textNicknameFieldFocusLost(FocusEvent evt, JTextField textField){
        if(textField.getText().trim().equals("") || textField.getText().trim().toLowerCase().equals(placeholderForNickname.toLowerCase()))textField.setText(placeholderForNickname);
    }

    default void textPasswordFocusGained(FocusEvent evt, JPasswordField passwordField) {
        if(String.valueOf(passwordField.getPassword()).trim().equals(placeholderForPassword)) passwordField.setText("");
    }

    default void textPasswordFocusLost(FocusEvent evt, JPasswordField passwordField){
        if(String.valueOf(passwordField.getPassword()).trim().equals("")) passwordField.setText(placeholderForPassword);
    }
}
