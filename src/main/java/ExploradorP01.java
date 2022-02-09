import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ExploradorP01 {

    public static void main(String[] args) throws IOException {

        if(args.length !=2){
            System.out.println("Faltan argumentos");
            System.exit(1);
        }
        String fileName= args[0];
        FileReader fileReader=null;

        String archivoName= args[1];
        FileReader fileReader1=null;

        try{
            fileReader=new FileReader(fileName);
            fileReader1=new FileReader(archivoName);

        }catch (FileNotFoundException e){
            System.out.println("EL nombre de un archivo  no se encontro");
            System.exit(2);
        }

        BufferedReader in = new BufferedReader(fileReader);
        String str;
        List<String> lista= new ArrayList<>();

        while((str=in.readLine()) != null){
            //System.out.println(in.readLine());
            lista.add(in.readLine());
        }
        BufferedReader in1= new BufferedReader(fileReader1);
        String st;
        int counter=0;

        //Comparador palabra con frase
        int size = lista.size();
        int [] contPal;
        contPal= new int[lista.size()];

        while ((st=in1.readLine()) !=null){
            String token[]=st.split("\\s+");
            for (int i = 0; i < token.length; i++) {
                for (int j = 0; j < size; j++) {
                    if(token[i].contains(lista.get(j))){
                        if(j<=19){
                            counter++;
                        }else if(j>20 && j<43){
                            counter=counter+2;
                        }else if(j>44){
                            counter=counter+3;
                        }
                        contPal[j]=contPal[j]+1;
                    }
                }
            }
        }

        //Imprimir
        for (int i = 0; i < size; i++) {
            if(contPal[i]>0){
                if(i>1 && i<=19){
                    System.out.println(lista.get(i)+" tuvo " + contPal[i] +" coincidencias.  Total  de puntos: " +contPal[i]*1 );
                }else if(i>20 && i<=43){
                    System.out.println(lista.get(i)+" tuvo " + contPal[i] +" coincidencias.  Total  de puntos: " +contPal[i]*2 );
                }else if(i>44){
                    System.out.println(lista.get(i)+" tuvo " + contPal[i] +" coincidencias.  Total  de puntos: " +contPal[i]*3 );
                }
            }
        }
        System.out.println("\nEl total de puntos por coincidencias es: " + counter);

    }

}
