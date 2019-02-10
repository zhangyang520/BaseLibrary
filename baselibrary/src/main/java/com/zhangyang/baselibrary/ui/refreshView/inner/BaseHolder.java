package com.zhangyang.baselibrary.ui.refreshView.inner;

import android.app.Activity;
import android.view.View;

public abstract class BaseHolder<Data>  {
	private int type;
	private View contentView;
	private Data data;
	private int position;
	public BaseHolder(){
		contentView=initView();
		contentView.setTag(this);
	}
	
	public BaseHolder(int type){
		this.type=type;
		contentView=initView();
		contentView.setTag(this);
	}
	/** 初始化view*/
	public  abstract View initView();
	public View getContentView() {
		return contentView;
	}
	public void setData(Data data,Activity activity){
		this.data=data;
		refreshView(data,activity);
	}
	
	public void setData(Data data,int position,Activity activity){
		this.data=data;
		this.position=position;
		refreshView(data,activity);
	}
	/** 刷新数据*/
	public abstract void refreshView(Data data,Activity activity);
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * @return the data
	 */
	public Data getData() {
		return data;
	}
	
	
}
