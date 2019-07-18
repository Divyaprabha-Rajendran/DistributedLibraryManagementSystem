import java.io.IOException;
import java.rmi.Remote; 
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.LinkedList;  

public interface UserInterfaceCon extends Remote{
	
	 public String borrowItem(String userId, String itemId,int noOfDays) throws RemoteException, IOException, InterruptedException;
	 public LinkedList<String> findItem(String userId, String itemName) throws RemoteException;
	 public String returnItem(String userId, String itemId) throws RemoteException, InterruptedException, IOException;
	 public LinkedList<String> returnData(String userId) throws RemoteException;
	 public HashSet printItems(String userId) throws RemoteException;
	 public LinkedList<String> printMessages(String userId) throws RemoteException;
}
