package com.cesantoni.produccion.productionreader.Analitics;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cesantoni.produccion.productionreader.MainMenu;
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

    private List<String> listaRutasArchivos;
    private String directorioRaiz;
    private TextView carpetaActual;

    Utilities u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_visualization);

        int tipo = getIntent().getExtras().getInt("tipo");
        u = new Utilities();

        carpetaActual = (TextView)findViewById(R.id.rutaActual);

        if (u.dirIsEnabled()) {
            if (tipo == 1) {
                directorioRaiz = u.getDirectorioTc();
            } else if (tipo == 2) {
                directorioRaiz = u.getDirectorioTi();
            } else {
                Toast.makeText(this, tipo, Toast.LENGTH_SHORT).show();
            }
            verArchivoDirectorio(directorioRaiz);
        } else {
            TextView error = (TextView)findViewById(R.id.txt_visualizacion);
            error.setText("No se encuentran registros");
            Button myButton = new Button(this);
            myButton.setText("Regresar");
            myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(DataVisualization.this, MainMenu.class);
                    startActivity(i);
                }
            });

            LinearLayout ll = (LinearLayout)findViewById(R.id.layoutp);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.BOTTOM;
            ll.addView(myButton, lp);
            Toast.makeText(this, "No se encuentran registros", Toast.LENGTH_SHORT).show();
        }

    }

    private void verArchivoDirectorio(String rutaDirectorio) {
        carpetaActual.setText("Estas en: " + rutaDirectorio);
        List<String> listaNombresArchivos = new ArrayList<>();
        listaRutasArchivos = new ArrayList<>();
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

        ArrayAdapter<String> adaptador = new ArrayAdapter<>
                (this, R.layout.text_view_lista_archivos, listaNombresArchivos);
        setListAdapter(adaptador);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        File archivo = new File(listaRutasArchivos.get(position));
        if(archivo.isFile()) {
            showFileOptions(archivo, true);
        } else {
            verArchivoDirectorio(listaRutasArchivos.get(position));
        }
    }

    private void showFileOptions(final File archivo, final boolean tarima_completa) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona una opción...");
        builder.setMessage("¿Qué desea hacer con el archivo?");
        builder.setCancelable(false);
        final TextView txt = (TextView)findViewById(R.id.txt_visualizacion);
        builder.setPositiveButton("Leer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                u.leerCsv(txt, tarima_completa, archivo);
            }
        });
        builder.setNegativeButton("Enviar mail", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                //intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"kirito_baz@outlook.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Codigos " + u.getFecha());
                intent.putExtra(Intent.EXTRA_TEXT, "Codigos leidos el día " + u.getFecha());
                Uri uri = Uri.fromFile(archivo);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(intent, "Send email..."));
            }
        });
        builder.create();
        builder.show();
    }

}