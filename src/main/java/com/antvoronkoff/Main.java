package  com.antvoronkoff;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class Main {

    static final String DATABASE_URL = "jdbc:postgresql://127.0.0.1:5432/minedb";
    static final String USER = "postgres";
    static final String PASSWORD = "pass";



    public static void main(String[] args){

        mainMenu();

    }

    static Scanner sc = new Scanner(System.in);


    static void mainMenu() {

        System.out.println("WELCOME");
        System.out.println();

        String pr;
        do {
            System.out.println();
            System.out.println("for check connection, press 0");

            System.out.print("\nfor exit, press q or Q: ");

            pr = sc.next().toUpperCase();

            switch (pr) {
                case "0":
                    checkStatus();
                    break;
                default:
                    break;
            }

        } while (!pr.equals("Q"));

    }

    public static void checkStatus(){
        try{ Connection connection = null;
        Statement statement = null;

        System.out.println("Registering JDBC driver...");

        Class.forName("org.postgresql.Driver");

        System.out.println("Creating database connection...");
        connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);

        System.out.println("Executing statement...");
        statement = connection.createStatement();

        String sql;
        //sql = "SELECT * FROM developers";
        sql="SELECT count(*) FROM link";

        ResultSet resultSet = statement.executeQuery(sql);

        System.out.println("Retrieving data from database...");
        System.out.println("\nStatus:");
        while (resultSet.next()) {
            int count= resultSet.getInt("count");



            System.out.println("\n================\n");
            System.out.println("count: " + count);

        }

        System.out.println("Closing connection and releasing resources...");
        resultSet.close();
        statement.close();
        connection.close();}
        catch(ClassNotFoundException exception){
        System.out.println(exception);

    }
        catch( SQLException exception){
        System.out.println(exception);

    }

    }
}