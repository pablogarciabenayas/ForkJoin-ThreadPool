import java.util.concurrent.Callable;

public class ImageExecutorTask implements Callable<Integer> {

	private int maskValue;
	private int[][] originalMatrix;
	private int[][] filter;
	private int row;
	private int column;

	public ImageExecutorTask(int maskValue, int[][] originalMatrix,
			int[][] filter, int row, int column) {
		super();
		this.maskValue = maskValue;
		this.originalMatrix = originalMatrix;
		this.filter = filter;
		this.row = row;
		this.column = column;
	}


	@Override
	public Integer call() throws Exception {
		
		Integer r = 0;

		for (int i = row - 1, i2 = 0; i <= row + 1; i++, i2++) {
			for (int j = column - 1, j2 = 0; j <= column + 1; j++, j2++) {
				r = r + (originalMatrix[i][j] * filter[i2][j2]);
			}
		}
		return (Math.abs(r)) / maskValue;
	}



}
