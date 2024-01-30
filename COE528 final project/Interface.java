/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package coe528finalproject;

import javafx.scene.text.Text;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import java.awt.Dimension;
import java.awt.Toolkit;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

/**
 *
 * @author andycao
 */
public class Interface extends Application {
    //?
    // universal window size vars
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int userH = screenSize.height;
    int userW = screenSize.width;
    // 
    int windowwidth = userW / 2;
    int windowlength = userH / 2;
    int widthdiv = windowwidth / 100; // universal space horizontal space between elems
    int lengthdiv = userH / 100;
    // universal fonts
    int normalfontsize = 20;
    int menufontsize = 30;
    
    Customer currentCustomer;
    ObservableList<Book> Books = FXCollections.observableArrayList();    
    ObservableList<Book> chosenBooks = FXCollections.observableArrayList();
    ObservableList<Customer> customer = FXCollections.observableArrayList();    
    ObservableList<Customer> chosenCustomer = FXCollections.observableArrayList();

    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Bookstore App");
        Button login = new Button();
        login.setText("Login");
        login.setMaxWidth(90);
        login.setMaxHeight(70);
        
        Button exit = new Button();
        exit.setText("Exit");
        exit.setMaxWidth(90);
        exit.setMaxHeight(70);
        
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setVgap(10);
        grid.setHgap(12);
        
        Text usernameWritten = new Text("Login");
        Text passwordWritten = new Text("Password");
        
        TextField usernameInput = new TextField();
        usernameInput.setPromptText("Enter Username: ");
        PasswordField passwordInput = new PasswordField();
        passwordInput.setPromptText("Enter Password: ");
        
        GridPane.setConstraints(usernameWritten, 0, 0);
        GridPane.setConstraints(passwordWritten,0,1);
        GridPane.setConstraints(usernameInput,1,0);
        GridPane.setConstraints(passwordInput,1,1);
        GridPane.setConstraints(login, 3,0);
        GridPane.setConstraints(exit,3,1);
        grid.getChildren().addAll(login,usernameWritten,passwordWritten,usernameInput,passwordInput,exit);  
        
        login.setOnAction((ActionEvent e) ->{
            CustomerList customers = new CustomerList("Customer.txt");
            String username = usernameInput.getText();
            String password = passwordInput.getText();
            if(username.equals("admin") && password.equals("admin")){
                ownerWindow(primaryStage);
            }else if(customers.loginCustomer(username, password) == true){
                customerWindow(primaryStage, username, password);
            }else{
                loginFailWindow(primaryStage);
            }
        });
        
        exit.setOnAction((ActionEvent e) ->{
            primaryStage.close();
        });
        
