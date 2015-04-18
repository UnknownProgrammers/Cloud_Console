package com.bluefrost.sql.light.usermanagement;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import bluefrost.serializable.objects.v1.json.MyJsonUtils;
import bluefrost.serializable.objects.v1.json.MyJsonUtils.JsonObject;

import com.bluefrost.nio.servernclient.useraccess.ClientManager.Client;
import com.bluefrost.sql.light.usermanagement.UserBase.PermissionsList.Permission;

public class CopyOfUserBase {

	private  UserDatabase sqld;
	@Deprecated
	public  UserDatabase getDatabase(){return sqld;}

	@Deprecated 
	public  static CopyOfUserBase setup()throws Exception{
		return new CopyOfUserBase(new UserDatabase(new File("C:\\JavaResources\\sample.db")));
	}
	public static CopyOfUserBase setup(File f) throws Exception{
		return new CopyOfUserBase(new UserDatabase(f));
	}
	private CopyOfUserBase(UserDatabase udb){
		sqld = udb;
	}

	public static class UserDatabase {
		private Statement statement;
		public Statement getStatement(){return statement;}

		private Connection connection;
		public Connection getConnection(){return connection;}

		private void Load(File f) throws Exception{ //Done
			Class.forName("org.sqlite.JDBC");

			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:"+f.getAbsolutePath());
			statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			statement.executeUpdate("create table if not exists Users (username string, password string, displayname string, permissions string)");
			prepareStatements();
		} //Done
		
