package rmi.server;

import rmi.Project;

import java.io.Serializable;
import java.util.Map;

/**
 * Clase que sirve para serializar a la variable de instancia que contiene a los
 * proyectos.
 * <p>
 * De esta forma, incluso habiéndose "caído" el RMID, al instanciar
 * un nuevo stub, se pueden recuperar los proyectos que existieron en instancias
 * anteriores.
 * <p>
 * A modo de ejemplo sólo se persisten los proyectos sin los
 * aportes. Podría persistirse toda la información.
 */
public class CrowdfundingData implements Serializable {
	private static final long serialVersionUID = 4763557739487362456L;
	private Map<String, Project> projects;
	
	public Map<String, Project> getProjects() {
		return projects;
	}
	
	public void setProjects(Map<String, Project> projects) {
		this.projects = projects;
	}
}

