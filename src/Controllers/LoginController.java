/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Views.LoginView;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author johnsonjm
 */
public class LoginController {
    public static UserController user;
    private ExpenseController expenseController; 
    public static LoginView loginPage;

    //Called when user tries to register or login from the LoginView
    public void loginUser(String userName, String password, LoginView loginView){
	this.loginPage = loginView;
	try {
	    DB db = new DB();
            //Check if User is a valid user in the database
	    user = new UserController(db.getUser(userName, password));
	    if (!user.validUser) {//Invalid user, show error message
		JOptionPane.showMessageDialog(null, "Username and/or password is incorrect", "Error Message", 0);
	    } else {//Valid User, hide login page and load the Main Page
		if(loginPage.isVisible()){
		    loginPage.setVisible(false);
		}
		expenseController = new ExpenseController();
		expenseController.showMainPage();
	    }
	} catch (SQLException ex) {
	    Logger.getLogger(ExpenseController.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    //Used by the login view to validate username, password, and full name for new user
    public boolean validateFields(String userName, String password, String name){
	//validate register fields
	boolean validString = Helper.validateStringNoSpaces(userName);
	boolean validFullName = Helper.validateStringWithSpaces(name);
	boolean validPass = Helper.validatePass(password);
	if(validString && validFullName && validPass){
	    return true;
	}
	else{
	    return false;
	}
    }
    //Used by the login controller to validate username and password for existing user
    public boolean validateFields(String userName, String password){
	boolean validString = Helper.validateStringNoSpaces(userName);
	boolean validPass = Helper.validatePass(password);
	if(validString && validPass){
	    return true;
	}
	else{
	    return false;
	}
    }
}