		public void prepareStatements() throws Exception{ //Done
			getAll = connection.prepareStatement("select * from Users");
			delete = connection.prepareStatement("delete from Users WHERE ? = ?");
			insert = connection.prepareStatement("insert into Users values(?,?,?,?)");
			select1 = connection.prepareStatement("select * from Users WHERE username = ?");
			updatePassword = connection.prepareStatement("UPDATE Users SET password = ? WHERE username = ?");
			updateUsername = connection.prepareStatement("UPDATE Users SET username = ? WHERE username = ?");
			updateDisplayName = connection.prepareStatement("UPDATE Users SET displayname = ? WHERE username = ?");
			updatePermissions = connection.prepareStatement("UPDATE Users SET permissions = ? WHERE username = ?");
		}
		private PreparedStatement getAll;
		private PreparedStatement delete;
		private PreparedStatement insert;
		private PreparedStatement select1;
		private PreparedStatement updatePassword;
		private PreparedStatement updatePermissions;
		private PreparedStatement updateUsername;
		private PreparedStatement updateDisplayName;
		//^Done
		/*Delete User*/public boolean deleteUser(String where, String what) {
			try{
				if(where.length() > 40 || what.length() > 40)return false;
				delete.setString(1, where);
				delete.setString(2, what);
				delete.executeUpdate();
				return true;
			}catch(Exception e){e.printStackTrace();}
			return false;
		}		
		/*Create User*/boolean createUser(String username, String password,String displayname, PermissionsJson list){
			try{
				if(username.length() > 40)return false;
				if(password.length() > 40)return false;
				if(displayname.length() > 40) return false;
				select1.setString(1, username);
				ResultSet rs = select1.executeQuery();
				if(rs.next()){return false;}

				insert.setString(1, username);
				insert.setString(2, password);
				insert.setString(3, username);

				insert.setObject(4, list.toString());
				insert.executeUpdate();
				rs = null;
				return true;
			}catch(Exception e){e.printStackTrace();}
			return false;
		}
		/*Validate User*/public boolean validateUser(String username, String password, Client c){ //Fix
			try{
				if(username.length() > 40)return false;
				if(password.length() > 40)return false;
				//ResultSet rs = sqld.getStatement().executeQuery("select * from Users WHERE username = '"+username+"'");
				select1.setString(1, username);
				ResultSet rs = select1.executeQuery();
				while(rs.next()){
					if(rs.getString("password").equals(password)){
						c.set(
								rs.getString("username"),
								rs.getString("password"),
								rs.getString("displayname"),
								(PermissionsJson) MyJsonUtils.fromString(rs.getString("permissions"))
								);
						return true; 
					}
				}
				rs = null;
			}catch(Exception e){e.printStackTrace();}
			return false;
		}
		/*Get Users*/public List<User> getUsers(){
			List<User> list = new ArrayList<User>();
			try{
				ResultSet rs = getAll.executeQuery();
				while(rs.next()){list.add(
						new User(
								rs.getString("username"),
								rs.getString("password"),
								rs.getString("displayname"),
								MyJsonUtils.fromString(rs.getString("permissions"))
								)
						);
				}
				rs = null;
				return list;
			}catch(Exception e){}
			return null;
		}
		/*Update Password*/public boolean changePassword(String username, String oldPassword, String newPassword){
			try{
				if(username.length() > 40)return false;
				if(newPassword.length() > 40)return false;
				if(oldPassword.length() > 40)return false;
				select1.setString(1, username);
				ResultSet rs = select1.executeQuery();
				while(rs.next()){
					if(rs.getString("username").equals(username)&&rs.getString("password").equals(oldPassword)){
						updatePassword.setString(1, newPassword);
						updatePassword.setString(2, username);
						updatePassword.executeUpdate();
						return true;
					}
				}
			}catch(Exception e){e.printStackTrace();}
			return false;
		}
		/*Update Password*/public boolean changePassword(String username, String newPassword){
			try{
				if(username.length() > 40)return false;
				if(newPassword.length() > 40)return false;
				select1.setString(1, username);
				ResultSet rs = select1.executeQuery();
				while(rs.next()){
					if(rs.getString("username").equals(username)){
						updatePassword.setString(1, newPassword);
						updatePassword.setString(2, username);
						updatePassword.executeUpdate();
						return true;
					}
				}
			}catch(Exception e){e.printStackTrace();}
			return false;
		}
		/*Update Permissions*/public boolean changePermissions(String username, PermissionsJson list){
			try{
				if(username.length() > 40)return false;
				select1.setString(1, username);
				ResultSet rs = select1.executeQuery();
				while(rs.next()){
					if(rs.getString("username").equals(username)){
						updatePermissions.setString(1, list.toString());
						updatePermissions.setString(2, username);
						updatePermissions.executeUpdate();
						return true;
					}
				}
			}catch(Exception e){e.printStackTrace();}
			return false;
		}
		/*Update DisplayName*/public boolean changeDisplayName(String username, String displayname){
			try{
				if(username.length() > 40)return false;
				select1.setString(1, username);
				ResultSet rs = select1.executeQuery();
				while(rs.next()){
					if(rs.getString("username").equals(username)){
						updateDisplayName.setString(1, displayname);
						updateDisplayName.setString(2, username);
						updateDisplayName.executeUpdate();
						return true;
					}
				}
			}catch(Exception e){e.printStackTrace();}
			return false;
		}
		/*Update Username*/public boolean changeUsername(String username, String newUsername){
			try{
				if(newUsername.length() > 40)return false;
				if(username.length() > 40) return false;
				select1.setString(1, newUsername);
				ResultSet rs = select1.executeQuery();
				if(rs.next())return false;
				updateUsername.setString(1, newUsername);
				updateUsername.setString(2, username);
				updateUsername.executeUpdate();
				return true;
			}catch(Exception e){e.printStackTrace();}
			return false;
		}

		//	PreparedStatement ps2 = prepareStatement("delete from Users WHERE username = ?");
		private UserDatabase(File f) throws Exception{
			Load(f); //setup 
		}
		
	}

	public static class PermissionsJson extends JsonObject {

		public boolean hasPermission(Permission perm) {
			// TODO Auto-generated method stub
			return false;
		}

		
	}

	public static class User{	
		public String username;
		public String password;
		public String displayname;
		public PermissionsJson list;

		public User(Object... s){
			username = (String) s[0];
			password = (String) s[1];
			displayname = (String) s[2];
			list = (PermissionsJson)s[3];
		}
	
	}

}