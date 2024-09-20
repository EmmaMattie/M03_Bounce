package com.example.m03_bounce;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.Random;

public class Square extends Ball{

    float size = 100;      // Square's side length
    float x;               // Square's top-left corner (x, y)
    float y;
    float speedX;          // Square's speed
    float speedY;
    private RectF bounds;  // Needed for Canvas.drawRect
    private Paint paint;   // The paint style, color used for drawing

    Random r = new Random();  // seed random number generator

    // Constructor
    public Square(int color) {
        super(color);
        bounds = new RectF();
        paint = new Paint();
        paint.setColor(color);

        // random position and speed
        x = size + r.nextInt(800);
        y = size + r.nextInt(800);
        speedX = r.nextInt(10) - 5;
        speedY = r.nextInt(10) - 5;
    }

    // Constructor
    public Square(int color, float x, float y, float speedX, float speedY) {
        super(color, x, y, speedX, speedY);
        bounds = new RectF();
        paint = new Paint();
        paint.setColor(color);

        // use parameter position and speed
        this.x = x;
        this.y = y;
        this.speedX = speedX;
        this.speedY = speedY;
    }
@Override
    public void moveWithCollisionDetection(Box box) {
        // Get new (x,y) position
        x += speedX;
        y += speedY;

        // Detect collision and react
        if (x + size > box.xMax) {
            speedX = -speedX;
            x = box.xMax - size;
        } else if (x < box.xMin) {
            speedX = -speedX;
            x = box.xMin;
        }
        if (y + size > box.yMax) {
            speedY = -speedY;
            y = box.yMax - size;
        } else if (y < box.yMin) {
            speedY = -speedY;
            y = box.yMin;
        }
    }
@Override
    public void draw(Canvas canvas) {
        bounds.set(x, y, x + size, y + size);
        canvas.drawRect(bounds, paint);
    }
}

