package rmi.server;

import rmi.*;

import java.io.*;
import java.rmi.MarshalledObject;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationID;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Servant implements CrowdfundingInitiatiorService, CrowdfundingBackerService {
	
	private static final Logger logger = Logger.getLogger(Servant.class.getName());
	
	private Map<Project, List<Pledge>> projects;
	
	public Servant(Map<Project, List<Pledge>> projects) {
		this.projects = projects;
	}
	
	private CrowdfundingData crowdfundingData;
	
	private File storage;
	
	public Servant(ActivationID id, MarshalledObject<File> data)
			throws ClassNotFoundException, IOException {
		
		storage = data.get();
		
		if (storage.exists()) {
			loadData(); //Carga del archivo con el listado de los proyectos.
		} else {
			logger.log(Level.INFO,
					"No existe un archivo con el listado de los proyectos.");
			crowdfundingData = new CrowdfundingData();
			projects = new HashMap<>();
		}
		
		Activatable.exportObject(this, id, 0);
	}
	
	@Override
	public List<Project> listProjects() {
		return projects.keySet().stream().filter(project -> !project.getState().equals(ProjectState.CANCELLED)).collect(Collectors.toList());
	}
	
	@Override
	public void pledge(Project project, int amount, CrowdfundingBackerResponseHandler supporter) throws RemoteException {
		
		synchronized (projects) {
			
			Optional<Project> op = projects.keySet().stream().filter(p -> p.getName().equals(project.getName())).findFirst();
			
			Project p;
			
			if (op.isPresent()) {
				p = op.get();
			} else {
				return;
			}
			
			
			if (p.getPrizes().stream().noneMatch(reward -> reward.getContribution().equals(amount))) {
				supporter.errorOnSupport(project.getName(), amount, "Pledge not equal to any prize.");
				return;
			}
			
			Optional<Reward> or = p.getPrizes().stream().filter(r ->
					r.getContribution() == amount).findFirst();
			
			Reward r;
			
			if (or.isPresent()) {
				r = or.get();
			} else {
				return;
			}
			
			
			p.sumPledge(amount);
			
			
			List<Pledge> pledges;
			
			synchronized (p) {
				switch (p.getState()) {
					case TO_CONFIRM:
						
						pledges = projects.get(p);
						pledges.add(new Pledge(supporter, amount, r));
						projects.put(p, pledges);
						
						if (p.getAccumulated() >= p.getGoal()) {
							
							p.setState(ProjectState.CONFIRMED);
							logger.log(Level.INFO, "Project " + p.getName() + " confirmed.");
							
							pledges.parallelStream().forEach(pl -> {
								try {
									pl.getHandler().prizeGranted(p.getName(), pl.getPledge(), r.getName());
								} catch (RemoteException ex) {
									logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
								}
							});
							
							try {
								updateData(); //Actualización del archivo con listado de proyectos
							} catch (Exception ex) {
								logger.log(Level.WARNING, ex.getMessage(), ex);
							}
							break;
						}
						
						break;
					case CONFIRMED:
						
						pledges = projects.get(p);
						pledges.add(new Pledge(supporter, amount, r));
						projects.put(p, pledges);
						supporter.prizeGranted(p.getName(), amount, r.getName());
						
						break;
					case CANCELLED:
						
						supporter.pledgeReturned(p.getName(), amount);
						
						break;
					default:
						throw new IllegalArgumentException();
				}
			}
		}
		
	}
	
	@Override
	public boolean createProject(String projectName, int goal, List<Reward> prizes) {
		
		synchronized (projects) {
			if (projects.containsKey(new Project(projectName, goal, prizes)))
				return false;
		}
		
		synchronized (this) {
			this.projects.put(new Project(projectName, goal, prizes), new ArrayList<>());
			logger.log(Level.INFO, "Project " + projectName + " created.");
			
			try {
				updateData();
			} catch (Exception ex) {
				logger.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
		
		return true;
	}
	
	@Override
	public boolean cancelProject(String projectName) throws RemoteException {
		
		synchronized (projects) {
			
			Optional<Project> op = projects.keySet().stream().filter(project -> project.getName().equals(projectName)).findFirst();
			
			Project p;
			
			if (op.isPresent()) {
				p = op.get();
			} else {
				return false;
			}
			
			if (p.getAccumulated().equals(p.getGoal()))
				return false;
			
			p.setState(ProjectState.CANCELLED);
			projects.put(p, projects.get(p));
			logger.log(Level.INFO, "Project " + projectName + " cancelled.");
			
			projects.get(p).parallelStream().forEach(pl -> {
				try {
					pl.getHandler().pledgeReturned(projectName, pl.getPledge());
				} catch (RemoteException ex) {
					logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
				}
			});
		}
		
		return true;
	}
	
	private synchronized void updateData() throws FileNotFoundException, IOException {
		crowdfundingData.setProjects(projects);
		ObjectOutputStream oos = new ObjectOutputStream(new
				FileOutputStream(storage));
		oos.writeObject(crowdfundingData);
		oos.close();
		logger.log(Level.INFO, "Se actualizó el archivo " +
				storage.getAbsolutePath() + " con el listado de proyectos.");
	}
	
	private synchronized void loadData() throws FileNotFoundException, IOException,
			ClassNotFoundException {
		logger.log(Level.INFO, "Se encontró el archivo " +
				storage.getAbsolutePath() + " con el listado de los proyectos");
		ObjectInputStream ois = new ObjectInputStream(new
				FileInputStream(storage));
		crowdfundingData = (CrowdfundingData) ois.readObject();
		projects = crowdfundingData.getProjects();
		ois.close();
	}
}
