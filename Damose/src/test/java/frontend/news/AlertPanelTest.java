package test.java.frontend.news;
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

/**
 * (GENERATA DA AI)
 */
class AlertPanelTest {

    private ServiceAlertInfo alertMock;

    @BeforeEach
    void setUp() {
        alertMock = Mockito.mock(ServiceAlertInfo.class);

        when(alertMock.getHeader()).thenReturn("ATTENZIONE");
        when(alertMock.getDescription()).thenReturn("Guasto alla linea elettrica.");
        when(alertMock.getRouteIds()).thenReturn(List.of("10", "12/"));
    }

    @Test
    @DisplayName("Il pannello deve mostrare correttamente i dati dell'alert")
    void testPanelContent() {
        AlertPanel panel = new AlertPanel(alertMock);

        assertThat(panel.getLayout())
                .as("Il layout deve essere verticale (Y_AXIS)")
                .isInstanceOf(BoxLayout.class);

        Component firstComponent = panel.getComponent(0);
        
        assertThat(firstComponent)
                .as("Il primo componente deve essere una JTextArea")
                .isInstanceOf(JTextArea.class);

        JTextArea textArea = (JTextArea) firstComponent;

        String testo = textArea.getText();
        
        assertThat(testo)
                .contains("ATTENZIONE")         
                .contains("Guasto alla linea")   
                .contains("[10, 12/]");          

        assertThat(textArea.getLineWrap()).isTrue();
        assertThat(textArea.isFocusable()).isFalse();
    }
    
    @Test
    @DisplayName("Gestione dei valori nulli")
    void testNullValues() {
        when(alertMock.getHeader()).thenReturn(null);
        when(alertMock.getDescription()).thenReturn(null);
        when(alertMock.getRouteIds()).thenReturn(java.util.Collections.emptyList()); // Lista vuota

        AlertPanel panel = new AlertPanel(alertMock);
        JTextArea textArea = (JTextArea) panel.getComponent(0);

        assertThat(textArea.getText()).contains("null");
        
    }
    
    @Test
    void testEmptyRoutes() {
        when(alertMock.getRouteIds()).thenReturn(List.of()); // Lista vuota
        
        AlertPanel panel = new AlertPanel(alertMock);
        JTextArea textArea = (JTextArea) panel.getComponent(0);
        
        assertThat(textArea.getText()).contains("Linee affette : []");
    }
}