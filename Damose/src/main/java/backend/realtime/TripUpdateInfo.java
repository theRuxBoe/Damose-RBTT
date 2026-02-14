package main.java.backend.realtime;

import java.util.Map;
import java.util.OptionalLong;

/**
 * The Class TripUpdateInfo -> represents a trip update object with its infos.
 */
public class TripUpdateInfo {
	
	/** The trip id. */
	private final String tripId;
	
	/** The route id. */
	private final String routeId;
	
	/** The timestamp. */
	private final long timestamp;
	
	/** The boolean which says if the trip is cancelled or not. */
	private final boolean cancelled;
	
	/** The delays indexed by stop sequence. */
	private final Map<Integer, Integer> delayByStopSequence;
	
	/** The delays indexed by stop id. */
	private final Map<String, Integer> delayByStopId;
	
	/** The predicted epochs (absolute expected trips' arrivals) indexed by stop sequence. */
	private final Map<Integer, Long> predictedEpochByStopSequence;
	
	/** The predicted epochs indexed by stop id. */
	private final Map<String, Long> predictedEpochByStopId;
	
	/**
	 * Instantiates a new trip update info.
	 *
	 * @param tId the trip id
	 * @param rId the route id
	 * @param ts the timestamp
	 * @param canc the boolean cancelled
	 * @param delss the delays by stop sequence
	 * @param delsId the delays by stop id
	 * @param predEpSeq the predicted epochs by stop sequence
	 * @param predEpStop the predicted epochs by stop id
	 */
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

	/**
	 * Gets the trip id.
	 *
	 * @return the trip id
	 */
	public String getTripId() {
		return this.tripId;
	}

	/**
	 * Gets the route id.
	 *
	 * @return the route id
	 */
	public String getRouteId() {
		return this.routeId;
	}

	/**
	 * Gets the timestamp.
	 *
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return this.timestamp;
	}

	/**
	 * Checks if the trip is cancelled.
	 *
	 * @return true, if is cancelled
	 */
	public boolean isCancelled() {
		return this.cancelled;
	}

	/**
	 * Gets the delays indexed by stop sequence.
	 *
	 * @return the delay by stop sequence
	 */
	public Map<Integer, Integer> getDelayByStopSequence() {
		return this.delayByStopSequence;
	}

	/**
	 * Gets the delays indexed by stop id.
	 *
	 * @return the delay by stop id
	 */
	public Map<String, Integer> getDelayByStopId() {
		return this.delayByStopId;
	}

	/**
	 * Gets the predicted epoch seconds indexed by stop sequence.
	 *
	 * @param stopSequence the stop sequence
	 * @return the predicted epoch seconds by stop sequence
	 */
	public OptionalLong getPredictedEpochSecondsByStopSequence(int stopSequence) {
		
		return predictedEpochByStopSequence.get(stopSequence) == null ? OptionalLong.empty() : OptionalLong.of(predictedEpochByStopSequence.get(stopSequence));
	}
	
	/**
	 * Gets the predicted epoch seconds indexed by stop id.
	 *
	 * @param stopId the stop id
	 * @return the predicted epoch seconds by stop id
	 */
	public OptionalLong getPredictedEpochSecondsByStopId(String stopId) {
		
		return predictedEpochByStopId.get(stopId) == null ? OptionalLong.empty() : OptionalLong.of(predictedEpochByStopId.get(stopId));
	}

}
