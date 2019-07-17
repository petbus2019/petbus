package com.petbus.tj.petbus.ui;

import com.petbus.tj.petbus.ui.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.BaseAdapter;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.AbsListView;
import android.widget.HeaderViewListAdapter;

import java.util.List;
import java.util.ArrayList;

class LoadMoreView extends LinearLayout {

    public LoadMoreView(Context context) {
        this(context, null);
    }

    public LoadMoreView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.listview_loadmore, this);
    }
}


public class actionlistview extends ListView {
    private boolean mIsLoading;
    private boolean mIsPageFinished;
    private View mLoadMoreView;
    private OnScrollListener mUserOnScrollListener;
    private OnPullUpLoadMoreListener mOnPullUpLoadMoreListener;

    public actionlistview(Context context) {
        super(context);
        init();
    }

    public actionlistview(Context context, AttributeSet attrs) { 
        super(context, attrs); 
        init(); 
    } 

    public actionlistview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void reset() {
        mIsLoading = false;
        mIsPageFinished = false;
        startloading();
    }

    private void init() {
        mIsLoading = false;
        mIsPageFinished = false;
        mLoadMoreView = new LoadMoreView(getContext());
        super.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 调用用户设置的OnScrollListener
                if (mUserOnScrollListener != null) {
                    mUserOnScrollListener.onScrollStateChanged(view, scrollState);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // Log.i( "PetBusApp", "actionlistview:onScroll " + firstVisibleItem + " -- "
                //       + visibleItemCount + "type:" + totalItemCount );
                // 调用用户设置的OnScrollListener
                if (mUserOnScrollListener != null) {
                    mUserOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }
                int lastVisibleItem = firstVisibleItem + visibleItemCount;
                if (!mIsLoading && !mIsPageFinished && lastVisibleItem == totalItemCount) {
                    if (mOnPullUpLoadMoreListener != null) {
                        mIsLoading = true;
                        showLoadMoreView();
                        mOnPullUpLoadMoreListener.onPullUpLoadMore();
                    }
                }
            }
        });
    }

    public void startloading(){
        if (!mIsLoading && !mIsPageFinished ) {
            if (mOnPullUpLoadMoreListener != null) {
                mIsLoading = true;
                showLoadMoreView();
                mOnPullUpLoadMoreListener.onPullUpLoadMore();
            }
        }
    }

    public void onFinishLoading(boolean isPageFinished) {
        mIsLoading = false;
        setIsPageFinished(isPageFinished);
    }

    private void setIsPageFinished(boolean isPageFinished) {
        mIsPageFinished = isPageFinished;
        removeFooterView(mLoadMoreView);
    }
    
    private void showLoadMoreView() {
        if (findViewById(R.id.id_load_more_layout) == null) {
            addFooterView(mLoadMoreView);
        }
    }

    public void setOnPullUpLoadMoreListener(OnPullUpLoadMoreListener l) {
        this.mOnPullUpLoadMoreListener = l;
    }

    public interface OnPullUpLoadMoreListener {
        void onPullUpLoadMore();
    }
}
