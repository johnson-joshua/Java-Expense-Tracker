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
public class LimitModel {
    private String name;
    private double amount;
    private LocalDate startDate;
    private LocalDate endDate;
    private String category;
    private int limitType;
    private int limitId;

    public LimitModel(){
	this.name = "No name given";
	this.amount = 0.0;
	this.startDate = LocalDate.now();
	this.endDate = startDate;
	this.category = " ";
	this.limitType = 2; //2 for expense limit
    }

    public LimitModel(String name, double amount, LocalDate startDate, LocalDate endDate, String category, int limitType){
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
	this.limitType = limitType;
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

    public int getLimitType() {
	return limitType;
    }

    public void setLimitType(int limitType) {
	this.limitType = limitType;
    }

    public int getLimitId() {
	return limitId;
    }

    public void setLimitId(int limitId) {
	this.limitId = limitId;
    }

    @Override
    public String toString(){
	return name +"," + String.valueOf(amount) +"," + startDate.toString() +"," + endDate.toString() +"," + category +"," +limitType;
    }

}
