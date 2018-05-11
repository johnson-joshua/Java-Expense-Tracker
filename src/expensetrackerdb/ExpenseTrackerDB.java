/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expensetrackerdb;

import Views.LoginView;
import javax.swing.JFrame;

/**
 *
 * @author johnsonjm
 */
public class ExpenseTrackerDB {
//Entry point for the program. Creates and shows a Login Page
    public static void main(String[] args) {
        LoginView loginPage = new LoginView();
        loginPage.setResizable(false);
        loginPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setupRegisterItems(loginPage);
        loginPage.setVisible(true);
    }
    
    public static void setupRegisterItems(LoginView loginPage){
        loginPage.getLblFullName().setVisible(false);
        loginPage.getLblNewUsername().setVisible(false);
        loginPage.getLblNewPassword().setVisible(false);
        loginPage.getTxtFullName().setVisible(false);
        loginPage.getTxtNewUsername().setVisible(false);
        loginPage.getPwdNewPassword().setVisible(false);
    }
}
