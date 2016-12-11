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

		//��ʼ������View����
		initViews();

		//Model����
		initData();

		//Controller������
		initAdapter();

		//������ѯ
		//����һ���µ��߳�
		new Thread(){
			@Override
			public void run() {
				while (!isUIVisiable) {
					try{
						Thread.sleep(2000);
					}catch(Exception e){
						e.printStackTrace();
					}
					//����ֱ���������UI
					//��ǰλ��������һλ(��ȡ��ǰλ�ü�һ����)
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// ����UI
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
		//����������
		viewPager.setAdapter(new MyAdapter());

		//Ĭ�����õ��м��ĳ��λ��
		//	int pos = Integer.MAX_VALUE/2-(Integer.MAX_VALUE/2%imageViewsList.size());
		//2147483647/2=1073741823-��1073741823%5��
		viewPager.setCurrentItem(5000000);//���õ�ĳ��λ��

	}
	class MyAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			//return imageViewsList.size();
			return Integer.MAX_VALUE;
		}
		//3.����ָ�����õ��ж��߼����̶�д��
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// �������µ���Ŀʱ���ַ�������view�Ƿ���Ա�����
			//�����жϹ���
			return arg0==arg1;
		}
		//1.����Ҫ��ʾ����Ŀ����,������Ŀ
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Log.i("SRL","instantiateItem��������ʼ��"+position);
			//container  ����:viewPager
			//position)  ��ǰҪ��ʾ��Ŀ��λ��
			int newPosition =position%imageViewsList.size();
			ImageView imageView=imageViewsList.get(newPosition);
			//a.��View������ӵ�container��
			container.addView(imageView);
			//b.��view���󷵻ظ���ܣ�������
			return imageView;//������д�������쳣
		}
		//2.������Ŀ
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			//object Ҫ���ٵĶ���
			Log.i("SRL","destroyItem����������"+position);
			container.removeView((View)object);

		}

	}
	private void initData() {
		//��ʼ��Ҫ��ʾ������
		//ͼƬ��Դid����
		int[] imagesResIds = new int[]{
				R.drawable.a,
				R.drawable.b,
				R.drawable.c,
				R.drawable.d,
				R.drawable.e			
		};
		//�ı�����
		contentDesc=new String[]{
				"��һ��ͼ",
				"�ڶ���ͼ",
				"������ͼ",
				"������ͼ",
				"������ͼ"
		};


		pointViews = new ArrayList<View>();
		imageViewsList = new ArrayList<ImageView>();
		ImageView imageView;
		View pointView;
		for(int i=0;i<imagesResIds.length;i++){
			imageView = new ImageView(this);
			imageView.setBackgroundResource(imagesResIds[i]);
			imageViewsList.add(imageView);

			//��С�׵㣬ָʾ��
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
		//��ȡ�ؼ�
		viewPager=(ViewPager)findViewById(R.id.viewPager);
		//	viewPager.setOffscreenPageLimit(1);//Ĭ�����Ҹ���ʾһ��ͼƬ������ͨ���޸Ĳ��������޸�
		//���ù�������
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
		// ����״̬�仯ʱ����

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// ����ʱ����

	}

	@Override
	public void onPageSelected(int arg0) {
		// �µ���Ŀ��ѡ��ʱ����
		int newPosition =arg0%imageViewsList.size();
		show.setText(contentDesc[newPosition]);
		//		for(int i=0;i<ll_point_container.getChildCount();i++){
		//			View childAt=ll_point_container.getChildAt(arg0);
		//			childAt.setEnabled(arg0 == i);
		//		}

		//�Ȱ�֮ǰ�Ľ��ã������µ�����
		ll_point_container.getChildAt(lastEnablePoint).setEnabled(false);		
		ll_point_container.getChildAt(newPosition).setEnabled(true);

		lastEnablePoint = newPosition;
	}

}
