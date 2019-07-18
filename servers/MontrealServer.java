import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry;
import java.rmi.Naming;
import java.rmi.RemoteException; 
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import entities.User;
import entities.Item;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;

import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;



public class MontrealServer {
	
	final static int ConcordiaPort = 6789;
	final static int McGillPort = 6788;
	final static int MontrealPort = 6787;

	public static Map<String, Item> itemList = new ConcurrentHashMap<String, Item>();
	public static Map<String, User> userList = new ConcurrentHashMap<String, User>();
	
	public static LinkedList<String> managerList = new LinkedList<String>();
	public static Map<String, LinkedList<String>> waitList = new HashMap<String, LinkedList<String>>();
	
	public static DatagramSocket commSocket = null;
	
	public static LinkedList<String> allData = new LinkedList<String>();
	
	public final static Logger logger = Logger.getLogger("montreal");
	
	public static void InitializeMcGillServer() throws SecurityException, IOException
	{
		itemList.put("MON1001", new Item("MON1001","Operating Systems",2));
		itemList.put("MON1002", new Item("MON1002", "Algorithms", 7));
		itemList.put("MON1003", new Item("MON1003","Data Structures", 9));
		itemList.put("MON1004", new Item("MON1004","Database",3));
		itemList.put("MON1005", new Item("MON1005","Web development",10));
		
		userList.put("MONU1011", new User("MONU1011"));
		userList.put("MONU1012", new User("MONU1012"));
		userList.put("MONU1013", new User("MONU1013"));
		userList.put("MONU1014", new User("MONU1014"));
		userList.put("MONU1015", new User("MONU1015"));
		
		commSocket = new DatagramSocket(MontrealPort);
		
		FileHandler file = new FileHandler(System.getProperty("user.dir")+"\\logs\\"+"montreal.log");
		file.setFormatter(new SimpleFormatter());
		file.setLevel(Level.ALL);
		logger.addHandler(file);
	}
	
	//******************************************************************************************************************************
	//******************************************************************************************************************************
	
	public static String FindLocalItem(String itemName)
	{
		String result="none";
		for (Entry<String, Item> entry : itemList.entrySet())
		{
			String key = entry.getKey().toString();
			Item curr_item = entry.getValue();
			System.out.println(itemName);
			System.out.println(curr_item.getItemName());
			if (curr_item.getItemName().trim().replace(" ", "").equalsIgnoreCase(itemName.trim().replace(" ", "")))
			{
				result =(curr_item.getItemId()+"--"+curr_item.getItemName()+"--"+curr_item.getQuantity());
				System.out.println(result);
			}
		}
		return result;
	}
	
	public static LinkedList<String> FindItem( String itemName) throws IOException, InterruptedException
	{
		String curr_request = "";
		LinkedList<String> allDataLocal = new LinkedList<String>();
		
		String result=FindLocalItem(itemName);
		CompileItem(result);
		
		sendRequest(McGillPort, itemName);
		TimeUnit.MILLISECONDS.sleep(500);
		sendRequest(MontrealPort, itemName);
		TimeUnit.MILLISECONDS.sleep(500);
		
		allDataLocal=(LinkedList<String>) allData.clone();
		
		System.out.println(allDataLocal.size());
		
		allData.removeAll(allDataLocal);
		
		return allDataLocal;
	}
	
	public static void CompileItem(String result)
	{
		if (result.contains("--"))
		{
			allData.add(result);
		}
		else if (result=="none")
		{
			allData.add("No results found in Montreal");
		}
		else if(result.contains(":"))
		{
			int port = Integer.parseInt(result.split(":")[1]);
			if (port==McGillPort)
				allData.add("No results found in McGill");
			else
				allData.add("No data found in Concordia");
		}

	}
	
	//******************************************************************************************************************************
	//******************************************************************************************************************************
	
	public static void PrintData()
	{
		for (Entry<String, Item> entry : itemList.entrySet())
		{
			String key = entry.getKey().toString();
			Item curr_item = entry.getValue();
			System.out.println(curr_item.getItemId()+"--"+curr_item.getItemName()+"--"+curr_item.getQuantity());
		}
	}
	
	public static void PrintUser(Map<String, User> map)
	{
		for (Entry<String, User> entry :map.entrySet())
		{
			String key = entry.getKey().toString();
			User curr_item = entry.getValue();
			System.out.println(curr_item.getUserId()+"--"+curr_item.getBorrowBooks()+"--"+curr_item.getOwnBooks());
		}
		
	}
	
	//******************************************************************************************************************************
	//******************************************************************************************************************************
	
