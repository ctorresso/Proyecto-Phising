import java.io.*;
import java.util.ArrayList;

class main {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Error! directorio no encontrado");
            System.exit(1);
        }
        String direc=args[0];
        System.out.println("Directorio: " + direc);

        //Listemos todas las carpetas y archivos de la carpeta actual

        File carpeta = new File(direc);
        String[] listado = carpeta.list();
        if (listado == null || listado.length == 0) {
            System.out.println("No hay elementos dentro de la carpeta actual");
            return;
        }
        else {
            for (int i=0; i< listado.length; i++) {
                System.out.println("Archivo a leer: "+listado[i]);
                FileReader fi=null;
                try {
                    fi = new FileReader(direc+listado[i]);

                } catch (FileNotFoundException ex) {
                    System.out.println(ex.getMessage());
                    System.exit(-1);
                }

                BufferedReader inputFile = new BufferedReader(fi);

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
                        }
                    }


                    // Obtener tiempo de ejecución
                    long tiempoEjecucion = System.currentTimeMillis() - startTime;
                    inputFile.close();
                    fi.close();

                    System.out.printf("%2.3f  segundos, %2d lineas y %3d palabras\n",
                            tiempoEjecucion / 1000.00, lineCount, wordCount - numberCount);

                    // Mostrar total de palabras diferentes
                    System.out.printf("%5d palabras diferentes\n", list.size());


                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }



            }
        }



    }
}