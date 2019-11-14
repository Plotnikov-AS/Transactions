import java.util.HashMap;
import java.util.Random;

public class Bank{
    private HashMap<String, Account> accounts = new HashMap<>();
    private final Random random = new Random();
    private boolean transferSuccess;
    private final int AMOUNT_FRAUD_THRESHOLD = 50_000;

    public synchronized boolean isFraud(String fromAccountNum, String toAccountNum, long amount)
        throws InterruptedException
    {
        accounts.get(fromAccountNum).setOnFraudChecking(true);
        accounts.get(toAccountNum).setOnFraudChecking(true);

        Thread.sleep(1000);

        accounts.get(fromAccountNum).setOnFraudChecking(false);
        accounts.get(toAccountNum).setOnFraudChecking(false);

        return random.nextBoolean();
    }

    /**
     * TODO: реализовать метод. Метод переводит деньги между счетами.
     * Если сумма транзакции > 50000, то после совершения транзакции,
     * она отправляется на проверку Службе Безопасности – вызывается
     * метод isFraud. Если возвращается true, то делается блокировка
     * счетов (как – на ваше усмотрение)
     */
    public void transfer(String fromAccountNum, String toAccountNum, long amount)
    {
        transferSuccess = false;
        Account from = accounts.get(fromAccountNum);
        Account to = accounts.get(toAccountNum);

        if (from.withdraw(amount)) {
            to.replenish(amount);
            transferSuccess = true;
        }
        if (amount > AMOUNT_FRAUD_THRESHOLD && transferSuccess){
            try {
                if(isFraud(fromAccountNum, toAccountNum, amount)){
                    from.blockAccount();
                    to.blockAccount();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * TODO: реализовать метод. Возвращает остаток на счёте.
     */
    public long getBalance(String accountNum)
    {
        return accounts.get(accountNum).getBalance();
    }

    public HashMap<String, Account> getAccounts() {
        return accounts;
    }

    public void generateAccounts(int quantity){
        for (int i = 0; i < quantity; i++){
            Account account = new Account(Integer.toString(i));
            accounts.put(account.getAccNumber(), account);
        }
    }
}
