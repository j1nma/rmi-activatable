package rmi;

import java.io.Serializable;

public class Pledge implements Serializable {
	
	private final CrowdfundingBackerResponseHandler handler;
	
	private final Integer pledge;
	
	private final Reward reward;
	
	
	public Pledge(CrowdfundingBackerResponseHandler handler, Integer pledge, Reward reward) {
		this.handler = handler;
		this.pledge = pledge;
		this.reward = reward;
	}
	
	public CrowdfundingBackerResponseHandler getHandler() {
		return handler;
	}
	
	public Integer getPledge() {
		return pledge;
	}
	
	public Reward getReward() {
		return reward;
	}
}
