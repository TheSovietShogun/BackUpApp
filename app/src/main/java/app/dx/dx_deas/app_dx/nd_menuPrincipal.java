package app.dx.dx_deas.app_dx;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class nd_menuPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    AlertDialog alert  ;
    String nombreOperador;
    String nombreUsuario;
    String idUnidad ;
    String idOperador ;
    String idUsuario ;
    String idViaje ;

    @SuppressLint("ServiceCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nd_menu_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("DX XPRESS");




        nombreOperador = getIntent().getStringExtra("nombreOperador");
        nombreUsuario = getIntent().getStringExtra("nombreUsuario");
        idUnidad = getIntent().getStringExtra("idUnidad");
        idOperador = getIntent().getStringExtra("idOperador");
        idUsuario = getIntent().getStringExtra("idUsuario");
        idViaje = getIntent().getStringExtra("idViaje");

        /*LocationManager locationManager;
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);*/

       /* if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            AlertNoGps();
        }*/


        Intent i = new Intent(nd_menuPrincipal.this, GPS_Service.class);
        i.putExtra("idUnidad", idUnidad);
        i.putExtra("idOperador", idOperador);
        startService(i);

        Intent in = new Intent(nd_menuPrincipal.this, notifi_Service.class);
        in.putExtra("idUnidad", idUnidad);
        startService(in);

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
        } //else if (id == R.id.nav_enviar) {
               //getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,new enviarFragment()).commit();
        //}
        else if (id == R.id.nav_cerrar) {
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


}
