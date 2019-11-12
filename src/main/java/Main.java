import java.util.ArrayList;
import java.util.List;

public class Main{
    private static final int THREADS_QUANTITY = Runtime.getRuntime().availableProcessors();
    private static Bank bank;
    private static int accountsQuantity;
    private static Object[] accounts;
    private static List<String[]> splittedAccounts;
    private static BankManager bankManager;

    public static void main(String[] args) {
        accountsQuantity = 100_000;
        initializeBank(accountsQuantity);
        accounts = bank.getAccounts().keySet().toArray();
        splittedAccounts = splitAccountsByThreads(accounts);

        for (int threadIndex = 0; threadIndex < THREADS_QUANTITY; threadIndex++){
            bankManager = new BankManager(splittedAccounts.get(threadIndex));
            new Thread(bankManager).start();
        }
    }

    private static void initializeBank(int accountsQuantity){
        bank = new Bank();
        bank.generateAccounts(accountsQuantity);
    }

    private static List<String[]> splitAccountsByThreads(Object[] accounts){
        List<String[]> splittedAccounts = new ArrayList<>();
        int partSize = accountsQuantity / THREADS_QUANTITY;

        for (int threadNumber = 1; threadNumber <= THREADS_QUANTITY; threadNumber++){
            String[] accountsPart;
            int indexOfMainAccArray = threadNumber * partSize - partSize;

            if (threadNumber == THREADS_QUANTITY) {
                partSize = accountsQuantity - ((THREADS_QUANTITY - 1) * partSize);
            }

            accountsPart = new String[partSize];
            for (int i = 0; i < partSize; i++) {
                accountsPart[i] = (String) accounts[indexOfMainAccArray];
                indexOfMainAccArray++;
            }
            splittedAccounts.add(accountsPart);
        }
        return splittedAccounts;
    }


private static class BankManager implements Runnable{
    private String[] accounts;

    BankManager(String[] accounts){
            this.accounts = accounts;
    }

    @Override
    public void run() {
        for (String from : accounts) {
            String to = Integer.toString((int) (Math.random() * accounts.length)); // Случайный номер номер счета из имеющихся
            if (!from.equals(to)) {
                long amount = getRandomAmount(1000, 150_000);
                bank.transfer(from, to, amount);
            }
        }
    }

    private static long getRandomAmount(long min, long max){
        max -= min;
        return (long) (Math.random() * ++max) + min;
    }
}
}


