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

package com.happy.samuelalva.bcykari.support.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.happy.samuelalva.bcykari.support.adapter.base.BasePagerAdapter;
import com.happy.samuelalva.bcykari.ui.fragment.BcyAllArtWorkFragment;
import com.happy.samuelalva.bcykari.ui.fragment.BcyAllFanartFragment;
import com.happy.samuelalva.bcykari.ui.fragment.BcyIllustTopPostFragment;

/**
 * Created by Samuel.Alva on 2015/4/19.
 */
public class BcyIllustPagerAdapter extends BasePagerAdapter {

    public BcyIllustPagerAdapter(FragmentManager fm) {
        super(fm);
        titles = new String[]{"原创", "二次创作", "本周热门"};
    }

    @Override
    protected Fragment createItem(int position) {
        switch (position) {
            case 0:
                return new BcyAllArtWorkFragment();
            case 1:
                return new BcyAllFanartFragment();
            case 2:
                return new BcyIllustTopPostFragment();

            default:
                return null;
        }
    }
}
