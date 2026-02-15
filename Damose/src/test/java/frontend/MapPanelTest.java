package test.java.frontend;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;

import main.java.frontend.MapPanel;

/**
 * (GENERATA DA AI)
 */
class MapPanelTest {

    // Helper per verificare se siamo in ambiente senza monitor (es. CI/CD)
    boolean isHeadless() {
        return GraphicsEnvironment.isHeadless();
    }

    @Test
    @DisplayName("Inizializzazione corretta: Dimensioni, Layout e Viewer")
    @DisabledIf("isHeadless") // Salta se non c'è display grafico
    void testMapPanelInitialization() throws InterruptedException, InvocationTargetException {
        // Swing deve essere eseguito nell'Event Dispatch Thread
        SwingUtilities.invokeAndWait(() -> {
            MapPanel panel = new MapPanel();
            
            // 1. Verifica Dimensioni e Layout
            assertEquals(new Dimension(500, 500), panel.getPreferredSize(), "La dimensione preferita deve essere 500x500");
            assertTrue(panel.getLayout() instanceof BorderLayout, "Il layout deve essere BorderLayout");
            
            // 2. Verifica creazione JXMapViewer
            JXMapViewer viewer = panel.getMapViewer();
            assertNotNull(viewer, "Il JXMapViewer interno non deve essere null");
            
            // 3. Verifica che il viewer sia stato aggiunto al pannello
            assertEquals(viewer, panel.getComponent(0), "Il viewer deve essere figlio del pannello");
        });
    }

    @Test
    @DisplayName("Configurazione Geografica: Deve essere centrato su Roma")
    @DisabledIf("isHeadless")
    void testMapLocationAndZoom() throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(() -> {
            MapPanel panel = new MapPanel();
            JXMapViewer viewer = panel.getMapViewer();

            // Coordinate attese (Roma)
            double expectedLat = 41.890210;
            double expectedLon = 12.492231;
            GeoPosition center = viewer.getAddressLocation();

            // Usiamo una tolleranza (delta) per i double
            assertEquals(expectedLat, center.getLatitude(), 0.0001, "La latitudine deve corrispondere a Roma");
            assertEquals(expectedLon, center.getLongitude(), 0.0001, "La longitudine deve corrispondere a Roma");

            // Verifica Zoom
            assertEquals(5, viewer.getZoom(), "Il livello di zoom iniziale deve essere 5");
        });
    }

    @Test
    @DisplayName("Verifica Listeners: Zoom e Pan devono essere attivi")
    @DisabledIf("isHeadless")
    void testListenersAttached() throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(() -> {
            MapPanel panel = new MapPanel();
            JXMapViewer viewer = panel.getMapViewer();

            // Verifica MouseListeners (Click, Pan)
            MouseListener[] mouseListeners = viewer.getMouseListeners();
            assertTrue(mouseListeners.length > 0, "Devono esserci MouseListener per il panning");

            // Verifica MouseWheelListeners (Zoom)
            MouseWheelListener[] wheelListeners = viewer.getMouseWheelListeners();
            assertTrue(wheelListeners.length > 0, "Deve esserci un MouseWheelListener per lo zoom");

            // Verifica MouseMotionListeners (Trascinamento)
            MouseMotionListener[] motionListeners = viewer.getMouseMotionListeners();
            assertTrue(motionListeners.length > 0, "Devono esserci MouseMotionListener per il movimento");
        });
    }
    
    // --- EDGE CASES ---

    @Test
    @DisplayName("Edge Case: Creazione multipla (Stress Test)")
    @DisabledIf("isHeadless")
    void testMultipleInstantiations() throws InterruptedException, InvocationTargetException {
        // Verifica che la classe statica WaypointManager non esploda se chiamata più volte
        // dal costruttore di MapPanel
        SwingUtilities.invokeAndWait(() -> {
            assertDoesNotThrow(() -> {
                new MapPanel();
                new MapPanel(); 
                new MapPanel();
            }, "Istanziare più MapPanel non deve causare errori nei metodi statici di WaypointManager");
        });
    }
    
    @Test
    @DisplayName("Edge Case: Verifica TileFactory impostata")
    @DisabledIf("isHeadless")
    void testTileFactory() throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(() -> {
            MapPanel panel = new MapPanel();
            assertNotNull(panel.getMapViewer().getTileFactory(), "La TileFactory (OpenStreetMap) deve essere inizializzata");
        });
    }
}
