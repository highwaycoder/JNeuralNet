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
	
	MapSimulation(int x, int y, int numOfSeekables, ItemType seekables) {
		xSize = x;
		ySize = y;
		mapItems = new ArrayList<MapItem>();
		for(int i=0;i<x*y;i++) {
			// if at edge of map, wall!
			if(i%xSize==0 || i/xSize==0 || i%xSize==xSize-1 || i/xSize==ySize-1) {
				mapItems.add(new MapItem(new int[] { i%xSize, i/xSize }, ItemType.WALL));
				//items[i].setFlag(Item.WALL);
			}
		}
		for(int i=0;i<numOfSeekables;i++) {
			spawnSeekable(seekables);
		}
	}
	
	MapSimulation(int numOfSeekables, ItemType seekables) {
		this(DEFAULT_XSIZE,DEFAULT_YSIZE,numOfSeekables,seekables);
	}
	
	MapSimulation(ItemType seekables) {
		this(DEFAULT_NUMOFSEEKABLES,seekables);
	}
	
	@SuppressWarnings("unchecked")
	void spawnSeekable(ItemType seekables) {
		int[] coordinate = getEmptySquare();
		Random r = new Random();
		try {
			// TODO SO MUCH KLUDGE!!! PLEASE FIXME ASAP!!
		mapItems.add(new MapItem(coordinate,(ItemType)new ArrayList<Enum>(seekables.type).get(r.nextInt(seekables.type.size())))); // five closing parens in a row? must be a record...
		} catch (Exception e) {
			// output a warning
			System.err.println("Warning: spawnSeekable() has been called when the robot is not seeking anything.");
			// if we output this we know that we are at best inefficient, at worst broken.
		}
	}
	
	int[] getEmptySquare() {
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
	
	boolean isSquareEmpty(int[] coord) throws Exception {
		for(int i=0;i<mapItems.size();i++)
			if(mapItems.get(i).getCoords()==coord)
				return mapItems.get(i).isEmpty();
		throw new Exception("Array index out of bounds");
	}

	MapItem getItemAt(int[] coords) {
		return mapItems.get((coords[0]*xSize) + coords[1]);
	}
	
	void setItemAt(int[] coords,ItemType t) {
		MapItem to = new MapItem(coords,t);
		mapItems.set((coords[0]*xSize) + coords[1],to); 
	}

	short getClosestWall(int[] coords, double heading) {
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

	short getClosestSeekable(ItemType seeking, int[] coords, double direction) {
		// TODO: make sure we're only getting the closest seekable, not just the first one we hit, also check seekable is in the right 'direction'
		// initialise rv as being the largest possible value, so we don't end up returning 0 if we don't find any seekables
		short rv = (short)mapItems.size();
		for(int i=0;i<mapItems.size();i++) {
			if(mapItems.get(i).item.type.containsAll(seeking.type)) { // if we have a landmine
				rv = (short)Math.floor(Math.hypot(coords[0]-mapItems.get(i).getCoords()[0],coords[1]-mapItems.get(i).getCoords()[1]));
				break; // break out of the for() loop
			}
		}
		return rv;
	}
	
	/* commenting out to re-implement (above)
	short getClosestSeekable(Item seeking, int[] coords, double direction) {
		int[] c = coords;
		int distanceChecked = 0;
		// sanitise "direction":
		while(direction < 0.0) direction += 2*Math.PI;
		while(direction > 2*Math.PI) direction -= 2*Math.PI;
		// the first two clauses are to protect the mapItems array from being molested with inappropriate values
		while(c[0]*c[1] <= mapItems.size() && c[0]*c[1] >= 0 && (mapItems.get(c[0]*c[1]).getItem().getFlags() & seeking.getFlags())==0) {
				c[0] += (int)Math.floor(Math.cos(direction));
				c[1] += (int)Math.floor(Math.sin(direction));
				// reason for infinite loop: there might not actually be a seekable directly ahead
				// sanity check: stop after mapItem.size() tries
				// NOTE: this sanity check does not take into account the fact that the robot can only see from himself to the map's edge
				// it assumes the robot will be looking from one side of the map to the other, it's inefficient and should be fixed (later)
				if(++distanceChecked >= mapItems.size()) break; 
				else continue;
		}
		if(c[0]*c[1] > mapItems.size() || c[0]*c[1] < 0) {
			System.err.println("Warning: sensor value out of range: "+c[0]*c[1]);
		}
		// return the integer from the function sqrt(a²+b²) where a and b are the lengths of the vectors between coords and coords+c[0], and
		// between coords and coords+c[1].  The result is the hypotenuse, according to Pythagorus' theorum.
		// Since this is a rather long line, I felt it deserved a rather long explanation.
		return (short)Math.floor(Math.hypot((c[0]-coords[0]),(c[1]-coords[1])));  // line shortened using Math.hypot()
	}
	*/
	public void draw() {
		for(int i=0; i<mapItems.size(); i++) {
			drawItem(mapItems.get(i).item,mapItems.get(i).coord[0],mapItems.get(i).coord[1]);
		}
	}
	public void drawItem(ItemType item, int CentreX, int CentreY) {
		
		
	}
}
