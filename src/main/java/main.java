import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class main extends SimpleFileVisitor<Path> {
    public static final Logger LOG = Logger.getLogger(main.class.getName());

    private boolean showWords;
    private Set<String> termSet;
    private Set<String> stopWords = null;

    public main(boolean showWords) {
        this.showWords = showWords;

        termSet = new TreeSet<>();

        stopWords = this.loadStopWords();

        try {
            Handler fileHandler = new FileHandler("results.log", 2000000, 5);
            fileHandler.setLevel(Level.INFO);
            fileHandler.setFormatter(new CvsFormatter());
            LOG.addHandler(fileHandler);
        } catch (IOException e) {
            LOG.severe(e.getMessage());
        }

    }


    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.exit(2);
        }

        boolean showWords = false;

        if (args.length == 2) {
            if (args[1].toLowerCase().equals("-v")) {
                showWords = true;
            }
        }
        //iniciar en este directorio
        Path startingDir = Paths.get(args[0]);

        //Clase para procesar los archivos

        main contadorLineas = new main(showWords);

        //Iniciar el recorrido de los archivos

        Files.walkFileTree(startingDir, contadorLineas);


    }
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

        String name = file.toAbsolutePath().toString();

        if(name.toLowerCase().endsWith(".txt")){
            FileReader fl= null;
            BufferedReader in = null;

            fl=new FileReader(name);
            in= new BufferedReader(fl);

            int lineCount = 0;
            int wordCount=0;
            int numberCount=0;

            final String delimiters = "\\s+|,\\s*|\\.\\s*|\\;\\s*|\\:\\s*|\\!\\s*|\\¡\\s*|\\¿\\s*|\\?\\s*|\\-\\s*"
                    + "|\\[\\s*|\\]\\s*|\\(\\s*|\\)\\s*|\\\"\\s*|\\_\\s*|\\%\\s*|\\+\\s*|\\/\\s*|\\#\\s*|\\$\\s*";

            //Lista con todas las palabras diferentes
            Set<String> wordsSet = new TreeSet<>();
            String textLine=null;

            //Tiempo inicial
            long starTime= System.currentTimeMillis();

            while((textLine= in.readLine()) != null){
                lineCount++;
                if(textLine.trim().length()==0){
                    continue; //la linea esta vacia,continuar
                }

                String words[] = textLine.split(delimiters);

                wordCount += words.length;
                for (String theWord : words) {

                    theWord = theWord.toLowerCase().trim();

                    boolean isNumeric = true;
                    String term = theWord.trim().toLowerCase();

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
                    if (!stopWords.contains(term)) {
                        wordsSet.add(term);
                    }
                    //Filtrar palabras vacias

                    //if(!stopWords.contains(theWord)){
                    //   listaSinStopsWords.add(theWord);
                    //}

                }


            }
            long tiempoEjecucion = System.currentTimeMillis() - starTime;
            in.close();

            System.out.printf("%s %2.3f segundos %n\t%,d lineas y %,d palabras ", name,
                    (tiempoEjecucion / 1000.00), lineCount,wordCount-numberCount);

            //Mostrar total de palabras diferentes

            System.out.printf("%,d diferentes %4.2f\n", wordsSet.size(),
                    (double) wordsSet.size() / (wordCount -numberCount));
            System.out.println(wordsSet);

            /*String record = String.format("%s,%6.3f,%d,%d",name,(double)(tiempoEjecucion/1000.00),
                    wordCount - numberCount, wordsSet.size() );*/
            String nombre= file.getFileName().toString();

            String record= String.format("Nombre %s, Palabras %d, Palabras diferentes %d",nombre,wordCount -numberCount,wordsSet.size());

            LOG.info(record);
            LOG.info(String.valueOf(wordsSet));
            if(showWords){
                displayWords(wordsSet);
            }
        }
        return FileVisitResult.CONTINUE;
    }

    private void displayWords(Set<String> words){
        for (String theWord : words){
            System.out.printf("%s ", theWord);
        }
        System.out.println();
    }

    private Set<String> loadStopWords() {
        TreeSet<String> set = new TreeSet<>();

        try {
            BufferedReader in = new BufferedReader(new FileReader("stop-word-list.txt"));
            String word = null;

            while ((word = in.readLine()) != null) {
                set.add(word.trim());
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return set;
    }

}

