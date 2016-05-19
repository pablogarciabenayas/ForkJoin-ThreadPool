public class Filters {
	
	public static final int ROW = 3;
	public static final int COLUMN = 3;
	
	public static final int[][] SOBEL_HORIZONTAL = new int[][] {
			{ -1, -2, -1 }, { 0, 0, 0 }, { 1, 2, 1 } };

	public static final int[][] SOBEL_VERTICAL = new int[][] { 
			{ -1, 0, 1 }, { -2, 0, 2 }, { -1, 0, 1 } };

	public static final int[][] ENFOCAR= new int[][] { 
			{ 0, -1, 0 }, { -1, 5, -1 }, { 0, -1, 0 } };

	public static final int[][] DETECCION_BORDES = new int[][] { 
			{ 0, 1, 0 }, { 1, -4, 1 }, { 0, 1, 0 } };

	public static final int[][] EMBOSS = new int[][] { 
			{ -2, -1, 0 }, { -1, 1, 1 }, { 0, 1, 2 } };

	public static final int[][] DESENFOCAR = new int[][] { 
			{ 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 } };
}
