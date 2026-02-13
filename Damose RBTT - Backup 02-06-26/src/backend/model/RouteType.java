package backend.model;

/**
 * The Enum RouteType -> identifies a route type.
 */
public enum RouteType {
    
    /** The tram. */
    TRAM(0),
    
    /** The metro. */
    METRO(1),
    
    /** The train. */
    TRAIN(2),
    
    /** The bus. */
    BUS(3);

    /** The code. */
    private final int code;

    /**
     * Instantiates a new route type.
     *
     * @param code the code
     */
    RouteType(int code) {
        this.code = code;
    }

    /**
     * Gets the code.
     *
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * From code.
     *
     * @param code the code
     * @return the route type
     */
    public static RouteType fromCode(int code) throws IllegalArgumentException {
        for (RouteType t : values()) {
            if (t.code == code) {
                return t;
            }
        }
        throw new IllegalArgumentException("Route type sconosciuto: " + code);
    }
}
