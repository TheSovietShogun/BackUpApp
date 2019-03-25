package app.dx.dx_deas.app_dx;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.WindowManager;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;
import static app.dx.dx_deas.app_dx.app.CHANNEL_ID;

public class GPS_Service extends Service {

    public static final int SERVICE_ID = 123 ;
    Double lat;
    Double lon;
    String latitud;
    String longitud;
    String mensaje = "";
    LocationManager locationManager;
    LocationListener listener;
    String idUnidad;
    String idOperador;
    String fecha;
    String SOAP_ACTION;
    String METHOD_NAME ;
    String NAMESPACE ;
    String URL ;


    @Override
    public void onCreate() {


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) { return null; }


    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //El servicio obtiene la latitus y la longitud , y empieza la llamada al servicio soap
        System.out.println("IMPRE: ON START INICIA ");

        Intent notificationIntent = new Intent(this, nd_menuPrincipal.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,notificationIntent,0);
        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle("Servicios de DX en ejecucion")
                .setContentText("")
                .setSmallIcon(R.drawable.logodx2)
                //.setContentIntent(pendingIntent)
                .build();
        startForeground(1,notification);

        try {

            listener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    lat = location.getLatitude();
                    lon = location.getLongitude();
                    latitud = lat.toString();
                    longitud = lon.toString();
                    Insert tarea = new Insert();

                    //System.out.println("IMPRE: VARIABLES ANTES DEL ASYNC" + idUnidad + "---- "+idOperador + "----"+ latitud + "----" + longitud);
                    tarea.execute();


                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {
                    System.out.println("NO GPS!!!");
                    //AlertNoGps();
                   // stopSelf();
                }
            };



            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 60 * 3 , 0, listener);


        }catch (Exception e){
            System.out.println("Excepcion en Servicio Location : " + e.getMessage() );

        }

        idUnidad = (String) intent.getExtras().get("idUnidad");
        idOperador=(String) intent.getExtras().get("idOperador");


        System.out.println("IMPRE : SE TERMINA EL ON START");

        return START_STICKY;
    }



    //Se inicia una tarea en segundo plano para ejecutar el soap (Se hace de esta forma ya que el servicio lanza excepciones si es ejecutado arriba)
    private class Insert extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() { super.onPreExecute(); }

        @Override
        protected Void doInBackground(Void... voids) {

            System.out.println("IMPRE: DO IN BACKGROUND ");


            SOAP_ACTION = "http://dxxpress.net/wsInspeccion/Version_20171221_1212" ;
            METHOD_NAME = "PosicionSave";
            NAMESPACE = "http://dxxpress.net/wsInspeccion/";
            URL = "http://dxxpress.net/wsInspeccion/interfaceOperadores3.asmx";
            fecha = (String) android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss", new Date());
            String version = "2.9";

            try {

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("latitud", latitud);
                Request.addProperty("longitud", longitud);
                Request.addProperty("fecha", fecha);
                Request.addProperty("idUnidad", idUnidad);
                Request.addProperty("idOperador", idOperador);
                Request.addProperty("version", version);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);


                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.call(SOAP_ACTION, soapEnvelope);

                SoapPrimitive ResultString = (SoapPrimitive) soapEnvelope.getResponse();

                mensaje = ResultString.toString();

                System.out.println("SE INSERTO A LAS : " + fecha );



            } catch (Exception ex) {

                mensaje = ex.getMessage();
                System.out.println("Excepcion en Servicio SOAP : " + mensaje );
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) { super.onPostExecute(aVoid); }
    }

    @Override
    public void onDestroy() {
        locationManager.removeUpdates(listener);
        super.onDestroy();
    }


}