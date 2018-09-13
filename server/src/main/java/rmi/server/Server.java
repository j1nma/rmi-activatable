package rmi.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class Server {
	private static Logger logger = LoggerFactory.getLogger(Server.class);
	
	public static void main(String[] args) throws RemoteException {
		logger.info("Server Starting ...");
		
		Servant servant = new Servant(new HashMap<>());
		
		final Remote remote = UnicastRemoteObject.exportObject(servant, 0);
		final Registry registry = LocateRegistry.getRegistry();
		
		registry.rebind("BackerService", remote);
		registry.rebind("InitiatorService", remote);
		
		
	}
}
