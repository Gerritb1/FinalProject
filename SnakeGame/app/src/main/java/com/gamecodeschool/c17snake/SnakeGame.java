package com.gamecodeschool.c17snake;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.io.IOException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.core.content.res.ResourcesCompat;
import java.util.*;

class SnakeGame extends SurfaceView implements Runnable, Game {

    // Objects for the game loop/thread
    private Thread mThread = null;

    private boolean isFirstPause = true;

    // Is the game currently playing and or paused?
    private volatile boolean mPlaying = false;
    private volatile boolean mPaused = true;

    // for playing sound effects
    private SoundPool mSP;
    private int mEat_ID = -1;
    private int mCrashID = -1;

    // The size in segments of the playable area
    private final int NUM_BLOCKS_WIDE = 40;
    private int mNumBlocksHigh;

    // How many points does the player have
    private int mScore;

    //Pause button rendering objects
    private Rect mPauseButtonRect;
    private Paint mPauseButtonPaint;

    // Objects for drawing
    private Canvas mCanvas;
    private final SurfaceHolder mSurfaceHolder;
    private final Paint mPaint;

    // Typeface object to hold the custom font
    private Typeface mCustomFont;

    // A snake ssss
    private Snake mSnake;
    // And an apple
    private Apple mApple;

    //And an Yellow Apple
    private YellowApple yApple;

    // And four rock objects
    private Rock rock1;
    private Rock rock2;
    private Rock rock3;
    private Rock rock4;
    private ArrayList<Rock> rocks;

    private Bitmap mBackgroundBitmap;
    private final DrawPauseButton drawPauseButton;
    private final UpdateSystem updateSystem;

    // This is the constructor method that gets called
    // from SnakeActivity
    protected SnakeGame(Context context, Point size) {
        super(context);

        // Refactored
        fontTryCatch(context);

        // Refactored
        loadBackgroundImage(context, size);

        // Refactored
        soundPool();

        // Refactored
        tryCatch(context);

        // Initialize the drawing objects
        mSurfaceHolder = getHolder();
        mPaint = new Paint();

        // Refactored
        callConstructors(context, size);

        // Create the pause button
        createPauseButton();

        //Initialize the drawButtonPause
        drawPauseButton = DrawPauseButton.getDrawPauseButton(context, this);
        updateSystem = new UpdateSystem();

        //Refactored
        listOfRocks();

    }

    public void listOfRocks() {
        rocks = new ArrayList<>();
        rocks.add(rock1);
        rocks.add(rock2);
        rocks.add(rock3);
        rocks.add(rock4);
    }

    //Builder for buildDesign Pattern Still under develelopment
   /* public SnakeGame() {


          DrawBuilder builder = new DrawBuilder()
                .setCanvas(mCanvas)
                .setPaint(mPaint)
                .setFirstPause(isFirstPause)
                .setPaused(mPaused);

        this.drawTapToPlayBehavior = builder.setMessage("Tap to play").buildDrawTapToPlay();
        this.drawNamesBehavior = builder.setMessage("John Doe").buildDrawNames();
        this.checkDrawConditionsBehavior = builder.buildCheckDrawConditions(drawTapToPlayBehavior, drawNamesBehavior);
        this.drawAppleBehavior = builder.buildDrawApple();
        this.drawColorSizeBehavior = builder.buildDrawColorSize();
        this.drawPausedBehavior = builder.buildDrawPaused();
    }*/


    // Dependency injector for DI design still under development
   /* public void setDependencies(Snake snake, Apple apple, SoundPool soundPool, int eatSoundID, int crashID, Point screenSize) {
        this.mSnake = snake;
        this.mApple = apple;
        this.mSP = soundPool;
        this.mEat_ID = eatSoundID;
        this.mCrashID = crashID;
    }*/

    public boolean isPaused() {
        return mPaused;
    }

