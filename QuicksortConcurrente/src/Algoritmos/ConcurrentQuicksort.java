package Algoritmos;

//Importa clases necesarias para programación concurrente
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;

//La clase ParallelQuicksort extiende RecursiveAction, lo que permite dividir tareas en subtareas sin devolver resultados
public class ConcurrentQuicksort extends RecursiveAction {
 private int[] array; // Array a ordenar
 private int low, high; // Índices que definen el segmento del array a ordenar

 // Constructor: recibe el array y los límites inferior y superior del segmento a ordenar
 public ConcurrentQuicksort(int[] array, int low, int high) {
     this.array = array;
     this.low = low;
     this.high = high;
 }

 // Método que contiene la lógica para dividir el trabajo entre subtareas
 @Override
 protected void compute() {
     // Si hay al menos dos elementos en el segmento
     if (low < high) {
         // Se obtiene el índice del pivote después de particionar
         int pi = partition(array, low, high);

         // Se crean dos subtareas para ordenar las dos mitades en paralelo
         invokeAll(
             new ConcurrentQuicksort(array, low, pi - 1), // Parte izquierda
             new ConcurrentQuicksort(array, pi + 1, high) // Parte derecha
         );
     }
 }

 // Método para particionar el array (igual que en QuickSort clásico)
 private int partition(int[] arr, int low, int high) {
     int pivot = arr[high]; // Se elige el último elemento como pivote
     int i = (low - 1); // Índice del elemento más pequeño

     for (int j = low; j < high; j++) {
         // Si el elemento actual es menor o igual al pivote
         if (arr[j] <= pivot) {
             i++;
             // Intercambia arr[i] y arr[j]
             int temp = arr[i];
             arr[i] = arr[j];
             arr[j] = temp;
         }
     }

     // Coloca el pivote en su posición correcta
     int temp = arr[i + 1];
     arr[i + 1] = arr[high];
     arr[high] = temp;

     return i + 1; // Retorna el índice del pivote
 }

 // Método principal (punto de entrada del programa)
 public static void main(String[] args) {
     int[] array = {10, 7, 8, 9, 1, 5}; // Array a ordenar

     // Crea un ForkJoinPool para ejecutar tareas concurrentes
     ForkJoinPool pool = new ForkJoinPool();

     // Inicia la ejecución del QuickSort concurrente en todo el array
     pool.invoke(new ConcurrentQuicksort(array, 0, array.length - 1));
     pool.shutdown();
     
     // Muestra el array ordenado por consola
     System.out.println("Array ordenado: ");
     for (int i = 0; i < array.length; i++) {
         System.out.print(array[i] + " ");
     }
 }
}