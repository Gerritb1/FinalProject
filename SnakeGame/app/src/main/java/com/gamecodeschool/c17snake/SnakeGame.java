package com.gamecodeschool.c17snake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.Log;
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
    private int yEat_ID = -1;
    private int pEat_ID = -1;
    private int mCrashID = -1;
    private int mCrashIDTrash = -1;
    private int mCrashIDRock = -1;

    // The size in segments of the playable area
    private final int NUM_BLOCKS_WIDE = 40;
    private int mNumBlocksHigh;

    // How many points does the player have
    private int mScore;

    //Pause button rendering objects
    private Rect mPauseButtonRect;
    private Rect mTriggerButtonRect;
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

    // And a Poisoned Apple
    private PoisonApple pApple;

    // And four rock objects
    private Rock rock1;
    private Rock rock2;
    private Rock rock3;
    private Rock rock4;
    private ArrayList<Rock> rocks;

    private Trash trash1;
    private Trash trash2;
    private Trash trash3;
    private Trash trash4;
    private ArrayList<Trash> trashStuff;

    private Bitmap mBackgroundBitmap;
    private DrawPauseButton drawPauseButton;
    private UpdateSystem updateSystem;
    private TextDrawer DrawNames;
    private Context mContext;
    private TextDrawer textDrawer;

    private Random random;

    private int randomNumber = 0;

    // MediaPlayer for background music
    private MediaPlayer mBackgroundMusic;

    // This tracks which piece of trash we're on
    private int trashPiece = 0;
    private int trashChance = 2;

    // For vulnerability of the snake
    private boolean isVulnerable = false;

    private Handler handler;
    private Runnable vulnerabilityTimer;

    private Bomb mBomb;

    private TriggerButton drawTriggerButton;
    private TriggerButton mTriggerButton;
    private boolean isButtonPressed = false;
    private MotionEvent motionEvent;


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

        //Create the trigger button
        createTriggerButton();

        // Refactored
        initialize(context);
    }

    // Refactored
    public void initialize(Context context) {

        //Initialize the buttons
        drawPauseButton = DrawPauseButton.getDrawPauseButton(context, this);
        drawTriggerButton = TriggerButton.getDrawTriggerButton(context, this);

        //Initialize the update system
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

    private void createTriggerButton() {
        mTriggerButton = new TriggerButton(getContext(), this);
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

        pApple = PoisonApple.getPoisonApple(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);

        mSnake = Snake.getSnake(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);
        // Call the constructor for the Bomb class in the callConstructors method
        mBomb = Bomb.getBomb(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize);

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
            if(!mPaused) {
                mBackgroundMusic.start();
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
                mBomb.spawn();
            }
            for(Rock rock: rocks) {
                rock.spawn();
            }

            for(Trash trash: trashStuff) {
                trash.spawn();
                trash.hide();
            }
        }
        isFirstPause = mPaused;
    }

    // Update the newGame() method to set isFirstPause to true
    @Override
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

            // Refactored
            updateDeath();

            updateBomb();
        }
    }

    public void updateBomb() {
        // Spawn the bomb if it hasn't been spawned yet
        if (!mBomb.isSpawned()) {
            mBomb.spawn();
        }

        // Check for collision with the snake
        mBomb.checkSnakeCollision(mSnake);

        if (mTriggerButton.isPressed()) {
            MotionEvent motionEvent = getMotionEvent(); // Ensure this method returns the MotionEvent correctly

            // Calculate the shooting direction based on the MotionEvent and button state
            mBomb.shootBomb(motionEvent, mTriggerButton.isPressed());
        }

        // Draw the bomb on the canvas
        mBomb.draw(mCanvas, mPaint);
    }

    public MotionEvent getMotionEvent() {
        return motionEvent;
    }


    // Method to set the button pressed flag
    public void setButtonPressed(boolean isPressed) {
        isButtonPressed = isPressed;
    }
    // Refactored
    public void updateDeath() {
        boolean snakeHitRock = false;
        boolean snakeHitTrash = false;
        for(Rock rock: rocks) {
            if (mSnake.hitRock(rock.getLocation())) {
                if(!isVulnerable) {
                    mSP.play(mCrashIDRock, 1, 1, 0, 0, 1);
                }
                snakeHitRock = true;
                if(!isVulnerable) {
                    resetGame();
                }
            }
        }
        for(Trash trash: trashStuff) {
            if (mSnake.hitRock(trash.getLocation())) {//hitRock has same functionality as a "hitSnake" would ******
                if(!isVulnerable) {
                    mSP.play(mCrashIDTrash, 1, 1, 0, 0, 1);
                }
                snakeHitTrash = true;
                if(!isVulnerable) {
                    resetGame();
                }
            }

        }
        if (mSnake.detectDeath() && !snakeHitTrash && !snakeHitRock) {
            mSP.play(mCrashID, 1, 1, 0, 0, 1);
            // Reset the score and the game if snake dies
            resetGame();
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

            for(int i = 0; i < 4; i++ ) {
                trashStuff.get(trashPiece).chanceToSpawn(mScore, trashChance);
                trashPiece = (trashPiece+1)%3;
                trashChance+= 2;
            }
            trashChance = 2;

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

            for(int i = 0; i < 4; i++ ) {
                trashStuff.get(trashPiece).chanceToSpawn(mScore, trashChance);
                trashPiece = (trashPiece+1)%3;
                trashChance+= 2;
            }
            trashChance = 2;

            mSP.play(yEat_ID, 1, 1, 0, 0, 1);
            randomNumber = random.nextInt(3);

            // to grow the snake body segment by 3, since 2+1=3
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

                mSP.play(pEat_ID, 1, 1, 0, 0, 1);

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

            if (mBackgroundMusic.isPlaying()) {
                mBackgroundMusic.pause();
                mBackgroundMusic.seekTo(0); // Rewind the background music to the beginning
            }

            // Refactored
            spawnHide();

            mApple.spawn();
            mApple.hide(); // Hide the apple upon resetting the game
            mSnake.reset(NUM_BLOCKS_WIDE, mNumBlocksHigh);
            mSnake.hide(); // Hide the snake upon resetting the game
            isFirstPause = true; // Set isFirstPause to true upon resetting the game
            mPaused = true; // Set mPaused to true upon resetting the game

            mSnake.setVulnerable(false);
            isVulnerable = false;

            // Cancel the vulnerability timer
            cancelVulnerabilityTimer();

            // Reset the vulnerability state

        }
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
        if(mBomb.isSpawned()) {
            mBomb.hide();
            mBomb.spawned = false;
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
            drawTriggerButton.drawButton(mCanvas, mPaint);
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
                textDrawer = new TextDrawer(getContext(), mCanvas, mPaint, this);
                textDrawer.setDrawPauseButton(DrawPauseButton.getDrawPauseButton(getContext(), this));
                textDrawer.drawNames();
            }

            // Check if NameDrawer instance is not null before calling drawNames
            if (textDrawer != null) {
                textDrawer.drawNames();
            }
        }
    }


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
        pApple.draw(mCanvas, mPaint);
        mBomb.draw(mCanvas, mPaint);

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

        mBomb.draw(mCanvas, mPaint);
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
            if(textDrawer == null) {
                // Instantiate TextDrawer preparing for Injection
                textDrawer = new TextDrawer(mContext, mCanvas, mPaint, this);
                textDrawer.setDrawPauseButton(drawPauseButton); //Injecting
            }

            //Refactored
            textDrawer.drawTapToPlay();
            textDrawer.drawNames();
        }
    }

    private boolean isTriggerButtonPressed = false;

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if ((motionEvent.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
            Log.d("TriggerButton", "Touch Event - X: " + motionEvent.getX() + ", Y: " + motionEvent.getY());

            if (isFirstPause) {
                // If the game beginning, start the game
                mPaused = false;
                newGame();
            } else if (mPaused && mPauseButtonRect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                // If the game is paused, resume the game
                mPaused = false;
            } else if (mPauseButtonRect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                // If the pause button is touched, pause the game
                mPaused = true;
                mBackgroundMusic.pause();
            } else if (!mPaused) {
                // If the game is running and not paused, handle snake movement
                mSnake.switchHeading(motionEvent);
            } else if (mTriggerButtonRect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                // Trigger the bomb shooting action
                mBomb.shootBomb(motionEvent, isTriggerButtonPressed);
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
