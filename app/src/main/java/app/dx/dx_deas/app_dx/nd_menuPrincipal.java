package app.dx.dx_deas.app_dx;

import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nd_menu_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("DX Xpress");

        String nombreOperador = getIntent().getStringExtra("nombreOperador");
        String nombreUsuario = getIntent().getStringExtra("nombreUsuario");
        String idUnidad = getIntent().getStringExtra("idUnidad");
        String idOperador = getIntent().getStringExtra("idOperador");
        String idUsuario = getIntent().getStringExtra("idUsuario");
        String idViaje = getIntent().getStringExtra("idViaje");

        Intent i = new Intent(nd_menuPrincipal.this, GPS_Service.class);
        i.putExtra("idUnidad", idUnidad);
        i.putExtra("idOperador", idOperador);
        startService(i);

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
        } else if (id == R.id.nav_enviar) {
                getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,new enviarFragment()).commit();
        } else if (id == R.id.nav_cerrar) {
            Intent i = new Intent(nd_menuPrincipal.this, log.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, GPS_Service.class));
        super.onDestroy();
    }
}
