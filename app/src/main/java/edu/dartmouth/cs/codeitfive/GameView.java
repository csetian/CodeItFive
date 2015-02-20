package edu.dartmouth.cs.codeitfive;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class GameView extends GLSurfaceView {

    private GameRenderer renderer;

    public GameView(Context context) {
        super(context);

        renderer = new GameRenderer();

//        this.setEGLConfigChooser(true);
        this.setRenderer(renderer);
        this.setZOrderOnTop(false);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        renderer = new GameRenderer();
        this.setRenderer(renderer);
        this.setZOrderOnTop(false);
    }
//
//    public GameView(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//    }

}