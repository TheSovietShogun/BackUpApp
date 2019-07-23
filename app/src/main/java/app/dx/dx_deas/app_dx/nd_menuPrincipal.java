package app.dx.dx_deas.app_dx;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class nd_menuPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    AlertDialog alert  ;
    String nombreOperador;
    String nombreUsuario;
    String idUnidad ;
    String idOperador ;
    String idUsuario ;
    String idViaje ;
    Context context;
    String deviceUniqueIdentifier;
    String imei;
    String emp ;
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA
    };


    @SuppressLint({"ServiceCast", "MissingPermission"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nd_menu_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("DX XPRESS");

        try {
            deviceUniqueIdentifier = null;
            TelephonyManager tm = (TelephonyManager) nd_menuPrincipal.this.getSystemService(Context.TELEPHONY_SERVICE);
             imei = tm.getDeviceId();
        }catch (Exception e ){
             imei = "";
            }

        nombreOperador = getIntent().getStringExtra("nombreOperador");
        nombreUsuario = getIntent().getStringExtra("nombreUsuario");
        idUnidad = getIntent().getStringExtra("idUnidad");
        idOperador = getIntent().getStringExtra("idOperador");
        idUsuario = getIntent().getStringExtra("idUsuario");
        idViaje = getIntent().getStringExtra("idViaje");
        emp =  getIntent().getStringExtra("emp");


        NavigationView naviView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  naviView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.nombre);

        try {
            nav_user.setText("DX XPRESS\n"+ nombreOperador +"\n"+"N°"+emp);
        }catch (Exception e ){
            nav_user.setText("DX XPRESS\n\n"+"N°"+emp);

        }


        if ( isMyServiceRunning(notifi_Service.class)){
            //Toast.makeText(nd_menuPrincipal.this, "Servicio Existente", Toast.LENGTH_LONG).show();
                String notiSer ;
        }else {
            //Toast.makeText(nd_menuPrincipal.this, "Servicio Reiniciado", Toast.LENGTH_LONG).show();

            Intent in = new Intent(nd_menuPrincipal.this, notifi_Service.class);
            in.putExtra("idUnidad", idUnidad);
            startService(in);
        }




       if ( isMyServiceRunning(GPS_ServiceV2.class)){
            Toast.makeText(nd_menuPrincipal.this, "Servicio Existente", Toast.LENGTH_LONG).show();
            String GPS_Ser ;
        }else {
            Toast.makeText(nd_menuPrincipal.this, "Servicio Reiniciado", Toast.LENGTH_LONG).show();

            Intent i = new Intent(nd_menuPrincipal.this, GPS_ServiceV2.class);
            i.putExtra("idUnidad", idUnidad);
            i.putExtra("idOperador", idOperador);
           // i.putExtra("imei", imei);
            //i.putExtra("emp", emp);
            startService(i);
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contenedor,new viajeFragment()).commit();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onStart() {

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        statusCheck();

        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nd_menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_viaje ){
            getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,new viajeFragment()).commit();
        } else if (id == R.id.nav_chat) {
            getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new chatFragment()).commit();
        } else if (id == R.id.nav_cerrar) {
            AlertCerrar();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    private void AlertCerrar() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(nd_menuPrincipal.this);
        builder.setMessage("Esta seguro de cerrar sesion ?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent i = new Intent(nd_menuPrincipal.this, cerrar.class);
                        startActivity(i);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alert = builder.create();
        alert.show();
    }




    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

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

    public void statusCheck() {
         LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Su ubicacion esta apagada . Desea encenderla ?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
