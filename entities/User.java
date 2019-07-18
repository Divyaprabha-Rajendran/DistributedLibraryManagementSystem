package entities;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class User {
	
	String userId;
	HashSet<String> items;
	LinkedList<String> messages;
	int ownBooks;
	int borrowBooks;
	String borrow1;
	String borrow2;
	
	private final Logger userLogger;
	
	public User() throws SecurityException, IOException
	{
		userLogger = Logger.getLogger("");
		
		FileHandler file = new FileHandler(System.getProperty("user.dir")+"\\user.log");
		file.setFormatter(new SimpleFormatter());
		file.setLevel(Level.ALL);
		userLogger.addHandler(file);
	}
	
	public LinkedList<String> getMessages() {
		return messages;
	}

	public void setMessages(String message) {
		this.messages.add(message);
	}

	public User(String userId) throws SecurityException, IOException
	{
		this.userId=userId;
		this.ownBooks=0;
		this.borrowBooks=0;
		this.items=new HashSet<String>();
		this.messages=new LinkedList<String>();
		this.borrow1="none";
		this.borrow2="none";
		
		userLogger = Logger.getLogger(userId);
		
		FileHandler file = new FileHandler(System.getProperty("user.dir")+"\\logs\\"+userId+".log");
		file.setFormatter(new SimpleFormatter());
		file.setLevel(Level.ALL);
		userLogger.addHandler(file);
		
		userLogger.info("user "+userId+" created");
	}

	public String getUserId() {
		return userId;
	}

	public Logger getUserLogger() {
		return userLogger;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public HashSet getItems() {
		return items;
	}

	public boolean setItems(String item) {
		return (this.items.add(item));
	}
	
	public void setItems(HashSet items) {
		this.items=items;
	}

	public int getOwnBooks() {
		return ownBooks;
	}

	public void setOwnBooks(int ownBooks) {
		this.ownBooks = this.ownBooks+ownBooks;
	}

	public int getBorrowBooks() {
		return borrowBooks;
	}
	
	public String getBorrow1() {
		return borrow1;
	}

	public void setBorrow1(String borrow1) {
		this.borrow1 = borrow1;
	}

	public String getBorrow2() {
		return borrow2;
	}

	public void setBorrow2(String borrow2) {
		this.borrow2 = borrow2;
	}

	public void setBorrowBooks(int borrowBooks) {
		this.borrowBooks = this.borrowBooks+borrowBooks;
	}

}
