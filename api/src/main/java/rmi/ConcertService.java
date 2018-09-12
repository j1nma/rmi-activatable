package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ConcertService extends Remote {
	void create(String name, int confirmationTickets, int maxTickets, int vipTickets) throws RemoteException;
	
	void cancel(String name) throws RemoteException;
}