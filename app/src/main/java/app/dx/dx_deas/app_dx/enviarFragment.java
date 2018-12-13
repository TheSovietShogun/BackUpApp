package app.dx.dx_deas.app_dx;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class enviarFragment extends Fragment {

    String idViaje;
    String tran;
    String responseString = "";
    String direccionTramo;
    SoapObject resultString;
    SoapPrimitive resultString3;
    SoapPrimitive resultString2;
    String mensaje;
    String mensaje2;
    EditText facturatxt, sellotxt, bultostxt, pesotxt, porcentajetxt;
    CheckBox checkEntr,checkSal ;
    TextView tramo , facturatv , sellotv , bultostv , pesotv, porcentajetv ,txtlle ;
    Button btnenviar;
    String llegada;
    String factura;
    String sello;
    String bultos;
    String peso;
    String porcentaje;
    String idDetalleViaje;
    String respuesta;
    Boolean selec ;
    String idUsuario,idUnidad ;
    String fechaSalida ,fechaEntrada;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent get = getActivity().getIntent();
        idUsuario = get.getStringExtra("idUsuario");
        idUnidad = get.getStringExtra("idUnidad");



    }

    @Override
    public void onStart() {
        super.onStart();
        WS_ViajeActual viajeActual = new WS_ViajeActual();
        viajeActual.execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_enviar, container, false);

        String fechatxt  = (String) android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss", new Date());

        tramo = (TextView) view.findViewById(R.id.tramoActualtxt);

        facturatxt = (EditText) view.findViewById(R.id.facturatxt);
        sellotxt = (EditText) view.findViewById(R.id.sellotxt);
        bultostxt = (EditText) view.findViewById(R.id.bultostxt);
        pesotxt = (EditText) view.findViewById(R.id.pesotxt);
        porcentajetxt = (EditText) view.findViewById(R.id.porcentajetxt);

        btnenviar = (Button) view.findViewById(R.id.btnEnviar);

        checkEntr = (CheckBox) view.findViewById(R.id.checkBoxEntr);
        checkSal = (CheckBox) view.findViewById(R.id.checkBoxSal);

        facturatv = (TextView) view.findViewById(R.id.factura);
        sellotv = (TextView) view.findViewById(R.id.sello);
        bultostv = (TextView) view.findViewById(R.id.bultos);
        pesotv = (TextView) view.findViewById(R.id.peso);
        porcentajetv = (TextView) view.findViewById(R.id.llenado);

        txtlle= (TextView) view.findViewById(R.id.textLle);

        facturatv.setVisibility(View.INVISIBLE);
        sellotv.setVisibility(View.INVISIBLE);
        bultostv.setVisibility(View.INVISIBLE);
        pesotv.setVisibility(View.INVISIBLE);
        porcentajetv.setVisibility(View.INVISIBLE);


        facturatxt.setVisibility(View.INVISIBLE);
        sellotxt.setVisibility(View.INVISIBLE);
        bultostxt.setVisibility(View.INVISIBLE);
        pesotxt.setVisibility(View.INVISIBLE);
        porcentajetxt.setVisibility(View.INVISIBLE);

        txtlle.setVisibility(View.INVISIBLE);
        btnenviar.setVisibility(View.INVISIBLE);


        boolean EntInput = checkEntr.isChecked();
        boolean SalInput = checkSal.isChecked();


        checkEntr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(checkEntr.isChecked())  //Check if first is checked
                {
                    checkSal.setChecked(false);
                    txtlle.setVisibility(View.VISIBLE);
                    btnenviar.setVisibility(View.VISIBLE);


                }else{
                    txtlle.setVisibility(View.INVISIBLE);
                    btnenviar.setVisibility(View.INVISIBLE);
                }


            }
        });

        checkSal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(checkSal.isChecked())  //Check if first is checked
                {
                    btnenviar.setEnabled(true);

                    checkEntr.setChecked(false);
                    facturatv.setVisibility(View.VISIBLE);
                    sellotv.setVisibility(View.VISIBLE);
                    bultostv.setVisibility(View.VISIBLE);
                    pesotv.setVisibility(View.VISIBLE);
                    porcentajetv.setVisibility(View.VISIBLE);


                    facturatxt.setVisibility(View.VISIBLE);
                    sellotxt.setVisibility(View.VISIBLE);
                    bultostxt.setVisibility(View.VISIBLE);
                    pesotxt.setVisibility(View.VISIBLE);
                    porcentajetxt.setVisibility(View.VISIBLE);

                    btnenviar.setVisibility(View.VISIBLE);
                }else{
                    facturatv.setVisibility(View.INVISIBLE);
                    sellotv.setVisibility(View.INVISIBLE);
                    bultostv.setVisibility(View.INVISIBLE);
                    pesotv.setVisibility(View.INVISIBLE);
                    porcentajetv.setVisibility(View.INVISIBLE);


                    facturatxt.setVisibility(View.INVISIBLE);
                    sellotxt.setVisibility(View.INVISIBLE);
                    bultostxt.setVisibility(View.INVISIBLE);
                    pesotxt.setVisibility(View.INVISIBLE);
                    porcentajetxt.setVisibility(View.INVISIBLE);

                    btnenviar.setVisibility(View.INVISIBLE);

                }

            }
        });



        btnenviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                factura = facturatxt.getText().toString();
                sello = sellotxt.getText().toString();
                bultos = bultostxt.getText().toString();
                peso = pesotxt.getText().toString();
                porcentaje = porcentajetxt.getText().toString();
                if (checkSal.isChecked()){
                    fechaSalida = (String) android.text.format.DateFormat.format("yyyy-MM-dd'T'hh:mm:ss", new Date());
                }
                if (checkEntr.isChecked()){
                    fechaEntrada = (String) android.text.format.DateFormat.format("yyyy-MM-dd'T'hh:mm:ss", new Date());
                }
                WS_Enviar ws_enviar = new WS_Enviar();
                ws_enviar.execute();
            }
        });

        return view;
    }




    private class WS_Enviar extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            String SOAP_ACTION = "http://dxxpress.net/wsInspeccion/Version_20171221_1212";
            String METHOD_NAME = "DatosCargaSave";
            String NAMESPACE = "http://dxxpress.net/wsInspeccion/";
            String URL = "http://dxxpress.net/wsInspeccion/interfaceOperadores3.asmx";
            String id = "";

            try {
                //SE CREA UN OBJETO SOAP Y SE LE AGREGAN LOS PARAMETROS DE ENTRADA
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("factura", factura);
                Request.addProperty("porcLlenado", porcentaje);
                Request.addProperty("sello", sello);
                Request.addProperty("peso", peso);
                Request.addProperty("bultos", bultos);
                Request.addProperty("idDetalleViaje", idDetalleViaje);
                Request.addProperty("fechaEntrada", fechaEntrada);
                Request.addProperty("fechaSalida", fechaSalida);


                //SE EMPAQUETA EL OBJETO ,SE LE ASIGNA UNA VERSION (V11,V12) Y SE ESCRIBE EL LENGUAJE DONDE FUE CREADO EL WS
                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.call(SOAP_ACTION, soapEnvelope);


                //SE OBTIENE LA RESPUESTA Y SE LE ASIGNA UNA VARIABLE

                resultString = (SoapObject) soapEnvelope.getResponse();


                respuesta = "OK";

            } catch (Exception ex) {
                respuesta = "ERROR: " + ex.getMessage();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
 try {
     if (respuesta != "OK") {
         Toast.makeText(getActivity(), "Error 404E", Toast.LENGTH_SHORT).show();
     }else {
         Toast.makeText(getActivity(), "Datos Enviados", Toast.LENGTH_SHORT).show();

         facturatxt.setText("");
         sellotxt.setText("");
         bultostxt.setText("");
         pesotxt.setText("");
         porcentajetxt.setText("");

         FragmentManager fragManager = getActivity().getSupportFragmentManager();
         fragManager.beginTransaction().replace(R.id.contenedor,new viajeFragment()).commit();



     }
 }catch (Exception e ){
     Toast.makeText(getActivity(), "Error 404E", Toast.LENGTH_SHORT).show();
 }



        }
    }

    private class WS_ViajeActual extends AsyncTask<Void, Void, Void> {

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

                resultString2 = (SoapPrimitive) soapEnvelope.getResponse();


                mensaje2= "OK";

            }catch (Exception ex){

                mensaje2 = "ERROR: " +ex.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {


                    //Respuesta de la llamada , es un json
                    tran = resultString2.toString();


                    //Si el usuario no existe el servicio regresara el string {"Usuario":[{}] }
                    if (tran.length() <= 15) {
                        Toast.makeText(getActivity(), "Viaje No Asignado", Toast.LENGTH_SHORT).show();
                        checkSal.setEnabled(false);
                        checkEntr.setEnabled(false);

                    } else {
                        checkSal.setEnabled(true);
                        checkEntr.setEnabled(true);
                        //Libreria gson se utliza para traducir de json a string y viceversa
                        Gson gson = new Gson();

                        //Retira la palabra usuario y los corchetes para el uso de la libreria gson
                        String reusu = tran.replace("{\"Viaje\":[{", "{");
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
                        idDetalleViaje = viaje.getIdDetalleViaje();
                        String idDetalleViajeDestino = viaje.getIdDetalleViajeDestino();
                        String idOperador = viaje.getIdOperador();
                        String idOperador2 = viaje.getIdOperador2();
                        String idRemolquePrincipal = viaje.getIdRemolquePrincipal();
                        String nombreTramo = viaje.getNombreTramo();
                        direccionTramo = viaje.getDireccionTramo();
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

                        tramo.setText(nombreTramo);
                        txtlle.setText("INFORMAR LLEGADA\n\n" + "Unidad : " + nombreUnidad + "\n\n" + "Operador : " + nombreOperador + "\n\n" + "Destino Final : " + nombreDestino + "\n\n"
                                + "Ruta : " + nombreRuta + "\n\n" + "Remolque : " + nombreRemolquePrincipal);
                    }



            }catch (Exception ex){
                Toast.makeText(getActivity(), "Error 404VA", Toast.LENGTH_SHORT).show();
                mensaje2 = "ERROR: " +ex.getMessage();
            }


            super.onPostExecute(aVoid);
        }
    }


}