package entities;

import java.util.*;
import java.io.Serializable;

public class Item {
	
	String itemId;
	int quantity=0;
	String itemName=null;
	
	public Item()
	{
		
	}
	
	public Item(String itemId, String itemName, int quantity)
	{
		this.itemId=itemId;
		this.quantity=quantity;
		this.itemName=itemName;
	}
	

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemId() {
		return itemId;
	}
	
	public int getQuantity() {
		return quantity;
	}

	
	public String getItemName() {
		return itemName;
	}

	
}
