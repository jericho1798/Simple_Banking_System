package banking;

import java.util.Random;

public class Account {
    private String cardNumber = "400000";;
    private String password = "";
    private long balance;

    public Account() {
        Random random = new Random();
        String a;
        for(int i = 1; i < 10 ; i++){
            a = String.valueOf(random.nextInt(9 + 1));
            this.cardNumber += a;
        }
        this.cardNumber += luhnAlgo(this.cardNumber);
        for(int i = 0; i < 4 ; i++){
            a = String.valueOf(random.nextInt(9 + 1));
            this.password += a;
        }

    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getCardNumber() {
        return cardNumber;
    }

    public String getPassword() {
        return password;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public static String luhnAlgo(String in) {
        String str1 = null;
        int sex = 0;
        int sum = 0;
        for(int i = 0; i < in.length(); i++) {
            str1 =  String.valueOf(in.charAt(i));
            sex = Integer.parseInt(str1);
            if(i % 2 == 0) {
                sex *= 2;
            }
            if(sex > 9){
                sex -= 9;
            }
            sum += sex;
        }
        for(int i = 0; i < 10; i++){
            if((sum + i) % 10 == 0) {
                str1 = String.valueOf(i);
            }
        }
        return str1;
    }
}
