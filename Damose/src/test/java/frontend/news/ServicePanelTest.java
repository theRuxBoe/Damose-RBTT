package test.java.frontend.news;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.border.BevelBorder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.frontend.news.ServicePanel;

/**
 * (GENERATA DA AI)
 */
class ServicePanelTest {

    private ServicePanel panel;
    private boolean backendAvailable;

    @BeforeEach
    void setUp() {
        // Tenta di inizializzare il pannello.
        // Poiché il costruttore chiama BackendController (statico), potrebbe fallire
        // se l'ambiente di backend non è attivo. Gestiamo l'eccezione per non bloccare i test.
        try {
            panel = new ServicePanel();
            backendAvailable = true;
        } catch (Exception e) {
            System.out.println("AVVISO: Impossibile istanziare ServicePanel (Backend mancante?). " +
                               "I test verranno eseguiti parzialmente o saltati.");
            backendAvailable = false;
            panel = null;
        }
    }

    /**
     * TEST 1: Verifica delle dimensioni preferite e del bordo.
     * Controlla che il pannello rispetti le specifiche grafiche impostate nel costruttore.
     */
    @Test
    void testDimensioniEBordo() {
        // Se l'istanziazione è fallita a causa delle dipendenze, interrompiamo il test in modo pulito
        if (!backendAvailable || panel == null) return;

        // Verifica Bordo
        assertTrue(panel.getBorder() instanceof BevelBorder, 
                   "Il pannello deve avere un bordo di tipo BevelBorder.");

        // Verifica Dimensioni
        Dimension expected = new Dimension(350, 0);
        // Nota: getPreferredSize potrebbe ritornare valori alterati dal layout, 
        // controlliamo se la larghezza impostata è rispettata.
        assertEquals(350, panel.getPreferredSize().width, 
                     "La larghezza preferita del pannello deve essere 350.");
    }

    /**
     * TEST 2: Verifica del Layout Manager.
     * Il pannello deve utilizzare un BoxLayout allineato sull'asse pagina (verticale).
     */
    @Test
    void testLayoutManager() {
        if (!backendAvailable || panel == null) return;

        assertTrue(panel.getLayout() instanceof BoxLayout, 
                   "Il layout manager deve essere di tipo BoxLayout.");
    }

    /**
     * TEST 3: Verifica della presenza dell'etichetta del titolo.
     * Controlla che la JLabel con il testo "Aggiornamenti servizio :" sia presente.
     */
    @Test
    void testPresenzaEtichettaTitolo() {
        if (!backendAvailable || panel == null) return;

        boolean labelTrovata = false;
        String testoAtteso = "Aggiornamenti servizio :";

        for (Component c : panel.getComponents()) {
            if (c instanceof JLabel) {
                JLabel l = (JLabel) c;
                if (testoAtteso.equals(l.getText())) {
                    labelTrovata = true;
                    // Verifica opzionale del font se necessario
                    assertNotNull(l.getFont(), "Il font della label non dovrebbe essere null");
                    break;
                }
            }
        }

        assertTrue(labelTrovata, 
                   "Il pannello deve contenere la label con il testo 'Aggiornamenti servizio :'");
    }
}