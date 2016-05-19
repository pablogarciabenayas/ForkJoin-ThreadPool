
public class ImageExecutorTaskR implements Runnable{
	
	private int maskValue;
	private int[][] originalMatrix;
	private int[][] filter;
	private int row;
	private int[][] newMatrix;

	public ImageExecutorTaskR(int maskValue, int[][] originalMatrix,int[][] filter, int row, int[][] toModify) {
		this.maskValue = maskValue;
		this.originalMatrix = originalMatrix;
		this.filter = filter;
		this.row = row;
		this.newMatrix = toModify;
	}

	

	@Override
	public void run() {
		for (int i = 1; i < originalMatrix[0].length - 1; i++) {
			newMatrix[row][i] = applyFilter(originalMatrix, filter, row, i, maskValue);
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
