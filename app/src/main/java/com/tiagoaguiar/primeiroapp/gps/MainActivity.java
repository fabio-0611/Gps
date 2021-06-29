package com.tiagoaguiar.primeiroapp.gps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btnGps;
    TextView txtLatitude, txtLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtLatitude = (TextView) findViewById(R.id.txtLatitude);
        txtLongitude = (TextView) findViewById(R.id.txtLatitude);

        btnGps = (Button) findViewById(R.id.btnGps);
        btnGps.setOnClickListener(new Button.OnClickListener(){
            public void  onClick(View v){
                pedirPermissoes();
            }
        });
    }
//permissões gerando
    private  void  pedirPermissoes(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED  ){

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

        }
else {
    configurarServico();
        }
    }

    @Override
    public void  onRequestPermissionsResult(int requestCode, String permissions[],int[]grandResults){
        switch (requestCode){
            case  1:{
                if (grandResults.length > 0 && grandResults[0]== PackageManager.PERMISSION_GRANTED){
                configurarServico();
            }
        else{
                Toast.makeText(this, "Não vai funcionar!!!!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }
    }
    public void configurarServico(){
        try {
            LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {
                @Override
                //atualizar os dados na tela do gps latitude e longitude
                public void onLocationChanged(Location location) {
                    atualizar(location);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) { }

                @Override
                public void onProviderEnabled(String provider) { }

                @Override
                public void onProviderDisabled(String provider) { }

            };
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,0, 0, locationListener);

        }catch (SecurityException ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

}
public void atualizar(Location location)
{
    Double latPoint = location.getLatitude();
    Double lngPoint = location.getLongitude();

    try {
        Address endereco = buscarEndereco(latPoint, lngPoint);

        TextView txt =(TextView) findViewById(R.id.txtCidade);
        txt.setText(endereco.getSubAdminArea());

        txt= (TextView) findViewById(R.id.txtestadoView);
        txt.setText("Estado:" +endereco.getAdminArea());

        txt = (TextView) findViewById( R.id.txtpaisView);
        txt. setText("País:" +endereco.getCountryName());

    }catch (IOException e){
        Log.i("GPS", e.getMessage());
    }
}

private  Address buscarEndereco( double latitude, double longitude) throws IOException{
    Geocoder geocoder;
    Address address = null;
    List <Address> addresses;

    geocoder = new Geocoder(getApplicationContext());
    addresses = geocoder.getFromLocation(latitude,longitude,1);

    if (addresses.size()>0){
        address = addresses.get(0);

    }
    return address;
    }

}
