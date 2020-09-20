import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicBoolean;

public class Master  implements Runnable {

    public static AtomicBoolean paused = new AtomicBoolean(true);;
    public static Terrain terrain;
    public static int low;
    public static int high;
    public static Water[][] water_list ;

    public Master(Terrain terrain, int low, int high) {

        Master.terrain = terrain;
        water_list = new Water[terrain.dimx][terrain.dimy];
        Master.low = low;
        Master.high = high;

    }
    public static void reset() {

        Flow.generations.set(0);
        BufferedImage img = terrain.getTransparentImage();
        int dimx = terrain.dimx;
        int dimy = terrain.dimy;
        for(int x=0; x < dimx; x++) {
            for (int y = 0; y < dimy; y++) {
                Color color = new Color(0, 0, 0, 0);
                img.setRGB(x, y, color.getRGB());
            }
        }
    }

    public static void pause() {
        paused.set(true);
        System.out.println("Status: " + paused);
    }

    public static void play() throws InterruptedException {
        paused.set(false);
        System.out.println("Status: " + paused);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        if (!paused.get()) {
            //clear water from boundaries
            for(int i=low; i< high;i++) {
                int[] location = new int[2];
                terrain.getPermute(i,location);
                int x_cord = location[0];
                int y_cord = location[1];
                int from_x = Math.max(0, x_cord-1);
                int to_x = Math.min(x_cord+1, terrain.dimx-1);
                int from_y = Math.max(0, y_cord-1);
                int to_y = Math.min(y_cord+1, terrain.dimy-1);
                //Find if there is water in this location
                BufferedImage img = terrain.getTransparentImage();
                int color = img.getRGB(x_cord,y_cord);
                if(color == Color.blue.getRGB()){
                    //there is water in this grid position
                    //  check the array to see if there was water detected here initially
                    Water water;
                    if(water_list[x_cord][y_cord] == null) {
                        water = new Water(x_cord, y_cord, 3);
                        water_list[x_cord][y_cord] = water;
                    }else {
                        water = new Water(x_cord, y_cord, water_list[x_cord][y_cord].getDepth());
                    }
                    //calculate the water surface
                    float surface = water.calcSurface(terrain);
                    float current_small = surface;
                    int x_small=0, y_small=0, small_depth=0;
                    for(int r= from_x; r<to_x ;r++){
                        for(int c= from_y; c<to_y ;c++){
                            Water wot;
                            if(water_list[r][c] == null){
                                wot = new Water(r,c,0);
                            }else{
                                wot = new Water(r,c,water_list[r][c].getDepth());
                            }
                            float surf_other = wot.calcSurface(terrain);
                            if(surf_other < current_small){
                                current_small = surf_other;
                                x_small = r ;
                                y_small = c;
                                small_depth = wot.getDepth();
                            }

                        }
                    }
                    int retval = Float.compare(surface, current_small);

                    if(retval > 0) {

                        Water w = new Water(x_small,y_small,small_depth+1);
                        water_list[x_small][y_small] = w;
                        if(water.getDepth()-1 > 0){
                            water_list[x_cord][y_cord].setDepth(water_list[x_cord][y_cord].getDepth()-1);
                        }else if(water.getDepth()-1 == 0){
                            water_list[x_cord][y_cord].setDepth(water_list[x_cord][y_cord].getDepth()-1);
                            Color colorr = new Color(0,0,0,0);
                            img.setRGB(x_cord,y_cord, colorr.getRGB());
                        }
                        img.setRGB(x_small,y_small, Color.blue.getRGB());

                    }
                }


            }
        }else{
            System.out.println("Status: "+paused);
        }



    }



}