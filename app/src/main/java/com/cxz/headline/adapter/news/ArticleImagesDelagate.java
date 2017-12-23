package com.cxz.headline.adapter.news;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cxz.headline.R;
import com.cxz.headline.bean.news.NewsMultiArticleDataBean;
import com.cxz.headline.util.ShareUtil;
import com.cxz.headline.util.TimeUtil;
import com.cxz.headline.util.imageloader.ImageLoader;
import com.cxz.headline.util.imageloader.ImageOptions;
import com.cxz.headline.util.imageloader.glide.GlideImageOptions;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenxz on 2017/12/23.
 */

public class ArticleImagesDelagate implements ItemViewDelegate<NewsMultiArticleDataBean> {

    private Context mContext;
    private ImageOptions options;

    public ArticleImagesDelagate(Context context) {
        this.mContext = context;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_article_list_images;
    }

    @Override
    public boolean isForViewType(NewsMultiArticleDataBean item, int position) {
        return item.isHas_image();
    }

    @Override
    public void convert(ViewHolder holder, final NewsMultiArticleDataBean bean, int position) {
        final String title = bean.getTitle();
        String extra = bean.getSource() + " " + bean.getComment_count() + "评论"
                + " " + TimeUtil.getTimeStampAgo(String.valueOf(bean.getBehot_time()));
        holder.setText(R.id.tv_title, title)
                .setText(R.id.tv_extra, extra);

        if (bean.getUser_info() != null && bean.getUser_info().getAvatar_url() != null) {
            ImageView iv_avatar = holder.getView(R.id.iv_avatar);
            options = GlideImageOptions.builder()
                    .url(bean.getUser_info().getAvatar_url())
                    .placeholder(R.mipmap.ic_default_avatar)
                    .imageView(iv_avatar)
                    .build();
            ImageLoader.getInstance().loadImage(mContext, options);
        }

        if (bean.getImage_list() != null && bean.getImage_list().size() > 0) {
            NewsMultiArticleDataBean.ImageListBean imageBean;
            List<ImageInfo> imageInfos = new ArrayList<>();
            ImageInfo imageInfo = new ImageInfo();
            for (int i = 0; i < bean.getImage_list().size(); i++) {
                imageBean = bean.getImage_list().get(i);
                imageInfo.thumbnailUrl = imageBean.getUrl();
                //imageInfo.bigImageUrl = imageBean.getUrl_list().get(i).getUrlX();
                //imageInfo.imageViewWidth = imageBean.getWidth();
                //imageInfo.imageViewHeight = imageBean.getHeight();
                imageInfos.add(imageInfo);
            }
            NineGridView nineGridView = holder.getView(R.id.nineGrid);
            nineGridView.setAdapter(new NineGridViewClickAdapter(mContext, imageInfos));
            NineGridView.setImageLoader(new NineGridView.ImageLoader() {
                @Override
                public void onDisplayImage(Context context, ImageView imageView, String url) {
                    options = GlideImageOptions.builder()
                            .url(url)
                            .imageView(imageView)
                            .build();
                    ImageLoader.getInstance().loadImage(mContext, options);
                }

                @Override
                public Bitmap getCacheImage(String url) {
                    return null;
                }
            });
        }

        final ImageView iv_dots = holder.getView(R.id.iv_dots);
        iv_dots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(mContext, iv_dots, Gravity.END, 0, R.style.MyPopupMenu);
                popupMenu.inflate(R.menu.menu_share);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();
                        if (itemId == R.id.action_share) {
                            ShareUtil.send(mContext, title + "\n" + bean.getShare_url());
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }
}
