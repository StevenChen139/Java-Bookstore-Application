/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package coe528finalproject;

import coe528finalproject.Customer;
import java.io.FileReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Scanner;
import static java.lang.System.exit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author andycao
 */
public class CustomerList {
   private static String filename = "Customer.txt";
   ObservableList<Customer> tempCustomer = FXCollections.observableArrayList();
    
    public CustomerList(String Customers){
        filename = Customers;
    }
    
    public boolean loginCustomer(String user, String pass){
        boolean registerCustomer = false;
        try{
            Scanner s = new Scanner(new FileReader(filename));

            while (s.hasNext()) {
                String[] in = s.nextLine().split(", ");
                String username = in[0];
                String password = in[1];
                double points = Double.parseDouble(in[2]);
                int points2 = (int)points;
                if(user.equals(username) && pass.equals(password)){
                    registerCustomer = true;
                }
            }
            
        }catch(Exception e){
            System.out.println("Fail");
        }
        return registerCustomer;
    }
    public void addCustomer(Customer customer){
         try{   
            FileWriter customers = new FileWriter(filename, true);

            // , is splitting syntax
            String in = customer.getUsername() + ", " + customer.getPassword() + ", " + customer.getPoints() + "\n";
            customers.write(in);

            customers.close(); // close writer
         }catch (Exception e){
             System.out.println("Error");
         }
    }
    
    public void deleteCustomer(Customer customer){
        String textToRemove = customer.getUsername() + ", " + customer.getPassword() + ", " + customer.getPoints();
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            StringBuilder stringBuilder = new StringBuilder();
            
            String currentLine;
            
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.equals(textToRemove)) {
                    continue;
                }
                stringBuilder.append(currentLine);
                stringBuilder.append(System.getProperty("line.separator"));
            }
            
            reader.close();
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(stringBuilder.toString());
            writer.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void modifyCustomer(String oldInformation, String newInformation){ 
        File modifyFile = new File(filename);
        String oldCustomer = "";
        BufferedReader reader = null;
        FileWriter writer = null;
        
        try{
            reader = new BufferedReader(new FileReader(filename));
            String information = reader.readLine();
            while (information != null){
                oldCustomer = oldCustomer + information + System.lineSeparator();
                information = reader.readLine();
            }
         
            String newCustomer = oldCustomer.replaceAll(oldInformation, newInformation);
            writer = new FileWriter(modifyFile);
            writer.write(newCustomer);
            
        }catch (Exception e){
            System.out.println("Error");
        } 
            try {
                //Closing the resources

                reader.close();

                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    public ObservableList<Customer> readCustomer(){
        try{
            Scanner s = new Scanner(new FileReader(filename));

            while (s.hasNext()) {
                String[] in = s.nextLine().split(", ");
                String user = in[0];
                String pword = in[1];
                double points = Double.parseDouble(in[2]);
                int points2 = (int)points;
                tempCustomer.add(new Customer(user, pword, points2));
            }
        }catch (Exception e){
            System.out.println("Fail");
        }
        return tempCustomer;
    }
    
}
