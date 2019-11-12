public class Account
{
    private long money;
    private String accNumber;
    private boolean isBlocked;

    public Account(String accNumber) {
        this.accNumber = accNumber;
        money = generateMoney();
        isBlocked = false;
    }

    public void blockAccount(){
        isBlocked = true;
        System.out.println("Account#" + accNumber + " is blocked!");
    }

    public void unblockAccount(){
        isBlocked = false;
    }

    public long getBalance() {
        return money;
    }

    public boolean withdraw(long amount) {
        if (!isBlocked) {
            if (amount > 0 && money >= amount) {
                money -= amount;
                System.out.println("Withdrawn amount: " + amount + ". Account #" + accNumber);
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

    public void replenish(long amount){
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
}
