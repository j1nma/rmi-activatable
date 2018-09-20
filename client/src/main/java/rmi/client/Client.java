package rmi.client;

import rmi.*;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
	private static final Logger logger = Logger.getLogger("Client");
	
	public static void main(String[] args)
			throws MalformedURLException, RemoteException, NotBoundException,
			InterruptedException {
		
		CrowdfundingInitiatiorService crowdfundingInitiatiorService =
				(CrowdfundingInitiatiorService) Naming
						.lookup("//localhost:1099/service");
		
//		CrowdfundingInitiatiorService crowdfundingInitiatiorService =
//				(CrowdfundingInitiatiorService) Naming
//						.lookup("//localhost:1099/" +
//								CrowdfundingInitiatiorService.class.getName());
		
		CrowdfundingBackerService crowdfundingBackerService =
				(CrowdfundingBackerService) Naming
						.lookup("//localhost:1099/service");
		
//		CrowdfundingBackerService crowdfundingBackerService =
//				(CrowdfundingBackerService) Naming
//						.lookup("//localhost:1099/" +
//								CrowdfundingBackerService.class.getName());
		/**
		 * 1. Creación de un proyecto
		 */
		try {
			List<Reward> project1Rewards = new ArrayList<>();
			project1Rewards.add(new Reward("Premio #1", 250));
			project1Rewards.add(new Reward("Premio #2", 750));
			System.out.println(crowdfundingInitiatiorService.createProject("Proyecto #1", 1000,
					project1Rewards));
		} catch (RemoteException ex) {
			logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
		}
		
		/**
		 * 2. Listado de proyectos
		 */
		List<Project> projects = null;
		
		try {
			projects = crowdfundingBackerService.listProjects();
			projects.stream().forEach(p -> System.out.println(p));
		} catch (RemoteException ex) {
			logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
		}
		
		/**
		 * 3. Creación de un proyecto con un nombre ya existente
		 */
		try {
			List<Reward> project2Rewards = new ArrayList<>();
			project2Rewards.add(new Reward("Premio #3", 1000));
			crowdfundingInitiatiorService.createProject("Proyecto #1", 1000,
					project2Rewards);
		} catch (RemoteException ex) {
			logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
		}
		
		/**
		 * 4. Aporte con un monto distinto a los montos de los premios del
		 * proyecto
		 */
		Project project1 = projects.get(0);
		try {
			CrowdfundingBackerResponseHandler handler = new
					ClientNotificator();
			crowdfundingBackerService.pledge(project1, 300, handler);
		} catch (RemoteException ex) {
			logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
		}
		
		/**
		 * 5. Aportes a un proyecto
		 */
		try {
			CrowdfundingBackerResponseHandler handler2 = new
					ClientNotificator();
			crowdfundingBackerService.pledge(project1, 250, handler2);
			CrowdfundingBackerResponseHandler handler3 = new
					ClientNotificator();
			crowdfundingBackerService.pledge(project1, 750, handler3);
		} catch (RemoteException ex) {
			logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
		}
		
		/**
		 * 6. Cancelar un proyecto
		 */
		try {
			crowdfundingInitiatiorService.cancelProject(project1.getName());
		} catch (RemoteException ex) {
			logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
			
		}
		
		/**
		 * 7. Aportar a un proyecto cancelado
		 */
		try {
			CrowdfundingBackerResponseHandler handler4 = new
					ClientNotificator();
			crowdfundingBackerService.pledge(project1, 750, handler4);
		} catch (RemoteException ex) {
			logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
		}
		
		/**
		 * 8. Cancelar un proyecto inexistente
		 */
		try {
			crowdfundingInitiatiorService.cancelProject("Proyecto Uno");
		} catch (RemoteException ex) {
			logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
		}
		
		/**
		 * 9. Cancelar un proyecto ya cancelado
		 */
		try {
			crowdfundingInitiatiorService.cancelProject(project1.getName());
		} catch (RemoteException ex) {
			logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
		}
		
		/**
		 * 10. Aportar sin un handler
		 */
		try {
			crowdfundingBackerService.pledge(project1, 750, null);
		} catch (RemoteException ex) {
			logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
		}
	}
	
}