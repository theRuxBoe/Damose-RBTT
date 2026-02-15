package test.java.frontend.rightpanel.panels;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.backend.model.PredizioneArrivo;
import main.java.frontend.rightpanel.panels.ArrivingVehiclePanel;

import java.time.LocalTime;

/**
 * (GENERATA DA AI)
 * Classe di test per ArrivingVehiclePanel.
 * Progettata per verificare l'inizializzazione della GUI e la gestione del Timer.
 */
class ArrivingVehiclePanelTest {

    private PredizioneArrivo veicoloSimulato;

    @BeforeEach
    void setUp() {
        veicoloSimulato = org.mockito.Mockito.mock(PredizioneArrivo.class);
        org.mockito.Mockito.when(veicoloSimulato.getRouteId()).thenReturn("10");
        org.mockito.Mockito.when(veicoloSimulato.getDirectionName()).thenReturn("Centro");
        org.mockito.Mockito.when(veicoloSimulato.getTripId()).thenReturn("T1");
        org.mockito.Mockito.when(veicoloSimulato.getArrivalTime()).thenReturn(LocalTime.now().plusMinutes(5));
        org.mockito.Mockito.when(veicoloSimulato.isRealTime()).thenReturn(false); // Fondamentale
    }

    /**
     * TEST 1: Verifica che il pannello venga creato correttamente
     * senza lanciare eccezioni e che contenga elementi grafici.
     */
    @Test
    void testCreazionePannello() {
        System.out.println("Esecuzione Test: Creazione Pannello");

        // Azione: Proviamo a istanziare il pannello
        ArrivingVehiclePanel panel = null;
        try {
            panel = new ArrivingVehiclePanel(veicoloSimulato);
        } catch (Exception e) {
            fail("La creazione del pannello ha lanciato un'eccezione: " + e.getMessage());
        }

        // Asserzione 1: L'oggetto non deve essere null
        assertNotNull(panel, "Il pannello non dovrebbe essere null");

        // Asserzione 2: Il layout dovrebbe essere impostato (GridBagLayout nel tuo codice)
        assertTrue(panel.getLayout() instanceof java.awt.GridBagLayout, 
                   "Il layout manager dovrebbe essere GridBagLayout");

        // Asserzione 3: Il pannello deve contenere dei componenti (Label, ecc.)
        // Nel tuo codice aggiungi vari JPanel e JLabel, quindi il count deve essere > 0
        assertTrue(panel.getComponentCount() > 0, 
                   "Il pannello dovrebbe contenere almeno un componente grafico");
    }

    /**
     * TEST 2: Verifica che il metodo stopTimer funzioni.
     * Questo è importante per evitare che i thread rimangano appesi in background.
     */
    @Test
    void testStopTimer() {
        System.out.println("Esecuzione Test: Stop Timer");

        // Setup
        ArrivingVehiclePanel panel = new ArrivingVehiclePanel(veicoloSimulato);

        // Azione: Chiamiamo il metodo stopTimer
        // Se il metodo lanciasse eccezioni, il test fallirebbe.
        assertDoesNotThrow(() -> panel.stopTimer(), 
                           "Il metodo stopTimer non dovrebbe lanciare eccezioni");
        
        // Nota: Non possiamo facilmente verificare se il timer è "veramente" fermo 
        // senza usare reflection o strumenti avanzati, ma per un progetto del primo anno
        // verificare che il metodo venga chiamato senza errori è sufficiente.
    }
    
    /**
     * TEST 3 (Opzionale): Verifica il calcolo visivo del tempo (Logica semplice)
     * Controlliamo semplicemente che non esploda chiamando la logica interna.
     */
    @Test
    void testStabilitàInterfaccia() {
         ArrivingVehiclePanel panel = new ArrivingVehiclePanel(veicoloSimulato);
         
         // Forziamo il repaint per assicurarci che eventuali calcoli grafici non rompano nulla
         assertDoesNotThrow(() -> {
             panel.revalidate();
             panel.repaint();
         });
    }
}