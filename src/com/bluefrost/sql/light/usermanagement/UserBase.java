package com.bluefrost.sql.light.usermanagement;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserBase {

	private  UserDatabase sqld;
	@Deprecated
	public  UserDatabase getDatabase(){return sqld;}

	@Deprecated
	public  static UserBase setup()throws Exception{
		File f = new File("C:\\Java\\sample.db");
		UserBase b = new UserBase();
		b.sqld = new UserDatabase(f);
		return b;
	}
	public static UserBase setup(File f) throws Exception{
		UserBase b = new UserBase();
		b.sqld = new UserDatabase(f);
		return b;
	}

	@Deprecated
	public  boolean createUser(String username, String password){
		return createUser(username, password, username, new PermissionsList());
	}
	public  boolean createUser(String username, String password,String displayname, PermissionsList list){
		synchronized(sqld){
			return sqld.createUser(username, password, displayname, list);
		}
	}
	public  boolean delete(String where, String what){
		synchronized(sqld){
			return sqld.deleteUser(where, what);
		}
	}
	public  User validateUser(String username, String password){
		synchronized(sqld){
			return sqld.validateUser(username, password);
		}
	}
	public  List<User> getUsers(){
		synchronized(sqld){
			return sqld.getUsers();
		}
	}
	public  boolean updatePassword(String username, String oldPassword, String newPassword){
		synchronized(sqld){
			return sqld.changePassword(username, oldPassword, newPassword);
		}
	}
	public  boolean updatePassword(String username, String newPassword){
		synchronized(sqld){
			return sqld.changePassword(username, newPassword);
		}
	}
	public  boolean updateDisplayName(String username, String displayname){
		synchronized(sqld){
			return sqld.changeDisplayName(username, displayname);
		}
	}
	public  boolean updateUsername(String username, String newUsername){
		synchronized(sqld){
			return sqld.changeUsername(username, newUsername);
		}
	}
	public  boolean updatePermissionsList(String username, PermissionsList list){
		synchronized(sqld){
			return sqld.changePermissions(username, list);
		}
	}

	public static class UserDatabase {

		private Statement statement;
		public Statement getStatement(){return statement;}

		private Connection connection;
		public Connection getConnection(){return connection;}

		private void Load(File f) throws Exception{
			Class.forName("org.sqlite.JDBC");

			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:"+f.getAbsolutePath());
			statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			statement.executeUpdate("create table if not exists Users (username string, password string, displayname string, permissions byte[])");
			prepareStatements();
		}
		public void prepareStatements() throws Exception{
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
		/*Create User*/boolean createUser(String username, String password,String displayname, PermissionsList list){
			try{
				if(username.length() > 40)return false;
				if(password.length() > 40)return false;
				if(displayname.length() > 40) return false;
				if(list.permsList.size() > 40) return false;
				select1.setString(1, username);
				ResultSet rs = select1.executeQuery();
				if(rs.next()){return false;}

				insert.setString(1, username);
				insert.setString(2, password);
				insert.setString(3, username);

				insert.setObject(4, list.toByteArray());
				insert.executeUpdate();
				rs = null;
				return true;
			}catch(Exception e){e.printStackTrace();}
			return false;
		}
		/*Validate User*/public User validateUser(String username, String password){
			try{
				if(username.length() > 40)return null;
				if(password.length() > 40)return null;
				//ResultSet rs = sqld.getStatement().executeQuery("select * from Users WHERE username = '"+username+"'");
				select1.setString(1, username);
				ResultSet rs = select1.executeQuery();
				while(rs.next()){
					if(rs.getString("password").equals(password)){
						return new User(
								rs.getString("username"),
								rs.getString("password"),
								rs.getString("displayname"),
								PermissionsList.fromByteArray(rs.getBytes("permissions"))
								);
					}
				}
				rs = null;
			}catch(Exception e){e.printStackTrace();}
			return null;
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
								PermissionsList.fromByteArray(rs.getBytes("permissions"))
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
		/*Update Permissions*/public boolean changePermissions(String username, PermissionsList list){
			try{
				if(username.length() > 40)return false;
				select1.setString(1, username);
				ResultSet rs = select1.executeQuery();
				while(rs.next()){
					if(rs.getString("username").equals(username)){
						updatePermissions.setBytes(1, list.toByteArray());
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

	public static class PermissionsList implements Serializable{
		private static final long serialVersionUID = 1L;
		public List<Permission> permsList = new ArrayList<Permission>();

		public boolean hasPermission(Permission p){
			if(permsList.contains(p))return true;
			if(p.equals(Permission.magical))return true;
			return false;
		}
		
		public static enum Permission{
			Console_, //All Console
			Console_Read,
			Console_Write,
			Master_Permission,
			Chat_, //All Chat
			Chat_Read,
			Chat_Write, 
			Bukkit_, //all
			Bukkit_Kick, //kick
			Bukkit_Ban, //ban
			Bukkit_Sudo,
			magical, //WARNING, DON'T USE THIS. THIS IS A PERMISSION THAT EVERYONE WILL HAVE
			/*magical is for internal ChatMessages, aswell as some other features which will be default to every user.*/
		}


		public static PermissionsList fromByteArray(byte[] array){
			try{
				ByteArrayInputStream bis = new ByteArrayInputStream(array);
				ObjectInput in = null;
				in = new ObjectInputStream(bis);
				Object o = in.readObject(); 
				try{
					bis.close();
					in.close();
				}catch(Exception e){System.out.println("A Memory Leak Has Happened!");e.printStackTrace();}
				return (PermissionsList)o;
			}catch(Exception e){}
			return null;
		}

		public byte[] toByteArray(){
			try{
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutput out = null;
				out = new ObjectOutputStream(bos);   
				out.writeObject(this);
				byte[] yourBytes = bos.toByteArray();
				try{
					bos.close();
					out.close();
				}catch(Exception e){System.out.println("A Memory Leak Has Happened!");e.printStackTrace();}
				return yourBytes;
			}catch(Exception e){
				return null;
			}
		}
	}

	public static class User{
		public String username;
		public String password;
		public String displayname;
		public PermissionsList list;

		public User(Object... s){
			username = (String) s[0];
			password = (String) s[1];
			displayname = (String) s[2];
			list = (PermissionsList)s[3];
		}
	}

}
