package test.java.frontend.rightpanel.panels;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.junit.Before;
import org.junit.Test;

import main.java.backend.model.Fermata;
import main.java.frontend.BackendController;
import main.java.frontend.rightpanel.panels.StopFocus;

/**
 * (GENERATA DA AI)
 * Classe di testing per StopFocus.
 * Include sia test JUnit per la logica di base, sia un main per il test visuale.
 */
public class StopFocusTest {

    private Fermata dummyFermata;

    /**
     * Preparazione dei dati prima di ogni test.
     */
    @Before
    public void setUp() {
        // CREAZIONE FERMATA FINTA
        // Esempio: new Fermata(ID, Nome, Latitudine, Longitudine)
        dummyFermata = new Fermata("1001", "Stazione Centrale", 45.48, 9.20); 
        
        // MOCK DEL BACKEND (Opzionale ma consigliato se il test fallisce)
        BackendController.openTransit();
        // Se il tuo costruttore di StopFocus chiama il BackendController e questo è null,
        // dovresti inizializzare qui un BackendController finto o di test.
    }

    /**
     * Test 1: Verifica che il pannello venga creato senza errori.
     */
    @Test
    public void testCreazionePannello() {
        System.out.println("Avvio test creazione pannello...");
        
        // Proviamo a istanziare la classe
        StopFocus stopFocusPanel = new StopFocus(dummyFermata);

        // Verifiche di base (Asserzioni)
        assertNotNull("Il pannello non dovrebbe essere null", stopFocusPanel);
        
        // Verifica che il layout sia stato impostato (BoxLayout come da codice)
        assertNotNull("Il layout manager dovrebbe essere impostato", stopFocusPanel.getLayout());
    }

    /**
     * Test 2: Verifica la coerenza dei dati della fermata passata.
     * (Simuliamo l'accesso ai campi privati tramite getter se disponibili, 
     * o verifichiamo componenti visuali se accessibili).
     */
    @Test
    public void testDatiFermata() {
        // Nota: Poiché 'stop' è privato in StopFocus, testiamo che l'oggetto 
        // passato non venga alterato o perso.
        StopFocus stopFocusPanel = new StopFocus(dummyFermata);
        
        // Qui verifichiamo solo che l'inizializzazione non lanci eccezioni
        // e che le dimensioni preferite siano calcolate.
        assertNotNull(stopFocusPanel.getPreferredSize());
    }

    // ===========================================================================
    // TEST VISUALE (Main Method)
    // Questo è utilissimo per i progetti Swing. Ti permette di vedere il pannello.
    // ===========================================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("Avvio Test Visuale StopFocus...");

                // 1. Creiamo una fermata di prova
                Fermata f = new Fermata("TEST_ID", "Fermata Test Università", 41.90, 12.49);

                // 2. IMPORTANTE: Inizializza qui eventuali servizi statici se necessario
                // Esempio: BackendController.init(); 
                // Se non lo fai, le chiamate a BackendController.getTTS() nel costruttore
                // potrebbero lanciare NullPointerException.
                
                // 3. Creiamo il pannello da testare
                StopFocus panel = new StopFocus(f);

                // 4. Creiamo una finestra (Frame) per ospitare il pannello
                JFrame frame = new JFrame("Test StopFocus - Anteprima");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(new Dimension(600, 800)); // Dimensione simile a quella reale
                
                // Aggiungiamo il pannello al frame
                frame.add(panel);
                
                // Centriamo e mostriamo
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                
                System.out.println("Finestra avviata. Verifica visivamente:");
                System.out.println("- Il nome della fermata");
                System.out.println("- Il pulsante dei preferiti (Stella)");
                System.out.println("- La lista degli arrivi (se il backend risponde)");

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Errore durante il test visuale: " + e.getMessage());
                System.err.println("Suggerimento: Verifica che BackendController sia inizializzato.");
            }
        });
    }
}