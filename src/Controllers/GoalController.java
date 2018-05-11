/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.GoalModel;
import Models.MyDefaultTableModel;
import Views.AddGoalView;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 *
 * @author johnsonjm
 */
public class GoalController {

    public static AddGoalView addGoalPage = new AddGoalView();
   // private static DB db = new DB();
    private static ArrayList<GoalModel> goalsArray;
    private static HashMap<String, Double> hmGoalsByCategory = new HashMap<String, Double>();
    private static double totalGoals;
    private static boolean editing = false;

    /**
     * SECTION : Navigation within the program
     */
    public void closeAddGoalView() {
	if(addGoalPage.isVisible()){
	    clearFields();
	    addGoalPage.setVisible(false);
	}
    }

    /**
     * SECTION : Add data to the program / load from database
     */

    public void addExpenseGoal() {
	addGoalPage.getLblIncomeOrExpense().setText("Expense Goal");
	addGoalPage.getCbxCategory().removeAllItems();
	//get categories based on income limit/goal or expense limit/goal
        DB db = new DB();
        ResultSet rs = db.getCategories(2); //2 for expense categories
	try {
	    while (rs.next()) {
		addGoalPage.getCbxCategory().addItem(new ComboItemController(rs.getInt("ID"), rs.getString("Name")).toString());
	    }   } catch (SQLException ex) {
		Logger.getLogger(AddGoalView.class.getName()).log(Level.SEVERE, null, ex);
	    }
	loadGoalsFromDB(2);
	loadTable();
	addGoalPage.setVisible(true);
    }

    public void addIncomeGoal() {
	addGoalPage.getLblIncomeOrExpense().setText("Income Goal");
	addGoalPage.getCbxCategory().removeAllItems();
	//get categories based on income limit/goal or expense limit/goal
	DB db = new DB();
	ResultSet rs = db.getCategories(1); //1 for income categories
	try {
	    while (rs.next()) {
		addGoalPage.getCbxCategory().addItem(new ComboItemController(rs.getInt("ID"), rs.getString("Name")).toString());
	    }   } catch (SQLException ex) {
		Logger.getLogger(AddGoalView.class.getName()).log(Level.SEVERE, null, ex);
	    }
	loadGoalsFromDB(1); //pass 1 in to represent income goal
	loadTable();
	addGoalPage.setVisible(true);
    }

    public static void loadGoalsFromDB(int type) { //type specifies income or expense goals
	totalGoals = 0;
	goalsArray = new ArrayList<GoalModel>();
	hmGoalsByCategory.clear();
	try{  
            DB db = new DB();
	    ResultSet rs = db.getGoals(LoginController.user.userID, type);
	    boolean nextRecordExists = rs.next();
	    while (nextRecordExists) {
		totalGoals += rs.getDouble("Amount");
		Double previousAmount = hmGoalsByCategory.get(rs.getString("Category")); //gets existing amount for category if it is in hashmap
		if(previousAmount != null){
		    hmGoalsByCategory.put(rs.getString("Category"), Double.parseDouble(rs.getString("Amount")) + previousAmount);
		}
		else{
		    previousAmount = 0.0;
		    hmGoalsByCategory.put(rs.getString("Category"), Double.parseDouble(rs.getString("Amount")) + previousAmount);
		}
		GoalModel goal = new GoalModel();
		goal.setAmount(rs.getDouble("Amount"));
		goal.setCategory(rs.getString("Category"));
		goal.setStartDate(LocalDate.parse(rs.getString("StartDate")));
		goal.setName(rs.getString("Name"));
		goal.setGoalId(rs.getInt("ID"));
		goal.setEndDate(Helper.dateToLocalDate(rs.getDate("EndDate")));
		goal.setGoalType(Helper.convertTypeToInt(rs.getString("Type")));
		goalsArray.add(goal);
		nextRecordExists = rs.next();
	    }
	}
	catch(Exception e){
	    System.out.println("Error occurred");
	}
    }    

