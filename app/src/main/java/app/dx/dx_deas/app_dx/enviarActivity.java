package app.dx.dx_deas.app_dx;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.transform.Templates;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class enviarActivity extends AppCompatActivity {


    private Button btnEnviar;
    private AlertDialog alert ;
    private Spinner spinner ;
    private EditText factura,sello,bultos,peso,porcentaje;
    private ImageView ima1,ima2,btncamara;
    private TextView tramo , txtima ,txtima2;
    private int secuG,secu;
    private String txtfactura,txtsello,txtbultos,txtpeso,txtporcentaje,txtspinner,encodedImage,encodedImage2, mensaje,tran ,idDetalleViaje,idViaje,fechatxt,nombreTramo,sele;
    //private String secuG,secu ;
    private static final int RESULT_LOAD_IMAGE = 1 ;
    private static final int RESULT_LOAD_IMAGE2 = 2 ;
    private Object resultString3 = null;
    private FrameLayout frameLayout;
    private String [] values;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar);

        try {
            idDetalleViaje = getIntent().getStringExtra("idDetalleViaje");
            idViaje = getIntent().getStringExtra("idViaje");
            nombreTramo = getIntent().getStringExtra("nombre");
            secuG = getIntent().getIntExtra("secuG", 100);
            secu = getIntent().getIntExtra("secu", 100);
        }catch (Exception e){
            Toast.makeText(enviarActivity.this, "Error Detalles", Toast.LENGTH_LONG).show();
        }

        if (secu == secuG){
            values = new String[]{"Llegada",};
        }else{
            values = new String[]{"Llegada", "Salida",};
        }


        tramo = (TextView) findViewById(R.id.tramoActualtxt);
        factura = (EditText) findViewById(R.id.facturatxt);
        sello = (EditText) findViewById(R.id.sellotxt);
        bultos = (EditText) findViewById(R.id.bultostxt);
        peso = (EditText) findViewById(R.id.pesotxt);
        porcentaje = (EditText) findViewById(R.id.porcentajetxt);
        btnEnviar = (Button) findViewById(R.id.btnEnviar);
        spinner = (Spinner) findViewById(R.id.spinner1);
        ima1 = (ImageView) findViewById(R.id.imagen_select);
        ima2 = (ImageView) findViewById(R.id.imagen_select2);
        txtima= (TextView) findViewById(R.id.txtima);
        txtima2= (TextView) findViewById(R.id.txtima2);
        frameLayout = (FrameLayout) findViewById(R.id.enviar_carga_contenedor);
        frameLayout.setVisibility(View.INVISIBLE);

        btncamara = (ImageView) findViewById(R.id.btncamara);

        txtima.setText("Factura Recibido\n" + nombreTramo);
        txtima2.setText("Factura de Carga\n" + nombreTramo);

        tramo.setText("Tramo seleccionado : \n" + nombreTramo);


    try {
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, values);
    adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
    spinner.setAdapter(adapter);
        }catch (Exception e){
        Toast.makeText(enviarActivity.this, "Error 5AD", Toast.LENGTH_LONG).show();;
            }


        btncamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(
                            MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
                    enviarActivity.this.startActivity(intent);
                }catch (Exception e ){
                    Toast.makeText(enviarActivity.this, "Error 5CM", Toast.LENGTH_LONG).show();
                }
            }
        });


        ima1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                }catch (Exception e){
                    Toast.makeText(enviarActivity.this, "Error 51M1", Toast.LENGTH_LONG).show();
                }
                            }
        });

        ima2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent,RESULT_LOAD_IMAGE2);
                }catch (Exception e){
                    Toast.makeText(enviarActivity.this, "Error 51M2", Toast.LENGTH_LONG).show();
                }

            }
        });


        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    ConnectivityManager manager =(ConnectivityManager) enviarActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
                    if (null != activeNetwork) {
                        if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                            AlertEnviar();
                        }
                    } else{
                            AlertRed();
                    }
                }catch (Exception e ){
                    Toast.makeText(enviarActivity.this, "Error 500EA", Toast.LENGTH_LONG).show();
                }



            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                try {
                    switch (sele = spinner.getSelectedItem().toString()) {
                        case "Llegada":

                            factura.setText(null);
                            sello.setText(null);
                            bultos.setText(null);
                            peso.setText(null);
                            porcentaje.setText(null);
                            ima1.setImageURI(null);
                            ima2.setImageURI(null);

                            factura.setEnabled(false);
                            sello.setEnabled(false);
                            bultos.setEnabled(false);
                            peso.setEnabled(false);
                            porcentaje.setEnabled(false);
                            ima1.setEnabled(false);
                            ima2.setEnabled(false);
                            txtima.setVisibility(View.INVISIBLE);
                            txtima2.setVisibility(View.INVISIBLE);
                            btncamara.setEnabled(false);
                            btnEnviar.setBackgroundColor(Color.parseColor("#088A08"));
                            btnEnviar.setText("Informar Llegada");
                            break;
                        case "Salida":
                            factura.setEnabled(true);
                            sello.setEnabled(true);
                            bultos.setEnabled(true);
                            peso.setEnabled(true);
                            porcentaje.setEnabled(true);
                            ima1.setEnabled(true);
                            ima2.setEnabled(true);
                            txtima.setVisibility(View.VISIBLE);
                            txtima2.setVisibility(View.VISIBLE);
                            btncamara.setEnabled(true);
                            btnEnviar.setBackgroundColor(Color.parseColor("#088A08"));
                            btnEnviar.setText("Informar Salida");
                            break;

                    }
                }catch (Exception e){
                    Toast.makeText(enviarActivity.this, "Error ChangeSpinner", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {

                Uri selectedImage = data.getData();
                ima1.setImageURI(selectedImage);
                txtima.setVisibility(View.INVISIBLE);
                //   logocam1.setVisibility(View.INVISIBLE);

            }
            if (requestCode == RESULT_LOAD_IMAGE2 && resultCode == RESULT_OK && data != null) {

                Uri selectedImage = data.getData();
                ima2.setImageURI(selectedImage);
                txtima2.setVisibility(View.INVISIBLE);
                // logocam2.setVisibility(View.INVISIBLE);
            }
        }catch (Exception e){
            Toast.makeText(enviarActivity.this, "Error al cargar imagen\nIntentelo de Nuevo ", Toast.LENGTH_LONG).show();
        }
    }


    private void PantallaCarga(){

        frameLayout.setVisibility(View.VISIBLE);

        tramo.setVisibility(View.INVISIBLE);
        factura.setVisibility(View.INVISIBLE);
        sello.setVisibility(View.INVISIBLE);
        bultos.setVisibility(View.INVISIBLE);
        peso.setVisibility(View.INVISIBLE);
        porcentaje.setVisibility(View.INVISIBLE);
        btnEnviar.setVisibility(View.INVISIBLE);
        spinner.setVisibility(View.INVISIBLE);
        ima1.setVisibility(View.INVISIBLE);
        ima2.setVisibility(View.INVISIBLE);
        txtima.setVisibility(View.INVISIBLE);
        txtima2.setVisibility(View.INVISIBLE);
        btncamara.setVisibility(View.INVISIBLE);

    }


    private void MostrarElementos(){

        frameLayout.setVisibility(View.INVISIBLE);

        tramo.setVisibility(View.VISIBLE);
        factura.setVisibility(View.VISIBLE);
        sello.setVisibility(View.VISIBLE);
        bultos.setVisibility(View.VISIBLE);
        peso.setVisibility(View.VISIBLE);
        porcentaje.setVisibility(View.VISIBLE);
        btnEnviar.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.VISIBLE);
        ima1.setVisibility(View.VISIBLE);
        ima2.setVisibility(View.VISIBLE);
        txtima.setVisibility(View.VISIBLE);
        txtima2.setVisibility(View.VISIBLE);
        btncamara.setVisibility(View.VISIBLE);
    }


    private void AlertEnviar() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(enviarActivity.this);
        builder.setMessage("Antes de enviar su informacion favor de revisarla para evitar algun error")
                .setCancelable(false)
                .setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                        try {

                            txtspinner = spinner.getSelectedItem().toString();

                            switch(txtspinner) {
                                case "Salida":


                                    txtfactura = factura.getText().toString();
                                    txtsello = sello.getText().toString();
                                    txtbultos = bultos.getText().toString();
                                    txtpeso = peso.getText().toString();
                                    txtporcentaje = porcentaje.getText().toString();

                                    Drawable uri1 = ima1.getDrawable();
                                    Drawable uri2 = ima2.getDrawable();

                                    if (    txtfactura == null  ||  txtfactura.length()==0 ||
                                            txtsello == null ||    txtsello.length()==0 ||
                                            txtbultos == null || txtbultos.length()==0 ||
                                            txtpeso == null || txtpeso.length()==0 ||
                                            txtporcentaje == null || txtporcentaje.length()==0 ||
                                            uri1 == null ||
                                            uri2 == null ) {
                                        Toast.makeText(enviarActivity.this, "Campos Vacios ", Toast.LENGTH_LONG).show();
                                        break;
                                    }else {

                                        //Toast.makeText(enviarActivity.this, " SALIDA", Toast.LENGTH_LONG).show();
                                        PantallaCarga();

                                        Bitmap image = ((BitmapDrawable) ima1.getDrawable()).getBitmap();
                                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                        image.compress(Bitmap.CompressFormat.JPEG,30 ,byteArrayOutputStream);
                                        encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT);

                                        Bitmap image2 = ((BitmapDrawable) ima2.getDrawable()).getBitmap();
                                        ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
                                        image2.compress(Bitmap.CompressFormat.JPEG,30 ,byteArrayOutputStream2);
                                        encodedImage2 = Base64.encodeToString(byteArrayOutputStream2.toByteArray(),Base64.DEFAULT);


                                SegundoPlano segundoPlano = new SegundoPlano();
                                segundoPlano.execute();
                                    }
                                    break;
                                case "Llegada":
                                   // Toast.makeText(enviarActivity.this, " LLEGADA", Toast.LENGTH_LONG).show();
                                   PantallaCarga();


                                    SegundoPlano segundoPlano = new SegundoPlano();
                                    segundoPlano.execute();
                                    break;

                            }




                        }catch (Exception e){
                            Toast.makeText(enviarActivity.this, "Error btnenviar", Toast.LENGTH_LONG).show();
                        }

                    }
                })

                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alert = builder.create();
        alert.show();
    }


    private void AlertRed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(enviarActivity.this);
        builder.setMessage("Señal de red debil o no cuenta con datos moviles!")
                .setCancelable(false)
                .setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


        alert = builder.create();
        alert.show();
    }


    private class SegundoPlano extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String SOAP_ACTION = "http://release.dxxpress.net/wsInspeccion/Version_20171221_1212";
            String METHOD_NAME = "DetalleViajeDatosValidar";
            String NAMESPACE  = "http://release.dxxpress.net/wsInspeccion/";
            String URL = "http://release.dxxpress.net/wsInspeccion/interfaceOperadores3.asmx";

            try{

                fechatxt  = (String) android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss", new Date());

                SoapObject Request = new SoapObject(NAMESPACE,METHOD_NAME);
                Request.addProperty("fecha", fechatxt);
                Request.addProperty("idDetalleViaje", idDetalleViaje);
                Request.addProperty("idViaje", idViaje);
                Request.addProperty("tipoMovimiento", txtspinner);

                Request.addProperty("factura", txtfactura);
                Request.addProperty("sello", txtsello);
                Request.addProperty("bultos", txtbultos);
                Request.addProperty("peso", txtpeso);
                Request.addProperty("porcLlenado", txtporcentaje);
                Request.addProperty("img641", encodedImage);
                Request.addProperty("img642", encodedImage2);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport= new HttpTransportSE(URL);
                transport.call(SOAP_ACTION, soapEnvelope);

                resultString3 = (Object) soapEnvelope.getResponse();

                mensaje= "OK";

            }catch (Exception ex){

                mensaje = "ERROR: " +ex.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


            try {
                tran = resultString3.toString();

                if (mensaje == "OK" && tran == "202") {

                    Toast.makeText(enviarActivity.this, "Enviado Correctamente", Toast.LENGTH_LONG).show();

                    Intent i = new Intent(enviarActivity.this, splash.class);
                    startActivity(i);

                } else {

                    Toast.makeText(enviarActivity.this, "Error al enviar", Toast.LENGTH_LONG).show();

                    MostrarElementos();

                }


            }catch (Exception ex){

                Toast.makeText(enviarActivity.this, "Error al enviar", Toast.LENGTH_LONG).show();

                MostrarElementos();
            }

            super.onPostExecute(aVoid);
        }
    }
}
