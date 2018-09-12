package rmi;

import java.util.Objects;

public class Reward {
	
	private final String name;
	
	private final Integer contribution;
	
	
	public Reward(String name, Integer contribution) {
		this.name = name;
		this.contribution = contribution;
	}
	
	public String getName() {
		return name;
	}
	
	public Integer getContribution() {
		return contribution;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Reward reward = (Reward) o;
		return Objects.equals(name, reward.name) &&
				Objects.equals(contribution, reward.contribution);
	}
	
	@Override
	public int hashCode() {
		
		return Objects.hash(name, contribution);
	}
	
	@Override
	public String toString() {
		return "Reward{" +
				"name='" + name + '\'' +
				", contribution=" + contribution +
				'}';
	}
}
