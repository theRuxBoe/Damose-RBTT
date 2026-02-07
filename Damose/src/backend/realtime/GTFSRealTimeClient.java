package backend.realtime;

import com.google.transit.realtime.GtfsRealtime.FeedMessage;
import com.google.transit.realtime.GtfsRealtime.Position;
import com.google.transit.realtime.GtfsRealtime.TranslatedString;
import com.google.transit.realtime.GtfsRealtime.Alert;
import com.google.transit.realtime.GtfsRealtime.EntitySelector;
import com.google.transit.realtime.GtfsRealtime.FeedEntity;
import com.google.transit.realtime.GtfsRealtime.TripUpdate;
import com.google.transit.realtime.GtfsRealtime.TripUpdate.StopTimeUpdate;
import com.google.transit.realtime.GtfsRealtime.VehicleDescriptor;
import com.google.transit.realtime.GtfsRealtime.VehiclePosition;
import com.google.transit.realtime.GtfsRealtime.TripDescriptor;
import com.google.protobuf.InvalidProtocolBufferException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GTFSRealTimeClient {
	
	private RealtimeSnapshot lastSnapshot = null;
	private long lastFetchTime = 0;  // in millisecondi
	private static final long CACHE_TTL_MS = 30_000; // 30 secondi
    private final int connectionTimeoutMs = 5_000; // 5 secondi
    private final int readTimeoutMs = 15_000; // 15 secondi
    private final String feedUrl;

    public GTFSRealTimeClient(String feedUrl) {
        this.feedUrl = feedUrl;
    }

    private byte[] downloadFeed() throws IOException {
        URL url = new URL(feedUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(connectionTimeoutMs);
        conn.setReadTimeout(readTimeoutMs);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);

        int code = conn.getResponseCode();
        if (code != HttpURLConnection.HTTP_OK) {
            conn.disconnect();
            throw new IOException("Errore HTTP " + code + " scaricando " + feedUrl);
        }

        try (InputStream in = conn.getInputStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[8192];
            int r;
            while ((r = in.read(buffer)) != -1) {
                baos.write(buffer, 0, r);
            }
            return baos.toByteArray();
        } finally {
            conn.disconnect();
        }
    }

    private FeedMessage parseFeed(byte[] raw) throws IOException {
        try {
            return FeedMessage.parseFrom(raw);
        } catch (InvalidProtocolBufferException e) {
            throw new IOException("Errore parsing protobuf GTFS-RT", e);
        }
    }
 
    private TripUpdateInfo parseTripUpdate(FeedEntity entity, long feedTimestampMillis) {
        if (!entity.hasTripUpdate()) return null;
        TripUpdate tu = entity.getTripUpdate();

        TripDescriptor td = tu.hasTrip() ? tu.getTrip() : TripDescriptor.getDefaultInstance();
        String tripId = td.hasTripId() ? td.getTripId() : "";
        String routeId = td.hasRouteId() ? td.getRouteId() : "";

        boolean cancelled = td.hasScheduleRelationship()
                && (td.getScheduleRelationship() == TripDescriptor.ScheduleRelationship.CANCELED);

        Map<Integer, Integer> delaysBySequence = new HashMap<Integer, Integer>();
        Map<String, Integer> delaysByStopId = new HashMap<String, Integer>();
        Map<Integer, Long> predictedTimesByStopSequence = new HashMap<Integer, Long>();
        Map<String, Long> predictedTimesByStopId = new HashMap<String, Long>();

        for (StopTimeUpdate stu : tu.getStopTimeUpdateList()) {
            if (stu.hasScheduleRelationship() && stu.getScheduleRelationship() == StopTimeUpdate.ScheduleRelationship.SKIPPED) {
                continue;
            }

            Integer stopSequence = stu.hasStopSequence() ? Integer.valueOf(stu.getStopSequence()) : null;
            String stopId = stu.hasStopId() ? stu.getStopId() : null;


            Integer delaySeconds = null;
            if (stu.hasArrival() && stu.getArrival().hasDelay()) {
                delaySeconds = (int) stu.getArrival().getDelay();
            } else if (stu.hasDeparture() && stu.getDeparture().hasDelay()) {
                delaySeconds = (int) stu.getDeparture().getDelay();
            }

            if (delaySeconds != null) {
                if (stopSequence != null) delaysBySequence.put(stopSequence, delaySeconds);
                if (stopId != null && !stopId.isBlank()) delaysByStopId.put(stopId, delaySeconds);
            } else {
                //se non c'è delay, può esserci arrival.time o departure.time (epoch seconds)
                if (stu.hasArrival() && stu.getArrival().hasTime()) {
                    long epoch = stu.getArrival().getTime();
                    predictedTimesByStopSequence.put(stopSequence != null ? stopSequence : -1, epoch);
                    predictedTimesByStopId.put(stopId != null ? stopId : "", epoch);
                } else if (stu.hasDeparture() && stu.getDeparture().hasTime()) {
                    long epoch = stu.getDeparture().getTime();
                    predictedTimesByStopSequence.put(stopSequence != null ? stopSequence : -1, epoch);
                    predictedTimesByStopId.put(stopId != null ? stopId : "", epoch);
                }
            }
        }

        //rende immutabili le mappe interne
        return new TripUpdateInfo(tripId, routeId, feedTimestampMillis, cancelled, Map.copyOf(delaysBySequence), Map.copyOf(delaysByStopId), Map.copyOf(predictedTimesByStopSequence), Map.copyOf(predictedTimesByStopId));
    }
    
    private VehiclePositionInfo parseVehiclePosition(FeedEntity entity) {
    	if (!entity.hasVehicle()) return null;
    	VehiclePosition vp = entity.getVehicle();
    	
    	if (!vp.hasTrip() || !vp.getTrip().hasTripId()) {
    	    return null;
    	}
    	
    	TripDescriptor td = vp.hasTrip() ? vp.getTrip() : TripDescriptor.getDefaultInstance();
        String tripId = td.hasTripId() ? td.getTripId() : null;
        
        VehicleDescriptor vd = vp.hasVehicle() ? vp.getVehicle() : null;
        String vehicleId = (vd != null && vd.hasId()) ? vd.getId() : "";
        String vehicleLabel = (vd != null && vd.hasLabel()) ? vd.getLabel() : "";
        
        Position pos = vp.hasPosition() ? vp.getPosition() : null;
        double lat = (pos != null && pos.hasLatitude()) ? pos.getLatitude() : Double.NaN;
        double lon = (pos != null && pos.hasLongitude()) ? pos.getLongitude() : Double.NaN;
        Double bearing = (pos != null && pos.hasBearing()) ? Double.valueOf(pos.getBearing()) : null;
        Double speed = (pos!= null && pos.hasSpeed()) ? Double.valueOf(pos.getSpeed()) : null;
        
        Long timestamp = vp.hasTimestamp() ? vp.getTimestamp() : null;
        Integer currentStopSequence = vp.hasCurrentStopSequence() ? vp.getCurrentStopSequence() : null;
        String currentStopId = vp.hasStopId() ? vp.getStopId() : null;
        
        return new VehiclePositionInfo(vehicleId, vehicleLabel, tripId, lat, lon, bearing, speed, timestamp, currentStopSequence, currentStopId);

    }
    
    private ServiceAlertInfo parseAlert(FeedEntity entity) {
    	
    	if (!entity.hasAlert()) return null;
    	Alert a = entity.getAlert();
    	
    	String alertId = entity.hasId() ? entity.getId() : null;
    	
    	String header = "";
    	TranslatedString hts = a.getHeaderText();
    	if (hts.getTranslationCount() > 0) {
    		
    		header = hts.getTranslation(0).getText();
    	}
    	
    	String description = "";
    	TranslatedString dts = a.getDescriptionText();
    	if (dts.getTranslationCount() > 0) {
    		
    		description = dts.getTranslation(0).getText();
    	}
    	
    	Long startTime = null;
    	Long endTime = null;
    	
    	    if (a.getActivePeriodCount() > 0) {
    	        for (var p : a.getActivePeriodList()) {
    	            if (p.hasStart()) {
    	                long s = p.getStart();
    	                if (startTime == null || s < startTime) startTime = s;
    	            }
    	            if (p.hasEnd()) {
    	                long e = p.getEnd();
    	                if (endTime == null || e > endTime) endTime = e;
    	            }
    	        }
    	    }
    	
    	List<String> tripIds = new ArrayList<String>();
    	List<String> routeIds = new ArrayList<String>();
    	List<String> stopIds = new ArrayList<String>();
    	
    	
    	for (EntitySelector sel : a.getInformedEntityList()) {
            if (sel.hasRouteId()) routeIds.add(sel.getRouteId());
            if (sel.hasTrip() && sel.getTrip().hasTripId()) tripIds.add(sel.getTrip().getTripId());
            if (sel.hasStopId()) stopIds.add(sel.getStopId()); 
            
    	}
    	
    	return new ServiceAlertInfo(alertId, header, description, startTime, endTime, routeIds, tripIds, stopIds);
    	
    }

    private RealtimeSnapshot buildSnapshot(FeedMessage msg) {
        Map<String, Map<Integer, Integer>> delayByTripAndSequence = new HashMap<>();
        Map<String, Map<String, Integer>> delayByTripAndStopId = new HashMap<>();
        Map<String, TripUpdateInfo> tripUpdateInfoMap = new HashMap<>();
        Map<String, VehiclePositionInfo> vehiclePositionByTripId = new HashMap<>();
        Map<String, VehiclePositionInfo> vehiclePositionByVehicleId = new HashMap<>(); // fallback
        List<ServiceAlertInfo> alerts = new ArrayList<ServiceAlertInfo>();
        
        long headerMillis = System.currentTimeMillis();
        if (msg.hasHeader() && msg.getHeader().hasTimestamp()) {
            headerMillis = msg.getHeader().getTimestamp() * 1000L;
        }

        for (FeedEntity e : msg.getEntityList()) {
            TripUpdateInfo info = parseTripUpdate(e, headerMillis);
            if (info == null) continue;
            String tId = info.getTripId();
            
            //evita tripId vuoto: se vuoto, può generare chiave diversa o saltare
            tripUpdateInfoMap.put(tId, info);
            if (!info.getDelayByStopSequence().isEmpty()) {
                delayByTripAndSequence.put(tId, info.getDelayByStopSequence());
            }
            if (!info.getDelayByStopId().isEmpty()) {
                delayByTripAndStopId.put(tId, info.getDelayByStopId());
            }
            
            VehiclePositionInfo vpi = parseVehiclePosition(e);
            if (vpi != null) {
                if (vpi.getTripId() != null && !vpi.getTripId().isBlank()) {
                    vehiclePositionByTripId.put(vpi.getTripId(), vpi);
                } else if (vpi.getVehicleId() != null && !vpi.getVehicleId().isBlank()) {
                    // fallback SOLO se non ho tripId
                    vehiclePositionByVehicleId.put(vpi.getVehicleId(), vpi);
                }
            }
            
            ServiceAlertInfo sai = parseAlert(e);
            if (sai != null) {
            	
            	alerts.add(sai);
            }
        }

        return new RealtimeSnapshot(delayByTripAndSequence, delayByTripAndStopId, tripUpdateInfoMap, vehiclePositionByTripId, vehiclePositionByVehicleId, alerts);
    }
    
    //controllo attivo per verificare se i server sono raggiungibili in questo momento
    public synchronized boolean checkConnectivity() {

        HttpURLConnection conn = null;
        try {
            URL url = new URL(feedUrl);
            conn = (HttpURLConnection) url.openConnection();

            // timeout MOLTO brevi
            conn.setConnectTimeout(2000);
            conn.setReadTimeout(3000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            int code = conn.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                return false;
            }

            try (InputStream in = conn.getInputStream()) {
                byte[] buffer = new byte[16];
                int read = in.read(buffer);
                return read > 0;
            }

        } catch (IOException e) {
            return false;
        } finally {
            if (conn != null) conn.disconnect();
        }
    }
    
    //metodo pubblico completo
    public synchronized RealtimeSnapshot fetchRealtime() throws IOException {
    	
    	long now = System.currentTimeMillis();
    	
    	//chiamata al server limitata a una volta ogni 30 secondi
    	if (lastSnapshot != null && (now - lastFetchTime) < CACHE_TTL_MS) {
            return lastSnapshot;
        }
    	
    	//altrimenti aggiorno lo snapshot
        byte[] raw = downloadFeed();
        FeedMessage msg = parseFeed(raw);
        RealtimeSnapshot snapshot = buildSnapshot(msg);
        
        lastFetchTime = now;
        lastSnapshot = snapshot;
        
        return snapshot;
    }
}