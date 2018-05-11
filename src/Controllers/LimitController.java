/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.LimitModel;
import Models.MyDefaultTableModel;
import Views.AddLimitView;
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
public class LimitController {
    public static AddLimitView addLimitPage = new AddLimitView();
    private static DB db = new DB();
    private static HashMap<String, Double> hmLimitsByCategory = new HashMap<String, Double>();
    private static ArrayList<LimitModel> limitArray;
    private static double totalLimits; //Used for calculations and label updates
    private static boolean editing = false;

    /**
     * SECTION : Navigate within the program
     */
    public void closeAddLimitView() {
	if(addLimitPage.isVisible()){
	    clearFields();
	    addLimitPage.setVisible(false);
	}
    }

    /**
     * SECTION : Add data into the program / load from database
     */
    public void addExpenseLimit() {
	addLimitPage.getLblIncomeOrExpense().setText("Expense Limit");
	addLimitPage.getCbxCategory().removeAllItems();
	//get categories based on income limit/goal or expense limit/goal
	db = new DB();
	ResultSet rs = db.getCategories(2); //2 for expense categories
	try {
	    while (rs.next()) {
		addLimitPage.getCbxCategory().addItem(new ComboItemController(rs.getInt("ID"), rs.getString("Name")).toString());
	    }   } catch (SQLException ex) {
		Logger.getLogger(AddLimitView.class.getName()).log(Level.SEVERE, null, ex);
	    }
	loadLimitsFromDB(2);
	loadTable();
	addLimitPage.setVisible(true);
    }

    public void addIncomeLimit() {
	addLimitPage.getLblIncomeOrExpense().setText("Income Limit");
	addLimitPage.getCbxCategory().removeAllItems();
	//get categories based on income limit/goal or expense limit/goal
	db = new DB();
	ResultSet rs = db.getCategories(1); //1 for income categories
	try {
	    while (rs.next()) {
		addLimitPage.getCbxCategory().addItem(new ComboItemController(rs.getInt("ID"), rs.getString("Name")).toString());
	    }   } catch (SQLException ex) {
		Logger.getLogger(AddLimitView.class.getName()).log(Level.SEVERE, null, ex);
	    }
	loadLimitsFromDB(1); //pass 1 in to represent income limit
	loadTable();
	addLimitPage.setVisible(true);
    }

    public void loadTable(){
	//load table at startup with initial values read in from file
	JTable limitsTable = addLimitPage.getTblLimits();
	limitsTable.removeAll();
	TableModel model = limitsTable.getModel();
	for(int i = 0; i < limitArray.size(); i++){
	    LimitModel currentLimit = limitArray.get(i);
	    model.setValueAt(currentLimit.getCategory(), i, 5);
	    model.setValueAt(currentLimit.getLimitType(), i, 4);
	    model.setValueAt(currentLimit.getAmount(), i, 1);
	    model.setValueAt(currentLimit.getStartDate(), i, 2);
	    model.setValueAt(currentLimit.getEndDate(), i, 3);
	    model.setValueAt(currentLimit.getName(), i, 0);
	    hmLimitsByCategory.put(currentLimit.getCategory(), currentLimit.getAmount());
	}

	limitsTable.setModel(model);
	addLimitPage.setTblLimits(limitsTable);
	addLimitPage.repaint();
    }

