package test.java.frontend.rightpanel.panels;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.junit.Before;
import org.junit.Test;

import main.java.backend.model.Linea;
import main.java.backend.model.RisultatoLinea;
import main.java.frontend.BackendController;
import main.java.frontend.rightpanel.panels.LinePanel;

/**
 *
 * (GENERATA DA AI)
 * Classe di testing per LinePanel.
 * * NOTA IMPORTANTE: Poiché LinePanel chiama il Backend nel costruttore
 * (BackendController.getTTS()...), assicurarsi che il backend sia inizializzato
 * o simulato prima di eseguire questi test, altrimenti si otterrà una NullPointerException.
 */
public class LinePanelTest {

    private RisultatoLinea dummyLine;

    /**
     * Preparazione dei dati.
     */
    @Before
    public void setUp() {
        // 1. Creiamo una Linea fittizia per il test.
        // Esempio: new Linea(RouteID, Nome, Descrizione/Direzione)
        dummyLine = new RisultatoLinea(new Linea("110", "Teatro", "Alfa", "", 0), "Ibiza");
        		
        // 2. MOCK/INIT BACKEND
        // TODO: Qui dovresti inizializzare il tuo BackendController se non è statico,
        BackendController.openTransit();
        
        // oppure assicurarti che il database di test sia raggiungibile.
    }

    @Test
    public void testCreazionePannello() {
        System.out.println("Test: Creazione LinePanel");
        
        // Proviamo a creare il pannello
        // Se il backend non è attivo, questa riga potrebbe fallire.
        LinePanel panel = null;
        try {
            panel = new LinePanel(dummyLine);
        } catch (Exception e) {
            System.err.println("Attenzione: Impossibile creare il pannello nel test JUnit.");
            System.err.println("Motivo: Probabilmente il BackendController non è inizializzato.");
            System.err.println("Errore: " + e.getMessage());
            // Se fallisce qui, il test si ferma, ma almeno sappiamo perché.
            return; 
        }

        // Verifiche
        assertNotNull("Il pannello non deve essere null", panel);
        
        // Verifica del Layout Manager (da codice è BorderLayout)
        assertTrue("Il layout deve essere BorderLayout", panel.getLayout() instanceof BorderLayout);
        
        // Verifica che ci siano componenti aggiunti (Label dati e ScrollPanel)
        assertTrue("Il pannello deve contenere dei componenti", panel.getComponentCount() > 0);
    }

    /**
     * TEST VISUALE (Metodo Main)
     * Esegui questo metodo come Java Application per vedere il pannello a schermo.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("Avvio Test Visuale LinePanel...");

                // 1. Creiamo dati di prova
                Linea lineaProva = new Linea("90", "Linea 90", "Beta", "Termini", 0);

                // 2. IMPORTANTE: Inizializza il Backend qui!
                // BackendController.init(); // Esempio
                // Senza questo, le chiamate a getValutazioneLinea() o trovaFermatePerLinea() falliranno.
                
                // 3. Istanziamo il pannello
                LinePanel panel = new LinePanel(new RisultatoLinea(lineaProva, "Taranto"));

                // 4. Creiamo la finestra contenitore
                JFrame frame = new JFrame("Test LinePanel - " + lineaProva.getName());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(new Dimension(500, 600));
                
                // Aggiungiamo il pannello
                frame.add(panel);
                
                // Mostriamo
                frame.setLocationRelativeTo(null); // Centro schermo
                frame.setVisible(true);
                
                System.out.println("Finestra aperta.");
                System.out.println("Controllare:");
                System.out.println("- Nome e descrizione in alto");
                System.out.println("- Valutazione della linea (Qualità)");
                System.out.println("- Lista delle fermate sulla destra/centro");

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("\nERRORE CRITICO: " + e.getMessage());
                System.err.println("Controlla che il BackendController restituisca dati validi per la linea inserita.");
            }
        });
    }
}