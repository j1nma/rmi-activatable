package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TicketService extends Remote {
	void applyForTicket(String concertName, TicketClient handler) throws RemoteException;
}
