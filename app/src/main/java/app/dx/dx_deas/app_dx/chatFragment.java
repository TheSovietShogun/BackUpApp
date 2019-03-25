package app.dx.dx_deas.app_dx;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class chatFragment extends Fragment {

    String id;
    String idViaje;
    String idUnidad;
    String responseString = "";
    EditText etcht;
    Button btnenv;
    String mensaje;
    String idUsuario;
    SoapPrimitive resultString;
    String respuesta;
    String JsonMen;
    SoapObject resultString2;
    String respuesta2;
    String JsonMen2;
    ListView lista2 ;
    ImageButton refresh ;
    SoapPrimitive resultString3;
    String respuesta3;
    String mensaje3;
    String tran3 ;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent get = getActivity().getIntent();
        //idViaje = get.getStringExtra("idViaje");
        idUsuario = get.getStringExtra("idUsuario");
        idUnidad = get.getStringExtra("idUnidad");




    }

    @Override
    public void onStart() {
        super.onStart();
        WS_ViajeActual viaje = new WS_ViajeActual();
        viaje.execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);


        lista2 = (ListView) view.findViewById(R.id.listachat);
        etcht = (EditText) view.findViewById(R.id.edittext_chatbox);
        btnenv = (Button) view.findViewById(R.id.button_chatbox_send);

        btnenv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mensaje = etcht.getText().toString();

                if (mensaje.length() == 0){

                    Toast.makeText(getActivity(), "Ingrese un Mensaje", Toast.LENGTH_SHORT).show();
                }else{
                    EnviarMensajesWS envia = new EnviarMensajesWS();
                    envia.execute();

                    WS_ViajeActual viaje = new WS_ViajeActual();
                    viaje.execute();
                }


            }
        });

        refresh = (ImageButton) view.findViewById(R.id.iBRrefresh);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragManager = getActivity().getSupportFragmentManager();
                fragManager.beginTransaction().replace(R.id.contenedor,new chatFragment()).commit();
            }
        });

        return view;
    }


    private class CargaMensajesWS extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            String SOAP_ACTION = "http://dxxpress.net/wsInspeccion/Version_20171221_1212";
            String METHOD_NAME = "ChatSelect";
            String NAMESPACE = "http://dxxpress.net/wsInspeccion/";
            String URL = "http://dxxpress.net/wsInspeccion/interfaceOperadores3.asmx";



            try {
                //SE CREA UN OBJETO SOAP Y SE LE AGREGAN LOS PARAMETROS DE ENTRADA
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("idViaje", idViaje);
                Request.addProperty("idUsuario", idUsuario);
                Request.addProperty("idUnidad", idUnidad);


                //SE EMPAQUETA EL OBJETO ,SE LE ASIGNA UNA VERSION (V11,V12) Y SE ESCRIBE EL LENGUAJE DONDE FUE CREADO EL WS
                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.call(SOAP_ACTION, soapEnvelope);


                //SE OBTIENE LA RESPUESTA Y SE LE ASIGNA UNA VARIABLE

                resultString = (SoapPrimitive) soapEnvelope.getResponse();


                respuesta = "OK";

            } catch (Exception ex) {

                respuesta = "ERROR: " + ex.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            JsonMen = resultString.toString();
            try {


                    Gson gson = new Gson();
                    String reusu = JsonMen.replace("{\"Chat\":[{", "[{");
                    String reusu2 = reusu.replace("}] }", "}]");


                        Type chatListType = new TypeToken<ArrayList<CChat>>() {
                        }.getType();
                        List<CChat> chats = new Gson().fromJson(reusu2, chatListType);

                        ArrayAdapter adapter2 = new chatAdapter(getActivity(), chats);

                        lista2.setAdapter(adapter2);





            } catch (Exception e) {
                //.Toast.makeText(getActivity(), "Error 404C", Toast.LENGTH_SHORT).show();
                System.out.println("ERROR : " + e);
            }
        }

    }

    private class EnviarMensajesWS extends AsyncTask <Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            String SOAP_ACTION = "http://dxxpress.net/wsInspeccion/Version_20171221_1212";
            String METHOD_NAME = "ChatSave";
            String NAMESPACE = "http://dxxpress.net/wsInspeccion/";
            String URL = "http://dxxpress.net/wsInspeccion/interfaceOperadores3.asmx";

            try {
                //SE CREA UN OBJETO SOAP Y SE LE AGREGAN LOS PARAMETROS DE ENTRADA
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("idViaje", idViaje);
                Request.addProperty("mensaje", mensaje);
                Request.addProperty("idUsuario", idUsuario);
                Request.addProperty("idUnidad", idUnidad);

                //SE EMPAQUETA EL OBJETO ,SE LE ASIGNA UNA VERSION (V11,V12) Y SE ESCRIBE EL LENGUAJE DONDE FUE CREADO EL WS
                SoapSerializationEnvelope soapEnvelope2 = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                soapEnvelope2.dotNet = true;
                soapEnvelope2.setOutputSoapObject(Request);

                HttpTransportSE transport2 = new HttpTransportSE(URL);
                transport2.call(SOAP_ACTION, soapEnvelope2);


                //SE OBTIENE LA RESPUESTA Y SE LE ASIGNA UNA VARIABLE

                resultString2 = (SoapObject) soapEnvelope2.getResponse();


                respuesta2 = "OK";

            } catch (Exception ex) {

                respuesta2 = "ERROR: " + ex.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                if (respuesta2 == "OK") {
                    etcht.setText("");
                    Toast.makeText(getActivity(), "Mensaje Enviado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Error 404E", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                Toast.makeText(getActivity(), "Error 404E", Toast.LENGTH_SHORT).show();
                System.out.println("ERROR : " + e);
            }
        }
    }

    private class WS_ViajeActual extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            String SOAP_ACTION = "http://dxxpress.net/wsInspeccion/Version_20171221_1212";
            String METHOD_NAME = "ViajeActual";
            String NAMESPACE  = "http://dxxpress.net/wsInspeccion/";
            String URL = "http://dxxpress.net/wsInspeccion/interfaceOperadores3.asmx";


            try{
                //SE CREA UN OBJETO SOAP Y SE LE AGREGAN LOS PARAMETROS DE ENTRADA
                SoapObject Request = new SoapObject(NAMESPACE,METHOD_NAME);
                Request.addProperty("idUnidad", idUnidad);
                Request.addProperty("idUsuario", idUsuario);

                //SE EMPAQUETA EL OBJETO ,SE LE ASIGNA UNA VERSION (V11,V12) Y SE ESCRIBE EL LENGUAJE DONDE FUE CREADO EL WS
                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport= new HttpTransportSE(URL);
                transport.call(SOAP_ACTION, soapEnvelope);


                //SE OBTIENE LA RESPUESTA Y SE LE ASIGNA UNA VARIABLE

                resultString3 = (SoapPrimitive) soapEnvelope.getResponse();


                mensaje3= "OK";

            }catch (Exception ex){

                mensaje3 = "ERROR: " +ex.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {


                    //Respuesta de la llamada , es un json
                    tran3 = resultString3.toString();

                    //Si el usuario no existe el servicio regresara el string {"Usuario":[{}] }

                    //Libreria gson se utliza para traducir de json a string y viceversa
                    Gson gson = new Gson();

                    //Retira la palabra usuario y los corchetes para el uso de la libreria gson
                    String reusu = tran3.replace("{\"Viaje\":[{", "{");
                    String reusu2 = reusu.replace("}] }", "}");

                    //Se usa la libreria para traducir el json , el string obtenido se almacena en la libreria Usuario
                    CViajeActual viaje = gson.fromJson(reusu2, CViajeActual.class);

                    idViaje = viaje.getIdViaje();
                    String folioViaje = viaje.getFolioViaje();
                    String tracking = viaje.getTracking();
                    String idCliente = viaje.getIdCliente();
                    String idRuta = viaje.getIdRuta();
                    String nombreRuta = viaje.getNombreRuta();
                    String secuencia = viaje.getSecuencia();
                    String idDetalleViaje = viaje.getIdDetalleViaje();
                    String idDetalleViajeDestino = viaje.getIdDetalleViajeDestino();
                    String idOperador = viaje.getIdOperador();
                    String idOperador2 = viaje.getIdOperador2();
                    String idRemolquePrincipal = viaje.getIdRemolquePrincipal();
                    String nombreTramo = viaje.getNombreTramo();
                    String direccionTramo = viaje.getDireccionTramo();
                    String descripcionCarga = viaje.getDescripcionCarga();
                    String descripcionTipoMovimiento = viaje.getDescripcionTipoMovimiento();
                    String nombreEstatus = viaje.getNombreEstatus();
                    String nombreUnidad = viaje.getNombreUnidad();
                    String nombreOperador = viaje.getNombreOperador();
                    String nombreOperador2 = viaje.getNombreOperador2();
                    String nombreRemolquePrincipal = viaje.getNombreRemolquePrincipal();
                    String nombreDestino = viaje.getNombreDestino();
                    String latitudDestino = viaje.getLatitudDestino();
                    String longitudDestino = viaje.getLongitudDestino();


                    CargaMensajesWS carga = new CargaMensajesWS();
                    carga.execute();


            }catch (Exception ex){
                //Toast.makeText(getActivity(), "Viaje No Asignado", Toast.LENGTH_SHORT).show();
                mensaje3 = "ERROR: " +ex.getMessage();
            }

            CargaMensajesWS carga = new CargaMensajesWS();
            carga.execute();

            super.onPostExecute(aVoid);
        }
    }

}







