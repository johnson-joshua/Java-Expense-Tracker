/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.ExpenseModel;
import Models.MyDefaultTableModel;
import Views.AddExpenseView;
import Views.ExpenseView;
import Views.MainPageView;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.TableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author johnsonjm
 */
public class ExpenseController {
    public static MainPageView mainPage;
    public static AddExpenseView addExpensePage;
    public static ExpenseView expensePage;
    //public static DB db = new DB();
    public static ArrayList<ExpenseModel> expenseArray;
    private static HashMap<String, Double> hmExpensesByCategory = new HashMap<String, Double>();
    private static double totalExpense; 
    private static boolean editing = false;

    /**
     * SECTION : Navigation within the application
     */
    public static void returnToMainPage() {
	if(addExpensePage != null){
	    addExpensePage.setVisible(false);
	} 
	if(expensePage != null){
	    expensePage.setVisible(false);
	}
    }

    public static void showAddExpenseView() {
	addExpensePage = new AddExpenseView();
	addExpensePage.setResizable(false);
	addExpensePage.setVisible(true);
    }

    public void closeAddExpenseView(){
	addExpensePage.getTxtAmount().setText("");
	addExpensePage.getCbxCategory().setSelectedIndex(0);
	addExpensePage.getChkRecurring().setSelected(false);
	addExpensePage.getCbxFrequency().setSelectedIndex(0);
	addExpensePage.setVisible(false);
    }

    public void showExpenseView() {
	this.expensePage = new ExpenseView();
	loadTable();
	updateLabelsWithExpense();
	this.expensePage.setVisible(true);
    }

    public void showMainPage() {
	mainPage = new MainPageView();
	loadExpensesFromDB();
	showExpensesOnPieChart();
	updateWarningLabel();
	updateGoalsLabel();
	mainPage.setVisible(true);
    }

    /**
     * SECTION : Add data into the program / Read from database
     */

    public void addTestDataToDB(){
	int counter = 0;
        DB db = new DB();
	db.addExpense(6, 10.0, LocalDate.parse("2017-04-12"), 9, "Note test-" +Integer.toString(counter), 1, 1, LocalDate.parse("2019-04-12"));        
	db.addExpense(6, 10.0, LocalDate.parse("2017-04-12"), 9, "Note test-" +Integer.toString(counter), 1, 1, LocalDate.parse("2019-04-12"));
	db.addExpense(6, 15.0, LocalDate.parse("2017-04-12"), 3, "Note test-"+Integer.toString(counter), 0, 0, LocalDate.parse("2019-04-13"));
	db.addExpense(6, 20.0, LocalDate.parse("2017-04-12"), 1, "Note test-" +Integer.toString(counter), 1, 1, LocalDate.parse("2019-04-14"));
	db.addExpense(6, 25.0, LocalDate.parse("2017-04-12"), 7, "Note test-"+Integer.toString(counter), 0, 0, LocalDate.parse("2019-04-15"));
    }

    public static void loadExpensesFromDB(){
	totalExpense = 0;
	try{  
            DB db = new DB();
	    ResultSet rs = db.getExpenses(LoginController.user.userID);
	    expenseArray = new ArrayList<>();
	    boolean nextRecordExists = rs.next();
	    while (nextRecordExists) {
		totalExpense += rs.getDouble("Amount");
		Double previousAmount = hmExpensesByCategory.get(rs.getString("Category")); //gets existing amount for category if it is in hashmap
		if(previousAmount != null){
		    hmExpensesByCategory.put(rs.getString("Category"), Double.parseDouble(rs.getString("Amount")) + previousAmount);
		}
		else{
		    previousAmount = 0.0;
		    hmExpensesByCategory.put(rs.getString("Category"), Double.parseDouble(rs.getString("Amount")) + previousAmount);
		}
		ExpenseModel expense = new ExpenseModel();
		expense.setAmount(rs.getDouble("Amount"));
		expense.setCategory(rs.getString("Category"));
		expense.setDate(LocalDate.parse(rs.getString("Date")));
		expense.setNote(rs.getString("Notes"));
		expense.setExpenseId(rs.getInt("ID"));
		expense.setEndDate(Helper.dateToLocalDate(rs.getDate("EndDate")));
		//TODO: ADD FREQUENCY TO THE EXPENSE BEFORE ADDING TO ARRAY
		expenseArray.add(expense);
		nextRecordExists = rs.next();
	    }
	}
	catch(Exception e){
	    System.out.println("Error occurred");
	}
    }

