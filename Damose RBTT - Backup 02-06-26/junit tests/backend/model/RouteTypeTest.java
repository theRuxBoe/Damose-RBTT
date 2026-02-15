package backend.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RouteTypeTest {
	
	@Test
	void testFromCode() {
	    assertEquals(RouteType.BUS, RouteType.fromCode(3));
	    assertEquals(RouteType.METRO, RouteType.fromCode(1));
	    
	    assertThrows(IllegalArgumentException.class, () -> {
	        RouteType.fromCode(99);
	    });
	}

}