	public static String lendBooks(String userId, String itemId, int no_days) throws IOException
	{

		System.out.println("Montreal Received request");
		String returnString="not assigned";
		if (userList.get(userId)==null)
		{
			User curr_user=new User(userId.toUpperCase());
			userList.put(userId, curr_user);
		}
		if(itemList.get(itemId.toUpperCase())!=null)
		{
			User user = userList.get(userId);
			if(itemList.get(itemId).getQuantity()>0)
			{
				if (user.setItems(itemId))
				{
					synchronized(ConcordiaServer.class)
					{
						int quantity=itemList.get(itemId).getQuantity();
						itemList.get(itemId).setQuantity(quantity-1);
					}
				user.setItems(itemId);
				user.setOwnBooks(1);
				logger.info("borrow_success:"+userId+","+itemId);
				user.getUserLogger().info("borrow_success:"+userId+","+itemId);
				returnString="borrow_success:"+userId+","+itemId;
				}
				else
					returnString="borrow_failure:already_borrowed,"+userId+","+itemId;
			}
			else
			{
				addToWaitlist(itemId, userId);
				logger.info("borrow_failure:added_to_waitlist,"+userId+","+itemId);
				user.getUserLogger().info("borrow_failure:added_to_waitlist,"+userId+","+itemId);
				returnString="borrow_failure:added_to_waitlist,"+userId+","+itemId;
				user.setMessages(returnString);
				//System.out.println("operation is failure..waitlist");
			}
			user.setMessages(returnString);
		}
		else if (itemId.substring(0,3).equalsIgnoreCase("CON") || itemId.substring(0,3).equalsIgnoreCase("MCG") )
		{
			System.out.println("borrow from other library");
			User user = userList.get(userId);
			if (user.getBorrowBooks()<2)
			{
				if (itemId.substring(0, 3).equalsIgnoreCase("CON") && !(user.getBorrow2().equals("concordia")))
				{
					System.out.println("concordia");
					logger.info("The request is sent to Concordia University..You will be notified soon!");
					user.getUserLogger().info("The request is sent to Concordia University..You will be notified soon!");
					returnString="The request is sent to Concordia University..You will be notified soon!";
					sendRequest(ConcordiaPort, userId+","+itemId+","+no_days);
				}
				else if(itemId.substring(0, 3).equalsIgnoreCase("MCG") && !(user.getBorrow2().equals("mcgill")))
				{
					System.out.println("mcgill");
					logger.info("The request is sent to McGill University..You will be notified soon!");
					user.getUserLogger().info("The request is sent to McGill University..You will be notified soon!");
					returnString="The request is sent to McGill University..You will be notified soon!";
					sendRequest(McGillPort, userId+","+itemId+","+no_days);				
				}
			}
			else
			{
				logger.info("borrow_failure:loan_books_reached_2,"+userId+","+itemId);
				user.getUserLogger().info("borrow_failure:loan_books_reached_2,"+userId+","+itemId);
				returnString="borrow_failure:loan_books_reached_2,"+userId+","+itemId;
			}
		}
		else
		{
			logger.info("borrow_failure:not_currently_available,"+userId+","+itemId);
			returnString="borrow_failure:not_currently_available,"+userId+","+itemId;
		}
	System.out.println(returnString);
	
	//PrintUser(userList);
	//System.out.println("************************************************************************");
	PrintData();
	System.out.println("************************************************************************");
	
	return returnString;
	
	}
	
	public static void addToWaitlist(String itemId,String userId)
	{
		synchronized(ConcordiaServer.class)
		{
		if(!(waitList.containsKey(itemId)))
		{
			waitList.put(itemId, new LinkedList());
		}
		
		if(!(waitList.get(itemId).contains(userId)))
			waitList.get(itemId).add(userId);
		else
		{
			logger.info("Already added to waitlist "+userId+" for "+itemId);
			System.out.println("Already added to waitlist "+userId+" for "+itemId);
		}
		}
		System.out.println(waitList);
	}
	
	//******************************************************************************************************************************
	//******************************************************************************************************************************
	
	public static String returnBooks(String userId, String itemId) throws IOException, InterruptedException
	{
		System.out.println("Montreal server in return book");
		String result="";
		if (userList.containsKey(userId.toUpperCase()) && itemList.containsKey(itemId.toUpperCase()))
		{
			User curr_user = userList.get(userId);
			curr_user.getItems().remove(itemId);
			curr_user.setOwnBooks(-1);
			
			synchronized(ConcordiaServer.class)
			{
			Item item = itemList.get(itemId);
			int quantity=item.getQuantity()+1;
			item.setQuantity(quantity);
			}
			adjustWaitlist(itemId);
			logger.info("return_success:"+userId+","+itemId);
			curr_user.getUserLogger().info("return_success:"+userId+","+itemId);
			result="return_success:"+userId+","+itemId;
		}	
		else if (itemId.substring(0,3).equalsIgnoreCase("CON"))
		{
			logger.info("Return request forwarded to Concordia");
			result="Return request forwarded to Concordia";
			sendRequest(ConcordiaPort, userId+","+itemId);
		}
		else if (itemId.substring(0,3).equalsIgnoreCase("MCG"))
		{
			logger.info("Return request forwarded to McGill");
			result="Return request forwarded to McGill";
			sendRequest(McGillPort, userId+","+itemId);
		}
		else
		{
			logger.info("return_failure:user_or_book_not_found"+userId+","+itemId);
			result="return_failure:user_or_book_not_found"+userId+","+itemId;
		}
		//PrintUser(userList);
		//System.out.println("************************************************************************");
		PrintData();
		System.out.println("************************************************************************");
		
		return result;
	
	}
	