    public void loadTable(){
	//load table at startup with initial values read in from file
	JTable goalsTable = addGoalPage.getTblGoals();
	goalsTable.removeAll();
	TableModel model = goalsTable.getModel();
	for(int i = 0; i < goalsArray.size(); i++){
	    GoalModel currentGoal = goalsArray.get(i);
	    model.setValueAt(currentGoal.getCategory(), i, 5);
	    model.setValueAt(currentGoal.getGoalType(), i, 4);
	    model.setValueAt(currentGoal.getAmount(), i, 1);
	    model.setValueAt(currentGoal.getStartDate(), i, 2);
	    model.setValueAt(currentGoal.getEndDate(), i, 3);
	    model.setValueAt(currentGoal.getName(), i, 0);
	    hmGoalsByCategory.put(currentGoal.getCategory(), currentGoal.getAmount());
	}
	goalsTable.setModel(model);
	addGoalPage.setTblGoals(goalsTable);
	addGoalPage.repaint();
    }

    public void processNewGoal() {
	if(editing == true){
	    int selectedRow = addGoalPage.getTblGoals().getSelectedRow();
	    JTable table = addGoalPage.getTblGoals();
	    MyDefaultTableModel model = (MyDefaultTableModel)table.getModel();
	    model.setValueAt(addGoalPage.getTxtName().getText(), selectedRow, 0);
	    model.setValueAt(addGoalPage.getTxtAmount().getText(), selectedRow, 1);
	    model.setValueAt(addGoalPage.getDtpStartDate(), selectedRow, 2);
	    model.setValueAt(addGoalPage.getDtpStopDate(), selectedRow, 3);
	    //Can't change type of label from Income Goal to Expense Goal
	    model.setValueAt(addGoalPage.getCbxCategory().getSelectedItem().toString(), selectedRow, 5);

	    GoalModel updatedGoal = new GoalModel();
	    updatedGoal.setGoalId(goalsArray.get(selectedRow).getGoalId());
	    goalsArray.remove(selectedRow);
	    updatedGoal.setName(addGoalPage.getTxtName().getText());
	    updatedGoal.setAmount(Double.parseDouble(addGoalPage.getTxtAmount().getText()));
	    updatedGoal.setStartDate(Helper.dateToLocalDate(addGoalPage.getDtpStartDate().getDate()));
	    updatedGoal.setEndDate(Helper.dateToLocalDate(addGoalPage.getDtpStopDate().getDate()));


	    ComboItemController comboItemController = null;
            DB db = new DB();
	    ResultSet rs = db.getCategories(1);   // 1 for income categories, 2 for expense categories
	    try {
		while (rs.next()) {
		    System.out.println(rs.getString("Name"));
		    if(rs.getString("Name").equalsIgnoreCase(updatedGoal.getCategory ())){
			comboItemController = new ComboItemController(rs.getInt("ID"), rs.getString("Name"));
			break;
		    }
		}   
	    } catch (SQLException ex) {
		System.out.println("Exception while adding to DB from Expense Controller");
	    } 


	    //updatedGoal.setGoalType(comboItemController.getKey());
	    updatedGoal.setCategory(comboItemController.getValue());
	    //ONLY SETUP FOR INCOME GOALS CURRENTLY!!!!!
	    System.out.println("total goals before edit: " +totalGoals);
	    db.editGoal(updatedGoal.getGoalId(), updatedGoal.getName(), updatedGoal.getAmount(), updatedGoal.getStartDate(), updatedGoal.getEndDate(), comboItemController.getKey(), 1);
	    editing = false;
	}
	else{
	    GoalModel newGoal = getNewGoal();
	    addGoalToTable(newGoal);
	    addGoalToDB(newGoal);
	}
	System.out.println("total goals before edit: " +totalGoals);
	//        loadGoalsFromDB(1); //to get updated goals for labels
	ExpenseController.updateGoalsLabel();
	clearFields();
    }

