package Controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import org.apache.commons.codec.digest.DigestUtils;


// 198.71.227.89:3306
// expenseTracker
// user: et
// pass: 3xPen$Etr@(er

public class DB {
    private final String url = "jdbc:mysql://198.71.227.89:3306/";
    private final String dbName = "expenseTracker";
    private final String driver = "com.mysql.jdbc.Driver";
    private final String userName = "et";
    private final String password = "3xPen$Etr@(er";
    private Connection conn = null;
    
    public DB() {
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url + dbName,userName,password);
        } catch (ClassNotFoundException|SQLException|InstantiationException|IllegalAccessException ex) {
            displayMessage(ex.getMessage(), "Error Message", 0);
        }
    }
    
    // GET STATEMENTS
    public ResultSet getExpenses(int userID) {
        String QUERY = "SELECT r.ID, r.Amount, r.Date, c.Name as Category, t.Name as Type, " +
                        "r.Notes, r.UserID, r.Recurring, f.Name as Frequency, r.EndDate FROM Register r " +
                        "INNER JOIN Categories c ON r.Category = c.ID " +
                        "LEFT OUTER JOIN Frequency f on r.Frequency = f.ID " +
                        "INNER JOIN Type t on r.Type = t.ID " +
                        "WHERE r.Type = 2 AND r.UserID = " + userID + " ORDER BY r.Date";
        return queryDB(QUERY);
    }
    
    public ResultSet getExpensesBySort(int userID, String orderByStatement) {
        String QUERY = "SELECT r.ID, r.Amount, r.Date, c.Name as Category, t.Name as Type, " +
                        "r.Notes, r.UserID, r.Recurring, f.Name as Frequency, r.EndDate FROM Register r " +
                        "INNER JOIN Categories c ON r.Category = c.ID " +
                        "LEFT OUTER JOIN Frequency f on r.Frequency = f.ID " +
                        "INNER JOIN Type t on r.Type = t.ID " +
                        "WHERE r.Type = 2 AND r.UserID = " + userID + " " +orderByStatement;
        return queryDB(QUERY);
    }
    
    public ResultSet getIncome(int userID) {
        String QUERY = "SELECT r.ID, r.Amount, r.Date, c.Name as Category, t.Name as Type, " +
                        "r.Notes, r.UserID, r.Recurring, f.Name as Frequency, r.EndDate " + 
                        "FROM Register r " +
                        "INNER JOIN Categories c ON r.Category = c.ID " +
                        "LEFT OUTER JOIN Frequency f on r.Frequency = f.ID " +
                        "INNER JOIN Type t on r.Type = t.ID " +
                        "WHERE r.Type = 1 AND r.UserID = " + userID + " ORDER BY r.Date";
        return queryDB(QUERY);
    }
    
    public ResultSet getIncomeBySort(int userID, String orderByStatement) {
        String QUERY = "SELECT r.ID, r.Amount, r.Date, c.Name as Category, t.Name as Type, " +
                        "r.Notes, r.UserID, r.Recurring, f.Name as Frequency, r.EndDate " + 
                        "FROM Register r " +
                        "INNER JOIN Categories c ON r.Category = c.ID " +
                        "LEFT OUTER JOIN Frequency f on r.Frequency = f.ID " +
                        "INNER JOIN Type t on r.Type = t.ID " +
                        "WHERE r.Type = 1 AND r.UserID = " + userID + " " +orderByStatement;
        return queryDB(QUERY);
    }    
    
    public ResultSet getGoals(int userID, int type) {
        String QUERY = "SELECT g.ID, g.UserID, g.Name, g.Amount, g.StartDate, " +
                        "g.EndDate, c.Name as Category, t.Name as Type " +
                        "FROM `Goals` g " +
                        "INNER JOIN Categories c ON g.Category = c.ID " +
                        "INNER JOIN Type t ON g.Type = t.ID " +
                        "WHERE g.Type = " + type + " AND g.UserID = " + userID + " ORDER BY g.EndDate";
        return queryDB(QUERY);
    }
    
    public ResultSet getGoalsBySort(int userID, int type, String orderByStatement) {
        String QUERY = "SELECT g.ID, g.UserID, g.Name, g.Amount, g.StartDate, " +
                        "g.EndDate, c.Name as Category, t.Name as Type " +
                        "FROM `Goals` g " +
                        "INNER JOIN Categories c ON g.Category = c.ID " +
                        "INNER JOIN Type t ON g.Type = t.ID " +
                        "WHERE g.Type = " + type + " AND g.UserID = " + userID + " " +orderByStatement;
        return queryDB(QUERY);
    }    
    
    public ResultSet getLimits(int userID, int type) {
        String QUERY = "SELECT l.ID, l.UserID, l.Name, l.Amount, l.StartDate, " +
                        "l.EndDate, c.Name as Category, t.Name as Type " +
                        "FROM `Limits` l " +
                        "INNER JOIN Categories c ON l.Category = c.ID " +
                        "INNER JOIN Type t ON l.Type = t.ID " +
                        "WHERE l.Type = " + type + " AND l.UserID = " + userID + " ORDER BY l.EndDate";
        return queryDB(QUERY);
    }
    
    public ResultSet getLimitsBySort(int userID, int type, String orderByStatement) {
        String QUERY = "SELECT l.ID, l.UserID, l.Name, l.Amount, l.StartDate, " +
                        "l.EndDate, c.Name as Category, t.Name as Type " +
                        "FROM `Limits` l " +
                        "INNER JOIN Categories c ON l.Category = c.ID " +
                        "INNER JOIN Type t ON l.Type = t.ID " +
                        "WHERE l.Type = " + type + " AND l.UserID = " + userID + " " +orderByStatement;
        return queryDB(QUERY);
    }
    
    public ResultSet getUser(String userName, String password) {
        // Used with UserController to validate/login user
        String encryptedPassword = DigestUtils.sha256Hex(password);
        String QUERY = "SELECT * FROM Users WHERE LCASE(Username) = '" + userName.toLowerCase() + "' AND Password = '" + encryptedPassword + "' LIMIT 1";
        return queryDB(QUERY);
    }
    
    public ResultSet getCategories(int type) {
        String QUERY = "SELECT * FROM Categories WHERE Type = " + type;
        return queryDB(QUERY);
    }
    
    public ResultSet getTypes() {
        String QUERY = "SELECT * FROM Type";
        return queryDB(QUERY);
    }
    
    public ResultSet getFrequencies() {
        String QUERY = "SELECT * FROM Frequency";
        return queryDB(QUERY);
    }
    
    // ADD STATEMENTS
    public void addExpense(int userID, double amount, LocalDate date, int category, String notes, int recurring, int frequency, LocalDate endDate) {
        // Frequency: use 0 for NONE
        // EndDate: use expense date for NONE
        String sDate = reFormatDate(date);
        String eDate = reFormatDate(endDate);
        String QUERY = "INSERT INTO Register (Amount,Date,Frequency,Recurring,Category,Type,Notes,EndDate,UserID)"
                + " VALUES (" + amount + ",'" + sDate + "'," + frequency + "," + recurring + "," + category + ",2,'" + notes + "','" + eDate + "'," + userID + ")";
        updateDB(QUERY);
    }
    
    public void addIncome(int userID, double amount, LocalDate date, int category, String notes, int recurring, int frequency, LocalDate endDate) {
        // Frequency: use 0 for NONE
        // EndDate: use expense date for NONE
        String sDate = reFormatDate(date);
        String eDate = reFormatDate(endDate);
        String QUERY = "INSERT INTO Register (Amount,Date,Frequency,Recurring,Category,Type,Notes,EndDate,UserID)"
                + " VALUES (" + amount + ",'" + sDate + "'," + frequency + "," + recurring + "," + category + ",1,'" + notes + "','" + eDate + "'," + userID + ")";
        updateDB(QUERY);
    }
    
    public void addGoal(int userID, String name, double amount, LocalDate startDate, LocalDate endDate, int category, int type) {
        String sDate = reFormatDate(startDate);
        String eDate = reFormatDate(endDate);
        String QUERY = "INSERT INTO Goals (UserID,Name,Amount,StartDate,EndDate,Category,Type)"
                + " VALUES (" + userID + ",'" + name + "'," + amount + ",'" + sDate + "','" + eDate + "'," + category + "," + type + ")";
        updateDB(QUERY);
    }
    
    public void addLimit(int userID, String name, double amount, LocalDate startDate, LocalDate endDate, int category, int type) {
        String sDate = reFormatDate(startDate);
        String eDate = reFormatDate(endDate);
        String QUERY = "INSERT INTO Limits (UserID,Name,Amount,StartDate,EndDate,Category,Type)"
                + " VALUES (" + userID + ",'" + name + "'," + amount + ",'" + sDate + "','" + eDate + "'," + category + "," + type + ")";
        updateDB(QUERY);
    }
    
    public void addUser(String userName, String password, String name) {
        String QUERY = "SELECT ID FROM Users WHERE LCASE(Username) = '" + userName.toLowerCase() + "'";
        ResultSet result = queryDB(QUERY);
        try {
            if (result.next()) {
                displayMessage("UserName Not Available\nPlease try a different UserName", "Error Message", 0);
            } else {
                String encryptedPassword = DigestUtils.sha256Hex(password);
                QUERY = "INSERT INTO Users (Name, Username, Password)"
                        + " VALUES ('" + name + "','" + userName + "','" + encryptedPassword + "')";
                updateDB(QUERY);
            }
        } catch (SQLException ex) {
            displayMessage(ex.getMessage(), "Error Message", 0);
        }
    }
    
        // EDIT STATEMENTS
    public void editExpense(int expenseID, double amount, LocalDate date, int category, String notes, int recurring, int frequency, LocalDate endDate) {
        // Frequency: use 0 for NONE
        // EndDate: use expense date for NONE
        String sDate = reFormatDate(date);
        String eDate = reFormatDate(endDate);
        String QUERY = "UPDATE Register SET Amount = " + amount + 
                ",Date = '" + sDate + "',Frequency = " + frequency + 
                ",Recurring = " + recurring + ",Category = " + category + 
                ",Notes = '" + notes + "',EndDate = '" + eDate + "' WHERE ID = " + expenseID;
        updateDB(QUERY);
    }
    
    public void editIncome(int incomeID, double amount, LocalDate date, int category, String notes, int recurring, int frequency, LocalDate endDate) {
        // Frequency: use 0 for NONE
        // EndDate: use expense date for NONE
        String sDate = reFormatDate(date);
        String eDate = reFormatDate(endDate);
        String QUERY = "UPDATE Register SET Amount = " + amount + 
                ",Date = '" + sDate + "',Frequency = " + frequency + 
                ",Recurring = " + recurring + ",Category = " + category + 
                ",Notes = '" + notes + "',EndDate = '" + eDate + "' WHERE ID = " + incomeID;
        updateDB(QUERY);
    }
    
    public void editGoal(int goalID, String name, double amount, LocalDate startDate, LocalDate endDate, int category, int type) {
        String sDate = reFormatDate(startDate);
        String eDate = reFormatDate(endDate);
        String QUERY = "UPDATE Goals SET Name = '" + name + 
                "',Amount = " + amount + ",StartDate = '" + sDate + 
                "',EndDate = '" + eDate + "',Category = " + category + 
                ",Type = " + type + " WHERE ID = " + goalID;
        updateDB(QUERY);
    }
    
    public void editLimit(int limitID, String name, double amount, LocalDate startDate, LocalDate endDate, int category, int type) {
        String sDate = reFormatDate(startDate);
        String eDate = reFormatDate(endDate);
//        String QUERY = "UPDATE Goals SET Name = '" + name + 
        String QUERY = "UPDATE Limits SET Name = '" + name + 
                "',Amount = " + amount + ",StartDate = '" + sDate + 
                "',EndDate = '" + eDate + "',Category = " + category + 
                ",Type = " + type + " WHERE ID = " + limitID;
        updateDB(QUERY);
    }
    
    // REMOVE STATEMENTS
    public void removeExpense(int expenseID) {
        String QUERY = "DELETE FROM Register WHERE ID = " + expenseID;
        updateDB(QUERY);
    }
    
    public void removeIncome(int incomeID) {
        String QUERY = "DELETE FROM Register WHERE ID = " + incomeID;
        updateDB(QUERY);
    }
    
    public void removeGoal(int goalID) {
        String QUERY = "DELETE FROM Goals WHERE ID = " + goalID;
        updateDB(QUERY);
    }
    
    public void removeLimit(int limitID) {
        String QUERY = "DELETE FROM Limits WHERE ID = " + limitID;
        updateDB(QUERY);
    }
    
    public void removeUser(int userID) {
        String QUERY = "DELETE FROM Users WHERE ID = " + userID;
        updateDB(QUERY);
        QUERY = "DELETE FROM Register WHERE UserID = " + userID;
        updateDB(QUERY);
        QUERY = "DELETE FROM Goals WHERE UserID = " + userID;
        updateDB(QUERY);
        QUERY = "DELETE FROM Limits WHERE UserID = " + userID;
        updateDB(QUERY);
    }
    
    private ResultSet queryDB(String QUERY) {
        ResultSet result = null;
        try {
            Statement statement = conn.createStatement();
            result = statement.executeQuery(QUERY);
        } catch (SQLException ex) {
            displayMessage(ex.getMessage(), "Error Message", 0);
        }
        return result;
    }
    
    private void updateDB(String QUERY) {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(QUERY);
        } catch (SQLException ex) {
            displayMessage(ex.getMessage(), "Error Message", 0);
        }
    }
    
    private void displayMessage(String message, String title, int prompt) {
        JOptionPane pane = new JOptionPane(message, prompt);  
        JDialog dialog = pane.createDialog(title);  
        dialog.setAlwaysOnTop(true);  
        dialog.setVisible(true); 
    }
    
    private String reFormatDate(LocalDate date) {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return date.toString();
    }
    
    public void main() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        Class.forName(driver).newInstance();
        Connection conn = DriverManager.getConnection(url + dbName,userName,password);
        
        String QUERY = "SELECT * FROM Register";
        Statement statement = conn.createStatement();
        ResultSet result = statement.executeQuery(QUERY);
        //for (String per: this.persons) {
        while (result.next()) {
            System.out.println("column 2: " + result.getString(2));
        }
        //if (statement != null) statement.close();
        if (conn != null) conn.close();
    }
}
