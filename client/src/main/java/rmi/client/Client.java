package rmi.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Client {
	private static Logger logger = LoggerFactory.getLogger(Client.class);
	
	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		logger.info("ex2 Client Starting ...");
		
		final ConcertService concertService = (ConcertService) Naming.lookup("//localhost:1099/ConcertService");
		final TicketService ticketService = (TicketService) Naming.lookup("//localhost:1099/TicketService");
		
		// Instantiate handler
		TicketClient clientNotificator = new ClientNotificator();
		
		// Export handler
		final Remote remote = UnicastRemoteObject.exportObject(clientNotificator, 0);
		
		concertService.create("concertABC", 7, 10, 3);
		
		for (int i = 0; i < 12; i++) {
			ticketService.applyForTicket("concertABC", clientNotificator);
		}
		
		
	}
}
