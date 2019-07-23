package app.dx.dx_deas.app_dx;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class tramosAdapter extends ArrayAdapter<CTramos> {



    public tramosAdapter(FragmentActivity fragmentActivity, List<CTramos> tramos) {


        super(fragmentActivity, 0,tramos);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tramo, parent, false);
        }

        CTramos currentTramo = getItem(position);



        TextView hora = (TextView) convertView.findViewById(R.id.hora);
        TextView colo = (TextView) convertView.findViewById(R.id.colo);
        TextView nombre = (TextView) convertView.findViewById(R.id.nombreTramo);
        ImageView logo  = (ImageView) convertView.findViewById(R.id.logo);


        hora.setText(currentTramo.getHoraCompromiso());
        nombre.setText(currentTramo.getNombre());
        colo.setText("");
        logo.setVisibility(View.INVISIBLE);

        int secu = Integer.parseInt(currentTramo.getSecuencia());
        int vali = Integer.parseInt(currentTramo.getEstatus());
        String fechaEntrada = currentTramo.getFechaEntrada();

        if (vali == 2 ){
            colo.setBackgroundColor(Color.parseColor("#074EAB"));
            colo.setText("Terminado");

        }
        if (vali == 1 ){
            colo.setBackgroundColor(Color.parseColor("#298A08"));
            colo.setText("Proximo");
            logo.setVisibility(View.VISIBLE);

        }
        if (vali == 3 ){
            colo.setBackgroundColor(Color.parseColor("#FE9A2E"));
            colo.setText("Cancelado");

        }
        if (secu == 1 ){
            colo.setBackgroundColor(Color.parseColor("#074EAB"));
            colo.setText("Terminado");
            logo.setVisibility(View.INVISIBLE);

        }
        if (vali == 1 && (fechaEntrada.length() >= 5)){
            colo.setBackgroundColor(Color.parseColor("#FFFF00"));
            colo.setText("Ultima\n"+"Salida");

        }






        return convertView;
    }
}