    // Refactored
    @Override
    public void fontTryCatch(Context context) {
        try {
            // Load the custom font
            mCustomFont = ResourcesCompat.getFont(context, R.font.font);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Refactored
    @Override
    public void loadBackgroundImage(Context context, Point size) {
        // Load the background image
        mBackgroundBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);

        // Scale the image to match the screen size
        mBackgroundBitmap = Bitmap.createScaledBitmap(mBackgroundBitmap, size.x, size.y, true);
    }

    // Method to create and draw the pause button
    @Override
    public void createPauseButton() {

        int buttonWidth = 400;
        int buttonHeight = 100;
        int buttonLeft = 800;
        int buttonTop = 50;

        // Create a Rect object representing the pause button's bounds
        mPauseButtonRect = new Rect(buttonLeft, buttonTop, buttonLeft + buttonWidth, buttonTop + buttonHeight);

        // Define the appearance of the pause button (e.g., color)
        mPauseButtonPaint = new Paint();
        mPauseButtonPaint.setColor(Color.RED); // Adjust color as needed
    }

    //Refactored
    @Override
    public void soundPool() {
        // Initialize the SoundPool
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        mSP = new SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(audioAttributes)
                .build();
    }

    //Refactored
    @Override
    public void tryCatch(Context context) {
        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            // Prepare the sounds in memory
            descriptor = assetManager.openFd("get_apple.ogg");
            mEat_ID = mSP.load(descriptor, 0);

            descriptor = assetManager.openFd("snake_death.ogg");
            mCrashID = mSP.load(descriptor, 0);

        } catch (IOException e) {
            // Error
        }
    }

    //Refactored
    @Override
    public void callConstructors(Context context, Point size) {
        // Work out how many pixels each block is
        int blockSize = size.x / NUM_BLOCKS_WIDE;
        mNumBlocksHigh = size.y / blockSize;

        // Call the constructors of our two game objects by using the Singelton pattern
        mApple = Apple.getApple(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);

        yApple = YellowApple.getYellowApple(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);

        mSnake = Snake.getSnake(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);

        //Refactored
        rockInitialization(context, size);

    }

    //Refactored
    public void rockInitialization(Context context, Point size) {
        // Work out how many pixels each block is
        int blockSize = size.x / NUM_BLOCKS_WIDE;
        mNumBlocksHigh = size.y / blockSize;

        // Initializing the rocks
        rock1 = Rock.getRock1(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);
        rock2 = Rock.getRock2(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);
        rock3 = Rock.getRock3(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);
        rock4 = Rock.getRock4(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);
    }

    // Handles the game loop
    @Override
    public void run() {
        while (mPlaying) {
            if(!mPaused) {
                if (updateSystem.updateRequired()) {
                    update();
                }
            }
            draw();
        }
    }

    @Override
    public void newGame() {
        // Reset the snake and spawn the apple if it's not paused and it's the first pause
        if (!mPaused && isFirstPause) {
            mSnake.reset(NUM_BLOCKS_WIDE, mNumBlocksHigh);
            mApple.spawn();
            if(mScore > 3) {
                yApple.spawn();
            }
            for(Rock rock: rocks) {
                rock.spawn();
            }
        }

        isFirstPause = mPaused;
    }

    // Update the newGame() method to set isFirstPause to true
    @Override
    public void update() {
        if (!mPaused) {
            mSnake.move();

            if (mSnake.checkDinner(mApple.getLocation())) {
                mApple.spawn();
                mScore++;
                mSP.play(mEat_ID, 1, 1, 0, 0, 1);
            }

            // Check if the score is above 3 and spawn the yellow apple
            if (mScore > 3 && !yApple.isSpawned()) {
                yApple.spawn();
            }

            if (mSnake.checkDinner(yApple.getLocation())) {
                yApple.spawn();
                mScore+=3;
                mSP.play(mEat_ID, 1, 1, 0, 0, 1);

                // to grow the snake body segment by 3, since 2+1=3
                mSnake.grow(2);
            }

            for(Rock rock: rocks) {
                if (mSnake.hitRock(rock.getLocation())) {
                    resetGame();
                }
            }

            if (mSnake.detectDeath()) {
                // Reset the score and the game if snake dies
                resetGame();
            }
        }
    }

    private void resetGame() {
        if (!mPaused) {
            mScore = 0;

            for(Rock rock: rocks) {
                rock.spawn();
                rock.hide();
            }

            if(yApple.isSpawned()) {

                yApple.hide();
                yApple.spawned = false;

            }
            mApple.spawn();
            mApple.hide(); // Hide the apple upon resetting the game
            mSnake.reset(NUM_BLOCKS_WIDE, mNumBlocksHigh);
            mSnake.hide(); // Hide the snake upon resetting the game
            isFirstPause = true; // Set isFirstPause to true upon resetting the game
            mPaused = true; // Set mPaused to true upon resetting the game
        }
    }

