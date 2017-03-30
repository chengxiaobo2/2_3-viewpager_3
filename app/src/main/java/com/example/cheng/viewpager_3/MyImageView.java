package com.example.cheng.viewpager_3;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by cheng on 2017/3/29.
 */

public class MyImageView extends ImageView {

    private float lastX=0.0f;
    private float lastY=0.0f;
    private float downX=0.0f;
    private float downY=0.0f;

    private float parentLastX=0.0f;
    private float parentLastY=0.0f;
    private float parentDownX=0.0f;
    private float parentDownY=0.0f;

    public MyImageView(Context context)
    {
        super(context);
    }

    public MyImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        System.out.println("====dispatchTouchEvent==========");

        return super.dispatchTouchEvent(event);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        final int action = event.getAction();

        lastX=event.getX();
        lastY=event.getY();

        float []b=getParentPoint(new float[]{lastX,lastY});

        parentLastX= b[0];
        parentLastY=b[1];

        switch (action & MotionEventCompat.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:

                downX=event.getX();
                downY=event.getY();

                float []a=getParentPoint(new float[]{downX,downY});

                parentDownX= a[0];
                parentDownY=a[1];

                System.out.println("====ACTION_DOWN================================================");

                return true;

            case MotionEvent.ACTION_MOVE:
                float moveX=event.getX();
                float moveY=event.getY();

                System.out.println("====ACTION_MOVE==last====x===="+lastX+"==Y=="+lastY);

                if((moveX!=downX||moveY!=downY))
                {
                    System.out.println("====intercept==ACTION_MOVE==========");

                    translate();
                }

                break;

            case MotionEvent.ACTION_UP:

                System.out.println("====ACTION_UP==========");

            case MotionEvent.ACTION_CANCEL:

                System.out.println("====ACTION_CANCEL==========");

                this.setTranslationX(0);
                this.setTranslationY(0);
                this.invalidate();

                break;

            default:
                break;
        }

        return true;
    }

    private void translate()
    {
        this.setTranslationX(parentLastX-parentDownX);
        this.setTranslationY(parentLastY-parentDownY);

        this.invalidate();

    }


    private float[] getParentPoint(float []src)
    {
         getMatrix().mapPoints(src);

         return src;
    }
}
