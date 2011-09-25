package JNeuralNet;

import java.util.Random;

public enum Item {
	EMPTY 		(0),
	WALL 		(1),
	LANDMINE 	(2),
	ROBOT		(4);
	
	int flags;
	
	Item(int f){
		this.flags = f;
	}
	Item setFlag(int f) {
		flags |= f;
		return this;
	}
	Item setFlag(Item i) {
		setFlag(i.flags);
		return this;
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
	int getFlags(){
		return flags;
	}
	Item randItem() throws Exception {
		// note that this sanity check should mean the other sanity check is not necessary,
		// but I had 'i' returned as a negative number before, so I'm going to leave both in.
		if(flags==0) return Item.EMPTY;
		Random r = new Random();
		int randomFlag = 1<<r.nextInt(32); // see comment below
		// this little magic doodah should give us a sensible upper-bound for shifting randomFlag later on
		int topend = flags;
		int i=0;
		for(i=0; topend>0; i++) topend >>= 1;
		// sanity check: if i <= 0, we don't want the caller to try calling on the same item again, but we -can-
		// continue, since the caller can sanitise its argument and -then- try again.
		if(i<=0) {
			throw new Exception("Fixme: randItem() has been called on Item.EMPTY (probably)");
		}
		// doodah ends
		while(!isFlagSet(randomFlag)) {
			// below is a fizzwidget that uses the above doodah to produce a (hopefully) sensible output
			randomFlag = 1<<r.nextInt(i); // shift 1 left by a random bounded int
			// test results confirm the output is hopefully-sensible.  
		}
		/* - the below error checking is, I believe, no longer necessary, as I have (greatly) simplified the above function
		if(Math.floor(Math.log(randomFlag)/Math.log(2)) != Math.log(randomFlag)/Math.log(2))
		{
			System.err.println("ARGH! randItem() is broken!");
			System.exit(-1);
		}
		*/
		return Item.EMPTY.setFlag(randomFlag);
	}
}
