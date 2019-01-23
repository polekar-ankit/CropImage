package com.gipl.imagecroping;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CropView extends ImageView {
    private static final int ZOOM = 123;
    private static final int NONE = 0;
    Paint paint = new Paint();
    int x1, x2, y1, y2;
    Point leftCenter = new Point();
    Point topCenter = new Point();
    Point rightCenter = new Point();
    Point bottomCenter = new Point();
    int bufferWidth;
    private Bitmap bitTopCenter;
    private Bitmap bitRightCenter;
    private Bitmap bitBottomCenter;
    private Bitmap bitLeftCenter;
    private Rect rectangle;
    private int maxX;
    private int maxY;
    private String direction;
    private Paint bitmapPaint;
    private int bufferHeight;
    private int leftX;
    private int leftY;
    private int rightX;
    private int rightY;
    private float mScaleFactor = 1.0f;
    private ScaleGestureDetector mScaleDetector;
    private int mode;

    // Adding parent class constructors
    public CropView(Context context) {
        super(context);
        initCropView();
    }

    public CropView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initCropView();
    }

    public CropView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initCropView();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//
        if (rectangle == null) {
            int centerX = ((int) getX() + (getMeasuredWidth() / 2));
            leftX = centerX - ((getWidth() / 2) / 2);

            int centerY = ((int) getY()) + ((getHeight() / 2));
            leftY = centerY - ((getHeight() / 2) / 2);

            rightX = centerX + (getWidth() / 4);
            rightY = centerY + (getHeight() / 4);

            rectangle = new Rect(leftX, leftY, rightX, rightY);
            topCenter.set((rectangle.right - (rectangle.width() / 2)) - (bitTopCenter.getWidth() / 2),
                    rectangle.top - (bufferHeight / 2));
            rightCenter.set((rectangle.right - (bufferWidth / 2))
                    , (rectangle.top + (rectangle.height() / 2)) - (bufferHeight / 2));
            bottomCenter.set((rectangle.left + (rectangle.width() / 2)) - (bufferWidth / 2),
                    rectangle.bottom - (bufferWidth / 2));
            leftCenter.set(rectangle.left - (bufferWidth / 2),
                    (rectangle.top + (rectangle.height() / 2)) - (bufferHeight / 2));
        }


        canvas.drawBitmap(bitTopCenter, topCenter.x, topCenter.y, bitmapPaint);
        canvas.drawBitmap(bitRightCenter, rightCenter.x, rightCenter.y, bitmapPaint);
        canvas.drawBitmap(bitBottomCenter, bottomCenter.x, bottomCenter.y, bitmapPaint);
        canvas.drawBitmap(bitLeftCenter, leftCenter.x, leftCenter.y, bitmapPaint);

        canvas.drawRect(rectangle, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventaction = event.getAction();
        mScaleDetector.onTouchEvent(event);
        switch (eventaction) {
            case MotionEvent.ACTION_POINTER_DOWN:
                break;

            case MotionEvent.ACTION_DOWN:
                x1 = (int) (event.getX());
                y1 = (int) (event.getY());

                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                x2 = (int) event.getX();
                y2 = (int) event.getY();

                Calculatingpoint();

                break;

            case MotionEvent.ACTION_UP:
                invalidate();
                break;
        }
        return true;
    }


    private void initCropView() {
        mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
        bitmapPaint = new Paint();
        bitmapPaint.setColor(Color.BLACK);
        bitTopCenter = getBitmap(R.drawable.squarsmall);
        bitRightCenter = getBitmap(R.drawable.squarsmall);
        bitBottomCenter = getBitmap(R.drawable.squarsmall);
        bitLeftCenter = getBitmap(R.drawable.squarsmall);
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        Display display = ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        maxX = size.x;
        maxY = size.y;
        bufferWidth = bitBottomCenter.getWidth();
        bufferHeight = bitBottomCenter.getHeight();


    }

    private Bitmap getBitmap(int drawableRes) {
        Drawable drawable = getResources().getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;
    }


    private void Calculatingpoint() {


        new CompositeDisposable().add(Observable.defer(new Callable<ObservableSource<Boolean>>() {
            @Override
            public ObservableSource<Boolean> call() throws Exception {
                if (x2 < maxX && y2 < maxY) {
                    int dx = (x2 - x1);
                    int dy = (y2 - y1);

                    int leftX = rectangle.left;
                    int leftY = rectangle.top;

                    int rightX = rectangle.right;
                    int rightY = rectangle.bottom;

                    if ((x1 > (leftCenter.x - bufferWidth) && x1 < (leftCenter.x + bufferWidth))
                            && (y1 > (leftCenter.y - bufferHeight) && y1 < (leftCenter.y + bufferHeight))) {
                        leftX = leftX + dx;
                    } else if ((x1 > topCenter.x - bufferWidth && x1 < topCenter.x + bufferWidth)
                            && (y1 > (topCenter.y - bufferHeight) && y1 < (topCenter.y + bufferHeight))) {
                        leftY = leftY + dy;
                    } else if ((x1 > (rightCenter.x - bufferWidth) && x1 < (rightCenter.x + bufferWidth))
                            && (y1 > (rightCenter.y - bufferHeight) && y1 < (rightCenter.y + bufferHeight))) {
                        rightX = rightX + dx;
                    } else if ((x1 > (bottomCenter.x - bufferWidth) && x1 < (bottomCenter.x + bufferWidth)
                            && (y1 > (bottomCenter.y - bufferHeight) && y1 < (bottomCenter.y + bufferHeight)))) {
                        rightY = rightY + dy;
                    }


                    rectangle.set(leftX, leftY, rightX, rightY);

                    topCenter.set((rectangle.right - (rectangle.width() / 2)) - (bitTopCenter.getWidth() / 2),
                            rectangle.top - (bufferHeight / 2));
                    rightCenter.set((rectangle.right - (bufferWidth / 2))
                            , (rectangle.top + (rectangle.height() / 2)) - (bufferHeight / 2));
                    bottomCenter.set((rectangle.left + (rectangle.width() / 2)) - (bufferWidth / 2),
                            rectangle.bottom - (bufferWidth / 2));
                    leftCenter.set(rectangle.left - (bufferWidth / 2),
                            (rectangle.top + (rectangle.height() / 2)) - (bufferHeight / 2));
                }
                return Observable.just(true);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        invalidate();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));

    }


//    public byte[] getCroppedImage() {
//        BitmapDrawable drawable = (BitmapDrawable) getDrawable();
//        float x = leftTop.x - center.x + (drawable.getBitmap().getWidth() / 2);
//        float y = leftTop.y - center.y + (drawable.getBitmap().getHeight() / 2);
//        Bitmap cropped = Bitmap.createBitmap(drawable.getBitmap(), (int) x, (int) y, (int) rightBottom.x - (int) leftTop.x, (int) rightBottom.y - (int) leftTop.y);
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        cropped.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        return stream.toByteArray();
//    }


    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();


            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

            Matrix m = getImageMatrix();
            float[] values = new float[9];
            m.getValues(values);

            m.setScale(mScaleFactor, mScaleFactor);

            return true;
        }
    }
}