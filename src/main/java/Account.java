public class Account
{
    private long money;
    private String accNumber;
    private boolean isBlocked;

    public Account(String accNumber) {
        this.accNumber = accNumber;
        isBlocked = false;
    }

    public void blockAccount(){
        isBlocked = true;
    }

    public void unblockAccount(){
        isBlocked = false;
    }

    public long getBalance() {
        return money;
    }

    public void withdraw(long amount) {
        if (!isBlocked) {
            if (money >= amount) {
                money -= amount;
            } else {
                System.out.println("Not enough money");
            }
        }
        else {
            System.out.println("Account blocked!");
        }
    }

    public void replenish(long amount){
        if (!isBlocked) {
            money += amount;
        }
        else {
            System.out.println("Account blocked!");
        }
    }

    public String getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }
}
