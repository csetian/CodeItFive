package edu.dartmouth.cs.codeitfive;

import android.content.Context;
import android.util.Log;
 
public class Global {
    public static Context context;
	public static int BACK = R.drawable.coke_background;
    public static int COKE = R.drawable.coke;
	public static final int GAME_THREAD_FPS_SLEEP = (1000/60);

    public static int GAME_SCREEN_WIDTH = 0;
    public static int GAME_SCREEN_HEIGHT = 0;
    public static float backYScale = 0f;
    public static float cokeYPos = 0.5f;
}