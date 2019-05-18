# 2_3-viewpager_3
2_3 viewpager 模拟微信朋友圈查看图片，横向滑动翻页，上下滑动，View上下移动，抬起时，如果移动到一定范围，则Viewpager消失，否则状态还原。实现疑问请见 viewpager_3说明.doc。

https://github.com/chengxiaobo2/2_3-viewpager_3/blob/master/viewpager_3%20%E8%AF%B4%E6%98%8E.docx 

![image](https://github.com/chengxiaobo2/2_3-viewpager_3/blob/master/viewpager_3_1.gif)
![image](https://github.com/chengxiaobo2/2_3-viewpager_3/blob/master/viewpager_3_2.gif)

2017年3月31日写demo 2019年5月18日写注释
## 思路分析
* 一开始的思路是，想判断滑动的方向是横向还是纵向。
  * 如果是横向的话，则不处理，让上层的viewpager处理。
  * 如果是纵向的话，则ImageView处理，设置ImageView的TranslationX和TranslationY。
  * 但是发现这样做的时候，有些卡顿（具体怎么回事，我忘了，哈哈）。
* 后来发现，我们可以利用ViewPager的拦截去处理。  
  * ImageView down->返回true。
  * ImageView 横向移动，ViewPager会拦截Move事件。并调用ImageView的Cancel。
     * 我们就在Cancel里面，让ImageView归位，ViewPager处理横向滑动。
  * ImageView 纵向移动，ViewPager不会拦截Move事件，这时候我们可以让ImageView处理位移以及设置父级的透明度。
  * 手指抬起的时候，根据移动的距离，来判断ViewPager是消失呢，还是ImageView归位呢。

## 具体实现遇到的问题
* 问题一：因为ImageView会根据我们手指的Move来设置Translation。这时候ImageView的坐标系变了。 导致计算移动的距离不对了。<br>
    * 我的代码用的是父级坐标系计算移动距离。
    ```java
    private float[] getParentPoint(float []src)
    {
         getMatrix().mapPoints(src);

         return src;
    }
    ```
    * 也可以用屏幕坐标去做。（别的Demo有这样实现哦）
   ```java
   event.getRawX()
   event.getRawY()
   ```