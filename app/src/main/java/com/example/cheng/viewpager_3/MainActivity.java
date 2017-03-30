package com.example.cheng.viewpager_3;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private List<Integer> list = new ArrayList<Integer>();
    private List<ViewGroup> viewList = new ArrayList<ViewGroup>();
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.vp);
        Button btn = (Button) findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewPager.setVisibility(View.VISIBLE);
            }
       });


        initdata();

        viewPager.setAdapter(new ViewPagerAdapter(viewList));
    }

    private void initdata() {

        int[] arr = {R.drawable.a, R.drawable.b, R.drawable.c};

        for (int i = 0; i < 5; i++) {
            Random random = new Random();
            int r1 = random.nextInt(3);

            list.add(arr[r1]);

            ViewGroup viewGroup = (ViewGroup) (View.inflate(MainActivity.this, R.layout.view_pager_page, null));
            MyImageView iv01 = (MyImageView) viewGroup.findViewById(R.id.iv_01);

            iv01.setDissmissInterface(new MyImageView.DissmissInterface()
            {
                @Override
                public void dissmiss() {

                    viewPager.setVisibility(View.GONE);
                }
            });

//            iv01.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View v) {
//
//                    Toast.makeText(MainActivity.this, "Touch===", Toast.LENGTH_SHORT).show();
//                }
//            });
            
            iv01.setImageResource(arr[r1]);

            viewList.add(viewGroup);
        }
    }


    @Override
    public void onBackPressed() {

        if(viewPager.getVisibility()==View.VISIBLE)
        {
            viewPager.setVisibility(View.GONE);
        }else
        {
            this.finish();
        }
    }

    class ViewPagerAdapter extends PagerAdapter {

            private List<ViewGroup> viewList;

            public ViewPagerAdapter(List<ViewGroup> viewList) {
                this.viewList = viewList;
            }

            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {

                container.removeView(viewList.get(position));

            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                ViewGroup view = viewList.get(position);
                container.addView(view);

                return view;
            }

        }
    }