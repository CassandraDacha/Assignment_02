import javax.swing.*;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.util.concurrent.atomic.AtomicInteger;

public class Flow {
	static long startTime = 0;
	static int frameX;
	static int frameY;
	protected static JLabel yearLabel;
	static FlowPanel fp;
	static int year = 0;
	static Terrain landdata = new Terrain();
	public static AtomicInteger generations;


	private static void tick(){
		startTime = System.currentTimeMillis();
	}


	private static float tock(){
		return (System.currentTimeMillis() - startTime) / 1000.0f;
	}

	public static void setupGUI(int frameX,int frameY,Terrain landdata) {

		Dimension fsize = new Dimension(800, 800);
		JFrame frame = new JFrame("Waterflow");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());

		JPanel g = new JPanel();
		g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS));
		fp = new FlowPanel(landdata);
		fp.setPreferredSize(new Dimension(frameX,frameY));
		g.add(fp);

		// to do: add a MouseListener, buttons and ActionListeners on those buttons

		JPanel b = new JPanel();
		b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS));
		JButton reset = new JButton("RESET");
		JButton pause = new JButton("PAUSE");
		JButton play = new JButton("PLAY");
		yearLabel = new JLabel("Year: 0");
		JButton endB = new JButton("End");


		reset.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){

				Master.reset();
			}
		});
		pause.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){

				Master.pause();
			}
		});
		play.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){

				try {
					Master.play();
				} catch (InterruptedException interruptedException) {
					interruptedException.printStackTrace();
				}
			}
		});

		endB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// to do ask threads to stop
				Master.reset();
				frame.removeAll();
				frame.repaint();
				frame.dispose();
				System.exit(0);
			}
		});
		b.add(reset);
		b.add(pause);
		b.add(play);
		b.add(endB);
		b.add(yearLabel);
		g.add(b);

		frame.setSize(frameX, frameY+50);	// a little extra space at the bottom for buttons
		frame.setLocationRelativeTo(null);  // center window on screen
		frame.add(g); //add contents to window
		frame.setContentPane(g);
		frame.setVisible(true);
		Thread fpt = new Thread(fp);
		fpt.start();
	}


	public static void main(String[] args) throws InterruptedException {


		// check that number of command line arguments is correct
//		if(args.length != 1)
//		{
//			System.out.println("Incorrect number of command line arguments. Should have form: java -jar flow.java intputfilename");
//			System.exit(0);
//		}

		// landscape information from file supplied as argument
		//
		landdata.readData("data/medsample_in.txt");
		frameX = landdata.getDimX();
		frameY = landdata.getDimY();

		SwingUtilities.invokeLater(()->setupGUI(frameX, frameY, landdata));
		// to do: initialise and start simulation
		generations = new AtomicInteger(0);
//		startSimulation();


	}

	public static void incrementYear(AtomicInteger gen){
		yearLabel.setText("Year: " + gen);
	}
}