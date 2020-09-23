import java.awt.*;
import java.awt.image.BufferedImage;

public class Master extends  Thread{

    public static Terrain terrain;
    public static Water water ;
    public static int low;
    public static int high;

    /**
     * This is the constructor of the simulation thread, it takes in four parameters
     * @param terrain
     * @param water
     * @param low
     * @param high
     */
    public Master(Terrain terrain,Water water, int low, int high) {
        Master.terrain = terrain;
        Master.water = water;
        Master.low = low;
        Master.high = high;
    }

    /**
     * The main method that is called whenever the start method is executed of the
     * instance of this class
     */
    @Override
    public void run() {
        for(int i=low; i< high;i++) {
            int[] location = new int[2];
            terrain.getPermute(i,location);
            int x_loc = location[0];
            int y_loc = location[1];
            int from_x = Math.max(0, x_loc-1);
            int to_x = Math.min(x_loc+1, terrain.dimx-1);
            int from_y = Math.max(0, y_loc-1);
            int to_y = Math.min(y_loc+1, terrain.dimy-1);
            BufferedImage img = terrain.getImage2();
            int color = img.getRGB(x_loc,y_loc);
            if(color == Color.blue.getRGB()){
                float f1 = water.calcSurface(x_loc,y_loc,terrain);
                float small = f1;
                int x_other=0, y_other=0;
                for(int r= from_x; r<to_x ;r++){
                    for(int c= from_y; c<to_y ;c++){
                        float f2 = water.calcSurface(r,c,terrain);
                        int ck = Float.compare(f2, small);
                        if(ck < 0){
                            small = f2 ;
                            x_other = r ;
                            y_other= c;
                        }
                    }
                }
                int check = Float.compare(f1, small);
                if(check > 0) {
                    water.increaseDepth(x_other,y_other);
                    water.decreaseDepth(x_loc,y_loc);
                    FlowPanel.paintWater(x_other,y_other);
                    FlowPanel.paintWater(x_loc,y_loc);
                }
            }
        }

    }
}
