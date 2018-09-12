package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Servicio para poder dar apoyo a proyectos utilizando RMI
 */
public interface CrowdfundingBackerService extends Remote {
	/**
	 * @return la lista de proyectos a los que se puede aportar
	 */
	List<Project> listProjects() throws RemoteException;
	
	/**
	 * Método asíncrono, las respuestas se envían mediante el parámetro
	 * {@link CrowdfundingBackerResponseHandler}
	 */
	void pledge(Project project, int amount, CrowdfundingBackerResponseHandler
			supporter) throws RemoteException;
}
