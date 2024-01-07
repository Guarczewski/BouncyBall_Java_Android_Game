package com.example.bouncyballgame;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import java.util.Random;


@SuppressLint("ViewConstructor")
class GameView extends View {
    private int barSize = 350; // Szerokość belki
    private int targetSize = 100; // Szerokośc celu
    private int offsetX = - 20, offsetY = -170; // Centrowanie celi
    private int targetX = 14, targetY = 15;
    private int points = 0;
    protected int ballVelX = 7, ballVelY = -7;

    Rect[][] targets;

    protected int cSize = 50;
    private final Handler handler = new Handler();
    private final long delayMillis = 16; // Około 60 klatek na sekundę

    private int mouseCordX;

    Rect bouncyBall, bouncyBar;

    @SuppressLint("ClickableViewAccessibility")
    public GameView(Context context){
        super(context);

        targets = new Rect[targetX][targetY];

        for (int i = 0; i < targetX; i++) {
            for (int j = 0; j < targetY; j++) {

                targets[i][j] = new Rect(
                        (i * targetSize) - offsetX,
                        (j * targetSize) - offsetY,
                        ((i + 1) * targetSize) - offsetX,
                        (((j + 1) * targetSize)) - offsetY);

            }
        }

        bouncyBall = new Rect(50,2250,150,2350);
        bouncyBar = new Rect(0,2500,0,2600);

        setOnTouchListener((v, event) -> {
            bouncyBar.left = (int) event.getX() - barSize;
            bouncyBar.right = (int) event.getX() + barSize;
            return true;
        });

        Runnable drawRunnable = new Runnable() {
            @Override
            public void run() {
                Update();
                invalidate();
                handler.postDelayed(this, delayMillis);
            }
        };
        handler.postDelayed(drawRunnable, delayMillis);
    }

    protected void Update(){

        if (bouncyBall.top + ballVelY <= 0) {
            ballVelY *= -1;
        }
        bouncyBall.top += ballVelY;
        bouncyBall.bottom += ballVelY;

        if (bouncyBall.left + ballVelX <= 0 || bouncyBall.right + ballVelX >= this.getWidth()) {
            ballVelX *= -1;
        }
        bouncyBall.left += ballVelX;
        bouncyBall.right += ballVelX;

        Rect temp = new Rect(bouncyBall);

        for (int i = 0; i < targetX; i++) {
            for (int j = 0; j < targetY; j++) {

                if (temp.intersect(targets[i][j])) {
                    targets[i][j] = new Rect(0, 0, 0, 0);
                    points++;
                    ballVelY *= -1;

                }
            }
        }

        if (temp.intersect(bouncyBar))
            ballVelY *= -1;


    }
    @SuppressLint("DrawAllocation")
    protected void onDraw(Canvas canvas) {
        @SuppressLint("DrawAllocation")
        Paint paint = new Paint();



        paint.setColor(Color.BLACK);
        canvas.drawRect(bouncyBar, paint);

        //canvas.drawOval(bouncyBall.left,bouncyBall.top,bouncyBall.right,bouncyBall.bottom, paint);

        canvas.drawRect(bouncyBall, paint);

        paint.setColor(Color.RED);

        for (int i = 0; i < targetX; i++) {
            for (int j = 0; j < targetY; j++) {
                canvas.drawRect(targets[i][j], paint);
            }
        }

        paint.setColor(Color.BLACK);
        paint.setTextSize(80);
        canvas.drawText("Punkty: " + points, 0, 100, paint);
    }
}
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GameView(this));
    }
}