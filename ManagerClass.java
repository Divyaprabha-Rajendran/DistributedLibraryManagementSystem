import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.io.IOException;
import java.io.Serializable;

import entities.Item;
import entities.User;

public class ManagerClass extends UnicastRemoteObject implements ManagerInterfaceCon, ManagerInterfaceMcg, ManagerInterfaceMon
{
	protected ManagerClass() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	public void addItem(String managerId, String itemID, String itemName, int quantity) throws InterruptedException, IOException
	{
		switch(managerId.substring(0, 3).toUpperCase().trim())
		{
		case "CON":
					{
						if (ConcordiaServer.itemList.containsKey(itemID))
						{
							Item curr_book = ConcordiaServer.itemList.get(itemID);
							int curr_quantity=curr_book.getQuantity();
							curr_quantity=curr_quantity+quantity;
							curr_book.setQuantity(curr_quantity);
							System.out.println(curr_book.getItemId()+" is updated to "+curr_book.getQuantity()+" successfully by "+ managerId);
							ConcordiaServer.logger.info(curr_book.getItemId()+" is updated to "+curr_book.getQuantity()+" successfully by "+ managerId);
							if (ConcordiaServer.waitList.size()>0 && ConcordiaServer.waitList.containsKey(itemID))
							{
								for (int i=0;i<quantity;i++)
								{
									if(ConcordiaServer.waitList.get(itemID).size()>0)
									{
										ConcordiaServer.adjustWaitlist(itemID);
									}
								}
							}
			
						}
						else
						{
							System.out.println(itemID+" not found");
							ConcordiaServer.itemList.put(itemID, new Item(itemID, itemName, quantity));
							System.out.println(itemID+" is added to Concordia library with a quantity of "+quantity+" successfully");
						}
						break;
					}
		case "MCG":
			{
				if (McGillServer.itemList.containsKey(itemID))
				{
					Item curr_book = McGillServer.itemList.get(itemID);
					int curr_quantity=curr_book.getQuantity();
					curr_quantity=curr_quantity+quantity;
					curr_book.setQuantity(curr_quantity);
					System.out.println(curr_book.getItemId()+" is updated to "+curr_book.getQuantity()+" successfully by "+ managerId);
					McGillServer.logger.info(curr_book.getItemId()+" is updated to "+curr_book.getQuantity()+" successfully by "+ managerId);
					if (McGillServer.waitList.size()>0 && McGillServer.waitList.containsKey(itemID))
					{
						for (int i=0;i<quantity;i++)
						{
							if(McGillServer.waitList.get(itemID).size()>0)
							{
								McGillServer.adjustWaitlist(itemID);
							}
						}
					}

				}
				else
				{
					System.out.println(itemID+" not found");
					McGillServer.itemList.put(itemID, new Item(itemID, itemName, quantity));
					System.out.println(itemID+" is added to McGill library with a quantity of "+quantity+" successfully");
				}
				break;
			}
		case "MON":
			{
				if (MontrealServer.itemList.containsKey(itemID))
				{
					Item curr_book = MontrealServer.itemList.get(itemID);
					int curr_quantity=curr_book.getQuantity();
					curr_quantity=curr_quantity+quantity;
					curr_book.setQuantity(curr_quantity);
					System.out.println(curr_book.getItemId()+" is updated to "+curr_book.getQuantity()+" successfully by "+ managerId);
					MontrealServer.logger.info(curr_book.getItemId()+" is updated to "+curr_book.getQuantity()+" successfully by "+ managerId);
					if (MontrealServer.waitList.size()>0 && MontrealServer.waitList.containsKey(itemID))
					{
						for (int i=0;i<quantity;i++)
						{
							if(MontrealServer.waitList.get(itemID).size()>0)
							{
								MontrealServer.adjustWaitlist(itemID);
							}
						}
					}

				}
				else
				{
					System.out.println(itemID+" not found");
					MontrealServer.itemList.put(itemID, new Item(itemID, itemName, quantity));
					System.out.println(itemID+" is added to Montreal library with a quantity of "+quantity+" successfully");
				}
				break;
			}
		}
	}
	