    private GoalModel getNewGoal(){
	GoalModel newGoal = new GoalModel();
	String amount = addGoalPage.getTxtAmount().getText();
	String name = addGoalPage.getTxtName().getText();
	LocalDate startDate = Helper.dateToLocalDate(addGoalPage.getDtpStartDate().getDate());
	if(startDate == null){
	    startDate = LocalDate.now();
	}
	LocalDate endingDate = null;
	String category = addGoalPage.getCbxCategory().getSelectedItem().toString();
	endingDate = Helper.dateToLocalDate(addGoalPage.getDtpStopDate().getDate());
	if(endingDate == null || endingDate.toString() == " "){
	    endingDate = Helper.setEndDateIfNull(startDate);
	}
	newGoal.setEndDate(endingDate);
	newGoal.setAmount(Double.parseDouble(amount));
	newGoal.setCategory(category);
	newGoal.setStartDate(startDate);
	newGoal.setName(name);
	if(addGoalPage.getLblIncomeOrExpense().getText().contains("Income"))
	{
	    newGoal.setGoalType(1); //1 for income
	}
	else{
	    newGoal.setGoalType(2); //2 for expense
	}

	return newGoal;
    }

    private void addGoalToTable(GoalModel newGoal){
	//save goal to table
	JTable table = addGoalPage.getTblGoals();
	TableModel model = table.getModel();
	int inputLocation = goalsArray.size();
	model.setValueAt(newGoal.getCategory(), inputLocation, 5);
	model.setValueAt(newGoal.getGoalType(), inputLocation, 4);
	model.setValueAt(newGoal.getAmount(), inputLocation, 1);
	model.setValueAt(newGoal.getStartDate(), inputLocation, 2);
	model.setValueAt(newGoal.getEndDate(), inputLocation, 3);
	model.setValueAt(newGoal.getName(), inputLocation, 0);
	hmGoalsByCategory.put(newGoal.getCategory(), newGoal.getAmount());
    }    

    private void addGoalToDB(GoalModel goal){
	Object item = addGoalPage.getCbxCategory().getSelectedItem();
	int userId = LoginController.user.userID;
	//get selected combobox item and add to db as an int
	ComboItemController comboItemController = null;
        DB db = new DB();
	ResultSet rs = db.getCategories(goal.getGoalType());
	try {
	    while (rs.next()) {
		if(rs.getString("Name").equalsIgnoreCase(item.toString())){
		    comboItemController = new ComboItemController(rs.getInt("ID"), rs.getString("Name"));
		    break;
		}
	    }   } catch (SQLException ex) {
		System.out.println("Exception while adding to DB from Goal Controller");
	    }
	totalGoals += goal.getAmount();
	if(goal.getStartDate() == null){
	    goal.setStartDate(LocalDate.now());
	}
	if(goal.getEndDate() == null){
	    Helper.setEndDateIfNull(goal.getStartDate());
	}
	db.addGoal(userId, goal.getName(), goal.getAmount(), goal.getStartDate(), goal.getEndDate(), comboItemController.getKey(), goal.getGoalType());
	//retrieve goalId and attach to goalModel in the goalArray
	rs = db.getGoals(userId, goal.getGoalType());
	try {
	    while(rs.next()){
		if(rs.getString("Category").equals(goal.getCategory()) && rs.getDouble("Amount") == goal.getAmount() &&rs.getString("Name").equals(goal.getName())){
		    goal.setGoalId(rs.getInt("ID"));
		}
	    }
	} catch (SQLException ex) {
	    Logger.getLogger(ExpenseController.class.getName()).log(Level.SEVERE, null, ex);
	}
	goalsArray.add(goal);
    }

    /**
     * SECTION : Edit data within the program
     */

