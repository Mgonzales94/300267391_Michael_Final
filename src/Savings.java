public class Savings {

    private String CustomerNumber;
    private String CustomerName;
    private double Deposit;
    private int Years;
    private String Type;

    public Savings (String customerNumber, String customerName,
                    double deposit, int years, String type){

        CustomerName = customerName;
        CustomerNumber = customerNumber;
        Deposit = deposit;
        Years = years;
        Type = type;
    }

    public String getCustomerNumber() {
        return CustomerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        CustomerNumber = customerNumber;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public double getDeposit() {
        return Deposit;
    }

    public void setDeposit(double deposit) {
        Deposit = deposit;
    }

    public int getYears() {
        return Years;
    }

    public void setYears(int years) {
        Years = years;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
