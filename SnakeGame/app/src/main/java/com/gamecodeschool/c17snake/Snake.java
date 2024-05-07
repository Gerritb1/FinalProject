package com.gamecodeschool.c17snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;

import java.util.HashMap;
import java.util.Map;

class Snake extends GameObject implements Movable, Collidable {

    // Where is the centre of the screen
    // horizontally in pixels?
    private int halfWayPoint;

    // For tracking movement Heading
    enum Heading {
        UP, RIGHT, DOWN, LEFT
    }

    // Start by heading to the right
    private Heading heading = Heading.RIGHT;

    // A bitmap for each direction the head can face
    private Bitmap mBitmapHeadRight;
    private Bitmap mBitmapHeadLeft;
    private Bitmap mBitmapHeadUp;
    private Bitmap mBitmapHeadDown;

    // A bitmap for the body
    private Bitmap mBitmapBody;

    // Maintain a single global reference to the snake
    private static Snake snake;

    Map <Heading, HeadRotate> headMap
            = new HashMap<Heading, HeadRotate>();
    Map<Heading, HeadRotate> moveMap
            = new HashMap<Heading, HeadRotate>();
    Map<Heading, HeadRotate> ifMap
            = new HashMap<Heading, HeadRotate>();
    Map<Heading, HeadRotate> elseMap
            = new HashMap<Heading, HeadRotate>();

    private boolean isVulnerable = false;

    private Snake(Context context, Point mr, int ss) {

        super(context, mr, ss);

        // Refactored
        headMovement(context, ss);

        // Refactored, Overload
        headMovement(ss);

        populateHeadMap();

        // Create and scale the body
        mBitmapBody = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.body);

        mBitmapBody = Bitmap
                .createScaledBitmap(mBitmapBody,
                        ss, ss, false);

