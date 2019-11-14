public class Account
{
    private long money;
    private String accNumber;
    private boolean isBlocked;
    private boolean isOnFraudChecking;

    public Account(String accNumber) {
        this.accNumber = accNumber;
        money = generateMoney();
        isBlocked = false;
        isOnFraudChecking = false;
    }

    public synchronized void blockAccount(){
        if (isOnFraudChecking){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        isBlocked = true;
        System.out.println("Account#" + accNumber + " is blocked!");
    }

    public synchronized void unblockAccount(){
        if (isOnFraudChecking){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        isBlocked = false;
    }

    public long getBalance() {
        return money;
    }

    public synchronized boolean withdraw(long amount) {
        if (!isBlocked) {
            if (isOnFraudChecking){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (amount > 0 && money >= amount) {
                money -= amount;
                System.out.println("Withdrawn amount: " + amount + ". Account #" + accNumber +
                        "\nTime: " + System.currentTimeMillis());
                return true;
            } else {
                System.out.println("Not enough money");
                return false;
            }
        }
        else {
            System.out.println("Account blocked!");
            return false;
        }
    }

    public synchronized void replenish(long amount){
        if (isOnFraudChecking){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (!isBlocked && amount > 0) {
            money += amount;
            System.out.println("Account#" + accNumber + " replenished by " + amount);
        }
        else {
            System.out.println("Account blocked!");
        }
    }

    public String getAccNumber() {
        return accNumber;
    }

    private long generateMoney(){
        long min = 10_000;
        long max = 100_000;

        max -= min;
        return (long) (Math.random() * ++max) + min;
    }

    public synchronized void setOnFraudChecking(boolean onFraudChecking) {
        isOnFraudChecking = onFraudChecking;
        if (!isOnFraudChecking){
            notify();
        }
    }
}
