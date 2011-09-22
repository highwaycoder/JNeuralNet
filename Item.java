package JNeuralNet;

public enum Item {
	EMPTY 		(0),
	WALL 		(1),
	LANDMINE 	(2),
	ROBOT		(4);
	
	int flags;
	
	Item(int f){
		this.flags = f;
	}
	void setFlag(int f) {
		flags |= f;
	}
	void setFlag(Item i) {
		setFlag(i.flags);
	}
	void removeFlag(int f) {
		flags &= ~f;
	}
	void removeFlag(Item i) {
		removeFlag(i.flags);
	}
	boolean isFlagSet(int f) {
		return ((flags & f)!=0)?true:false;
	}
	boolean isFlagSet(Item i) {
		return isFlagSet(i.flags);
	}
}
