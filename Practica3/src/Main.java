import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Main {

	public static void main(String[] args) throws IOException {

		System.out.println("Iniciando programa");
		System.out.println();
		System.out
				.println("Este programa aplicara un filtro de una imagen en formato .pgm de tipo P2. Se solicitara un fichero de entrada y un tipo de filtro para aplicar. \n"
						+ "Los filtros se aplicaran con Executors y ForkJoin mostrando el tiempo de calculo para cada uno."
						+ "\nSe generaran dos imagenes que se almacenaran en el destino seleccionado por el usuario.");
		System.out.println("\n\n\n");
		System.out.println("Seleccionar fichero de entrada...");
		FileSelector fileSelector = new FileSelector();
		String imageFile = fileSelector.selectFile();
		System.out.println("El fichero de imagen seleccionado es " + imageFile);
		//
		int availableProcessors = Runtime.getRuntime().availableProcessors();
		System.out.println("Numero de procesadores disponibles: "
				+ availableProcessors);

		// String imageFile =
		// "/home/pablo/Escritorio/pc/Practica3/casablanca.ascii.pgm";

		Map<Integer, int[][]> filters = new HashMap();
		filters.put(1, Filters.SOBEL_HORIZONTAL);
		filters.put(2, Filters.SOBEL_VERTICAL);
		filters.put(3, Filters.ENFOCAR);
		filters.put(4, Filters.DETECCION_BORDES);
		filters.put(5, Filters.EMBOSS);
		filters.put(6, Filters.DESENFOCAR);

		System.out
				.println("---------------------------------------------------------------");

		Scanner scanner = new Scanner(System.in);
		System.out.println("Seleccion de filtro:");
		System.out.println("\t1.Sobel horizontal");
		System.out.println("\t2.Sobel vertical");
		System.out.println("\t3.Enfocar");
		System.out.println("\t4.Deteccion de bordes");
		System.out.println("\t5.Emboss");
		System.out.println("\t6.Desenfocar");
		System.out.print("opcion: ");
		while (!scanner.hasNextInt()) {
			System.out
					.println("valor seleccionado no valido, valor entre <1-6>");
			System.out.print("opcion: ");
			scanner.next();
		}
		int f = scanner.nextInt();
		long startTime = 0;

		System.out
				.println("---------------------------------------------------------------");

		Image inputImage = new Image(imageFile);
		inputImage.imageToArray();

		int[][] newFilteredImage = new int[inputImage.getHeight()][inputImage
				.getWidth()];

		int mValue = getFilterMaskValue(filters.get(f));

		// SECUENCIAL
		// long startTime = System.nanoTime();
		// int value;
		// for (int i = 1; i < inputImage.getHeight() - 1; i++) {
		// for (int j = 1; j < inputImage.getWidth() - 1; j++) {
		// value = applyFilter(inputImage.getMatrix(), filters.get(f), i,j,
		// mValue);
		// newFilteredImage[i][j] = value;
		// }
		// }
		//
		// Image imageForkJoin = new Image();
		// imageForkJoin.setHeight(inputImage.getHeight());
		// imageForkJoin.setWidth(inputImage.getWidth());
		// imageForkJoin.setMaxValue(inputImage.getMaxValue());
		// imageForkJoin.setMatrix(newFilteredImage);
		// imageForkJoin.imageToFile();

		// FIN SECUENCIAL

		// EXECUTOR SERVICE
		ExecutorService executor = Executors
				.newFixedThreadPool(availableProcessors);

		startTime = System.nanoTime();

		//Executor service con llamadas por cada pixel con callable
//		for (int i = 1; i < inputImage.getHeight() - 1; i++) {
//			for (int j = 1; j < inputImage.getWidth() - 1; j++) {
//				Future<Integer> r = executor.submit(new ImageExecutorTask(
//						mValue, inputImage.getMatrix(), filters.get(f), i, j));
//				try {
//					newFilteredImage[i][j] = r.get();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				} catch (ExecutionException e) {
//					e.printStackTrace();
//				}
//			}
//		}
		
		//Executor service para entregar, hace llamadas para cada fila con runable
		for (int i = 1; i < inputImage.getHeight()-1; i++) {
			executor.submit(new ImageExecutorTaskR(mValue, inputImage.getMatrix(), filters.get(f), i,newFilteredImage));
		}
	long executionTime = System.nanoTime() - startTime;
		executor.shutdown();

		System.out.println("Tiempo de ejecución con Executor service: "
				+ TimeUnit.MILLISECONDS.convert(executionTime,
						TimeUnit.NANOSECONDS) + " milisegundos");

		Image imageExecutor = new Image();
		imageExecutor.setHeight(inputImage.getHeight());
		imageExecutor.setWidth(inputImage.getWidth());
		imageExecutor.setMaxValue(inputImage.getMaxValue());
		imageExecutor.setMatrix(newFilteredImage);
		imageExecutor.imageToFile();

		// FIN EXECUTOR SERVICE

		// FORK JOIN
		 int[][] resul = new int[inputImage.getHeight()][inputImage.getWidth()];
		 ForkJoinPool pool = new ForkJoinPool(availableProcessors);
		 startTime = System.nanoTime();
		 pool.invoke(new ImageTaskForkJoin(inputImage.getMatrix(),filters.get(f),mValue,resul,1,inputImage.getHeight()-2));
		 executionTime = System.nanoTime() - startTime;
		 pool.shutdown();
		
		
		 executionTime = System.nanoTime() - startTime;
		
		 System.out.println("Tiempo de ejecución con Fork/Join: "
		 + TimeUnit.MILLISECONDS.convert(executionTime,
		 TimeUnit.NANOSECONDS) + " milisegundos");
		 Image imageForkJoin = new Image();
		 imageForkJoin.setHeight(inputImage.getHeight());
		 imageForkJoin.setWidth(inputImage.getWidth());
		 imageForkJoin.setMaxValue(inputImage.getMaxValue());
		 imageForkJoin.setMatrix(resul);
		 imageForkJoin.imageToFile();
		// FIN FORK JOIN

		System.out.println("Fin del programa");

	}

	public static int applyFilter(int[][] origin, int[][] filter, int row,
			int colum, int maskValue) {
		int r = 0;

		for (int i = row - 1, i2 = 0; i <= row + 1; i++, i2++) {
			for (int j = colum - 1, j2 = 0; j <= colum + 1; j++, j2++) {
				r = r + (origin[i][j] * filter[i2][j2]);
			}
		}
		return (Math.abs(r)) / maskValue;
	}

	public static int getFilterMaskValue(int[][] filter) {
		int positive = 0;
		int negative = 0;
		for (int i = 0; i < Filters.ROW; i++) {
			for (int j = 0; j < Filters.COLUMN; j++) {
				if (filter[i][j] > 0) {
					positive = positive + filter[i][j];
				} else {
					negative = negative + Math.abs(filter[i][j]);
				}
			}
		}
		return Math.max(positive, negative);
	}

}
