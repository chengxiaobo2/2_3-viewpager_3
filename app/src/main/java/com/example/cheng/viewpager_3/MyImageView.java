package com.example.cheng.viewpager_3;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
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
    private float alpha=1.0f;

    private ViewGroup viewGroupParent;

    private DissmissInterface dissmissInterface;

    public MyImageView(Context context)
    {
        super(context);
        initData();
    }

    public MyImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initData();
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initData();
    }

    public void setDissmissInterface(DissmissInterface dissmissInterface) {
        this.dissmissInterface = dissmissInterface;
    }

    public void initData()
    {
        this.post(new Runnable() {
            @Override
            public void run() {

                viewGroupParent=(ViewGroup)(MyImageView.this.getParent());
            }
        });

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

                if(Math.abs(parentLastY-parentDownY)>viewGroupParent.getHeight()/3)
                {
                    float finalTranslateX=0.0f;
                    float finalTranslateY=0.0f;

                    if (parentLastY-parentDownY>=0) //向下
                    {
                        finalTranslateY=(viewGroupParent.getHeight()+this.getHeight())/2;
                    }else //向上
                    {
                        finalTranslateY=-(viewGroupParent.getHeight()+this.getHeight())/2;
                    }
                    if(parentLastX-parentDownX>=0) //向右
                    {
                        finalTranslateX=(viewGroupParent.getWidth()+this.getWidth())/2;
                    }else //向左
                    {
                        finalTranslateX=-(viewGroupParent.getWidth()+this.getWidth())/2;
                    }

                    AnimatorSet set=new AnimatorSet();
                    set.playTogether(ObjectAnimator.ofFloat(this,"TranslationX",parentLastX-parentDownX,finalTranslateX),ObjectAnimator.ofFloat(this,"TranslationY",parentLastY-parentDownY,finalTranslateY),ObjectAnimator.ofFloat(viewGroupParent,"alpha",alpha,0.0f));
                    set.start();
                    set.setDuration(1000);
                    set.addListener(new Animator.AnimatorListener() {

                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {

                            translate0();

                            if(dissmissInterface!=null)
                            {
                                dissmissInterface.dissmiss();
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });




                }else
                {
                    translate0();
                }

            case MotionEvent.ACTION_CANCEL:

                System.out.println("====ACTION_CANCEL==========");

                translate0();

                break;

            default:
                break;
        }

        return true;
    }

    private void translate()
    {
        alpha=1.0f-Math.abs(parentLastY-parentDownY)/((viewGroupParent.getHeight()+this.getHeight())/2);
        viewGroupParent.setAlpha(alpha);


        this.setTranslationX(parentLastX-parentDownX);
        this.setTranslationY(parentLastY-parentDownY);

        this.invalidate();


    }

    private void translate0()
    {
        this.setTranslationX(0);
        this.setTranslationY(0);
        viewGroupParent.setAlpha(1.0f);

        this.invalidate();
    }

    private float[] getParentPoint(float []src)
    {
         getMatrix().mapPoints(src);

         return src;
    }

    interface DissmissInterface
    {
        void dissmiss();
    }
}
