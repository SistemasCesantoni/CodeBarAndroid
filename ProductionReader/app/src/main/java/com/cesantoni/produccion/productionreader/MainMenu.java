package com.cesantoni.produccion.productionreader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.cesantoni.produccion.productionreader.Analitics.DataVisualization;
import com.cesantoni.produccion.productionreader.escaner.ContinuousCaptureActivity;
import com.cesantoni.produccion.productionreader.utilities.CatalogosSingleton;
import com.cesantoni.produccion.productionreader.utilities.Utilities;


/**
 * Created by Juan Antonio on 14/03/2017.
 */

public class MainMenu extends AppCompatActivity {

    private CatalogosSingleton cat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Utilities u = new Utilities();

        //Obtener los datos enviados por el escaner
        Intent intent = this.getIntent();
        //verificar si el intent viene del escaner
        if (intent == null){
        } else {
            Bundle b = intent.getExtras();
            if(b != null) {
                cat = (CatalogosSingleton)b.getSerializable("catalogo");
            }
        }
        if(cat==null) {
            cat = CatalogosSingleton.getInstance();
        }
    }

    /**
     * Listener para el boton de escaner, inicia la activity del escaner.
     *
     * @param v boton escaner
     */
    public void escanearPer(View v) {
        Intent escaner = new Intent(this, ContinuousCaptureActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("catalogo", cat);
        escaner.putExtras(mBundle);
        startActivity(escaner);
        finish();
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
