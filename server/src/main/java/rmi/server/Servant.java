package rmi.server;

import rmi.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Servant implements CrowdfundingInitiatiorService, CrowdfundingBackerService {
	
	private final Map<Project, List<Pledge>> projects;
	
	public Servant(Map<Project, List<Pledge>> projects) {
		this.projects = projects;
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
			
			projects.get(p).add(new Pledge(supporter, amount));
			
			p.sumPledge(amount);
			
			if (p.getAccumulated() >= p.getGoal()) {
				p.setState(ProjectState.CONFIRMED);
				supporter.pledgeReceived(p.getName(), amount);
				
				List<Pledge> pledges = projects.get(p);
				int index = 0;
				
				for (Reward reward : p.getPrizes()) {
					pledges.get(index++).getHandler().prizeGranted(p.getName(), reward.getContribution(), reward.getName());
				}
			}
		}
		
	}
	
	@Override
	public boolean createProject(String projectName, int goal, List<Reward> prizes) {
		
		if (projects.containsKey(new Project(projectName, goal, prizes))) {
			return false;
		} else {
			this.projects.put(new Project(projectName, goal, prizes), new ArrayList<>());
			return true;
		}
	}
	
	@Override
	public boolean cancelProject(String projectName) throws RemoteException {
		
		Optional<Project> op = projects.keySet().stream().filter(project -> project.getName().equals(projectName)).findFirst();
		
		Project p;
		
		if (op.isPresent()) {
			p = op.get();
		} else {
			return false;
		}
		
		if (p.getAccumulated().equals(p.getGoal()))
			return false;
		
		for (Pledge pledge : projects.get(p)) {
			pledge.getHandler().pledgeReturned(p.getName(), pledge.getPledge());
		}
		
		
		return true;
	}
}
