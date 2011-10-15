package neuralrobots;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import JNeuralNet.MapSimulation;

public class RobotDisplay implements Runnable{
	
	static final int SCREENSIZE_X = 400;
	static final int SCREENSIZE_Y = 400;
	static final int REFRESH_RATE = 500; // in milliseconds
	
	static Display display = new Display();
	static Shell shell = new Shell(display);
	
	MapSimulation[] worlds;
	int currentWorldIndex;
	
	static Combo worldSelector = new Combo(shell,SWT.DROP_DOWN | SWT.BORDER);
	Canvas worldCanvas;
	
	
	
	@Override
	public void run() {
		// shell.setSize(SCREENSIZE_X,SCREENSIZE_Y);
		worldSelector.addModifyListener(new worldSelectorModifyListener());
		worldCanvas = new Canvas(shell, SWT.NONE);
		shell.pack();
		shell.open();
		
		Runnable drawMap = new Runnable() {
			public void run() {
				
				
			}
		};
		
		while(!shell.isDisposed()) {
			if(!display.readAndDispatch()) {
				display.timerExec(REFRESH_RATE,drawMap);
				display.sleep();
			}
		}
		display.dispose();
	}
	
	public void setWorlds(MapSimulation[] w) {
		worlds = w;
		worldSelector.removeAll();
		for(int i=0;i<worlds.length;i++) {
			worldSelector.add("World "+(i+1));
		}
		currentWorldIndex = 0;
	}

	private class worldSelectorModifyListener implements ModifyListener {

		public void modifyText(ModifyEvent e) {
			currentWorldIndex = worldSelector.getSelectionIndex();
		}
		
	}
	
	
}
