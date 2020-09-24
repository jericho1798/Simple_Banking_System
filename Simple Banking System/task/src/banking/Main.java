package banking;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Main {

    public final static Scanner in = new Scanner(System.in);
    public static int state = -1;
    public static String url;
    public static String acc = null;
    public static SQLiteDataSource dataSource = new SQLiteDataSource();

    public static void createNewDataBase(String filename) {
        url = "jdbc:sqlite:" + filename;
        dataSource.setUrl(url);
    }

    public static void createNewTable() {
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
               // System.out.println("Table created!");
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS card(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "number TEXT," +
                        "pin TEXT," +
                        "balance INTEGER DEFAULT 0)");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void Insert(String num, String pin) {
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                statement.executeUpdate("INSERT INTO card (number, pin)" +
                        "VALUES" +
                        "('" + num + "','" + pin + "')");

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void main(String[] args) {
        createNewDataBase(args[1]);
        createNewTable();
        while(state != 0) {
            switch (state) {
                case -1:
                    state = greetings();
                    break;
                case 1:
                    createAccount();
                    state = -1;
                    break;
                case 2:
                    logIntoAccount();
                    break;
                case 3:
                    accountMenu(acc);
                    break;
                default: {
                    break;
                }
            }
        }
        System.out.println("Bye!");
    }

    public static int greetings() {
        int res = -1;
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");
        if(in.hasNextInt()){
            res = in.nextInt();
        } else {
            System.out.println("Not a number!");
            res = 0;
        }
        return res;
    }
    public static void createAccount() {
        Account account = new Account();
        long a;
        account.setBalance(0);
        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        a = Long.parseLong(account.getCardNumber());
        System.out.println(a);
        System.out.println("Your card PIN:");
        System.out.println(account.getPassword());
        Insert(account.getCardNumber(), account.getPassword());

    }
    public static void logIntoAccount() {
        System.out.println("Enter your card number:");
        String cardNumber = in.next();
        System.out.println("Enter your card PIN:");
        String cardPin = in.next();
       int res = checkAccount(cardNumber, cardPin);
        if(res == 1) {
            System.out.println("You have successfully logged in!");
            acc = cardNumber;
            state = 3;
        } else {
            System.out.println("Wrong card number or PIN!");
            state = -1;
        }
    }
    public static int checkAccount(String number, String PIN) {
        int res = 0;
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) as count FROM card " +
                        "WHERE number = '" + number +"' AND pin = '" + PIN +"'")) {
                    while (resultSet.next()) {
                        res = resultSet.getInt("count");
                    }
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return res;
    }
    public static void accountMenu(String cardNumber) {
        int am = -1;
        while (am != 10) {
            switch (am) {
                case -1:
                    am = accountInfo();
                    break;
                case 1:
                    System.out.println("Balance: " + getBalance(cardNumber));
                    am = -1;
                    break;
                case 2:
                    System.out.println("Enter income:");
                    int mon = in.nextInt();
                    Income(cardNumber, mon);
                    System.out.println("Income was added!");
                    am = -1;
                    break;
                case 3:
                    System.out.println("Transfer");
                    System.out.println("Enter card number:");
                    String cNUm = in.next();
                    if(cNUm.equals(cardNumber)) {
                        System.out.println("You can't transfer money to the same account!");
                        am = -1;
                    } else {
                        Transfer(cNUm);
                    }
                    break;
                case 4:
                    CloseAccount(cardNumber);
                    state = -1;
                    am = 10;
                    break;
                case 5:
                    System.out.println("You have successfully logged out");
                    state = -1;
                    am = 10;
                    break;
                case 0:
                    state = 0;
                    am = 10;
                    break;
                default: break;
            }
        }
    }

    public static int accountInfo() {
        int res = 3;
        System.out.println("1. Balance");
        System.out.println("2. Add income");
        System.out.println("3. Do transfer");
        System.out.println("4. Close account");
        System.out.println("5. Logout");
        System.out.println("0. Exit");
        if(in.hasNextInt()){
            res = in.nextInt();
        } else {
            System.out.println("Not a number!");
        }
        return res;
    }

    public static int getBalance (String cardNUmber) {
        int result = 0;
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery(String.format("SELECT balance FROM card WHERE number = '%s", cardNUmber))) {
                    while (resultSet.next()) {
                        result = resultSet.getInt("count");
                    }
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    public static void Income(String cardNumber, int money) {
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                statement.executeUpdate(String.format("UPDATE card SET balance = balance + '%d' WHERE number = '%s'", money, cardNumber));

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void CloseAccount(String cardNumber) {
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                statement.executeUpdate(String.format("DELETE FROM card WHERE number = '%s'",  cardNumber));
                System.out.println("The account has been closed!!");

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void Transfer(String cardNumber) {
        String cn = "";
        for(int i = 0; i < cardNumber.length() - 1; i++) {
            cn += cardNumber.charAt(i);
        }
        if(checkExist(cardNumber) == 0) {
            System.out.println("Such card does not exist.");
        } else {
            if (String.valueOf(cardNumber.length() - 1).equals(Account.luhnAlgo(cn))) {
                System.out.println("Enter how much you want to transfer:");
                int tr = in.nextInt();
                if (getBalance(cardNumber) < tr) {
                    System.out.println("Not enough money!");
                } else {
                    Income(cardNumber, tr);
                    System.out.println("Success!");
                }
            } else {
                System.out.println("Probably you made mistake in the card number. Please try again!");
            }
        }
    }

    public static int checkExist(String number) {
        int res = 0;
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) as count FROM card " +
                        "WHERE number = '" + number)) {
                    while (resultSet.next()) {
                        res = resultSet.getInt("count");
                    }
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return res;
    }
}