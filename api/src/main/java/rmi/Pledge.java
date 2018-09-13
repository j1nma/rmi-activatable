package rmi;

import java.util.Objects;

public class Pledge {
	
	private final CrowdfundingBackerResponseHandler handler;
	
	private final Integer pledge;
	
	
	public Pledge(CrowdfundingBackerResponseHandler handler, Integer pledge) {
		this.handler = handler;
		this.pledge = pledge;
	}
	
	public CrowdfundingBackerResponseHandler getHandler() {
		return handler;
	}
	
	public Integer getPledge() {
		return pledge;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Pledge pledge1 = (Pledge) o;
		return Objects.equals(handler, pledge1.handler) &&
				Objects.equals(pledge, pledge1.pledge);
	}
	
	@Override
	public int hashCode() {
		
		return Objects.hash(handler, pledge);
	}
	
	@Override
	public String toString() {
		return "Pledge{" +
				"handler=" + handler +
				", pledge=" + pledge +
				'}';
	}
}
