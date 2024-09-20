package com.example.m03_bounce;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class Rectangle extends Ball {
    float left, top, right, bottom; // Rectangle's boundaries
    private Paint paint;
    private RectF bounds; // Needed for Canvas.drawRect

    // Constructor
    public Rectangle(int color, float left, float top, float right, float bottom) {
        super(color);
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.paint = new Paint();
        this.paint.setColor(color);
        this.bounds = new RectF(left, top, right, bottom);
    }

    // Draw the rectangle on the canvas
    public void draw(Canvas canvas) {
        canvas.drawRect(bounds, paint);
    }

    // Method to check for collision with another shape
    public boolean isColliding(float x, float y, float size) {
        return x + size > left && x < right && y + size > top && y < bottom;
    }
}