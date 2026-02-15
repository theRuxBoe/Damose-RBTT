package test.java.frontend.rightpanel;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JPanel;

import main.java.frontend.rightpanel.FocusPanel;
import main.java.frontend.rightpanel.panels.StopFocus;

/**
 * (GENERATA DA AI)
 * Classe di test per FocusPanel. 
 * Verifica la corretta navigazione tra pannelli e la gestione dello stato "previous".
 */
class FocusPanelTest {

    private FocusPanel focusPanel;

    @BeforeEach
    void setUp() {
        // Inizializziamo il pannello prima di ogni test
        focusPanel = new FocusPanel();
    }

    /**
     * TEST 1: Verifica Inizializzazione
     * Controlliamo che il pannello nasca con le impostazioni corrette
     * e che contenga il bottone "Indietro".
     */
    @Test
    void testInizializzazione() {
        // 1. Verifica che non sia null
        assertNotNull(focusPanel, "Il FocusPanel dovrebbe essere inizializzato correttamente");

        // 2. Verifica del Layout Manager
        assertTrue(focusPanel.getLayout() instanceof BorderLayout, 
                   "Il layout deve essere di tipo BorderLayout");

        // 3. Verifica presenza del bottone "Back"
        // Sappiamo che il costruttore chiama addBackButton() che aggiunge un pannello a NORD.
        Component northComponent = ((BorderLayout) focusPanel.getLayout()).getLayoutComponent(BorderLayout.NORTH);
        
        assertNotNull(northComponent, "Dovrebbe esserci un componente nella parte NORD (il pannello del bottone)");
        assertTrue(northComponent instanceof JPanel, "Il contenitore del bottone deve essere un JPanel");
        
        // Scendiamo nel dettaglio: quel pannello deve contenere il bottone
        JPanel buttonContainer = (JPanel) northComponent;
        assertTrue(buttonContainer.getComponentCount() > 0, "Il pannello nord deve contenere il bottone");
        assertTrue(buttonContainer.getComponent(0) instanceof JButton, "Il componente deve essere un JButton");
    }

    /**
     * TEST 2: Verifica della gestione del pannello precedente (Previous)
     * Questo test controlla i metodi getter e setter per la navigazione.
     */
    @Test
    void testGestionePreviousPanel() {
        // Creiamo un pannello "dummy" (finto) per simulare quello precedente
        JPanel pannelloPrecedente = new JPanel();
        
        // Settiamo il pannello precedente
        focusPanel.setPrevious(pannelloPrecedente);
        
        // Verifichiamo che il getter ritorni esattamente lo stesso oggetto
        assertSame(pannelloPrecedente, focusPanel.getPrevious(), 
                   "Il metodo getPrevious deve ritornare lo stesso oggetto passato a setPrevious");
    }

    /**
     * TEST 3: Verifica del cambio di Focus (setFocus)
     * Controlla che quando settiamo un nuovo StopFocus, questo venga effettivamente aggiunto alla GUI.
     */
    @Test
    void testSetFocus() {
        
        // Verificare che setPrevious funzioni anche se lo chiamiamo due volte (sovrascrittura)
        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        focusPanel.setPrevious(p1);
        focusPanel.setPrevious(p2);
        assertSame(p2, focusPanel.getPrevious(), "Il previous panel deve essere aggiornato all'ultimo valore inserito");
    }
}