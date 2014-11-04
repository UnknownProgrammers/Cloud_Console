package com.bluefrost.sql.light.usermanagement;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserManager {

	private static SqlDatabase sqld;
	@Deprecated
	public static SqlDatabase getDatabase(){return sqld;}

	public static void setup(File f)throws Exception{
		sqld = new SqlDatabase(f);
		
		sqld.getStatement().executeUpdate("create table if not exists Users (username string, password string, displayname string)");
	}



	public synchronized static boolean changePassword(String username, String oldPassword, String newPassword){
		try{
			if(username.length() > 40)return false;
			if(newPassword.length() > 40)return false;
			if(oldPassword.length() > 40)return false;
			PreparedStatement ps1 = sqld.getConnection().prepareStatement("select * from Users WHERE username = ?");
			ps1.setString(1, username);
			ResultSet rs = ps1.executeQuery();
			while(rs.next()){
				if(rs.getString("username").equals(username)&&rs.getString("password").equals(oldPassword)){
					PreparedStatement ps = prepareStatement("UPDATE Users SET password = ? WHERE username = ?");
					ps.setString(1, newPassword);
					ps.setString(2, username);
					ps.executeUpdate();
					return true;
				}
			}
		}catch(Exception e){e.printStackTrace();}
		return false;
	}


	public synchronized static boolean deleteUser(String username){
		try{
			if(username.length() > 40)return false;
			PreparedStatement ps2 = prepareStatement("delete from Users WHERE username = ?");
			ps2.setString(1, username);
			ps2.executeUpdate();
			ps2 = null;
			return true;
		}catch(Exception e){e.printStackTrace();}
		return false;
	}



	public synchronized static boolean createUser(String username, String password){
		try{
			if(username.length() > 40)return false;
			if(password.length() > 40)return false;
			PreparedStatement ps1 = sqld.getConnection().prepareStatement("select * from Users WHERE username = ?");
			ps1.setString(1, username);
			ResultSet rs = ps1.executeQuery();
			if(rs.next()){return false;}
			PreparedStatement ps = sqld.getConnection().prepareStatement("insert into Users values(?,?,?)");
			ps.setString(1, username);
			ps.setString(2, password);
			ps.setString(3, username);
			ps.executeUpdate();
			ps = null;
			ps1 = null;
			rs = null;
			return true;
		}catch(Exception e){e.printStackTrace();}
		return false;
	}

	public synchronized static boolean validateUser(String username, String password){
		try{
			if(username.length() > 40)return false;
			if(password.length() > 40)return false;
			//ResultSet rs = sqld.getStatement().executeQuery("select * from Users WHERE username = '"+username+"'");
			PreparedStatement ps = sqld.getConnection().prepareStatement("select * from Users WHERE username = ?");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				if(rs.getString("password").equals(password)){
					return true;
				}
			}
			ps = null;
			rs = null;
		}catch(Exception e){e.printStackTrace();}
		return false;
	}

	public static synchronized List<User> getUsers(){
		List<User> list = new ArrayList<User>();
		try{
			PreparedStatement ps = prepareStatement("select * from Users");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){list.add(new User(rs.getString("username"),rs.getString("password"),rs.getString("displayname")));}
			ps = null;
			rs = null;
			return list;
		}catch(Exception e){}
		return null;
	}



	private static class SqlDatabase {

		private Statement statement;
		public Statement getStatement(){return statement;}

		private Connection connection;
		public Connection getConnection(){return connection;}

		private void Load(File f) throws Exception{
			Class.forName("org.sqlite.JDBC");

			connection = null;

			f = new File("C:\\Users\\Sky\\Coding\\Java\\sample.db");
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:"+f.getAbsolutePath());
			statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
		}

		public SqlDatabase(File f) throws Exception{
			Load(f);
		}
	}


	private static PreparedStatement prepareStatement(String s) throws SQLException{
		return sqld.connection.prepareStatement(s);
	}


	public static class User{
		String username;
		String password;
		String displayname;

		public User(String... s){
			username = s[0];
			password = s[1];
			displayname = s[2];
		}
	}
	
	
	
}
