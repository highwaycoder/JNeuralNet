class MapSimulation {
	
	static final int DEFAULT_XSIZE = 100;
	static final int DEFAULT_YSIZE = 100;
	
	Item[] items;
	
	int xSize;
	int ySize;
	
	
	
	MapSimulation() {
		
	}
	
	MapSimulation(int x, int y) {
		size_x = x;
		size_y = y;
	}
	
	int[] getEmptySquare() {
		int[] rv;
		for(int i=0;i<items.length;i++) {
			if(items[i]==I_EMPTY) {
				// X coordinate is stored in coords[0]
				rv[0] = i%size_x;
				// Y coordinate is stored in coords[1]
				rv[1] = i/size_x;
			}
		}
	}
}
