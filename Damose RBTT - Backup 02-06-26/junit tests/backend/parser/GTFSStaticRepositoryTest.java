package backend.parser;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class GTFSStaticRepositoryTest {

	@Test
    void testInitIfNeededAndGetters() throws Exception {
        String url = "https://romamobilita.it/sites/default/files/rome_static_gtfs.zip";
        GTFSStaticRepository.initIfNeeded(url);

        assertNotNull(GTFSStaticRepository.getFermate(), "Repository dovrebbe contenere fermate");
        assertNotNull(GTFSStaticRepository.getLinee(), "Repository dovrebbe contenere linee");
        assertNotNull(GTFSStaticRepository.getOrari(), "Repository dovrebbe contenere orari");
        assertNotNull(GTFSStaticRepository.getCorse(), "Repository dovrebbe contenere corse");
        assertNotNull(GTFSStaticRepository.getCalendarMap(), "Repository dovrebbe contenere servizi calendario");

   }
	
}