        Scene scene = new Scene(grid, 350, 100);
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }
    
    public void loginFailWindow(Stage primaryStage){
        primaryStage.setTitle("Bookstore App");
        Text loginFail = new Text("Error: Usernamme or Password may be Invalid");
        Text please = new Text("Please check is username and password was enter correctly");
        
        Button back = new Button("Back");
        back.setMinSize(100,40);
        
        back.setOnAction((ActionEvent e) ->{
           start(primaryStage); 
        });
        
        
        VBox loginFailScreen = new VBox();
        loginFailScreen.getChildren().addAll( loginFail, please, back);
        loginFailScreen.setPadding(new Insets(40, 40, 40, 40));
        loginFailScreen.setAlignment(Pos.CENTER);
        loginFailScreen.setSpacing(20);
        Scene scene = new Scene(loginFailScreen);
        primaryStage.setTitle("Bookstore App");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        
    }
    
    public void customerWindow(Stage primaryStage, String username, String password){
        BookStorage BookStorage = new BookStorage("Books.txt");
        CustomerList customers = new CustomerList("Customer.txt");
        customer = customers.readCustomer();
        for (int i = 0; i < customer.size(); i++){
            if(customer.get(i).getUsername().equals(username)){
                currentCustomer = customer.get(i);
            }
        }
        Text welcome = new Text("Welcome " + username + ". You have " + currentCustomer.getPoints() + " Points" + ". Your Status is " + currentCustomer.getRank());
        
        TableView<Book> bookTable;
        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setMinWidth(250);
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        
        TableColumn<Book, String> priceColumn = new TableColumn<>("Price");
        priceColumn.setMinWidth(100);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        TableColumn<Book, Boolean> chosenColumn = new TableColumn<>("Select");
        chosenColumn.setMinWidth(100);
        chosenColumn.setCellValueFactory(new PropertyValueFactory<>("check"));
        
        bookTable = new TableView<>();
        bookTable.setItems(BookStorage.readBook());
        bookTable.getColumns().addAll(titleColumn, priceColumn, chosenColumn);
        
        Button buyBook = new Button("Buy");
        buyBook.setMinSize(100,40);
        Button buyRedeemBook = new Button("Buy  and Redeem");
        buyRedeemBook.setMinSize(100,40);
        Button logout = new Button("Logout");
        logout.setMinSize(100,40);
    
        buyBook.setOnAction((ActionEvent e) ->{
            Books = bookTable.getItems();
            chosenBooks = currentCustomer.chosenBooks(Books);
            int points = currentCustomer.getPoints();

            
            purchaseWindow(primaryStage, currentCustomer.buyBook(chosenBooks), currentCustomer.getPoints(), currentCustomer.getRank());
            customers.modifyCustomer(currentCustomer.getUsername() + ", " + currentCustomer.getPassword() + ", " + points, currentCustomer.getUsername() + ", " + currentCustomer.getPassword() + ", " + currentCustomer.getPoints());
            for(int i = 0; i < chosenBooks.size(); i ++){
                BookStorage.deleteBook(chosenBooks.get(i));
            }
        });
        
        buyRedeemBook.setOnAction((ActionEvent e) ->{
            Books = bookTable.getItems();
            chosenBooks = currentCustomer.chosenBooks(Books);
            int points = currentCustomer.getPoints();
            
            purchaseWindow(primaryStage, currentCustomer.redeemBuyBook(chosenBooks), currentCustomer.getPoints(), currentCustomer.getRank());
            customers.modifyCustomer(currentCustomer.getUsername() + ", " + currentCustomer.getPassword() + ", " + points, currentCustomer.getUsername() + ", " + currentCustomer.getPassword() + ", " + currentCustomer.getPoints());
            for(int i = 0; i < chosenBooks.size(); i ++){
                BookStorage.deleteBook(chosenBooks.get(i));
            }
        });
        
        logout.setOnAction((ActionEvent e) ->{
           start(primaryStage); 
        });
        VBox customerScene = new VBox();
        customerScene.getChildren().addAll(welcome, bookTable, buyBook, buyRedeemBook, logout);
        customerScene.setPadding(new Insets(40, 40, 40, 40));
        customerScene.setAlignment(Pos.CENTER);
        customerScene.setSpacing(20);
        Scene scene = new Scene(customerScene);
        primaryStage.setTitle("Bookstore App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    
    public void purchaseWindow(Stage primaryStage, double totalCost, int points, String rank){
        Text pruchaseCost = new Text(30, 50, "Total Cost: $" + totalCost);
        Text pointsGained = new Text(30, 50, "Points: " + points);
        Text currentRank = new Text(30, 50, "Status: " + currentCustomer.getRank());
        
        Button logout = new Button("Logout");
        logout.setMinSize(100, 40);
        
        VBox purchaseScene = new VBox(3);
        purchaseScene.getChildren().addAll(pruchaseCost, pointsGained, currentRank, logout);
        purchaseScene.setPadding(new Insets(40, 40, 40, 40));
        purchaseScene.setAlignment(Pos.CENTER);
        purchaseScene.setSpacing(20);
        
        logout.setOnAction((ActionEvent e) ->{
           start(primaryStage); 
        });
        
        Scene scene = new Scene(purchaseScene, 330, 250);
        primaryStage.setTitle("Bookstore App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void ownerWindow(Stage primaryStage) {
        // menu vars
        int buttonspacingH = windowlength / 3;
        int buttonspacingV = windowwidth / 6;

        String uniVbuttonpara = " -fx-font-size: 20px; -fx-border-width: 5px;";

        // button creation and formatting
        Button btn_ownerBook = new Button();
        btn_ownerBook.setText("Books");
        btn_ownerBook.setStyle(uniVbuttonpara);

        Button btn_ownerCustomers = new Button();
        btn_ownerCustomers.setText("Customers");
        btn_ownerCustomers.setStyle(uniVbuttonpara);

        Button btn_ownerLogout = new Button();
        btn_ownerLogout.setText("Logout");
        btn_ownerLogout.setStyle(uniVbuttonpara);

        // button functions
        btn_ownerBook.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    oBooksWindow(primaryStage);
                } catch (Exception e) {
                }
            }
        });
        btn_ownerCustomers.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    oCustWindow(primaryStage);
                } catch (Exception e) {
                }
            }
        });
        btn_ownerLogout.setOnAction((ActionEvent e) ->{
           start(primaryStage); 
        });

        // vbox and hbox creation and formatting
        HBox ownerBtns = new HBox(buttonspacingH, btn_ownerBook, btn_ownerCustomers, btn_ownerLogout);
        ownerBtns.setAlignment(Pos.BOTTOM_CENTER);
        Label admin = new Label("Welcome Admin");
        admin.setFont(new Font(menufontsize));

        VBox labelandbtns = new VBox(buttonspacingV, admin, ownerBtns);
        labelandbtns.setAlignment(Pos.CENTER);
        Scene scene = new Scene(labelandbtns, windowwidth, windowlength);

        primaryStage.setTitle("Bookstore App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    
    public void oBooksWindow(Stage p){
        BookStorage BookStorage = new BookStorage("Books.txt"); 
        p.setTitle("Books");

        // sizing vars
        int tablecolmin = 250;
        String uniVbuttonpara = " -fx-font-size: 15px; ";

        // textfields
        Label t1 = new Label("Title:");
        t1.setFont(new Font(normalfontsize));
        TextField tIn = new TextField();
        Label t2 = new Label("Price:");
        t2.setFont(new Font(normalfontsize));
        TextField pIn = new TextField();
        Label t3 = new Label();
        t3.setFont(new Font(normalfontsize));

        // table
        TableView<Book> booktable = new TableView<>();
        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setMinWidth(tablecolmin);
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Book, String> priceColumn = new TableColumn<>("Price");
        priceColumn.setMinWidth(100);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        booktable.setPlaceholder(new Label("No books to display")); // if no books
        booktable.prefWidthProperty().bind(p.widthProperty()); // fix table width to window width

        // data
       
        booktable.setItems(BookStorage.readBook());
        booktable.getColumns().addAll(titleColumn, priceColumn);
        
        // select rows
        TableViewSelectionModel selectmode = booktable.getSelectionModel();
        selectmode.setSelectionMode(SelectionMode.MULTIPLE);

        // buttons
        Button add = new Button("Add");
        add.setStyle(uniVbuttonpara);
        add.setOnAction((ActionEvent e) ->{
            String title = tIn.getText();
            String price = pIn.getText();
            double price2 = Double.parseDouble(price);
            Book book = new Book(title, price2);
            BookStorage.bookWrite(book);
            oBooksWindow(p);
        });
        Button del = new Button("Delete");
        del.setStyle(uniVbuttonpara);
        del.setOnAction ((ActionEvent e) -> {
            try{
            Books = booktable.getItems();
            chosenBooks = booktable.getSelectionModel().getSelectedItems();
            System.out.println(chosenBooks.get(0).getTitle());
            BookStorage.deleteBook(chosenBooks.get(0));
            oBooksWindow(p);
            }catch(Exception f){
                System.out.println(f);
            }
        });
        Button bck = new Button("Back");
        bck.setStyle(uniVbuttonpara);
        bck.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    ownerWindow(p);
                } catch (Exception e) {
                }
            }
        });
        int butspace = 10;
        int txtspace = 10;

        // format
        HBox hTxt = new HBox(txtspace, t1, tIn, t2, pIn);
        HBox hBut = new HBox(butspace, add, del, bck);
        VBox labelandbuttons = new VBox(butspace, hTxt, t3, hBut);
        VBox vWindow = new VBox(butspace, booktable, labelandbuttons);

        hTxt.setAlignment(Pos.CENTER);
        hBut.setAlignment(Pos.CENTER);
        labelandbuttons.setAlignment(Pos.CENTER);
        //vWindow.setAlignment(Pos.BOTTOM_CENTER);
        Scene s = new Scene(vWindow, windowwidth, windowlength);

        p.setScene(s);

    }
    
    public void oCustWindow(Stage s){
        CustomerList customers = new CustomerList("Customer.txt");
        s.setTitle("Customers");

        // sizing vars
        int tablecolmin = 250;
        String uniVbuttonpara = " -fx-font-size: 15px; ";

        // textfields
        Label t1 = new Label("Username:");
        t1.setFont(new Font(normalfontsize));
        TextField uIn = new TextField();
        Label t2 = new Label("Password:");
        t2.setFont(new Font(normalfontsize));
        TextField pIn = new TextField();
        Label t3 = new Label();
        t3.setFont(new Font(normalfontsize));

        // table
        TableView<Customer> customerTable = new TableView<>();
        TableColumn<Customer, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setMinWidth(tablecolmin);
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<Customer, String> passwordColumn = new TableColumn<>("Password");
        passwordColumn.setMinWidth(100);
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        TableColumn<Customer, String> pointsColumn = new TableColumn<>("Points");
        pointsColumn.setMinWidth(100);
        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));

        customerTable.setPlaceholder(new Label("No customers to display")); // if no cust
        customerTable.prefWidthProperty().bind(s.widthProperty()); // fix table width to window width
        // headers
        customerTable.setItems(customers.readCustomer());
        customerTable.getColumns().addAll(usernameColumn, passwordColumn, pointsColumn);
        
        // select rows
        TableViewSelectionModel selectmode = customerTable.getSelectionModel();
        selectmode.setSelectionMode(SelectionMode.MULTIPLE);

        // buttons
        Button add = new Button("Add");
        add.setStyle(uniVbuttonpara);
        add.setOnAction((ActionEvent e) ->{
           String name = uIn.getText();
           String password = pIn.getText();
           Customer customer = new Customer(name, password);
           customers.addCustomer(customer);
           oCustWindow(s);
           
        });

        Button delete = new Button("Delete");
        delete.setStyle(uniVbuttonpara);
        delete.setOnAction ((ActionEvent e) -> {
            try{
            customer = customerTable.getItems();
            chosenCustomer = customerTable.getSelectionModel().getSelectedItems();
            System.out.println(chosenCustomer.get(0).getUsername());
            customers.deleteCustomer(chosenCustomer.get(0));
            oCustWindow(s);
            }catch(Exception f){
                System.out.println(f);
            }
        });
        Button back = new Button("Back");
        back.setStyle(uniVbuttonpara);
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    ownerWindow(s);
                } catch (Exception e) {
                }
            }
        });

        int butspace = 10;
        int txtspace = 10;

        // Hbox and Vbox creation 
        HBox hTxt = new HBox(txtspace, t1, uIn, t2, pIn);
        HBox hBut = new HBox(butspace, add, delete, back);
        VBox labelandbuttons = new VBox(butspace, hTxt, t3, hBut);
        VBox vWindow = new VBox(butspace, customerTable, labelandbuttons);
        vWindow.setPadding(new Insets(40,40,40,40));
        vWindow.setSpacing(20);
        vWindow.setAlignment(Pos.CENTER);

        //positions
        hTxt.setAlignment(Pos.CENTER);
        hBut.setAlignment(Pos.CENTER);
        labelandbuttons.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vWindow, windowwidth, windowlength);
        s.setScene(scene);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
