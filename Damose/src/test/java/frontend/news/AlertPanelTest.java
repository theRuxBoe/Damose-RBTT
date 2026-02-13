package test.frontend.news;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import main.java.backend.realtime.ServiceAlertInfo;
import main.java.frontend.news.AlertPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// Import statici per rendere il codice più leggibile
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class AlertPanelTest {

    // 1. Dichiariamo il mock (l'oggetto finto)
    private ServiceAlertInfo alertMock;

    @BeforeEach
    void setUp() {
        // 2. Creiamo il mock manualmente (funziona sempre, senza bisogno di estensioni)
        alertMock = Mockito.mock(ServiceAlertInfo.class);

        // 3. Istruiamo il mock su cosa rispondere quando vengono chiamati i suoi metodi
        when(alertMock.getHeader()).thenReturn("ATTENZIONE");
        when(alertMock.getDescription()).thenReturn("Guasto alla linea elettrica.");
        when(alertMock.getRouteIds()).thenReturn(List.of("10", "12/"));
    }

    @Test
    @DisplayName("Il pannello deve mostrare correttamente i dati dell'alert")
    void testPanelContent() {
        // --- ACT (Esecuzione) ---
        // Passiamo il mock al costruttore del pannello
        AlertPanel panel = new AlertPanel(alertMock);

        // --- ASSERT (Verifica) ---
        
        // A. Verifiche sulla struttura del pannello
        assertThat(panel.getLayout())
                .as("Il layout deve essere verticale (Y_AXIS)")
                .isInstanceOf(BoxLayout.class);

        // B. Recuperiamo la JTextArea (sappiamo che è il primo componente aggiunto)
        Component firstComponent = panel.getComponent(0);
        
        assertThat(firstComponent)
                .as("Il primo componente deve essere una JTextArea")
                .isInstanceOf(JTextArea.class);

        JTextArea textArea = (JTextArea) firstComponent;

        // C. Verifiche sul testo contenuto
        String testo = textArea.getText();
        
        assertThat(testo)
                .contains("ATTENZIONE")          // Controlla l'header
                .contains("Guasto alla linea")   // Controlla la descrizione
                .contains("[10, 12/]");          // Controlla la lista delle linee

        // D. Verifiche sulle proprietà grafiche
        assertThat(textArea.getLineWrap()).isTrue();
        assertThat(textArea.isFocusable()).isFalse();
    }
    
    @Test
    @DisplayName("Gestione dei valori nulli")
    void testNullValues() {
        // Setup: il mock restituisce null
        when(alertMock.getHeader()).thenReturn(null);
        when(alertMock.getDescription()).thenReturn(null);
        when(alertMock.getRouteIds()).thenReturn(java.util.Collections.emptyList()); // Lista vuota

        AlertPanel panel = new AlertPanel(alertMock);
        JTextArea textArea = (JTextArea) panel.getComponent(0);

        // Verifica: Attualmente il codice stamperà "null", vediamo se è vero
        assertThat(textArea.getText()).contains("null");
        
        // Oppure, se modifichi la classe per gestire i null, cambierai l'assert:
        // assertThat(textArea.getText()).doesNotContain("null");
    }
    
    @Test
    void testEmptyRoutes() {
        when(alertMock.getRouteIds()).thenReturn(List.of()); // Lista vuota
        
        AlertPanel panel = new AlertPanel(alertMock);
        JTextArea textArea = (JTextArea) panel.getComponent(0);
        
        assertThat(textArea.getText()).contains("Linee affette : []");
    }
}