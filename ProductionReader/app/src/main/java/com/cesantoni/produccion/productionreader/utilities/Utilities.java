package com.cesantoni.produccion.productionreader.utilities;

import android.os.Environment;
import android.widget.TextView;

import com.cesantoni.produccion.productionreader.dao.Calibre;
import com.cesantoni.produccion.productionreader.dao.Tono;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    public String[] separarCadena(String codigoInterno, String fecha, String lote, HashMap presentaciones) {
        separarContenido(codigoInterno);
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

    public ArrayList<Tono> cargarTonos() {
        ArrayList<Tono> tonos = new ArrayList<>();
        tonos.add(new Tono("00", "Fletes"));
        tonos.add(new Tono("01", "1"));
        tonos.add(new Tono("02", "2"));
        tonos.add(new Tono("03", "3"));
        tonos.add(new Tono("04", "4"));
        tonos.add(new Tono("05", "5"));
        tonos.add(new Tono("06", "6"));
        tonos.add(new Tono("07", "7"));
        tonos.add(new Tono("08", "8"));
        tonos.add(new Tono("09", "9"));
        tonos.add(new Tono("0A", "091"));
        tonos.add(new Tono("0B", "0111"));
        tonos.add(new Tono("10", "10"));
        tonos.add(new Tono("11", "11"));
        tonos.add(new Tono("13", "00"));
        tonos.add(new Tono("14", "000"));
        tonos.add(new Tono("15", "01"));
        tonos.add(new Tono("16", "02"));
        tonos.add(new Tono("17", "03"));
        tonos.add(new Tono("18", "04"));
        tonos.add(new Tono("19", "05"));
        tonos.add(new Tono("20", "06"));
        tonos.add(new Tono("21", "07"));
        tonos.add(new Tono("22", "08"));
        tonos.add(new Tono("23", "09"));
        tonos.add(new Tono("24", "010"));
        tonos.add(new Tono("25", "011"));
        tonos.add(new Tono("26", "012"));
        tonos.add(new Tono("27", "013"));
        tonos.add(new Tono("28", "014"));
        tonos.add(new Tono("29", "015"));
        tonos.add(new Tono("30", "016"));
        tonos.add(new Tono("31", "017"));
        tonos.add(new Tono("32", "018"));
        tonos.add(new Tono("33", "019"));
        tonos.add(new Tono("34", "020"));
        tonos.add(new Tono("35", "021"));
        tonos.add(new Tono("36", "022"));
        tonos.add(new Tono("37", "023"));
        tonos.add(new Tono("38", "024"));
        tonos.add(new Tono("39", "025"));
        tonos.add(new Tono("40", "026"));
        tonos.add(new Tono("41", "027"));
        tonos.add(new Tono("42", "028"));
        tonos.add(new Tono("43", "029"));
        tonos.add(new Tono("44", "030"));
        tonos.add(new Tono("45", "031"));
        tonos.add(new Tono("46", "032"));
        tonos.add(new Tono("47", "033"));
        tonos.add(new Tono("48", "034"));
        tonos.add(new Tono("49", "035"));
        tonos.add(new Tono("50", "036"));
        tonos.add(new Tono("51", "037"));
        tonos.add(new Tono("52", "039"));
        tonos.add(new Tono("53", "040"));
        tonos.add(new Tono("54", "041"));
        tonos.add(new Tono("55", "042"));
        tonos.add(new Tono("56", "044"));
        tonos.add(new Tono("57", "045"));
        tonos.add(new Tono("58", "046"));
        tonos.add(new Tono("59", "050"));
        tonos.add(new Tono("60", "051"));
        tonos.add(new Tono("61", "L.E."));
        tonos.add(new Tono("62", "L.U."));
        tonos.add(new Tono("63", "038"));
        tonos.add(new Tono("64", "043"));
        tonos.add(new Tono("65", "047"));
        tonos.add(new Tono("66", "048"));
        tonos.add(new Tono("67", "049"));
        tonos.add(new Tono("68", "052"));
        tonos.add(new Tono("69", "053"));
        tonos.add(new Tono("70", "054"));
        tonos.add(new Tono("71", "055"));
        tonos.add(new Tono("72", "056"));
        tonos.add(new Tono("73", "058"));
        tonos.add(new Tono("74", "073"));
        tonos.add(new Tono("75", "060"));
        tonos.add(new Tono("76", "065"));
        tonos.add(new Tono("77", "064"));
        tonos.add(new Tono("78", "063"));
        tonos.add(new Tono("79", "067"));
        tonos.add(new Tono("80", "068"));
        tonos.add(new Tono("81", "070"));
        tonos.add(new Tono("82", "057"));
        tonos.add(new Tono("83", "072"));
        tonos.add(new Tono("84", "074"));
        tonos.add(new Tono("85", "075"));
        tonos.add(new Tono("86", "079"));
        tonos.add(new Tono("87", "081"));
        tonos.add(new Tono("88", "061"));
        tonos.add(new Tono("89", "12"));
        tonos.add(new Tono("90", "13"));
        tonos.add(new Tono("91", "062"));
        tonos.add(new Tono("92", "082"));
        tonos.add(new Tono("93", "077"));
        tonos.add(new Tono("94", "085"));
        tonos.add(new Tono("95", "086"));
        tonos.add(new Tono("96", "087"));
        tonos.add(new Tono("97", "089"));
        tonos.add(new Tono("98", "41"));
        tonos.add(new Tono("99", "15"));
        tonos.add(new Tono("P1", "3.8"));
        tonos.add(new Tono("P2", "4.8"));
        tonos.add(new Tono("P3", "20"));
        tonos.add(new Tono("P4", "4.08"));
        tonos.add(new Tono("P5", "3.8 (U) II"));
        return tonos;
    }

    public ArrayList<Calibre> cargarCalibres() {
        ArrayList<Calibre> calibres = new ArrayList<>();
        calibres.add(new Calibre("39", "A"));
        calibres.add(new Calibre("01", "B"));
        calibres.add(new Calibre("14", "B/C"));
        calibres.add(new Calibre("02", "C"));
        calibres.add(new Calibre("15", "C/D"));
        calibres.add(new Calibre("03", "D"));
        calibres.add(new Calibre("16", "D/E"));
        calibres.add(new Calibre("04", "E"));
        calibres.add(new Calibre("21", "E/F"));
        calibres.add(new Calibre("05", "F"));
        calibres.add(new Calibre("22", "F/G"));
        calibres.add(new Calibre("17", "G"));
        calibres.add(new Calibre("23", "G/H"));
        calibres.add(new Calibre("18", "H"));
        calibres.add(new Calibre("19", "I"));
        calibres.add(new Calibre("24", "N"));
        calibres.add(new Calibre("25", "O"));
        calibres.add(new Calibre("47", "O/P"));
        calibres.add(new Calibre("40", "O/Q"));
        calibres.add(new Calibre("06", "P"));
        calibres.add(new Calibre("26", "P/Q"));
        calibres.add(new Calibre("42", "P/R"));
        calibres.add(new Calibre("41", "P/T"));
        calibres.add(new Calibre("07", "Q"));
        calibres.add(new Calibre("27", "Q/S"));
        calibres.add(new Calibre("08", "R"));
        calibres.add(new Calibre("28", "R/S"));
        calibres.add(new Calibre("46", "R/T"));
        calibres.add(new Calibre("09", "S"));
        calibres.add(new Calibre("13", "S/T"));
        calibres.add(new Calibre("43", "S/U"));
        calibres.add(new Calibre("10", "T"));
        calibres.add(new Calibre("44", "T/V"));
        calibres.add(new Calibre("50", "T/W"));
        calibres.add(new Calibre("11", "U"));
        calibres.add(new Calibre("29", "U/V"));
        calibres.add(new Calibre("45", "U/W"));
        calibres.add(new Calibre("12", "V"));
        calibres.add(new Calibre("49", "V/U"));
        calibres.add(new Calibre("30", "V/W"));
        calibres.add(new Calibre("48", "V/X"));
        calibres.add(new Calibre("31", "W"));
        calibres.add(new Calibre("32", "W/X"));
        calibres.add(new Calibre("33", "X"));
        calibres.add(new Calibre("35", "Y"));
        calibres.add(new Calibre("37", "Z"));
        return calibres;
    }

    public boolean esCodigoInterno(String codigo) {
        String clave = codigo.substring(0, 3);
        return !clave.equals("750");
    }

}