    public void processNewLimit() {
	if(editing == true){
	    int selectedRow = addLimitPage.getTblLimits().getSelectedRow();
	    JTable table = addLimitPage.getTblLimits();
	    MyDefaultTableModel model = (MyDefaultTableModel)table.getModel();
	    model.setValueAt(addLimitPage.getTxtName().getText(), selectedRow, 0);
	    model.setValueAt(addLimitPage.getTxtAmount().getText(), selectedRow, 1);
	    model.setValueAt(addLimitPage.getDtpStartDate(), selectedRow, 2);
	    model.setValueAt(addLimitPage.getDtpEndDate(), selectedRow, 3);
	    //Can't change type of label from Income Goal to Expense Goal
	    model.setValueAt(addLimitPage.getCbxCategory().getSelectedItem().toString(), selectedRow, 5);
	    LimitModel updatedLimit = new LimitModel();
	    updatedLimit.setLimitId(limitArray.get(selectedRow).getLimitId());
	    limitArray.remove(selectedRow);
	    updatedLimit.setName(addLimitPage.getTxtName().getText());
	    updatedLimit.setAmount(Double.parseDouble(addLimitPage.getTxtAmount().getText()));
	    updatedLimit.setStartDate(Helper.dateToLocalDate(addLimitPage.getDtpStartDate().getDate()));
	    updatedLimit.setEndDate(Helper.dateToLocalDate(addLimitPage.getDtpEndDate().getDate()));
	    ComboItemController comboItemController = null;
	    ResultSet rs = db.getCategories(2);   // 1 for income categories, 2 for expense categories
	    try {
		while (rs.next()) {
		    System.out.println(rs.getString("Name"));
		    if(rs.getString("Name").equalsIgnoreCase(updatedLimit.getCategory ())){
			comboItemController = new ComboItemController(rs.getInt("ID"), rs.getString("Name"));
			break;
		    }
		}   
	    } catch (SQLException ex) {
		System.out.println("Exception while adding to DB from Limit Controller");
	    } 
	    updatedLimit.setCategory(comboItemController.getValue());
	    //ONLY SETUP FOR INCOME GOALS CURRENTLY!!!!!
	    System.out.println("total limits before edit: " +totalLimits);
	    db.editLimit(updatedLimit.getLimitId(), updatedLimit.getName(), updatedLimit.getAmount(), updatedLimit.getStartDate(), updatedLimit.getEndDate(), comboItemController.getKey(), 2);
	    editing = false;
	}
	else{
	    LimitModel newLimit = getNewLimit();
	    addLimitToTable(newLimit);
	    addLimitToDB(newLimit);
	}
	ExpenseController.updateWarningLabel();
	clearFields();
    }

    private void addLimitToTable(LimitModel newLimit){
	//save limit to table
	JTable table = addLimitPage.getTblLimits();
	TableModel model = table.getModel();
	int inputLocation = limitArray.size();
	model.setValueAt(newLimit.getCategory(), inputLocation, 5);
	model.setValueAt(newLimit.getLimitType(), inputLocation, 4);
	model.setValueAt(newLimit.getAmount(), inputLocation, 1);
	model.setValueAt(newLimit.getStartDate(), inputLocation, 2);
	model.setValueAt(newLimit.getEndDate(), inputLocation, 3);
	model.setValueAt(newLimit.getName(), inputLocation, 0);
    }    

    private LimitModel getNewLimit(){
	LimitModel newLimit = new LimitModel();
	String amount = addLimitPage.getTxtAmount().getText();
	String name = addLimitPage.getTxtName().getText();
	LocalDate startDate = Helper.dateToLocalDate(addLimitPage.getDtpStartDate().getDate());
	if(startDate == null){
	    startDate = LocalDate.now();
	}
	LocalDate endingDate = null;
	String category = addLimitPage.getCbxCategory().getSelectedItem().toString();
	endingDate = Helper.dateToLocalDate(addLimitPage.getDtpEndDate().getDate());
	if(" ".equals(endingDate.toString()) || endingDate == null){
	    endingDate = Helper.setEndDateIfNull(startDate);
	}
	newLimit.setEndDate(endingDate);
	newLimit.setAmount(Double.parseDouble(amount));
	newLimit.setCategory(category);
	newLimit.setStartDate(startDate);
	newLimit.setName(name);
	if(addLimitPage.getLblIncomeOrExpense().getText().contains("Income"))
	{
	    newLimit.setLimitType(1); //1 for income
	}
	else{
	    newLimit.setLimitType(2); //2 for expense
	}

	Double previousAmount = hmLimitsByCategory.get(newLimit.getCategory()); //gets existing amount for category if it is in hashmap
	if(previousAmount != null){
	    hmLimitsByCategory.put(newLimit.getCategory(), newLimit.getAmount() + previousAmount);
	}
	else{
	    previousAmount = 0.0;
	    hmLimitsByCategory.put(newLimit.getCategory(), newLimit.getAmount() + previousAmount);
	}

	return newLimit;
    }