	public static String adjustWaitlist(String itemId) throws  InterruptedException, IOException
	{
		System.out.println("Adjusting Waitlist..");
		logger.info("Adjusting Waitlist..");
		String result="";
		if(waitList.size()>0 && waitList.containsKey(itemId))
		{
		LinkedList<String> wait_list = waitList.get(itemId);
		if(wait_list.size()>0)
		{
		String userId=wait_list.get(0);
		User user = userList.get(userId);
		if(userId.contains("MON") || user.getItems().size()==0)
		{
		logger.info(result);
		user.getUserLogger().info(result);
		result=lendBooks(userId, itemId, 5);
		if (result.contains("borrow_success"))
		{
			synchronized(ConcordiaServer.class)
			{	
				(waitList.get(itemId)).remove(userId);
			}
		if(userId.toUpperCase().contains("MON"))
			user.setMessages(result);
		else if (userId.toUpperCase().contains("MCG"))
			sendRequest(McGillPort, result);
		else if (userId.toUpperCase().contains("CON"))
			sendRequest(ConcordiaPort, result);
		}
		result=result+" waitlist adjusted for user "+userId;
		}
		else
		{
			logger.info("Already borrowed book from this library "+userId);
			user.getUserLogger().info("Already borrowed book from this library "+userId);
			System.out.println("Already borrowed book from this library "+userId);
		}
		}
		}
		else
			result="no waitlist available";
		System.out.println(result);
		return result;
	}
	
	//******************************************************************************************************************************
	//******************************************************************************************************************************
	
	public static void processBorrowReply(String message)
	{
		if (message.contains("success"))
		{
		String info=message.split(":")[1];
		String userId=info.split(",")[0];
		String itemId=info.split(",")[1];
		
		User user = userList.get(userId);
		user.setItems(itemId);
		user.setMessages(message);
		user.setBorrowBooks(1);
		
		if(itemId.toUpperCase().contains("CON"))
			user.setBorrow1("concordia");
		else
			user.setBorrow2("mcgill");
		
		System.out.println("borrow1 "+user.getBorrow1());
		System.out.println("borrow2 "+user.getBorrow2());
		
		System.out.println("processed "+message);
		logger.info("processed "+message);
		user.getUserLogger().info("processed "+message);
		}
		else if (message.contains("failure"))
		{
			String info=message.split(":")[1];
			String userId=info.split(",")[1];
			
			User user = userList.get(userId);
			user.setMessages(message);
						
			System.out.println("processed "+message);
			logger.info("processed "+message);
			user.getUserLogger().info("processed "+message);
		}
	}
	
	
	public static void processReturnReply(String message)
	{
		if (message.contains("success"))
		{
			String info=message.split(":")[1];
			String userId=info.split(",")[0];
			String itemId=info.split(",")[1];
			
			User user = userList.get(userId);
			HashSet<String> temp=user.getItems();
			temp.remove(itemId);
			user.setItems(temp);
			
			user.setMessages(message);
			user.setBorrowBooks(-1);
			
			if (itemId.contains("CON"))
				user.setBorrow1("none");
			else if (itemId.contains("MCG"))
				user.setBorrow2("none");

			System.out.println("borrow1 "+user.getBorrow1());
			System.out.println("borrow2 "+user.getBorrow2());
			
			logger.info("processed "+message);
			user.getUserLogger().info("processed "+message);
		}
		else
		{
			String info=message.split(":")[1];
			String userId=info.split(",")[1];
			
			User user = userList.get(userId);
			user.setMessages(message);
			
			user.setMessages(message);
			
			logger.info("processed "+message);
			user.getUserLogger().info("processed "+message);
		}
	}
	
	/*public static LinkedList PrintData()
	{
		for (Entry<String, Item> entry : itemList.entrySet())
		{
			String key = entry.getKey().toString();
			Item curr_item = entry.getValue();
			String curr_val=curr_item.getItemId()+"     "+curr_item.getItemName()+"    "+curr_item.getQuantity();
			System.out.println(curr_item.getItemId()+"     "+curr_item.getItemName()+"    "+curr_item.getQuantity());
			allData.add(curr_val);
			//sendRequest(port, curr_val );
		}
		sendRequest(ConcordiaPort, "printdata");
		sendRequest(McGillPort, "printdata");
		return allData;
	}*/
	
