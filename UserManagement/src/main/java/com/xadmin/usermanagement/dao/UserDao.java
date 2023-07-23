package com.xadmin.usermanagement.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.xadmin.usermanagement.bean.User;

public class UserDao {
	
	private String jdbcURL="jdbc:mysql://localhost:3306/userdb?useSSL=false";
	private String jdbcUsername="root";
	private String jdbcPassword ="Soham@2023";
	private String jdbcDriver="com.mysql.cj.jdbc.Driver";
	
	public static final String INSERT_USERS_SQL="INSERT INTO users"+" (name,email,country) VALUES "+"(?,?,?);";
	public static final String SELECT_USER_BY_ID="select id,name,email, country from users where id=?";
	public static final String SELECT_ALL_USERS="select * from users";
	public static final String DELETE_USERS_SQL="delete from users where id=?;";
	public static final String UPDATE_USERS_SQL="update users set name= ?,email= ?, country=? where id=?;";
	
	public UserDao() {
		
	}
	protected Connection getConnection() {
		 Connection connection = null;  
         try {  
             Class.forName(jdbcDriver);  
            // Class.forName("com.mysql.jdbc.Driver");
            // DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
             connection = DriverManager.getConnection(jdbcURL,jdbcUsername,jdbcPassword);  
         }catch(SQLException e) {
        	 e.printStackTrace();
         }
         catch(ClassNotFoundException e) {
        	e.printStackTrace();
         }
         return connection;
	}
	//insert user method
	public void insertUser(User user) {
		System.out.println(INSERT_USERS_SQL);
		try(Connection connection=getConnection()){
		PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL);
		preparedStatement.setString (1, user.getName());
		preparedStatement.setString (2, user.getEmail());
		preparedStatement.setString(3, user.getCountry());
		System.out.println(preparedStatement);
		preparedStatement.executeUpdate();

		}catch(SQLException e) {
			printSQLException(e);
		}
	}
	
	
	//select method by id
	public User selectUser(int id) {
		User user=null;
		try(Connection connection=getConnection()){
			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);
			preparedStatement.setInt (1,id);
			System.out.println(preparedStatement);
			ResultSet rs=preparedStatement.executeQuery();
			 
			while(rs.next()) {
				String name=rs.getString("name");
				String email=rs.getString("email");
				String country=rs.getString("country");
				user =new User(id,email,name,country);
			}
		}catch(SQLException e) {
			printSQLException(e);
		
	}
		return user;
	}
	
	//select all users
	public List<User> selectAllUsers(){
		List<User> users=new ArrayList<>();  
        
        try(Connection connection=getConnection()){  
            
            PreparedStatement preparedStatement=connection.prepareStatement(SELECT_ALL_USERS);  
            System.out.println(preparedStatement);
			ResultSet rs=preparedStatement.executeQuery();
            while(rs.next()){  
                int id=rs.getInt("id");
                String name=rs.getString("name");
				String email=rs.getString("email");
				String country=rs.getString("country");
				users.add(new User(id,email,name,country));
            }  
        }catch(SQLException e) {
			printSQLException(e);
		
	}
		return users;
	}
	//update user method
	public boolean updateUser(User user)throws SQLException {
		boolean rowUpdated;
	
		try(Connection connection=getConnection()){
			PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);
			System.out.println("update user"+statement);
			statement.setString (1, user.getName());
			statement.setString (2, user.getEmail());
			statement.setString(3, user.getCountry());
			statement.setInt(4, user.getId());
			
			rowUpdated=statement.executeUpdate()>0;
		
	}
		return rowUpdated;
	}
	
	//delete user
	public boolean deleteUser(int id) throws SQLException{
		
		boolean rowDeleted;
		try(Connection connection=getConnection()){
			PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL);
			statement.setInt(1, id);
			rowDeleted =statement.executeUpdate()>0;
		}
		return rowDeleted;
	}
	
	
	
	private void printSQLException(SQLException ex) {
		// TODO Auto-generated method stub
		for (Throwable e : ex) {
	        if (e instanceof SQLException) {
	            

	                e.printStackTrace(System.err);
	                System.err.println("SQLState: " +
	                    ((SQLException)e).getSQLState());

	                System.err.println("Error Code: " +
	                    ((SQLException)e).getErrorCode());

	                System.err.println("Message: " + e.getMessage());

	                Throwable t = ex.getCause();
	                while(t != null) {
	                    System.out.println("Cause: " + t);
	                    t = t.getCause();
	                }
	            }
	        }
	}
}