    public void processNewExpense() {
	if(editing == true){
	    //get selected row, update values in selected row
	    int selectedRow = expensePage.getTblExpense().getSelectedRow();        
	    JTable table = expensePage.getTblExpense();
	    MyDefaultTableModel model = (MyDefaultTableModel)table.getModel();
	    model.setValueAt(addExpensePage.getCbxCategory().getSelectedItem().toString(), selectedRow, 0);
	    model.setValueAt(addExpensePage.getTxtAmount().getText(), selectedRow, 1);
	    model.setValueAt(addExpensePage.getChkRecurring().isSelected(), selectedRow, 2);
	    model.setValueAt(addExpensePage.getDtpStartDate(), selectedRow, 3);
	    model.setValueAt(addExpensePage.getDtpEndDate(), selectedRow, 4);
	    model.setValueAt(addExpensePage.getCbxFrequency().getSelectedItem().toString(), selectedRow, 5);
	    model.setValueAt(addExpensePage.getTxtaNote().getText(), selectedRow, 6);
	    //update values in array at selected row
	    ExpenseModel updatedExpense = new ExpenseModel();
	    updatedExpense.setExpenseId(expenseArray.get(selectedRow).getExpenseId());
	    expenseArray.remove(selectedRow);
	    updatedExpense.setAmount(Double.parseDouble(addExpensePage.getTxtAmount().getText()));
	    updatedExpense.setCategory(addExpensePage.getCbxCategory().getSelectedItem().toString());
	    updatedExpense.setDate(Helper.dateToLocalDate(addExpensePage.getDtpStartDate().getDate()));
	    updatedExpense.setEndDate(Helper.dateToLocalDate(addExpensePage.getDtpEndDate().getDate()));
	    updatedExpense.setRecurring(addExpensePage.getChkRecurring().isSelected());
	    //updatedExpense.setFrequency(addExpensePage.getCbxFrequency().getSelectedItem().toString());
	    updatedExpense.setNote(addExpensePage.getTxtaNote().getText());
	    expenseArray.add(selectedRow, updatedExpense);
	    //update values in db at selected row
            DB db = new DB();
	    ComboItemController comboItemController = null;
	    ResultSet rs = db.getCategories(2);   // 1 for income categories, 2 for expense categories
	    try {
		while (rs.next()) {
		    if(rs.getString("Name").equalsIgnoreCase(updatedExpense.getCategory())){
			comboItemController = new ComboItemController(rs.getInt("ID"), rs.getString("Name"));
			break;
		    }
		}   } catch (SQLException ex) {
		    System.out.println("Exception while adding to DB from Expense Controller");
		}            db.editExpense(updatedExpense.getExpenseId(), updatedExpense.getAmount(), updatedExpense.getDate(), comboItemController.getKey(), updatedExpense.getNote(), updatedExpense.isRecurring(), updatedExpense.getFrequency(), updatedExpense.getEndDate());
		editing = false;
		//        totalExpense -= updatedExpense.getAmount();
		updateLabelsWithExpense();
		this.closeAddExpenseView();
	}
	else{
	    ExpenseModel newExpense = getNewExpense();
	    addExpenseToTable(newExpense);
	    addExpenseToDB(newExpense);
	    closeAddExpenseView();
	}
	loadExpensesFromDB(); //to get updated expenses for labels
	updatePieChart();
	updateLabelsWithExpense();
	updateWarningLabel();
    }

    private void addExpenseToTable(ExpenseModel expense){
	//save expense to table
	JTable table = expensePage.getTblExpense();
	TableModel model = table.getModel();
	int inputLocation = expenseArray.size();
	model.setValueAt(expense.getCategory(), inputLocation, 0);
	model.setValueAt(expense.getAmount(), inputLocation, 1);
	model.setValueAt(expense.isRecurring(), inputLocation, 2);
	model.setValueAt(expense.getDate(), inputLocation, 3);
	model.setValueAt(expense.getEndDate(), inputLocation, 4);
	model.setValueAt(expense.getFrequency(), inputLocation, 5);
	model.setValueAt(expense.getNote(), inputLocation, 6);
	hmExpensesByCategory.put(expense.getCategory(), expense.getAmount());
    }

