/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.IncomeModel;
import Models.MyDefaultTableModel;
import Views.AddIncomeView;
import Views.IncomeView;
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
public class IncomeController {
    //public static DB db = new DB();
    public static IncomeView incomePage;
    public static AddIncomeView addIncomePage;
    private static double totalIncome;
    private static HashMap<String, Double> hmIncomeByCategory = new HashMap<String, Double>();
    private static ArrayList<IncomeModel> incomeArray;
    private static boolean editing = false;

    /**
     * SECTION : Navigate within the program
     * 
     */

    private void closeAddIncomeView() {
	addIncomePage.getTxtAmount().setText("");
	addIncomePage.getCbxCategory().setSelectedIndex(0);
	addIncomePage.getChkRecurring().setSelected(false);
	addIncomePage.getCbxFrequency().setSelectedIndex(0);
	addIncomePage.setVisible(false); 
    }

    public void showIncomeView() {
	this.incomePage = new IncomeView();
	loadIncomeFromDB();
	loadTable();
	updateLabelsWithIncome();
	incomePage.setVisible(true);
    }

    public void returnToMainPage() {
	if(addIncomePage != null){
	    addIncomePage.setVisible(false);
	} 
	if(incomePage != null){
	    incomePage.setVisible(false);
	}
    }

    public void showAddIncomeView() {
	addIncomePage = new AddIncomeView();
	addIncomePage.setVisible(true);
    }

    /**
     * SECTION : Add data into the program / load data from database
     */


    private static IncomeModel getNewIncome() {
	IncomeModel income = new IncomeModel();
	String amount = addIncomePage.getTxtAmount().getText();
	String note = addIncomePage.getTxtaNote().getText();
	LocalDate startDate = Helper.dateToLocalDate(addIncomePage.getDtpDate().getDate());
	if(startDate == null){
	    startDate = LocalDate.now();
	}
	LocalDate endingDate = null;
	String category = addIncomePage.getCbxCategory().getSelectedItem().toString();
	boolean recurring = addIncomePage.getChkRecurring().isSelected();
	if(recurring == true){
	    endingDate = Helper.dateToLocalDate(addIncomePage.getDtpEndDate().getDate());
	    if(endingDate == null || " ".equals(endingDate.toString())){
		endingDate = Helper.setEndDateIfNull(startDate);
	    }
	    int frequency = 0;
	    ComboItemController comboItemController = null; //used to get the id of the selected item
	    Object item = addIncomePage.getCbxFrequency().getSelectedItem();
            DB db = new DB();
	    ResultSet rs = db.getFrequencies();
	    try {
		while (rs.next()) {
		    if(rs.getString("Name").equalsIgnoreCase(item.toString())){
			comboItemController = new ComboItemController(rs.getInt("ID"), rs.getString("Name"));
			frequency = Integer.parseInt(rs.getString("ID"));
			break;
		    }
		}   } catch (SQLException ex) {
		    System.out.println("Exception while getting new IncomeModel from Income Controller");
		}
	    income.setEndDate(endingDate);
	    income.setFrequency(frequency);
	}
	income.setAmount(Double.parseDouble(amount));
	income.setCategory(category);
	income.setRecurring(recurring);
	income.setDate(startDate);
	income.setNote(note);
	return income;    
    }

    private static void addIncomeToTable(IncomeModel newIncome) {
	//save income to table
	JTable table = incomePage.getTblIncome();
	MyDefaultTableModel model = (MyDefaultTableModel)table.getModel();
	int inputLocation = incomeArray.size()    ;
	model.setValueAt(newIncome.getCategory(), inputLocation, 0);
	model.setValueAt(newIncome.getAmount(), inputLocation, 1);
	model.setValueAt(newIncome.isRecurring(), inputLocation, 2);
	model.setValueAt(newIncome.getDate(), inputLocation, 3);
	model.setValueAt(newIncome.getEndDate(), inputLocation, 4);
	model.setValueAt(newIncome.getFrequency(), inputLocation, 5);
	model.setValueAt(newIncome.getNote(), inputLocation, 6);
	hmIncomeByCategory.put(newIncome.getCategory(), newIncome.getAmount());
    }

