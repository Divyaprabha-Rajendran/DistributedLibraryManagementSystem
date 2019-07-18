import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry; 
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import entities.Item;
import entities.User;  


public class Client {

	
	public static void PrintData(Map<String, User> map)
	{
		for (Entry<String, User> entry :map.entrySet())
		{
			String key = entry.getKey().toString();
			User curr_item = entry.getValue();
			System.out.println(curr_item.getUserId()+"--"+curr_item.getBorrowBooks()+"--"+curr_item.getOwnBooks());
		}
		
	}
	
	public static void userOperationsCon(UserInterfaceCon ConUser,String userId) throws IOException, InterruptedException
	{
		BufferedReader scan = new BufferedReader(new InputStreamReader(System.in));

		String choice="";
		do
		{
			System.out.println("1...Find books accross libraries");
			System.out.println("2...Borrow book");
			System.out.println("3...Return book");
			System.out.println("4...List books in library");
			System.out.println("Enter the option...");
			int input=Integer.parseInt(scan.readLine());
			
			switch(input)
			{
			case 1: 
			{
				System.out.println("Enter the book name to find");
				String itemName=scan.readLine().toUpperCase();
				LinkedList<String> data=ConUser.findItem(userId, itemName);
				DisplayData(data);
				break;
			}
			case 2:
			{
				System.out.println("Enter the book id");
				String itemId=scan.readLine().toUpperCase();
				System.out.println("Enter the number of days");
				int no_days=Integer.parseInt(scan.readLine());
				String result=ConUser.borrowItem(userId, itemId, no_days);
				System.out.println(result);
				TimeUnit.MILLISECONDS.sleep(1000);
				//System.out.println("Control back to client");
				DisplayData(ConUser.printMessages(userId));
				System.out.println("Books you own.."+ConUser.printItems(userId));
				break;
			}
			case 3:
			{
				System.out.println("Enter the book id");
				String itemId=scan.readLine().toUpperCase();
				String result=ConUser.returnItem(userId, itemId);
				System.out.println(result);
				TimeUnit.MILLISECONDS.sleep(1000);
				DisplayData(ConUser.printMessages(userId));
				System.out.println("Books you own.."+ConUser.printItems(userId));
				break;
			}
			case 4:
			{
				LinkedList<String> data_list=ConUser.returnData(userId);
				DisplayData(data_list);
				break;
			}
			default: System.out.println("Invalid choice...");
			}
			System.out.println("Do you want to contionue(y/n)..");
			choice=scan.readLine();
		}while(choice.equalsIgnoreCase("y"));
	}
	
	public static void userOperationsMcg(UserInterfaceMcg McgUser,String userId) throws IOException, InterruptedException
	{
		BufferedReader scan = new BufferedReader(new InputStreamReader(System.in));
		String choice="";
		do
		{
			System.out.println("1...Find books accross libraries");
			System.out.println("2...Borrow book");
			System.out.println("3...Return book");
			System.out.println("4...List books in library");
			System.out.println("Enter the option...");
			int input=Integer.parseInt(scan.readLine());
			
			switch(input)
			{
			case 1: 
			{
				System.out.println("Enter the book name to find");
				String itemName=scan.readLine().toUpperCase();
				LinkedList<String> data=McgUser.findItem(userId, itemName);
				DisplayData(data);
				break;
			}
			case 2:
			{
				System.out.println("Enter the book id");
				String itemId=scan.readLine().toUpperCase();
				System.out.println("Enter the number of days");
				int no_days=Integer.parseInt(scan.readLine());
				String result=McgUser.borrowItem(userId, itemId, no_days);
				System.out.println(result);
				TimeUnit.MILLISECONDS.sleep(1000);
				//System.out.println("Control back to client");
				//PrintData(ConcordiaServer.userList);
				DisplayData(McgUser.printMessages(userId));
				System.out.println("Books you own.."+McgUser.printItems(userId));
				break;
			}
			case 3:
			{
				System.out.println("Enter the book id");
				String itemId=scan.readLine().toUpperCase();
				String result=McgUser.returnItem(userId, itemId);
				System.out.println(result);
				TimeUnit.MILLISECONDS.sleep(1000);
				DisplayData(McgUser.printMessages(userId));
				System.out.println("Books you own.."+McgUser.printItems(userId));
				break;
			}
			case 4:
			{
				LinkedList<String> data_list=McgUser.returnData(userId);
				DisplayData(data_list);
				break;
			}
			default: System.out.println("Invalid choice...");
			}
			System.out.println("Do you want to contionue(y/n)..");
			choice=scan.readLine();
		}while(choice.equalsIgnoreCase("y"));
	}
	
