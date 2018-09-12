package rmi.server;

import rmi.ConcertService;
import rmi.TicketClient;
import rmi.TicketService;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ConcertServant implements ConcertService, TicketService {
	
	private Map<Concert, List<TicketClient>> concerts;
	
	public ConcertServant(Map<Concert, List<TicketClient>> concerts) {
		this.concerts = concerts;
	}
	
	@Override
	public void create(String concertName, int confirmationTickets, int maxTickets, int vipTickets) {
		this.concerts.put(new Concert(concertName, confirmationTickets, maxTickets, vipTickets), new LinkedList<>());
	}
	
	@Override
	public void cancel(String concertName) {
		
		Optional<Concert> optionalConcert = concerts.keySet().stream().filter(concert -> concert.getName().equals(concertName)).findFirst();
		
		optionalConcert.ifPresent(concert -> {
			
			concert.cancel();
			
			List<TicketClient> handlers = concerts.get(concert);
			
			// Notify all clients concert is cancelled
			for (int i = 0; i < concert.getCurrentTickets(); i++) {
				try {
					handlers.get(i).concertCancelled(concertName);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
		
		
	}
	
	@Override
	public synchronized void applyForTicket(String concertName, TicketClient handler) {
		
		Optional<Concert> optionalConcert = concerts.keySet().stream().filter(concert -> concert.getName().equals(concertName)).findFirst();
		
		optionalConcert.ifPresent(concert -> {
			
			switch (concert.getState()) {
				case SOLD_OUT:
					try {
						handler.concertSoldOut(concertName);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					break;
				case CANCELLED:
					try {
						handler.concertCancelled(concertName);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					break;
				case CONFIRMED:
					
					if (concert.getCurrentTickets() < concert.getMaxTickets()) {
						
						concert.addTicket();
						
						// Add client handler
						concerts.get(concert).add(handler);
						
						// Notify client ticket was confirmed
						try {
							handler.ticketConfirmed(concertName, "confirmID");
						} catch (RemoteException e) {
							e.printStackTrace();
						}
						
						if (concert.getCurrentTickets().equals(concert.getMaxTickets())) {
							concert.setState(ConcertState.SOLD_OUT);
						}
					}
					
					break;
				case TO_CONFIRM:
					
					// Account for VIP ticket if possible
					if (concert.getCurrentVipTickets() < concert.getVipTickets()) {
						concert.addVipTicket();
					}
					
					// Add to count of all tickets
					concert.addTicket();
					
					// Add client handler
					concerts.get(concert).add(handler);
					
					// Notify client ticket was reserved
					try {
						handler.ticketReserved(concertName);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					
					// Notify all clients ticket was confirmed
					if (concert.getCurrentTickets().equals(concert.getConfirmationTickets())) {
						concert.setState(ConcertState.CONFIRMED);
						
						List<TicketClient> handlers = concerts.get(concert);
						
						// VIP handlers
						for (int i = 0; i < concert.getCurrentVipTickets(); i++) {
							try {
								handlers.get(i).ticketVipConfirmed(concertName, "vipconfirmID");
							} catch (RemoteException e) {
								e.printStackTrace();
							}
						}
						
						// Other handlers
						for (int i = concert.getCurrentVipTickets(); i < concert.getCurrentTickets(); i++) {
							try {
								handlers.get(i).ticketConfirmed(concertName, "vipconfirmID");
							} catch (RemoteException e) {
								e.printStackTrace();
							}
						}
					}
					
					
					break;
			}
			
		});
		
	}
	
}