    private static void addIncomeToDB(IncomeModel newIncome) {
	Object item = addIncomePage.getCbxCategory().getSelectedItem();
	int userId = LoginController.user.userID;
	//get selected combobox item and add to db as an int
	ComboItemController comboItemController = null;
        DB db = new DB();
	ResultSet rs = db.getCategories(1);   // 1 for income categories, 2 for expense categories
	try {
	    while (rs.next()) {
		if(rs.getString("Name").equalsIgnoreCase(item.toString())){
		    comboItemController = new ComboItemController(rs.getInt("ID"), rs.getString("Name"));
		    break;
		}
	    }   } catch (SQLException ex) {
		System.out.println("Exception while adding to DB from Income Controller");
	    }
	totalIncome += newIncome.getAmount();
	db.addIncome(userId, newIncome.getAmount(), newIncome.getDate(), comboItemController.getKey(), newIncome.getNote(), newIncome.isRecurring(), newIncome.getFrequency(), newIncome.getEndDate());
	//retrieve incomeID and attach to incomemodel in the incomeArray
	rs = db.getExpenses(userId);
	try {
	    while(rs.next()){
		if(rs.getString("Category").equals(newIncome.getCategory())){
		    if(rs.getDouble("Amount") == newIncome.getAmount()){
			if(rs.getString("Notes").equals(newIncome.getNote())){
			    newIncome.setIncomeId(rs.getInt("ID"));
			}
		    }
		}
	    }
	} catch (SQLException ex) {
	    Logger.getLogger(ExpenseController.class.getName()).log(Level.SEVERE, null, ex);
	}
	incomeArray.add(newIncome);
    }

    public void processNewIncome() {
	if(editing == true){
	    //get selected row, update values in selected row
	    int selectedRow = incomePage.getTblIncome().getSelectedRow();        
	    JTable table = incomePage.getTblIncome();
	    MyDefaultTableModel model = (MyDefaultTableModel)table.getModel();
	    model.setValueAt(addIncomePage.getCbxCategory().getSelectedItem().toString(), selectedRow, 0);
	    model.setValueAt(addIncomePage.getTxtAmount().getText(), selectedRow, 1);
	    model.setValueAt(addIncomePage.getChkRecurring().isSelected(), selectedRow, 2);
	    model.setValueAt(addIncomePage.getDtpDate(), selectedRow, 3);
	    model.setValueAt(addIncomePage.getDtpEndDate(), selectedRow, 4);
	    model.setValueAt(addIncomePage.getCbxFrequency().getSelectedItem().toString(), selectedRow, 5);
	    model.setValueAt(addIncomePage.getTxtaNote().getText(), selectedRow, 6);
	    //update values in array at selected row
	    IncomeModel updatedIncome = new IncomeModel();
	    updatedIncome.setIncomeId(incomeArray.get(selectedRow).getIncomeId());
	    incomeArray.remove(selectedRow);
	    updatedIncome.setAmount(Double.parseDouble(addIncomePage.getTxtAmount().getText()));
	    updatedIncome.setCategory(addIncomePage.getCbxCategory().getSelectedItem().toString());
	    updatedIncome.setDate(Helper.dateToLocalDate(addIncomePage.getDtpDate().getDate()));
	    updatedIncome.setEndDate(Helper.dateToLocalDate(addIncomePage.getDtpEndDate().getDate()));
	    updatedIncome.setRecurring(addIncomePage.getChkRecurring().isSelected());
	    updatedIncome.setNote(addIncomePage.getTxtaNote().getText());
	    incomeArray.add(selectedRow, updatedIncome);
	    //update values in db at selected row
	    ComboItemController comboItemController = null;
            DB db = new DB();
	    ResultSet rs = db.getCategories(1);   // 1 for income categories, 2 for expense categories
	    try {
		while (rs.next()) {
		    if(rs.getString("Name").equalsIgnoreCase(updatedIncome.getCategory())){
			comboItemController = new ComboItemController(rs.getInt("ID"), rs.getString("Name"));
			break;
		    }
		}   } catch (SQLException ex) {
		    System.out.println("Exception while adding to DB from Expense Controller");
                    
		}            db.editIncome(updatedIncome.getIncomeId(), updatedIncome.getAmount(), updatedIncome.getDate(), comboItemController.getKey(), updatedIncome.getNote(), updatedIncome.isRecurring(), updatedIncome.getFrequency(), updatedIncome.getEndDate());
		editing = false;
		updateLabelsWithIncome();
		this.closeAddIncomeView();
	}
	else{
	    IncomeModel newIncome = getNewIncome();
	    addIncomeToTable(newIncome);
	    addIncomeToDB(newIncome);
	    this.closeAddIncomeView();
	}
	loadIncomeFromDB(); //to get updated income for labels
	updateLabelsWithIncome();
	ExpenseController.updateGoalsLabel();
    }

