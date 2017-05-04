package com.cesantoni.produccion.productionreader.utilities;

import android.os.Environment;
import android.widget.TextView;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Juan Antonio on 17/03/2017.
 */

public class Utilities {

    //Claves
    private String modelo = "";
    private String color = "";
    private String calidad = "";
    private String tamaño = "";
    private String formato = "";
    private String dec = "";
    private String tono = "";
    private String calibre = "";
    private String productCode = "";

    //tarimas completas
    private File tarjeta = Environment.getExternalStorageDirectory(); //direccion raiz de la tarjeta sd
    private File file_tc = null;

    private File file_ti = null;

    /**
     * Metodo encargado de verificar si la tarjeta sd esta activa
     *
     * @return sdDisponible
     */
    private boolean disponibilidadSD() {
        boolean sdDisponible = true;
        //Comprobamos el estado de la memoria externa (tarjeta SD)
        String estado = Environment.getExternalStorageState();
        if (estado.equals(Environment.MEDIA_MOUNTED)) {
            sdDisponible = true;
        } else if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            sdDisponible = false;
        } else {
            sdDisponible = false;
        }
        return sdDisponible;
    }

    /**
     * Verifica si el archivo en la ruta especifica existe, si no, crea la rama de directorios
     * necesarios y despues crea el archivo.
     *
     * @return true si se pudo crear el directorio y el archivo, false si no es posible crearlos
     */
    private boolean crearDirectorio() throws IOException {
        File tc_dir = new File(tarjeta.getAbsolutePath() + "/log/escaner/tc/" + getFecha().trim());
        File ti_dir = new File(tarjeta.getAbsolutePath() + "/log/escaner/ti/" + getFecha().trim());
        String filename = "items.csv";
        String filename_ti = "items_inc.csv";
        if(tc_dir.exists() && ti_dir.exists()) {
            file_tc = new File(tc_dir, filename);
            file_ti = new File(ti_dir.getAbsolutePath(), filename_ti);
            return true;
        } else {
            if(tc_dir.mkdirs() && ti_dir.mkdirs()) {
                file_tc = new File(tc_dir, filename);
                file_ti = new File(ti_dir, filename_ti);
                return true;
            }
            return false;
        }
    }

    /**
     * Verifica si el archivo esta vacio.
     *
     * @param file  Archivo que sera revisado.
     * @return      true si el archivo no esta vacio, false si esta vacio.
     */
    private boolean verificarArchivo(File file) {
        FileReader fr = null;
        BufferedReader br;
        try {
            fr = new FileReader (file);
            br = new BufferedReader(fr);
            return br.readLine()!=null;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }finally{
            try{
                if( null != fr ){
                    fr.close();
                }
            }catch (Exception e2){
                e2.printStackTrace();
            }
        }
    }

    /**
     * Con los datos guardados en el array de claves.
     * Se instancia un objeto writter para poder escribir los datos en un archivo.
     *
     * @param code              Arreglo con las claves separadas
     * @param header            Arreglo con las cabeceras de los archivos csv
     * @param tarimaIncompleta  boolean para verificar si la tarima es completa o no
     * @return                  true si los datos fueron escritos correctamente, false en caso
     *                          contrario
     */
    public boolean escribirCsv(String code[], String header[], boolean tarimaIncompleta) {
        CSVWriter writer2 = null;
        CSVWriter writer3 = null;
        try {
            crearDirectorio();
            if (!tarimaIncompleta) {
                try {
                    writer2 = new CSVWriter(new FileWriter(file_tc, true), ',', '\''); //instanciar el writercsv y el file writer, con el archivo creado y poner true para no borrar el contenido
                    if (!verificarArchivo(file_tc)) {
                        writer2.writeNext(header);
                    }
                    writer2.writeNext(code);
                } catch (IOException ie) {
                    return false;
                } finally {
                    try {
                        assert writer2 != null;
                        writer2.close();
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }
            }
            if (tarimaIncompleta) {
                try {
                    writer3 = new CSVWriter(new FileWriter(file_ti, true), ',', '\'');

                    if (!verificarArchivo(file_ti)) {
                        writer3.writeNext(header);
                    }
                    writer3.writeNext(code);
                }catch (IOException ie) {
                    return false;
                } finally {
                    try {
                        writer3.close();
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }
            }
        } catch (IOException ie) {
            return false;
        }
        return true;
    }

    /**
     * Lee los datos almacenados en el archivo csv creado y los muestra en el TextView
     * que recibe como parametro.
     *
     * @param tx    textView enviado desde la interface para mostrar los datos
     * @return      true si fue posible escribir los datos, false si no fue posible
     */
    public boolean leerCsv(TextView tx, boolean tarima_completa) {
        CSVReader csvReader = null; //instancia para el lector de csv
        try {
            crearDirectorio();
            //instanciar el lector, enviando el archivo ya creado, separado por comas, identificado con "" y comenzando desde la linea 1
            if(tarima_completa)
                csvReader = new CSVReader(new FileReader(file_tc), ',');
            else
                csvReader = new CSVReader(new FileReader(file_ti), ',');
            String[] itemDetails; //para guardar los datos leidos del csv
            String res = ""; //para formatear la salida en un textview
            //recorrer el archivo csv
            while((itemDetails = csvReader.readNext())!=null) {
                //concatenar lo leido a la variable res
                res += Arrays.toString(itemDetails);
                res += "\n";
            }
            //mostrar el resultado
            tx.setText(res);
                return true;
        }
        catch(Exception ee) {
            ee.printStackTrace();
                return false;
        } finally {
            try {
                csvReader.close();
            }
            catch(Exception ee) {
                ee.printStackTrace();
            }
        }
    }

    /**
     * Obtener la fecha y hora actual del sistema
     *
     * @return currentTimeStamp
     */
    public String getFecha() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String currentTimeStamp = dateFormat.format(new Date());

            return currentTimeStamp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getHora() {
        try {
            SimpleDateFormat hourFormat = new SimpleDateFormat("HH-mm-ss");
            String currentHour = hourFormat.format(new Date());

            return currentHour;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene la cadena content separada en por claves y concatena fecha, lote y codigo externo,
     * guarda estos datos en un arreglo llamado code, esto para tarimas completas.
     *
     * @param codigoInterno     codigo de barras interno
     * @param fecha             fecha del escaneo
     * @param lote              lote de la tarima
     * @param codigoExt         codigo externo de la empresa
     * @return                  code[]
     */
    public String[] separarCadena(String codigoInterno, String fecha, String lote, String codigoExt, HashMap presentaciones) {
        separarContenido(codigoInterno);
        separarCodigo(codigoExt);
        //arreglo que sera guardado en el csv
        //Log.e("ERR", productCode);
        String cant_cajas = presentaciones.get(formato).toString();
        String[] code = {fecha, codigoInterno, lote, productCode, cant_cajas, "", modelo, color, calidad, tamaño, formato, dec, tono, calibre};
        return code;
    }

    /**
     * Obtiene la cadena content separada en por claves y concatena fecha, lote y codigo externo,
     * guarda estos datos en un arreglo llamado code, esto para tarimas incompletas.
     *
     * @param codigoInterno codigo de barras interno
     * @param fecha         fecha del escaneo
     * @param lote          lote de la tarima
     * @return              code[]
     */
    public String[] separarCadena(String codigoInterno, String fecha, String lote, String cant_cajas) {
        separarContenido(codigoInterno);
        //arreglo que sera guardado en el csv
        String[] code = {fecha, codigoInterno, lote, cant_cajas, "", modelo, color, calidad, tamaño, formato, dec, tono, calibre};
        return code;
    }

    /**
     * Separar el codigo interno en claves individuales.
     *
     * @param codigoInterno codigo leido desde el escaner
     */
    private void separarContenido(String codigoInterno) {
        //separar la cadena en las claves
        for (int i = 0; i < codigoInterno.length(); i++) {
            if (i < 3) {
                modelo += codigoInterno.charAt(i);
            } else if (i >= 3 && i < 5) {
                color += codigoInterno.charAt(i);
            } else if (i == 5) {
                calidad += codigoInterno.charAt(i);
            } else if (i >= 6 && i < 8) {
                tamaño += codigoInterno.charAt(i);
            } else if (i >= 8 && i < 10) {
                formato += codigoInterno.charAt(i);
            } else if (i >= 10 && i < 13) {
                dec += codigoInterno.charAt(i);
            } else if (i >= 13 && i < 15) {
                tono += codigoInterno.charAt(i);
            } else {
                calibre += codigoInterno.charAt(i);
            }
        }
    }

    public String obtenerCantCajas(String codigo) {
        separarContenido(codigo);
        return formato;
    }

    /**
     * Extrae el codigo de producto desde el codigo externo leido en el escaner.
     *
     * @param codigoExt codigo leido desde el escaner
     */
    private void separarCodigo(String codigoExt) {
        for(int i = 0; i< codigoExt.length(); i++) {
            if (i > 6)
                productCode += codigoExt.charAt(i);
            if (codigoExt.length() == 13 && i == 11)
                break;
            if (codigoExt.length() == 12 && i == 10)
                break;
        }
    }

    public HashMap<String, String> cargarPresentaciones() {
        HashMap<String, String> presentaciones = new HashMap<String, String>();
        presentaciones.put("00", "Fletes");
        presentaciones.put("01", "Unidad");
        presentaciones.put("02", "72");
        presentaciones.put("03", "48");
        presentaciones.put("04", "24");
        presentaciones.put("05", "24");
        presentaciones.put("06", "48");
        presentaciones.put("07", "66");
        presentaciones.put("08", "78");
        presentaciones.put("09", "81");
        presentaciones.put("10", "48");
        presentaciones.put("11", "24");
        presentaciones.put("12", "24");
        presentaciones.put("13", "32");
        presentaciones.put("14", "32");
        presentaciones.put("15", "32");
        presentaciones.put("16", "24");
        presentaciones.put("17", "40");
        presentaciones.put("18", "60");
        presentaciones.put("19", "40");
        presentaciones.put("20", "36");
        presentaciones.put("21", "32");
        presentaciones.put("22", "32");
        presentaciones.put("23", "40");
        presentaciones.put("24", "36");
        presentaciones.put("25", "32");
        presentaciones.put("26", "32");
        presentaciones.put("27", "32");
        presentaciones.put("28", "40");
        presentaciones.put("29", "40");
        presentaciones.put("30", "28");
        presentaciones.put("31", "50");
        presentaciones.put("32", "84");
        presentaciones.put("33", "64");
        presentaciones.put("34", "120");
        presentaciones.put("35", "24");
        presentaciones.put("36", "24");
        presentaciones.put("37", "24");
        presentaciones.put("38", "40");
        presentaciones.put("39", "24");
        presentaciones.put("40", "22");
        presentaciones.put("41", "24");
        presentaciones.put("42", "22");
        presentaciones.put("43", "52");
        presentaciones.put("44", "84");
        return presentaciones;
    }

    public boolean esCodigoInterno(String codigo) {
        String clave = codigo.substring(0, 3);
        return !clave.equals("750");
    }

}
