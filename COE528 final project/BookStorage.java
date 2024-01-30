/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coe528finalproject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

/**
 *
 * @author N
 */
public class BookStorage {
    private static String filename = "Books.txt";
    ObservableList<Book> tempbook = FXCollections.observableArrayList();

    public BookStorage(String Books){
        filename = Books;
    }
    // write to book
    public void bookWrite(Book Book){
        try{
            FileWriter Books = new FileWriter(filename, true);

            // , is splitting syntax
            String in = Book.getTitle() + ", " + Book.getPrice() + "\n";
            Books.write(in);

            Books.close(); // close writer
        }catch (Exception e){
            System.out.println("Error");
        }
    }


    // del book
    public void deleteBook(Book Book){
        String textToRemove = Book.getTitle() + ", " + Book.getPrice();
        
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

    // read from books
    public ObservableList<Book> readBook(){
        try{
            Scanner s = new Scanner(new FileReader(filename));

            while (s.hasNext()) {
                String[] in = s.nextLine().split(",");
                String title = in[0];
                double price = Double.parseDouble(in[1]);
                tempbook.add(new Book(title, price));
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return tempbook;
    }

}