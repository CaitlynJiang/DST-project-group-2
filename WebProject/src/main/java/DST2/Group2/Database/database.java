package DST2.Group2.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class database {
	private static String url="jdbc:postgresql://localhost/biomed";
	private static String username="postgres";
	private static String password="password";

	public static Connection connpostgres() {
		Connection connection = null;
		try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			connection = DriverManager.getConnection(url,username,password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        return connection;
	}
}
