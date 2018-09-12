package rmi;

import java.util.List;
import java.util.Objects;

public class Project {
	
	private final String name;
	
	private final Integer goal;
	
	private final List<Reward> rewards;
	
	
	public Project(String name, Integer goal, List<Reward> rewards) {
		this.name = name;
		this.goal = goal;
		this.rewards = rewards;
	}
	
	public String getName() {
		
		return name;
	}
	
	public Integer getGoal() {
		return goal;
	}
	
	public List<Reward> getRewards() {
		return rewards;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Project project = (Project) o;
		return Objects.equals(name, project.name) &&
				Objects.equals(goal, project.goal) &&
				Objects.equals(rewards, project.rewards);
	}
	
	@Override
	public int hashCode() {
		
		return Objects.hash(name, goal, rewards);
	}
	
	@Override
	public String toString() {
		return "Project{" +
				"name='" + name + '\'' +
				", goal=" + goal +
				", rewards=" + rewards +
				'}';
	}
}
