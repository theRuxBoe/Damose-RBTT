package main.java.backend.model;


/**
 * The Class RisultatoLinea -> a wrapper with a Linea object and an additional field which indentifies the route's direction name.
 */
public class RisultatoLinea extends DatoGTF {
	
	/** The direction name. */
	private Linea linea;
	private String directionName;
	
	/**
	 * Instantiates a new risultato linea.
	 *
	 * @param routeId the route id
	 * @param directionName the direction name
	 */
	public RisultatoLinea(Linea linea, String directionName) {
		
		this.linea = linea;
		this.directionName = directionName;
		
	}
	
	/**
	 * To string.
	 *
	 * @return the string with the route id and its direction name
	 */
	@Override 
	public String toString() {
		
		return "Linea " + linea.toString() + " direzione " + directionName;
	}

	/**
	 * Gets the route id.
	 *
	 * @return the route id
	 */
	public Linea getLinea() {
		return linea;
	}

	/**
	 * Gets the direction name.
	 *
	 * @return the direction name
	 */
	public String getDirectionName() {
		return directionName;
	}
	
	public String getRouteId() {
		
		return linea.getRouteId();
	}
	
	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        // Usa il routeId per l'hashcode della linea per essere sicuro
        result = prime * result + ((linea == null) ? 0 : linea.getRouteId().hashCode());
        result = prime * result + ((directionName == null) ? 0 : directionName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        
        RisultatoLinea other = (RisultatoLinea) obj;
        
        // Controllo sulla Linea (basandoci sul RouteID che è univoco)
        if (linea == null) {
            if (other.linea != null) return false;
        } else if (!linea.getRouteId().equals(other.linea.getRouteId()))
            return false;
            
        // Controllo sulla Direzione
        if (directionName == null) {
            if (other.directionName != null) return false;
        } else if (!directionName.equals(other.directionName))
            return false;
            
        return true;
    }

}
