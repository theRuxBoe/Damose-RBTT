package backend.realtime;

import java.util.Map;
import java.util.OptionalLong;

public class TripUpdateInfo {
	
	private final String tripId;
	private final String routeId;
	private final long timestamp;
	private final boolean cancelled;
	private final Map<Integer, Integer> delayByStopSequence;
	private final Map<String, Integer> delayByStopId;
	private final Map<Integer, Long> predictedEpochByStopSequence; //stopSequence -> orario previsto aggiornato
	private final Map<String, Long> predictedEpochByStopId; //stopId -> orario previsto aggiornato
	
	public TripUpdateInfo(String tId, String rId, long ts, boolean canc, Map<Integer, Integer> delss, Map<String, Integer> delsId, Map<Integer, Long> predEpSeq, Map<String, Long> predEpStop) {
		
		this.tripId = tId;
		this.routeId = rId;
		this.timestamp = ts;
		this.cancelled = canc;
		this.delayByStopSequence = Map.copyOf(delss);
		this.delayByStopId = Map.copyOf(delsId);
		this.predictedEpochByStopSequence = Map.copyOf(predEpSeq);
		this.predictedEpochByStopId = Map.copyOf(predEpStop);
		
	}

	public String getTripId() {
		return this.tripId;
	}

	public String getRouteId() {
		return this.routeId;
	}

	public long getTimestamp() {
		return this.timestamp;
	}

	public boolean isCancelled() {
		return this.cancelled;
	}

	public Map<Integer, Integer> getDelayByStopSequence() {
		return this.delayByStopSequence;
	}

	public Map<String, Integer> getDelayByStopId() {
		return this.delayByStopId;
	}

	public OptionalLong getPredictedEpochSecondsByStopSequence(int stopSequence) {
		
		return predictedEpochByStopSequence.get(stopSequence) == null ? OptionalLong.empty() : OptionalLong.of(predictedEpochByStopSequence.get(stopSequence));
	}
	
	public OptionalLong getPredictedEpochSecondsByStopId(String stopId) {
		
		return predictedEpochByStopId.get(stopId) == null ? OptionalLong.empty() : OptionalLong.of(predictedEpochByStopId.get(stopId));
	}

}
