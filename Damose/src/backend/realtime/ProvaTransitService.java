package backend.service;

import backend.model.Fermata;
import backend.model.Linea;
import backend.model.RisultatoLinea;

import java.io.IOException;
import java.util.List;

public class ProvaTransitService {

    public static void main(String[] args) {
        try {
            // Crea il service completo con GTFS aggiornati
            TransitServiceImpl service = TransitServiceImpl.createDefault();

            // Scegli un ID fermata reale (ad esempio una delle più note)
            String stopId = "79315"; 

            System.out.println("=== Test: trovaLineePerIdFermata(" + stopId + ") ===");
            List<RisultatoLinea> linee = service.trovaLineePerIdFermata(stopId);
            System.out.println("Trovate " + linee.size() + " linee");

            for (int i = 0; i < linee.size(); i++) {
                System.out.println("- " + linee.get(i));
            }
            
            System.out.println("\n=== Test: trovaLineePerNomeFermata('Tarantelli') ===");
            List<RisultatoLinea> linee2 = service.trovaLineePerNomeFermata("TARANTELLI");
            System.out.println("Trovate " + linee2.size() + " linee");

            for (int i = 0; i < linee2.size(); i++) {
                System.out.println("- " + linee2.get(i));
            }

            System.out.println("\n=== Test: cercaLinee('78') ===");
            service.cercaLinee("78").forEach(l ->
                System.out.println(l.getRouteId() + " (" + l.getDescription() + ")")
            );
            
            System.out.println("\n== Test: trovaFermatePerLinea");
            
            String routeId = "777";           
            String directionName = "AGRICOLTURA";

            System.out.println("=== TEST: trovaFermatePerLinea ===");
            System.out.println("Linea: " + routeId);
            System.out.println("Direzione: " + directionName + "\n");

            List<Fermata> fermate =
                    service.trovaFermatePerLinea(routeId, directionName);

            if (fermate.isEmpty()) {
                System.out.println("❌ Nessuna fermata trovata");
                System.out.println("⚠ Possibili cause:");
                System.out.println("- routeId errato");
                System.out.println("- linea senza corse");
                System.out.println("- dati GTFS inconsistenti");
                return;
            }

            System.out.println("✔ Fermate trovate: " + fermate.size() + "\n");

            int i = 1;
            for (Fermata f : fermate) {
                System.out.println(i++ + ") "
                    + f.getStopId()
                    + " - "
                    + f.getName()
                );
            }
           

            System.out.println("\n=== TEST COMPLETATO ===");

            System.out.println("\n=== Test: getFermataById('79315') ===");
            service.getFermataById("79315").ifPresentOrElse(
                f -> System.out.println("Fermata trovata: " + f),
                () -> System.out.println("Fermata non trovata.")
            );
            
            System.out.println("\n=== Test: ricercaGenerica('CADUTI LIBERAZIONE') ===");
            service.ricercaGenerica("").forEach(f ->
            System.out.println(f));
            
            System.out.println("\n=== Test: cercaFermate('TARANTELLI') ===");
            service.cercaFermate("TARANTELLI").forEach(f ->
                System.out.println(f.toString())
            );
            
            //System.out.println("\n=== Test: trovaLineePerNomeFermata('TARANTELLI') ===");
            //List<RisultatoLinea> altreLinee = service.trovaLineePerNomeFermata("TARANTELLI");
            //System.out.println("Trovate " + altreLinee.size() + " linee");

            //for (int i = 0; i < altreLinee.size(); i++) {
                //System.out.println("- " + altreLinee.get(i));
            //}
            
            //System.out.println("\n=== Test: trovaFermatePerLinea('777', 'BEATA VERGINE DEL CARMELO') ===");
            //List<RisultatoFermata> fermate = service.trovaFermatePerLinea("777", "BEATA VERGINE DEL CARMELO");
            //System.out.println("Trovate " + fermate.size() + " fermate");

            //for (int i = 0; i < fermate.size(); i++) {
                //System.out.println("- " + fermate.get(i));
            //}
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

