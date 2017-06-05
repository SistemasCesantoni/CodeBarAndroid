package com.cesantoni.produccion.productionreader.escaner;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.cesantoni.produccion.productionreader.MainMenu;
import com.cesantoni.produccion.productionreader.R;
import com.cesantoni.produccion.productionreader.dao.Calibre;
import com.cesantoni.produccion.productionreader.dao.Tarima;
import com.cesantoni.produccion.productionreader.dao.Tono;
import com.cesantoni.produccion.productionreader.utilities.CatalogosSingleton;
import com.cesantoni.produccion.productionreader.utilities.Utilities;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * Created by Juan Antonio on 04/04/2017.
 *
 */
public class ContinuousCaptureActivity extends Activity
        implements DecoratedBarcodeView.TorchListener{
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;

    private Button switchFlashlightButton;
    private EditText cantCajas;

    private int scannCounts = 0;                //Contador de codigos leidos

    //codigos leidos
    private String codigoInterno = "";
    private String lote = "";

    int code_type = 0;

    private CatalogosSingleton cat;
    private HashMap<String, String> presentaciones;
    private ArrayList tonos = new ArrayList<>();
    private ArrayList calibres = new ArrayList<>();

    private Tarima tar;

    private Utilities u;

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            //Para evitar duplicados
            if(result.getText() == null || result.getText().equals(lastText)) {
                return;
            }
            scannCounts++;

            lastText = result.getText();
            barcodeView.setStatusText(result.getText());
            beepManager.playBeepSoundAndVibrate();

            //primer codigo leido
            if (scannCounts == 1) {
                codigoInterno = result.getText();
                if(codigoInterno.length()==13) {
                    if (u.esCodigoInterno(codigoInterno)) {
                        code_type = 0;
                    } else {
                        Toast.makeText(ContinuousCaptureActivity.this,
                                "Favor de leer el codigo interno",
                                Toast.LENGTH_SHORT)
                                .show();
                        scannCounts--;
                    }

                } else if(codigoInterno.length()==17) {
                    code_type = 1;
                } else {
                    Toast.makeText(ContinuousCaptureActivity.this,
                            "Favor de leer el codigo interno",
                            Toast.LENGTH_SHORT)
                            .show();
                    scannCounts--;
                }
            }
            //segundo codigo leido
            if(scannCounts==2) {
                lote = result.getText();
                if (lote.length() > 6 || lote.length() < 5 ) {
                    Toast.makeText(ContinuousCaptureActivity.this,
                            "Favor de leer el lote",
                            Toast.LENGTH_SHORT)
                            .show();
                    scannCounts--;
                } else {
                    onPause();
                    //preguntar si la tarima esta completa
                    tar = u.crearTarima(codigoInterno, lote);
                    showDialog();

                }

            }

            ImageView imageView = (ImageView) findViewById(R.id.barcodePreview);
            imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.continuous_scan);

        u = new Utilities();

        Intent intent = this.getIntent();
        Bundle b = intent.getExtras();
        cat = (CatalogosSingleton)b.getSerializable("catalogo");
        assert cat != null;
        presentaciones = cat.getPresentaciones();
        tonos = cat.getTonos();
        calibres =  cat.getCalibres();

        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        barcodeView.decodeContinuous(callback);
        barcodeView.setTorchListener(this);

        switchFlashlightButton = (Button)findViewById(R.id.switch_flashlight);

        if (!hasFlash()) {
            switchFlashlightButton.setVisibility(View.GONE);
        }

        beepManager = new BeepManager(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void pause(View view) {
        barcodeView.pause();
    }

    public void resume(View view) {
        barcodeView.resume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public void switchFlashlight(View view) {
        if (getString(R.string.turn_on_flashlight).equals(switchFlashlightButton.getText())) {
            barcodeView.setTorchOn();
        } else {
            barcodeView.setTorchOff();
        }
    }

    @Override
    public void onTorchOn() {
        switchFlashlightButton.setText(R.string.turn_off_flashlight);
    }

    @Override
    public void onTorchOff() {
        switchFlashlightButton.setText(R.string.turn_on_flashlight);
    }

    public void cancelScan(View v) {
        Intent cancel = new Intent(this, MainMenu.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(cancel);
        finish();
    }

    private void showDialog() {
        String cant_cajas = presentaciones.get(tar.getFormato());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alerta!");
        builder.setMessage("Cajas: " + cant_cajas + "\n¿La cantidad de cajas mostrada es correcta?");
        builder.setCancelable(false);
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tar.setTarima_completa(true);
                obtenerCantCajas();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tar.setTarima_completa(false);
                obtenerCantCajas();
            }
        });
        builder.create();
        builder.show();
    }

    public void obtenerCantCajas() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.ingreso_cantidad_cajas, null);

        cantCajas = (EditText) dialogView.findViewById(R.id.txt_cant_cajas);

        final Spinner spinnerTonos = (Spinner)dialogView.findViewById(R.id.spinner_tonos);
        ArrayAdapter<Tono> adapter = new ArrayAdapter<Tono>(this,
                android.R.layout.simple_spinner_dropdown_item, tonos);
        spinnerTonos.setAdapter(adapter);

        final Spinner spinnerCalibres = (Spinner)dialogView.findViewById(R.id.spinner_calibres);
        ArrayAdapter<Calibre> adapterC = new ArrayAdapter<Calibre>(this,
                android.R.layout.simple_spinner_dropdown_item, calibres);
        spinnerCalibres.setAdapter(adapterC);

        cantCajas.setEnabled(false);

        CheckBox check_cajas = (CheckBox)dialogView.findViewById(R.id.checkbox_cant_cajas);

        check_cajas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

               @Override
               public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked) {
                    cantCajas.setEnabled(true);
                    tar.setTarima_completa(false);
                } else {
                    cantCajas.setEnabled(false);
                    tar.setTarima_completa(true);
                }
               }
           }
        );

        if (tar.isTarima_completa()) {
            String cant_cajas = presentaciones.get(tar.getFormato());
            cantCajas.setText(cant_cajas);
            check_cajas.setChecked(false);
        } else {
            check_cajas.setChecked(true);
        }

        builder.setView(dialogView)
                .setPositiveButton(R.string.btn_guardar_cajas, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        onPause();

                        String cant_cajas_final = cantCajas.getText().toString();
                        if (!cant_cajas_final.isEmpty()) {
                            final Tono tono_sel = (Tono) spinnerTonos.getSelectedItem();
                            final Calibre cal_sel = (Calibre) spinnerCalibres.getSelectedItem();

                            tar.setCantCajas(cant_cajas_final);
                            tar.setTono(tono_sel.getKey());
                            tar.setCalibre(cal_sel.getKey());

                            String message = u.guardarDatos(tar);
                            guardadoCompleto(message);
                            dialog.dismiss();
                        }
                    }
                })
                .setNegativeButton(R.string.btn_cancelar_cajas, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(ContinuousCaptureActivity.this, ContinuousCaptureActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(i);
                        finish();
                    }
                });
        builder.setTitle("Ingresa Cant. Cajas");
        builder.create();
        builder.show();

    }

    private void guardadoCompleto(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alerta!");
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent return_main = new Intent(ContinuousCaptureActivity.this, MainMenu.class)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("catalogo", cat);
                return_main.putExtras(mBundle);
                startActivity(return_main);
                finish();
            }
        });
        builder.create();
        builder.show();
    }

}
