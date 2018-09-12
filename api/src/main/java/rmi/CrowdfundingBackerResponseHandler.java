package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * servicio de respuesta para un backer de un proyecto
 */
public interface CrowdfundingBackerResponseHandler extends Remote {
	/**
	 * se invoca con cualquier error ocurrido durante el llamado a
	 * {@link CrowdfundingBackerService#pledge}
	 */
	void errorOnSupport(String projectName, int amount, String
			errorMessage) throws RemoteException;
	
	/**
	 * se invoca al llamar a {@link CrowdfundingBackerService#pledge} si se
	 * aceptó el aporte pero aún no se otorga el premio porque el proyecto no
	 * está aún confirmado.
	 */
	void pledgeReceived(String projectName, int amount) throws
			RemoteException;
	
	/**
	 * Se invoca cuando un premio es otorgado ya sea por aportar a un proyecto
	 * confirmado o porque se confirmó el proyecto.
	 */
	void prizeGranted(String projectName, int amount, String reward)
			throws RemoteException;
	
	/**
	 * Se invoca cuando un proyecto fue cancelado para notificar la devolución
	 * del aporte.
	 */
	void pledgeReturned(String projectName, int amount) throws
			RemoteException;
}