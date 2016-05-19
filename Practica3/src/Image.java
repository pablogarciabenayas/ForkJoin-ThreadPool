import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JFileChooser;

public class Image {
	private String path;
	private int height;
	private int width;
	private int maxValue;
	private int[][] imageMatrix;

	public Image(String p) {
		this.path = p;
	}
	
	public Image(){
		super();
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int[][] getMatrix() {
		return imageMatrix;
	}

	public void setMatrix(int[][] matrix) {
		this.imageMatrix = matrix;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public void imageToArray() throws FileNotFoundException {
		int[][] image;
		int cols, rows, maxVal;
		Scanner scan = new Scanner(new File(this.path));
		String temp = scan.next();
		// verify P2 file...
		if (!temp.equals("P2")) {
			System.out.println("solo se permiten formato P2");
			System.exit(1);
		}

		// remove comments
		while (!scan.hasNextInt()) {
			scan.nextLine();
		}

		this.width = scan.nextInt();
		this.height = scan.nextInt();
		this.maxValue = scan.nextInt();

		image = new int[height][width];
		for (int i = 0; i < height; ++i) {
			for (int j = 0; j < width; ++j) {
				image[i][j] = scan.nextInt();
			}
		}

		this.imageMatrix = image;
	}

	public void imageToFile() throws FileNotFoundException {
		
		JFileChooser jFileChooser = new JFileChooser();
		jFileChooser.setDialogTitle("Seleccionar fichero de salida");
		jFileChooser.setSelectedFile(new File("file.pgm"));
		jFileChooser.showSaveDialog(null);
		
		
		
		maxValue();
		PrintWriter pw = new PrintWriter(jFileChooser.getSelectedFile());
		pw.println("P2");
		pw.println(this.width + " " + this.height);
		pw.println(this.maxValue);

		int lineLength = 0;
		for (int i = 0; i < height; ++i) {
			for (int j = 0; j < width; ++j) {
				int value = this.imageMatrix[i][j];

				String stringValue = "" + value;
				int currentLength = stringValue.length() + 1;
				if (currentLength + lineLength > 70) {
					pw.println();
					lineLength = 0;
				}
				lineLength += currentLength;
				pw.print(value + " ");
			}
		}
		pw.close();
	}

	private void maxValue() {

		int max = 0;

		for (int i = 0; i < this.height; ++i) {
			for (int j = 0; j < this.width; ++j) {
				if (imageMatrix[i][j] > max) {
					max = imageMatrix[i][j];
				}
			}
		}
		this.maxValue = max;
	}
}