    public void loadTable(){
	//load table at startup with initial values read in from file
	JTable expenseTable = expensePage.getTblExpense();
	TableModel model = expenseTable.getModel();
	for(int i = 0; i < expenseArray.size(); i++){
	    ExpenseModel currentExpense = expenseArray.get(i);
	    model.setValueAt(currentExpense.getCategory(), i, 0);
	    model.setValueAt(currentExpense.getAmount(), i, 1);
	    model.setValueAt(currentExpense.isRecurring(), i, 2);
	    model.setValueAt(currentExpense.getDate(), i, 3);
	    model.setValueAt(currentExpense.getNote(), i, 6);
	    if(currentExpense.isRecurring() == 1){
		if(currentExpense.getEndDate() == null){
		    Helper.setEndDateIfNull(currentExpense.getDate());
		}
		else{
		    model.setValueAt(currentExpense.getEndDate(), i, 4);  
		}
		model.setValueAt(currentExpense.getFrequency(), i, 5);
	    }
	    else{
		model.setValueAt(currentExpense.getDate(), i, 4);
		model.setValueAt("Not Applicable", i, 5);
	    }
	}
	expenseTable.setModel(model);
	expensePage.setTblExpense(expenseTable);
	expensePage.repaint();
    }

    private void addExpenseToDB(ExpenseModel expense){
	Object item = addExpensePage.getCbxCategory().getSelectedItem();
        DB db = new DB();
	int userId = LoginController.user.userID;
	//get selected combobox item and add to db as an int
	ComboItemController comboItemController = null;
	ResultSet rs = db.getCategories(2);   // 1 for income categories, 2 for expense categories
	try {
	    while (rs.next()) {
		if(rs.getString("Name").equalsIgnoreCase(item.toString())){
		    comboItemController = new ComboItemController(rs.getInt("ID"), rs.getString("Name"));
		    break;
		}
	    }   } catch (SQLException ex) {
		System.out.println("Exception while adding to DB from Expense Controller");
	    }
	totalExpense += expense.getAmount();
	db.addExpense(userId, expense.getAmount(), expense.getDate(), comboItemController.getKey(), expense.getNote(), expense.isRecurring(), expense.getFrequency(), expense.getEndDate());
	//retrieve expenseID and attach to expensemodel in the expensearray
	rs = db.getExpenses(userId);
	try {
	    while(rs.next()){
		if(rs.getString("Category").equals(expense.getCategory())){
		    if(expense.getAmount() == rs.getDouble("Amount")){
			if(rs.getString("Notes").equals(expense.getNote())){
			    expense.setExpenseId(rs.getInt("ID"));
			}
		    }
		}
	    }
	} catch (SQLException ex) {
	    Logger.getLogger(ExpenseController.class.getName()).log(Level.SEVERE, null, ex);
	}
	expenseArray.add(expense);
    }

    /**
     * Edit data within the program
     */

