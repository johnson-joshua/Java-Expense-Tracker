/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author johnsonjm
 */
public class UserController {
    public String userName = "NOT SET";
    public String name = "NOT SET";
    public int userID = 0;
    public boolean validUser = false;


    public UserController(ResultSet rs) throws SQLException {
	if (rs.next()) {
	    this.userID = rs.getInt("ID");
	    this.name = rs.getString("Name");
	    this.userName = rs.getString("UserName");
	    this.validUser = true;
	}
    }
}
