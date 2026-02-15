package backend.parser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CsvUtilsTest {
	
	@TempDir
    Path tempDir;

    @Test
    void testReadCSV() throws IOException {
        // Crea un file CSV temporaneo
        Path csvFile = tempDir.resolve("test.csv");
        Files.writeString(csvFile, "col1,col2\nval1,val2\nval3,val4");

        List<String[]> result = CsvUtils.readCSV(csvFile);

        assertEquals(2, result.size(), "Dovrebbe leggere 2 righe di dati (saltando l'intestazione)");
        assertEquals("val1", result.get(0)[0]);
        assertEquals("val2", result.get(0)[1]);
    }

}
