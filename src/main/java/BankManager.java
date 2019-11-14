public class BankManager implements Runnable{
    private String[] accounts;
    private Bank bank;

    BankManager(String[] accounts, Bank bank) {
        this.accounts = accounts;
        this.bank = bank;
    }

    @Override
    public void run() {
        for (String from : accounts) {
            String to = Integer.toString((int) (Math.random() * accounts.length)); // Случайный номер номер счета из имеющихся
            if (!from.equals(to)) {
                int minAmount = 1000;
                int maxAmount = 150_000;
                long amount = getRandomAmount(minAmount, maxAmount);
                bank.transfer(from, to, amount);
            }
        }
    }

    private long getRandomAmount(int min, int max) {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }

    public void runTestFraud() {
        TestFraud testFraud = new TestFraud("1", "2", bank);
        new Thread(testFraud).start();
        TestChangeBalance testChangeBalance = new TestChangeBalance("1", "2", bank);
        new Thread(testChangeBalance).start();
    }

    //Следующие два класса запускают метод isFraud для одного счета и из другого потока пытаются изменить баланс этого же счета.
    private static class TestFraud implements Runnable {
        Bank bank;
        String fromAccNumber;
        String toAccNumber;

        TestFraud(String fromAccNumber, String toAccNumber, Bank bank){
            this.fromAccNumber = fromAccNumber;
            this.toAccNumber = toAccNumber;
            this.bank = bank;
        }

        @Override
        public void run(){
            try {
                long currentTime = System.currentTimeMillis();
                System.out.println("isFraud вызван для счета №" + fromAccNumber + "! Время: " + currentTime +
                        ". Проверка закончится в: " + (currentTime + 1000));
                bank.getAccounts().get(fromAccNumber).replenish(100_000);
                bank.isFraud(fromAccNumber, toAccNumber, 60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class TestChangeBalance implements Runnable {
        Bank bank;
        String fromAccNumber;
        String toAccNumber;

        TestChangeBalance(String fromAccNumber, String toAccNumber, Bank bank){
            this.fromAccNumber = fromAccNumber;
            this.toAccNumber = toAccNumber;
            this.bank = bank;
        }

        @Override
        public void run(){
            int amount = 30000;
            System.out.println("Пытаюсь получить поступ к счету №" + fromAccNumber + " в состоянии isFraud. " + "Время: " + System.currentTimeMillis());
            bank.getAccounts().get(fromAccNumber).withdraw(amount);
        }
    }
}
