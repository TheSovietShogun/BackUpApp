package app.dx.dx_deas.app_dx;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class cerrar extends AppCompatActivity {

    private String login , password ,tran , mensaje;
    private  SoapPrimitive resultString ;
    private EditText unidad , contrseña ;
    private Button btnSalir ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cerrar);


        unidad = (EditText) findViewById(R.id.c_unidad);
        contrseña = (EditText) findViewById(R.id.c_contra);
        btnSalir = (Button) findViewById(R.id.btnSalir);

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login = unidad.getText().toString();
                password = contrseña.getText().toString();

                if (login.length()==0 && password.length()==0){
                    Toast.makeText(cerrar.this, "Campos vacios", Toast.LENGTH_SHORT).show();
                }
                else if (login.length()==0 ){
                    Toast.makeText(cerrar.this, "Debes Ingresar un Usuario", Toast.LENGTH_SHORT).show();

                }
                else if (password.length()==0){
                    Toast.makeText(cerrar.this, "Debes Ingresar una Contraseña ", Toast.LENGTH_SHORT).show();

                }
                else if (login.length() !=0 && password.length() !=0) {



                    SegundoPlano segundoPlano = new SegundoPlano();
                    segundoPlano.execute();
                }






            }
        });

    }

    private class SegundoPlano extends AsyncTask<Void, Void, Void> {


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

        @Override
        protected void onPostExecute(Void aVoid) {

            try {

                //Respuesta de la llamada , es un json
                tran = resultString.toString();

                //Si el usuario no existe el servicio regresara el string {"CUsuario":[{}] }
                if (tran.length() <= 15) {
                    Toast.makeText(cerrar.this, "Cuenta Equivocada", Toast.LENGTH_SHORT).show();
                } else {

                    stopService(new Intent(cerrar.this ,notifi_Service.class));
                        stopService(new Intent(cerrar.this , GPS_Service.class));

                        SharedPreferences preferences = getSharedPreferences ("credenciales", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("user", "");
                        editor.putString("pass", "");
                        editor.putString("emp", "");
                        editor.commit();


                        Intent i = new Intent(cerrar.this, splash.class);
                        startActivity(i);

                }
            }catch (Exception ex){
                Toast.makeText(cerrar.this, "Error 404", Toast.LENGTH_SHORT).show();
            }

            super.onPostExecute(aVoid);
        }
    }

}
