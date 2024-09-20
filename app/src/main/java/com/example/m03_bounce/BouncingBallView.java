package com.example.m03_bounce;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Russ on 08/04/2014.
 */
public class BouncingBallView extends View {

    private ArrayList<Ball> balls = new ArrayList<Ball>(); // list of Balls
    private Ball ball_1;  // use this to reference first ball in arraylist
    private Box box;
    private Rectangle targetRectangle;
    private int score = 0; // Score counter

    // For touch inputs - previous touch (x, y)
    private float previousX;
    private float previousY;
    private Paint scorePaint; // Paint object for drawing the score
    public BouncingBallView(Context context, AttributeSet attrs) {
        super(context, attrs);

        Log.v("BouncingBallView", "Constructor BouncingBallView");

        // create the box
        box = new Box(Color.WHITE);  // ARGB

        //ball_1 = new Ball(Color.GREEN);
        balls.add(new Ball(Color.GREEN));
        ball_1 = balls.get(0);  // points ball_1 to the first; (zero-ith) element of list
        Log.w("BouncingBallLog", "Just added a bouncing ball");

        //ball_2 = new Ball(Color.CYAN);
        balls.add(new Ball(Color.CYAN));
        Log.w("BouncingBallLog", "Just added another bouncing ball");
        // Create the target rectangle
        targetRectangle = new Rectangle(Color.MAGENTA, 100, 300, 400, 400); // Position and size of the rectangle

        // Initialize Paint for the score display
        scorePaint = new Paint();
        scorePaint.setColor(Color.BLACK); // Set score text color
        scorePaint.setTextSize(60);       // Set the size of the score text
        scorePaint.setTextAlign(Paint.Align.CENTER); // Center the text
        // To enable keypad
        this.setFocusable(true);
        this.requestFocus();
        // To enable touch mode
        this.setFocusableInTouchMode(true);
    }

    // Called back to draw the view. Also called after invalidate().
    @Override
    protected void onDraw(Canvas canvas) {

        Log.v("BouncingBallView", "onDraw");

        // Draw the components
        box.draw(canvas);
        targetRectangle.draw(canvas);
        //canvas.drawARGB(0,25,25,25);
        //canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        for (Ball shape : balls) {
            shape.draw(canvas);  // Draw each shape
            shape.moveWithCollisionDetection(box);  // Update the position of the shape

            // Check for collision with the target rectangle
            if (targetRectangle.isColliding(shape.x, shape.y, shape.radius)) {
                score++; // Increment score
                Log.d("Collision", "Collision detected! Score: " + score);
            }
        }
        // Draw the score at the bottom of the screen
        canvas.drawText("Score: " + score, (float) getWidth() / 2, getHeight() - 100, scorePaint);


        // Delay on UI thread causes big problems!
        // Simulates doing busy work or waits on UI (DB connections, Network I/O, ....)
        //  I/Choreographer? Skipped 64 frames!  The application may be doing too much work on its main thread.
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//        }

        // Check what happens if you draw the box last
        //box.draw(canvas);

        // Check what happens if you don't call invalidate (redraw only when stopped-started)
        // Force a re-draw
        this.invalidate();
    }

    // Called back when the view is first created or its size changes.
    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        // Set the movement bounds for the ball
        box.set(0, 0, w, h);
        Log.w("BouncingBallLog", "onSizeChanged w=" + w + " h=" + h);
    }

    // Touch-input handler
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float currentX = event.getX();
        float currentY = event.getY();
        float deltaX, deltaY;
        float scalingFactor = 5.0f / (Math.min(box.xMax, box.yMax));
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                previousX = currentX;
                previousY = currentY;
                break;

            case MotionEvent.ACTION_UP:
                // Calculate the swipe distance
                deltaX = currentX - previousX;
                deltaY = currentY - previousY;
                double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                // Set a speed threshold to decide between square and circle
                float speedThreshold = 5.0f; // Adjust this value based on your needs

                if (speed > speedThreshold) {
                    // Fast swipe: add a Square
                    balls.add(new Square(Color.RED, currentX, currentY, deltaX * scalingFactor, deltaY * scalingFactor));
                } else {
                    // Slow swipe: add a Circle
                    float speedMultiplier = 20.0f; // Increase this value to make the ball move faster
                    balls.add(new Ball(Color.BLUE, currentX, currentY, deltaX * scalingFactor * speedMultiplier, deltaY * scalingFactor * speedMultiplier));
                }
                ball_1.speedX += deltaX * scalingFactor;
                ball_1.speedY += deltaY * scalingFactor;
                //Log.w("BouncingBallLog", " Xspeed=" + ball_1.speedX + " Yspeed=" + ball_1.speedY);
                Log.w("BouncingBallLog", "x,y= " + previousX + " ," + previousY + "  Xdiff=" + deltaX + " Ydiff=" + deltaY);

                // A way to clear list when too many balls
                if (balls.size() > 20) {
                    // leave first ball, remove the rest
                    Log.v("BouncingBallLog", "too many balls, clear back to 1");
                    balls.clear();
                    // Add a new ball to ensure the list is not empty
                    balls.add(new Square(Color.GREEN, 100, 100, 5, 5));  // Add a default square or circle
                    ball_1 = balls.get(0);  // points ball_1 to the first (zero-ith) element of list
                }

        }
        // Save current x, y
        previousX = currentX;
        previousY = currentY;

        // Try this (remove other invalidate(); )
//        this.invalidate();

        return true;  // Event handled
    }

    Random rand = new Random();
    // called when button is pressed
    public void RussButtonPressed() {
        Log.d("BouncingBallView  BUTTON", "User tapped the  button ... VIEW");

        //get half of the width and height as we are working with a circle
        int viewWidth = this.getMeasuredWidth();
        int viewHeight = this.getMeasuredHeight();

        // make random x,y, velocity
        int x = rand.nextInt(viewWidth);
        int y = rand.nextInt(viewHeight);
        int dx = rand.nextInt(300);
        int dy = rand.nextInt(200);
        int[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA};
        int randomColor = colors[(int)(Math.random() * colors.length)];
        balls.add(new Ball(randomColor,x, y, dx, dy));  // add ball with random color
//        balls.add(new Ball(Color.RED, x, y, dx, dy));  // add ball at every touch event
    }
}
