package JNeuralNet;

public class MapItem {
	int[] coord;
	ItemType item;
	
	MapItem(int[] coord, ItemType i) {
		this.coord = coord;
		// this is better than operator= (TODO: verify that I'm right in thinking this)
		item.type.addAll(i.type);
	}
	
	void acquired(ItemType t) {
		// remove it from our instance if it has been acquired
		item.type.remove(t);
	}
	boolean isEmpty() {
		return item.type.isEmpty();
	}
	int[] getCoords(){
		return coord;
	}
	ItemType getItem() {
		return item;
	}
}
