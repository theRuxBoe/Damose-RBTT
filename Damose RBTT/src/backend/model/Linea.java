package backend.model;

import java.util.Objects;

public class Linea {
	
	private String routeId, name, agencyId, description;
	private int type;
	
	public Linea(String rId, String n, String agId, String desc, int t) {
		
		this.routeId = rId;
		this.name = n;
		this.agencyId = agId;
		this.description = desc;
		this.type = t;
	}
	
	@Override
	public String toString() {
		
		return "Linea "+this.routeId+" ("+this.description+")";
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(agencyId, description, name, routeId, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Linea other = (Linea) obj;
		return Objects.equals(agencyId, other.agencyId) && Objects.equals(description, other.description)
				&& Objects.equals(name, other.name) && Objects.equals(routeId, other.routeId) && type == other.type;
	}
	

}
