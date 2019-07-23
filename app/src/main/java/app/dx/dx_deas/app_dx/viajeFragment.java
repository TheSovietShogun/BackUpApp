package app.dx.dx_deas.app_dx;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.Date;

public class viajeFragment extends Fragment {


    private Button btnTramos,btnViaje ,btnsolcitar;
    private String nombreOperador;
    private String idUnidad;
    private String idUsuario;
    private View v1,v2;
    private TextView desttxt,foliotxt,loadtxt,rutatxt,cajatxt,unidadtxt,direTramotxt,ventanatxt,estimadotxt;
    private TextView dest,folio,load,ruta,caja,unidad,ventana,estimado,welcome,TTarde ;
    String tran,tran2 ;
    String responseString = "";
    String direccionTramo;
    SoapPrimitive resultString;
    SoapPrimitive resultString2;
    String mensaje;
    String mensaje2;
    String latitudDestino ;
    String longitudDestino ;
    String idViaje;
    final Fragment tramos = new tramosFragment();
    ImageButton refresh ;
    boolean btn = true;
    //final Fragment viajeActual = new viajeActualFragment();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        Intent get = getActivity().getIntent();
        nombreOperador = get.getStringExtra("nombreOperador");
        idUsuario = get.getStringExtra("idUsuario");
        idUnidad = get.getStringExtra("idUnidad");