    public static void loadLimitsFromDB(int type) { //type specifies income or expense limits
	totalLimits = 0;
	hmLimitsByCategory.clear();
	limitArray = new ArrayList<>();
	try{  
	    ResultSet rs = db.getLimits(LoginController.user.userID, type);
	    boolean nextRecordExists = rs.next();
	    while (nextRecordExists) {
		totalLimits += rs.getDouble("Amount");
		Double previousAmount = hmLimitsByCategory.get(rs.getString("Category")); //gets existing amount for category if it is in hashmap
		if(previousAmount != null){
		    hmLimitsByCategory.put(rs.getString("Category"), Double.parseDouble(rs.getString("Amount")) + previousAmount);
		}
		else{
		    previousAmount = 0.0;
		    hmLimitsByCategory.put(rs.getString("Category"), Double.parseDouble(rs.getString("Amount")) + previousAmount);
		}
		LimitModel limit = new LimitModel();
		limit.setAmount(rs.getDouble("Amount"));
		limit.setCategory(rs.getString("Category"));
		limit.setStartDate(LocalDate.parse(rs.getString("StartDate")));
		limit.setName(rs.getString("Name"));
		limit.setLimitId(rs.getInt("ID"));
		limit.setEndDate(Helper.dateToLocalDate(rs.getDate("EndDate")));
		limit.setLimitType(Helper.convertTypeToInt(rs.getString("Type")));
		limitArray.add(limit);
		nextRecordExists = rs.next();
	    }
	}
	catch(NumberFormatException | SQLException e){
	    System.out.println("Error occurred");
	}
    }    

    private void addLimitToDB(LimitModel limit){
	Object item = addLimitPage.getCbxCategory().getSelectedItem();
	int userId = LoginController.user.userID;
	//get selected combobox item and add to db as an int
	ComboItemController comboItemController = null;
	ResultSet rs = db.getCategories(limit.getLimitType()); // 1 for income categories, 2 for expense categories
	try {
	    while (rs.next()) {
		if(rs.getString("Name").equalsIgnoreCase(item.toString())){
		    comboItemController = new ComboItemController(rs.getInt("ID"), rs.getString("Name"));
		    break;
		}
	    }   } catch (SQLException ex) {
		System.out.println("Exception while adding to DB from Limit Controller");
	    }
	totalLimits += limit.getAmount();
	if(limit.getStartDate() == null){
	    limit.setStartDate(LocalDate.now());
	}
	if(limit.getEndDate() == null){
	    Helper.setEndDateIfNull(limit.getStartDate());
	}
	db.addLimit(userId, limit.getName(), limit.getAmount(), limit.getStartDate(), limit.getEndDate(), comboItemController.getKey(), limit.getLimitType());
	//retrieve limitId and attach to limitModel in the limitArray
	rs = db.getLimits(userId, limit.getLimitType());
	try {
	    while(rs.next()){
		if(rs.getString("Category").equals(limit.getCategory()) && rs.getDouble("Amount") == limit.getAmount() &&rs.getString("Name").equals(limit.getName())){
		    limit.setLimitId(rs.getInt("ID"));
		}
	    }
	} 
	catch (SQLException ex) {
	    Logger.getLogger(LimitController.class.getName()).log(Level.SEVERE, null, ex);
	}
	limitArray.add(limit);
    }

