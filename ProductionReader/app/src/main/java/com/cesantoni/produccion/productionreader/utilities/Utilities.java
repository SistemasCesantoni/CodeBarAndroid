package com.cesantoni.produccion.productionreader.utilities;

import android.os.Environment;
import android.widget.TextView;

import com.cesantoni.produccion.productionreader.dao.Tarima;
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
import java.util.List;

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

    public boolean dirIsEnabled() {
        try {
            File test = new File(tarjeta.getAbsolutePath() + "/log/escaner/");
            return test.exists();
        }catch (Exception e) {
            return false;
        }
    }

    public String getDirectorioRaiz() {
        return tarjeta.getAbsolutePath() + "/log";
    }

    public String getDirectorioAlm() {
        return tarjeta.getAbsolutePath() + "/log/escaner";
    }

    public String getDirectorioTc() {
        return tarjeta.getAbsolutePath() + "/log/escaner/tc";
    }

    public String getDirectorioTi() {
        return tarjeta.getAbsolutePath() + "/log/escaner/ti";
    }

    /**
     * Verifica si el archivo en la ruta especifica existe, si no, crea la rama de directorios
     * necesarios y despues crea el archivo.
     *
     * @return true si se pudo crear el directorio y el archivo, false si no es posible crearlos
     */
    private boolean crearDirectorio(String lote) throws IOException {
        File tc_dir = new File(tarjeta.getAbsolutePath() + "/log/escaner/tc/" + lote);
        File ti_dir = new File(tarjeta.getAbsolutePath() + "/log/escaner/ti/" + lote);
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
    private boolean escribirCsv(String code[], String header[], boolean tarimaIncompleta, String lote) {
        CSVWriter writer2 = null;
        CSVWriter writer3 = null;
        try {
            crearDirectorio(lote);
            if (tarimaIncompleta) {
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
            if (!tarimaIncompleta) {
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
                        assert writer3 != null;
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
    public boolean leerCsv(TextView tx, boolean tarima_completa, File archivo) {
        CSVReader csvReader = null; //instancia para el lector de csv
        try {
            csvReader = new CSVReader(new FileReader(archivo), ',');
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
                assert csvReader != null;
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

            return dateFormat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getHora() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");

            return dateFormat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Tarima crearTarima(String codigoInterno, String lote) {
        separarContenido(codigoInterno);

        Tarima tarima = new Tarima();
        tarima.setCodigocompleto(codigoInterno);
        tarima.setModelo(modelo);
        tarima.setColor(color);
        tarima.setCalidad(calidad);
        tarima.setTamaño(tamaño);
        tarima.setFormato(formato);
        tarima.setDec(dec);
        tarima.setLote(lote);

        return tarima;
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
            }
        }
    }

    public boolean esCodigoInterno(String codigo) {
        String clave = codigo.substring(0, 3);
        return !clave.equals("750");
    }

    public String guardarDatos(Tarima tarima) {
        String message;
        String[] code = {getFecha(), getHora(), tarima.getCodigocompleto(), tarima.getLote(), tarima.getCantCajas(),
                tarima.getMetrosPorTarima(), tarima.getModelo(), tarima.getColor(),tarima.getCalidad(), tarima.getTamaño(),
                tarima.getFormato(), tarima.getDec(), tarima.getTono(),tarima.getCalibre()};
        String[] header = {"Fecha", "Hora", "Codigo Interno", "Lote", "Cantidad cajas", "Metros Por Tarima", "Modelo",
                "Color", "Calidad", "Tamaño", "Formato", "Dec", "Tono", "Calibre"};
        //Verificar que fue posible separar las cadenas y obtener los codigos
        if(code!=null) {
            //verificar si fue posible guardar en el csv
            if (!escribirCsv(code, header, tarima.isTarima_completa(), tarima.getLote())) {
                message = "Error al guardar los datos";
            } else {
                message = "Datos Guardados Correctamente";
            }
        } else {
            message = "Error al leer los datos";
        }
        return message;
    }

    public String getMetroPT(String cant_cajas, String metrosPorCaja) {
        double cajasPT = Double.parseDouble(cant_cajas);
        double metrosPC = Double.parseDouble(metrosPorCaja);
        Double metrosPT = cajasPT*metrosPC;
        return metrosPT.toString();
    }


}