    public void startTableEdit() {
	//Get fields from selected row of table
	//Get selected row, a row must be selected!      
	JTable table = expensePage.getTblExpense();
	MyDefaultTableModel model = (MyDefaultTableModel)table.getModel();
	int selectedRow = expensePage.getTblExpense().getSelectedRow();        
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
	    addExpensePage = new AddExpenseView();
	    ComboItemController comboItemController = null;
	    //To set the category combobox to selected row's value
            DB db = new DB();
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
	    //To set the frequency combobox to the selected row's value
	    if("Not Applicable".equals(editFrequency)){
		addExpensePage.getCbxFrequency().setSelectedIndex(0);
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
			    addExpensePage.getCbxFrequency().setSelectedIndex(comboItemControllerFreq.getKey());
			    break;
			}
		    } 
		} catch (SQLException ex) {
		    System.out.println("Exception while getting frequencies in ExpenseController edit method");
		}
	    }

	    //Show add expense page with prefilled data
	    addExpensePage.getCbxCategory().setSelectedIndex(0);
	    if(editAmount.contains(".")){
		int periodIndex = editAmount.indexOf(".");
		editAmount = editAmount.substring(0, periodIndex);
	    }
	    addExpensePage.getTxtAmount().setText(editAmount);
	    addExpensePage.getTxtaNote().setText(editNotes);
	    if("0".equals(editRecurring)){
		addExpensePage.getChkRecurring().setSelected(false);
	    }else{
		addExpensePage.getChkRecurring().setSelected(true);
	    }
	    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    try {
		Date startDate = df.parse(editStartDate);
		Date endDate = df.parse(editEndDate);
		addExpensePage.getDtpStartDate().setDate(startDate);
		addExpensePage.getDtpEndDate().setDate(endDate);
	    } catch (ParseException ex) {
		Logger.getLogger(ExpenseController.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    addExpensePage.setVisible(true);
	}
	else{//row = -1, user didn't select a row
	    editing = false;
	    JOptionPane.showMessageDialog(expensePage, "You must select a row to edit!");
	}
	//On "Add Expense" button click, update data: table, db, array, labels
    }

    public void editRecord() {
	editing = true;
	startTableEdit();
    }

    private ExpenseModel getNewExpense(){
	ExpenseModel expense = new ExpenseModel();
	String amount = addExpensePage.getTxtAmount().getText();
	String note = addExpensePage.getTxtaNote().getText();
	LocalDate startDate = Helper.dateToLocalDate(addExpensePage.getDtpStartDate().getDate());
	if(startDate == null){
	    startDate = LocalDate.now();
	}
	LocalDate endingDate = null;
	String category = addExpensePage.getCbxCategory().getSelectedItem().toString();
	boolean recurring = addExpensePage.getChkRecurring().isSelected();
	if(recurring == true){
	    endingDate = Helper.dateToLocalDate(addExpensePage.getDtpEndDate().getDate());
	    if(endingDate == null || " ".equals(endingDate.toString())){
		endingDate = Helper.setEndDateIfNull(startDate);
	    }
	    int frequency = 0;
	    ComboItemController comboItemController = null;
	    Object item = addExpensePage.getCbxFrequency().getSelectedItem();
            DB db = new DB();
	    ResultSet rs = db.getFrequencies();
	    try {
		while (rs.next()) {
		    if(rs.getString("Name").equalsIgnoreCase(item.toString())){
			comboItemController = new ComboItemController(rs.getInt("ID"), rs.getString("Name"));
			frequency = Integer.parseInt(rs.getString("ID"));
			break;
		    }
		}   
	    } catch (SQLException ex) {
		System.out.println("Exception while getting new ExpenseModel from Expense Controller");
	    }
	    expense.setEndDate(endingDate);
	    expense.setFrequency(frequency);
	}
	expense.setAmount(Double.parseDouble(amount));
	expense.setCategory(category);
	expense.setRecurring(recurring);
	expense.setDate(startDate);
	expense.setNote(note);
	return expense;
    }

    /**
     * SECTION : Remove data from the program
     */

    public void deleteRecord() {
	//remove from table, remove from db, remove from expenseArray, subtract amount from hmExpensesByCategory, update pie graph & labels
	ExpenseModel expenseToRemove = deleteFromTable();
	deleteFromDB(expenseToRemove);
	expenseArray.remove(expenseToRemove);
	String category = expenseToRemove.getCategory();
	double amountToSubtract = expenseToRemove.getAmount();
	if(hmExpensesByCategory.containsKey(category)){
	    double newAmount = hmExpensesByCategory.get(category) - amountToSubtract;
	    hmExpensesByCategory.replace(category, newAmount);
	}
	totalExpense -= expenseToRemove.getAmount();
	updateLabelsWithExpense();
	updatePieChart();
    }

    private ExpenseModel deleteFromTable(){
	JTable table = expensePage.getTblExpense();
	MyDefaultTableModel model = (MyDefaultTableModel)table.getModel();
	int selectedRow = table.getSelectedRow();
	ExpenseModel expenseToDelete = null;
	//selectedRow = -1 if the user hasn't selected a row
	if (selectedRow != -1) {
	    model.removeRow(selectedRow);
	    double amountToRemove = expenseArray.get(selectedRow).getAmount();
	    String categoryToUpdate = expenseArray.get(selectedRow).getCategory();
	    double newAmount = hmExpensesByCategory.get(categoryToUpdate) - amountToRemove;
	    if(newAmount != 0){
		hmExpensesByCategory.replace(categoryToUpdate, newAmount);
	    }
	    else{
		hmExpensesByCategory.remove(categoryToUpdate);
	    }
	    model.addMoreRows(1, 7);
	    expenseToDelete = expenseArray.get(selectedRow);
	    expenseArray.remove(selectedRow);
	    return expenseToDelete;
	}
	return expenseToDelete;
    }

    private void deleteFromDB(ExpenseModel expenseToRemove){
        DB db = new DB();
	db.removeExpense(expenseToRemove.getExpenseId());
    }

    /**
     * SECTION : Updates and validation
     */

    public boolean validateFields(){
	String amount = addExpensePage.getTxtAmount().getText();
	String note = addExpensePage.getTxtaNote().getText();
	if(Helper.validateAmount(amount) && Helper.validateStringWithSpaces(note)){
	    return true;
	}
	else{
	    return false;
	}
    }

    public void updateLabelsWithExpense(){
	expensePage.getLblTotalExpense().setText("Total Expenses: " +totalExpense);
	updateMainPageLabels();
    }

    public static void updatePieChart(){
	//will load breakdown of expenses by category into pie chart
	Set categoryKeys = hmExpensesByCategory.keySet();
	DefaultPieDataset pieDataSet = new DefaultPieDataset();
	//lambda expression
	categoryKeys.forEach((s) -> pieDataSet.setValue((String)s, hmExpensesByCategory.get(s)));
	JFreeChart pieChart = ChartFactory.createPieChart3D("Expenses By Category", pieDataSet);
	BufferedImage image;
	image = pieChart.createBufferedImage(400, 225);
	JLabel label = mainPage.getLblGraph();
	label.setText("");
	ImageIcon icon = new ImageIcon(image);
	label.setIcon(icon);
	mainPage.setLblGraph(label);
    }

    public static void updateMainPageLabels(){
	double currentBalance = 0.0;
	double totalIncome = IncomeController.getTotalIncome();
	currentBalance = totalIncome - totalExpense;
	JLabel lblBalance = mainPage.getLblBalance();
	lblBalance.setText("Current Balance is: " +currentBalance);
	if(currentBalance < 0){
	    lblBalance.setForeground(Color.RED);
	}
	else{
	    lblBalance.setForeground(Color.BLACK);
	}
    }

    public static void updateWarningLabel(){
	//if Expense in a certain category goes over a limit, update label with red font
	LimitController.loadLimitsFromDB(2);
	HashMap<String, Double> limitsByCategory = LimitController.getLimitsByCategory();
	String category = "";
	for(String s : limitsByCategory.keySet()){
	    if(hmExpensesByCategory.containsKey(s))
	    {
		Double limitAmount = limitsByCategory.get(s);
		Double expenseAmount = hmExpensesByCategory.get(s);
		//there is a limit for the current expense, check if we are still under the limit
		if(expenseAmount >= limitAmount){
		    category += s + " ";
		}
	    }
	}
	if(!"".equals(category)){
	    mainPage.getLblWarnings().setBorder(new LineBorder(Color.RED, 1, true));
	    mainPage.getLblWarnings().setForeground(Color.RED);
	    mainPage.getLblWarnings().setText("You have exceeded your limit for category: " +category);
	}
	else{
	    mainPage.getLblWarnings().setBorder(javax.swing.BorderFactory.createEmptyBorder());
	    mainPage.getLblWarnings().setForeground(Color.BLACK);
	    mainPage.getLblWarnings().setText("");
	}


    }

    public static void updateGoalsLabel(){
	//if income in a certain category goes over a goal, update label with green font
	GoalController.loadGoalsFromDB(1);
	IncomeController.loadIncomeFromDB();
	HashMap<String, Double> goalsByCategory = GoalController.getGoalsByCategory();
	HashMap<String, Double> incomeByCategory = IncomeController.getIncomeByCategory();
	String category = "";
	for(String s : goalsByCategory.keySet()){
	    if(incomeByCategory.containsKey(s))
	    {
		Double goalAmount = goalsByCategory.get(s);
		Double incomeAmount = incomeByCategory.get(s);
		//there is a limit for the current expense, check if we are still under the limit
		if(incomeAmount >= goalAmount){
		    category += s + " ";
		}
	    }
	}
	if(!"".equals(category)){
	    mainPage.getLblGoalsBanner().setBorder(new LineBorder(Color.GREEN, 1, true));
	    mainPage.getLblGoalsBanner().setForeground(Color.GREEN);
	    mainPage.getLblGoalsBanner().setText("You have exceeded your goal for category: " +category);
	}
	else{
	    mainPage.getLblGoalsBanner().setBorder(javax.swing.BorderFactory.createEmptyBorder());
	    mainPage.getLblGoalsBanner().setText("");
	}
    }

    public static void showExpensesOnPieChart(){
	//will load breakdown of expenses by category into pie chart
	Set categoryKeys = hmExpensesByCategory.keySet();
	DefaultPieDataset pieDataSet = new DefaultPieDataset();
	//lambda expression
	categoryKeys.forEach((s) -> pieDataSet.setValue((String)s, (hmExpensesByCategory.get(s))));
	JFreeChart pieChart = ChartFactory.createPieChart3D("Expenses By Category", pieDataSet);
	BufferedImage image;
	image = pieChart.createBufferedImage(400, 225);
	JLabel label = mainPage.getLblGraph();
	label.setText("");
	ImageIcon icon = new ImageIcon(image);
	label.setIcon(icon);
	mainPage.setLblGraph(label);
	updateMainPageLabels();
    }    
}//end class
