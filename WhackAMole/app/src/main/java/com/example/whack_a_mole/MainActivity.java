package com.example.whack_a_mole;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    CountDownTimer mainTimer;

    MediaPlayer mediaPlayer;
    SoundPool soundPool;
    int idExplosion, idBossAlert, idAlarma, idWhack;

    ArrayList<Integer> toposId = new ArrayList<Integer>();
    ArrayList<Integer> toposWhackId = new ArrayList<Integer>();
    ArrayList<Integer> toposCascoId = new ArrayList<Integer>();
    ArrayList<Integer> toposCascoWhackId = new ArrayList<Integer>();
    ArrayList<Integer> toposBossId = new ArrayList<Integer>();
    ArrayList<Integer> toposBossWhackId = new ArrayList<Integer>();
    ArrayList<Integer> bombaId = new ArrayList<Integer>();
    ArrayList<Integer> bombaOnId = new ArrayList<Integer>();
    ArrayList<Integer> boomId = new ArrayList<Integer>();
    ArrayList<Integer> timerId = new ArrayList<Integer>();

    int numTopos = 10;
    ArrayList<Boolean> hoyoSelecionados = new ArrayList<Boolean>();
    ArrayList<Boolean> topoTocado = new ArrayList<Boolean>();
    ArrayList<Boolean> topoOn = new ArrayList<Boolean>();
    ArrayList<Boolean> topoCascoOn = new ArrayList<Boolean>();
    ArrayList<Boolean> topoBossOn = new ArrayList<Boolean>();
    ArrayList<Boolean> bombaEncendida = new ArrayList<Boolean>();
    ArrayList<Boolean> timerArray = new ArrayList<Boolean>();
    AlertDialog.Builder construirDialogo;
    AlertDialog dialogo;
    GestureDetectorCompat mDet;
    Random random = new Random();
    int velocidadJuego = 350;
    int puntuacion = 0;
    int tiempoJuego = 60000;
    int globalTiempo;
    Boolean aparecerBoss, stopAlarma;
    Timer timerAlarma;
    TimerTask taskAlarma;

    long touchDownMs, lastTapTimeMs;
    int numberOfTaps;
    long segundosRestantes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        mDet = new GestureDetectorCompat(this, this);
        mDet.setOnDoubleTapListener(this);

        setUpTopos();
        setUpPuntuacion();
        setUpSonido();
        jugar(tiempoJuego);
    }

    protected void restart() {
        Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void setUpSonido(){
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.danger);
        mediaPlayer.setVolume(0.5f,0.5f);

        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        idExplosion = soundPool.load(getApplicationContext(),R.raw.explosion, 0 );
        idBossAlert = soundPool.load(getApplicationContext(),R.raw.bossalert, 0 );
        idWhack = soundPool.load(getApplicationContext(),R.raw.whack, 0 );
    }

    public void activarExplosion() {
        soundPool.play(idExplosion,1,1,1,0,1);
    }

    public void activarBossAlert() {
        soundPool.play(idBossAlert,1,1,1,0,1);
    }

    public void activarWhack() {
        soundPool.play(idWhack,1,1,1,0,1);
    }

    public void activarAlarma(){
        mediaPlayer.start();
    }

    public void pararAlarma() {
        mediaPlayer.stop();
    }

    public void setUpTopos() {
        Resources r = getResources();
        String name = getPackageName();

        View alarma = (View)findViewById(R.id.alarma);
        alarma.setVisibility(View.GONE);
        aparecerBoss = false;
        stopAlarma = false;

        ImageView topo;
        ImageView topoCasco;
        ImageView topoBoss;
        ImageView whack;
        ImageView whackCasco;
        ImageView whackBoss;
        ImageView bomba;
        ImageView bombaOn;
        ImageView boom;
        ImageView time;

        int id = -1;
        int idConCasco = -1;
        int idBoss = -1;
        int idWhack = -1;
        int idCascoWhack = -1;
        int idBossWhack = -1;
        int idBomba = -1;
        int idBombaOn = -1;
        int idBoom = -1;
        int idTime = -1;

        for(int i = 1; i <= numTopos; i++) {
            int iD = i;
            id = r.getIdentifier("topo" + i, "id", name);
            idConCasco = r.getIdentifier("topoCasco" + i, "id", name);
            idBoss = r.getIdentifier("topoBoss" + i, "id", name);
            idWhack = r.getIdentifier("topoWhack" + i, "id", name);
            idCascoWhack = r.getIdentifier("topoCascoWhack" + i, "id", name);
            idBossWhack = r.getIdentifier("topoBossWhack" + i, "id", name);
            idBomba = r.getIdentifier("bomba" + i, "id", name);
            idBombaOn = r.getIdentifier("bombaOn" + i, "id", name);
            idBoom = r.getIdentifier("boom" + i, "id", name);
            idTime = r.getIdentifier("timer" + i, "id", name);

            topo = (ImageView) findViewById(id);
            topoCasco = (ImageView) findViewById(idConCasco);
            topoBoss = (ImageView) findViewById(idBoss);
            whack = (ImageView) findViewById(idWhack);
            whackCasco = (ImageView) findViewById(idCascoWhack);
            whackBoss = (ImageView) findViewById(idBossWhack);
            bomba = (ImageView) findViewById(idBomba);
            bombaOn = (ImageView) findViewById(idBombaOn);
            boom = (ImageView) findViewById(idBoom);
            time = (ImageView) findViewById(idTime);

            topo.setVisibility(View.GONE);
            topoCasco.setVisibility(View.GONE);
            topoBoss.setVisibility(View.GONE);
            whack.setVisibility(View.GONE);
            whackCasco.setVisibility(View.GONE);
            whackBoss.setVisibility(View.GONE);
            bomba.setVisibility(View.GONE);
            bombaOn.setVisibility(View.GONE);
            boom.setVisibility(View.GONE);
            time.setVisibility(View.GONE);

            topo.setOnTouchListener(new View.OnTouchListener() {
                private GestureDetector gDet = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDown(MotionEvent e) {
                        topoTocado.set(iD - 1, true);
                        ocultarTopo(iD - 1);
                        whackTopo(iD - 1);
                        return super.onDown(e);
                    }
                });
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    gDet.onTouchEvent(motionEvent);
                    return true;
                }
            });

            topoCasco.setOnTouchListener(new View.OnTouchListener() {
                private GestureDetector gDet = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
                   @Override
                   public boolean onDoubleTap(MotionEvent e) {
                       topoTocado.set(iD - 1, true);
                       ocultarTopoCasco(iD - 1);
                       whackTopoCasco(iD - 1);
                       return super.onDoubleTap(e);
                   }
                });
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    gDet.onTouchEvent(motionEvent);
                    return true;
                }
            });

            topoBoss.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            touchDownMs = System.currentTimeMillis();
                            break;
                        case MotionEvent.ACTION_UP:
                            //handler.removeCallbacksAndMessages(null);
                            if ((System.currentTimeMillis() - touchDownMs) > ViewConfiguration.getTapTimeout()) {
                                numberOfTaps = 0;
                                lastTapTimeMs = 0;
                                break;
                            }
                            if (numberOfTaps > 0
                                    && (System.currentTimeMillis() - lastTapTimeMs) < ViewConfiguration.getDoubleTapTimeout()) {
                                numberOfTaps += 1;
                            } else {
                                numberOfTaps = 1;
                            }
                            lastTapTimeMs = System.currentTimeMillis();
                            if (numberOfTaps > 5) {
                                topoTocado.set(iD - 1, true);
                                ocultarTopoBoss(iD - 1);
                                whackTopoBoss(iD - 1);
                            }
                    }
                    return true;
                }
            });

            bomba.setOnTouchListener(new View.OnTouchListener() {
                private GestureDetector gDet = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        bombaEncendida.set(iD - 1, true);
                        ocultarBomba(iD - 1);
                        whackBomba(iD - 1);
                        return true;
                    }
                });
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    gDet.onTouchEvent(motionEvent);
                    return true;
                }
            });

            time.setOnTouchListener(new View.OnTouchListener() {
                private GestureDetector gDet = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public void onLongPress(MotionEvent e) {
                        mainTimer.cancel();
                        //Log.d("", "EL TIEMPO ES: " + globalTiempo);
                        jugar((int)globalTiempo + 6000);
                        ocultarTime(iD - 1);
                    }
                });
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    gDet.onTouchEvent(motionEvent);
                    return true;
                }
            });

            toposId.add(id);
            toposCascoId.add(idConCasco);
            toposBossId.add(idBoss);
            toposWhackId.add(idWhack);
            toposCascoWhackId.add(idCascoWhack);
            toposBossWhackId.add(idBossWhack);
            bombaId.add(idBomba);
            bombaOnId.add(idBombaOn);
            boomId.add(idBoom);
            timerId.add(idTime);

            hoyoSelecionados.add(false);
            topoTocado.add(false);
            topoOn.add(false);
            topoCascoOn.add(false);
            topoBossOn.add(false);
            bombaEncendida.add(false);
            timerArray.add(false);
        }
    }

    public void mostrarTime() {
        int indice = (int)(random.nextDouble()*10);
        if (timerArray.get(indice) == false) {
            hoyoSelecionados.set(indice,true);
            timerArray.set(indice, true);
            int randomId = (int)timerId.get(indice);
            ImageView time = (ImageView) findViewById(randomId);
            time.setVisibility(View.VISIBLE);

            int delay = (int)(random.nextDouble()* 2000 + 1000);
            Timer timer = new Timer();
            TimerTask task = new TimerTask(){
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ocultarTime(indice);
                        }
                    });
                }
            };
            timer.schedule(task, delay); // disminuir según tiempo --> aparición más rápida

        }
        else {}
    }

    public void mostrarTopo(){
        int indice = (int)(random.nextDouble()*10);
        if (hoyoSelecionados.get(indice) == false) {
            hoyoSelecionados.set(indice,true);
            topoOn.set(indice,true);
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
            //mostrarTopo();
        }
    }

    public void mostrarTopoCasco() {
        int indice = (int) (random.nextDouble() * 10);
        if (hoyoSelecionados.get(indice) == false) {
            topoCascoOn.set(indice,true);
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
            //mostrarTopoCasco();
        }
    }

    public void mostrarTopoBoss() {
        int indice = (int) (random.nextDouble() * 10);
        if (hoyoSelecionados.get(indice) == false) {
            topoBossOn.set(indice,true);
            hoyoSelecionados.set(indice, true);
            aparecerBoss = true;
            int randomId = (int) toposBossId.get(indice);
            ImageView topoBoss = (ImageView) findViewById(randomId);
            activarBossAlert();
            topoBoss.setVisibility(View.VISIBLE);

            int delay = (int) (random.nextDouble() * 3500 + 2000);
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ocultarTopoBoss(indice);
                        }
                    });
                }
            };
            timer.schedule(task, delay); // disminuir según tiempo --> aparición más rápida
        } else {
            mostrarTopoBoss();
        }
    }

    public void mostrarBomba() {
        int indice = (int) (random.nextDouble() * 10);
        if (hoyoSelecionados.get(indice) == false) {
            hoyoSelecionados.set(indice, true);
            int randomId = (int) bombaId.get(indice);
            ImageView bomba = (ImageView) findViewById(randomId);
            bomba.setVisibility(View.VISIBLE);

            int delay = (int) (random.nextDouble() * 3500 + 2000);
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ocultarBomba(indice);
                        }
                    });
                }
            };
            timer.schedule(task, delay); // disminuir según tiempo --> aparición más rápida
        } else {
            mostrarBomba();
        }
    }

    public void ocultarTime(int indice) {
        int id = timerId.get(indice);
        ImageView time = findViewById(id);
        time.setVisibility(View.GONE);
        timerArray.set(indice, false);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!timerArray.get(indice)){
                            hoyoSelecionados.set(indice, false);
                        }
                    }
                });
            }
        };
        timer.schedule(task, 1300);
    }

    public void ocultarTopo(int indice) {
        int id = (int)toposId.get(indice);
        ImageView topo = (ImageView) findViewById(id);
        topo.setVisibility(View.GONE);
        topoOn.set(indice,false);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!topoTocado.get(indice)) {
                            hoyoSelecionados.set(indice,false);
                        }
                    }
                });
            }
        };
        timer.schedule(task, 1300);
    }

    public void ocultarTopoCasco(int indice){
        int id = (int)toposCascoId.get(indice);
        ImageView topoCasco = (ImageView) findViewById(id);
        topoCasco.setVisibility(View.GONE);
        topoCascoOn.set(indice,false);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!topoTocado.get(indice)) {
                            hoyoSelecionados.set(indice,false);
                        }
                    }
                });
            }
        };
        timer.schedule(task, 1300);
    }

    public void ocultarTopoBoss(int indice) {
        int id = (int) toposBossId.get(indice);
        ImageView topoBoss = (ImageView) findViewById(id);
        topoBoss.setVisibility(View.GONE);
        topoBossOn.set(indice,false);

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopAlarma = true;
                        if(!topoTocado.get(indice)) {
                            hoyoSelecionados.set(indice,false);
                        }
                    }
                });
            }
        };
        timer.schedule(task, 1300);
    }

    public void ocultarBomba(int indice) {
        int id = (int)bombaId.get(indice);
        ImageView bomba = (ImageView) findViewById(id);
        bomba.setVisibility(View.GONE);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!bombaEncendida.get(indice)) {
                            hoyoSelecionados.set(indice,false);
                        }
                    }
                });
            }
        };
        timer.schedule(task, 1900);
    }

    public void whackTopo(int indice){
        int id = (int)toposWhackId.get(indice);
        ImageView topoWhack = (ImageView) findViewById(id);
        activarWhack();
        topoWhack.setVisibility(View.VISIBLE);
        topoOn.set(indice,false);
        sumarPuntos(10);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        topoWhack.setVisibility(View.GONE);
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        topoTocado.set(indice,false);
                                        hoyoSelecionados.set(indice,false);
                                    }
                                });
                            }
                        };
                        timer.schedule(task, 600);
                    }
                });
            }
        };
        timer.schedule(task, 800);
    }

    public void whackTopoCasco(int indice){
        int id = (int)toposCascoWhackId.get(indice);
        ImageView topoWhack = (ImageView) findViewById(id);
        activarWhack();
        topoWhack.setVisibility(View.VISIBLE);
        topoCascoOn.set(indice,false);
        sumarPuntos(20);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        topoWhack.setVisibility(View.GONE);
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        topoTocado.set(indice,false);
                                        hoyoSelecionados.set(indice,false);
                                    }
                                });
                            }
                        };
                        timer.schedule(task, 600);
                    }
                });
            }
        };
        timer.schedule(task, 800);
    }

    public void whackTopoBoss(int indice){
        int id = (int)toposBossWhackId.get(indice);
        ImageView topoBossWhack = (ImageView) findViewById(id);
        activarWhack();
        topoBossWhack.setVisibility(View.VISIBLE);
        topoBossOn.set(indice,false);
        sumarPuntos(50);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        topoBossWhack.setVisibility(View.GONE);
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        topoTocado.set(indice,false);
                                        hoyoSelecionados.set(indice,false);
                                    }
                                });
                            }
                        };
                        timer.schedule(task, 600);
                    }
                });
            }
        };
        timer.schedule(task, 800);
    }

    public void whackBomba(int indice){
        int id = (int)bombaOnId.get(indice);
        int idBoom = (int)boomId.get(indice);
        ImageView bombaOn = (ImageView) findViewById(id);
        ImageView boom = (ImageView) findViewById(idBoom);
        bombaOn.setVisibility(View.VISIBLE);

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bombaOn.setVisibility(View.GONE);
                        activarExplosion();
                        boom.setVisibility(View.VISIBLE);
                        iniciarExplosion();
                        Timer timer = new Timer();
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        boom.setVisibility(View.GONE);
                                        bombaEncendida.set(indice,false);
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
                                        timer.schedule(task, 600);
                                    }
                                });
                            }
                        };
                        timer.schedule(task, 500);
                    }
                });
            }
        };
        timer.schedule(task, 800);
    }

    public void iniciarExplosion(){
        for(int i = 0; i < numTopos; i++) {
            if(topoOn.get(i)) {
                topoTocado.set(i, true);
                ocultarTopo(i);
                whackTopo(i);

            }
            if(topoCascoOn.get(i)) {
                topoTocado.set(i, true);
                ocultarTopoCasco(i);
                whackTopoCasco(i);
            }
            if(topoBossOn.get(i)){
                topoTocado.set(i, true);
                ocultarTopoBoss(i);
                whackTopoBoss(i);
            }

        }
    }

    public void iniciarAlarma(){
        View alarma = (View)findViewById(R.id.alarma);
        timerAlarma = new Timer();
        taskAlarma = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        alarma.setVisibility(View.VISIBLE);
                        if(stopAlarma) {
                            pararAlarma();
                            alarma.setVisibility(View.GONE);
                            pararAlarma();
                            timerAlarma.cancel();
                            timerAlarma.purge();
                            taskAlarma.cancel();
                        }

                        Timer timer = new Timer();
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        alarma.setVisibility(View.GONE);

                                    }
                                });
                            }
                        };
                        timer.schedule(task, 500);
                    }
                });
            }
        };

        timerAlarma.scheduleAtFixedRate(taskAlarma, new Date(), 1000);
    }

    public void decreaseTime() {
        //Log.d("", "Decreasing time by 1000: " + globalTiempo);
        globalTiempo = globalTiempo - 1000;
    }

    public void jugar(int tiempo) {
        globalTiempo = tiempo;
        //cronometro del juego
        // tiempo de juego establecido
        // llamada onTick cada 1000ms -> 1s
        mainTimer = new CountDownTimer(tiempo, 1000) {
            TextView contador = (TextView)findViewById(R.id.contador);
            public void onTick(long millisUntilFinished) {
                segundosRestantes = millisUntilFinished / 1000;
                contador.setText("00:" + String.format("%02d", segundosRestantes));
                if(!aparecerBoss && puntuacion >= 250) {
                    iniciarAlarma();
                    activarAlarma();
                }
                if(!aparecerBoss && (puntuacion >= 300 || puntuacion >= 700))
                    mostrarTopoBoss();
                if((segundosRestantes == 20 || segundosRestantes == 30 || segundosRestantes == 40) && puntuacion >= 300)
                    mostrarBomba();
                if (segundosRestantes == 20 || segundosRestantes == 45)
                    mostrarTime();

                decreaseTime();
                //Log.d("", hoyoSelecionados.toString());
            }

            public void onFinish() {
                if (globalTiempo == 0)
                masterFinish();
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
        timer.scheduleAtFixedRate(task, new Date(), velocidadJuego);
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

    public void masterFinish() {
        TextView contador = (TextView)findViewById(R.id.contador);
        contador.setText("FIN");
        finJuego();

        cargarDialogo();
        dialogo = construirDialogo.create();
        if(dialogo != null && !dialogo.isShowing()) {
            dialogo.show();
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
        construirDialogo.setCancelable(false);
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