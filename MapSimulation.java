package JNeuralNet;

import java.util.ArrayList;
import java.util.Random;

class MapSimulation {
	
	static final int DEFAULT_XSIZE = 100;
	static final int DEFAULT_YSIZE = 100;
	static final int DEFAULT_NUMOFSEEKABLES = 100;
	
	//Item[] items;
	ArrayList<MapItem> mapItems;
	int xSize;
	int ySize;
	
	MapSimulation(int x, int y, int numOfSeekables, Item[] seekables) {
		xSize = x;
		ySize = y;
		mapItems = new ArrayList<MapItem>();
		for(int i=0;i<x*y;i++) {
			// if at edge of map, wall!
			if(i%xSize==0 || i/xSize==0 || i%xSize==xSize-1 || i/xSize==ySize-1) {
				mapItems.add(new MapItem(new int[] { i%xSize, i/xSize }, Item.WALL));
				//items[i].setFlag(Item.WALL);
			}
		}
		for(int i=0;i<numOfSeekables;i++) {
			int[] coordinate = getEmptySquare();
			mapItems.add(new MapItem(coordinate, seekables[new Random().nextInt(seekables.length)]));
			//items[coordinate[2]].setFlag(seekables[new Random().nextInt(seekables.length)]);
		}
	}
	
	MapSimulation(int numOfSeekables, Item[] seekables) {
		this(DEFAULT_XSIZE,DEFAULT_YSIZE,numOfSeekables,seekables);
	}
	
	MapSimulation(Item[] seekables) {
		this(DEFAULT_NUMOFSEEKABLES,seekables);
	}
	
	int[] getEmptySquare() {
		int[] rv = new int[3];
		Random r = new Random();
		int i = r.nextInt(xSize * ySize);
		while(mapItems.get(i).isEmpty()) {
			// X coordinate is stored in coords[0]
			rv[0] = i%xSize;
			// Y coordinate is stored in coords[1]
			rv[1] = i/xSize; // don't try to be clever and change this to ySize, it will break!
			rv[2] = i;
			// try again next time if we don't succeed
			i = r.nextInt(xSize * ySize);
		}
		return rv;
	} // int[] getEmptySquare()
	
	boolean isSquareEmpty(int[] coord) {
		
	}
	}

	Item getItemAt(int[] coords) {
		return mapItems.get((coords[0]*xSize) + coords[1]).getItem();
	}

	short getClosestWall(int[] coords, double heading) {
		int[] c = coords;
		while(mapItems.get(c[0]*c[1]).isEmpty()) { // while is empty (remove double negative, woot!)
			c[0] += Math.cos(heading);
			c[1] += Math.sin(heading);
		}
		return (short)Math.floor(Math.sqrt((c[0]-coords[0])*(c[0]-coords[0]) + (c[1]-coords[1])*(c[1]-coords[1])));
	}

	short getClosestSeekable(Item[] seeking, int[] coords, double direction) {
		int[] c = coords;
		while(mapItems.get(c[0]*c[1]).isEmpty()) { // while is empty (remove double negative, woot!)
			c[0] += Math.cos(direction);
			c[1] += Math.sin(direction);	
		}
		
	}
	
}
