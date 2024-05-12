package com.gamecodeschool.c17snake;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.media.MediaPlayer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.core.content.res.ResourcesCompat;
import java.util.*;
import java.util.Random;
import android.os.Handler;


class SnakeGame extends SurfaceView implements Runnable{

    //Flags
    private volatile boolean mPlaying = false;
    private volatile boolean mPaused = true;
    private boolean isFirstPause = true;
    private boolean isVulnerable = false;
    public static Boolean activityFlag = false;
    private boolean isSnakeDead = true;

    // How many points does the player have
    private int mScore;

    // A snake ssss
    private Snake mSnake;

    //Apple objects
    private Apple mApple;
    private YellowApple yApple;
    private PoisonApple pApple;

    //Rock objects
    private Rock rock1;
    private Rock rock2;
    private Rock rock3;
    private Rock rock4;
    private ArrayList<Rock> rocks;

    //Trash objects
    private Trash trash1;
    private Trash trash2;
    private Trash trash3;
    private Trash trash4;
    private ArrayList<Trash> trashStuff;

    //Energy drink object
    private EnergyDrink eDrink;

    // Game loop/thread objects
    private Thread mThread = null;
    private UpdateSystem updateSystem;

    // Sound effects
    private SoundPool mSP;
    private int mEat_ID = -1;
    private int yEat_ID = -1;
    private int pEat_ID = -1;
    private int mCrashID = -1;
    private int mCrashIDTrash = -1;
    private int mCrashIDRock = -1;

    // Background music
    private MediaPlayer mBackgroundMusic;

    // Drawing objects
    private Canvas mCanvas;
    private final SurfaceHolder mSurfaceHolder;
    private final Paint mPaint;
    private Bitmap mBackgroundBitmap;
    private DrawPauseButton drawPauseButton;
    private TextDrawer textDrawer;

    // Typeface for custom font
    private Typeface mCustomFont;

    // Game area size
    private final int NUM_BLOCKS_WIDE = 40;
    private int mNumBlocksHigh;

    // Pause button rendering objects
    private Rect mPauseButtonRect;
    private Paint mPauseButtonPaint;

    // Timer objects
    private Handler handler;
    private Runnable speedResetTimer;
    private Runnable vulnerabilityTimer;

    //Application context object
    private Context mContext;

    //Randomization
    private Random random;
    private int randomNumber = 0;

    // Trash index & trash probability
    private int trashPiece = 0;
    private int trashChance = 3;
    
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

