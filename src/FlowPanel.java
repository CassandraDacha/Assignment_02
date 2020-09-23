import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class FlowPanel extends JPanel implements Runnable {
	static Terrain land;
	public static Water water ;
	public static AtomicBoolean isPaused;
	public static AtomicBoolean end;
	public static AtomicInteger years;
	public static AtomicBoolean isReset;
	Master thread_01, thread_02, thread_03, thread_04;


	/**
	 * FlowPanel constructor that takes in one argument
	 *
	 * @param terrain
	 */
	FlowPanel(Terrain terrain) {
		isPaused = new AtomicBoolean(true);
		end = new AtomicBoolean(false);
		isReset = new AtomicBoolean(false);
		years = new AtomicInteger(0);
		land = terrain;
		water = new Water(land.dimx, land.dimy);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int x=e.getX();
				int y=e.getY();
				int from_r = Math.max(0, x-4);
				int to_r = Math.min(x+4, land.dimy-1);
				int from_c = Math.max(0, y-4);
				int to_c = Math.min(y+4, land.dimx-1);
				for(int i=from_r; i<to_r ;i++){
					for(int j=from_c ; j<to_c; j++){ ;
						water.setDepth(i,j,3);
						paintWater(i,j);
					}
				}
				repaint();
			}
			@Override
			public void mouseReleased(MouseEvent e) {
			}
		});
	}

    /**
     * Paints water on the given x,y cordinate
	 *
	 * @param x
     * @param y
	 */
	public static void paintWater(int x, int y){
		BufferedImage img = land.getImage2();
		if(water.getDepth(x,y) > 0){
			img.setRGB(x, y, Color.blue.getRGB());
		}
		else{
			Color c = new Color(0, 0, 0, 0);
			img.setRGB(x, y, c.getRGB());
		}
	}

	/**
	 * draw the landscape in greyscale as an image
	 * @param g
	 */
	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);
		if (land.getImage() != null){
			g.drawImage(land.getImage(), 0, 0, null);
		}
		if(land.getImage2() != null){
			g.drawImage(land.getImage2(), 0, 0, null);
		}
	}

	/**
	 * Clears all the water from the model does isResetting the landscape
	 * when the paintWater method is called
	 */
	public static void reset() {
		isReset.set(true);
		isPaused.set(true);
		years.set(0);
		int dimx = land.dimx;
		int dimy = land.dimy;
		for(int x=0; x < dimx; x++) {
			for (int y = 0; y < dimy; y++) {
				water.clearWater(x,y);
				paintWater(x,y);
			}
		}
	}

	/**
	 * pauses the simulation
	 */
	public static void pause() {
		isPaused.set(true);
	}

	/**
	 * starts the simulation
	 */
	public static void play() {
		isPaused.set(false);
		isReset.set(false);
	}

	/**
	 * Ends the simulation and exits the program
	 */
	public static void endMaster() {
		isPaused.set(true);
		System.exit(1);
	}

	public void run() {
		// display loop here
		// to do: this should be controlled by the GUI
		// to allow stopping and starting
		int dim = land.dim();
		while(!end.get()){
			if(!isPaused.get()){
				//At the start water must be cleared at the boundaries as indicated
				water.clearWater(0,0);
				paintWater(0,0);
				water.clearWater(land.dimx-1,land.dimy-1);
				paintWater(land.dimx-1,land.dimy-1);

				thread_01 = new Master(land, water, 0,dim / 4);
				thread_02 = new Master(land, water, dim / 4, dim / 2);
				thread_03 = new Master(land, water, dim/2, dim / 2 + dim / 4);
				thread_04 = new Master(land, water, dim / 2 + dim / 4, dim);

				thread_01.start();
				thread_02.start();
				thread_03.start();
				thread_04.start();

				years.getAndIncrement();
				Flow.increment(years);
				repaint();
			}
			repaint();
		}
	}
}