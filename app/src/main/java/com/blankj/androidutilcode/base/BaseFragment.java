package com.blankj.androidutilcode.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2017/03/28
 *     desc  : Fragment－v4 基类
 * </pre>
 */
public abstract class BaseFragment extends Fragment
        implements IBaseView {

    private static final String TAG                  = "BaseFragment";
    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";

    protected View         contentView;
    protected BaseActivity mActivity;

    private long lastClick = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commitAllowingStateLoss();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        contentView = inflater.inflate(bindLayout(), null);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        initData(bundle);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: ");
        super.onActivityCreated(savedInstanceState);
        mActivity = (BaseActivity) getActivity();
        initView(savedInstanceState, contentView);
        doBusiness();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView: ");
        if (contentView != null) {
            ((ViewGroup) contentView.getParent()).removeView(contentView);
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }

    private boolean isFastClick() {
        long now = System.currentTimeMillis();
        if (now - lastClick >= 200) {
            lastClick = now;
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        if (!isFastClick()) onWidgetClick(view);
    }
}
