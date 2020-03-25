package cn.roy.demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.List;

import cn.roy.demo.R;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/3/20 16:27
 * @Version: v1.0
 */
public class GroupHeadView extends View {

    public GroupHeadView(Context context) {
        super(context);

        init(context);
    }

    public GroupHeadView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GroupHeadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    private int width;
    private int composeMode;
    private int ringWidth;
    private int ringColor;

    public GroupHeadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GroupHeadView);
        composeMode = typedArray.getInt(R.styleable.GroupHeadView_model, 0);
        ringWidth = typedArray.getDimensionPixelSize(R.styleable.GroupHeadView_ringWidth, 0);
        ringColor = typedArray.getColor(R.styleable.GroupHeadView_ringColor, Color.TRANSPARENT);
        typedArray.recycle();// 回收

        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        computePosition();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    private int[] colors = {R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark,
            R.color.colorBtnRegister};

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(Color.RED);
        canvas.drawText("你好", 50, 50, mPaint);
        for (int i = 0; i < composeMode; i++) {
            Matrix matrix = new Matrix();
            matrix.setTranslate(centerX - imageWidth / 2, centerY - imageWidth / 2);
            int k = composeMode % 2;
            float rotate = k == 1 ? -(90 + degree / 2) : -(90 + degree);
            if (composeMode == 2) {
                rotate = -225;
            }
            matrix.postRotate(rotate, centerX, centerY);
            if (composeMode == 1) {
                Bitmap bitmap = createCycleBitmap(imageWidth / 2);
                canvas.drawBitmap(bitmap, 0, 0, mPaint);
            } else if (composeMode == 2 && i == 0) {
                Bitmap bitmap = createCycleBitmap(imageWidth / 2);
                canvas.drawBitmap(bitmap, centerX - imageWidth / 2, centerY - imageWidth / 2, mPaint);
            } else {
                Bitmap bitmap = crateBorderBitmap(imageWidth / 2, ringWidth, (int) degree);
                canvas.drawBitmap(bitmap, matrix, mPaint);
            }
            mPaint.setStrokeWidth(2);
            canvas.drawLine(width / 2, rotateY, centerX, centerY, mPaint);
            canvas.rotate(degree, width / 2, rotateY);
        }
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    private Paint mPaint;

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    private List<String> headPathList;
    private Bitmap bitmap;

    private int centerX, centerY;
    private float degree;

    public void setHeadList(String... headPath) {
        headPathList.clear();
        headPathList.addAll(headPathList);
        if (headPathList.isEmpty()) {
            composeMode = 0;
        } else {
            composeMode = headPathList.size() > 9 ? 9 : headPathList.size();
        }
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    private int imageWidth;
    private int rotateY;

    private void computePosition() {
        // 构建成的单个三角形的角度
        degree = (float) (360.0 / composeMode);

        double range = Math.toRadians(degree);
        double range2 = Math.toRadians(degree / 2);
        int radius;
        switch (composeMode) {
            case 1:
                radius = width / 2;
                break;
            case 2:
                radius = (int) (width / 2 / (1 + Math.sin(Math.toRadians(45))));
                break;
            case 5:
                radius = (int) (width / 2 / (1 + Math.sin(range) / Math.sin(range2)));
                break;
            case 6:
                radius = (int) (width / 2 / (1 + 1 / Math.sin(range2)));
                break;
            case 7:
            case 8:
                double range3 = Math.toRadians(degree + degree / 2);
                radius = (int) (width / 2 / (1 + Math.sin(range3) / Math.sin(range2)));
                break;
            case 9:
                double range4 = Math.toRadians(degree + degree);
                radius = (int) (width / 2 / (1 + Math.sin(range4) / Math.sin(range2)));
                break;
            default:// 3，4
                radius = width / 4;
                break;
        }

        imageWidth = 2 * radius;
        int R = (int) (radius / Math.sin(range2));
        double dy = (width - (2 * radius + R + R * Math.cos(range2))) / 2;
        // 计算第一个图像的位置
        int i = composeMode % 2;
        if (i == 1) {// 中心正上方
            centerX = width / 2;
            centerY = (int) (dy + radius);
            rotateY = (int) (dy + radius + R);
        } else {// 中心偏左
            if (composeMode == 2) {
                centerX = centerY = (int) (width / 2 - radius * Math.sin(Math.toRadians(45)));
            } else {
                centerX = (int) (width / 2 - radius);
                centerY = (int) (width / 2 - R * Math.cos(range2));
            }
            rotateY = width / 2;
        }
    }

    private Bitmap makeUnitBitmap(int width) {
        Bitmap bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(ringWidth);
        paint.setColor(ringColor);
        int layer = canvas.saveLayer(0, 0, width, width, null, Canvas.ALL_SAVE_FLAG);
        // 绘制DST
        canvas.drawCircle(width / 2, width / 2, width / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        // 绘制SRC
        Bitmap srcBitmap = crateTestBitmap(width, width);
        canvas.drawBitmap(srcBitmap, 0, 0, paint);
        paint.setXfermode(null);
        paint.setStyle(Paint.Style.STROKE);
//        RectF rect = new RectF(0, 0, width, width);
//        canvas.drawArc(rect, 0, 360, false, paint);
//        canvas.drawCircle(width / 2, width / 2, width / 2 - ringWidth / 2, paint);
        canvas.restoreToCount(layer);

        return bitmap;
    }

    // create a bitmap with a rect, used for the "src" image
    static Bitmap crateTestBitmap(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.GREEN);
        c.drawRect(0, 0, w, h, p);
        return bm;
    }

    static Bitmap createCycleBitmap(int radius) {
        Bitmap bm = Bitmap.createBitmap(radius * 2, radius * 2, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.GREEN);
        c.drawCircle(radius, radius, radius, p);
        return bm;
    }

    static Bitmap crateBorderBitmap(int radius, int ringWidth, float degree) {
        int d = radius * 2;
        Bitmap bm = Bitmap.createBitmap(d, d, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.GREEN);
        c.drawCircle(radius, radius, radius, p);
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        p.setColor(Color.RED);
        // 圆弧相关参数
        double range = Math.toRadians(degree / 2);
        int y = (int) (radius - radius * Math.cos(range));
        int dy = Math.min(ringWidth, y);
        RectF rectF = new RectF(0, -(d - dy), d, dy);
        c.drawArc(rectF, 90 - degree / 2, degree, true, p);

        return bm;
    }

}
