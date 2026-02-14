package main.java.backend.model;

import java.util.Objects;

/**
 * The Class Linea -> identifies a certain route.
 */
public class Linea extends DatoGTF {
	
	/** The route id, the route name, the route agency id and the description (it can be blank). */
	private String routeId, name, agencyId, description;
	
	/** The type of the route (e.g. a bus). */
	private int type;
	
	/**
	 * Instantiates a new linea.
	 *
	 * @param rId the route id
	 * @param n the name
	 * @param agId the agency id
	 * @param desc the description
	 * @param t the type
	 */
	public Linea(String rId, String n, String agId, String desc, int t) {
		
		this.routeId = rId;
		this.name = n;
		this.agencyId = agId;
		this.description = desc;
		this.type = t;
	}
	
	/**
	 * To string.
	 *
	 * @return the string with the route id and the description
	 */
	@Override
	public String toString() {
		
		return "Linea "+this.routeId+" ("+this.description+")";
	}

	/**
	 * Gets the route id.
	 *
	 * @return the route id
	 */
	public String getRouteId() {
		return routeId;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the agency id.
	 *
	 * @return the agency id
	 */
	public String getAgencyId() {
		return agencyId;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode() {
		return Objects.hash(agencyId, description, name, routeId, type);
	}

	/**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
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
	
	/**
	 * Gets the route type.
	 *
	 * @return the route type
	 */
	public RouteType getRouteType() {
		
		return RouteType.fromCode(type);
	}

}
