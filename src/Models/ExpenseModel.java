/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import Controllers.Helper;
import java.time.LocalDate;

/**
 *
 * @author anchor
 */
public class ExpenseModel {
    private double amount;
    private String category;
    private String note;
    private LocalDate date;
    private LocalDate endDate;
    private int recurring;
    private int frequency;
    private int userId;
    private int expenseId;

    public ExpenseModel(){
	this.amount = 0.0;
	this.category = "";
	this.note = " ";
	this.date = LocalDate.now();
	//this.endDate = Helper.setEndDateIfNull(date);
	this.endDate = date;
	this.recurring = 0;
	this.frequency = 0;
	this.userId = 0;
    }



    public ExpenseModel(double amount, String category, LocalDate date, boolean recurring, LocalDate endDate, int frequency, String note, int userId){
	this.amount = amount;
	this.category = category;
	if(note == ""){
	    this.note = " ";
	}
	else{
	    this.note = note;
	}
	if(date == null){
	    this.date = LocalDate.now();
	}
	else{
	    this.date = date;
	}
	if(endDate == null){
	    this.endDate = Helper.setEndDateIfNull(date);
	}
	else{
	    this.endDate = endDate;
	}
	if(recurring == false){
	    this.recurring = 0;
	}
	else{
	    this.recurring = 1;
	}
	this.frequency = frequency;
	this.userId = userId;
    }

    public int getUserId(){
	return userId;
    }

    //        public void setUserId(int userId){
    //            this.userId = userId;
    //        }

    public int getExpenseId(){
	return expenseId;
    }

    public void setExpenseId(int id){//because ID is set by database, we want to be able to set expenseID in expenseModel
	this.expenseId = id;
    }

    public double getAmount() {
	return amount;
    }

    public void setAmount(double amount) {
	this.amount = amount;
    }

    public String getCategory() {
	return category;
    }

    public void setCategory(String category) {
	this.category = category;
    }

    public String getNote() {
	if("".equals(note)){
	    return " ";
	}
	else{
	    return note;
	}
    }

    public void setNote(String note) {
	if("".equals(note)){
	    this.note = " ";
	}
	else{
	    this.note = note;
	}
    }

    public LocalDate getDate() {
	return date;
    }

    public void setDate(LocalDate date) {
	this.date = date;
    }

    public LocalDate getEndDate() {
	return endDate;
    }

    public void setEndDate(LocalDate endDate) {
	this.endDate = endDate;
    }

    public int isRecurring() {
	return this.recurring;
    }

    public void setRecurring(boolean recurring) {
	if(recurring){
	    this.recurring = 1;
	}
	else{
	    this.recurring = 0;
	}
	//this.recurring = recurring;
    }


    public int getFrequency() {
	return frequency;
    }

    public void setFrequency(int frequency) {
	//            if(frequency == ""){
	//                frequency = "Not Applicable";
	//            }
	//            else{
	this.frequency = frequency;
	//            }
    }

    @Override
    public String toString(){
	//TODO: REWRITE TOSTRING() IN FORMAT FOR DATAFILE
	//50,Groceries,2018-04-02,true,2018-07-13,monthly,note goes here
	return( getAmount() +"," +getCategory() +"," +getDate()+","+isRecurring()+","+getEndDate()+","+getFrequency()+","+getNote() );
    }
}
