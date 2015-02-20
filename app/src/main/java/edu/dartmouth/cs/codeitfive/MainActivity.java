package edu.dartmouth.cs.codeitfive;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;


public class MainActivity extends Activity {
    private static final String TAG = "Main Activity";
    private static final int MOVE_UP = 0;
    private static final int MOVE_DOWN = 1;
    private static final String TIME_KEY = "best_time_key";
    private Button start_button, replay_button, exit_button;
    private TextView best_time, current_time, shake_it;
    private ImageView bottle;
    private Chronometer timer;
    private float BOTTLE_START_X, BOTTLE_START_Y, BOTTLE_MIN_Y, BOTTLE_MAX_Y;
    private long startTime;
    private float distance_moved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Global.context = getApplicationContext();
        setContentView(R.layout.activity_main);

        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(this);
        long bestTime = mPref.getLong(TIME_KEY, 0);

        // hide restart/exit
        replay_button = (Button) findViewById(R.id.button_restart);
        exit_button = (Button) findViewById(R.id.button_exit);
        replay_button.setVisibility(View.GONE);
        exit_button.setVisibility(View.GONE);


        // get button references
        start_button = (Button) findViewById(R.id.button_start);
        shake_it = (TextView) findViewById(R.id.shakeIttextView);
        best_time = (TextView) findViewById(R.id.timerView);
        if (bestTime > 0)
            best_time.setText(getDurationBreakdown(bestTime));
        else
            best_time.setText("n/a");
        timer = (Chronometer) findViewById(R.id.chronometer);
        bottle = (ImageView) findViewById(R.id.cokeBottle);
        BOTTLE_START_X = bottle.getX();
        BOTTLE_START_Y = bottle.getY();
//        BOTTLE_MIN_Y = BOTTLE_START_Y * 0.5f;
//        BOTTLE_MAX_Y = BOTTLE_START_Y * 1.5f;
        distance_moved = 0;
        Log.d(TAG, "bottle init pos " + BOTTLE_START_X + "," + BOTTLE_START_X);

        bottle.setOnTouchListener(new View.OnTouchListener() {
            private PointF DownPT = new PointF(); // Record Mouse Position When Pressed Down
            private PointF StartPT = new PointF(); // Record Start Position of 'img'

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eid = event.getAction();
                long ctime = System.currentTimeMillis();
                if (ctime - startTime > 50){
                    Log.d(TAG, "time difference!");
                    if (distance_moved > 100){
                        Global.backYScale+=0.025;
                        if (Global.backYScale>=.99){
                            wonGame();
                        }
                    }
                    else{
                        if (Global.backYScale != 0){
                            Global.backYScale-=0.05;

                        }
                    }
                    startTime= ctime;
                    distance_moved = 0;
                }
                switch (eid) {
                    case MotionEvent.ACTION_MOVE:
                        PointF mv = new PointF(event.getX() - DownPT.x, event.getY() - DownPT.y);
                        bottle.setY((int) (StartPT.y + mv.y));
                        distance_moved+=Math.abs(mv.y);
                        StartPT = new PointF(bottle.getX(), bottle.getY());
                        break;
                    case MotionEvent.ACTION_DOWN:
                        DownPT.x = event.getX();
                        DownPT.y = event.getY();
                        StartPT = new PointF(bottle.getX(), bottle.getY());
                        break;
                    case MotionEvent.ACTION_UP:
                        // Nothing have to do
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

    }


               /* Log.d(TAG, "bottle has been touched!");
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        x = event.getX();
                        y = event.getY();
                        Log.d(TAG, "ACTION DOWN event at pos " +x+","+y);
                        old = x + y;
                        old_y = y;
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        x = event.getX();
                        y = event.getY();
                        Log.d(TAG, "ACTION POINTER DOWN event at pos " +x+","+y);
                        break;
                    case  MotionEvent.ACTION_UP:
                        x = event.getX();
                        y = event.getY();
                        Log.d(TAG, "ACTION UP event at pos " +x+","+y);
                        break;
                    case MotionEvent.ACTION_MOVE:


                        int newv= (int) (event.getX() + event.getY());
                        float ny = event.getY();
                        Log.d(TAG, "old y = "+old_y + ". new y = "+ny);
                        if(Math.abs(old_y-ny)>50)
                        {
                            Log.d(TAG, "threshold reached");

                            old = event.getX() + event.getY();
                            old_y = event.getY();
                            bottle.setY(event.getY());
                        }

                        x = event.getRawX();
                        y = event.getRawY();
                        Log.d(TAG, "ACTION MOVE event at pos " +x+","+y);
                  //      bottle.setY(y);
                        break;

                }
                if (event.getEdgeFlags() == MotionEvent.EDGE_TOP || event.getEdgeFlags() == MotionEvent.EDGE_BOTTOM){
                    Log.d(TAG, "Edge flags top or bottom");
                    bottle.setY(BOTTLE_START_Y);
                }
                */
//                return true;
//            }
//        });
//
//    }

//    private void checkDirectionChange(PointF p1, PointF p2) {
//        if (p2.y > (p1.y + 75)){
//
//        }






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

    public void onStartClicked(View view) {
        start_button.setVisibility(View.INVISIBLE);
        shake_it.setVisibility(View.INVISIBLE);
        exit_button.setVisibility(View.INVISIBLE);
        replay_button.setVisibility(View.INVISIBLE);
        bottle.setImageDrawable(getDrawable(R.drawable.coke));
        timer.start();
        startTime = System.currentTimeMillis();
    }

    private void wonGame() {
        long elapsedTime = SystemClock.elapsedRealtime() - timer.getBase();
        timer.stop();
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(this);
        long bestTime = mPref.getLong(TIME_KEY, Long.MAX_VALUE);
        if (elapsedTime < bestTime || bestTime <= 0) {
            SharedPreferences.Editor editor = mPref.edit();
            editor.putLong(TIME_KEY, elapsedTime);
            editor.commit();
        }
        best_time.setText(getDurationBreakdown(bestTime));
        bottle.setImageDrawable(getDrawable(R.drawable.open));
    }

    public void onRestartClicked(View v) {
        onStartClicked(v);
    }

    public void onExitClicked(View v) {
        finish();
    }

    public static String getDurationBreakdown(long millis)
    {
        if(millis < 0)
        {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder sb = new StringBuilder(64);
        sb.append(minutes);
        sb.append(":");
        sb.append(seconds);
        sb.append(".");
        sb.append(millis);

        return(sb.toString());
    }
}
