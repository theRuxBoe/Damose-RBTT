package main.java.frontend.waypoints;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.java.backend.model.RouteType;

/**
 * A factory for creating {@link BufferedImage}s for
 * vehicles to show on the map.
 */
public class VehicleImgFactory {

	/** The image for a bus. */
	private static BufferedImage busImg;

	/** The image for a tram. */
	private static BufferedImage tramImg;

	/** The image for a train. */
	private static BufferedImage trainImg;

	/** The image for the metro train. */
	private static BufferedImage metroImg;

	/**
	 * Selects the image for a specific vehicle type.
	 *
	 * @param type the vehicle type
	 * @return the buffered image to be painted on the map
	 */
	public BufferedImage selectImage(RouteType type) {

		BufferedImage x;
		if (type == null) {return null;}
		switch (type) {
		case TRAM:
			if (tramImg == null) {
				try {
					tramImg = ImageIO.read(WaypointManager.class.getResource("/main/res/waypoints/tram.png"));
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
			x = tramImg;
			break;
		case METRO:
			if (metroImg == null) {
				try {
					metroImg = ImageIO.read(WaypointManager.class.getResource("/main/res/waypoints/metro.png"));
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
			x = metroImg;
			break;
		case TRAIN:
			if (trainImg == null) {
				try {
					trainImg = ImageIO.read(WaypointManager.class.getResource("/main/res/waypoints/train.png"));
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
			x = trainImg;
			break;
		case BUS:
			if (busImg == null) {
				try {
					busImg = ImageIO.read(WaypointManager.class.getResource("/main/res/waypoints/bus.png"));
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
			x = busImg;

			break;

		default:
			x = null;
			break;
		}
		return x;
	}
}
