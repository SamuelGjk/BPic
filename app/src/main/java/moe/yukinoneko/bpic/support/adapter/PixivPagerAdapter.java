/*
 * Copyright 2015 SamuelGjk <samuel.alva@outlook.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package moe.yukinoneko.bpic.support.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import moe.yukinoneko.bpic.support.adapter.base.BasePagerAdapter;
import moe.yukinoneko.bpic.ui.fragment.PixivNormalFragment;


/**
 * Created by Samuel.Alva on 2015/5/6.
 */
public class PixivPagerAdapter extends BasePagerAdapter {
    public PixivPagerAdapter(FragmentManager fm) {
        super(fm);
        titles = new String[]{"全年龄（大概）"};
    }

    @Override
    protected Fragment createItem(int position) {
        switch (position) {
            case 0:
                return new PixivNormalFragment();

            default:
                return null;
        }
    }
}
