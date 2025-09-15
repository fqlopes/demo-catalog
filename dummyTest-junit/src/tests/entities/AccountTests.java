package tests.entities;

import entities.Account;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tests.factory.AccountFactory;


public class AccountTests {

    @Test
    public void depositShouldIncreaseBalanceWhenPositiveAmount (){

        double amount = 200.0;
        double expectedValue = 196.0;
        Account acc = AccountFactory.createEmptyAccount();

        acc.deposit(amount);

        Assertions.assertEquals(expectedValue, acc.getBalance());
    }

    @Test
    public void depositShouldDoNothingWhenDepositeIsNegative(){

        double expectedValue = 100.0;
        Account acc = AccountFactory.createEmptyAccount(expectedValue);
        double amount = -200.0;

        acc.deposit(amount);

        Assertions.assertEquals(expectedValue, acc.getBalance());
    }

    @Test
    public void fullWithdrawShouldClearBalance (){

        double expectedValue = 0.0;
        double initialBalance = 123.45;
        Account acc = AccountFactory.createEmptyAccount(initialBalance);

        double result = acc.fullWithdraw();

//        Assertions.assertEquals(expectedValue, acc.getBalance());
        Assertions.assertTrue(expectedValue == acc.getBalance());
        Assertions.assertTrue(result == initialBalance);
    }


    @Test
    public void withdrawShouldDecreaseBalanceWhenSufficientBalance(){
        Account acc = AccountFactory.createEmptyAccount(800.0);

        acc.withdraw(500);

        Assertions.assertEquals(300, acc.getBalance());
    }

    @Test
    public void withdrawShouldThrowExceptionInsufficientBalance(){


        Assertions.assertThrows(IllegalArgumentException.class, () -> {

            Account acc = AccountFactory.createEmptyAccount(800.0);
            acc.withdraw(1000.0);

        } );

    }
}
