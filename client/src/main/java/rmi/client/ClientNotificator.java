package rmi.client;

import rmi.TicketClient;

public class ClientNotificator implements TicketClient {
	
	@Override
	public void ticketReserved(String concert) {
		System.out.println("Ticket for concert " + concert + " reserved.");
	}
	
	@Override
	public void ticketVipConfirmed(String concert, String ticket) {
		System.out.println("VIP ticket for concert " + concert + " confirmed.");
	}
	
	@Override
	public void ticketConfirmed(String concert, String ticket) {
		System.out.println("Ticket for concert " + concert + " confirmed.");
	}
	
	@Override
	public void concertSoldOut(String concert) {
		System.out.println("Ticket for concert " + concert + " could not be reserved. Concert is sold out.");
	}
	
	@Override
	public void concertCancelled(String concert) {
		System.out.println("Ticket for concert " + concert + " could not be reserved. Concert is cancelled.");
	}
}
