package com.example.whac_a_mole;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    ArrayList<Integer> toposId = new ArrayList<Integer>();
    ArrayList<Integer> toposCascoId = new ArrayList<Integer>();
    ArrayList<Boolean> hoyoSelecionados = new ArrayList<Boolean>();
    AlertDialog.Builder construirDialogo;
    AlertDialog dialogo;
    GestureDetectorCompat mDet;
    Random random = new Random();
    int velocidadJuego = 500;
    int puntuacion = 0;
    int tiempoJuego = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        mDet = new GestureDetectorCompat(this, this);
        mDet.setOnDoubleTapListener(this);

        goneTopos();
        cargarTopos();
        jugar();
    }

    //Método para reiniciar el juego
    protected void restart() {
        Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    public void cargarTopos(){
        //topos normales
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
        //Topos con casco
        toposCascoId.add(R.id.topoCasco);
        toposCascoId.add(R.id.topoCasco2);
        toposCascoId.add(R.id.topoCasco3);
        toposCascoId.add(R.id.topoCasco4);
        toposCascoId.add(R.id.topoCasco5);
        toposCascoId.add(R.id.topoCasco6);
        toposCascoId.add(R.id.topoCasco7);
        toposCascoId.add(R.id.topoCasco8);
        toposCascoId.add(R.id.topoCasco9);
        toposCascoId.add(R.id.topoCasco10);
        for(int i = 0; i < toposId.size(); i++) {
            hoyoSelecionados.add(false);
        }
    }

    public void goneTopos(){
        //topos normales
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

        //Topos con casco
        ImageView topoCasco = (ImageView)findViewById(R.id.topoCasco);
        topoCasco.setVisibility(View.GONE);
        topoCasco = (ImageView)findViewById(R.id.topoCasco2);
        topoCasco.setVisibility(View.GONE);
        topoCasco = (ImageView)findViewById(R.id.topoCasco3);
        topoCasco.setVisibility(View.GONE);
        topoCasco = (ImageView)findViewById(R.id.topoCasco4);
        topoCasco.setVisibility(View.GONE);
        topoCasco = (ImageView)findViewById(R.id.topoCasco5);
        topoCasco.setVisibility(View.GONE);
        topoCasco = (ImageView)findViewById(R.id.topoCasco6);
        topoCasco.setVisibility(View.GONE);
        topoCasco = (ImageView)findViewById(R.id.topoCasco7);
        topoCasco.setVisibility(View.GONE);
        topoCasco = (ImageView)findViewById(R.id.topoCasco8);
        topoCasco.setVisibility(View.GONE);
        topoCasco = (ImageView)findViewById(R.id.topoCasco9);
        topoCasco.setVisibility(View.GONE);
        topoCasco = (ImageView)findViewById(R.id.topoCasco10);
        topoCasco.setVisibility(View.GONE);
    }

    public void mostrarTopo(){
        int indice = (int)(random.nextDouble()*10);
        if (hoyoSelecionados.get(indice) == false) {
            hoyoSelecionados.set(indice,true);
            int randomId = (int)toposId.get(indice);
            ImageView topo = (ImageView) findViewById(randomId);
            topo.setVisibility(View.VISIBLE);

            int delay = (int)(random.nextDouble()* 2000 + 1000);
            Timer timer = new Timer();
            TimerTask task = new TimerTask(){
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ocultarTopo(indice);
                            mostrarTopoCasco();
                        }
                    });
                }
            };
            timer.schedule(task, delay); // disminuir según tiempo --> aparición más rápida

        }
        else{
        }
    }

    public void mostrarTopoCasco() {
        int indice = (int) (random.nextDouble() * 10);
        if (hoyoSelecionados.get(indice) == false) {
            hoyoSelecionados.set(indice, true);
            int randomId = (int) toposCascoId.get(indice);
            ImageView topoCasco = (ImageView) findViewById(randomId);
            topoCasco.setVisibility(View.VISIBLE);

            int delay = (int) (random.nextDouble() * 2000 + 1000);
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ocultarTopoCasco(indice);
                        }
                    });
                }
            };
            timer.schedule(task, delay); // disminuir según tiempo --> aparición más rápida
        } else {
        }
    }

    public void ocultarTopo(int indice){
        int id = (int)toposId.get(indice);
        ImageView topo = (ImageView) findViewById(id);
        topo.setVisibility(View.GONE);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hoyoSelecionados.set(indice,false);
                    }
                });
            }
        };
        timer.schedule(task, 1000);
    }

    public void ocultarTopoCasco(int indice){
        int id = (int)toposCascoId.get(indice);
        ImageView topoCasco = (ImageView) findViewById(id);
        topoCasco.setVisibility(View.GONE);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hoyoSelecionados.set(indice,false);
                    }
                });
            }
        };
        timer.schedule(task, 1000);

    }

    public void jugar(){
        //cronometro del juego
        // tiempo de juego establecido
        // llamada onTick cada 1000ms -> 1s
        new CountDownTimer(tiempoJuego, 1000) {
            TextView contador = (TextView)findViewById(R.id.contador);
            public void onTick(long millisUntilFinished) {
                contador.setText("00:" + String.format("%02d", millisUntilFinished / 1000));
            }

            public void onFinish() {
                contador.setText("FIN");
                //stop juego

                //Alerta con restart
                cargarDialogo();
                dialogo = construirDialogo.create();
                if(dialogo != null && !dialogo.isShowing()) {
                    dialogo.show();
                }

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

    public void sumarPuntos(int punto){
        TextView puntos = (TextView)findViewById(R.id.puntos);
        puntuacion = puntuacion + punto;
        puntos.setText(puntuacion);
    }

    public void cargarDialogo(){
        construirDialogo = new AlertDialog.Builder(this);
        construirDialogo.setTitle("Fin del juego");
        construirDialogo.setMessage("La puntuación final del juego es " + puntuacion);
        construirDialogo.setPositiveButton("Volver a jugar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                restart();
            }
        });
        construirDialogo.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.mDet.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }


    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        Log.d("MAINACTIVITY", "ONDOUBLETAP");
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

}