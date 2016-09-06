# CeleryRatingbar
因为android自带的Ratingbar有诸多的限制，所以重新自定义一个CeleryRatingbar控件，对Ratingbar的星星大小和间距问题进行优化。
#使用
###属性说明

```celery:numStars```：  表示星星的总数；

```celery:rating```：    表示当前控件的数值；

```celery:stepSize```：  表示数值变化的单位大小；

```celery:star```：      背景星星图片；

```celery:progress```：  被选中时的图片；

```celery:isChangeValue```：表示用户是否可以改变当前的数值；

```celery:showType```：表示星星图片的大小是否会自动适应当前控件的大小；

###事例
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:celery="http://schemas.android.com/apk/res-auto"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.celery.celeryratingbar.CeleryRatingBar
        android:id="@+id/rating"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        celery:isChangeValue="true"
        celery:numStars="5"
        celery:rating="3"
        celery:stepSize="0.1"
        celery:star="@mipmap/star_nor"
        celery:progress="@mipmap/star_presse"
        celery:showType="auto"/>


</LinearLayout>
```
#效果

![image](https://raw.githubusercontent.com/gwenjun08/CeleryRatingbar/master/CeleryRatingbar/app/2016-09-06-10mzDemo.gif)

