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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cesantoni.produccion.productionreader.MainMenu;
import com.cesantoni.produccion.productionreader.R;
import com.cesantoni.produccion.productionreader.utilities.Utilities;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.HashMap;
import java.util.List;

/**
 *
 * Created by Juan Antonio on 04/04/2017.
 *
 */
public class ContinuousCaptureActivity extends Activity implements DecoratedBarcodeView.TorchListener{
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;

    Utilities u;

    private Button switchFlashlightButton;

    private int scannCounts = 0;                //Contador de codigos leidos

    //codigos leidos
    private String codigoInterno = "";
    private String lote = "";
    private String codigoExt = "";

    int code_type = 0;

    private HashMap<String, String> presentaciones;

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
                if (lote.length() > 5) {
                    Toast.makeText(ContinuousCaptureActivity.this,
                            "Favor de leer el lote",
                            Toast.LENGTH_SHORT)
                            .show();
                    scannCounts--;
                } else {
                    onPause();
                    //preguntar si la tarima esta completa
                    showDialog();
                }

            }
            //tercer codigo leido
            if(scannCounts==3) {
                codigoExt = result.getText();
                if (codigoExt.length() < 12 || codigoExt.length() > 13) {
                    scannCounts--;
                } else {
                    if (u.esCodigoInterno(codigoExt)) {
                        Toast.makeText(ContinuousCaptureActivity.this,
                                "Favor de leer el codigo externo",
                                Toast.LENGTH_SHORT)
                                .show();
                        scannCounts--;
                    } else {
                        scannCounts++;
                    }
                }
            }
            //Se han leido los 3 codigos, se regresan los datos para guardarlos en el csv
            if(scannCounts ==4) {
                Intent regresar = new Intent(ContinuousCaptureActivity.this, MainMenu.class);
                regresar.putExtra("codigoInterno", codigoInterno);
                regresar.putExtra("lote", lote);
                regresar.putExtra("codigoExt", codigoExt);
                regresar.putExtra("tarimac", 1);
                regresar.putExtra("codetype", code_type);
                startActivity(regresar);
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
        presentaciones = (HashMap)b.get("presentaciones");

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
        Intent cancel = new Intent(this, MainMenu.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(cancel);
        finish();
    }

    private void showDialog() {
        Utilities u = new Utilities();
        String formato = u.obtenerCantCajas(codigoInterno);
        String cant_cajas = presentaciones.get(formato);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alerta!");
        builder.setMessage("Cajas: " + cant_cajas + "\n¿La cantidad de cajas es correcta?");
        builder.setCancelable(false);
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onResume();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
        builder.setView(dialogView)
                .setPositiveButton(R.string.btn_guardar_cajas, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        onPause();
                        EditText cantCajas = (EditText) dialogView.findViewById(R.id.txt_cant_cajas);
                        String cantidad = cantCajas.getText().toString();
                        Toast.makeText(ContinuousCaptureActivity.this, "cantidad ingresada " + cantCajas, Toast.LENGTH_SHORT).show();
                        Intent return_main = new Intent(ContinuousCaptureActivity.this, MainMenu.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        return_main.putExtra("codigoInterno", codigoInterno);
                        return_main.putExtra("lote", lote);
                        return_main.putExtra("tarimac", 2);
                        return_main.putExtra("cantCajas", cantidad);
                        startActivity(return_main);
                        finish();
                    }
                })
                .setNegativeButton(R.string.btn_cancelar_cajas, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(ContinuousCaptureActivity.this, MainMenu.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(i);
                        finish();
                    }
                });
        builder.setTitle("Ingresa Cant. Cajas");
        builder.create();
        builder.show();

    }
}