	public void removeItem(String managerId, String itemID, int Quantity)
	{
		switch(managerId.substring(0, 3).toUpperCase().trim())
		{
		case "CON": 
				{
			
					if (ConcordiaServer.itemList.containsKey(itemID))
					{
						Item curr_book = ConcordiaServer.itemList.get(itemID);
						int curr_quantity=curr_book.getQuantity();
						if(curr_quantity==Quantity)
						{
							ConcordiaServer.itemList.remove(itemID);
							System.out.println(itemID+" is successfully deleted by "+managerId);
							ConcordiaServer.logger.info(itemID+" is successfully deleted by "+managerId);
							for (Entry<String, User> entry : ConcordiaServer.userList.entrySet())
							{
								String key = entry.getKey().toString();
								User curr_user = entry.getValue();
								if (curr_user.getItems().contains(itemID))
								{
									HashSet<String> temp = curr_user.getItems();
									temp.remove(itemID);
									curr_user.setItems(temp);
								}
							}
				
						}
						else
						{
							curr_quantity=curr_quantity-Quantity;
							curr_book.setQuantity(curr_quantity);
							System.out.println(curr_book.getItemId()+" is reduced to "+curr_book.getQuantity()+" successfully by "+ managerId);
							ConcordiaServer.logger.info(curr_book.getItemId()+" is reduced to "+curr_book.getQuantity()+" successfully by "+ managerId);
						}
					}
					else
					{
						System.out.println("Book not found "+itemID);
						ConcordiaServer.logger.info("Book not found "+itemID);
					}
					break;
				}
		case "MCG":
				{
					if (McGillServer.itemList.containsKey(itemID))
					{
						Item curr_book = McGillServer.itemList.get(itemID);
						int curr_quantity=curr_book.getQuantity();
						if(curr_quantity==Quantity)
						{
							McGillServer.itemList.remove(itemID);
							System.out.println(itemID+" is successfully deleted by "+managerId);
							McGillServer.logger.info(itemID+" is successfully deleted by "+managerId);
							for (Entry<String, User> entry : McGillServer.userList.entrySet())
							{
								String key = entry.getKey().toString();
								User curr_user = entry.getValue();
								if (curr_user.getItems().contains(itemID))
								{
									HashSet<String> temp = curr_user.getItems();
									temp.remove(itemID);
									curr_user.setItems(temp);
								}
							}
				
						}
						else
						{
							curr_quantity=curr_quantity-Quantity;
							curr_book.setQuantity(curr_quantity);
							System.out.println(curr_book.getItemId()+" is reduced to "+curr_book.getQuantity()+" successfully by "+ managerId);
							McGillServer.logger.info(curr_book.getItemId()+" is reduced to "+curr_book.getQuantity()+" successfully by "+ managerId);
						}
					}
					else
					{
						System.out.println("Book not found "+itemID);
						McGillServer.logger.info("Book not found "+itemID);
					}
					break;
				}
		case "MON" :
					{
						if (MontrealServer.itemList.containsKey(itemID.toUpperCase()))
						{
							Item curr_book = MontrealServer.itemList.get(itemID);
							int curr_quantity=curr_book.getQuantity();
							if(curr_quantity==Quantity)
							{
								MontrealServer.itemList.remove(itemID.toUpperCase());
								System.out.println(itemID+" is successfully deleted by "+managerId);
								MontrealServer.logger.info(itemID+" is successfully deleted by "+managerId);
								for (Entry<String, User> entry : MontrealServer.userList.entrySet())
								{
									String key = entry.getKey().toString();
									User curr_user = entry.getValue();
									if (curr_user.getItems().contains(itemID))
									{
										HashSet<String> temp = curr_user.getItems();
										temp.remove(itemID);
										curr_user.setItems(temp);
									}
									
								}
					
							}
							else
							{
								curr_quantity=curr_quantity-Quantity;
								curr_book.setQuantity(curr_quantity);
								System.out.println(curr_book.getItemId()+" is reduced to "+curr_book.getQuantity()+" successfully by "+ managerId);
								MontrealServer.logger.info(curr_book.getItemId()+" is reduced to "+curr_book.getQuantity()+" successfully by "+ managerId);
							}
						}
						else
						{
							System.out.println("Book not found "+itemID);
							MontrealServer.logger.info("Book not found "+itemID);
						}
						break;
					
					}
		}
	}
	
	public LinkedList listItemAvailability(String userId)
	{
		System.out.println("Executing listItemAvailability");
		LinkedList<String> print_list=new LinkedList<String>();
		switch(userId.substring(0, 3).toUpperCase().trim())
		{
		case "CON":
		{
			for (Entry<String, Item> entry : ConcordiaServer.itemList.entrySet())
			{
			String key = entry.getKey().toString();
			Item curr_item = entry.getValue();
			print_list.add(curr_item.getItemId()+"     "+curr_item.getItemName()+"    "+curr_item.getQuantity());
			}
			break;
		}
		case "MCG":
		{
			for (Entry<String, Item> entry : McGillServer.itemList.entrySet())
			{
			String key = entry.getKey().toString();
			Item curr_item = entry.getValue();
			print_list.add(curr_item.getItemId()+"     "+curr_item.getItemName()+"    "+curr_item.getQuantity());
			}
			break;
		}
		case "MON":
		{
			for (Entry<String, Item> entry : MontrealServer.itemList.entrySet())
			{
			String key = entry.getKey().toString();
			Item curr_item = entry.getValue();
			print_list.add(curr_item.getItemId()+"     "+curr_item.getItemName()+"    "+curr_item.getQuantity());
			}
			break;
		}
		}
		//System.out.println(ConcordiaServer.itemList.size());
		return print_list;
	}
	
/*	public static void main(String args[]) throws RemoteException
	{
		ConcordiaServer.InitializeConcordiaServer();
		ManagerClass manager=new ManagerClass();
		manager.addItem("divya","CON1006","Advanced Operating Systems",7);
		manager.addItem("divya","CON1007","Advanced Operating Systems one",9);
		//System.out.println(ConcordiaServer.itemList.get("CON1001").getQuantity());
		//ConcordiaServer.PrintData(ConcordiaServer.itemList);
		manager.listItemAvailability("divya");
		manager.removeItem("divya","CON1006",2);
		manager.listItemAvailability("divya");
		manager.removeItem("divya","CON1007",9);
		manager.listItemAvailability("divya");
	}*/
}
