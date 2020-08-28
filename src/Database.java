import java.sql.*;
public class Database {	
	public static Connection getConn() {
		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SafeText","root","");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return con;	
	}
}
