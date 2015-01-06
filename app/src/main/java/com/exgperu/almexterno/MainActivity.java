package com.exgperu.almexterno;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Vector;


public class MainActivity extends ActionBarActivity {

    private static String FICHERO = Environment.getExternalStorageDirectory() + "/Documento.txt";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        final EditText editar = (EditText)findViewById(R.id.txt_editar);
        Button agregar = (Button)findViewById(R.id.btn_agregar);

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarArchivo(editar.getText().toString(),context);
                editar.setText("");
                actualizarEtiqueta();
            }
        });
    }

    public void actualizarEtiqueta(){
        context = this;
        TextView archivo = (TextView)findViewById(R.id.txt_view);
        Vector<String> data = obtenerData(context);
        archivo.setText(data.toString());
    }

    @Override
    public void onResume(){
        super.onResume();
        actualizarEtiqueta();
    }

    public Vector<String> obtenerData(Context context){
        Vector<String> result = new Vector<String>();
        String stadoSD = Environment.getExternalStorageState();
        if (!stadoSD.equals(Environment.MEDIA_MOUNTED) && !stadoSD.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            Toast.makeText(context, "No puedo leer en la memoria externa", Toast.LENGTH_LONG).show();
            return result;
        }
        try {
            FileInputStream f = new FileInputStream(FICHERO);

            BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(f));

            String linea;
            do {
                linea = entrada.readLine();
                if (linea != null) {
                    result.add(linea);
                }
            } while ( linea != null);
            f.close();
        } catch (Exception e) {
            Log.e("App Ficheros", e.getMessage(), e);
        }
        return result;
    }

    public void guardarArchivo(String datos,Context context){
        String stadoSD = Environment.getExternalStorageState();
        if (!stadoSD.equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(context, "No puedo escribir en la memoria externa", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            FileOutputStream f = new FileOutputStream(FICHERO,true);

            String texto = datos + "\n";
            f.write(texto.getBytes());
            Toast.makeText(context, "Cadena Guardada en el Fichero", Toast.LENGTH_LONG).show();
            f.close();

        } catch (Exception e) {
            Log.e("App Ficheros", e.getMessage(), e);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
