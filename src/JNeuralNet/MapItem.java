package JNeuralNet;

import java.util.EnumSet;

public class MapItem {
	int[] coord;
	EnumSet<ItemType> item;
	
	public MapItem(int[] coord, EnumSet<ItemType> i) {
		this.coord = coord;
		// this is better than operator= (TODO: verify that I'm right in thinking this)
		item = i;
	}
	
	public void acquired(EnumSet<ItemType> t) {
		// remove it from our instance if it has been acquired
		item.remove(t);
	}
	public boolean isEmpty() {
		return item.isEmpty();
	}
	public int[] getCoords(){
		return coord;
	}
	public EnumSet<ItemType> getItem() {
		return item;
	}
}
