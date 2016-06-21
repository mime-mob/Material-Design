# Material-Design
介绍Material design的相关控件在开发中使用

##概述
对于开发人员，Android21新增了许多新控件和新特性，这些控件和特性都是基于Material Design的设计理念的，这一篇就来讲讲Material Design给Android开发带来的变化。

---

##主题和布局
Material提供了下面三种主题
 - @android:style/Theme.Material (dark version)
 - @android:style/Theme.Material.Light (light version)
 -  @android:style/Theme.Material.Light.DarkActionBar
 
 使用Material的主题必须Api在21及以上；如果要适配21以下的设备，可以使用兼容包：
	    ```Theme.AppCompat.Light
		   Theme.AppCompat.Light.NoActionBar
	    ```
继承Material主题时，可以通过下面的属性来自定义调色板:
```
<style name="AppTheme" parent="android:Theme.Material">
	<item name="colorPrimary"></item>
	<item name="colorPrimaryDark"></item>
	<item name="colorAccent"></item>
	<item name="textColorPrimary"></item>
	<item name="windowBackground"></item>
	<item name="navigationBarColor"></item>
</style>
```
 <img src="http://img.blog.csdn.net/20160617171643078" height=540>

**设置StatusBar和系统NavigationBar的颜色**

- Andriod4.4 Holo主题
	- 使用主题：继承主题`Theme.Holo.Light.NoActionBar.TranslucentDecor` ;不继承这个也可以，设置下面属性即可:
    ```
	  getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
	  getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    ```
	- 设置 fitsSystemWindows 属性为true来进行布局调整
	- 使用SystemTintBar开源库设置颜色（其实就是自己画个View上去）

- Android5.0及以上
1. 使用主题：继承主题Theme.Material.Light，且SDK >= 21
2. 设置属性或者调用方法：
	
	```
	colorPrimaryDark、navigationBarColor
	getWindow().setStatusBarColor(Color.RED);
	getWindow().setNavigationBarColor(Color.RED);
	```
- 使用兼容包
`Theme.AppCompat.Light` 和上面一样

**Theme Individual Views  个人主题View**