	public static void userOperationsMon(UserInterfaceMon MonUser,String userId) throws IOException, InterruptedException
	{
		BufferedReader scan = new BufferedReader(new InputStreamReader(System.in));
		String choice="";
		do
		{
			System.out.println("1...Find books accross libraries");
			System.out.println("2...Borrow book");
			System.out.println("3...Return book");
			System.out.println("4...List books in library");
			System.out.println("Enter the option...");
			int input=Integer.parseInt(scan.readLine());
			
			switch(input)
			{
			case 1: 
			{
				System.out.println("Enter the book name to find");
				String itemName=scan.readLine().toUpperCase();
				LinkedList<String> data=MonUser.findItem(userId, itemName);
				DisplayData(data);
				break;
			}
			case 2:
			{
				System.out.println("Enter the book id");
				String itemId=scan.readLine().toUpperCase();
				System.out.println("Enter the number of days");
				int no_days=Integer.parseInt(scan.readLine());
				String result=MonUser.borrowItem(userId, itemId, no_days);
				System.out.println(result);
				TimeUnit.MILLISECONDS.sleep(1000);
				//System.out.println("Control back to client");
				//PrintData(ConcordiaServer.userList);
				DisplayData(MonUser.printMessages(userId));
				System.out.println("Books you own.."+MonUser.printItems(userId));
				break;
			}
			case 3:
			{
				System.out.println("Enter the book id");
				String itemId=scan.readLine().toUpperCase();
				String result=MonUser.returnItem(userId, itemId);
				System.out.println(result);
				TimeUnit.MILLISECONDS.sleep(1000);
				DisplayData(MonUser.printMessages(userId));
				System.out.println("Books you own.."+MonUser.printItems(userId));
				break;
			}
			case 4:
			{
				LinkedList<String> data_list=MonUser.returnData(userId);
				DisplayData(data_list);
				break;
			}
			default: System.out.println("Invalid choice...");
			}
			System.out.println("Do you want to contionue(y/n)..");
			choice=scan.readLine();
		}while(choice.equalsIgnoreCase("y"));
	}
	public static void managerOperationsCon(ManagerInterfaceCon ConManager,String userId) throws InterruptedException, IOException
	{
		BufferedReader scan = new BufferedReader(new InputStreamReader(System.in));
		while(true)
		{
			System.out.println("1...Add Books");
			System.out.println("2...Remove books");
			System.out.println("3...List books in library");
			System.out.println("Enter the option...");
			int input=Integer.parseInt(scan.readLine());
			
			switch(input)
			{
			case 1:
			{
				System.out.println("Enter the book id");
				String itemId=scan.readLine();
				System.out.println("Enter the book name");
				String itemName=scan.readLine();
				System.out.println("Enter the quantity");
				int quantity = Integer.parseInt(scan.readLine());
				if (quantity>0)
					ConManager.addItem(userId.toUpperCase(), itemId.toUpperCase(), itemName, quantity);
				else
					System.out.println("Enter valid quantity greater than Zero.");
				break;
			}
			case 2:
			{
				System.out.println("Enter the book id");
				String itemId=scan.readLine();
				System.out.println("Enter the quantity");
				int quantity = Integer.parseInt(scan.readLine());
				if (quantity>0)
					ConManager.removeItem(userId.toUpperCase(), itemId.toUpperCase(), quantity);
				else
					System.out.println("Enter valid quantity greater than Zero.");
				break;
			}
			case 3:
			{
				LinkedList data_list=ConManager.listItemAvailability(userId.toUpperCase());
				DisplayData(data_list);
				break;
			}
			default: System.out.println("Invalid option...");
			}
		}
	}

	public static void managerOperationsMcg(ManagerInterfaceMcg McgManager,String userId) throws InterruptedException, IOException
	{
		BufferedReader scan = new BufferedReader(new InputStreamReader(System.in));
		while(true)
		{
			System.out.println("1...Add Books");
			System.out.println("2...Remove books");
			System.out.println("3...List books in library");
			System.out.println("Enter the option...");
			int input=Integer.parseInt(scan.readLine());
			
			switch(input)
			{
			case 1:
			{
				System.out.println("Enter the book id");
				String itemId=scan.readLine();
				System.out.println("Enter the book name");
				String itemName=scan.readLine();
				System.out.println("Enter the quantity");
				int quantity = Integer.parseInt(scan.readLine());
				if (quantity>0)
					McgManager.addItem(userId.toUpperCase(), itemId.toUpperCase(), itemName, quantity);
				else
					System.out.println("Enter valid quantity greater than Zero.");
				break;
			}
			case 2:
			{
				System.out.println("Enter the book id");
				String itemId=scan.readLine();
				System.out.println("Enter the quantity");
				int quantity = Integer.parseInt(scan.readLine());
				if (quantity>0)
					McgManager.removeItem(userId.toUpperCase(), itemId.toUpperCase(), quantity);
				else
					System.out.println("Enter valid quantity greater than Zero.");
				break;
			}
			case 3:
			{
				LinkedList data_list=McgManager.listItemAvailability(userId.toUpperCase());
				DisplayData(data_list);
				break;
			}
			default: System.out.println("Invalid option...");
			}
		}
	}

