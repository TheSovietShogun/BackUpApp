package app.dx.dx_deas.app_dx;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.ksoap2.SoapEnvelope;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class log extends AppCompatActivity {

    private EditText usu, pass;
    private Button ingre;
    String login;
    String password;
    String mensaje;
    String tran;
    SoapPrimitive resultString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        usu = (EditText) findViewById(R.id.editTextUser);
        pass = (EditText)findViewById(R.id.editTextPass);
        ingre = (Button)findViewById(R.id.btnIngresar);

        runtime_permissions();


        ingre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                login = usu.getText().toString();
                password = pass.getText().toString();


                //Filtros del login
                if (login.length()==0 && password.length()==0){
                    Toast.makeText(log.this, "Campos vacios", Toast.LENGTH_SHORT).show();
                }
                else if (login.length()==0 ){
                    Toast.makeText(log.this, "Debes Ingresar un Usuario", Toast.LENGTH_SHORT).show();

                }
                else if (password.length()==0){
                    Toast.makeText(log.this, "Debes Ingresar una Contra ", Toast.LENGTH_SHORT).show();

                }
                else if (login.length() !=0 && password.length() !=0) {
                    SegundoPlano tarea = new SegundoPlano();
                    tarea.execute();
                }



            }
        });
    }

    private boolean runtime_permissions(){
        if (Build.VERSION.SDK_INT>=23 &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)

        { requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},100);

            return true;
        }else {
            return false;
        }

    }
    //Se solicita permisos al usuario  y se Inicia el servicio
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode== 100){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager
                    .PERMISSION_GRANTED) {
            }
        }

    }





    private class SegundoPlano extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            try {

                //Respuesta de la llamada , es un json
                tran = resultString.toString();


                //Si el usuario no existe el servicio regresara el string {"CUsuario":[{}] }
                if (tran.length() <= 15) {
                    Toast.makeText(log.this, "Cuenta Equivocada", Toast.LENGTH_SHORT).show();
                } else {

                    //Libreria gson se utliza para traducir de json a string y viceversa
                    Gson gson = new Gson();

                    //Retira la palabra usuario y los corchetes para el uso de la libreria gson
                    String reusu = tran.replace("{\"Usuario\":[{", "{");
                    String reusu2 = reusu.replace("}] }", "}");

                    //Se usa la libreria para traducir el json , el string obtenido se almacena en la libreria CUsuario
                    CUsuario Usuario1 = gson.fromJson(reusu2, CUsuario.class);

                    String nombreOperador = Usuario1.getNombreOperador();
                    String idEmpresa = Usuario1.getIdEmpresa();
                    String idUsuario = Usuario1.getIdUsuario();
                    String nombreUsuario = Usuario1.getNombreUsuario();
                    String idUnidad = Usuario1.getIdUnidad();
                    String claveUnidad = Usuario1.getClaveUnidad();
                    String idOperador = Usuario1.getIdOperador();
                    String idFlota = Usuario1.getIdFlota();
                    String idViaje = Usuario1.getIdViaje();

                    //Se llama a la siguiente actividad y se envian variables
                    Intent i = new Intent(log.this, nd_menuPrincipal.class);
                    i.putExtra("nombreOperador", nombreOperador);
                    i.putExtra("nombreUsuario", nombreUsuario);
                    i.putExtra("idUnidad", idUnidad);
                    i.putExtra("idUsuario", idUsuario);
                    i.putExtra("idOperador", idOperador);
                    i.putExtra("idViaje", idViaje);
                    startActivity(i);

                }
            }catch (Exception ex){
                Toast.makeText(log.this, "Error 404", Toast.LENGTH_SHORT).show();
            }

            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String SOAP_ACTION = "http://dxxpress.net/wsInspeccion/Version_20171221_1212";
            String METHOD_NAME = "Login";
            String NAMESPACE  = "http://dxxpress.net/wsInspeccion/";
            String URL = "http://dxxpress.net/wsInspeccion/interfaceOperadores3.asmx";


            try{

                SoapObject Request = new SoapObject(NAMESPACE,METHOD_NAME);
                Request.addProperty("login", login);
                Request.addProperty("password", password);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport= new HttpTransportSE(URL);
                transport.call(SOAP_ACTION, soapEnvelope);


                resultString = (SoapPrimitive) soapEnvelope.getResponse();


                mensaje= "OK";

            }catch (Exception ex){

                mensaje = "ERROR: " +ex.getMessage();
            }
            return null;
        }
    }




}