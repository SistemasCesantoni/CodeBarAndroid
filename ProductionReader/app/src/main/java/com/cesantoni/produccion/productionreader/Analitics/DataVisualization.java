package com.cesantoni.produccion.productionreader.Analitics;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.cesantoni.produccion.productionreader.R;
import com.cesantoni.produccion.productionreader.utilities.Utilities;


/**
 * Created by Juan Antonio on 23/03/2017.
 */

public class DataVisualization extends AppCompatActivity {

    private TextView visualTxt;     //textviews para mostrar información

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_visualization);

        visualTxt = (TextView)findViewById(R.id.txt_visualizacion);

        int tipo = getIntent().getExtras().getInt("tipo");
        Utilities u = new Utilities();
        if(tipo == 1) {
            if (!u.leerCsv(visualTxt, true))
                Toast.makeText(this, "No hay registros", Toast.LENGTH_SHORT).show();
        } else if(tipo == 2) {
            if (!u.leerCsv(visualTxt, false))
                Toast.makeText(this, "No hay registros", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, tipo, Toast.LENGTH_SHORT).show();
        }

    }
}
