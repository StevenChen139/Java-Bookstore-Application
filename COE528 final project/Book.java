/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package coe528finalproject;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
/**
 *
 * @author andycao
 */
public class Book {
    private final String title;
    private final double price;
    public CheckBox check;
    
    public Book(String title, double price){
        this.title = title;
        this.price = price;
        check = new CheckBox();
    }
    
    public String getTitle(){
        return title;
    }
    
    public double getPrice(){
        return price;
    }
    
    public CheckBox getCheck(){
        return check;
    }
    
    public void setCheck(CheckBox check){
        this.check = check;
        
    }
}
