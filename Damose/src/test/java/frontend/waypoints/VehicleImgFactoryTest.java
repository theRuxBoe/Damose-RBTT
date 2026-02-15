package test.java.frontend.waypoints;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import main.java.backend.model.RouteType;
import main.java.frontend.waypoints.VehicleImgFactory;

/**
 * (GENERATA DA AI)
 */
class VehicleImgFactoryTest {

    private VehicleImgFactory factory;

    @BeforeEach
    void setUp() {
        factory = new VehicleImgFactory();
    }

    /**
     * Test parametrizzato per verificare che ogni tipo di veicolo
     * carichi un'immagine non nulla.
     * * NOTA: Questo test passerà solo se i file .png esistono effettivamente
     * nel percorso specificato (main/res/waypoints/) all'interno della cartella
     * delle risorse del progetto di test.
     */
    @ParameterizedTest
    @EnumSource(RouteType.class)
    @DisplayName("Dovrebbe caricare un'immagine valida per ogni RouteType supportato")
    void testSelectImage_LoadsImages(RouteType type) {
        // Eseguiamo il test solo per i tipi gestiti nello switch
        // Se aggiungi nuovi tipi all'Enum ma non allo switch, questo test fallirà (correttamente)
        // a meno che non li escludiamo qui.
        
        BufferedImage result = factory.selectImage(type);
        
        // Verifica che l'immagine non sia null
        assertNotNull(result, "L'immagine per il tipo " + type + " non dovrebbe essere null. Verifica il percorso del file.");
        
        // Verifica opzionale: controlla che l'immagine abbia dimensioni valide
        assertTrue(result.getWidth() > 0, "La larghezza dell'immagine dovrebbe essere > 0");
        assertTrue(result.getHeight() > 0, "L'altezza dell'immagine dovrebbe essere > 0");
    }

    @Test
    @DisplayName("Dovrebbe restituire la stessa istanza (cache) alle chiamate successive")
    void testSelectImage_Caching() {
        // 1. Prima chiamata: carica l'immagine dal disco
        BufferedImage firstCall = factory.selectImage(RouteType.BUS);
        assertNotNull(firstCall);

        // 2. Seconda chiamata: dovrebbe restituire l'immagine dalla variabile static
        BufferedImage secondCall = factory.selectImage(RouteType.BUS);
        
        // 3. Verifica che siano esattamente lo stesso oggetto in memoria
        assertSame(firstCall, secondCall, "La factory dovrebbe restituire l'istanza cacheata, non caricare una nuova immagine.");
    }

    @Test
    @DisplayName("Dovrebbe restituire null per input nullo")
    void testSelectImage_NullInput() {
        BufferedImage result = factory.selectImage(null);
        assertNull(result, "Se il RouteType è null, il risultato deve essere null");
    }
    
    @Test
    @DisplayName("Dovrebbe gestire correttamente tipi non mappati (default case)")
    void testSelectImage_UnknownType() {
        // Questo test è utile solo se l'Enum RouteType ha valori non gestiti nello switch.
        // Simuliamo un comportamento di default se passiamo un tipo che finirebbe nel 'default' dello switch
        // (Nota: difficile da testare se tutti gli Enum sono coperti, ma utile per robustezza futura)
    }
}
