package rmi.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rmi.CrowdfundingBackerResponseHandler;
import rmi.CrowdfundingBackerService;
import rmi.Project;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ClientBacker {
	private static Logger logger = LoggerFactory.getLogger(ClientBacker.class);
	
	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		logger.info("Client Backer Starting ...");
		
		final CrowdfundingBackerService backerService = (CrowdfundingBackerService) Naming.lookup("//localhost:1099/BackerService");
		
		// Instantiate handler
		CrowdfundingBackerResponseHandler clientNotificator = new ClientNotificator();
		
		// Export handler
		final Remote remote = UnicastRemoteObject.exportObject(clientNotificator, 0);
		
		List<Project> projects = backerService.listProjects();
		
		System.out.println(projects);
		
		for (int i = 0; i < projects.size(); i++) {
			backerService.pledge(projects.get(i), i * i * i, clientNotificator);
		}
	}
}