        // Refactored
        initialize(context);
    }

    // Refactored
    public void initialize(Context context) {
        //Initialize the drawButtonPause
        drawPauseButton = DrawPauseButton.getDrawPauseButton(context, this);

        updateSystem = new UpdateSystem();

        //Refactored
        listOfRocks();
        listOfTrash();
        this.mContext = context;

        random = new Random();

        // Initialize background music
        mBackgroundMusic = MediaPlayer.create(context, R.raw.background_music);
        mBackgroundMusic.setLooping(true);

        handler = new Handler();
    }

    public void listOfRocks() {
        rocks = new ArrayList<>();
        rocks.add(rock1);
        rocks.add(rock2);
        rocks.add(rock3);
        rocks.add(rock4);
    }

    public void listOfTrash() {
        trashStuff = new ArrayList<>();
        trashStuff.add(trash1);
        trashStuff.add(trash2);
        trashStuff.add(trash3);
        trashStuff.add(trash4);
    }


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
            // Load the sounds from the raw folder
            mEat_ID = mSP.load(context, R.raw.get_apple, 1);
            mCrashIDTrash = mSP.load(context, R.raw.trash_sound, 1);
            mCrashIDRock = mSP.load(context, R.raw.rock_sound, 1);
            mCrashID = mSP.load(context, R.raw.game_over_sound, 1);
            yEat_ID = mSP.load(context, R.raw.yellow_apple_sound, 1);
            pEat_ID = mSP.load(context, R.raw.poison_apple_sound, 1);

        } catch (Exception e) {
            e.printStackTrace();
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

        this.yApple = YellowApple.getYellowApple(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);

        this.pApple = PoisonApple.getPoisonApple(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);

        mSnake = Snake.getSnake(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);

        eDrink = EnergyDrink.getEnergyDrink(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);

        //Refactored
        rockInitialization(context, size);
        trashInitialization(context, size);
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

    public void trashInitialization(Context context, Point size) {
        // Work out how many pixels each block is
        int blockSize = size.x / NUM_BLOCKS_WIDE;
        mNumBlocksHigh = size.y / blockSize;

        // Initializing the trash
        trash1 = Trash.getTrash1(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);
        trash2 = Trash.getTrash2(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);
        trash3 = Trash.getTrash3(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);
        trash4 = Trash.getTrash4(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);
    }
    
    // Handles the game loop
    @Override
    public void run() {
        while (mPlaying) {
            if (!mPaused) {
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
            for(Rock rock: rocks) {
                rock.spawn();
            }
            mApple.spawn();
            if(mScore > 3) {
                yApple.spawn();
                eDrink.spawn();
            }
            for(Trash trash: trashStuff) {
                trash.spawn();
                trash.hide();
            }
        }
        isFirstPause = mPaused;
        isSnakeDead = false;
        activityFlag = true;
    }

    // Update the newGame() method to set isFirstPause to true
    public void update() {
        if (!mPaused) {
            mBackgroundMusic.start();
            mSnake.move();

            // Refactored, this is for the red apple
            updateMApple();

            // Refactored, this is for the yellow apple
            updateYApple();

            // Refactored, this is for the poison apple
            updatePApple();
            updateEnergyDrink();

            // Refactored
            updateDeath();
        }
    }

    // Refactored
    public void updateDeath() {
        boolean snakeHitRock = false;
        boolean snakeHitTrash = false;

        for(Rock rock: rocks) {
            if (mSnake.hitRock(rock.getLocation())) {
                if(!isVulnerable) {
                    mSP.play(mCrashIDRock, 1, 1, 0, 0, 1);
                    snakeHitRock = true;
                    break;
                }
            }
        }

        for(Trash trash: trashStuff) {
            if (mSnake.hitRock(trash.getLocation())) {
                if(!isVulnerable) {
                    mSP.play(mCrashIDTrash, 1, 1, 0, 0, 1);
                    snakeHitTrash = true;
                    break;
                }
            }
        }

        if (mSnake.detectDeath() && !snakeHitTrash && !snakeHitRock) {
            mSP.play(mCrashID, 1, 1, 0, 0, 1);
        }
        if (snakeHitRock || snakeHitTrash || mSnake.detectDeath()) {
            startGameOverActivity();
        }
    }


    private void startGameOverActivity() {
        Intent gameOver = new Intent(mContext, GameOverActivity.class);
        gameOver.putExtra("key", mScore);

        mContext.startActivity(gameOver);
        if (mContext instanceof Activity) {
            ((Activity) mContext).overridePendingTransition(0, 0);
            ((Activity)  mContext).finishAffinity();
        }

        mBackgroundMusic.pause();
        mBackgroundMusic.seekTo(0);
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
            if (eDrink.isSpawned()) {
                eDrink.hide();
            }
            mScore++;

            for(int i = 0; i < 4; i++ ) {
                trashStuff.get(trashPiece).chanceToSpawn(mScore, trashChance);
                trashPiece = (trashPiece+1)%3;
                trashChance+= 3;
            }
            trashChance = 4;

            mSP.play(mEat_ID, 1, 1, 0, 0, 1);
            randomNumber = random.nextInt(3);
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
            if (eDrink.isSpawned()) {
                eDrink.hide();
            }
            mScore+=3;

            for(int i = 0; i < 4; i++ ) {
                trashStuff.get(trashPiece).chanceToSpawn(mScore, trashChance);
                trashPiece = (trashPiece+1)%3;
                trashChance+= 4;
            }
            trashChance = 3;

            mSP.play(yEat_ID, 1, 1, 0, 0, 1);
            randomNumber = random.nextInt(3);

            // to grow the snake body segment by 3, since 2+1=3
            mSnake.grow(1);
            mSnake.grow(2);

            // Set the snake as vulnerable
            isVulnerable = true;

            // Set the snake as vulnerable
            mSnake.setVulnerable(true);

            // Start the vulnerability timer
            startVulnerabilityTimer();
        }
    }


    private void startVulnerabilityTimer() {
        // Cancel any existing timer first
        cancelVulnerabilityTimer();

        // Create a new Runnable for the timer
        vulnerabilityTimer = new Runnable() {
            @Override
            public void run() {
                // After 5 seconds, reset the vulnerability
                isVulnerable = false;

                // After 5 seconds, reset the vulnerability
                mSnake.setVulnerable(false);
            }
        };

        // 5 seconds for the vulnerability
        handler.postDelayed(vulnerabilityTimer, 5000);
    }

    private void cancelVulnerabilityTimer() {
        if (vulnerabilityTimer != null) {
            handler.removeCallbacks(vulnerabilityTimer);
        }
    }

    public void updateEnergyDrink() { //Refactored
        // Spawn energy drink if score is a multiple of 5
        if ((mScore > 0) && (mScore % 5 == 0) && !eDrink.isSpawned()) {
            eDrink.spawn();
        }

        // If snake eats the energy drink
        if (mSnake.checkDrink(eDrink.getLocation())) {
            eDrink.hide();
            mApple.spawn();
            if (pApple.isSpawned()) {
                pApple.hide();
            }
            if (yApple.isSpawned()) {
                yApple.hide();
            }
            if(mApple.isSpawned()) {
                mApple.hide();
            }
            if(eDrink.isSpawned()) {
                eDrink.hide();
            }
            randomNumber = random.nextInt(3);

            // Increase snake speed
            snakeSpeed();
        }
    }

    public void snakeSpeed() { //Extracted from updateEnergyDrink
        // Increase game speed
        updateSystem.setTargetFPS(20);

        // Cancel any existing timer
        if (speedResetTimer != null) {
            handler.removeCallbacks(speedResetTimer);
        }

        // Start a new timer to reset the speed after 5 seconds
        speedResetTimer = new Runnable() {
            @Override
            public void run() {
                // Reset game speed
                updateSystem.setTargetFPS(10);
            }
        };
        handler.postDelayed(speedResetTimer, 5000);
    }

    // Refactored, this is for the poison apple
    public void updatePApple() {
        if (mSnake.bigCheckDinner(pApple.getLocation())) {
            mScore -= 2;
            //if (mScore < 0) {
            //    startGameOverActivity();
            //}
            if(pApple.isSpawned()) {
                pApple.hide();
                mApple.spawn();
            }
            if (yApple.isSpawned()) {
                yApple.hide();
            }
            if (eDrink.isSpawned()) {
                eDrink.hide();
            }

            mSP.play(pEat_ID, 1, 1, 0, 0, 1);
            if(mScore < 0)
                startGameOverActivity();
            else {
                mSnake.shrink(3);
                randomNumber = random.nextInt(4);
            }
        }

        if ((mScore > 0) && (randomNumber == 1) && !pApple.isSpawned()) {
            pApple.spawn();
        }
    }

    //Refactored
    public void resetGameState(){ //Extracted from resetGame
        //Reset game objects
        mApple.spawn();
        mApple.hide();
        mSnake.reset(NUM_BLOCKS_WIDE, mNumBlocksHigh);
        mSnake.hide(); // Hide the snake upon resetting the game
        eDrink.hide();

        //Reset flags
        isFirstPause = true;
        mPaused = true;
        mSnake.setVulnerable(false);
        isVulnerable = false;
        isSnakeDead = true;
        activityFlag = false;

        // Cancel the vulnerability timer
        cancelVulnerabilityTimer();

        //Reset game loop speed if snake dies
        updateSystem.setTargetFPS(10);

    }

    // Refactored
    public void spawnHide() {
        for(Rock rock: rocks) {
            rock.spawn();
            rock.hide();
        }

        for(Trash trash: trashStuff) {
            trash.spawn();
            trash.hide();
        }

        if(yApple.isSpawned()) {
            yApple.hide();
            yApple.spawned = false;
        }

        if(pApple.isSpawned()) {
            pApple.hide();
            pApple.spawned = false;
        }
        if (eDrink.isSpawned()){
            eDrink.hide();
        }
    }

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
            // Refactored
            drawSpawnables();
        }
    }

    //Refactored
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
        eDrink.draw(mCanvas, mPaint);

        // Draw the Rocks
        for(Rock rock: rocks) {
            rock.draw(mCanvas, mPaint);
        }

        // Draw the Trash
        for(Trash trash: trashStuff) {
            trash.draw(mCanvas, mPaint);
        }

    }

    // Refactored
    public void drawSpawnables() {
        // Draw the rock only if the game is not paused
        for(Rock rock: rocks) {
            rock.draw(mCanvas, mPaint);
        }

        // Draw the Trash only if the game is not paused
        for(Trash trash: trashStuff) {
            trash.draw(mCanvas, mPaint);
        }

        // Draw the apple only if the game is not paused
        mApple.draw(mCanvas, mPaint);

        yApple.draw(mCanvas, mPaint);

        pApple.draw(mCanvas, mPaint);

        eDrink.draw(mCanvas, mPaint);
    }

    // Refactored
    public void drawPaused() {
        // Set the size and color of the mPaint for the text
        mPaint.setColor(Color.argb(255, 203, 67, 53));
        mPaint.setTextSize(250);
        // Set the custom font to the Paint object
        mPaint.setTypeface(mCustomFont);

        if (isFirstPause && mPaused) {
            if (textDrawer == null) {
                // Instantiate TextDrawer preparing for Injection
                textDrawer = new TextDrawer(mContext, mCanvas, mPaint);
            }

            //Refactored
            textDrawer.drawTapToPlay(ResourcesCompat.getFont(mContext, R.font.retro));

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
            } else if (mPaused && mPauseButtonRect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                //If the game is paused, resume the game
                mPaused = false;
            } else if (mPauseButtonRect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                // If the pause button is touched, pause the game
                mPaused = true;
                mBackgroundMusic.pause();
            } else if (!mPaused) {
                // If the game is running and not paused, handle snake movement
                mSnake.switchHeading(motionEvent);
            }
            return true;
        }
        return true;
    }

    // Stop the thread
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
