import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Map;

public interface ManagerInterfaceMcg extends Remote {
	
	public void addItem(String userId, String itemID, String itemName, int quantity) throws RemoteException, InterruptedException, IOException;
	public void removeItem(String userId, String itemID, int Quantity) throws RemoteException;
	public LinkedList listItemAvailability(String userId) throws RemoteException;

}
