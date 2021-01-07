package com.example.whac_a_mole;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    ArrayList<Integer> toposId = new ArrayList<Integer>();
    ArrayList<Boolean> topoSelecionados = new ArrayList<Boolean>();
    Random random = new Random();
    int velocidadJuego = 500;

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
        topo.setVisibility(View.INVISIBLE);
        topo = (ImageView)findViewById(R.id.topo2);
        topo.setVisibility(View.INVISIBLE);
        topo = (ImageView)findViewById(R.id.topo3);
        topo.setVisibility(View.INVISIBLE);
        topo = (ImageView)findViewById(R.id.topo4);
        topo.setVisibility(View.INVISIBLE);
        topo = (ImageView)findViewById(R.id.topo5);
        topo.setVisibility(View.INVISIBLE);
        topo = (ImageView)findViewById(R.id.topo6);
        topo.setVisibility(View.INVISIBLE);
        topo = (ImageView)findViewById(R.id.topo7);
        topo.setVisibility(View.INVISIBLE);
        topo = (ImageView)findViewById(R.id.topo8);
        topo.setVisibility(View.INVISIBLE);
        topo = (ImageView)findViewById(R.id.topo9);
        topo.setVisibility(View.INVISIBLE);
        topo = (ImageView)findViewById(R.id.topo10);
        topo.setVisibility(View.INVISIBLE);
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

            timer.schedule(task, delay); // aumentar según tiempo (disminuir)

        }
        else{
        }
    }

    public void ocultarTopo(int indice){
        int id = (int)toposId.get(indice);
        ImageView topo = (ImageView) findViewById(id);
        topo.setVisibility(View.INVISIBLE);
        topoSelecionados.set(indice,false);
    }

    public void jugar(){
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
        // lo que tarda en salir el topo ---> aumentar según tiempo
        timer.scheduleAtFixedRate(task, new Date(), velocidadJuego * 3);



    }
}