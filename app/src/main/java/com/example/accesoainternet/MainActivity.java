package com.example.accesoainternet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {


    TextView tv;
    Button b;
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv=findViewById(R.id.textView);
        b=findViewById(R.id.b);
        et=findViewById(R.id.et1);

        //Método que se ejecuta al pulsar el botón
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = connectivityManager.getActiveNetworkInfo();

                if(info!=null && info.isConnected()){
                    DescargarPaginaWeb descargarPaginaWeb=new DescargarPaginaWeb();
                    String direccion=et.getText().toString();
                    descargarPaginaWeb.execute(direccion);
                }else{
                    Toast.makeText(getApplicationContext(),"No se ha podido realizar la conexión",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class DescargarPaginaWeb extends AsyncTask<String,Void,String>{
        //Se ejecuta terminar doInBackground
        @Override
        protected void onPostExecute(String resultado) {
            tv.setText(resultado);
        }

        //Se irá ejecutando en segundo plano
        @Override
        protected String doInBackground(String... urls) {
            return descargarUrl(urls[0]);
        }


        private String descargarUrl(String urlString){
            try {
                URL url=new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //Configuración
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(12000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                //Realizamos la conexión
                conn.connect();
                int codigoRespuesta=conn.getResponseCode();
                //Leemos los datos de la página web
                InputStream is = conn.getInputStream();

                String resultado="";

                int i=is.read();
                while(i!=-1){
                    resultado+=(char)i;
                    i=is.read();
                }

                return resultado;



            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }

        }
    }
}