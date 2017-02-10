package com.asionbo.coolweather.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;

import com.asionbo.coolweather.R;

public class PullRefreshView extends ListView {

	private View headerView;
	private ImageView ivRefresh;
	private int headerViewHeight;
	private int downY;//按下的坐标
	
	private final int PULL_REFRESH = 0;//下拉刷新
	private final int RELEASE_REFRESH = 1;//释放刷新
	private final int REFRESHING = 2;//正在刷新
	private int current_state = PULL_REFRESH;//当前刷洗状态
	private RotateAnimation rotateAnimation;
	
	private OnRefreshListener listener;

	public PullRefreshView(Context context) {
		super(context);
		initUi();
	}
	

	public PullRefreshView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initUi();
	}


	//初始化界面
	private void initUi() {
		initHeaderView();
		initRotateAnimation();
	}

	//初始化动画
	private void initRotateAnimation() {
		rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		rotateAnimation.setDuration(1000);
		rotateAnimation.setRepeatCount(3);
	}


	//初始化头布局
	private void initHeaderView() {
		headerView = View.inflate(getContext(), R.layout.layout_header, null);
		
		ivRefresh = (ImageView) headerView.findViewById(R.id.iv_refresh);
		
		headerView.measure(0, 0);
		headerViewHeight = headerView.getMeasuredHeight();
		headerView.setPadding(0, -headerViewHeight, 0, 0);
		
		addHeaderView(headerView);
	}
	
	//通过触摸位置判断当前状态
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			
			if(current_state == REFRESHING){
				break;
			}
			
			int deltaY = (int) (ev.getY() - downY);//手指移动距离
			
			int paddingTop = -headerViewHeight + deltaY;
			if(paddingTop > -headerViewHeight && getFirstVisiblePosition() == 0){//移动时
				headerView.setPadding(0, paddingTop, 0, 0);
				
				if(paddingTop >= 0 && current_state == PULL_REFRESH){
					//从下拉刷新进入释放刷新，界面完全显示
					current_state = RELEASE_REFRESH;
					refreshViewByState();
				}else if(paddingTop < 0 && current_state == RELEASE_REFRESH){
					//从释放刷新进入下拉刷新，界面出来一部分
					current_state = PULL_REFRESH;
					refreshViewByState();
				}
				return true;
			}
			break;

		case MotionEvent.ACTION_UP:
			if(current_state == PULL_REFRESH){
				headerView.setPadding(0, -headerViewHeight, 0, 0);
			}else if(current_state == RELEASE_REFRESH){
				headerView.setPadding(0, 0, 0, 0);
				current_state = REFRESHING;
				refreshViewByState();
				
				if(listener != null){//让外部可以在这里调用,用来刷新数据
					listener.onPullRefresh();
				}
			}
			break;
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}


	/**
	 * 根据状态刷新view
	 */
	private void refreshViewByState() {
		switch (current_state) {
		case PULL_REFRESH://下拉刷新
			
			break;
		case REFRESHING:
			ivRefresh.setAnimation(rotateAnimation);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 完成刷新，重置状态
	 */
	public void completeRefresh(){
		headerView.setPadding(0, -headerViewHeight, 0, 0);
		current_state = PULL_REFRESH;
	}
	
	public void setOnRefreshListener(OnRefreshListener listener){
		this.listener = listener;
	}
	
	public interface OnRefreshListener{
		void onPullRefresh();
	}

}
