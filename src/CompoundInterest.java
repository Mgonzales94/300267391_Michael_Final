import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/*
 * Created by JFormDesigner on Fri Aug 13 16:04:54 PDT 2021
 */



/**
 * @author Michael Gonzales
 */
public class CompoundInterest extends JFrame {

    SqlConnection con = new SqlConnection();
    Connection connection= con.Connect();

    public CompoundInterest() {

        initComponents();

        txtDeposit.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String text = ((JTextField) input).getText();
                double num;

                try {
                    num = Double.parseDouble(text);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid number");
                    ((JTextField) input).setText("");
                    return false;
                }
                return true;
            }
        });

       txtYears.setInputVerifier(new InputVerifier() {
           @Override
           public boolean verify(JComponent input) {
               String text = ((JTextField) input).getText();
               int num;

               try {
                   num = Integer.parseInt(text);
               } catch (NumberFormatException e) {
                   JOptionPane.showMessageDialog(null, "Please enter a valid number");
                   ((JTextField) input).setText("");
                   return false;
               }
               return true;
           }
       });

       txtNumber.setInputVerifier(new InputVerifier() {
           @Override
           public boolean verify(JComponent input) {
               String text = ((JTextField) input).getText();
               int num;

               try {
                   num = Integer.parseInt(text);
               } catch (NumberFormatException e) {
                   JOptionPane.showMessageDialog(null, "Please enter a valid number");
                   ((JTextField) input).setText("");
                   return false;
               }
               return true;
           }
       });
    }

    public static void main(String[] args) {

        CompoundInterest form = new CompoundInterest();
        try {
            form.UpdateTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {

            //Pick first client automatically
            form.tableClients.setRowSelectionInterval(0,0);
            DefaultTableModel df = (DefaultTableModel) form.tableClients.getModel();

            int index = form.tableClients.getSelectedRow();

            //Set textfield and combobox texts
            form.txtNumber.setText(df.getValueAt(index,0).toString());
            form.txtName.setText(df.getValueAt(index,1).toString());
            form.txtDeposit.setText(df.getValueAt(index,2).toString());
            form.txtYears.setText(df.getValueAt(index,3).toString());
            form.cboxType.setSelectedItem(df.getValueAt(index,4).toString());

        } catch (Exception e){
            e.printStackTrace();
        }
        form.setVisible(true);
        form.setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    public void UpdateTables() throws SQLException {

        //Clients Table
        String sq = "SELECT * FROM savingstable";
        PreparedStatement query = connection.prepareStatement(sq);
        ResultSet set = query.executeQuery();

        DefaultTableModel df = (DefaultTableModel) tableClients.getModel();

        set.last();
        int i = set.getRow();
        set.beforeFirst();

        String [][] array = new String[0][];
        if(i > 0){
            array = new String[i][5];
        }

        int j =0;

        while(set.next()){
            array[j][0]= set.getString("custno");
            array[j][1] = set.getString("custname");
            array[j][2] = set.getString("cdep");
            array[j][3] = set.getString("nyears");
            array[j][4] = set.getString("savtype");
            ++j;
        }

        String[] cols = {"Number", "Name", "Deposit", "Years", "Type of Savings"};

        DefaultTableModel model = new DefaultTableModel(array, cols);
        tableClients.setModel(model);


    }

    private void btnAddActionPerformed(ActionEvent e) {

        //variables
        String number = txtNumber.getText().toString();
        String name = txtName.getText();
        String type = cboxType.getSelectedItem().toString();
        double deposit = Double.parseDouble(txtDeposit.getText());
        int years = Integer.parseInt(txtYears.getText());

        //if any fields are blank, error
        if (number.equals("") || name.equals("") ||
                txtDeposit.getText().equals("") || txtYears.getText().equals("")){

            JOptionPane.showMessageDialog(null, "Please make sure all fields are filled out");

        } else {

            if (type.equals("Savings - Deluxe")) {

                //create new account
                Deluxe account = new Deluxe(number, name, deposit, years, "Deluxe");

                //add account in database
                try {

                    String sq ="SELECT * FROM savingstable WHERE custno =?";
                    PreparedStatement query = connection.prepareStatement(sq);

                    query.setString(1,number);

                    ResultSet rs = query.executeQuery();

                    //check if customer number already exists
                    if(rs.next()){
                        JOptionPane.showMessageDialog(null, "That Record Already Exists");
                        ClearText();
                        return;
                    }

                    String sq2 = "INSERT INTO savingstable VALUES (?,?,?,?,?)";
                    PreparedStatement insert = connection.prepareStatement(sq2);

                    insert.setString(1,number);
                    insert.setString(2,name);
                    insert.setDouble(3,deposit);
                    insert.setInt(4,years);
                    insert.setString(5,type);

                    insert.executeUpdate();

                    //show record was added, update table, clear fields
                    JOptionPane.showMessageDialog(null, "Record Added");
                    UpdateTables();
                    ClearText();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

            } else {

                Regular account = new Regular(number, name, deposit, years, "Regular");

                try {

                    String sq ="SELECT * FROM savingstable WHERE custno =?";
                    PreparedStatement query = connection.prepareStatement(sq);

                    query.setString(1,number);

                    ResultSet rs = query.executeQuery();

                    if(rs.next()){
                        JOptionPane.showMessageDialog(null, "That Record Already Exists");
                        ClearText();
                        return;
                    }


                    String sq2 = "INSERT INTO savingstable VALUES (?,?,?,?,?)";
                    PreparedStatement insert = connection.prepareStatement(sq2);

                    insert.setString(1,number);
                    insert.setString(2,name);
                    insert.setDouble(3,deposit);
                    insert.setInt(4,years);
                    insert.setString(5,type);

                    insert.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Record Added");
                    UpdateTables();
                    ClearText();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

            }
        }

    }

    private void btnEditActionPerformed(ActionEvent e) {

        try {

            DefaultTableModel df = (DefaultTableModel) tableClients.getModel();
            int index = tableClients.getSelectedRow();

            String number = txtNumber.getText().toString();
            String name = txtName.getText();
            String type = cboxType.getSelectedItem().toString();
            double deposit = Double.parseDouble(txtDeposit.getText());
            int years = Integer.parseInt(txtYears.getText());

            String oldvalue = df.getValueAt(index, 0).toString();

            //update entry based on customer number
            String sq = "UPDATE savingstable SET custno=?, custname=?, cdep=?, nyears=?, savtype=? WHERE custno =?";
            PreparedStatement edit = connection.prepareStatement(sq);

            edit.setString(1,number);
            edit.setString(2,name);
            edit.setDouble(3,deposit);
            edit.setInt(4,years);
            edit.setString(5,type);
            edit.setString(6, oldvalue);

            edit.executeUpdate();

            //show record was edited, update table, clear fields
            JOptionPane.showMessageDialog(null, "Record Edited");
            UpdateTables();
            ClearText();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void btnDeleteActionPerformed(ActionEvent e) {

        //show confirm dialog
        int deleteOption = JOptionPane.showConfirmDialog(null,"Do you really want to delete this record?");

        if (deleteOption==JOptionPane.YES_OPTION) {

            try {
                //delete entry based on customer number
                String number = txtNumber.getText().toString();

                String sq = "DELETE FROM savingstable WHERE custno=?";
                PreparedStatement delete =  connection.prepareStatement(sq);

                delete.setString(1,number);
                delete.executeUpdate();

                //show record was deleted, update table, clear fields
                JOptionPane.showMessageDialog(null, "Record Deleted");
                UpdateTables();
                ClearText();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public void ClearText() {

        //set text fields to empty
        txtNumber.setText("");
        txtName.setText("");
        txtDeposit.setText("");
        txtYears.setText("");

    }

    private void tableClientsMousePressed(MouseEvent e) {

        DefaultTableModel df = (DefaultTableModel) tableClients.getModel();

        int index = tableClients.getSelectedRow();

        //Set textfield and combobox texts
        txtNumber.setText(df.getValueAt(index,0).toString());
        txtName.setText(df.getValueAt(index,1).toString());
        txtDeposit.setText(df.getValueAt(index,2).toString());
        txtYears.setText(df.getValueAt(index,3).toString());
        cboxType.setSelectedItem(df.getValueAt(index,4).toString());

    }
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        lblNum = new JLabel();
        txtNumber = new JTextField();
        lblName = new JLabel();
        txtName = new JTextField();
        lblDeposit = new JLabel();
        txtDeposit = new JTextField();
        lblYears = new JLabel();
        txtYears = new JTextField();
        lblType = new JLabel();
        cboxType = new JComboBox<>();
        scrollPane1 = new JScrollPane();
        tableClients = new JTable();
        scrollPane2 = new JScrollPane();
        tableSavings = new JTable();
        btnAdd = new JButton();
        btnEdit = new JButton();
        btnDelete = new JButton();
        scrollPane3 = new JScrollPane();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]"));

        //---- lblNum ----
        lblNum.setText("Enter the Customer Number");
        contentPane.add(lblNum, "cell 0 0");
        contentPane.add(txtNumber, "cell 1 0");

        //---- lblName ----
        lblName.setText("Enter the Customer Name");
        contentPane.add(lblName, "cell 0 1");
        contentPane.add(txtName, "cell 1 1");

        //---- lblDeposit ----
        lblDeposit.setText("Enter the Initial Deposit");
        contentPane.add(lblDeposit, "cell 0 2");
        contentPane.add(txtDeposit, "cell 1 2");

        //---- lblYears ----
        lblYears.setText("Enter the Number of Years");
        contentPane.add(lblYears, "cell 0 3");
        contentPane.add(txtYears, "cell 1 3");

        //---- lblType ----
        lblType.setText("Choose the Type of Savings");
        contentPane.add(lblType, "cell 0 4");

        //---- cboxType ----
        cboxType.setModel(new DefaultComboBoxModel<>(new String[] {
            "Savings - Deluxe",
            "Savings - Regular"
        }));
        contentPane.add(cboxType, "cell 1 4");

        //======== scrollPane1 ========
        {

            //---- tableClients ----
            tableClients.setMinimumSize(new Dimension(30, 40));
            tableClients.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    tableClientsMousePressed(e);
                }
            });
            scrollPane1.setViewportView(tableClients);
        }
        contentPane.add(scrollPane1, "cell 0 5");

        //======== scrollPane2 ========
        {
            scrollPane2.setViewportView(tableSavings);
        }
        contentPane.add(scrollPane2, "cell 1 5");

        //---- btnAdd ----
        btnAdd.setText("Add");
        btnAdd.addActionListener(e -> btnAddActionPerformed(e));
        contentPane.add(btnAdd, "cell 0 6");

        //---- btnEdit ----
        btnEdit.setText("Edit");
        btnEdit.addActionListener(e -> btnEditActionPerformed(e));
        contentPane.add(btnEdit, "cell 0 6");

        //---- btnDelete ----
        btnDelete.setText("Delete");
        btnDelete.addActionListener(e -> btnDeleteActionPerformed(e));
        contentPane.add(btnDelete, "cell 0 6");
        contentPane.add(scrollPane3, "cell 1 6");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JLabel lblNum;
    private JTextField txtNumber;
    private JLabel lblName;
    private JTextField txtName;
    private JLabel lblDeposit;
    private JTextField txtDeposit;
    private JLabel lblYears;
    private JTextField txtYears;
    private JLabel lblType;
    private JComboBox<String> cboxType;
    private JScrollPane scrollPane1;
    private JTable tableClients;
    private JScrollPane scrollPane2;
    private JTable tableSavings;
    private JButton btnAdd;
    private JButton btnEdit;
    private JButton btnDelete;
    private JScrollPane scrollPane3;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
