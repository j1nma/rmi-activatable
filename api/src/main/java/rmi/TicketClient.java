package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TicketClient extends Remote {
	void ticketReserved(String concert) throws RemoteException;
	
	void ticketVipConfirmed(String concert, String ticket) throws RemoteException;
	
	void ticketConfirmed(String concert, String ticket) throws RemoteException;
	
	void concertSoldOut(String concert) throws RemoteException;
	
	void concertCancelled(String concert) throws RemoteException;
}
