package app.dx.dx_deas.app_dx;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Date;

import javax.xml.transform.Templates;

public class enviarActivity extends AppCompatActivity {

    private Button btnEnviar;
    private AlertDialog alert  ;
    private Spinner spinner ;
    private EditText factura,sello,bultos,peso,porcentaje;
    private ImageView ima1 ;
    private TextView tramo ;
    private static final int RESULT_LOAD_IMAGE = 1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar);

        String fechatxt  = (String) android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss", new Date());

        tramo = (TextView) findViewById(R.id.tramoActualtxt);
        factura = (EditText) findViewById(R.id.facturatxt);
        sello = (EditText) findViewById(R.id.sellotxt);
        bultos = (EditText) findViewById(R.id.bultostxt);
        peso = (EditText) findViewById(R.id.pesotxt);
        porcentaje = (EditText) findViewById(R.id.porcentajetxt);
        btnEnviar = (Button) findViewById(R.id.btnEnviar);
        spinner = (Spinner) findViewById(R.id.spinner1);
        ima1 = (ImageView) findViewById(R.id.imagen_select);


        String [] values = {"Llegada","Salida",};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        ima1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent,RESULT_LOAD_IMAGE);
            }
        });


        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertEnviar();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            ima1.setImageURI(selectedImage);
        }
    }

    private void AlertEnviar() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(enviarActivity.this);
        builder.setMessage("Antes de enviar su informacion favor de revisarla para evitar algun error")
                .setCancelable(false)
                .setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(enviarActivity.this, splash.class);
                        startActivity(i);


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
}
