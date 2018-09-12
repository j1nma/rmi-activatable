package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * servicio para el manejo de proyectos a través de un servicio RMI sincrónico
 */
public interface CrowdfundingInitiatiorService extends Remote {
	/**
	 * @return true si pudo crear el proyecto, false si no
	 */
	boolean createProject(String projectName, int goal, List<Reward> prizes) throws
			RemoteException;
	
	/**
	 * @return true si el proyecto pudo ser cancelado
	 */
	boolean cancelProject(String projectName) throws RemoteException;
}