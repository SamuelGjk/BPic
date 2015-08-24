package com.happy.samuelalva.bcykari.ui.activity;

import com.happy.samuelalva.bcykari.support.Constants;
import com.happy.samuelalva.bcykari.support.adapter.ImagePagerAdapter;
import com.happy.samuelalva.bcykari.ui.activity.base.BaseImageActivity;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

/**
 * Created by Samuel.Alva on 2015/6/15.
 */
public class PixivImageActivity extends BaseImageActivity {
    @Override
    protected ImagePagerAdapter getAdapter() {
        for (int i = 0; i < urls.size(); i++) {
            urls.set(i, urls.get(i).replace("c/240x480/img-master", "img-original").replace("_master1200", ""));
        }
        return new ImagePagerAdapter(this, urls, new Header[]{new BasicHeader("Referer", Constants.BASE_API_PIXIV)});
    }
}
