import java.util.Arrays;

public class Water {
    private int [][] depth;

    /**
     * Constructor
     * @param x_dim
     * @param y_dim
     */
    public Water (int x_dim, int y_dim){
        //populate the grid with default values
        depth = new int[x_dim][y_dim];
        for(int i=0 ; i<x_dim; i++){
            for(int ii=0 ; ii<y_dim; ii++){
                depth[i][ii] = 0;
            }
        }
    }

    /**
     *
     * @param x
     * @param y
     * @return depth of water at x, y cordinate
     */
    synchronized  public int getDepth(int x, int y) {
        return depth[x][y];
    }

    /**
     * set depth of water at x, y cordinate
     * @param x
     * @param y
     * @param depth
     */
    synchronized  public void setDepth(int x, int y, int depth) {
        this.depth[x][y] = depth;
    }

    /**
     * increases the depth of water at the x,y cordinate
     * @param x
     * @param y
     */
    synchronized  public void increaseDepth(int x, int y){
        int d = getDepth(x,y);
        this.setDepth(x,y,d+1);;
    }

    /**
     * decreases the depth of water at the x,y cordinate
     * @param x
     * @param y
     */
    synchronized  public void decreaseDepth(int x, int y){
        int d = getDepth(x,y);
        if(d-1 <= 0)
            this.setDepth(x,y,0);
        this.setDepth(x,y,d-1);
    }

    /**
     * Clears water at the x,y cordinate
     * @param x
     * @param y
     */
    synchronized  public void clearWater(int x, int y){
        depth[x][y] = 0;
    }

    /**
     * Calculates the surface of water based on the terrain heigh values and
     * the depth of water in the specific grid position.
     * @param x
     * @param y
     * @param terrain
     * @return
     */
    synchronized  public float calcSurface(int x, int y, Terrain terrain){
        float depth = this.getDepth(x,y) * 0.01f;
        float elev = terrain.height[x][y];
        return depth+elev;
    }

}
