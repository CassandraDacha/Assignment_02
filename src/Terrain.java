import java.io.File;
import java.awt.image.*;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class Terrain {

	float [][] height; // regular grid of height values
	int dimx, dimy; // data dimensions
	BufferedImage img; // greyscale image for displaying the terrain top-down
	BufferedImage transparent;
	ArrayList<Integer> permute;	// permuted list of integers in range [0, dimx*dimy)


	int dim(){
		return dimx*dimy;
	}


	int getDimX(){
		return dimx;
	}

	int getDimY(){
		return dimy;
	}

	public BufferedImage getImage() {
		return img;
	}

	public BufferedImage getTransparentImage() {
		return transparent;
	}


	void locate(int pos, int [] ind) {
		ind[0] = (int) pos / dimy; // x
		ind[1] = pos % dimy; // y
	}


	void deriveImage() {
		img = new BufferedImage(dimx, dimy, BufferedImage.TYPE_INT_ARGB);
		transparent = new BufferedImage(dimx, dimy, BufferedImage.TYPE_INT_ARGB);
		float maxh = -10000.0f, minh = 10000.0f;

		// determine range of heights
		for(int x=0; x < dimx; x++) {
			for (int y = 0; y < dimy; y++) {
				float h = height[x][y];
				if (h > maxh)
					maxh = h;
				if (h < minh)
					minh = h;
			}
		}

		for(int x=0; x < dimx; x++) {
			for (int y = 0; y < dimy; y++) {
				// find normalized height value in range
				float val = (height[x][y] - minh) / (maxh - minh);
				Color col = new Color(val, val, val, 1.0f);
				Color color = new Color(0, 0, 0, 0);
				img.setRGB(x, y, col.getRGB());
				transparent.setRGB(x, y, color.getRGB());
			}
		}
	}


	void genPermute() {
		permute = new ArrayList<Integer>();
		for(int idx = 0; idx < dim(); idx++)
			permute.add(idx);
		java.util.Collections.shuffle(permute);
	}


	void getPermute(int i, int [] loc) {
		locate(permute.get(i), loc);
	}

	void readData(String fileName){
		try{
			Scanner sc = new Scanner(new File(fileName));
			sc.useLocale(Locale.US);

			// read grid dimensions
			// x and y correpond to columns and rows, respectively.
			// Using image coordinate system where top left is (0, 0).
			dimy = sc.nextInt(); //get rows
			dimx = sc.nextInt(); //get columns

			// populate height grid
			height = new float[dimx][dimy];

			for(int y = 0; y < dimy; y++){
				for(int x = 0; x < dimx; x++)
					height[x][y] = sc.nextFloat();
			}
			sc.close();
			// create randomly permuted list of indices for traversal
			genPermute();
			// generate greyscale heightfield image
			deriveImage();
		}
		catch (IOException e){
			System.out.println("Unable to open input file "+fileName);
			e.printStackTrace();
		}
		catch (java.util.InputMismatchException e){
			System.out.println("Malformed input file "+fileName);
			e.printStackTrace();
		}
	}
}