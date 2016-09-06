package com.celery.celeryratingbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by ganwenjun on 16/2/29.
 */
public class CeleryRatingBar extends View {

    public static final String  RATING_BUNDLE_KEY = "rating";
    public static final String  PARCELABLE_BUNDLE_KEY = "parcelable";

    public interface OnRatingChangeListener {
        void onRatingChange(float before, float after, boolean isUser);
    }

    public static class ShowType {
        public static final int AUTO = 0;

        public static final int MANUAL = 1;
    }

    private int numStars;

    private float stepSize;

    private float rating;

    private Bitmap starDrawable;

    private Bitmap progressColor;

    private Bitmap showStarBtmap;

    private Bitmap showProgressBitmap;

    private Bitmap showPartProgressStar;

    private float stepDimen;

    private int showType;

    private boolean isChangeValueByUser;

    private OnRatingChangeListener onRatingChangeListener;

    public CeleryRatingBar(Context context) {
        super(context);
        init(context, null);
    }

    public CeleryRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CeleryRatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        if(onRatingChangeListener != null) {
            onRatingChangeListener.onRatingChange(this.rating, rating, false);
        }
        this.rating = rating;
    }

    /**
     * 获得星星的总数
     * @return
     */
    public int getNumStars() {
        return numStars;
    }

    /**
     * 设置星星的总素
     * @param numStars 星星的总数
     */
    public void setNumStars(int numStars) {
        this.numStars = numStars;
    }

    /**
     * 获得滑动的单位大小，
     * @return
     */
    public float getStepSize() {
        return stepSize;
    }

    /**
     * 设置滑动的单位大小
     * @param stepSize
     */
    public void setStepSize(float stepSize) {
        if(stepSize < 0.1) {
            this.stepSize = 0.1f;
        } else {
            this.stepSize = stepSize;
        }
    }

    /**
     * 设置当数值被用户改变的监听
     * @param onRatingChangeListener
     */
    public void setOnRatingChangeListener(OnRatingChangeListener onRatingChangeListener) {
        this.onRatingChangeListener = onRatingChangeListener;
    }

    @Override
    protected Parcelable onSaveInstanceState() {

        Parcelable parcelable = super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putFloat(RATING_BUNDLE_KEY, rating);
        bundle.putParcelable(PARCELABLE_BUNDLE_KEY, parcelable);

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {

        if(state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            rating = bundle.getFloat(RATING_BUNDLE_KEY);
            Parcelable parcelable  = bundle.getParcelable(PARCELABLE_BUNDLE_KEY);
            super.onRestoreInstanceState(parcelable);
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = 0;
        int height = 0;

        width = (int) (getPaddingLeft() + getPaddingRight() + (starDrawable.getWidth() * numStars) + (stepDimen * (numStars - 1)));
        height = getPaddingBottom() + getPaddingTop() + starDrawable.getHeight();

        setMeasuredDimension(makeMeasure(width, widthMeasureSpec), makeMeasure(height, heightMeasureSpec));

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int leftPadding = getPaddingLeft();
        int rightPadding = getPaddingRight();
        int toPadding = getPaddingTop();
        int bottomPadding = getPaddingBottom();

        float starBitmapHeight = starDrawable.getHeight();
        float showHeight = height - toPadding - bottomPadding;
        float scale = showHeight / starBitmapHeight;

        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        if(showStarBtmap == null) {
            showStarBtmap = Bitmap.createBitmap(starDrawable, 0, 0, starDrawable.getWidth(), starDrawable.getHeight(), matrix, true);
        }

        if(showProgressBitmap == null) {
            showProgressBitmap = Bitmap.createBitmap(progressColor, 0, 0, progressColor.getWidth(), progressColor.getHeight(), matrix, true);
        }

        if(showPartProgressStar == null) {
            showPartProgressStar = null;
        } else {
            showPartProgressStar.recycle();
            showPartProgressStar = null;
        }

        if(showStarBtmap != starDrawable) {
            starDrawable.recycle();
        }
        if(showProgressBitmap != progressColor) {
            progressColor.recycle();
        }

        if(showType == ShowType.AUTO) {
            int showWidth = width - getPaddingRight() - getPaddingLeft();
            stepDimen = (showWidth - (numStars * showStarBtmap.getWidth())) / (numStars - 1);
        }

        float numProgressStar = rating;
        int numProgressStarInt = (int) Math.floor(numProgressStar);
        float numprogressStarFloat = numProgressStar - numProgressStarInt;

        if(numprogressStarFloat > 0 && numprogressStarFloat < 1) {
            showPartProgressStar = getProgressBitmap(numprogressStarFloat, showProgressBitmap, showStarBtmap);
        }

        for(int i = 0; i < numStars; i++) {
            int x = (int) (leftPadding  + (i * (stepDimen + showStarBtmap.getWidth())));
            int y = toPadding;

            if(i < numProgressStarInt) {
                canvas.drawBitmap(showProgressBitmap, x, y, null);
            } else if(showPartProgressStar != null && i == numProgressStarInt) {
                canvas.drawBitmap(showPartProgressStar, x, y, null);
            } else {
                canvas.drawBitmap(showStarBtmap, x, y, null);
            }


        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(isChangeValueByUser) {

            int width = getWidth();
            int height = getHeight();
            int leftPadding = getPaddingLeft();
            int rightPadding = getPaddingRight();
            int toPadding = getPaddingTop();
            int bottomPadding = getPaddingBottom();

            int showWidth = width - leftPadding - rightPadding;

            float x = event.getX() - leftPadding;
            float y = event.getY() - toPadding;

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    rating = getXRating(x, showWidth);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    rating = getXRating(x, showWidth);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }

            return true;
        } else {
            return false;
        }
    }

    private float getXRating(float x, int width) {
        float rating = (x / width) * numStars;
        int numStep = Math.round(rating / stepSize);
        rating = numStep * stepSize;

        if(onRatingChangeListener != null) {
            onRatingChangeListener.onRatingChange(this.rating, rating, true);
        }
        return rating;
    }

    private int makeMeasure(int size, int measureSpec) {

        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if(specMode == MeasureSpec.AT_MOST) {
            result = size;
        } else if(specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if(specMode == MeasureSpec.UNSPECIFIED) {
            result = size;
        }

        return result;

    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CeleryRatingBar);

        numStars = typedArray.getInt(R.styleable.CeleryRatingBar_numStars, 5);
        stepSize = typedArray.getFloat(R.styleable.CeleryRatingBar_stepSize, 0.5f);
        if(stepSize < 0.1) {
            stepSize = 0.1f;
        }
        rating = typedArray.getFloat(R.styleable.CeleryRatingBar_rating, 0);
        Drawable tempDrawable = typedArray.getDrawable(R.styleable.CeleryRatingBar_star);
        Drawable tempProgressDrawable = typedArray.getDrawable(R.styleable.CeleryRatingBar_progress);
        stepDimen = typedArray.getDimension(R.styleable.CeleryRatingBar_stepDimen, 0);
        showType = typedArray.getInteger(R.styleable.CeleryRatingBar_showType, ShowType.AUTO);

        if(tempDrawable == null) {
            starDrawable = BitmapFactory.decodeResource(context.getResources(), R.mipmap.favourite_nor);
        } else {
            starDrawable = drawableToBitamp(tempDrawable);
        }
        if(tempProgressDrawable == null) {
            progressColor = BitmapFactory.decodeResource(context.getResources(), R.mipmap.favourite_presse);
        } else {
            progressColor = drawableToBitamp(tempProgressDrawable);
        }
        isChangeValueByUser = typedArray.getBoolean(R.styleable.CeleryRatingBar_isChangeValue, false);
    }

    private Bitmap drawableToBitamp(Drawable drawable)
    {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        System.out.println("Drawable转Bitmap");
        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w,h,config);
        //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);

        return bitmap;
    }

    private Bitmap getProgressBitmap(float per, Bitmap source, Bitmap star) {
        Matrix matrix = new Matrix();
        matrix.setScale(1f, 1f);
        int width = (int) (source.getWidth() * per);
        Bitmap tempBitmap = Bitmap.createBitmap(source.getWidth(), source.getHeight(),
                Bitmap.Config.ARGB_8888);
        Bitmap tempShowProgressBitmap = Bitmap.createBitmap(source, 0, 0, width, source.getHeight(), matrix, true);
        Canvas canvas = new Canvas(tempBitmap);
        canvas.drawBitmap(tempShowProgressBitmap, 0, 0, null);
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        canvas.drawBitmap(star, 0, 0, paint);

        return tempBitmap;
    }
}
