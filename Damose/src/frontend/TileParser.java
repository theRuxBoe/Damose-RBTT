package frontend;

import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.TileFactoryInfo;

public class TileParser {
	
	public static void main(String[] args) {
		TileFactoryInfo info = new OSMTileFactoryInfo("OpenStreetMap", "https://tile.openstreetmap.org");
		DefaultTileFactory tileFactory = new DefaultTileFactory(info);
		
//		tileFactory.run();
		
	}
}
