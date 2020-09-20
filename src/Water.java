import java.util.Objects;

public class Water implements Comparable{
    private int xpos;
    private int ypos;
    private int depth;
    static float depthfactor = 0.01f;


    public Water(int xpos, int ypos, int depth) {

        this.xpos = xpos;
        this.ypos = ypos;
        this.depth = depth;
    }
    synchronized public void reset() {
        this.depth = 0;
    }


    synchronized public int getXpos() {
        return xpos;
    }


    synchronized public void setXpos(int xpos) {
        this.xpos = xpos;
    }


    synchronized public int getYpos() {
        return ypos;
    }


    synchronized public void setYpos(int ypos) {
        this.ypos = ypos;
    }


    synchronized public int getDepth() {
        return depth;
    }


    synchronized public void setDepth(int depth) {
        this.depth = depth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Water)) return false;
        Water water = (Water) o;
        return getXpos() == water.getXpos() &&
                getYpos() == water.getYpos() &&
                getDepth() == water.getDepth();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getXpos(), getYpos(), getDepth());
    }

    @Override
    public int compareTo(Object waterObject) {
        Water Water = (Water) waterObject;
        return Integer.compare(this.depth, Water.depth);
    }


    synchronized  public float calcSurface(Terrain terrain){
        float water_depth = this.depth * depthfactor ;
        float terrain_elevation = terrain.height[this.xpos][this.ypos];
        return water_depth+terrain_elevation;
    }
}