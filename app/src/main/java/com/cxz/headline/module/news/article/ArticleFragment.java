package com.cxz.headline.module.news.article;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.cxz.headline.R;
import com.cxz.headline.app.App;
import com.cxz.headline.base.BaseFragment;
import com.cxz.headline.di.component.DaggerArticleFragmentComponent;
import com.cxz.headline.di.module.ArticleFragmentModule;
import com.cxz.headline.util.TimeUtil;

import butterknife.BindView;

/**
 * Created by chenxz on 2017/12/10.
 */

public class ArticleFragment extends BaseFragment<ArticlePresenter> implements ArticleContract.View {

    private static String CATEGORY_ID = "categoryId";

//    @BindView(R.id.recycler_view)
//    RecyclerView mRecyclerView;
//    @BindView(R.id.refresh_layout)
//    SwipeRefreshLayout mSwipeRefreshLayout;

    private String categoryId;

    public static ArticleFragment newInstance(String categoryId) {
        ArticleFragment fragment = new ArticleFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CATEGORY_ID, categoryId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showErrorMsg(String msg) {

    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_article;
    }

    @Override
    protected void initInject() {
        DaggerArticleFragmentComponent.builder()
                .appComponent(App.getAppComponent())
                .articleFragmentModule(new ArticleFragmentModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        categoryId = getArguments().getString(CATEGORY_ID, "");
        if (categoryId.equals("news_hot")) {
            Log.e(TAG, "initView: " + categoryId);
            mPresenter.loadNewsArticleList(categoryId, TimeUtil.getCurrentTimeStamp());
        }
    }

    @Override
    public void updateNewsArticleList() {

    }
}