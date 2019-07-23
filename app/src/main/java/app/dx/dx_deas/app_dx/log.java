package app.dx.dx_deas.app_dx;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.telephony.TelephonyManager;
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

    private EditText usu, pass,numemp;
    private Button ingre;
    String login;
    String password;
    String mensaje;
    String idempleado;
    String tran;
    SoapPrimitive resultString;


    String deviceIMEI;
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_log);

        usu = (EditText) findViewById(R.id.editTextUser);
        pass = (EditText)findViewById(R.id.editTextPass);
        ingre = (Button)findViewById(R.id.btnIngresar);
        numemp = (EditText)findViewById(R.id.editTextNumEmp);






        if (!isTaskRoot()
                && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
                && getIntent().getAction() != null
                && getIntent().getAction().equals(Intent.ACTION_MAIN)) {

            finish();
            return;
        }



            ingre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    idempleado = numemp.getText().toString();
                    login = usu.getText().toString();
                    password = pass.getText().toString();


                    //Filtros del login
                    if (login.length()==0 && password.length()==0 && idempleado.length() == 0){
                        Toast.makeText(log.this, "Campos vacios", Toast.LENGTH_SHORT).show();
                    }
                    else if (login.length()==0 ){
                        Toast.makeText(log.this, "Debes Ingresar un Usuario", Toast.LENGTH_SHORT).show();

                    }
                    else if (password.length()==0){
                        Toast.makeText(log.this, "Debes Ingresar una Contraseña ", Toast.LENGTH_SHORT).show();

                    }
                    else if (idempleado.length() == 0){
                        Toast.makeText(log.this, "Debes su Numero de Empleado ", Toast.LENGTH_SHORT).show();

                    }
                    else if (login.length() !=0 && password.length() !=0 && idempleado.length() != 0) {
                        SegundoPlano tarea = new SegundoPlano();
                        tarea.execute();
                    }

                }
            });


    }

    @Override
    protected void onStart() {

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        super.onStart();
    }

    ///////////////////////////////////////////////////////////////

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }



        public void doSomthing() {
            deviceIMEI = getDeviceIMEI(log.this);

            //andGoToYourNextStep
        }

        @SuppressLint("HardwareIds")
        public static String getDeviceIMEI(Activity activity) {

            String deviceUniqueIdentifier = null;
            TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
            if (null != tm) {
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                else
                    deviceUniqueIdentifier = tm.getDeviceId();
                if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length())
                    deviceUniqueIdentifier = "0";
            }
            return deviceUniqueIdentifier;
    }

    ///////////////////////////////////////////////////

    @Override
    public void onBackPressed() {

    }

    /////////////////////////////////////









///////////////////////////////////////////////////////////////////

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

                    SharedPreferences preferences = getSharedPreferences ("credenciales", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("user", login);
                    editor.putString("pass", password);
                    editor.putString("emp", idempleado);
                    editor.commit();

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
                    i.putExtra("emp",idempleado);
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
                Request.addProperty("claveoperador", idempleado);

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