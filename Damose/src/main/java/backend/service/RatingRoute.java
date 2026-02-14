package main.java.backend.service;

/**
 * The Enum RatingRoute -> an enum class which contains the rating adjectives of a route's service quality.
 */
public enum RatingRoute {
	
	/** Excellent service. */
	ECCELLENTE("Eccellente"),
	
	/** Good service. */
	BUONO("Buono"),
	
	/** Decent service. */
	SUFFICIENTE("Sufficiente"),
	
	/** Mediocre service. */
	MEDIOCRE("Mediocre"),
	
	/** Terrible service. */
	PESSIMO("Pessimo");
	
	/** The label. */
	private final String etichetta;
	
	/**
	 * Instantiates a new rating route.
	 *
	 * @param etichetta the label
	 */
	RatingRoute(String etichetta) {
		
		this.etichetta = etichetta;
	}
	
	/**
	 * Gets the label.
	 *
	 * @return the label
	 */
	public String getEtichetta() {
        return etichetta;
    }
	
	/**
	 * From score.
	 *
	 * @param score the score
	 * @return the rating route
	 */
	public static RatingRoute fromScore(int score) {
        if (score >= 50) {
            return ECCELLENTE;
        } else if (score >= 10) {
            return BUONO;
        } else if (score >= -10) {
            return SUFFICIENTE;
        } else if (score >= -50) {
            return MEDIOCRE;
        } else {
            return PESSIMO;
        }
    }

}
