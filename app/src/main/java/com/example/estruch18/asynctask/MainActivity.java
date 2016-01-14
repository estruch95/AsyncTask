package com.example.estruch18.asynctask;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import javax.xml.transform.Result;

public class MainActivity extends AppCompatActivity {

    private TextView estado, progreso;
    private ProgressBar barraProgreso;
    private Button btnIniciar, btnCancelar;
    private RealizaCarga rc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        declaracionViews();

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

    @Override
    protected void onSaveInstanceState(Bundle guardarEstado) {
        super.onSaveInstanceState(guardarEstado);
        guardarEstado.putString("estado", estado.getText().toString());
        guardarEstado.putString("progreso", progreso.getText().toString());
        guardarEstado.getInt("progreso_barra", barraProgreso.getProgress());
    }

    @Override
    protected void onRestoreInstanceState(Bundle recuperarEstado) {
        super.onRestoreInstanceState(recuperarEstado);
        String estadoStr = recuperarEstado.getString("estado");
        String progresoStr = recuperarEstado.getString("progreso");
        int progresoBarra = recuperarEstado.getInt("progreso_barra");

        estado.setText(estadoStr);
        progreso.setText(progresoStr);
        barraProgreso.setProgress(progresoBarra);
    }

    public void declaracionViews(){
        estado = (TextView)findViewById(R.id.tvEstado);
        progreso = (TextView)findViewById(R.id.tvProgreso);
        barraProgreso = (ProgressBar)findViewById(R.id.barra_progreso);
        btnIniciar = (Button)findViewById(R.id.btnIniciar);
        btnCancelar = (Button)findViewById(R.id.btnCancelar);
        rc = new RealizaCarga();
    }

    public class RealizaCarga extends AsyncTask<Integer, Integer,Void>{

        @Override
        protected Void doInBackground(Integer... params) {
            int contador = 0;

            if(this.isCancelled()){
                Toast.makeText(getApplicationContext(), "Estado: STOP", Toast.LENGTH_SHORT).show();
            }
            else{
                //Toast.makeText(getApplicationContext(), "Estado: RUNNING", Toast.LENGTH_SHORT).show();
                for(int a=0; a<5; a++){
                    if(!this.isCancelled()) {
                        try {
                            Thread.sleep(1000);
                            contador += 20;
                            publishProgress(contador);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else{
                        return null;
                    }
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values[0]);
            barraProgreso.setProgress(values[0]);
            progreso.setText(values[0] + " %");
        }

        @Override
        protected void onPostExecute(Void contador) {
            super.onPostExecute(contador);
            btnIniciar.setVisibility(View.VISIBLE);
            estado.setText("Tarea finalizada");
            Toast.makeText(getApplicationContext(), "Tarea finalizada con éxito", Toast.LENGTH_SHORT).show();
            btnCancelar.setVisibility(View.INVISIBLE);
        }
    }


    public void actionBtnIniciar(View v){
        estado.setText("Empezamos la tarea de larga duración...");
        btnIniciar.setVisibility(View.INVISIBLE);
        btnCancelar.setVisibility(View.VISIBLE);

        //Iniciamos el AsyncTask
        rc = new RealizaCarga();
        rc.execute();
    }

    public void actionBtnCancelar(View v) {
        estado.setText("Tarea cancelada!!");
        btnIniciar.setVisibility(View.VISIBLE);
        btnCancelar.setVisibility(View.INVISIBLE);
        progreso.setText("0 %");

        //Paramos el AsyncTask
        rc.cancel(true);
        barraProgreso.setProgress(0);
    }
}
