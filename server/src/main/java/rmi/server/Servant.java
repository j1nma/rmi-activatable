package rmi.server;

import rmi.*;

import java.util.List;

public class Servant implements CrowdfundingInitiatiorService, CrowdfundingBackerService {
	
	@Override
	public List<Project> listProjects() {
		return null;
	}
	
	@Override
	public void pledge(Project project, int amount, CrowdfundingBackerResponseHandler supporter) {
	
	}
	
	@Override
	public boolean createProject(String projectName, int goal, List<Reward> prizes) {
		return false;
	}
	
	@Override
	public boolean cancelProject(String projectName) {
		return false;
	}
}
