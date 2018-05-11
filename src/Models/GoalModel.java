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
public class GoalModel {
    private String name;
    private double amount;
    private LocalDate startDate;
    private LocalDate endDate;
    private String category;
    private int goalType;
    private int goalId;

    public GoalModel(){
	this.name = "No name given";
	this.amount = 0.0;
	this.startDate = LocalDate.now();
	this.endDate = startDate;
	this.category = " ";
	this.goalType = 2; //2 for expense limit
    }

    public GoalModel(String name, double amount, LocalDate startDate, LocalDate endDate, String category, int goalType){
	this.name = name;
	this.amount = amount;
	if(startDate == null){
	    this.startDate = LocalDate.now();
	}
	else{
	    this.startDate = startDate;
	}
	if(endDate == null){
	    this.endDate = Helper.setEndDateIfNull(startDate);
	}
	else{
	    this.endDate = endDate;
	}
	this.category = category;
	this.goalType = goalType;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public double getAmount() {
	return amount;
    }

    public void setAmount(double amount) {
	this.amount = amount;
    }

    public LocalDate getStartDate() {
	return startDate;
    }

    public void setStartDate(LocalDate startDate) {
	this.startDate = startDate;
    }

    public LocalDate getEndDate() {
	return endDate;
    }

    public void setEndDate(LocalDate endDate) {
	this.endDate = endDate;
    }

    public String getCategory() {
	return category;
    }

    public void setCategory(String category) {
	this.category = category;
    }

    public int getGoalType() {
	return goalType;
    }

    public void setGoalType(int goalType) {
	this.goalType = goalType;
    }

    public int getGoalId() {
	return goalId;
    }

    public void setGoalId(int goalId) {
	this.goalId = goalId;
    }

    public String toString(){
	return name +"," + String.valueOf(amount) +"," + startDate.toString() +"," + endDate.toString() +"," + category +"," +goalType;
    }

}
