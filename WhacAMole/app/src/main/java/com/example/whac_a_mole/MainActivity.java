package com.example.whac_a_mole;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
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
    ImageView image;

    ArrayList<Integer> toposId = new ArrayList<Integer>();
    ArrayList<Integer> toposCascoId = new ArrayList<Integer>();
    int numTopos = 10;
    ArrayList<Boolean> hoyoSelecionados = new ArrayList<Boolean>();
    AlertDialog.Builder construirDialogo;
    AlertDialog dialogo;
    GestureDetectorCompat mDet;
    Random random = new Random();
    int velocidadJuego = 500;
    int puntuacion = 0;
    int tiempoJuego = 60000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        mDet = new GestureDetectorCompat(this, this);
        mDet.setOnDoubleTapListener(this);

        setUpTopos();
        setUpPuntuacion();
        jugar();
    }

    //Método para reiniciar el juego
    protected void restart() {
        Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void setUpTopos() {
        Resources r = getResources();
        String name = getPackageName();

        ImageView topo;
        ImageView topoCasco;
        ImageView whack;
        ImageView whackCasco;
        int id = -1;
        int idConCasco = -1;
        int idWhack = -1;
        int idWhackCasco = -1;

        View.OnTouchListener touch = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                Log.d("OnTouchListener", "Topo tocado");
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        for (int i = 1; i <= numTopos; i++) {
                        if (i == 1) {
                            int idTocado = r.getIdentifier("topo", "id", name);
                            if (view.getId() == idTocado) {
                                ocultarTopo(0);
                                sumarPuntos(10);
                            }
                        } else {
                            int idTocado = r.getIdentifier("topo" + i, "id", name);
                            if (view.getId() == idTocado) {
                                ocultarTopo(i - 1);
                                sumarPuntos(10);
                            }
                        }
                    }
                    break;
                }
                return true;
            }
        };

        for(int i = 1; i <= numTopos; i++) {
            if (i == 1) {
                id = r.getIdentifier("topo", "id", name);
                idConCasco = r.getIdentifier("topoCasco", "id", name);
                idWhack = r.getIdentifier("topoWhack", "id", name);
                idWhackCasco = r.getIdentifier("topoCascoWhack", "id", name);


            }
            else {
                id = r.getIdentifier("topo" + i, "id", name);
                idConCasco = r.getIdentifier("topoCasco" + i, "id", name);
                idWhack = r.getIdentifier("topoWhack" + i, "id", name);
                idWhackCasco = r.getIdentifier("topoCascoWhack" + i, "id", name);
            }

            topo = (ImageView) findViewById(id);
            topoCasco = (ImageView) findViewById(idConCasco);
            whack = (ImageView) findViewById(idWhack);
            whackCasco = (ImageView) findViewById(idWhackCasco);

            topo.setVisibility(View.GONE);
            topoCasco.setVisibility(View.GONE);
            whack.setVisibility(View.GONE);
            whackCasco.setVisibility(View.GONE );

            topo.setOnTouchListener(touch);
            topoCasco.setOnTouchListener(touch);

            toposId.add(id);
            toposCascoId.add(idConCasco);

            hoyoSelecionados.add(false);
        }
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
                finJuego();

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

    public void finJuego(){
        for(int indice = 0; indice <10; indice++){
            int id = (int)toposId.get(indice);
            ImageView topo = (ImageView) findViewById(id);
            topo.setVisibility(View.GONE);

            id = (int)toposCascoId.get(indice);
            ImageView topoCasco = (ImageView) findViewById(id);
            topoCasco.setVisibility(View.GONE);

            hoyoSelecionados.set(indice,true);
        }

    }

    public void setUpPuntuacion() {
        TextView puntos = (TextView)findViewById(R.id.puntos);
        puntuacion = 0;
        puntos.setText("Puntos: " + puntuacion);
    }


    public void sumarPuntos(int punto){
        TextView puntos = (TextView)findViewById(R.id.puntos);
        puntuacion = puntuacion + punto;
        puntos.setText("Puntos: " + puntuacion);
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