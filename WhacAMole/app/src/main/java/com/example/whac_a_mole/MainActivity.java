package com.example.whac_a_mole;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    ArrayList<Integer> toposId = new ArrayList<Integer>();
    ArrayList<Boolean> topoSelecionados = new ArrayList<Boolean>();
    AlertDialog.Builder construirDialogo;
    AlertDialog dialogo;
    Random random = new Random();
    int velocidadJuego = 500;
    int puntuacion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ocultarTopos();
        cargarTopos();
        jugar();
    }


    public void cargarTopos(){
        toposId.add(R.id.topo);
        toposId.add(R.id.topo2);
        toposId.add(R.id.topo3);
        toposId.add(R.id.topo4);
        toposId.add(R.id.topo5);
        toposId.add(R.id.topo6);
        toposId.add(R.id.topo7);
        toposId.add(R.id.topo8);
        toposId.add(R.id.topo9);
        toposId.add(R.id.topo10);
        for(int i = 0; i < toposId.size(); i++) {
            topoSelecionados.add(false);
        }
    }
    public void ocultarTopos(){
        ImageView topo = (ImageView)findViewById(R.id.topo);
        topo.setVisibility(View.GONE);
        topo = (ImageView)findViewById(R.id.topo2);
        topo.setVisibility(View.GONE);
        topo = (ImageView)findViewById(R.id.topo3);
        topo.setVisibility(View.GONE);
        topo = (ImageView)findViewById(R.id.topo4);
        topo.setVisibility(View.GONE);
        topo = (ImageView)findViewById(R.id.topo5);
        topo.setVisibility(View.GONE);
        topo = (ImageView)findViewById(R.id.topo6);
        topo.setVisibility(View.GONE);
        topo = (ImageView)findViewById(R.id.topo7);
        topo.setVisibility(View.GONE);
        topo = (ImageView)findViewById(R.id.topo8);
        topo.setVisibility(View.GONE);
        topo = (ImageView)findViewById(R.id.topo9);
        topo.setVisibility(View.GONE);
        topo = (ImageView)findViewById(R.id.topo10);
        topo.setVisibility(View.GONE);
    }

    public void mostrarTopo(){
        int indice = (int)(random.nextDouble()*10);
        if (topoSelecionados.get(indice) == false) {
            int randomId = (int)toposId.get(indice);
            ImageView topo = (ImageView) findViewById(randomId);
            topo.setVisibility(View.VISIBLE);
            topoSelecionados.set(indice,true);
            int delay = (int)(random.nextDouble()* 3000 + 500);
            Timer timer = new Timer();
            TimerTask task = new TimerTask(){
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ocultarTopo(indice);
                        }
                    });
                }
            };

            timer.schedule(task, delay); // disminuir según tiempo --> aparición más rápida

        }
        else{
        }
    }

    public void ocultarTopo(int indice){
        int id = (int)toposId.get(indice);
        ImageView topo = (ImageView) findViewById(id);
        topo.setVisibility(View.GONE);
        topoSelecionados.set(indice,false);
    }

    public void jugar(){
        //cronometro del juego
        // 30s
        // llamada onTick cada 1000ms -> 1s
        new CountDownTimer(30000, 1000) {
            TextView contador = (TextView)findViewById(R.id.contador);
            public void onTick(long millisUntilFinished) {
                contador.setText("00:" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                contador.setText("FIN");
                //stop juego

                //Alerta con restart
                cargarDialogo();
                dialogo = construirDialogo.create();
                dialogo.show();
            }
        }.start();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mostrarTopo();
                    }
                });
            }
        };
        // lo que tarda en salir el topo ---> disminuir según tiempo
        timer.scheduleAtFixedRate(task, new Date(), velocidadJuego * 3);



    }

    public void stopJuego(){


    }

    public void cargarDialogo(){
        construirDialogo = new AlertDialog.Builder(MainActivity.this);
        construirDialogo.setTitle("Fin del juego");
        construirDialogo.setMessage("La puntuación final del juego es " + puntuacion);
        construirDialogo.setPositiveButton("Volver a jugar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // rejugar
            }
        });
        construirDialogo.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
    }
}