    public void addTestDataToDB(){
	int counter = 0;
        DB db = new DB();
	db.addIncome(6, 100.0, LocalDate.parse("2017-04-12"), 9, "Note test-" +Integer.toString(counter), 1, 1, LocalDate.parse("2019-04-12"));
	db.addIncome(6, 150.0, LocalDate.parse("2017-04-12"), 3, "Note test-"+Integer.toString(counter), 0, 0, LocalDate.parse("2019-04-13"));
	db.addIncome(6, 200.0, LocalDate.parse("2017-04-12"), 1, "Note test-" +Integer.toString(counter), 1, 1, LocalDate.parse("2019-04-14"));
	db.addIncome(6, 250.0, LocalDate.parse("2017-04-12"), 7, "Note test-"+Integer.toString(counter), 0, 0, LocalDate.parse("2019-04-15"));
    }

    public void loadTable(){
	//load table at startup with initial values read in from file
	JTable incomeTable = incomePage.getTblIncome();
	TableModel model = incomeTable.getModel();               
	for(int i = 0; i < incomeArray.size(); i++){
	    IncomeModel currentIncome = incomeArray.get(i);
	    model.setValueAt(currentIncome.getCategory(), i, 0);
	    model.setValueAt(currentIncome.getAmount(), i, 1);
	    model.setValueAt(currentIncome.isRecurring(), i, 2);
	    model.setValueAt(currentIncome.getDate(), i, 3); 
	    model.setValueAt(currentIncome.getNote(), i, 6);
	    if(currentIncome.isRecurring() == 1){
		if(currentIncome.getEndDate() == null){
		    Helper.setEndDateIfNull(currentIncome.getDate());
		}
		else{
		    model.setValueAt(currentIncome.getEndDate(), i, 4);  
		}
		model.setValueAt(currentIncome.getFrequency(), i, 5);
	    }
	    else{
		model.setValueAt(currentIncome.getDate(), i, 4);
		model.setValueAt("Not Applicable", i, 5);
	    }
	}
	incomeTable.setModel(model);
	incomePage.setTblIncome(incomeTable);
	incomePage.repaint();
    }

    public static void loadIncomeFromDB(){
	totalIncome = 0; //reset totalIncome so we can reload it with each record in db
	hmIncomeByCategory.clear();
	try{  
            DB db = new DB();
	    ResultSet rs = db.getIncome(LoginController.user.userID); //result set representing all income for a given userid
	    incomeArray = new ArrayList<>();
	    boolean nextRecordExists = rs.next();
	    while (nextRecordExists) {
		totalIncome += rs.getDouble("Amount");
		Double previousAmount = hmIncomeByCategory.get(rs.getString("Category")); //gets existing amount for category if it is in hashmap
		if(previousAmount != null){
		    hmIncomeByCategory.put(rs.getString("Category"), Double.parseDouble(rs.getString("Amount")) + previousAmount);
		}
		else{
		    previousAmount = 0.0;
		    hmIncomeByCategory.put(rs.getString("Category"), Double.parseDouble(rs.getString("Amount")) + previousAmount);
		}		
		IncomeModel income = new IncomeModel();
		income.setAmount(rs.getDouble("Amount"));
		income.setCategory(rs.getString("Category"));
		income.setDate(LocalDate.parse(rs.getString("Date")));
		income.setNote(rs.getString("Notes"));
		income.setIncomeId(rs.getInt("ID"));     
		income.setEndDate(Helper.dateToLocalDate(rs.getDate("EndDate")));
		incomeArray.add(income);
		nextRecordExists = rs.next();
	    }
	}
	catch(NumberFormatException | SQLException e){
	    System.out.println("Error occurred");
	}
    }

    /**
     * SECTION : Edit data within the program
     */

