package com.hencoder.hencoderpracticedraw4.practice;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.hencoder.hencoderpracticedraw4.R;

public class Practice14FlipboardView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap;
    Camera camera = new Camera();
    Matrix matrix=new Matrix();
    int degree;
    ObjectAnimator animator = ObjectAnimator.ofInt(this, "degree", 0, 180);

    public Practice14FlipboardView(Context context) {
        super(context);
    }

    public Practice14FlipboardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice14FlipboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.maps);

        animator.setDuration(2500);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        animator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        animator.end();
    }

    @SuppressWarnings("unused")
    public void setDegree(int degree) {
        this.degree = degree;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int x = centerX - bitmapWidth / 2;
        int y = centerY - bitmapHeight / 2;

        //画上部分
        canvas.save();
        canvas.clipRect(0, 0, centerX * 2, centerY);
        canvas.drawBitmap(bitmap, x, y, paint);
        canvas.restore();

//        //画下部分
//        canvas.save();
//        camera.save();
//        canvas.translate(centerX, centerY);//这部分和下面没注释的写法不同，位移的顺序是相反的。这是因为camera和canvas不一样坐标系。是以谁为参照物。
//        camera.rotateX(degree);
//        camera.applyToCanvas(canvas);
//        canvas.translate(-centerX, -centerY);
//        camera.restore();
//
//        canvas.clipRect(0, centerY, centerX * 2, centerY * 2);
//        canvas.drawBitmap(bitmap, x, y, paint);
//        canvas.restore();



        matrix.reset();

        camera.save();
        camera.rotateX(degree);
        camera.getMatrix(matrix);
        camera.restore();

        matrix.preTranslate(-centerX,-centerY);
        matrix.postTranslate(centerX,centerY/4);

        canvas.save();
        canvas.concat(matrix);
        canvas.clipRect(0,centerY,centerX*2,centerY*2);
        canvas.drawBitmap(bitmap,x,y,paint);
        canvas.restore();
    }
}
