package rmi.client;

import rmi.CrowdfundingBackerResponseHandler;

public class ClientNotificator implements CrowdfundingBackerResponseHandler {
	
	
	@Override
	public void errorOnSupport(String projectName, int amount, String errorMessage) {
		System.out.println("Project " + projectName + " with pledge amount " + amount + " error: " + errorMessage);
	}
	
	@Override
	public void pledgeReceived(String projectName, int amount) {
		System.out.println("Project " + projectName + " with pledge amount " + amount + " received. ");
		
	}
	
	@Override
	public void prizeGranted(String projectName, int amount, String reward) {
		System.out.println("Project " + projectName + " with prize amount " + amount + " granted: " + reward);
		
	}
	
	@Override
	public void pledgeReturned(String projectName, int amount) {
		System.out.println("Project " + projectName + " with pledge amount " + amount + " returned.");
		
	}
}
