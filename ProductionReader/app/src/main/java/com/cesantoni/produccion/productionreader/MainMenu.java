package com.cesantoni.produccion.productionreader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cesantoni.produccion.productionreader.Analitics.DataVisualization;
import com.cesantoni.produccion.productionreader.escaner.ContinuousCaptureActivity;
import com.cesantoni.produccion.productionreader.utilities.Utilities;

import java.util.HashMap;

/**
 * Created by Juan Antonio on 14/03/2017.
 */

public class MainMenu extends AppCompatActivity {

    private String codigoInterno = "";   //codigo interno
    private String codigoExt = "";         //codigo externo
    private int codeType;

    //verifica si el codigo leido era de tarima completa
    private boolean tarima_incompleta = false;

    private HashMap<String, String> presentaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Utilities u = new Utilities();
        presentaciones = u.cargarPresentaciones();

        //Obtener los datos enviados por el escaner
        Intent intent = this.getIntent();
        //verificar si el intent viene del escaner
        if (intent == null){
            Log.e("Tag", "La actividad no se ha llamado mediante un intent.");
        } else {
            Bundle b = intent.getExtras();
            if(b != null) {
                codigoInterno = b.getString("codigoInterno");
                codeType = b.getInt("codeType");
                String lote = b.getString("lote");
                String fecha = u.getFecha();
                //verificar si la tarima esta completa
                //1 si esta completa
                if(b.getInt("tarimac") == 1) {
                    codigoExt = b.getString("codigoExt");
                    tarima_incompleta = false;
                    guardarDatos(fecha, codigoInterno, lote, codigoExt);
                //2 si esta incompleta
                } else{
                    tarima_incompleta = true;
                    String cant_cajas = b.getString("cantCajas");
                    guardarDatosTarimaInc(fecha, codigoInterno, lote, cant_cajas);
                }
            }
        }
    }

    /**
     * Listener para el boton de escaner, inicia la activity del escaner.
     *
     * @param v boton escaner
     */
    public void escanearPer(View v) {
        Intent escaner = new Intent(this, ContinuousCaptureActivity.class);
        escaner.putExtra("presentaciones", presentaciones);
        startActivity(escaner);
        finish();
    }

    /**
     * Prepara los datos para guardarlos en el csv si la tarima esta completa,
     * verifica si fue posible guardarlos y muestra mensajes de error en caso de que no sea posible.
     *
     * @param fecha         fecha actual del escaneo
     * @param codigoInterno codigo de producto con valores internos
     * @param lote          lote de la tarima
     * @param codigoExt     codigo externo de la empresa
     */
    private void guardarDatos(String fecha, String codigoInterno,  String lote, String codigoExt) {
        Utilities u = new Utilities();
        String[] code = u.separarCadena(codigoInterno, fecha, lote, codigoExt, presentaciones);
        String[] header = {"Fecha", "Codigo Interno", "Lote", "Codigo Externo", "Cantidad cajas", "", "Modelo", "Color", "Calidad", "Tamaño", "Formato", "Dec", "Tono", "Calibre"};
        //Verificar que fue posible separar las cadenas y obtener los codigos
        if(code!=null) {
            //verificar si fue posible guardar en el csv
            if (!u.escribirCsv(code, header, tarima_incompleta))
                Toast.makeText(this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al leer los datos", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Prepara los datos para guardarlos en el csv si la tarima esta incompleta,
     * verifica si fue posible guardarlos y muestra mensajes de error en caso de que no sea posible.
     *
     * @param fecha         fecha actual del escaneo
     * @param codigoInterno codigo de producto con valores internos
     * @param lote          lote de la tarima
     */
    private void guardarDatosTarimaInc(String fecha, String codigoInterno,  String lote, String cant_cajas) {
        Utilities u = new Utilities();
        String[] header = {"Fecha", "Codigo Interno", "Lote", "Cantidad cajas", "", "Modelo", "Color", "Calidad", "Tamaño", "Formato", "Dec", "Tono", "Calibre"};
        String[] code = u.separarCadena(codigoInterno, fecha, lote, cant_cajas);
        //Verificar que fue posible separar las cadenas y obtener los codigos
        if(code!=null) {
            //verificar si fue posible guardar en el csv
            if (!u.escribirCsv(code, header, tarima_incompleta))
                Toast.makeText(this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al leer los datos", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * listener para el boton btn_registro.
     * Cambia la activity actual a DataVisualization con los datos de tarimas completas.
     *
     * @param v boton btn_registro
     */
    public void mostrarRegistro(View v) {
        Intent mostrar = new Intent(this, DataVisualization.class);
        //1 = completa
        mostrar.putExtra("tipo", 1);
        startActivity(mostrar);
    }

    /**
     * Listener para el boton btn_registro_ti.
     * Cambia la activity actual a DataVisualization con los datos de tarimas incompletas.
     *
     * @param v boton btn_registro_ti
     */
    public void mostrarRegistro_ti(View v) {
        Intent mostrar = new Intent(this, DataVisualization.class);
        //2 = incompleta
        mostrar.putExtra("tipo", 2);
        startActivity(mostrar);
    }
}