在layout的xml定义中，可以使用android:theme来指定该View所适用的主题，指定后，它将改变当前View及其子View的theme。
```
<Button
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="个人主题"
    android:theme="@style/xxx" />
```
Ps: [搞清沉浸式状态栏和透明状态栏](http://niorgai.github.io/2016/03/20/Android-transulcent-status-bar/)

------
##视图和阴影
上一篇讲到阴影的产生是因为高度差，阴影的大小是由相对高度差决定的。

![阴影示例](http://img.blog.csdn.net/20160617174736287)

在以前UI只是二维时，View的位置参数有以下公式
`X = Left + translationX`
`Y = Top = translationY;`
Material新增的第三维度同样遵循这个公式
`Z = elevation + translationZ`

在初始化View的时候，elevation被复制，之后在Z轴上做属性动画，改变translationZ，两者之和是View的绝对高度。

###设置Elevation

- 在layout.xml中设置
`android:elevation="2dp"`

- 在代码中设置
`mButton.setElevation(4f);`

###Palette取色器
这个类挺有意思，在Android的版本发展中，UI越来越成为Google的发展中心，这次的Android5.X创新的使用了Palette来提取颜色，从而让主题能够动态适应当前页面的色调，使得整个app的颜色基本和谐统一。

Android内置了几种提取颜色的种类：

- Vibrant（充满活力的）
- Vibrant dark（充满活力的黑）
- Vibrant light（充满活力的白）
- Muted(柔和的)
- Muted dark(柔和的黑)
- Muted light(柔和的白)

使用：
1. gradle

`compile 'com.android.support:palette-v7:21.0.+'`

2. Api

```
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    etContentView(R.layout.activity_main);
	    setPalette();
	}

	private void setPalette() {
	    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
	    //创建Palette对象
	    Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
	        @Override
	        public void onGenerated(Palette palette) {
	            //通过Palette来获取对应的色调
	            Palette.Swatch vibrant = palette.getDarkVibrantSwatch();
	            //将颜色设置给相应的组件
	            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(vibrant.getRgb()));
	            Window window = getWindow();
	            window.setStatusBarColor(vibrant.getRgb());
	        }
	    });
	}
```
3.效果

![palette效果](http://img.blog.csdn.net/20160617180213250)

4.分析

- **生成Palette对象的方法有好多**

```
Palette palette = Palette.from(bitmap).generate();
Palette palette = Palette.generate(bitmap);
Palette palette = Palette.generate(bitmap, 24);

```
这几个都是同步的获取，如果bitmap很小可以这样使用，如果bitmap很大，就需要使用异步获取。

```
Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
    @Override
    public void onGenerated(Palette palette) {
        
    }
});
```
**然而最新版本都deprecated了，建议使用Builder来构建Palette，Builder方式也支持同步和异步两种方式。**

**同步：**
```
Palette palette = new Palette.Builder(bitmap).generate();
```
**异步：**

```
new Palette.Builder(bitmap).generate(new Palette.PaletteAsyncListener() {
    @Override
    public void onGenerated(Palette palette) {
        
    }
});
```
Ps:**重要的一点**
上面有一个方法传了24，这个值代表的是取样的最大颜色数，默认是16，建议范围8-32.
数值越大，取样耗时越长，但是取样代表性越强，所以根据你的图片的大小和需求，自己确定数值的大小。

- **取样风格**
`Palette.Swatch vibrant = palette.getDarkVibrantSwatch();`

Palette提供了6种取样风格:

```
palette.getVibrantSwatch(); //充满活力的
palette.getDarkVibrantSwatch(); //充满活力的黑
palette.getLightVibrantSwatch(); //充满活力的白
palette.getMutedSwatch(); //柔和的
palette.getDarkMutedSwatch(); //柔和的黑
palette.getLightMutedSwatch(); //柔和的白
```
Ps：**这里的黑、白是指深色系和浅色系**

- **样本值**
Demo里用到的vibrant.getRgb()是获取取样后的颜色RGB值
除了这个api，还有以下：

	>getPopulation():Swatch所代表的取样样本的像素个数
getRgb():Swatch取样的颜色RGB
getHsl():Swatch取样的颜色HLS
**getBodyTextColor():在此取样颜色上，显示Body文本的推荐色**
**getTitleTextColor():在此取样颜色上，显示Title文本的推荐色**

- **Tinting着色器**
先看一个例子，Tinting的使用非常的简单，只要在XML中配置好tint和tintMode就可以了，对于配置组合效果，只需要大家实际操作一下，就能非常清晰的理解处理效果。
	```
	<ImageView
		android:layout_width="100dp"
		android:layout_height="100dp"
		android:src="@mipmap/ic_launcher" />

	<ImageView
		android:layout_width="100dp"
		android:layout_height="100dp"
		android:src="@mipmap/ic_launcher"
		android:tint="@android:color/holo_blue_bright" />

	<ImageView
		android:layout_width="100dp"
		android:layout_height="100dp"
		android:src="@mipmap/ic_launcher"
		android:tint="@android:color/holo_blue_bright"
		android:tintMode="add" />

	<ImageView
		android:layout_width="100dp"
		android:layout_height="100dp"
		android:src="@mipmap/ic_launcher"
		android:tint="@android:color/holo_blue_bright"
		android:tintMode="multiply" />
	```
	*Ps:tint->着色颜色；tintMode->着色模式*
![tinting图片对比](http://img.blog.csdn.net/20160620093610788)

	所谓着色，就是在背景颜色上再画一层颜色，不同的着色模式，对两层颜色的处理不一样。
系统控件有很多用到了着色，比如EditText，在Material主题下，设置colorAccent，EditText在聚焦时会变成高亮色。
![着色](http://img.blog.csdn.net/20160620094405804)

如果要用两张图片的方式就太浪费资源了，系统用的就是用TintManager为背景着色，着色模式是SRC_IN。
着色模式有以下几种：
![这里写图片描述](http://img.blog.csdn.net/20160620094559558)
>文字解释：
>1.PorterDuff.Mode.CLEAR:所绘制不会提交到画布上。
>2.PorterDuff.Mode.SRC:显示上层绘制图片
>3.PorterDuff.Mode.DST:显示下层绘制图片
>4.PorterDuff.Mode.SRC_OVER:正常绘制显示，上下层绘制叠盖。
>5.PorterDuff.Mode.DST_OVER:上下层都显示。下层居上显示。
>6.PorterDuff.Mode.SRC_IN:取两层绘制交集。显示上层。
>7.PorterDuff.Mode.DST_IN:取两层绘制交集。显示下层。
>8.PorterDuff.Mode.SRC_OUT:取上层绘制非交集部分。
>9.PorterDuff.Mode.DST_OUT:取下层绘制非交集部分。
>10.PorterDuff.Mode.SRC_ATOP:取下层非交集部分与上层交集部分
>11.PorterDuff.Mode.DST_ATOP:取上层非交集部分与下层交集部分
>12.PorterDuff.Mode.XOR:异或：去除两图层交集部分
>13.PorterDuff.Mode.DARKEN:取两图层全部区域，交集部分颜色加深
>14.PorterDuff.Mode.LIGHTEN:取两图层全部，点亮交集部分颜色
>15.PorterDuff.Mode.MULTIPLY:取两图层交集部分叠加后颜色
>16.PorterDuff.Mode.SCREEN:取两图层全部区域，交集部分变为透明色

代码设置：

```
	Drawable drawable = ContextCompat.getDrawable(this,R.mipmap.ic_launcher);
  Drawable.ConstantState state = drawable.getConstantState();
  Drawable drawable1 = DrawableCompat.wrap(state == null ? drawable : state.newDrawable()).mutate();
  drawable1.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
  DrawableCompat.setTint(drawable, ContextCompat.getColor(this, R.color.colorAccent));
  //ivTint.setImageDrawable(drawable);
  ivTint.setImageDrawable(drawable1);
```
	
比如我现在要对一个自定义组件实现对Tint的支持，其实只用继承下，加一些代码就好了，代码如下（几乎通用）：

```
	public class AppCompatFlowLayout extends FlowLayout implements TintableBackgroundView {
	    private static final int[] TINT_ATTRS = {
            android.R.attr.background
	    };
	    private TintInfo mInternalBackgroundTint;
	    private TintInfo mBackgroundTint;
	    private TintManager mTintManager;
	    public AppCompatFlowLayout(Context context) {
	        this(context, null);
	    }
	    public AppCompatFlowLayout(Context context, AttributeSet attributeSet) {
	        this(context, attributeSet, 0);
	    }

    public AppCompatFlowLayout(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);

        if (TintManager.SHOULD_BE_USED) {
            TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attributeSet,
                    TINT_ATTRS, defStyle, 0);
            if (a.hasValue(0)) {
                ColorStateList tint = a.getTintManager().getTintList(a.getResourceId(0, -1));
                if (tint != null) {
                    setInternalBackgroundTint(tint);
                }
            }
            mTintManager = a.getTintManager();
            a.recycle();
        }
    }

    private void applySupportBackgroundTint() {
        if (getBackground() != null) {
            if (mBackgroundTint != null) {
                TintManager.tintViewBackground(this, mBackgroundTint);
            } else if (mInternalBackgroundTint != null) {
                TintManager.tintViewBackground(this, mInternalBackgroundTint);
            }
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        applySupportBackgroundTint();
    }

    private void setInternalBackgroundTint(ColorStateList tint) {
        if (tint != null) {
            if (mInternalBackgroundTint == null) {
                mInternalBackgroundTint = new TintInfo();
            }
            mInternalBackgroundTint.mTintList = tint;
            mInternalBackgroundTint.mHasTintList = true;
        } else {
            mInternalBackgroundTint = null;
        }
        applySupportBackgroundTint();
    }

    @Override
    public void setSupportBackgroundTintList(ColorStateList tint) {
        if (mBackgroundTint == null) {
            mBackgroundTint = new TintInfo();
        }
        mBackgroundTint.mTintList = tint;
        mBackgroundTint.mHasTintList = true;

        applySupportBackgroundTint();
    }

    @Nullable
    @Override
    public ColorStateList getSupportBackgroundTintList() {
        return mBackgroundTint != null ? mBackgroundTint.mTintList : null;
    }

    @Override
    public void setSupportBackgroundTintMode(PorterDuff.Mode tintMode) {
        if (mBackgroundTint == null) {
            mBackgroundTint = new TintInfo();
        }
        mBackgroundTint.mTintMode = tintMode;
        mBackgroundTint.mHasTintMode = true;

        applySupportBackgroundTint();
    }

    @Nullable
    @Override
    public PorterDuff.Mode getSupportBackgroundTintMode() {
        return mBackgroundTint != null ? mBackgroundTint.mTintMode : null;
    }
}
```

- **outLine和Clipping裁剪**

Clipping可以让我们改变一个视图的外形，要使用Clipping，首先需要使用ViewOutlineProvider来修改outline作用给视图
下面这个例子，将一个正方形的textview通过Clipping裁剪成一个圆角正方形和一个圆。

```
<TextView
	android:id="@+id/tv_rect"
	android:layout_width="100dp"
	android:layout_height="100dp"
	android:elevation="1dp" />

<TextView
	android:id="@+id/tv_circle"
	android:layout_width="100dp"
	android:layout_height="100dp"
	android:elevation="1dp" />
```
```
@Bind(R.id.tv_rect)
TextView tv1;

@Bind(R.id.tv_circle)
TextView tv2;

ViewOutlineProvider vlp1 = new ViewOutlineProvider() {
	@Override
	public void getOutline(View view, Outline outline) {
		outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 30);
	}
};

ViewOutlineProvider vlp2 = new ViewOutlineProvider() {
	@Override
	public void getOutline(View view, Outline outline) {
		outline.setOval(0, 0, view.getWidth(), view.getHeight());
	}
};

tv1.setOutlineProvider(vlp1);
tv2.setOutlineProvider(vlp2);
```
![方形裁剪](http://img.blog.csdn.net/20160620095449803)  ![圆形裁剪](http://img.blog.csdn.net/20160620095516780)

Ps:**这个灰色边框是因为elevation>0而产生的阴影**。

打开Outline.java文件，公开的方法并不算多
>**outline.canClip();**
是否支持裁剪，目前只有矩形、圆形、圆角矩形支持裁剪
**outline.getAlpha();**
设置透明度
**outline.isEmpty();**
是否是空的。刚创建的时候是空的，调用了setEmpty后是空的。
**outline.offset(0, 0);**
设置x、y的偏移量
**outline.setAlpha(1);**
设置透明度
**outline.setConvexPath(null);**
设置用于构造一个outline的path
**outline.setEmpty();**
置空
**outline.setOval(0, 0, 0, 0);**
设置椭圆
**outline.setOval(new Rect(0, 0, 0, 0));**
同上
**outline.setRoundRect(0, 0, 0, 0, 10);**
设置圆角矩形
**outline.setRoundRect(new Rect(0, 0, 0, 0), 10);**
同上

以上都是在操作outline，有了outline就可以用它来裁剪View。 `view.setClipToOutline(true)`

还用以上面的例子来说：
一个TextView，设置背景颜色
![原始textview](http://img.blog.csdn.net/20160620100219173)

```
<TextView
    android:layout_width="100dp"
    android:layout_height="100dp"
    android:background="#d0d0d0"
    android:text="OutLine"
    android:gravity="center"
    android:elevation="2dp"/>
```

设置一个outline
`tv.setOutlineProvider(outline);`

![设置outlineprovider的textview](http://img.blog.csdn.net/20160620100219173)

你会发现并没有变化。
因为outline并没有应用到裁剪view的内容，需要调用`tv.setClipToOutline(true)`
这句话会用当前的outline裁剪view的内容，需要canClip返回true，之前提到，目前只有矩形、圆形、圆角矩形支持裁剪。
裁剪结果：
![裁剪效果](http://img.blog.csdn.net/20160620100446315)

Ps：**关于outline、clipping、background、alpha**
Outline可以影响投影的轮廓，但如果setClipToOutline(false)，那它并不会裁剪view的内容，所以也不能改变background和forground的形状。
Outline只是用path描述的一种形状，本身并没有颜色，但是它却有alpha值，并且它的alpha值会影响阴影的透明度。
按照Material的原则，光照不透明的实体才会产生阴影，outline能影响阴影轮廓，却又没有改变view的实体形状，这就不好理解了。
setClipToOutline内部调用了native方法，没法知道具体的实现。

----
##UI控件
Android5.0基于Material Design，提供了很多新控件，这些控件无论在功能、体验和性能上都很出色，下面一一列举各个控件的使用方法。

###SnackBar###
SnackBar通过在屏幕底部展示简洁的信息，为一个操作提供了一个轻量级的反馈，并且在Snackbar中还可以包含一个操作，在同一时间内，仅且只能显示一个 Snackbar，它的显示依赖于UI，不像Toast那样可以脱离应用显示。它的用法和Toast很相似，唯一不同的就是它的第一个参数不是传入Context而是传入它所依附的父视图，但是他比Toast更强大。
（Dialog > SnackBar > Toast）
**gradle**
`compile 'com.android.support:design:23.1.1'`

**最简单的提示**
`Snackbar.make(view, "SnackBar Demo Text", Snackbar.LENGTH_LONG).show();`
![SnackBar Demo](http://img.blog.csdn.net/20160620101214304)

**设置一个Action,自带点击事件**
```
Snackbar.make(view, "Change Text", Snackbar.LENGTH_LONG)
.setAction("OK", new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tv.setText("New Text");
        }
}).show();
```
![action snackbar](http://img.blog.csdn.net/20160620101343174)

**设置背景颜色**
```
Snackbar sb=Snackbar.make(view, "SnackBar demo", Snackbar.LENGTH_LONG);
sb.setAction("OK", new onClickListener()());
sb.getView().setBackgroundColor(0xfff44336);
sb.show();
```
![背景色SnackBar](http://img.blog.csdn.net/20160620101454914)

**设置Action文字颜色**
默认的Action文字颜色是ColorAccent，也可以通过api设置`sb.setActionTextColor();`

**其他API**
>**sb.dismiss()**;消失
**sb.isShown()**;是否在显示
**sb.isShownOrQueued()**;是否在显示或者在等待队列
**sb.setText()**;设置文字

**显示时长**
>**LENGTH_LONG**：长时间
**LENGTH_SHORT**：短时间
**LENGTH_INDEFINITE**：无限期，知道调用dismiss或者下一个SnackBar显示

**注意1.SnackBar始终从底部弹出**
SnackBar调用make时传进去一个View，它会顺着这个View去找父级，一直找到CoordinatorLayout或者FrameLayout，然后在它底部弹出。如果你的布局中没有包含这两个容器的一个，它就会一直找到Widnow的DecorView，效果就是在屏幕底部弹出。
看源码：
```
do {
    if(view instanceof CoordinatorLayout) {
        ...            
    }
    if(view instanceof FrameLayout) {
        ...
    }
 } while(view != null);
```
了解这特性有两个用处，解决异常Bug，实现特殊需求。
Demo:
```
<android.support.design.widget.CoordinatorLayout
	android:layout_width="match_parent"
	android:layout_height="match_parent"
	android:layout_margin="30dp">

	<android.support.design.widget.FloatingActionButton
		android:layout_width="wrap_content"
		android:layout_height="wrap_content"
		android:src="@mipmap/ic_launcher"
		app:borderWidth="0dp"
		app:fabSize="normal" />
</android.support.design.widget.CoordinatorLayout>
```
![snackbar 底部弹出](http://img.blog.csdn.net/20160620102500221)

**注意2.悬浮按钮被snackBar遮挡**
```
<RelaticeLayout
	android:layout_width="match_parent"
	android:layout_height="match_parent"
	android:layout_marginBottom="30dp">

	<android.support.design.widget.FloatingActionButton
		android:layout_width="wrap_content"
		android:layout_height="wrap_content"
		android:src="@mipmap/ic_launcher"
		app:borderWidth="0dp"
		app:fabSize="normal" />
</RelaticeLayout>
```
![FloatingButon 被遮挡](http://img.blog.csdn.net/20160620102758100)
这个问题一看很棘手，其实也很简单用一下Material design库里的CoordinatorLayout即可，Java代码完全不用动。
```
<android.support.design.widget.CoordinatorLayout
	android:layout_width="match_parent"
	android:layout_height="match_parent"
	android:layout_margin="30dp">

	<android.support.design.widget.FloatingActionButton
		android:layout_width="wrap_content"
		android:layout_height="wrap_content"
		android:src="@mipmap/ic_launcher"
		app:borderWidth="0dp"
		app:fabSize="normal" />
</android.support.design.widget.CoordinatorLayout>
```
![CoordinatorLayout下的SnackBar](http://img.blog.csdn.net/20160620102914263)

**稍微总结下：**
>1.比toast更加好，毕竟snackbar 可以响应点击事件
2.比Dialog轻量，可以替换很多Dialog的场景
3.snackbar 同一时间有且只有一个在显示
4.snackbar 上不要有图标
5.snackbar上action 只能有一个
6.如果有悬浮按钮FloatingActionButton的话，snackbar 在弹出的时候不要覆盖这个button
7.了解为什么SnackBar是从底部弹出的

###CardView###
CardView曾经开始流行在Google+，后来越来越多的APP也引入了Card这样的布局方式，说到底，CardView也是一个容器布局，只是他提供了一种卡片的形式，Google所幸提供了一个CardView控件，方便大家使用这种布局，开发者可以设置大小和视图高度，圆角的角度等。
![cardview](http://img.blog.csdn.net/20160620103120434)
```
<android.support.v7.widget.CardView
	android:id="@+id/cardview"
	android:layout_width="match_parent"
	android:layout_height="80dp"
	android:layout_margin="16dp"
	android:clickable="true"
	android:foreground="?android:attr/selectableItemBackground"
	app:cardBackgroundColor="@color/colorAccent"
	app:cardCornerRadius="4dp"
	app:cardElevation="6dp">

	<TextView
		android:layout_width="wrap_content"
		android:layout_height="wrap_content"
		android:layout_gravity="center"
		android:text="TextView in CardView"
		android:textStyle="bold"
		android:textColor="@color/colorPrimaryDark"
		android:textSize="26sp" />
</android.support.v7.widget.CardView>
```
其实不用CardView也能实现，但是CardView做了很好的封装，只需要设置几个属性，就可以实现很好的效果，并且也减少了drawable资源，而且即便你什么都不设置，它也会设置合适的默认值。

**Ps:**

- 在低版本中设置了 CardElevation 之后 CardView 会自动留出空间供阴影显示，而 Lollipop 之后则需要手动设置 Margin 边距来预留空间，导致我在设置 Margin 在 Android 5.x 机器上调试好后，在 Kitkat 机器调试时发现边距非常大，严重地浪费了屏幕控件。因此，我们需要自定义两个dimen作为CardView的Margin值。
>/res/value
/res/value-v21
android:layout_margin="@dimen/xxx_card_margin"

-  android:foreground="?attr/selectableItemBackground"
这个属性会在 Lollipop 上自动加上 Ripple 效果，在旧版本则是一个变深/变亮的效果。
-  让点击效果更加贴近 Material Design，要实现这个效果不难，我们只需要借助Lollipop的一个新属性`android:stateListAnimator`
```
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
     <item android:state_enabled="true" android:state_pressed="true">
          <objectAnimator
               android:duration="@android:integer/config_shortAnimTime"
               android:propertyName="translationZ"
               android:valueTo="@dimen/touch_raise"
               android:valueType="floatType" />
     </item>
     <item>
           <objectAnimator
           android:duration="@android:integer/config_shortAnimTime"
           android:propertyName="translationZ"
           android:valueTo="0dp"
           android:valueType="floatType" />
     </item>
</selector>
```
`android:stateListAnimator="@anim/touch_raise"`

###Toolbar###
Toolbar是在 Android 5.0 开始推出的一个 Material Design 风格的导航控件 ，Google 非常推荐大家使用 Toolbar 来作为Android客户端的导航栏，以此来取代之前的 Actionbar 。与 Actionbar 相比， Toolbar 明显要灵活的多。它不像 Actionbar 一样，一定要固定在Activity的顶部，而是可以放到界面的任意位置。除此之外，在设计 Toolbar 的时候，Google也留给了开发者很多可定制修改的余地，这些可定制修改的属性在API文档中都有详细介绍，如：

- 设置导航栏图标；
- 设置App的logo；
- 支持设置标题和子标题；
- 支持添加一个或多个的自定义控件；
- 支持Action Menu；

**使用**
前面提到 Toolbar 是在 Android 5.0 才开始加上的，Google 为了将这一设计向下兼容，自然也少不了要推出兼容版的 Toolbar 。为此，我们需要在工程中引入 appcompat-v7 的兼容包，使用 android.support.v7.widget.Toolbar 进行开发。

MXL文件：
```
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">
    <android.support.v7.widget.Toolbar
        android:id="@+id/toolbar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@color/color_0176da">
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Clock" />
    </android.support.v7.widget.Toolbar>
</LinearLayout>
```

MENU文件：
```
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <item
        android:id="@id/action_search"
        android:icon="@mipmap/ic_search"
        android:title="@string/menu_search"
        app:showAsAction="ifRoom" />
    <item
        android:id="@id/action_notification"
        android:icon="@mipmap/ic_notifications"
        android:title="@string/menu_notifications"
        app:showAsAction="ifRoom" />
    <item
        android:id="@+id/action_item1"
        android:title="@string/item_01"
        app:showAsAction="never" />
    <item
        android:id="@+id/action_item2"
        android:title="@string/item_02"
        app:showAsAction="never" />
</menu>
```
Activity:
```
public class ToolBarActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_bar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.mipmap.ic_drawer_home);//设置导航栏图标
        toolbar.setLogo(R.mipmap.ic_launcher);//设置app logo
        toolbar.setTitle("Title");//设置主标题
        toolbar.setSubtitle("Subtitle");//设置子标题
        toolbar.setTitleTextColor(Color.rgb(250, 250, 250));//设置标题颜色
        toolbar.setSubtitleTextColor(Color.rgb(250, 250, 250));//设置子标题颜色
        toolbar.inflateMenu(R.menu.base_toolbar_menu);//设置右上角的填充菜单
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                
                return true;
            }
        });
    }
}
```
![toolbar](http://img.blog.csdn.net/20160620104229388)

**设置更多Menu的icon：**
`<item name="actionOverflowButtonStyle">@style/ActionButton.Overflow.White</item>`
```
<style name="ActionButton.Overflow.White" 
parent="android:style/Widget.Material.Light.ActionButton.Overflow">
    <item name="android:src">@mipmap/ic_more</item>
</style>
```

**设置返回menu的icon**
`toolbar.setNavigationIcon(R.mipmap.ic_drawer_home);`

**通过着色改变颜色**
```java
Drawable drawable = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
drawable.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
toolbar.setNavigationIcon(R.mipmap.ic_drawer_home);
```

***精卫填坑***

- **坑1：在XML中设置Toolbar属性不生效**
```
<android.support.v7.widget.Toolbar
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="@color/color_0176da"
    android:logo="@mipmap/ic_launcher"
    android:navigationIcon="@mipmap/ic_drawer_home"
    android:subtitle="456"
	android:title="123">
```
**解决方法：在根布局中加入自定义属性的命名空间**
```
xmlns:toolbar="http://schemas.android.com/apk/res-auto"
<android.support.v7.widget.Toolbar
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@color/color_0176da"
        toolbar:navigationIcon="@mipmap/ic_drawer_home"
        toolbar:logo="@mipmap/ic_launcher"
        toolbar:subtitle="456"
        toolbar:title="123">
```
为什么会出现这种问题呢？我猜测是因为这个控件是兼容版的控件，用 android:xxx 设置无效是的这些属性是在兼容包中，不在默认的Android SDK中，所以我们需要额外的引入。至于为什么IDE不报错，估计就是bug了吧！

- **坑2：menu字体颜色不能改变**
在style中设置：`<item name="actionMenuTextColor">#ffffff</item>` 并没有用
网上有人建议直接改全局的字体颜色
在style中设置：`<item name="android:textColorPrimary">#ffffff</item>`
但是这样其它所有文字的默认颜色都变了，**慎重慎重**！

- **坑3：ActionMode不能悬浮**
![actionMode](http://img.blog.csdn.net/20160620105241137)
**解决方法：在style中设置悬浮**
	```
	<item name="windowActionModeOverlay">true</item>
	```
![modeoverlay](http://img.blog.csdn.net/20160620105359133)

- **坑4：自定义的View位于 title 、 subtitle 和 actionmenu 之间，这意味着，如果 title 和 subtitle 都在，且 actionmenu选项 太多的时候，留给自定义View的空间就越小**

- **坑5：必须设置为没有ActionBar**
使用Toolbar必须要求当前Activity没有ActionBar，方法有两种。
	```
	<style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">

	supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
	RequestWindowFeature(Window.FEATURE_NO_TITLE);
	```
###FloatingActionButton###
FloatingActionButton从名字可以看出它是一个浮动的按钮，它是一个带有阴影的圆形按钮，可以通过fabSize来改变其大小，主要负责界面的基本操作，这个按钮总体来说还是比较简单的。
![floatingActionButton](http://img.blog.csdn.net/20160620105744769) 

默认FloatingActionButton 的背景色是应用主题的 colorAccent（其实MD中的控件主题默认基本都是应用的这个主题），可以通过app:backgroundTint 属性或者setBackgroundTintList (ColorStateList tint)方法去改变背景颜色。

- FloatingActionButton 的大小尺寸，可以用过app:fabSize 属性设置
- android:src属性改变drawable
- app:rippleColor设置点击button时候的颜色（水波纹效果）
- app:borderWidth设置 button 的边框宽度
- app:elevation设置普通状态阴影的深度（默认是 6dp）
- app:pressedTranslationZ设置点击状态的阴影深度（默认是 12dp）
```
<android.support.design.widget.FloatingActionButton
        android:id="@+id/fab_search"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:layout_alignParentRight="true"
        android:layout_margin="20dp"
        android:clickable="true"
        android:src="@android:drawable/ic_dialog_email"
        app:borderWidth="1dp"
        app:fabSize="normal"
        app:rippleColor="#0000ff"
        app:elevation="3dp"
        app:pressedTranslationZ="6dp"/>
```

###NavigationView###
NavigationView可以和DrawerLayout一起结合使用
![NavigationView](http://img.blog.csdn.net/20160620105959027)

代码：
```
<android.support.v4.widget.DrawerLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/drawer_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fitsSystemWindows="true">
    <android.support.design.widget.NavigationView
        android:id="@+id/navigation_view"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:layout_gravity="start"
        app:headerLayout="@layout/navigation_header"
        app:menu="@menu/drawer" />
</android.support.v4.widget.DrawerLayout>
```
```
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="192dp"
    android:background="?attr/colorPrimaryDark"
    android:gravity="center"
    android:orientation="vertical"
    android:padding="16dp"
    android:theme="@style/ThemeOverlay.AppCompat.Dark">

    <ImageView
        android:layout_width="72dp"
        android:layout_height="72dp"
        android:src="@mipmap/profile"
        app:border_color="@color/primary_light"
        app:border_width="2dp" />

    <TextView
        android:layout_marginTop="10dp"
        android:textSize="18sp"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="APP开发者"/>
</LinearLayout>
```
和普通的menu比，就多了单选属性

```
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    tools:context=".MainActivity">
    <group android:checkableBehavior="single">
        <item
            android:id="@+id/navigation_item_example"
            android:icon="@drawable/ic_favorite"
            android:title="@string/navigation_example" />
        <item
            android:id="@+id/navigation_item_blog"
            android:icon="@drawable/ic_favorite"
            android:title="@string/navigation_my_blog" />
        <item
            android:id="@+id/navigation_item_about"
            android:icon="@drawable/ic_favorite"
            android:title="@string/navigation_about" />
    </group>
</menu>
```

```
navigationView.setNavigationItemSelectedListener(
	new NavigationView.OnNavigationItemSelectedListener() {
		@Override
		public boolean onNavigationItemSelected(MenuItem menuItem) {
			
			return true;
		}
	});
```
在Material Design中，Navigation drawer导航抽屉，被设计用于应用导航，提供了一种通用的导航方式，体现了设计的一致性。
而NavigationView的典型用途就是配合之前v4包的DrawerLayout，作为其中的Drawer部分，即导航菜单的本体部分。NavigationView是一个导航菜单框架，使用menu资源填充数据，使我们可以更简单高效的实现导航菜单。它提供了不错的默认样式、选中项高亮、分组单选、分组子标题、以及可选的Header。

###TextInputLayout###
使用过EditText的同学肯定知道，有一个叫hint的属性，它可以提示用户此处应该输入什么内容，然而当用户输入真实内容之后，hint的提示内容就消失了，用户的体验效果是十分不好的，TextInputLayout的出现解决了这个问题。
*初始状态*
![初始状态](http://img.blog.csdn.net/20160620110606077)

*点击输入内容*
![点击输入内容](http://img.blog.csdn.net/20160620110626328)

当用户在输入的时候hint的内容就会跑到输入内容的上边去，并且是以动画的形式，体验非常好。其中TextInputLayout中字体的颜色是style文件中的colorAccent。

**看下代码：**

```
<android.support.design.widget.TextInputLayout
	android:id="@+id/til"
	android:layout_width="match_parent"
	android:layout_height="wrap_content"
	app:errorEnabled="true">

	<EditText
		android:id="@+id/et"
		android:layout_width="match_parent"
		android:layout_height="wrap_content"
		android:hint="Password"/>
</android.support.design.widget.TextInputLayout>


textLayout = (TextInputLayout)findViewById(R.id.til);
textLayout.setHint("Password");
```
非常简单的使用方法，只是在EditText外再套一层TextInputLayout。

**错误提示**
TextInputLayout还有一个挺不错的设计，就是错误提示，当用户输入内容不正确时，可以在底部弹出一个错误提示。要使用错误提示，需要先设置errorEnable为true。

```
textInputLayout.setErrorEnabled(true);
app:errorEnable=”true”
```
然后调用setError(“输入错误”);
![错误提示](http://img.blog.csdn.net/20160620110854266)

**设置样式**

```
<style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
    <item name="colorAccent">#3498db</item>
</style>
```

###TabLayout###
我们在使用viewpager的时候，经常会使用TabPageIndicator来与其配合。达到很漂亮的滑动效果。但是TabPageIndicator是第三方的，而且比较老了，当然了现在很多大神都已经开始自己写TabPageIndicator来满足需求，在2015年的google大会上，新的Android Support Design库就包含了TabLayout，它可以实现TabPageIndicator的效果，而且兼容性非常好，最低可以兼容到2.2，32个赞不能再少了。
![tablayout](http://img.blog.csdn.net/20160620111028235)
**XML代码**

```
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"  
    android:layout_width="match_parent"  
    android:layout_height="match_parent"  
    xmlns:app="http://schemas.android.com/apk/res-auto"  
    android:orientation="vertical">  

    <android.support.design.widget.TabLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@color/titleBlue"
        app:tabIndicatorColor="@color/white"
        app:tabSelectedTextColor="@color/gray"
        app:tabTextColor="@color/white" />

    <android.support.v4.view.ViewPager
        android:layout_width="fill_parent"
        android:layout_height="0dp"
        android:layout_weight="1" />
</LinearLayout>
```
**java代码**

```
TabLayout tabLayout;
tabLayout = (TabLayout)view.findViewById(R.id.tab_FindFragment_title);
tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
tabLayout.setTabMode(TabLayout.MODE_FIXED);
tabLayout.addTab(tabLayout.newTab().setText("Tab1"));
tabLayout.addTab(tabLayout.newTab().setText("Tab2"));
tabLayout.addTab(tabLayout.newTab().setText("Tab3"));
tabLayout.addTab(tabLayout.newTab().setText("Tab4"));

viewPager.stAdapter(adapter);
tabLayout.setViewPager(viewPager);
//tabLayout.setupWithViewPager(viewPager);
```
**属性**

```
tabLayout.setTabMode(TabLayout.MODE_FIXED);
```
**MODE_FIXED：**固定模式，每一个Tab等宽，不管多少都显示全部。
![MODE_FIXED：](http://img.blog.csdn.net/20160620111340291)
**MODE_SCROLLABLE**：滚动模式，适合多Tab，长短不一
![MODE_SCROLLABLE](http://img.blog.csdn.net/20160620111423542)

```
tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
```

**GRAVITY_CENTER：**居中显示
![GRAVITY_CENTER](http://img.blog.csdn.net/20160620111516168)
**GRAVITY_FILL：**充满整个宽度
![GRAVITY_FILL](http://img.blog.csdn.net/20160620111543247)

**setIcon：** tab.setIcon(R.drawable.ic_1);
![setIcon](http://img.blog.csdn.net/20160620111624362)
要icon横向显示，可以使用**setCustomView**
`tab.setCustomView(getCustomView(index));`
![setCustomView](http://img.blog.csdn.net/20160620111710077)

***精卫填坑***
**tabLayout.setupWithViewPager(viewPager);**
看源码，这个方法中调用了`setTabsFromPagerAdapter(adapter);`

```
public void setTabsFromPagerAdapter(@NonNull PagerAdapter adapter) {
  removeAllTabs();
  for (int i = 0, count = adapter.getCount(); i < count; i++) {
      addTab(newTab().setText(adapter.getPageTitle(i)));
  }
}
```
`removeAllTabs()`这个就是说把前面所有tablayout添加的view都删掉。也就是说在之前不管怎么处理view都被干掉。然后设置为PagerAdapter返回的title。
![错误代码](http://img.blog.csdn.net/20160620112120286)
```
tabLayout.addTab(tab1);
tabLayout.addTab(tab2);
tabLayout.addTab(tab3);
tabLayout.addTab(tab4);
tabLayout.setupWithViewPager(viewPager);
```
![正确代码](http://img.blog.csdn.net/20160620112140896)

```
tabLayout.setupWithViewPager(viewPager);
tabLayout.addTab(tab1);
tabLayout.addTab(tab2);
tabLayout.addTab(tab3);
tabLayout.addTab(tab4);
```
通过Adapter返回title

```
@Override
public CharSequence getPageTitle(int position) {
    return “xxx”;
}
```
###Notification###
Notification作为一个时间触发性的交互提示接口，让我们获得消息的时候，在状态栏，锁屏界面显示相应的消息

Google在Android5.X中，又进一步的改进了通知栏，优化了Notification，在5.X设备上一个标准的通知是这样的，长按会显示消息的来源。
![Notification](http://img.blog.csdn.net/20160620112327633)

> - 基本的Notification
顶部显示横条
- 折叠式Notification
下拉时显示扩展模式，高度变高
- 悬挂式Notification
在顶部悬浮显示，提示性更强
- 显示等级的Notification
	**VISIBILITY_PUBLIC** 只有在没有锁屏时会显示通知
	**VISIBILITY_PRIVATE** 任何情况都会显示通知
	**VISIBILITY_SECRET** 在安全锁和没有锁屏的情况下显示通知

**RecyclerView**
[这篇文章已经写的很好了，不需要再做任何总结了。](http://blog.csdn.net/lmj623565791/article/details/45059587)

---
##动画
###触摸反馈###
触摸反馈是很有必要的，是对用于操作的反馈确认，通常就是颜色的改变，border的改变。
常规的做法是用两个drawable切换（图片或者xml），Material提供了一种更加优雅、更加符合Material风格的方式，就是水波纹效果ripple。
![触摸反馈](http://img.blog.csdn.net/20160620113041939)

```
//波纹有边界
android:background="?android:attr/selectableItemBackground"
//波纹无边界
android:background="?android:attr/selectableItemBackgroundBorderless"
```
*自定义Ripple*

```
<?xml version="1.0" encoding="utf-8"?>
<ripple xmlns:android="http://schemas.android.com/apk/res/android"
        android:color="@android:color/holo_red_dark">
    <item>
        <shape android:shape="rectangle">
            <solid android:color="@color/colorPrimary" />
        </shape>
    </item>
</ripple>
```
揭露效果：

```
<ImageView
        android:id="@+id/rect"
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:layout_marginTop="30dp"
        android:src="@mipmap/ic_launcher"  />

rect =  (ImageView) findViewById(R.id.rect);

Animator animator2 = ViewAnimationUtils.createCircularReveal(rect, 0, 0, 0, (float) Math.hypot(rect.getWidth(), rect.getHeight()));
animator2.setDuration(2000);
animator2.start();
```
###Activity过渡动画###
Android的activity跳转动画只是很生硬的切换，即使通过 overridePendingTransition( int inId, int outId)这个方法来给Activity增加一些切换动画，效果也只是差强人意，而在Android5.X中，Google对动画效果进行了更深一步的诠释，为Activity的转场效果设计了更加丰富的动画效果

- **Android5.X提供了三种Transition类型**
 进入：一个进入的过渡动画决定Activity中的所有视图怎么进入屏幕
退出：一个退出的过渡动画决定Activity中的所有视图怎么退出屏幕
共享元素：一个共享元素过渡动画决定两个Activity之间的过渡，怎么共享他们的视图

	*进人和退出效果包括*
explode(分解)一一从屏幕中间进或出,移动视图
slide(滑动)——从屏幕边缘进或出，移动视图
fade(淡出) 一一通过改变屏幕上视图的不透明度达到添加或者移除视图

	*共享元素包括*
changeBounds——改变目标视图的布局边界
changeCliBounds——裁剪目标视图边界
changeTransfrom——改变目标视图的缩放比例和旋转角度
changeImageTransfrom——改变目标图片的大小和缩放比例
可以发现,在Android5.X上，动画效果的种类变得更加丰富了

	首先来看看普通的三种Activity过渡动画， 要使用这些动画非常简单，例如从ActivityA转到ActivityB，只需要在ActivityA中将基本的startActivity(intent)方法改为如下代码即可
`startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());`
而在AchvityB中，只需要设置下如下所示代码
`getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);`	
或者在样式文件中设置如下所示代码
`<item name="android:windowContentTransitions">true</item>`

	那么接下来就可以设置进人/退出ActivityB的具体的动画效果了， 代码如下所示
	```
getWindow().setEnterTransition(new Explode());
getWindow().setEnterTransition(new Slide());
getWindow().setEnterTransition(new Fade());
	```
	要想在程序中使用共享元素的动画效果也很简单，首先需要在他的activity1布局中设置共享元素，增加元素代码`android:transitionName="XXX"`
	同时在activity2中，也增加一个相应的共享元素属性，如果只要一个共享元素，那么在activity1中可以这样写
	```
startActivity(intent, 
ActivityOptions.makeSceneTransitionAnimation(this, view, "share").toBundle());
	```
使用的参数就是前面普通动画的基础上增加了共享的的view和前面取的名字，如果由多个共享元素，那么我们可以通过`Pair.create()`来创建多个共享元素。

	```
startActivity(intent,
ActivityOptions.makeSceneTransitionAnimation(this, Pair.create(view,"share"),Pair.create(fab,"fab")).toBundle());
	```
- **View state changes Animation（视图状态改变动画）**
StaetListAnimator作为视图改变时的动画效果，通常会使用Seletor来进行设置，但是以前我们设置Seletor的时候，通常是修改他的背景来达到反馈的效果，但是再现在Android5.X中有更高端的方法，可以使用动画来实现。

```
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:state_pressed="true">
        <set>
            <objectAnimator
android:duration="2000"
android:property="rotationX"
android:valueTo="360"
android:valuyeType="floatType" />
        </set>
    </item>

    <item android:state_pressed="false">
        <set>
            <objectAnimator
android:duration="2000"
android:property="rotationX"
android:valueTo="0"
android:valuyeType="floatType" />
        </set>
    </item>
</selector>

<Button
	android:layout_width="200dp"
	android:layout_height="200dp"
	android:background="@drawable/animatorstate" />
```

- **曲线运动**
Material design中的动画依靠曲线，这个曲线适用于时间插值器和控件运动模式。 
PathInterpolator类是一个基于贝塞尔曲线(Bézier curve)或路径(Path)对象上的新的插值器。 
在materialdesign规范中，系统提供了三个基本的曲线： 
>@interpolator/fast_out_linear_in.xml 
@interpolator/fast_out_slow_in.xml 
@interpolator/linear_out_slow_in.xml 

	你可以传递一个PathInterpolator对象给Animator.setInterpolator()方法。 
ObjectAnimator类有了新的构造方法，使你能够一次能同时使用两个或多个属性去绘制动画的路径。例如，下面的动画使用一个Path对象进行视图X和Y属性的动画绘制：

	```
ObjectAnimator mAnimator;  
mAnimator = ObjectAnimator.ofFloat(view, View.X, View.Y, path);  
...
mAnimator.start();
	```


-----

**未完待续**
