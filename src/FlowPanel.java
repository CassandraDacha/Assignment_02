import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class FlowPanel extends JPanel implements Runnable {
	Terrain land;
	int width;
	int height;
	boolean clicked = false;


	FlowPanel(Terrain terrain) {
		land=terrain;
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int x=e.getX();
				int y=e.getY();
				clicked = true;
				int from_r = Math.max(0, x-3);
				int to_r = Math.min(x+4, width-1);
				int from_c = Math.max(0, y-3);
				int to_c = Math.min(y+3, height-1);
				BufferedImage img = land.getTransparentImage();

				for(int i=from_r; i<to_r ;i++){
					for(int j=from_c ; j<to_c; j++){
						img.setRGB(i,j,Color.blue.getRGB());
						repaint(i,j,width,height);
					}
				}
				System.out.println("Clicked Frame");
			}
			@Override
			public void mouseReleased(MouseEvent e) {
			}
		});
	}


	@Override
	protected void paintComponent(Graphics g) {
		width = getWidth();
		height = getHeight();
		super.paintComponent(g);
		// draw the landscape in greyscale as an image
		if (land.getImage() != null){
			g.drawImage(land.getImage(), 0, 0, null);
			g.drawImage(land.getTransparentImage(), 0, 0, null);
		}

	}

	public void run() {
		while (true) {
			if(!Master.paused.get()) {
				Master master, master2, master3, master4;
				Thread thread, thread2, thread3, thread4;
				int dim = land.dim();
				int low_1 = 0, low_2 = dim / 4, low_3 = dim / 2, low_4 = dim / 2 + dim / 4;
				int high_1 = dim / 4, high_2 = dim / 2, high_3 = dim / 2 + dim / 4, high_4 = dim;
				master = new Master(land, low_1, high_1);
				master2 = new Master(land, low_2, high_2);
				master3 = new Master(land, low_3, high_3);
				master4 = new Master(land, low_4, high_4);
				thread = new Thread(master);
				thread2 = new Thread(master2);
				thread3 = new Thread(master3);
				thread4 = new Thread(master4);
				thread.start();
				thread2.start();
				thread3.start();
				thread4.start();
				Flow.generations.getAndIncrement();
				Flow.incrementYear(Flow.generations);
				repaint();
			}
		}


	}
}