package absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos;

import absortio.m00p4.negocio.service.singleton.SystemSingleton;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ExcelSolucion {

    public static String generarExcelSolucion(Solucion solucion) throws IOException {
        String nombre = "";
        ArrayList<ArrayList<ArrayList<String>>> rutas = solucion.obtenerRutas();
        ArrayList<ArrayList<Integer>> pasos = solucion.getPasos();
        File archivosXLS = new File(SystemSingleton.getInstance().getRute() + File.separator + "Rutas.xlsx");
        Integer i = 1;
        while (archivosXLS.exists()) {
            archivosXLS = new File(SystemSingleton.getInstance().getRute() + File.separator + "Rutas (" + i.toString() + ").xlsx");
            i++;
            System.out.println(i);
        }
        nombre = archivosXLS.getName();
        archivosXLS.createNewFile();
        XSSFWorkbook workbook = new XSSFWorkbook();
        for (int j = 0; j < rutas.size(); j++) {
            i = j + 1;
            Integer k;
            XSSFSheet hoja = workbook.createSheet("Ruta " + i.toString());
            Row fila1 = hoja.createRow(1);
            for (Integer m = 0; m < rutas.get(j).get(0).size(); m++) {
                Cell celda1 = fila1.createCell(m + 5);
                celda1.setCellValue(Integer.valueOf(m).toString());
            }
            for (k = 0; k < rutas.get(j).size(); k++) {
                Row fila = hoja.createRow(k + 2);
                for (Integer m = 0; m < rutas.get(j).get(k).size(); m++) {
                    Cell celda = fila.createCell(m + 5);
                    celda.setCellValue(rutas.get(j).get(k).get(m));
                }
            }

            for (int n = 0; n < pasos.get(j).size(); n++) {
                Row fila = hoja.getRow(n + 2);
                if (fila == null) fila = hoja.createRow(n + 2);
                Cell celda1 = fila.createCell(1);
                celda1.setCellValue(Integer.valueOf(n + 1).toString());
                Cell celda2 = fila.createCell(2);
                celda2.setCellValue(solucion.obtenerCoordenada(pasos.get(j).get(n)).toString());
            }

            for (int h = 0; h < rutas.get(0).size(); h++) {
                Row fila = hoja.getRow(h + 2);
                if (fila == null) fila = hoja.createRow(h + 2);
                Cell celda1 = fila.createCell(4);
                celda1.setCellValue(Integer.valueOf(h).toString());
            }

            Row fila = hoja.getRow(1);
            Cell celda1 = fila.createCell(1);
            celda1.setCellValue("Paso");
            Cell celda2 = fila.createCell(2);
            celda2.setCellValue("Coordenadas");
        }
        FileOutputStream archivo = new FileOutputStream(archivosXLS);
        workbook.write(archivo);
        archivo.close();
        return nombre;
    }

    private static String getStringRepresentation(ArrayList<Character> list) {
        StringBuilder builder = new StringBuilder(list.size());
        for (Character ch : list) {
            builder.append(ch);
        }
        return builder.toString();
    }

    public static String deserializarSimulacion(String simulacion) throws IOException {
        File archivosXLS = new File(SystemSingleton.getInstance().getRute() + File.separator + "Rutas.xlsx");
        Integer k = 1;
        while (archivosXLS.exists()) {
            archivosXLS = new File(SystemSingleton.getInstance().getRute() + File.separator + "Rutas (" + k.toString() + ").xlsx");
            k++;
            System.out.println(k);
        }
        String nombre = archivosXLS.getName();
        archivosXLS.createNewFile();
        XSSFWorkbook workbook = new XSSFWorkbook();

        int i = 0;
        int numRutas = 0;
        while (true) {
            numRutas = numRutas * 10 + (simulacion.charAt(i) - '0');
            i++;
            if (simulacion.charAt(i) == ';') break;
        }
        ArrayList<ArrayList<String>> coordenadasRuta = new ArrayList<>();
        for (int j = 0; j < numRutas; j++) {
            ArrayList<String> coordenadas = new ArrayList<>();
            while (true) {
                ArrayList<Character> cadena = new ArrayList<>();
                while (true) {
                    i++;
                    if (simulacion.charAt(i) == '(') break;
                }
                while (true) {
                    cadena.add(simulacion.charAt(i));
                    i++;
                    if (simulacion.charAt(i) == ')') break;
                }
                cadena.add(simulacion.charAt(i));
                i++;
                coordenadas.add(getStringRepresentation(cadena));
                if (simulacion.charAt(i) == ';') break;
            }
            coordenadasRuta.add(coordenadas);
        }
        for (int gggg = 0; gggg < coordenadasRuta.get(0).size(); gggg++)
            System.out.println(coordenadasRuta.get(0).get(gggg));
        i++;
        Integer altura = 0;
        while (true) {
            altura = altura * 10 + (simulacion.charAt(i) - '0');
            i++;
            if (simulacion.charAt(i) == ';') break;
        }
        i++;
        Integer ancho = 0;
        while (true) {
            ancho = ancho * 10 + (simulacion.charAt(i) - '0');
            i++;
            if (simulacion.charAt(i) == ';') break;
        }
        i++;

        ArrayList<ArrayList<ArrayList<String>>> rutas = new ArrayList<>();
        for (int m = 0; m < numRutas; m++) {
            ArrayList<ArrayList<String>> ruta = new ArrayList<>();
            for (int p = 0; p < altura; p++) {
                ArrayList<String> fila = new ArrayList<>();
                for (int q = 0; q < ancho; q++) {
                    fila.add(String.valueOf(simulacion.charAt(i)));
                    i++;
                }
                ruta.add(fila);
            }
            rutas.add(ruta);
            i++;
        }

        Integer rutaNum;
        for (int j = 0; j < rutas.size(); j++) {
            rutaNum = j + 1;
            Integer g;
            XSSFSheet hoja = workbook.createSheet("Ruta " + rutaNum.toString());
            Row fila1 = hoja.createRow(1);
            for (Integer m = 0; m < rutas.get(j).get(0).size(); m++) {
                Cell celda1 = fila1.createCell(m + 5);
                celda1.setCellValue(Integer.valueOf(m).toString());
            }
            for (g = 0; g < rutas.get(j).size(); g++) {
                Row fila = hoja.createRow(g + 2);
                for (Integer m = 0; m < rutas.get(j).get(g).size(); m++) {
                    Cell celda = fila.createCell(m + 5);
                    celda.setCellValue(rutas.get(j).get(g).get(m));
                }
            }

            for (int n = 0; n < coordenadasRuta.get(j).size(); n++) {
                Row fila = hoja.getRow(n + 2);
                if (fila == null) fila = hoja.createRow(n + 2);
                Cell celda1 = fila.createCell(1);
                celda1.setCellValue(Integer.valueOf(n + 1).toString());
                Cell celda2 = fila.createCell(2);
                celda2.setCellValue(coordenadasRuta.get(j).get(n));
            }
            for (int h = 0; h < rutas.get(0).size(); h++) {
                Row fila = hoja.getRow(h + 2);
                if (fila == null) fila = hoja.createRow(h + 2);
                Cell celda1 = fila.createCell(4);
                celda1.setCellValue(Integer.valueOf(h).toString());
            }

            Row fila = hoja.getRow(1);
            Cell celda1 = fila.createCell(1);
            celda1.setCellValue("Paso");
            Cell celda2 = fila.createCell(2);
            celda2.setCellValue("Coordenadas");
        }
        FileOutputStream archivo = new FileOutputStream(archivosXLS);
        workbook.write(archivo);
        archivo.close();


        return nombre;
    }


    private static ArrayList<ArrayList<String>> copiarMapa(ArrayList<ArrayList<String>> matrizOriginal) {
        ArrayList<ArrayList<String>> matrizCopia = new ArrayList<>();
        for (int i = 0; i < matrizOriginal.size(); i++) {
            matrizCopia.add(new ArrayList<>());
            for (int j = 0; j < matrizOriginal.get(i).size(); j++) {
                matrizCopia.get(i).add(matrizOriginal.get(i).get(j));
            }
        }
        return matrizCopia;
    }

    public static String generarExcelSolucion(ArrayList<ArrayList<Coordenada>> pasos, ArrayList<ArrayList<String>> matrizOriginal) throws IOException {
        String nombre = "";
        ArrayList<ArrayList<ArrayList<String>>> rutas = new ArrayList<>();
        ArrayList<ArrayList<String>> rutaActual = copiarMapa(matrizOriginal);
        ArrayList<ArrayList<Coordenada>> pasosXRuta = new ArrayList<>();
        ArrayList<Coordenada> pasoXRutaActual = new ArrayList<>();

        for (int h = 0; h < pasos.size(); h++) {
            for (int f = 0; f < pasos.get(h).size(); f++) {
                rutaActual.get(pasos.get(h).get(f).getY()).set(pasos.get(h).get(f).getX(), "o");
            }
            if (pasos.get(h).get(pasos.get(h).size() - 1).equals(pasos.get(0).get(0))) {
                pasosXRuta.add(pasoXRutaActual);
                rutas.add(rutaActual);
                rutaActual = copiarMapa(matrizOriginal);
                pasoXRutaActual = new ArrayList<>();
                rutaActual.get(pasos.get(0).get(0).getY()).set(pasos.get(0).get(0).getX(), "o");
            } else {
                pasoXRutaActual.add(pasos.get(h).get(pasos.get(h).size() - 1));
            }
        }

        File archivosXLS = new File(SystemSingleton.getInstance().getRute() + File.separator + "Rutas.xlsx");
        Integer i = 1;
        while (archivosXLS.exists()) {
            archivosXLS = new File(SystemSingleton.getInstance().getRute() + File.separator + "Rutas (" + i.toString() + ").xlsx");
            i++;
            System.out.println(i);
        }
        nombre = archivosXLS.getName();
        archivosXLS.createNewFile();
        XSSFWorkbook workbook = new XSSFWorkbook();
        for (int j = 0; j < rutas.size(); j++) {
            i = j + 1;
            Integer k;
            XSSFSheet hoja = workbook.createSheet("Ruta " + i.toString());
            Row fila1 = hoja.createRow(1);
            for (Integer m = 0; m < rutas.get(j).get(0).size(); m++) {
                Cell celda1 = fila1.createCell(m + 5);
                celda1.setCellValue(Integer.valueOf(m).toString());
            }
            for (k = 0; k < rutas.get(j).size(); k++) {
                Row fila = hoja.createRow(k + 2);
                for (Integer m = 0; m < rutas.get(j).get(k).size(); m++) {
                    Cell celda = fila.createCell(m + 5);
                    celda.setCellValue(rutas.get(j).get(k).get(m));
                }
            }

            for (int n = 0; n < pasosXRuta.get(j).size(); n++) {
                Row fila = hoja.getRow(n + 2);
                if (fila == null) fila = hoja.createRow(n + 2);
                Cell celda1 = fila.createCell(1);
                celda1.setCellValue(Integer.valueOf(n + 1).toString());
                Cell celda2 = fila.createCell(2);
                celda2.setCellValue(pasosXRuta.get(j).get(n).toString());
            }

            for (int h = 0; h < rutas.get(0).size(); h++) {
                Row fila = hoja.getRow(h + 2);
                if (fila == null) fila = hoja.createRow(h + 2);
                Cell celda1 = fila.createCell(4);
                celda1.setCellValue(Integer.valueOf(h).toString());
            }

            Row fila = hoja.getRow(1);
            Cell celda1 = fila.createCell(1);
            celda1.setCellValue("Paso");
            Cell celda2 = fila.createCell(2);
            celda2.setCellValue("Coordenadas");
        }
        FileOutputStream archivo = new FileOutputStream(archivosXLS);
        workbook.write(archivo);
        archivo.close();
        return nombre;
    }
}
