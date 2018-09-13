package rmi;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Project implements Serializable {
	
	private final String name;
	
	private final Integer goal;
	
	private final List<Reward> prizes;
	
	private Integer accumulated;
	
	private ProjectState state;
	
	public Project(String name, Integer goal, List<Reward> prizes) {
		this.name = name;
		this.goal = goal;
		this.prizes = prizes;
		this.accumulated = 0;
		this.state = ProjectState.TO_CONFIRM;
	}
	
	public String getName() {
		
		return name;
	}
	
	public Integer getGoal() {
		return goal;
	}
	
	public Integer getAccumulated() {
		return accumulated;
	}
	
	public ProjectState getState() {
		return state;
	}
	
	public void setState(ProjectState state) {
		this.state = state;
	}
	
	public List<Reward> getPrizes() {
		return prizes;
	}
	
	public void sumPledge(int amount) {
		this.accumulated += amount;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Project project = (Project) o;
		return Objects.equals(name, project.name);
	}
	
	@Override
	public int hashCode() {
		
		return Objects.hash(name);
	}
	
	@Override
	public String toString() {
		return "Project{" +
				"name='" + name + '\'' +
				", goal=" + goal +
				", prizes=" + prizes +
				", accumulated=" + accumulated +
				", state=" + state +
				'}';
	}
}
