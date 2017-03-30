package com.example.cheng.viewpager_3;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ImageView;

/**
 * Created by cheng on 2017/3/29.
 */

public class MyImageView extends ImageView {

    private boolean isIntercept=false;
    private boolean isFirstMove=true;

    private float lastX=0.0f;
    private float lastY=0.0f;
    private float downX=0.0f;
    private float downY=0.0f;

    private float parentLastX=0.0f;
    private float parentLastY=0.0f;
    private float parentDownX=0.0f;
    private float parentDownY=0.0f;

    private int mTouchSlop;

    public MyImageView(Context context)
    {
        super(context);
        init(context);
    }

    public MyImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context)
    {
        mTouchSlop = ViewConfiguration.get(context).getScaledPagingTouchSlop();
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

                isIntercept=false;

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

                if(isFirstMove)
                {

                    if((moveX!=downX||moveY!=downY)&&(Math.abs(moveX - downX)>mTouchSlop*2||Math.abs(moveY - downY)>mTouchSlop*2))
                    {
                        isFirstMove = false;

                        if (Math.abs(moveX - downX) < Math.abs(moveY - downY)) {
                            System.out.println("====intercept==ACTION_MOVE==========");

                            this.getParent().requestDisallowInterceptTouchEvent(true);

                            isIntercept = true;

                            translate();

                            return true;
                        } else {
                            this.getParent().requestDisallowInterceptTouchEvent(false);
                            System.out.println("====nointercept==ACTION_MOVE==========");

                            return false;
                        }
                    }

                }else
                {
                    if(isIntercept)//上次拦截
                    {

                        System.out.println("===next=intercept==ACTION_MOVE==========");

                        translate();
                        return true;

                    }else
                    {
                        System.out.println("===next=nointercept==ACTION_MOVE==========");
                        return false;
                    }
                }



            case MotionEvent.ACTION_UP:

                System.out.println("====ACTION_UP==========");
                isIntercept=false;
                isFirstMove=true;

                this.setTranslationX(0);
                this.setTranslationY(0);

                this.invalidate();

                this.getParent().requestDisallowInterceptTouchEvent(false);

                break;

            case MotionEvent.ACTION_CANCEL:

                System.out.println("====ACTION_CANCEL==========");
                isIntercept=false;
                isFirstMove=true;

                this.getParent().requestDisallowInterceptTouchEvent(false);
                break;

            default:
                break;
        }

        return false;
    }

    private void translate()
    {
//        ObjectAnimator objectAnimatorX=ObjectAnimator.ofFloat(this,"translationX",0,lastX-downX);
//        ObjectAnimator objectAnimatorY=ObjectAnimator.ofFloat(this,"translationY",0,lastY-downY);
//        objectAnimatorX.setDuration(100);
//        objectAnimatorY.setDuration(100);
//        objectAnimatorX.start();
//        objectAnimatorY.start();

//        this.setTranslationX(10/        this.setTranslationY(100);


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
