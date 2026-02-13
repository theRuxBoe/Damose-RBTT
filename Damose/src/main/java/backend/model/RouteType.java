package main.java.backend.model;

public enum RouteType {
    TRAM(0),
    METRO(1),
    TRAIN(2),
    BUS(3);

    private final int code;

    RouteType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static RouteType fromCode(int code) {
        for (RouteType t : values()) {
            if (t.code == code) {
                return t;
            }
        }
        throw new IllegalArgumentException("Route type sconosciuto: " + code);
    }
}
