package com.cesantoni.produccion.productionreader.Analitics;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cesantoni.produccion.productionreader.R;
import com.cesantoni.produccion.productionreader.utilities.Utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by Juan Antonio on 23/03/2017.
 */

public class DataVisualization extends ListActivity {

    //private TextView visualTxt;     //textviews para mostrar información
    private List<String> listaNombresArchivos;
    private List<String> listaRutasArchivos;
    private ArrayAdapter<String> adaptador;
    private String directorioRaiz;
    private TextView carpetaActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_visualization);

       // visualTxt = (TextView)findViewById(R.id.txt_visualizacion);

        int tipo = getIntent().getExtras().getInt("tipo");
        Utilities u = new Utilities();

        carpetaActual = (TextView)findViewById(R.id.rutaActual);

        if(tipo == 1) {
            directorioRaiz = u.getDirectorioTc();
        } else if(tipo == 2) {
            directorioRaiz = u.getDirectorioTi();
        } else {
            Toast.makeText(this, tipo, Toast.LENGTH_SHORT).show();
        }
        verArchivoDirectorio(directorioRaiz);

    }

    private void verArchivoDirectorio(String rutaDirectorio) {
        carpetaActual.setText("Estas en: " + rutaDirectorio);
        listaNombresArchivos = new ArrayList<String>();
        listaRutasArchivos = new ArrayList<String>();
        File directorioActual = new File(rutaDirectorio);
        File[] listaArchivos = directorioActual.listFiles();

        int x = 0;

        if(!rutaDirectorio.equals(directorioRaiz)) {
            listaNombresArchivos.add("../");
            listaRutasArchivos.add(directorioActual.getParent());
            x = 1;
        }

        for (File archivo : listaArchivos) {
            listaRutasArchivos.add(archivo.getPath());
        }

        Collections.sort(listaRutasArchivos, String.CASE_INSENSITIVE_ORDER);

        for (int i = x; i < listaRutasArchivos.size(); i++) {
            File archivo = new File(listaRutasArchivos.get(i));
            if(archivo.isFile()) {
                listaNombresArchivos.add(archivo.getName());
            } else {
                listaNombresArchivos.add("/" + archivo.getName());
            }
        }

        if(listaArchivos.length < 1) {
            listaNombresArchivos.add("No hay ningun archivo");
            listaRutasArchivos.add(rutaDirectorio);
        }

        adaptador = new ArrayAdapter<String>(this, R.layout.text_view_lista_archivos, listaNombresArchivos);
        setListAdapter(adaptador);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        File archivo = new File(listaRutasArchivos.get(position));
        if(archivo.isFile()) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"kiritobaz@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Lectura codebar");
            intent.putExtra(Intent.EXTRA_TEXT, "test");
            Uri uri = Uri.fromFile(archivo);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(intent, "Send email..."));
        } else {
            verArchivoDirectorio(listaRutasArchivos.get(position));
        }
    }

}