        // The halfway point across the screen in pixels
        // Used to detect which side of screen was pressed
        halfWayPoint = mr.x * ss / 2;
    }

    public void setVulnerable(boolean vulnerable) {
        isVulnerable = vulnerable;
    }

    public void populateElseMap(){
        elseMap.put(Heading.UP, new HeadRotate() {
            @Override
            public void rotate(Canvas canvas, Paint paint) {
                heading = Heading.LEFT;
            }
        });
        elseMap.put(Heading.LEFT, new HeadRotate() {
            @Override
            public void rotate(Canvas canvas, Paint paint) {
                heading = Heading.DOWN;
            }
        });
        elseMap.put(Heading.RIGHT, new HeadRotate() {
            @Override
            public void rotate(Canvas canvas, Paint paint) {
                heading = Heading.UP;
            }
        });

        elseMap.put(Heading.DOWN, new HeadRotate() {
            @Override
            public void rotate(Canvas canvas, Paint paint) {
                heading = Heading.RIGHT;
            }
        });

    }
    public void populateIfMap(){
        ifMap.put(Heading.UP, new HeadRotate() {
            @Override
            public void rotate(Canvas canvas, Paint paint) {
                heading = Heading.RIGHT;
            }
        });

        ifMap.put(Heading.RIGHT, new HeadRotate() {
            @Override
            public void rotate(Canvas canvas, Paint paint) {
                heading = Heading.DOWN;
            }
        });

        ifMap.put(Heading.LEFT, new HeadRotate() {
            @Override
            public void rotate(Canvas canvas, Paint paint) {
                heading = Heading.UP;
            }
        });

        ifMap.put(Heading.DOWN, new HeadRotate() {
            @Override
            public void rotate(Canvas canvas, Paint paint) {
                heading = Heading.LEFT;
            }
        });
    }
    public void populateMoveMap(Point p){
        moveMap.put(Heading.UP, new HeadRotate() {
            @Override
            public void rotate(Canvas canvas, Paint paint) {
                p.y--;
            }
        });

        moveMap.put(Heading.RIGHT, new HeadRotate() {
            @Override
            public void rotate(Canvas canvas, Paint paint) {
                p.x++;
            }
        });

        moveMap.put(Heading.LEFT, new HeadRotate() {
            @Override
            public void rotate(Canvas canvas, Paint paint) {
                p.x--;
            }
        });

        moveMap.put(Heading.DOWN, new HeadRotate() {
            @Override
            public void rotate(Canvas canvas, Paint paint) {
                p.y++;
            }
        });
    }
    //Populates the headMap
    public void populateHeadMap(){
        headMap.put(Heading.UP, new HeadRotate() {
            @Override
            public void rotate(Canvas canvas, Paint paint) {
                canvas.drawBitmap(mBitmapHeadUp,
                        segmentLocations.get(0).x
                                * mSegmentSize,
                        segmentLocations.get(0).y
                                * mSegmentSize, paint);
            }
        });

        headMap.put(Heading.RIGHT, new HeadRotate() {
            @Override
            public void rotate(Canvas canvas, Paint paint) {
                canvas.drawBitmap(mBitmapHeadRight,
                        segmentLocations.get(0).x
                                * mSegmentSize,
                        segmentLocations.get(0).y
                                * mSegmentSize, paint);
            }
        });

        headMap.put(Heading.LEFT, new HeadRotate() {
            @Override
            public void rotate(Canvas canvas, Paint paint) {
                canvas.drawBitmap(mBitmapHeadLeft,
                        segmentLocations.get(0).x
                                * mSegmentSize,
                        segmentLocations.get(0).y
                                * mSegmentSize, paint);
            }
        });

        headMap.put(Heading.DOWN, new HeadRotate() {
            @Override
            public void rotate(Canvas canvas, Paint paint) {
                canvas.drawBitmap(mBitmapHeadDown,
                        segmentLocations.get(0).x
                                * mSegmentSize,
                        segmentLocations.get(0).y
                                * mSegmentSize, paint);
            }
        });


    }

    // Provide access to the snake, creating it if necessary
    public static Snake getSnake(Context context, Point mr, int ss) {
        if(snake == null)
            snake = new Snake(context, mr, ss);
        return snake;
    }

    // Method to hide the snake
    @Override
    public void hide() {
        // Set the snake's head position outside the visible screen
        segmentLocations.get(0).set(-1, -1);

        // Set each segment's position outside the visible screen
        for (int i = 1; i < segmentLocations.size(); i++) {
            segmentLocations.get(i).x = -1;
            segmentLocations.get(i).y = -1;
        }
    }


    // Refactored
    @Override
    public void headMovement(Context context, int ss) {
        // Create and scale the bitmaps
        mBitmapHeadRight = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.head);

        // Create 3 more versions of the head for different headings
        mBitmapHeadLeft = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.head);

        mBitmapHeadUp = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.head);

        mBitmapHeadDown = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.head);

    }

    // Refactored
    // Overload
    protected void headMovement(int ss) {
        // Modify the bitmaps to face the snake head
        // in the correct direction
        mBitmapHeadRight = Bitmap
                .createScaledBitmap(mBitmapHeadRight,
                        ss, ss, false);

        // A matrix for scaling
        Matrix matrix = new Matrix();
        matrix.preScale(-1, 1);

        mBitmapHeadLeft = Bitmap
                .createBitmap(mBitmapHeadRight,
                        0, 0, ss, ss, matrix, true);

        // A matrix for rotating
        matrix.preRotate(-90);
        mBitmapHeadUp = Bitmap
                .createBitmap(mBitmapHeadRight,
                        0, 0, ss, ss, matrix, true);

        // Matrix operations are cumulative
        // so rotate by 180 to face down
        matrix.preRotate(180);
        mBitmapHeadDown = Bitmap
                .createBitmap(mBitmapHeadRight,
                        0, 0, ss, ss, matrix, true);
    }

    // Get the snake ready for a new game
    protected void reset(int w, int h) {

        // Reset the heading
        heading = Heading.RIGHT;

        // Delete the old contents of the ArrayList
        segmentLocations.clear();

        // Start with a single snake segment
        segmentLocations.add(new Point(w / 2, h / 2));
    }

    @Override
    public void move() {
        //Refactored
        movingLoop();
        Canvas c =new Canvas();
        Paint paint = new Paint();

        // Move the head in the appropriate heading
        // Get the existing head position
        Point p = segmentLocations.get(0);
        populateMoveMap(p);

        // Move it appropriately
        moveMap.get(heading).rotate(c, paint);
    }

    // Refactored
    @Override
    public void movingLoop() {
        // Move the body
        // Start at the back and move it
        // to the position of the segment in front of it
        for (int i = segmentLocations.size() - 1; i > 0; i--) {

            // Make it the same value as the next segment
            // going forwards towards the head
            segmentLocations.get(i).x = segmentLocations.get(i - 1).x;
            segmentLocations.get(i).y = segmentLocations.get(i - 1).y;
        }
    }

    @Override
    public boolean detectDeath() {
        // Has the snake died?
        boolean dead = false;

        // Hit any of the screen edges
        if (segmentLocations.get(0).x == -1 ||
                segmentLocations.get(0).x > mMoveRange.x ||
                segmentLocations.get(0).y == -1 ||
                segmentLocations.get(0).y > mMoveRange.y) {

            dead = true;
        }

        // Eaten itself?
        for (int i = segmentLocations.size() - 1; i > 0; i--) {
            // Have any of the sections collided with the head
            if (segmentLocations.get(0).x == segmentLocations.get(i).x &&
                    segmentLocations.get(0).y == segmentLocations.get(i).y) {

                dead = true;
            }
        }
        return dead;
    }

    @Override
    public boolean checkDinner(Point l) {
        //if (snakeXs[0] == l.x && snakeYs[0] == l.y) {
        if (segmentLocations.get(0).x == l.x &&
                segmentLocations.get(0).y == l.y) {

            // Add a new Point to the list
            // located off-screen.
            // This is OK because on the next call to
            // move it will take the position of
            // the segment in front of it
            segmentLocations.add(new Point(-10, -10));
            return true;
        }
        return false;
    }

    public boolean bigCheckDinner(Point l) {
        // Calculate the boundaries of the yellow apple
        int appleSize = mSegmentSize * 2; // Size of the yellow apple

        // Calculate the boundaries of the apple
        int appleLeft = l.x * mSegmentSize;
        int appleRight = appleLeft + appleSize;
        int appleTop = l.y * mSegmentSize;
        int appleBottom = appleTop + appleSize;

        // Calculate the boundaries of the snake's head
        int headLeft = segmentLocations.get(0).x * mSegmentSize;
        int headRight = headLeft + mSegmentSize;
        int headTop = segmentLocations.get(0).y * mSegmentSize;
        int headBottom = headTop + mSegmentSize;

        // Check if the head of the snake intersects with the apple
        if (headLeft < appleRight && headRight
                > appleLeft && headTop
                < appleBottom && headBottom > appleTop) {
            // Add a new Point to the list located off-screen
            // This is OK because on the next call to move, it will take the position of the segment in front of it
            segmentLocations.add(new Point(-10, -10));
            return true; // Collision detected
        }

        return false; // No collision
    }



    // Method to grow the snake body as many segment as you want
    public void grow(int segments) {
        // Get the last segment of the snake's body
        Point lastSegment = segmentLocations.get(segmentLocations.size() - 1);

        // Add new body segments to the snake's body
        for (int i = 0; i < segments; i++) {
            segmentLocations.add(new Point(lastSegment.x, lastSegment.y));
        }
    }

    public void shrink(int segments) {
        // Remove segments from the snake's body starting from the tail
        for (int i = 0; i < segments; i++) {
            Point lastSegment = segmentLocations.get(segmentLocations.size() - 1);
            segmentLocations.remove(lastSegment);
        }
    }

    public boolean hitRock(Point l) {

        boolean dead = false;

        // Calculate the boundaries of the rock
        int rockSize = mSegmentSize * 2; // Size of the rock

        // Calculate the boundaries of the rock
        int rockLeft = l.x * mSegmentSize;
        int rockRight = rockLeft + rockSize;
        int rockTop = l.y * mSegmentSize;
        int rockBottom = rockTop + rockSize;

        // Calculate the boundaries of the snake's head
        int headLeft = segmentLocations.get(0).x * mSegmentSize;
        int headRight = headLeft + mSegmentSize;
        int headTop = segmentLocations.get(0).y * mSegmentSize;
        int headBottom = headTop + mSegmentSize;

        // Check if the head of the snake intersects with the rock
        if (headLeft < rockRight && headRight
                > rockLeft && headTop
                < rockBottom && headBottom > rockTop) {
            dead = true; // Collision detected, snake should be dead
        }

        return dead;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

        // Don't run this code if ArrayList has nothing in it
        if (!segmentLocations.isEmpty()) {
            // Save the original alpha value
            int originalAlpha = paint.getAlpha();

            // If the snake is vulnerable, set the alpha value to make it semi-transparent
            if (isVulnerable) {
                paint.setAlpha(128);
            }

            // Draw the head
            headMap.get(heading).rotate(canvas, paint);

            // Refactored
            DrawSnakeBody(canvas, paint);

            // Restore the original alpha value if it is not vulnerable
            paint.setAlpha(originalAlpha);
        }
    }

    //Refactored
    protected void DrawSnakeBody(Canvas canvas, Paint paint) {
        // Draw the snake body one block at a time
        for (int i = 1; i < segmentLocations.size(); i++) {
            canvas.drawBitmap(mBitmapBody,
                    segmentLocations.get(i).x
                            * mSegmentSize,
                    segmentLocations.get(i).y
                            * mSegmentSize, paint);
        }
    }

    // Handle changing direction
    @Override
    public void switchHeading(MotionEvent motionEvent) {
        Canvas c = new Canvas();
        Paint p = new Paint();
        populateIfMap();
        populateElseMap();
        // Is the tap on the right hand side?
        if (motionEvent.getX() >= halfWayPoint) {
            ifMap.get(heading).rotate(c, p);

        } else {
            // Rotate left
            elseMap.get(heading).rotate(c, p);
        }
    }

    private Point mHeading; // Add this field to keep track of the snake's heading
    
    public void setHeading(Point newHeading) {
        this.mHeading = newHeading;
    }
    
    // This method stores the position of the snake head (for the bomb feature)
    public Point updatePosition() {
        // Get the current position of the snake's head
        Point oldPosition = new Point(segmentLocations.get(0).x, segmentLocations.get(0).y);

        // Update the snake's position based on the current heading
        if (heading == Heading.UP) {
            oldPosition.y -= 1;
        } else if (heading == Heading.DOWN) {
            oldPosition.y += 1;
        } else if (heading == Heading.LEFT) {
            oldPosition.x -= 1;
        } else if (heading == Heading.RIGHT) {
            oldPosition.x += 1;
        }

        // After updating the position, update the heading
        Point newHeading = new Point(oldPosition.x - segmentLocations.get(0).x, oldPosition.y - segmentLocations.get(0).y);
        setHeading(newHeading);

        // Update the location of the head in segmentLocations
        segmentLocations.set(0, oldPosition);
        return oldPosition;
    }

}
