package rmi.server;

import java.util.Objects;

public class Concert {
	
	private final String name;
	private final Integer confirmationTickets;
	private final Integer maxTickets;
	private final Integer vipTickets;
	
	private ConcertState state;
	
	private Integer currentTickets;
	private Integer currentVipTickets;
	
	public Concert(String name, Integer confirmationTickets, Integer maxTickets, Integer vipTickets) {
		this.name = name;
		this.confirmationTickets = confirmationTickets;
		this.maxTickets = maxTickets;
		this.vipTickets = vipTickets;
		this.state = ConcertState.TO_CONFIRM;
		this.currentTickets = 0;
		this.currentVipTickets = 0;
	}
	
	public String getName() {
		return name;
	}
	
	public Integer getConfirmationTickets() {
		return confirmationTickets;
	}
	
	public Integer getMaxTickets() {
		return maxTickets;
	}
	
	public Integer getVipTickets() {
		return vipTickets;
	}
	
	public ConcertState getState() {
		return state;
	}
	
	public synchronized void setState(ConcertState state) {
		this.state = state;
	}
	
	public Integer getCurrentTickets() {
		return currentTickets;
	}
	
	public Integer getCurrentVipTickets() {
		return currentVipTickets;
	}
	
	public synchronized void addTicket() {
		this.currentTickets++;
	}
	
	public synchronized void cancel() {
		this.state = ConcertState.CANCELLED;
	}
	
	@Override
	public String toString() {
		return "Concert{" +
				"name='" + name + '\'' +
				", confirmationTickets=" + confirmationTickets +
				", maxTickets=" + maxTickets +
				", vipTickets=" + vipTickets +
				", state=" + state +
				'}';
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Concert concert = (Concert) o;
		return Objects.equals(name, concert.name);
	}
	
	@Override
	public int hashCode() {
		
		return Objects.hash(name);
	}
	
	public synchronized void addVipTicket() {
		this.currentVipTickets++;
	}
}
