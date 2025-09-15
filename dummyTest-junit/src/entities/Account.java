package entities;


public class Account {

    public static final double DEPOSIT_FEE_PERCENTAGE = 0.02;

    private Long id;
    private Double balance;

    public Account() {
    }

    public Account(Long id, Double balance) {
        this.id = id;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getBalance() {
        return balance;
    }

    public void deposit(double amount){
        if (amount > 0) {
            amount = amount - (amount * DEPOSIT_FEE_PERCENTAGE);
            balance += amount;
        } else {
            System.out.println("Quantia inválida. Um depósito precisa maior que zero!");
        }

    }

    public void withdraw(double amount){
        if (amount > this.balance) {
            throw new IllegalArgumentException();

        }
        this.balance -= amount;
    }

    public double fullWithdraw(){

        double money = balance;
        balance = 0.0;
        return money;
    }
}
