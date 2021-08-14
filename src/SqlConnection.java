import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlConnection {

    public java.sql.Connection Connect(){

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        java.sql.Connection con1 = null;

        try {
            con1 = DriverManager.getConnection("jdbc:mysql://localhost/savings", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con1;

    }

}
