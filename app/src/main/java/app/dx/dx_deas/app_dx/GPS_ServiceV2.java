package app.dx.dx_deas.app_dx;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Date;

import static app.dx.dx_deas.app_dx.app.CHANNEL_ID;

public class GPS_ServiceV2 extends Service {

        private LocationManager locManager;
        private LocationListener locListener = new myLocationListener();
        private boolean gps_enabled = false;
        private boolean network_enabled = false;
        private Handler handler = new Handler();
        Thread t;
    String idUnidad;
    String idOperador;
    String fecha;
    String SOAP_ACTION;
    String METHOD_NAME ;
    String NAMESPACE ;
    String URL ;
    String BatteryLevel;
    String imei;
    String emp;
    String mensaje;
    String lat_old ;
    String lon_old ;
    String lat_new;
    String lon_new;
    String latitud;
    String longitud;

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onCreate() {

        }



        public int onStartCommand(Intent intent, int flags, int startId) {


            Intent notificationIntent = new Intent(this, nd_menuPrincipal.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,notificationIntent,0);
            Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                    .setContentTitle("Servicios de DX en ejecucion")
                    .setContentText("")
                    .setSmallIcon(R.drawable.logodx2)
                    .setPriority(2)
                    //.setContentIntent(pendingIntent)
                    .build();
            startForeground(1,notification);

            Toast.makeText(getBaseContext(), "Service Started", Toast.LENGTH_SHORT)
                    .show();

            idUnidad = (String) intent.getExtras().get("idUnidad");
            idOperador=(String) intent.getExtras().get("idOperador");

            final Runnable r = new Runnable() {
                public void run() {
                    location();
                    handler.postDelayed(this, 180000);
                }
            };
            handler.postDelayed(r, 180000);
            return START_REDELIVER_INTENT;
        }



        @SuppressLint("MissingPermission")
        public void location() {
            locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            try {
                gps_enabled = locManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ex) {
            }


            if (gps_enabled ) {
                locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                        0, locListener);
            }


        }

        private class myLocationListener implements LocationListener {


            @Override
            public void onLocationChanged(Location location) {

                if (location != null) {
                    locManager.removeUpdates(locListener);
                    lon_new = String.valueOf(location.getLongitude());
                    lat_new = String.valueOf(location.getLatitude());
                    String longitude = "Longitude: " + location.getLongitude();
                    String latitude = "Latitude: " + location.getLatitude();
                   // Log.v("Debug",  "Latt: " + latitude + " Long: " + longitude);
                    Toast.makeText(getApplicationContext(),
                            longitude + "\n" + latitude, Toast.LENGTH_SHORT).show();
                    lat_old = lat_new;
                    lon_old = lon_new;
                  longitud= lon_new.toString();
                  latitud = lat_new.toString();

                    Insert insert = new Insert();
                    insert.execute();
                }
            }

            @Override
            public void onProviderDisabled(String provider) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

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
            String version = "420";

            try {

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("latitud", latitud);
                Request.addProperty("longitud", longitud);
                Request.addProperty("fecha", fecha);
                Request.addProperty("idUnidad", idUnidad);
                Request.addProperty("idOperador", idOperador);
                //Request.addProperty("nivelbateria", BatteryLevel);
                //Request.addProperty("imei", imei);
                //Request.addProperty("claveoperador", emp);
                Request.addProperty("version", version);


                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);


                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.call(SOAP_ACTION, soapEnvelope);

                SoapPrimitive ResultString = (SoapPrimitive) soapEnvelope.getResponse();

                mensaje = ResultString.toString();

               // System.out.println("SE INSERTO A LAS : " + fecha );



            } catch (Exception ex) {

                mensaje = ex.getMessage();
                System.out.println("Excepcion en Servicio SOAP GPS: " + mensaje );
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) { super.onPostExecute(aVoid);
            System.out.println("RESPUESTA DEL SERVER: " + mensaje);
        }
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }


}