    /**
     * SECTION : Edit data within the program
     */
    public void startTableEdit() {
	//Get fields from selected row of table
	//Get selected row, a row must be selected!      
	JTable table = addLimitPage.getTblLimits();
	MyDefaultTableModel model = (MyDefaultTableModel)table.getModel();
	int selectedRow = addLimitPage.getTblLimits().getSelectedRow();        
	if(selectedRow != -1)//returns -1 when no row is selected
	{
	    editing = true;
	    String editName = model.getValueAt(selectedRow, 0).toString();
	    String editAmount = model.getValueAt(selectedRow, 1).toString();
	    String editStartDate = model.getValueAt(selectedRow, 2).toString();
	    String editEndDate = model.getValueAt(selectedRow, 3).toString();
	    String editLimitType = model.getValueAt(selectedRow, 4).toString();
	    String editCategory = model.getValueAt(selectedRow, 5).toString();
	    ComboItemController comboItemController = null;
	    //To set the category combobox to selected row's value
	    ResultSet rs = db.getCategories(2);   // 1 for income categories, 2 for expense categories
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
	    addLimitPage.getCbxCategory().setSelectedIndex(1);
	    if(editAmount.contains(".")){
		int periodIndex = editAmount.indexOf(".");
		editAmount = editAmount.substring(0, periodIndex);
	    }
	    addLimitPage.getTxtAmount().setText(editAmount);
	    addLimitPage.getTxtName().setText(editName);
	    addLimitPage.getCbxCategory().setSelectedIndex(0);
	    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    try {
		Date startDate = df.parse(editStartDate);
		Date endDate = df.parse(editEndDate);
		addLimitPage.getDtpStartDate().setDate(startDate);
		addLimitPage.getDtpEndDate().setDate(endDate);
	    } catch (ParseException ex) {
		Logger.getLogger(LimitController.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
	else{//row = -1, user didn't select a row
	    editing = false;
	    JOptionPane.showMessageDialog(addLimitPage, "You must select a row to edit!");
	}
	//On "Add Goal" button click, update data: table, db, array, labels    }
    }

    /**
     * SECTION : Remove data from the program
     */
    public void deleteRecord() {
	//remove from table, remove from db, remove from limitArray, update labels
	LimitModel limitToRemove = deleteFromTable();
	deleteFromDB(limitToRemove);
	limitArray.remove(limitToRemove);
	String category = limitToRemove.getCategory();
	double amountToSubtract = limitToRemove.getAmount();
	if(hmLimitsByCategory.containsKey(category)){
	    double newAmount = hmLimitsByCategory.get(category) - amountToSubtract;
	    hmLimitsByCategory.replace(category, newAmount);
	}
	totalLimits -= limitToRemove.getAmount();
	ExpenseController.updateWarningLabel();
    }

    private LimitModel deleteFromTable(){
	JTable table = addLimitPage.getTblLimits();
	MyDefaultTableModel model = (MyDefaultTableModel)table.getModel();
	int selectedRow = table.getSelectedRow();
	LimitModel limitToDelete = null;
	//selectedRow = -1 if the user hasn't selected a row
	if (selectedRow != -1) {
	    model.removeRow(selectedRow);
	    double amountToRemove = limitArray.get(selectedRow).getAmount();
	    String categoryToUpdate = limitArray.get(selectedRow).getCategory();
	    double newAmount = hmLimitsByCategory.get(categoryToUpdate) - amountToRemove;
	    if(newAmount != 0){
		hmLimitsByCategory.replace(categoryToUpdate, newAmount);
	    }
	    else{
		hmLimitsByCategory.remove(categoryToUpdate);
	    }
	    model.addMoreRows(1, 6);
	    limitToDelete = limitArray.get(selectedRow);
	    limitArray.remove(selectedRow);
	    return limitToDelete;
	}
	return limitToDelete;
    }

    private void deleteFromDB(LimitModel limitToRemove){
	db.removeLimit(limitToRemove.getLimitId());
    }

    /**
     * SECTION : Updates and Validation
     */
    private void clearFields(){
	addLimitPage.getTxtName().setText("");
	addLimitPage.getTxtAmount().setText("");
	addLimitPage.getCbxCategory().setSelectedIndex(0);
	addLimitPage.getDtpStartDate().getEditor().setValue(null);
	addLimitPage.getDtpEndDate().getEditor().setValue(null);
    }

    public boolean validateFields(){
	String amount = addLimitPage.getTxtAmount().getText();
	String name = addLimitPage.getTxtName().getText();

	if(Helper.validateAmount(amount) && Helper.validateStringWithSpaces(name)){
	    return true;
	}
	else{
	    return false;
	}
    }    

    /**
     * SECTION : Getters 
     */
    public static double getTotalLimits(){
	return totalLimits;
    }

    public static ArrayList<LimitModel> getLimitsArray(){
	return limitArray;
    }

    public static HashMap<String, Double> getLimitsByCategory(){
	return hmLimitsByCategory;
    }

}
