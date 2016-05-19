import java.util.concurrent.RecursiveAction;

public class ImageTaskForkJoin extends RecursiveAction {

	private static final long serialVersionUID = 1L;
	private int[][] originalMatrix;
	private int[][] filter;
	private int mValue;
	private int[][] newMatrix;
	private int low;
	private int high;

	public ImageTaskForkJoin(int[][] originalMatrix, int[][] filter, int m,
			int[][] toModify, int l, int h) {

		this.originalMatrix = originalMatrix;
		this.filter = filter;
		this.mValue = m;
		this.newMatrix = toModify;
		this.low = l;
		this.high = h;

	}

	protected void compute() {

		if (low == high) {
			
			int condicion = originalMatrix[0].length - 1;
			for (int i = 1; i < condicion; i++) {
				newMatrix[low][i] = applyFilter(originalMatrix, filter, low, i,
						mValue);
			}
		} else {
			
			int mid = low + (high - low) / 2;
			ImageTaskForkJoin left = new ImageTaskForkJoin(originalMatrix,filter, mValue, newMatrix, low, mid);
			ImageTaskForkJoin right = new ImageTaskForkJoin(originalMatrix,filter, mValue, newMatrix, mid+1, high);
			left.fork();
			right.compute();
			left.join();
		}

	}

	public int applyFilter(int[][] origin, int[][] filter, int row,
			int colum, int maskValue) {
		int r = 0;
		
		for (int i = row - 1, i2 = 0; i <= row + 1; i++, i2++) {
			for (int j = colum - 1, j2 = 0; j <= colum + 1; j++, j2++) {
				r = r + (origin[i][j] * filter[i2][j2]);
			}
		}
		return (Math.abs(r)) / maskValue;
	}
}