	public static void managerOperationsMon(ManagerInterfaceMon MonManager,String userId) throws InterruptedException, IOException
	{
		BufferedReader scan = new BufferedReader(new InputStreamReader(System.in));
		while(true)
		{
			System.out.println("1...Add Books");
			System.out.println("2...Remove books");
			System.out.println("3...List books in library");
			System.out.println("Enter the option...");
			int input=Integer.parseInt(scan.readLine());
			
			switch(input)
			{
			case 1:
			{
				System.out.println("Enter the book id");
				String itemId=scan.readLine();
				System.out.println("Enter the book name");
				String itemName=scan.readLine();
				System.out.println("Enter the quantity");
				int quantity = Integer.parseInt(scan.readLine());
				if (quantity>0)
					MonManager.addItem(userId.toUpperCase(), itemId.toUpperCase(), itemName, quantity);
				else
					System.out.println("Enter valid quantity greater than Zero.");
				break;
			}
			case 2:
			{
				System.out.println("Enter the book id");
				String itemId=scan.readLine();
				System.out.println("Enter the quantity");
				int quantity = Integer.parseInt(scan.readLine());
				if (quantity>0)
					MonManager.removeItem(userId.toUpperCase(), itemId.toUpperCase(), quantity);
				else
					System.out.println("Enter valid quantity greater than Zero.");
				break;
			}
			case 3:
			{
				LinkedList data_list=MonManager.listItemAvailability(userId.toUpperCase());
				DisplayData(data_list);
				break;
			}
			default: System.out.println("Invalid option...");
			}
		}
	}

	
	public static void DisplayData(LinkedList<String> list)
	{
		for (String str : list)
		{
			System.out.println(str);
		}
	}
	
	public static void main(String args[])
	{
		
		try
		{
			Registry registry = LocateRegistry.getRegistry(null);
	
			System.out.println("Client Ready...");
			System.out.println(ConcordiaServer.userList.size());
			Scanner scan=new Scanner(System.in);
			System.out.println("Please enter the UserID or ManagerId");
			String id=scan.nextLine();
			String id_pos = id.charAt(3)+"".toUpperCase();
			String identification = id.substring(0, 3).toUpperCase();
			
			if (id_pos.equalsIgnoreCase("U"))
			{
			switch(identification)
			{
			case "CON":
				{
					UserInterfaceCon ConUser = (UserInterfaceCon) Naming.lookup("UserInterfaceCon");
					userOperationsCon(ConUser, id);
				}
			case "MCG" :
				{
					UserInterfaceMcg McgUser = (UserInterfaceMcg) Naming.lookup("UserInterfaceMcg");
					userOperationsMcg(McgUser, id);
				}
			case "MON" :
				{
					UserInterfaceMon MonUser = (UserInterfaceMon) Naming.lookup("UserInterfaceMon");
					userOperationsMon(MonUser, id);
				}
			default:
				{
					System.out.println("Unidentified User");
				}
			}
			}
			else if (id_pos.equalsIgnoreCase("M"))
			{
				switch(identification)
				{
				case "CON":
					{
						ManagerInterfaceCon ConManager = (ManagerInterfaceCon) Naming.lookup("ManagerInterfaceCon");
						managerOperationsCon(ConManager, id);
					}
				case "MCG" :
					{
						ManagerInterfaceMcg McgManager = (ManagerInterfaceMcg) Naming.lookup("ManagerInterfaceMcg");
						managerOperationsMcg(McgManager, id);
					}
				case "MON" :
					{
						ManagerInterfaceMon MonManager = (ManagerInterfaceMon) Naming.lookup("ManagerInterfaceMon");
						managerOperationsMon(MonManager, id);
					}
				default:
					{
						System.out.println("Unidentified User");
					}
				}	
			}
			
			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
}
