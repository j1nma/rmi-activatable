package rmi.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rmi.CrowdfundingBackerResponseHandler;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.server.UnicastRemoteObject;

public class ClientBacker {
	private static Logger logger = LoggerFactory.getLogger(ClientBacker.class);
	
	public static void main(String[] args) {
		logger.info("Client Backer Starting ...");
		
		final ConcertService concertService = (ConcertService) Naming.lookup("//localhost:1099/ConcertService");
		final TicketService ticketService = (TicketService) Naming.lookup("//localhost:1099/TicketService");
		
		// Instantiate handler
		CrowdfundingBackerResponseHandler clientNotificator = new ClientNotificator();
		
		// Export handler
		final Remote remote = UnicastRemoteObject.exportObject(clientNotificator, 0);
		
		concertService.create("concertABC", 7, 10, 3);
		
		for (int i = 0; i < 12; i++) {
			ticketService.applyForTicket("concertABC", clientNotificator);
		}
	}
}