    public void startTableEdit() {
	//Get fields from selected row of table
	//Get selected row, a row must be selected!      
	JTable table = addGoalPage.getTblGoals();
	MyDefaultTableModel model = (MyDefaultTableModel)table.getModel();
	int selectedRow = addGoalPage.getTblGoals().getSelectedRow();        
	if(selectedRow != -1)//returns -1 when no row is selected
	{
	    editing = true;
	    String editName = model.getValueAt(selectedRow, 0).toString();
	    String editAmount = model.getValueAt(selectedRow, 1).toString();
	    String editStartDate = model.getValueAt(selectedRow, 2).toString();
	    String editEndDate = model.getValueAt(selectedRow, 3).toString();
	    String editGoalType = model.getValueAt(selectedRow, 4).toString();
	    String editCategory = model.getValueAt(selectedRow, 5).toString();

	    ComboItemController comboItemController = null;
	    //To set the category combobox to selected row's value
            DB db = new DB();
	    ResultSet rs = db.getCategories(1);   // 1 for income categories, 2 for expense categories
	    try {
		while (rs.next()) {
		    if(rs.getString("Name").equalsIgnoreCase(editCategory)){
			comboItemController = new ComboItemController(rs.getInt("ID"), rs.getString("Name"));
			break;
		    }
		}       
	    } catch (SQLException ex) {
		System.out.println("Exception while getting expense ID in Expense Controller startTableEdit");
	    }
	    addGoalPage.getCbxCategory().setSelectedIndex(1);
	    if(editAmount.contains(".")){
		int periodIndex = editAmount.indexOf(".");
		editAmount = editAmount.substring(0, periodIndex);
	    }
	    addGoalPage.getTxtAmount().setText(editAmount);
	    addGoalPage.getTxtName().setText(editName);
	    addGoalPage.getCbxCategory().setSelectedIndex(0);
	    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    try {
		Date startDate = df.parse(editStartDate);
		Date endDate = df.parse(editEndDate);
		addGoalPage.getDtpStartDate().setDate(startDate);
		addGoalPage.getDtpStopDate().setDate(endDate);
	    } catch (ParseException ex) {
		Logger.getLogger(GoalController.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
	else{//row = -1, user didn't select a row
	    editing = false;
	    JOptionPane.showMessageDialog(addGoalPage, "You must select a row to edit!");
	}
	//On "Add Expense" button click, update data: table, db, array, labels    }
    }

    /**
     * SECTION : Remove data from the program
     */

    public void deleteRecord() {
	//remove from table, remove from db, remove from goalArray, update labels
	GoalModel goalToRemove = deleteFromTable();
	deleteFromDB(goalToRemove);
	goalsArray.remove(goalToRemove);
	String category = goalToRemove.getCategory();
	double amountToSubtract = goalToRemove.getAmount();
	if(hmGoalsByCategory.containsKey(category)){
	    double newAmount = hmGoalsByCategory.get(category) - amountToSubtract;
	    hmGoalsByCategory.replace(category, newAmount);
	}
	totalGoals -= goalToRemove.getAmount();
	ExpenseController.updateGoalsLabel();
    }

    private GoalModel deleteFromTable(){
	JTable table = addGoalPage.getTblGoals();
	MyDefaultTableModel model = (MyDefaultTableModel)table.getModel();
	int selectedRow = table.getSelectedRow();
	GoalModel goalToDelete = null;
	//selectedRow = -1 if the user hasn't selected a row
	if (selectedRow != -1) {
	    model.removeRow(selectedRow);
	    double amountToRemove = goalsArray.get(selectedRow).getAmount();
	    String categoryToUpdate = goalsArray.get(selectedRow).getCategory();
	    double newAmount = hmGoalsByCategory.get(categoryToUpdate) - amountToRemove;
	    if(newAmount != 0){
		hmGoalsByCategory.replace(categoryToUpdate, newAmount);
	    }
	    else{
		hmGoalsByCategory.remove(categoryToUpdate);
	    }
	    model.addMoreRows(1, 6);
	    goalToDelete = goalsArray.get(selectedRow);
	    goalsArray.remove(selectedRow);
	    return goalToDelete;
	}
	return goalToDelete;
    }

    private void deleteFromDB(GoalModel goalToRemove){
        DB db = new DB();
	db.removeGoal(goalToRemove.getGoalId());
    }

    public boolean validateFields(){
	String amount = addGoalPage.getTxtAmount().getText();
	String name = addGoalPage.getTxtName().getText();
	if(Helper.validateAmount(amount) && Helper.validateStringWithSpaces(name)){
	    return true;
	}
	else{
	    return false;
	}
    }    

    private void clearFields(){
	addGoalPage.getTxtName().setText("");
	addGoalPage.getTxtAmount().setText("");
	addGoalPage.getCbxCategory().setSelectedIndex(0);
	addGoalPage.getDtpStartDate().getEditor().setValue(null);
	addGoalPage.getDtpStopDate().getEditor().setValue(null);
    }

    /**
     * SECTION : Getters 
     */
    public static double getTotalGoals(){
	return totalGoals;
    }

    public static ArrayList<GoalModel> getGoalsArray(){
	return goalsArray;
    }

    public static HashMap<String, Double> getGoalsByCategory(){
	return hmGoalsByCategory;
    }

}
