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
 * @author johnsonjm
 */
public class IncomeModel {
    private String category;
    private Double amount;
    private int recurring;
    private LocalDate date;
    private String note;
    private LocalDate endDate;
    private int frequency;
    private int userId;
    private int incomeId;

    public IncomeModel(){
	this.amount = 0.0;
	this.category = "";
	this.note = " ";
	this.date = LocalDate.now();
	this.endDate = date;
	this.recurring = 0;
	this.frequency = 0;
	this.userId = 0;
    }

    public IncomeModel(double amount, String category, LocalDate date, boolean recurring, LocalDate endDate, int frequency, String note, int userId){
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

    public int getFrequency() {
	return frequency;
    }

    public void setFrequency(int frequency) {
	this.frequency = frequency;
    }

    public String getCategory() {
	return category;
    }

    public void setCategory(String category) {
	this.category = category;
    }

    public void setIncomeId(int incomeId){
	this.incomeId = incomeId;
    }

    public int getIncomeId(){
	return this.incomeId;
    }

    public Double getAmount() {
	return amount;
    }

    public void setAmount(Double amount) {
	this.amount = amount;
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

    public LocalDate getDate() {
	return date;
    }

    public void setDate(LocalDate date) {
	this.date = date;
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

    public LocalDate getEndDate() {
	return endDate;
    }

    public void setEndDate(LocalDate endDate) {
	this.endDate = endDate;
    }

    @Override
    public String toString(){
	//TODO: REWRITE TOSTRING() IN FORMAT FOR DATAFILE
	//50,Groceries,2018-04-02,true,2018-07-13,monthly,note goes here
	return( getAmount() +"," +getCategory() +"," +getDate()+","+isRecurring()+","+getEndDate()+","+getFrequency()+","+getNote() );
    }
}