    public void startTableEdit() {
	//Get fields from selected row of table
	//Get selected row, a row must be selected!      
	JTable table = incomePage.getTblIncome();
	MyDefaultTableModel model = (MyDefaultTableModel)table.getModel();
	int selectedRow = incomePage.getTblIncome().getSelectedRow();        
	if(selectedRow != -1)//returns -1 when no row is selected
	{
	    editing = true;
	    String editCategory = model.getValueAt(selectedRow, 0).toString();
	    String editAmount = model.getValueAt(selectedRow, 1).toString();
	    String editRecurring = model.getValueAt(selectedRow, 2).toString();
	    String editStartDate = model.getValueAt(selectedRow, 3).toString();
	    String editEndDate = model.getValueAt(selectedRow, 4).toString();
	    String editFrequency = model.getValueAt(selectedRow, 5).toString();
	    String editNotes = model.getValueAt(selectedRow, 6).toString();
	    addIncomePage = new AddIncomeView();
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
	    //To set the frequency combobox to the selected row's value
	    if("Not Applicable".equals(editFrequency)){
		addIncomePage.getCbxFrequency().setSelectedIndex(0);
	    }
	    else{
		ComboItemController comboItemControllerFreq = null;
		rs = db.getFrequencies();
		try {
		    while (rs.next()) {
			System.out.println("Current Name = " +rs.getString("Name"));
			System.out.println("Current ID = " +rs.getString("ID"));
			System.out.println("Selected Item = " +editFrequency);
			if(rs.getString("Name").equalsIgnoreCase(editFrequency)){
			    comboItemControllerFreq = new ComboItemController(rs.getInt("ID"), rs.getString("Name"));
			    addIncomePage.getCbxFrequency().setSelectedIndex(comboItemControllerFreq.getKey());
			    break;
			}
		    } 
		} catch (SQLException ex) {
		    System.out.println("Exception while getting frequencies in ExpenseController edit method");
		}
	    }
	    addIncomePage.getCbxCategory().setSelectedIndex(0);
	    if(editAmount.contains(".")){
		int periodIndex = editAmount.indexOf(".");
		editAmount = editAmount.substring(0, periodIndex);
	    }
	    addIncomePage.getTxtAmount().setText(editAmount);
	    addIncomePage.getTxtaNote().setText(editNotes);
	    if("0".equals(editRecurring)){
		addIncomePage.getChkRecurring().setSelected(false);
	    }else{
		addIncomePage.getChkRecurring().setSelected(true);
	    }

	    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    try {
		Date startDate = df.parse(editStartDate);
		Date endDate = df.parse(editEndDate);
		addIncomePage.getDtpDate().setDate(startDate);
		addIncomePage.getDtpEndDate().setDate(endDate);
	    } catch (ParseException ex) {
		Logger.getLogger(ExpenseController.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    addIncomePage.setVisible(true);
	}
	else{//row = -1, user didn't select a row
	    editing = false;
	    JOptionPane.showMessageDialog(incomePage, "You must select a row to edit!");
	}
	//On "Add Expense" button click, update data: table, db, array, labels    }
    }


    /**
     * SECTION : Delete data from the program 
     */

    private IncomeModel deleteFromTable(){
	JTable table = incomePage.getTblIncome();
	MyDefaultTableModel model = (MyDefaultTableModel)table.getModel();
	int selectedRow = table.getSelectedRow();
	IncomeModel incomeToDelete = null;
	//selectedRow = -1 if the user hasn't selected a row
	if (selectedRow != -1) {
	    model.removeRow(selectedRow);
	    double amountToRemove = incomeArray.get(selectedRow).getAmount();
	    String categoryToUpdate = incomeArray.get(selectedRow).getCategory();
	    double newAmount = hmIncomeByCategory.get(categoryToUpdate) - amountToRemove;
	    if(newAmount != 0){
		hmIncomeByCategory.replace(categoryToUpdate, newAmount);
	    }
	    else{
		hmIncomeByCategory.remove(categoryToUpdate);
	    }	    model.addMoreRows(1, 7);
	    incomeToDelete = incomeArray.get(selectedRow);
	    incomeArray.remove(selectedRow);
	    return incomeToDelete;
	}
	return incomeToDelete;
    }

    private void deleteFromDB(IncomeModel incomeToRemove){
        DB db = new DB();
	db.removeExpense(incomeToRemove.getIncomeId());
    }

    public void deleteRecord() {
	//remove from table, remove from db, remove from expenseArray, subtract amount from hmExpensesByCategory, update pie graph & labels
	IncomeModel incomeToRemove = deleteFromTable();
	deleteFromDB(incomeToRemove);
	incomeArray.remove(incomeToRemove);
	String category = incomeToRemove.getCategory();
	double amountToSubtract = incomeToRemove.getAmount();
	if(hmIncomeByCategory.containsKey(category)){
	    double newAmount = hmIncomeByCategory.get(category) - amountToSubtract;
	    hmIncomeByCategory.replace(category, newAmount);
	}	totalIncome -= incomeToRemove.getAmount();
	updateLabelsWithIncome();    
    }

    /**
     * SECTION : Updates and Validation
     */

    public void updateLabelsWithIncome(){
	incomePage.getLblCurrentIncome().setText("Total Income: " +totalIncome);
	ExpenseController.updateMainPageLabels();
    }

    public boolean validateFields(){
	String amount = addIncomePage.getTxtAmount().getText();
	String note = addIncomePage.getTxtaNote().getText();
	if(Helper.validateAmount(amount)){
	    if("".equals(note)){
		return true;
	    }
	    else{
		if(Helper.validateStringWithSpaces(note)){
		    return true;
		}
	    }
	    return false;
	}
	else{
	    return false;
	}
    }

    /**
     * SECTION : Getters 
     */

    public static HashMap<String, Double> getIncomeByCategory(){
	return hmIncomeByCategory;
    }

    public static double getTotalIncome() {
	//load income from db to update totalIncome amount
	loadIncomeFromDB();
	return totalIncome;
    }

}
