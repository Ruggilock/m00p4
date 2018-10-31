package absortio.m00p4.negocio.model.auxiliares;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class SQLReader {
    private static ArrayList<String> listaDeQueries = null;

    public ArrayList<String> crearQueries(String ruta)
    {
        String queryLinea = new String();
        StringBuffer sBuffer = new StringBuffer();
        listaDeQueries = new ArrayList<String>();
        try
        {
            FileReader fr = new FileReader(new File(ruta));
            BufferedReader br = new BufferedReader(fr);
            //Lee el archivo SQL linea por linea
            while((queryLinea = br.readLine()) != null)
            {
            // Ignora las lineas que empiezan con #
                int indiceDelSignoComentario = queryLinea.indexOf('#');
                if(indiceDelSignoComentario != -1)
                {
                    if(queryLinea.startsWith("#"))
                    {
                        queryLinea = new String("");
                    }
                    else
                        queryLinea = new String(queryLinea.substring(0, indiceDelSignoComentario-1));
                }

                // Ignora las lineas que empiezan con  --
                indiceDelSignoComentario = queryLinea.indexOf("--");
                if(indiceDelSignoComentario != -1)
                {
                    if(queryLinea.startsWith("--"))
                    {
                        queryLinea = new String("");
                    }
                    else
                        queryLinea = new String(queryLinea.substring(0, indiceDelSignoComentario-1));
                }

                // Ignora las lineas que empiezan con /* */
                indiceDelSignoComentario = queryLinea.indexOf("/*");
                if(indiceDelSignoComentario != -1)
                {
                    if(queryLinea.startsWith("#"))
                    {
                        queryLinea = new String("");
                    }
                    else
                        queryLinea = new String(queryLinea.substring(0, indiceDelSignoComentario-1));
                    sBuffer.append(queryLinea + " ");

                    // Ignora todos los caracteres dentro del comentario
                    do
                    {
                        queryLinea = br.readLine();
                    }
                    while(queryLinea != null && !queryLinea.contains("*/"));
                    indiceDelSignoComentario = queryLinea.indexOf("*/");
                    if(indiceDelSignoComentario != -1)
                    {
                        if(queryLinea.endsWith("*/"))
                        {
                            queryLinea = new String("");
                        }
                        else
                            queryLinea = new String(queryLinea.substring(indiceDelSignoComentario+2, queryLinea.length()-1));
                    }
                }


                if(queryLinea != null)
                    sBuffer.append(queryLinea + " ");
            }
            br.close();

            // Usamos como delimitador el ";"
            String[] splittedQueries = sBuffer.toString().split(";");

            // Filtramos las filas vacias
            for(int i = 0; i<splittedQueries.length; i++)
            {
                if(!splittedQueries[i].trim().equals("") && !splittedQueries[i].trim().equals("\t"))
                {
                    listaDeQueries.add(new String(splittedQueries[i]));
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("*** Error : "+e.toString());
            System.out.println("*** ");
            System.out.println("*** Error : ");
            e.printStackTrace();
            System.out.println("################################################");
            System.out.println(sBuffer.toString());
        }
        return listaDeQueries;
    }


}
