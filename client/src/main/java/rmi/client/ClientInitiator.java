package rmi.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rmi.CrowdfundingInitiatiorService;
import rmi.Reward;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ClientInitiator {
	private static Logger logger = LoggerFactory.getLogger(ClientInitiator.class);
	
	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		logger.info("Client Initiator Starting ...");
		
		final CrowdfundingInitiatiorService initiatiorService = (CrowdfundingInitiatiorService) Naming.lookup("//localhost:1099/InitiatorService");
		
		
		for (int i = 0; i < 4; i++) {
			
			List<Reward> rewards = new ArrayList<>();
			
			rewards.add(new Reward("Ball", i + 1));
			rewards.add(new Reward("Bat", i * i + 2));
			rewards.add(new Reward("Hat", i * i * i + 3));
			
			initiatiorService.createProject("Project " + i, i * i, rewards);
		}
		
		initiatiorService.cancelProject("Project 2");
		
	}
}