	public static void sendRequest(int port, String request)
	{
	
		try {
			byte[] message = new byte[1000];
			InetAddress Host = InetAddress.getByName("localhost");
			message=request.getBytes();
			System.out.println(request);
			DatagramPacket request_packet = new DatagramPacket(message, message.length,Host,port);
			commSocket.send(request_packet);
			System.out.println("Request SENT...."+request);
			System.out.println("Request sent successfully to "+port);
			System.out.println("**********************************************************************");
			logger.info("Request SENT...."+request);
			logger.info("Request sent successfully to "+port);
			logger.info("**********************************************************************");
		}
		catch(SocketException e){
			System.out.println("Socket: "+e.getMessage());
		}
		catch(IOException e){
			e.printStackTrace();
			System.out.println("IO: "+e.getMessage());
		}
		finally{
			//if(commSocket != null) commSocket.close();
		}
	}
	
	public static void sendData(int port)
	{
		for (Entry<String, Item> entry : itemList.entrySet())
		{
			String key = entry.getKey().toString();
			Item curr_item = entry.getValue();
			String curr_val=curr_item.getItemId()+"     "+curr_item.getItemName()+"    "+curr_item.getQuantity();
			System.out.println(curr_item.getItemId()+"     "+curr_item.getItemName()+"    "+curr_item.getQuantity());
			sendRequest(port, curr_val );
		}
		sendRequest(port, "Done");
	}
	
	//******************************************************************************************************************************
	//******************************************************************************************************************************
	
	public static void receiveRequest() throws InterruptedException
	{
		String curr_request="";
		try {
		
			byte[] message = new byte[1000];
			DatagramPacket request = new DatagramPacket(message, message.length);
			System.out.println("Receive Request created");
			logger.info("Receive Request created");
			commSocket.receive(request);
			System.out.println("Sender port  :"+request.getPort());
			curr_request=new String(request.getData()).trim();
			System.out.println("Received request...."+curr_request);
			logger.info("Received request...."+curr_request);
			if (curr_request.contains(",") && (!curr_request.contains("success")) && (!curr_request.contains("failure")))
			{
				
				if(curr_request.split(",").length==2)
				{
					String result="";
					String userId=curr_request.split(",")[0];
					String itemId=curr_request.split(",")[1];
					result=returnBooks(userId, itemId);
					sendRequest(request.getPort(), result);
					PrintData();
				}
				if(curr_request.split(",").length==3)
				{
					String result="";
					String userId=curr_request.split(",")[0];
					String itemId=curr_request.split(",")[1];
					int no_days=Integer.parseInt(curr_request.split(",")[2]);
					result=lendBooks(userId, itemId, no_days);
					System.out.println(result);
					sendRequest(request.getPort(), result);
					//PrintData();
				}
			}
			else if(curr_request.contains("borrow_success")|| curr_request.contains("borrow_failure") )
			{
				processBorrowReply(curr_request);
			}
			else if(curr_request.contains("return_success:")|| curr_request.contains("return_failure:"))
			{
				processReturnReply(curr_request);
				//processReply(curr_request);
			}
			else if(curr_request.equals("none"))
			{
				System.out.println(curr_request+":"+request.getPort());
				String add_data=curr_request+":"+request.getPort();
				CompileItem(add_data);
			}
			else if(curr_request.contains("--"))
			{
				System.out.println(curr_request);
				CompileItem(curr_request);
			}
			else
			{
				System.out.println(curr_request);
				String result=FindLocalItem(curr_request);
				System.out.println(result);
				sendRequest(request.getPort(), result);
			}
		}
		catch(SocketException e){
			System.out.println("Socket: "+e.getMessage());
		}
		catch(IOException e){
			e.printStackTrace();
			System.out.println("IO: "+e.getMessage());
		}
		finally{
			//if(commSocket != null) commSocket.close();
		}
		System.out.println("***********************************************************");
		logger.info("***********************************************************");
	}
	
	//******************************************************************************************************************************
	//******************************************************************************************************************************
	//******************************************************************************************************************************
	//******************************************************************************************************************************
	
	public static void main(String args[])
	{
		try
		{
			UserClass MonUser = new UserClass();
			ManagerClass MonManager = new ManagerClass();
			
			Registry library_registry = LocateRegistry.getRegistry();
			
			library_registry.bind("UserInterfaceMon", MonUser);
			library_registry.bind("ManagerInterfaceMon", MonManager);
			System.err.println("Server ready");
			logger.info("Server ready");
			InitializeMcGillServer();
			logger.info("Server initialized...");
			
			while(true){
				System.out.println("Receiving thread running...");
				System.out.println("Server waiting for request...");
				receiveRequest();
				System.out.println("Receiving thread resuming...");
		 }
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
}
