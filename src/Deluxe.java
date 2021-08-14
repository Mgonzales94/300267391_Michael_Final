import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Deluxe extends Savings implements Compound_Interest{

    private double InterestRate;

    public Deluxe(String customerNumber, String customerName, double deposit, int years, String type) {
        super(customerNumber, customerName, deposit, years, type);


        setInterestRate(0.15);
    }

    public double getInterestRate() {
        return InterestRate;
    }

    public void setInterestRate(double interestRate) {
        InterestRate = interestRate;
    }

    @Override
    public void generateTable() {

    }
}
