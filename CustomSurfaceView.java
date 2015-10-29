package com.example.orientationsensor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

public class CustomSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private Camera mCamera;

    private boolean isNeededDraw = false;
    private Paint textPaint = new Paint();
    private String text = "hello";
    private float startX = 0, startY = 0;
    private float currentX = 0, currentY = 0;
    private float prevY = 0, prevX = 0;
    public static Bitmap star;


    private SQLiteDatabase dataBase;
    private LettersPath lettersPath;
    private TreeMap<Integer, Integer> coordinates = new TreeMap<>();

    //private SurfaceThread surfaceThread;

    public CustomSurfaceView(Context context)
    {
        super(context);

        text = text.toUpperCase();
        context.deleteDatabase(LetterDataBase.dbName);
        LetterDataBase myLetDB = new LetterDataBase(getContext());
        dataBase = myLetDB.getReadableDatabase();

        star = BitmapFactory.decodeResource(getResources(), R.drawable.star);

        textPaint.setColor(Color.RED);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setStrokeWidth(10.0f);


        setWillNotDraw(false);
        init();
    }
    protected void onDraw(Canvas canvas)
    {
        if (isNeededDraw) {
            for (int i =  (int) -prevX; i < getWidth() - prevX; ++i)
            {
                Integer y = coordinates.get(i);
                if (y != null)
                {
                    if (i + prevX > -10 && i + prevX < getWidth() && y + prevY > -10 && y + prevY <getHeight())
                    {
                        canvas.drawBitmap(star, (float)(i) + prevX, (float) y + prevY, textPaint);
                    }
                }

            }
            calculateMovePixels();

            for (int i = 0; i < text.length(); ++i) {
                canvas.drawPath(lettersPath.drawMyPath(dataBase, "" + text.charAt(i),
                        prevX + lettersPath.letterWidth * i, prevY), textPaint);
                float [][] points = lettersPath.getPoints(dataBase, "" + text.charAt(i));
                for (int j = 1; j < points[0][0] + 1; ++j){
                    canvas.drawBitmap(star,
                            points[j][0]*lettersPath.letterWidth + prevX + lettersPath.letterWidth * i - star.getWidth()/2 ,
                            points[j][1]*lettersPath.letterHeight + prevY - star.getHeight()/2, null);
                }
            }
            invalidate();
        }
    }

    public void setDraw(boolean toDraw)
    {
        this.isNeededDraw = toDraw;
    }
    public void setStartedPoint(float X, float Y)
    {
        float maxY = 10.0f;
        this.startX = X;
        this.startY = Y;

        lettersPath = new LettersPath(getWidth()/5.0f, getHeight()/3.0f);

        float maxYValue = (maxY - startY) * getWidth()/4.0f;
        float minYValue = (-startY - maxY) * getWidth()/4.0f;
        float maxXValue = (maxY - startX) * getHeight()/5.0f;
        float minXValue = (-startX - maxY) * getHeight()/5.0f;

        for (int i = 0; i < 50; ++i)
        {
            int x = (int) (Math.random() * (maxYValue - minYValue) + minYValue);
            int y = (int) (Math.random() * (maxXValue - minXValue) + minXValue);
            coordinates.put(x, y);
        }
    }
    public void setCurrentPoint(float X, float Y) {
        this.currentX = X;
        this.currentY = Y;
    }
    private void init()
    {
        mHolder = getHolder();
        mHolder.addCallback(this);




        mCamera = android.hardware.Camera.open();
    }
    private void calculateMovePixels()
    {
        int xAccuracy = 30;
        int yAccuracy = 50;

        float x = (startX - currentX)*(getHeight()/4.0f);
        float y = (startY - currentY)*(getWidth()/5.0f);



        if (x - xAccuracy > prevY)
        {
            prevY += 15;
        } else if (x + xAccuracy < prevY)
        {
            prevY -= 15;
        }


        if (y - yAccuracy > prevX)
        {
            prevX += 20;
        } else if (y + yAccuracy  < prevX)
        {
            prevX -= 20;
        }
    }
    public void surfaceCreated(SurfaceHolder holder)
    {
        try{
            if (mCamera == null)
            {
                mCamera = Camera.open();
            }
            if (mHolder == null)
            {
                mHolder = getHolder();
                mHolder.addCallback(this);
            }
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        }catch (IOException  e)
        {

        }
//        surfaceThread.setRunnable(true);
//        surfaceThread.start();
    }
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h )
    {
        if (mHolder.getSurface() == null)
        {
            return;
        }


        try{
            mCamera.stopPreview();
        }catch (Exception e) {

        }


        try {
            mCamera.setPreviewDisplay(holder);
            Camera.Parameters parameters = mCamera.getParameters();
            List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
            int width = getWidth();
            int height = getHeight();
            Camera.Size optimalSize = getOptimalPreviewSize(sizes, width, height);
            parameters.setPreviewSize(optimalSize.width, optimalSize.height);
            mCamera.setParameters(parameters);
            mCamera.startPreview();

        }catch (IOException e)
        {
            e.printStackTrace();
        }


    }
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        if (mHolder.getSurface() == null)
        {
            return;
        }

        try{
            mCamera.stopPreview();
        }catch (Exception e) {

        }

        if (mCamera != null)
        {
            mCamera.release();
            mCamera = null;
        }

        /*boolean retry = true;
        surfaceThread.setRunnable(false);

        while(retry)
        {
            try {
               surfaceThread.join();
                retry = false;
            } catch (InterruptedException e) {

            }
        }
        surfaceThread = null;*/
    }
    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio=(double)h / w;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }
}