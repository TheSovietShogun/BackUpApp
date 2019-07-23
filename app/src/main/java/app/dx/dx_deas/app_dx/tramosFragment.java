package app.dx.dx_deas.app_dx;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class tramosFragment extends Fragment {

    String idViaje ;
    String tran ;
    String responseString = "";
    String direccionTramo;
    SoapPrimitive resultString;
    String mensaje;
    String mensaje2;
    ListView lista ;
    List<CTramos> tramos;
    String data ;
    int secu ;
    int secuT;
    int vali ;
    int secuG;
    int estatus;
    String fechaEntrada ;
    String nombre ;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
             idViaje = getArguments().getString("idViaje");
                if (idViaje == null ){
             Toast.makeText(getActivity(), "Viaje No Asignado", Toast.LENGTH_SHORT).show();
         }else{
                 LlamadaTramosWS llamadaWS = new LlamadaTramosWS();
                llamadaWS.execute();
                }
            }catch (Exception e){
            mensaje = "ERROR: " +e.getMessage();
            }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_tramos, container, false);

        lista = (ListView) view.findViewById(R.id.listTramos);

        return view;
    }


    private class LlamadaTramosWS extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            String SOAP_ACTION = "http://release.dxxpress.net/wsInspeccion/Version_20171221_1212";
            String METHOD_NAME = "Tramos";
            String NAMESPACE  = "http://release.dxxpress.net/wsInspeccion/";
            String URL = "http://release.dxxpress.net/wsInspeccion/interfaceOperadores3.asmx";



            try{
                //SE CREA UN OBJETO SOAP Y SE LE AGREGAN LOS PARAMETROS DE ENTRADA
                SoapObject Request = new SoapObject(NAMESPACE,METHOD_NAME);
                Request.addProperty("idViaje", idViaje);


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
                    } else {

                        //Libreria gson se utliza para traducir de json a string y viceversa
                        //Gson gson = new Gson();
                        Gson gson = new GsonBuilder().serializeNulls().create();
                        //Retira la palabra usuario y los corchetes para el uso de la libreria gson
                        String reusu = tran.replace("{\"Tramos\":[{", "[{");
                        String reusu2 = reusu.replace("}] }", "}]");

                        //Se usa la libreria para traducir el json , el string obtenido se almacena en la libreria Usuario

                        // Tramos[] Tramos1 = gson.fromJson(reusu2, Tramos[].class);

                        Type tramosListType = new TypeToken<ArrayList<CTramos>>() {}.getType();

                        tramos = new Gson().fromJson(reusu2, tramosListType);

                        for (int i=0;i<tramos.size();i++){

                            secu = Integer.parseInt(tramos.get(i).getSecuencia());
                            estatus = Integer.parseInt(tramos.get(i).getEstatus());

                            if (secu > secuG && estatus != 3){

                                secuG = secu ;

                            }
                        }

                        ArrayAdapter adapter = new tramosAdapter(getActivity(), tramos );

                        lista.setAdapter(adapter);

                        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //Toast.makeText(getActivity(),tramos[position], Toast.LENGTH_SHORT).show();

                                 data  = tramos.get(position).getIdDetalleViaje();
                                 secuT = Integer.parseInt(tramos.get(position).getSecuencia());
                                 vali = Integer.parseInt(tramos.get(position).getEstatus());
                                 fechaEntrada = tramos.get(position).getFechaEntrada();
                                nombre = tramos.get(position).getNombre();

                                //Toast.makeText(getActivity(), "Tramo Data -\nSecuencia " + secuT + "\nEstatus " + vali +"\nNombre " + nombre, Toast.LENGTH_LONG).show();

                                if (secuT == 1 ) {
                                    Toast.makeText(getActivity(), "Tramo Terminado", Toast.LENGTH_SHORT).show();
                                }
                                else if (vali == 2 ){
                                    Toast.makeText(getActivity(),"Tramo Terminado", Toast.LENGTH_SHORT).show();

                                }
                                else if (vali == 1 ){
                                   //Toast.makeText(getActivity(),"Detalle Viaje" + data + "\nid viaje"+ idViaje, Toast.LENGTH_SHORT).show();

                                    //lista.getChildAt(position).setEnabled(false);
                                    Intent i = new Intent(getActivity(), enviarActivity.class);
                                    i.putExtra("idDetalleViaje", data);
                                    i.putExtra("idViaje", idViaje);
                                    i.putExtra("nombre", nombre);
                                    i.putExtra("secu", secuT);
                                    i.putExtra("secuG", secuG);
                                    startActivity(i);

                                }
                                else if (vali == 3 ){
                                    Toast.makeText(getActivity(),"Tramo Cancelado", Toast.LENGTH_SHORT).show();


                                }
                                else if (vali == 1 && (fechaEntrada.length() >= 5)){
                                    //lista.getChildAt(position).setEnabled(false);
                                  // Toast.makeText(getActivity(),"Detalle Viaje" + data + "\nid viaje"+ idViaje, Toast.LENGTH_SHORT).show();
                                   Intent i = new Intent(getActivity(), enviarActivity.class);
                                    i.putExtra("idDetalleViaje", data);
                                    i.putExtra("idViaje", idViaje);
                                    i.putExtra("nombre", nombre);
                                    i.putExtra("secu", secuT);
                                    i.putExtra("secuG", secuG);
                                    startActivity(i);

                                }




                            }
                        });

                        String LeeSin = "La insec papu ";
                    }


            }catch (Exception ex){
                mensaje2 = "ERROR: " +ex.getMessage();
                Toast.makeText(getActivity(), "Error 404T", Toast.LENGTH_SHORT).show();
            }


            super.onPostExecute(aVoid);
        }
    }

}