        SegundoPlano llamadaWS = new SegundoPlano();
        llamadaWS.execute();

    }



    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_viaje, container, false);

        try {
                ConnectivityManager manager =(ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
                if (null != activeNetwork) {
                if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                //we have WIFI
                    String uwu ;
                 }
                } else{
                //we have no connection :(
                    Toast.makeText(getActivity(), "Red : ALERTA! No esta conectado a ninguna red", Toast.LENGTH_LONG).show();
                }

        }catch (Exception e ){
            String ovo = "ERROR: " +e.getMessage();
                }


        try {
            if(Settings.Secure.getInt(getActivity().getContentResolver(), Settings.Secure.LOCATION_MODE) == 2)
            {
                // do stuff
                Toast.makeText(getActivity(), "ALERTA! Ubicacion en Modo Ahorro de Bateria !", Toast.LENGTH_LONG).show();

            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        welcome = (TextView) view.findViewById(R.id.welcome);
        desttxt = (TextView) view.findViewById(R.id.Destinotxt);
        foliotxt = (TextView) view.findViewById(R.id.Foliotxt);
        loadtxt = (TextView) view.findViewById(R.id.Loadtxt);
        rutatxt = (TextView) view.findViewById(R.id.Rutatxt);
        cajatxt = (TextView) view.findViewById(R.id.Cajatxt);
        unidadtxt = (TextView) view.findViewById(R.id.Unidadtxt);
        direTramotxt = (TextView) view.findViewById(R.id.direTramo);
        ventanatxt = (TextView) view.findViewById(R.id.ventanatxt);
        estimadotxt = (TextView) view.findViewById(R.id.estimadotxt);

        v1= (View) view.findViewById(R.id.vista1);
        v2= (View) view.findViewById(R.id.vista2);

        dest = (TextView) view.findViewById(R.id.Destino);
        folio = (TextView) view.findViewById(R.id.Folio);
        load = (TextView) view.findViewById(R.id.Load);
        ruta = (TextView) view.findViewById(R.id.Ruta);
        caja = (TextView) view.findViewById(R.id.Caja);
        unidad = (TextView) view.findViewById(R.id.Unidad);
        ventana = (TextView) view.findViewById(R.id.ventana);
        estimado = (TextView) view.findViewById(R.id.estimado);

        btnViaje = (Button) view.findViewById(R.id.btnViaje);
        btnTramos = (Button) view.findViewById(R.id.btnTramos);

        TTarde = (TextView) view.findViewById(R.id.tiempoTarde);

        btnsolcitar = (Button) view.findViewById(R.id.btn_solicitar);
        btnsolcitar.setVisibility(View.INVISIBLE);
        btnsolcitar.setBackgroundColor(Color.parseColor("#088A08"));


        btnsolcitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Solicitar solicitar = new Solicitar();
               solicitar.execute();

            }
        });



        direTramotxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            //google.navigation:q=
                            Uri.parse("google.navigation:q=" + latitudDestino + " " + longitudDestino));
                    startActivity(intent);

                }catch (Exception e){
                   Toast.makeText(getActivity(), "Favor de Instalar Google Maps", Toast.LENGTH_SHORT).show();
                }
            }
        });


        refresh = (ImageButton) view.findViewById(R.id.iBRrefresh);

        //refresh.setBackgroundColor(Color.RED);
        refresh.setEnabled(false);
        //when use press button
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                //disable your button here
                //refresh.setVisibility(View.INVISIBLE);
               // refresh.setBackgroundColor(Color.BLUE);
                refresh.setEnabled(true);
            }
        }, 5*1000); //your delay time

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(viajeFragment.this);
                ft.attach(viajeFragment.this);
                ft.commit();


                FragmentManager fragManager = getActivity().getSupportFragmentManager();
                fragManager.beginTransaction().replace(R.id.contenedor,new viajeFragment()).commit();

                /*Fragment currentFragment = getFragmentManager().findFragmentByTag("contenedorViaje");
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.detach(currentFragment);
                fragmentTransaction.attach(currentFragment);
                fragmentTransaction.commit();*/


            }
        });


        btnViaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction2 = getFragmentManager().beginTransaction();
                transaction2.hide(tramos);
                transaction2.commit();
                desttxt.setVisibility(View.VISIBLE);
                foliotxt.setVisibility(View.VISIBLE);
                loadtxt.setVisibility(View.VISIBLE);
                rutatxt.setVisibility(View.VISIBLE);
                cajatxt.setVisibility(View.VISIBLE);
                unidadtxt.setVisibility(View.VISIBLE);
                direTramotxt.setVisibility(View.VISIBLE);
                ventanatxt.setVisibility(View.VISIBLE);
                estimadotxt.setVisibility(View.VISIBLE);

                v1.setVisibility(View.VISIBLE);
                v2.setVisibility(View.VISIBLE);

                dest.setVisibility(View.VISIBLE);
                folio.setVisibility(View.VISIBLE);
                load.setVisibility(View.VISIBLE);
                ruta.setVisibility(View.VISIBLE);
                caja.setVisibility(View.VISIBLE);
                unidad.setVisibility(View.VISIBLE);
                ventana.setVisibility(View.VISIBLE);
                estimado.setVisibility(View.VISIBLE);

            }
        });

        btnTramos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.tramosContenedor, tramos);
                transaction.show(tramos);
                transaction.commit();
                desttxt.setVisibility(View.INVISIBLE);
                foliotxt.setVisibility(View.INVISIBLE);
                loadtxt.setVisibility(View.INVISIBLE);
                rutatxt.setVisibility(View.INVISIBLE);
                cajatxt.setVisibility(View.INVISIBLE);
                unidadtxt.setVisibility(View.INVISIBLE);
                direTramotxt.setVisibility(View.INVISIBLE);
                ventanatxt.setVisibility(View.INVISIBLE);
                estimadotxt.setVisibility(View.INVISIBLE);

                v1.setVisibility(View.INVISIBLE);
                v2.setVisibility(View.INVISIBLE);

                dest.setVisibility(View.INVISIBLE);
                folio.setVisibility(View.INVISIBLE);
                load.setVisibility(View.INVISIBLE);
                ruta.setVisibility(View.INVISIBLE);
                caja.setVisibility(View.INVISIBLE);
                unidad.setVisibility(View.INVISIBLE);
                ventana.setVisibility(View.INVISIBLE);
                estimado.setVisibility(View.INVISIBLE);

            }
        });




        return view;
    }






    private class SegundoPlano extends AsyncTask<Void, Void, Void> {

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


                    //Si el usuario no existe el servicio regresara el string {"Usuario":[{}] }
                    if (tran.length() <= 15) {
                        Toast.makeText(getActivity(), "Viaje No Asignado", Toast.LENGTH_SHORT).show();
                        btnsolcitar.setVisibility(View.VISIBLE);
                    } else {

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
                        String idDetalleViaje = viaje.getIdDetalleViaje();
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
                        latitudDestino = viaje.getLatitudDestino();
                        longitudDestino = viaje.getLongitudDestino();
                        String ventanaFecha = viaje.getFechaVentana();
                        String eta = viaje.getEta();

                        Bundle args = new Bundle();
                        args.putString("idViaje", idViaje);
                        tramos.setArguments(args);
                        getFragmentManager().beginTransaction().add(R.id.tramosContenedor, tramos).commit();
                        FragmentTransaction transaction2 = getFragmentManager().beginTransaction();
                        transaction2.hide(tramos);
                        transaction2.commit();

                        ventanatxt.setText(ventanaFecha);
                        estimadotxt.setText(eta);
                        direTramotxt.setText(direccionTramo);
                        desttxt.setText(nombreDestino);
                        foliotxt.setText(folioViaje );
                        loadtxt.setText(tracking);
                        rutatxt.setText(nombreRuta);
                        cajatxt.setText(nombreRemolquePrincipal);
                        unidadtxt.setText(nombreUnidad);
                        welcome.setText("Bienvenido \n" + nombreOperador);

                        loadtxt.setVisibility(View.INVISIBLE);

                        String ventanaFecha2 = viaje.getFechaVentana();
                        String eta2 = viaje.getEta();


                           try {
                               SimpleDateFormat format = new SimpleDateFormat("yyyy-M-dd hh:mm");

                               Date cita = format.parse(ventanaFecha2);
                               Date llegada = format.parse(eta2);
                               System.out.println(cita);
                               System.out.println(llegada);

                               long different = cita.getTime() - llegada.getTime();

                               System.out.println("cita : " + cita);
                               System.out.println("llegada : " + llegada);
                               System.out.println("different : " + different);

                               long secondsInMilli = 1000;
                               long minutesInMilli = secondsInMilli * 60;
                               long hoursInMilli = minutesInMilli * 60;
                               long daysInMilli = hoursInMilli * 24;

                               long elapsedDays = different / daysInMilli;
                               different = different % daysInMilli;

                               long elapsedHours = different / hoursInMilli;
                               different = different % hoursInMilli;

                               long elapsedMinutes = different / minutesInMilli;
                               different = different % minutesInMilli;

                               long elapsedSeconds = different / secondsInMilli;

                               System.out.printf(
                                       "%d days, %d hours, %d minutes, %d seconds%n",
                                       elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);

                               if (elapsedDays <= 0 && elapsedHours <= 0 && elapsedMinutes <= 0) {
                                   int dias = (int) Math.abs(elapsedDays);
                                   int horas = (int) Math.abs(elapsedHours);
                                   int minutos = (int) Math.abs(elapsedMinutes);
                                   TTarde.setText("TARDE \n" + dias + " Dias/ " + horas + " Horas/ " + minutos + " Minutos");
                                   TTarde.setBackgroundColor(Color.parseColor("#FF0000"));
                               }
                               if (elapsedDays >= 0 && elapsedHours >= 0 && elapsedMinutes >= 0) {

                                   TTarde.setText("A TIEMPO\n" + elapsedDays + " Dias/ " + elapsedHours + " Horas/ " + elapsedMinutes + " Minutos");
                                   TTarde.setBackgroundColor(Color.parseColor("#04B404"));
                               }


                           }catch (Exception e) {
                               TTarde.setText("SIN VENTANA");
                               TTarde.setBackgroundColor(Color.parseColor("#FFFF00"));
                           }
                    }





            }catch (Exception ex){
                mensaje2 = "ERROR: " +ex.getMessage();
              //  Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();

            }


            super.onPostExecute(aVoid);
        }
    }


    private class Solicitar extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            String SOAP_ACTION = "http://release.dxxpress.net/wsInspeccion/Version_20171221_1212";
            String METHOD_NAME = "SolicitarViaje";
            String NAMESPACE  = "http://release.dxxpress.net/wsInspeccion/";
            String URL = "http://release.dxxpress.net/wsInspeccion/interfaceOperadores3.asmx";


            try{

                SoapObject Request = new SoapObject(NAMESPACE,METHOD_NAME);
                Request.addProperty("idUnidad", idUnidad);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport= new HttpTransportSE(URL);
                transport.call(SOAP_ACTION, soapEnvelope);


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

                if (mensaje2!="OK"){
                    Toast.makeText(getActivity(), "Sin Red", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(), "Solicitud Enviada", Toast.LENGTH_SHORT).show();

                }
            }catch (Exception e){
                String ovo = "ERROR: " +e.getMessage();
            }

            super.onPostExecute(aVoid);
        }
    }

}