    @Override
    public void draw() {
        // Get a lock on the canvas
        if (mSurfaceHolder.getSurface().isValid()) {
            mCanvas = mSurfaceHolder.lockCanvas();

            // Draw the background image
            mCanvas.drawBitmap(mBackgroundBitmap, 0, 0, null);

            // Draw the score
            drawColorSize();

            // Refactored
            drawConditions();

            // Unlock the canvas and reveal the graphics for this frame
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }


    // Refactored
    public void drawConditions() {
        // Check and draw conditions based on game state (paused, tap to play, etc.)
        checkDrawConditions();

        // Draw the pause button in any case except for the initial "Tap to play" state
        if (!isFirstPause || !mPaused) {
            drawPauseButton.drawButton(mCanvas, mPaint);
        }

        // Draw game elements if not paused
        if (!mPaused) {
            drawApple();
            drawRock();
            drawYellowApple();
        }
    }

    public void drawRock() {
        // Draw the rock only if the game is not paused
        for(Rock rock: rocks) {
            rock.draw(mCanvas, mPaint);
        }

    }

    public void checkDrawConditions() {
        if (isFirstPause && mPaused) {
            // Draw the "Tap to play" prompt if the game is initially paused
            drawPaused();
        } else if (mPaused) {
            // Draw the names if the game is paused
            if (nameDrawer == null) {
                nameDrawer = new NameDrawer(getContext(), mCanvas, mPaint, this, DrawPauseButton.getDrawPauseButton(getContext(), this));
                nameDrawer.drawNames();
            }

            // Check if NameDrawer instance is not null before calling drawNames
            if (nameDrawer != null) {
                //Injecting Dependencies
                nameDrawer.setDrawPauseButton(DrawPauseButton.getDrawPauseButton(getContext(), this));
                nameDrawer.drawNames();
            }
        }
    }


    //Refactored for extraction
    public void drawApple() {
        // Draw the apple only if the game is not paused
        mApple.draw(mCanvas, mPaint);

    }

    public void drawYellowApple() {
        yApple.draw(mCanvas, mPaint);
    }

    // Refactored
    @Override
    public void drawColorSize() {
        // Set the size and color of the mPaint for the text
        mPaint.setColor(Color.argb(255, 255, 255, 255));
        mPaint.setTextSize(120);

        // Draw the score
        mCanvas.drawText("" + mScore, 20, 120, mPaint);

        // Draw the apples and the snake
        mApple.draw(mCanvas, mPaint);
        mSnake.draw(mCanvas, mPaint);
        yApple.draw(mCanvas, mPaint);

        // Draw the Rocks
        for(Rock rock: rocks) {
            rock.draw(mCanvas, mPaint);
        }

    }

    // Refactored
    @Override
    public void drawPaused() {
        // Set the size and color of the mPaint for the text
        mPaint.setColor(Color.argb(255, 203, 67, 53));
        mPaint.setTextSize(250);
        // Set the custom font to the Paint object
        mPaint.setTypeface(mCustomFont);

        if (isFirstPause && mPaused) {
            //Refactored
            drawTapToPlay();

            drawNames();
        }

    }

    // Refactored
    @Override
    public void drawTapToPlay() {
        // Draw the "Tap to play" message if the game is initially paused
        String message = getResources().getString(R.string.tap_to_play);

        // Get the width and height of the message
        float messageWidth = mPaint.measureText(message);
        float messageHeight = mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().top;

        // Get the screen dimensions
        Point screenDimensions = drawPauseButton.getScreenDimensions();
        int screenWidth = screenDimensions.x;
        int screenHeight = screenDimensions.y;

        // Calculate the position to center the text horizontally and vertically
        float centerX = (screenWidth - messageWidth) / 2;
        float centerY = (screenHeight + messageHeight) / 2;

        // Draw the "Tap to play" message centered on the screen
        mCanvas.drawText(message, centerX, centerY, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if ((motionEvent.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
            if (isFirstPause) {
                // If the game beginning, start the game
                mPaused = false;
                newGame();
            }else if(mPaused && mPauseButtonRect.contains((int) motionEvent.getX(), (int) motionEvent.getY())){
                //If the game is paused, resume the game
                mPaused = false;
            }else if (mPauseButtonRect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                // If the pause button is touched, pause the game
                mPaused = true;
            } else if (!mPaused) {
                // If the game is running and not paused, handle snake movement
                mSnake.switchHeading(motionEvent);
            }
            return true;
        }
        return true;
    }

    // Stop the thread
    @Override
    public void pause() {
        mPlaying = false;
        try {
            mThread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }

    // Start the thread
    @Override
    public void resume() {
        mPlaying = true;
        mThread = new Thread(this);
        mThread.start();
    }
}
