package com.example.c4_demo2;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;

import java.util.ArrayList;

import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class MainActivity extends Activity implements OnPageChangeListener{
	private ViewPager viewPager;
	private ArrayList<ImageView> imageViewsList;
	private ArrayList<View> pointViews;
	private LinearLayout ll_point_container;
	private TextView show ;
	private String[] contentDesc;
	private int lastEnablePoint = 0;
	boolean isUIVisiable = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//初始化布局View布局
		initViews();

		//Model数据
		initData();

		//Controller控制器
		initAdapter();

		//开启轮询
		//开启一个新的线程
		new Thread(){
			@Override
			public void run() {
				while (!isUIVisiable) {
					try{
						Thread.sleep(2000);
					}catch(Exception e){
						e.printStackTrace();
					}
					//不能直接在这更新UI
					//当前位置往下跳一位(获取当前位置加一即可)
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// 更新UI
							viewPager.setCurrentItem(viewPager.getCurrentItem()+1);

						}
					});
				}
			};
		}.start();

	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isUIVisiable = false;
	}
	private void initAdapter() {
		ll_point_container.getChildAt(0).setEnabled(true);
		show.setText(contentDesc[0]);
		//设置适配器
		viewPager.setAdapter(new MyAdapter());

		//默认设置到中间的某个位置
		//	int pos = Integer.MAX_VALUE/2-(Integer.MAX_VALUE/2%imageViewsList.size());
		//2147483647/2=1073741823-（1073741823%5）
		viewPager.setCurrentItem(5000000);//设置到某个位置

	}
	class MyAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			//return imageViewsList.size();
			return Integer.MAX_VALUE;
		}
		//3.返回指定复用的判断逻辑，固定写法
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// 当滑到新的条目时，又返回来，view是否可以被复用
			//返回判断规则
			return arg0==arg1;
		}
		//1.返回要显示的条目内容,创建条目
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Log.i("SRL","instantiateItem方法，初始化"+position);
			//container  容器:viewPager
			//position)  当前要显示条目的位置
			int newPosition =position%imageViewsList.size();
			ImageView imageView=imageViewsList.get(newPosition);
			//a.把View对象添加到container中
			container.addView(imageView);
			//b.把view对象返回给框架，适配器
			return imageView;//必须重写，否则报异常
		}
		//2.销毁条目
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			//object 要销毁的对象
			Log.i("SRL","destroyItem方法，销毁"+position);
			container.removeView((View)object);

		}

	}
	private void initData() {
		//初始化要显示的数据
		//图片资源id数组
		int[] imagesResIds = new int[]{
				R.drawable.a,
				R.drawable.b,
				R.drawable.c,
				R.drawable.d,
				R.drawable.e			
		};
		//文本描述
		contentDesc=new String[]{
				"第一张图",
				"第二张图",
				"第三张图",
				"第四张图",
				"第五张图"
		};


		pointViews = new ArrayList<View>();
		imageViewsList = new ArrayList<ImageView>();
		ImageView imageView;
		View pointView;
		for(int i=0;i<imagesResIds.length;i++){
			imageView = new ImageView(this);
			imageView.setBackgroundResource(imagesResIds[i]);
			imageViewsList.add(imageView);

			//加小白点，指示器
			pointView=new View(this);
			pointView.setBackgroundResource(R.drawable.selector_bg_point);

			LayoutParams layoutParams = new LinearLayout.LayoutParams(5, 5);
			if(i!=0){
				layoutParams.leftMargin=10;
			}
			pointView.setEnabled(false);
			ll_point_container.addView(pointView,layoutParams);
		}

	}

	private void initViews() {
		//获取控件
		viewPager=(ViewPager)findViewById(R.id.viewPager);
		//	viewPager.setOffscreenPageLimit(1);//默认左右各显示一张图片，可以通过修改参数进行修改
		//设置滚动监听
		viewPager.setOnPageChangeListener(this);
		ll_point_container = (LinearLayout)findViewById(R.id.ll_point);

		show = (TextView)findViewById(R.id.tv_dis);


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// 滚动状态变化时调用

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// 滚动时调用

	}

	@Override
	public void onPageSelected(int arg0) {
		// 新的条目被选中时调用
		int newPosition =arg0%imageViewsList.size();
		show.setText(contentDesc[newPosition]);
		//		for(int i=0;i<ll_point_container.getChildCount();i++){
		//			View childAt=ll_point_container.getChildAt(arg0);
		//			childAt.setEnabled(arg0 == i);
		//		}

		//先把之前的禁用，把最新的启用
		ll_point_container.getChildAt(lastEnablePoint).setEnabled(false);		
		ll_point_container.getChildAt(newPosition).setEnabled(true);

		lastEnablePoint = newPosition;
	}

}
