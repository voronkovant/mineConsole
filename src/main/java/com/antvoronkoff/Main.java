package  com.antvoronkoff;

import java.sql.*;
import java.util.*;
import java.io.IOException;


public class Main {

    static final String DATABASE_URL = "jdbc:postgresql://127.0.0.1:5432/minedb";
    static final String USER = "postgres";
    static final String PASSWORD = "Qwerty1";



    public static void main(String[] args){

        mainMenu();

    }

    static Scanner sc = new Scanner(System.in);


    static void mainMenu() {

        System.out.println("WELCOME");
        System.out.println();

        String pr;
        do {
            clearScreen();
            System.out.println();
            System.out.println("for check connection, press 0");
            System.out.println("for add link, press 1");
            System.out.println("for search link by tags, press 2");
            System.out.print("\nfor exit, press q or Q: ");

            pr = sc.next().toUpperCase();

            switch (pr) {
                case "0":
                    checkStatus();
                    break;
                case "1":
                    addNewLink();
                    break;
                case "2":
                    findLinkByTag();
                    break;
                default:
                    break;
            }

        } while (!pr.equals("Q"));

    }

    public static void checkStatus(){
        try{
            Connection connection = null;
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
        sc.next();
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

    static void addNewLink() {

        String val;
        String repeatAdd="n";
        // ArrayList<Student> newInputStudents = new ArrayList<>();
//-----------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------
        boolean value;
//-----------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------
        System.out.println();
        System.out.println("Insert link ");

        do {
            System.out.println();
            System.out.print("Insert val ");

            val = sc.next().trim();
            while (val.isEmpty()) {
                System.out.println("val is empty, retry: ");
                val = sc.nextLine().trim();

            }
            UUID link = UUID.randomUUID();
            int rowsAffected = insertNewLink(link, val);

            if (rowsAffected == 0) {
                System.out.println("Something went wrong.");
            } else {
                System.out.println("link was saved");

                System.out.println();
                do{
                    System.out.println("add tag?  (Y/N): ");
                    repeatAdd = sc.next().toUpperCase();
                    if(repeatAdd.equals("Y")){
                        addTag(link);
                    }
                }while(!repeatAdd.equals("N"));
                clearScreen();
                System.out.print("\nAdd new link?   (Y/N): ");
                repeatAdd = sc.next().toUpperCase();
            }
        }while (repeatAdd.equals("Y")) ;
    }

    static void findLinkByTag() {
        clearScreen();
        String val;
        ArrayList<UUID> filterTag=new ArrayList<>();
        String repeatAdd="n";
        // ArrayList<Student> newInputStudents = new ArrayList<>();
//-----------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------
        boolean value;
//-----------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------

        do {
            System.out.println("Insert tag:");

            val = sc.next().trim();
            while (val.isEmpty()) {
                System.out.println("tag is empty, retry: ");
                val = sc.nextLine().trim();

            }

            UUID tag= findTagByVal(val);
            if(tag==null){
                    System.out.println("tag no founded");
                } else {
                    filterTag.add(tag);
                    System.out.println("tag added to filter");
                }
                System.out.println("add tag?  (Y/N): ");
                repeatAdd = sc.next().toUpperCase();

        }while (repeatAdd.equals("Y")) ;
        findLinkByTag(filterTag);
    }

    public static void findLinkByTag(List<UUID> filterTag){
        java.sql.Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        UUID resultUUID=null;;

        try {

            Statement statement = null;
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            String filter="";
            for(UUID tag:filterTag){
                filter+="'"+tag.toString()+"',";
            }
            StringBuffer sb= new StringBuffer(filter);
            sb.deleteCharAt(sb.length()-1);
            String query="select distinct tl.link_fk id, l.val link from tag_link tl" +
                    " left join link l on l.id=tl.link_fk where tl.tag_fk in (" +
                    sb+
                    ")";
            System.out.println(query);
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            System.out.println("Founded:");
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String link=resultSet.getString("link");
                System.out.println("id|"+id+"| link|"+link+"");
            }
            sc.next();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static int insertNewLink(UUID uuid, String val) {
        int rowsAffected = 0;

        java.sql.Connection connection = null;
        PreparedStatement preparedStatement = null;


        try {

            Statement statement = null;

            System.out.println("Registering JDBC driver...");

            Class.forName("org.postgresql.Driver");

            System.out.println("Creating database connection...");
            connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);



            String query = " insert into link (id, val)  values ("+"'" + uuid +"', '"+val+"'); ";
            System.out.println(query);
            preparedStatement = connection.prepareStatement(query);


            System.out.println("Executing statement...");
            rowsAffected = preparedStatement.executeUpdate();


        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return rowsAffected;
    }

    public static int insertNewTag(UUID uuid, String val) {
        int rowsAffected = 0;

        java.sql.Connection connection = null;
        PreparedStatement preparedStatement = null;


        try {

            Statement statement = null;
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            String query = " insert into tag (id, val)  values ("+"'" + uuid +"', '"+val+"'); ";
            System.out.println(query);
            preparedStatement = connection.prepareStatement(query);

            rowsAffected = preparedStatement.executeUpdate();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return rowsAffected;
    }

    public static int insertNewTagLink(UUID tag, UUID link) {
        int rowsAffected = 0;

        java.sql.Connection connection = null;
        PreparedStatement preparedStatement = null;


        try {

            Statement statement = null;
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            UUID uuid = UUID.randomUUID();
            String query = " insert into tag_link (id, tag_fk, link_fk) " +
                    " values ('" + uuid +"',"+"'" + tag +"', '"+link+"'); ";
            System.out.println(query);
            preparedStatement = connection.prepareStatement(query);

            rowsAffected = preparedStatement.executeUpdate();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return rowsAffected;
    }


    public static UUID findTagByVal(String val) {
        java.sql.Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        UUID resultUUID=null;;

        try {

            Statement statement = null;
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);

            String query="select id from tag where val='"+val+"';";
            System.out.println(query);
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                resultUUID = UUID.fromString(id);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return  resultUUID;
    }

    static void addTag(UUID link) {

        String val;
        String repeatAdd="n";
        // ArrayList<Student> newInputStudents = new ArrayList<>();
//-----------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------
        boolean value;
//-----------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------
        System.out.println();
        System.out.println("Adding tag ");

        do {
            System.out.println();
            System.out.print("Insert tag ");

            val = sc.next().trim();
            while (val.isEmpty()) {
                System.out.println("tag is empty, retry: ");
                val = sc.nextLine().trim();

            }
            UUID tag= findTagByVal(val);
            int rowsAffected=1;
            if(tag==null){
               tag= UUID.randomUUID();
               rowsAffected = insertNewTag(tag, val);
                if (rowsAffected == 0) {
                    System.out.println("Something went wrong.");
                } else {
                    System.out.println("tag was saved");
                }
            }
            rowsAffected =insertNewTagLink(tag, link);

            if (rowsAffected == 0) {
                System.out.println("Something went wrong.");
            } else {
                System.out.println("tag_link was saved");

                System.out.println();

                System.out.print("\nAdd new tag?   (Y/N): ");
                repeatAdd = sc.next().toUpperCase();
            }
        }while (repeatAdd.equals("Y")) ;
    }

    public static void clearScreen(){
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}