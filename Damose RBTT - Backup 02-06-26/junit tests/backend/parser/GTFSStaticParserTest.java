package backend.parser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class GTFSStaticParserTest {
	
	private GTFSStaticParser parser = new GTFSStaticParser();

    @Test
    void testListsAreNotNull() throws IOException {
    	
    	parser.parseAll("https://romamobilita.it/sites/default/files/rome_static_gtfs.zip");
        assertNotNull(parser.getFermate(), "La lista fermate non dovrebbe essere nulla");
        assertNotNull(parser.getLinee(), "La lista linee non dovrebbe essere nulla");
        assertNotNull(parser.getCorse(), "La lista corse non dovrebbe essere nulla");
        assertNotNull(parser.getOrari(), "La lista orari non dovrebbe essere nulla");
        assertNotNull(parser.getCalendarMap(), "La mappa calendari non dovrebbe essere nulla");
    }

}
