package app.dx.dx_deas.app_dx;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
            String SOAP_ACTION = "http://dxxpress.net/wsInspeccion/Version_20171221_1212";
            String METHOD_NAME = "Tramos";
            String NAMESPACE  = "http://dxxpress.net/wsInspeccion/";
            String URL = "http://dxxpress.net/wsInspeccion/interfaceOperadores3.asmx";



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

                        Type tramosListType = new TypeToken<ArrayList<CTramos>>() {
                        }.getType();
                        List<CTramos> tramos = new Gson().fromJson(reusu2, tramosListType);

                        ArrayAdapter adapter = new tramosAdapter(getActivity(), tramos);

                        lista.setAdapter(adapter);

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