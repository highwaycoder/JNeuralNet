package JNeuralNet;

public class MapItem {
	int[] coord;
	Item item;
	
	MapItem(int[] coord, Item i) {
		this.coord = coord;
		item = i;
	}
	
	void acquired(Item type) {
		item.removeFlag(type);
	}
	boolean isEmpty() {
		return item.flags == Item.EMPTY.flags;
	}
	int[] getCoords(){
		return coord;
	}
	Item getItem() {
		return item;
	}
}
