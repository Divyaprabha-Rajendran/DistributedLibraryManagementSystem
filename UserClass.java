import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Map.Entry;

import entities.User;
import entities.Item;

public class UserClass  extends UnicastRemoteObject implements UserInterfaceCon, UserInterfaceMcg, UserInterfaceMon
{
	protected UserClass() throws RemoteException {
		super();
	}

	public String borrowItem(String userId,String itemId,int noOfDays) throws IOException, InterruptedException
	{
		String result="hmmmmmm";
		String id_str = userId.substring(0, 3).toUpperCase();
		switch(id_str)
		{
		case "CON" : {
					 result = ConcordiaServer.lendBooks(userId.toUpperCase(), itemId.toUpperCase(), noOfDays);
					 System.out.println("request forwarded to concordia");
					 break;}
		case "MCG" : {
					  result = McGillServer.lendBooks(userId.toUpperCase(), itemId.toUpperCase(),noOfDays);
		              System.out.println("request forwarded to concordia");
					  break;}
		case "MON" : {
					  result =MontrealServer.lendBooks(userId.toUpperCase(), itemId.toUpperCase(),noOfDays);
					  System.out.println("request forwarded to concordia"); 
					  break;}
		default     : System.out.println("Unknown user trying to access the system");
		}
		System.out.println("control back to userclass");
		System.out.println(result);
		return result;
	}
	
	public LinkedList findItem(String userId,String itemName)
	{
		LinkedList<String> all_data=new LinkedList<String>();
		try {
			String identified_user = userId.substring(0, 3);
			switch(identified_user.toUpperCase())
			{
			case "CON" : all_data=ConcordiaServer.FindItem(itemName);
						 break;
			case "MCG" : all_data=McGillServer.FindItem(itemName);
						 break;
			case "MON" : all_data = MontrealServer.FindItem(itemName);
						 break;
			default     : all_data.add("Unknown user trying to access the system");
			}
			
		} 
		catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return all_data;
	}
	
	public String returnItem(String userId,String itemId) throws InterruptedException, IOException
	{
		String result="";
		if (userId.substring(0, 3).toUpperCase().equals("CON"))
		{
			result=ConcordiaServer.returnBooks(userId.toUpperCase(), itemId.toUpperCase());
		}
		else if (userId.substring(0, 3).toUpperCase().equals("MON"))
		{
			result=MontrealServer.returnBooks(userId.toUpperCase(), itemId.toUpperCase());
		}
		else if (userId.substring(0, 3).toUpperCase().equals("MCG"))
		{
			result=McGillServer.returnBooks(userId, itemId);
		}
		System.out.println(result);
		return result;
	}
	
	public LinkedList<String> returnData(String userId)
	{
		LinkedList<String> data_list = new LinkedList<String>();
		if(userId.substring(0,3).equalsIgnoreCase("CON"))
		{
		for (Entry<String, Item> entry : ConcordiaServer.itemList.entrySet())
			{
			String key = entry.getKey().toString();
			Item curr_item = entry.getValue();
			data_list.add(curr_item.getItemId()+"--"+curr_item.getItemName()+"--"+curr_item.getQuantity());
			}
		}
		else if(userId.substring(0,3).equalsIgnoreCase("MCG"))
		{
			for (Entry<String, Item> entry : McGillServer.itemList.entrySet())
			{
				String key = entry.getKey().toString();
				Item curr_item = entry.getValue();
				data_list.add(curr_item.getItemId()+"--"+curr_item.getItemName()+"--"+curr_item.getQuantity());
			}	
		}
		else if(userId.substring(0,3).equalsIgnoreCase("MON"))
		{
			for (Entry<String, Item> entry : MontrealServer.itemList.entrySet())
			{
				String key = entry.getKey().toString();
				Item curr_item = entry.getValue();
				data_list.add(curr_item.getItemId()+"--"+curr_item.getItemName()+"--"+curr_item.getQuantity());
			}
		}
		return data_list;
	}
	
	public HashSet printItems(String userId)
	{
		System.out.println("printing items");
		HashSet<String> items=new HashSet<String>();
		if (userId.substring(0, 3).toUpperCase().equals("CON"))
		{
			 items=ConcordiaServer.userList.get(userId.toUpperCase()).getItems();
		}
		else if (userId.substring(0, 3).toUpperCase().equals("MCG"))
		{
			items=McGillServer.userList.get(userId.toUpperCase()).getItems();
		}
		else if (userId.substring(0, 3).toUpperCase().equals("MON"))
		{
			items=MontrealServer.userList.get(userId.toUpperCase()).getItems();
		}
		return items;
	}
	
	public LinkedList<String> printMessages(String userId)
	{
		System.out.println("printing messages");
		LinkedList<String> messages = new LinkedList<String>();
		if (userId.substring(0, 3).toUpperCase().equals("CON"))
		{
			messages=ConcordiaServer.userList.get(userId.toUpperCase()).getMessages();
			//ConcordiaServer.userList.get(userId.toUpperCase()).getMessages().clear();
		}
		else if (userId.substring(0, 3).toUpperCase().equals("MCG"))
		{
			messages=McGillServer.userList.get(userId.toUpperCase()).getMessages();
			//McGillServer.userList.get(userId.toUpperCase()).getMessages().clear();
		}
		else if (userId.substring(0, 3).toUpperCase().equals("MON"))
		{
			messages=MontrealServer.userList.get(userId.toUpperCase()).getMessages();
			//MontrealServer.userList.get(userId.toUpperCase()).getMessages().clear();
		}
		return messages;
	}
	
	public static void main(String args[]) throws RemoteException
	{
		//UserClass user = new UserClass();
		//user.borrowItem("CONU100c1");
	}
	
	
}
