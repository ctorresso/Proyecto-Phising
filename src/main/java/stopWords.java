import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class stopWords {
    private static final DecimalFormat df = new DecimalFormat("0.00");
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Error! directorio no encontrado");
            System.exit(1);
        }
        String direc=args[0];
        String stopWords= args[1];
        System.out.println("Directorio: " + direc);
        System.out.println("");

        //Agregar Coleccion
        FileReader fileReader=null;

        //Listemos todas las carpetas y archivos de la carpeta actual

        File carpeta = new File(direc);
        String[] listado = carpeta.list();
        if(carpeta.exists()){
            if (listado == null || listado.length == 0) {
                System.out.println("No hay elementos dentro de la carpeta actual");
                return;
            }
            else {
                //Burbuja
                String aux;
                for(int i=1; i<=listado.length; i++) {
                    for(int j=0; j<listado.length-i; j++) {
                        if( listado[j].compareTo( listado[j+1] ) > 0 ) {
                            aux   = listado[j];
                            listado[j]  = listado[j+1];
                            listado[j+1]= aux;
                        }
                    }
                }
                for (int i = 0; i <listado.length; i++) {
                    System.out.println("Archivo a leer: "+listado[i]);
                    FileReader fi=null;
                    try {
                        fileReader=new FileReader(stopWords);
                        fi = new FileReader(direc+listado[i]);

                    } catch (FileNotFoundException ex) {
                        System.out.println(ex.getMessage());
                        System.exit(-1);
                    }

                    BufferedReader inputFile = new BufferedReader(fi);
                    BufferedReader in =new BufferedReader(fileReader);

                    List<String> lista = new ArrayList<>();
                    List<String> listaSinStopsWords= new ArrayList<>();
                    String st;
                    while((st=in.readLine()) !=null){
                        lista.add(in.readLine());
                    }

                    String textLine = null;

                    int lineCount = 0;
                    int wordCount = 0;
                    int numberCount = 0;

                    String delimiters = "\\s+|,\\s*|\\.\\s*|\\;\\s*|\\:\\s*|\\!\\s*|\\¡\\s*|\\¿\\s*|\\?\\s*|\\-\\s*"
                            + "|\\[\\s*|\\]\\s*|\\(\\s*|\\)\\s*|\\\"\\s*|\\_\\s*|\\%\\s*|\\+\\s*|\\/\\s*|\\#\\s*|\\$\\s*";


                    // Lista con todas las palabras diferentes
                    ArrayList<String> list = new ArrayList<String>();

                    // Tiempo inicial
                    long startTime = System.currentTimeMillis();
                    try {
                        while ((textLine = inputFile.readLine()) != null) {
                            lineCount++;

                            if (textLine.trim().length() == 0) {
                                continue; // la linea esta vacia, continuar
                            }

                            // separar las palabras en cada linea
                            String words[] = textLine.split(delimiters);

                            wordCount += words.length;

                            for (String theWord : words) {

                                theWord = theWord.toLowerCase().trim();

                                boolean isNumeric = true;

                                // verificar si el token es un numero
                                try {
                                    Double num = Double.parseDouble(theWord);
                                } catch (NumberFormatException e) {
                                    isNumeric = false;
                                }

                                // Si el token es un numero, pasar al siguiente
                                if (isNumeric) {
                                    numberCount++;
                                    continue;
                                }

                                // si la palabra no esta en la lista, agregar a la lista
                                if (!list.contains(theWord)) {
                                    list.add(theWord);
                                }
                                //Filtrar palabras vacias
                                if(!lista.contains(theWord)){
                                    listaSinStopsWords.add(theWord);
                                }

                            }


                        }

                        // Obtener tiempo de ejecución
                        long tiempoEjecucion = System.currentTimeMillis() - startTime;
                        inputFile.close();
                        fi.close();
                        fileReader.close();

                        System.out.printf("%2.3f  segundos, %2d lineas y %3d palabras\n",
                                tiempoEjecucion / 1000.00, lineCount, wordCount - numberCount);
                        // Mostrar total de palabras diferentes
                        int denom=wordCount-numberCount;
                        double porcentaje=(double) listaSinStopsWords.size()/denom;
                        System.out.printf("%2d palabras diferentes\n", list.size());
                        System.out.printf("%2d palabras utiles filtrando stopWords \n", listaSinStopsWords.size());
                        System.out.println(df.format(porcentaje*100)+"% son palabras utiles");
                        System.out.println("");

                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }



                }

            }
        }else {
            System.out.println("No existe la carpeta");
        }




    }
}
