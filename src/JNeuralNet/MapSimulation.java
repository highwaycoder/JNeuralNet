package JNeuralNet;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Random;

public class MapSimulation {
	
	static final int DEFAULT_XSIZE = 100;
	static final int DEFAULT_YSIZE = 100;
	static final int DEFAULT_NUMOFSEEKABLES = 100;
	static final int GRID_XSIZE = 4;
	static final int GRID_YSIZE = 4;
	
	//Item[] items;
	ArrayList<MapItem> mapItems;
	int xSize;
	int ySize;
	
	public MapSimulation(int x, int y, int numOfSeekables, EnumSet<ItemType> seekables) {
		xSize = x;
		ySize = y;
		mapItems = new ArrayList<MapItem>();
		for(int i=0;i<x*y;i++) {
			// if at edge of map, wall!
			if(i%xSize==0 || i/xSize==0 || i%xSize==xSize-1 || i/xSize==ySize-1) {
				mapItems.add(new MapItem(new int[] { i%xSize, i/xSize }, EnumSet.of(ItemType.WALL)));
			}
		}
		for(int i=0;i<numOfSeekables;i++) {
			spawnSeekable(seekables);
		}
	}
	
	public MapSimulation(int numOfSeekables, EnumSet<ItemType> seekables) {
		this(DEFAULT_XSIZE,DEFAULT_YSIZE,numOfSeekables,seekables);
	}
	
	public MapSimulation(EnumSet<ItemType> seekables) {
		this(DEFAULT_NUMOFSEEKABLES,seekables);
	}
	
	public void spawnSeekable(EnumSet<ItemType> seekables) {
		int[] coordinate = getEmptySquare();
		Random r = new Random();
		try {
		// TODO SO MUCH KLUDGE!!! PLEASE FIXME ASAP!!
		mapItems.add(new MapItem(coordinate, EnumSet.of((ItemType)seekables.toArray()[r.nextInt(seekables.size())])));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public int[] getEmptySquare() {
		int[] rv = new int[3];
		Random r = new Random();
		int i = r.nextInt(mapItems.size());
		while(mapItems.get(i).isEmpty()) {
			// X coordinate is stored in coords[0]
			rv[0] = i%xSize;
			// Y coordinate is stored in coords[1]
			rv[1] = i/xSize; // don't try to be clever and change this to ySize, it will break!
			rv[2] = i;
			// try again next time if we don't succeed
			i = r.nextInt(mapItems.size());
		}
		return rv;
	} // int[] getEmptySquare()
	
	public boolean isSquareEmpty(int[] coord) throws Exception {
		for(int i=0;i<mapItems.size();i++)
			if(mapItems.get(i).getCoords()==coord)
				return mapItems.get(i).isEmpty();
		throw new Exception("Array index out of bounds");
	}

	public MapItem getItemAt(int[] coords) {
		return mapItems.get((coords[0]*xSize) + coords[1]);
	}
	
	public void setItemAt(int[] coords,EnumSet<ItemType> t) {
		MapItem to = new MapItem(coords,t);
		mapItems.set((coords[0]*xSize) + coords[1],to); 
	}

	public short getClosestWall(int[] coords, double heading) {
		int[] c = coords;
		while(c[0]*c[1] <= mapItems.size() && c[0]*c[1] >= 0 && (mapItems.get(c[0]*c[1]).isEmpty())) { // while is empty (remove double negative, woot!)
			c[0] += (int)Math.floor(Math.cos(heading));
			c[1] += (int)Math.floor(Math.sin(heading));
		}
		// return the integer from the function sqrt(a²+b²) where a and b are the lengths of the vectors between coords and coords+c[0], and
		// between coords and coords+c[1].  The result is the hypotenuse, according to Pythagorus' theorum.
		// Since this is a rather long line, I felt it deserved a rather long explanation.
		return (short)Math.floor(Math.hypot((c[0]-coords[0]),(c[1]-coords[1])));  // line shortened using Math.hypot()
	}

	public short getClosestSeekable(EnumSet<ItemType> seeking, int[] coords, double direction) {
		// TODO: make sure we're only getting the closest seekable, not just the first one we hit, also check seekable is in the right 'direction'
		// initialise rv as being the largest possible value, so we don't end up returning 0 if we don't find any seekables
		short rv = (short)mapItems.size();
		for(int i=0;i<mapItems.size();i++) {
			if(mapItems.get(i).item.containsAll(seeking)) { // if we have a landmine
				rv = (short)Math.floor(Math.hypot(coords[0]-mapItems.get(i).getCoords()[0],coords[1]-mapItems.get(i).getCoords()[1]));
				break; // break out of the for() loop
			}
		}
		return rv;
	}
}
