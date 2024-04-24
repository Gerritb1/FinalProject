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
import java.util.Random;

class SnakeGame extends SurfaceView implements Runnable {

    // Objects for the game loop/thread
    private Thread mThread = null;

    protected boolean isFirstPause = true;

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
    private Canvas sCanvas;
    protected final SurfaceHolder mSurfaceHolder;
    private final Paint mPaint;

    // Typeface object to hold the custom font
    protected Typeface mCustomFont;

    // A snake ssss
    private Snake mSnake;
    // And an apple
    private Apple mApple;

    //And an Yellow Apple
    private YellowApple yApple;

    // And a Poisoned Apple
    private PoisonApple pApple;

    // And four rock objects
    private Rock rock1;
    private Rock rock2;
    private Rock rock3;
    private Rock rock4;
    private ArrayList<Rock> rocks;

    private Bitmap mBackgroundBitmap;
    private final DrawPauseButton drawPauseButton;
    private final UpdateSystem updateSystem;
    private Context mContext;
    private TextDrawer textDrawer;

    private Random random;
    private boolean isSnakeDead = true;

    private int randomNumber = 0;
    private SurfaceHolder sf;

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
        sf = getHolder();
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
        this.mContext = context;

        random = new Random();

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


    public boolean isPaused() {
        return mPaused;
    }

    // Refactored

    public void fontTryCatch(Context context) {
        try {
            // Load the custom font
            mCustomFont = ResourcesCompat.getFont(context, R.font.font);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Refactored
    public void loadBackgroundImage(Context context, Point size) {
        // Load the background image
        mBackgroundBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);

        // Scale the image to match the screen size
        mBackgroundBitmap = Bitmap.createScaledBitmap(mBackgroundBitmap, size.x, size.y, true);

    }

    // Method to create and draw the pause button

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

        pApple = PoisonApple.getPoisonApple(context,
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
                if (updateSystem.updateRequired() && !isSnakeDead) {
                    update();
                }
            }

                draw();

        }
    }

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
        isSnakeDead = false;
    }

    // Update the newGame() method to set isFirstPause to true
    public void update() {
        if (!mPaused) {
            mSnake.move();

            // Refactored, this is for the red apple
            updateMApple();

            // Refactored, this is for the yellow apple
            updateYApple();

            // Refactored, this is for the poison apple
            updatePApple();

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

    // Refactored, this is for the red apple
    public void updateMApple() {
        if (mSnake.checkDinner(mApple.getLocation())) {
            mApple.spawn();
            if (yApple.isSpawned()) {
                yApple.hide();
            }
            if (pApple.isSpawned()) {
                pApple.hide();
            }
            mScore++;
            mSP.play(mEat_ID, 1, 1, 0, 0, 1);
            randomNumber = random.nextInt(4);
        }
    }

    // Refactored, this is for the yellow apple
    public void updateYApple() {
        // Check if the score is a dividable by 4 and spawn the yellow apple
        if ((mScore > 0) && (mScore % 4 == 0) && !yApple.isSpawned()) {
            yApple.spawn();
        }

        if (mSnake.bigCheckDinner(yApple.getLocation())) {
            yApple.hide();
            mApple.spawn();
            if(pApple.isSpawned()){
                pApple.hide();
            }
            mScore+=3;
            mSP.play(mEat_ID, 1, 1, 0, 0, 1);

            randomNumber = random.nextInt(3);

            // to grow the snake body segment by 3, since 2+1=3
            mSnake.grow(2);
        }
    }

    // Refactored, this is for the poison apple
    public void updatePApple() {
        if (mSnake.bigCheckDinner(pApple.getLocation())) {
            mScore -= 2;
            if (mScore < 0) {
                resetGame();
            } else {
                pApple.hide();
                mApple.spawn();
                if (yApple.isSpawned()) {
                    yApple.hide();
                }
                mSP.play(mEat_ID, 1, 1, 0, 0, 1);

                mSnake.shrink(3);
                randomNumber = random.nextInt(5);
            }
        }

        if ((mScore > 0) && (randomNumber == 2) && !pApple.isSpawned()) {
            pApple.spawn();
        }
    }

    private void resetGame() {
        if (!mPaused) {
            mScore = 0;

            // Refactored
            spawnHide();

            mApple.spawn();
            mApple.hide(); // Hide the apple upon resetting the game
            mSnake.reset(NUM_BLOCKS_WIDE, mNumBlocksHigh);
            mSnake.hide(); // Hide the snake upon resetting the game
            isFirstPause = true;// Set isFirstPause to true upon resetting the game
            isSnakeDead = true;
            mPaused = true; // Set mPaused to true upon resetting the game
        }
    }

    // Refactored
    public void spawnHide() {
        for(Rock rock: rocks) {
            rock.spawn();
            rock.hide();
        }

        if(yApple.isSpawned()) {
            yApple.hide();
            yApple.spawned = false;
        }

        if(pApple.isSpawned()) {
            pApple.hide();
            pApple.spawned = false;
        }
    }

    public void draw() {
        // Get a lock on the canvas
        if (mSurfaceHolder.getSurface().isValid()) {
            mCanvas = mSurfaceHolder.lockCanvas();

            // Draw the background image
            mCanvas.drawBitmap(mBackgroundBitmap, 0, 0, mPaint);

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
            // Refactored
            drawSpawnables();
        }
    }

    //Refactored for extraction
    public void checkDrawConditions() {
        if (isFirstPause && mPaused) {
            // Draw the "Tap to play" prompt if the game is initially paused
            drawPaused();
        } else if (mPaused) {
            // Draw the names if the game is paused
            if (textDrawer == null) {
                textDrawer = new TextDrawer(mContext, mCanvas, mPaint);

                textDrawer.drawNames(mCustomFont);
            }

            // Check if NameDrawer instance is not null before calling drawNames
            if (textDrawer != null) {
                textDrawer.drawNames(mCustomFont);
            }
        }
    }


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
        pApple.draw(mCanvas, mPaint);

        // Draw the Rocks
        for(Rock rock: rocks) {
            rock.draw(mCanvas, mPaint);
        }

    }

    // Refactored
    public void drawSpawnables() {
        // Draw the rock only if the game is not paused
        for(Rock rock: rocks) {
            rock.draw(mCanvas, mPaint);
        }
        // Draw the apple only if the game is not paused
        mApple.draw(mCanvas, mPaint);

        yApple.draw(mCanvas, mPaint);

        pApple.draw(mCanvas, mPaint);
    }

    // Refactored
    public void drawPaused() {
        // Set the size and color of the mPaint for the text
        mPaint.setColor(Color.argb(255, 203, 67, 53));
        mPaint.setTextSize(250);
        // Set the custom font to the Paint object
        mPaint.setTypeface(mCustomFont);

        if (isFirstPause && mPaused) {
            if(textDrawer == null) {
                // Instantiate TextDrawer preparing for Injection
                textDrawer = new TextDrawer(mContext, mCanvas, mPaint);
            }

            //Refactored
            textDrawer.drawTapToPlay( ResourcesCompat.getFont(mContext, R.font.retro));

            textDrawer.drawNames(mCustomFont);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if ((motionEvent.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
            if (isFirstPause && isSnakeDead) {
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
    public void pause() {
        mPlaying = false;
        try {
            mThread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }

    // Start the thread
    public void resume() {
        mPlaying = true;
        mThread = new Thread(this);
        mThread.start();
    }
}
