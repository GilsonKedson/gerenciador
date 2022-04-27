package postgresql.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionDB {
	private static final String DRIVER = "org.postgresql.Driver";
	private static final String URL = "jdbc:postgresql://localhost:5432/gerenciador-de-tarefas";
	private static final String USER = "postgres";
	private static final String PASSWORD = "";

	public static Connection getConnection() throws ClassNotFoundException {
		try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL, USER, PASSWORD);        
        } catch (SQLException ex) {
            throw new RuntimeException("Erro na Conexão!", ex);
        }
	}
	
	public static void closeConnection(Connection conn) {
		try {
			if(conn != null){   
				conn.close();
	        }
	    } catch (SQLException ex) {
	    	Logger.getLogger(ConnectionDB.class.getName()).log(Level.SEVERE, null, ex);
	 	}
	}
	
	public static void closeConnection(Connection conn, PreparedStatement stmt) {
		closeConnection(conn);
        try{
            if(stmt != null){
                stmt.close();
            }
        } catch (SQLException ex){
                Logger.getLogger(ConnectionDB.class.getName()).log(Level.SEVERE, null, ex);            
        }
	}
	
	public static void closeConnection(Connection conn, PreparedStatement stmt, ResultSet rs) {
		closeConnection(conn, stmt);
        try{
            if(rs != null){
                rs.close();
            }
        } catch (SQLException ex){
                Logger.getLogger(ConnectionDB.class.getName()).log(Level.SEVERE, null, ex);            
        }

	}
}
