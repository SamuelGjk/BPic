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

package moe.yukinoneko.bpic.ui.fragment;

import android.os.Bundle;
import android.view.View;

import moe.yukinoneko.bpic.support.Constants;

/**
 * Created by Samuel.Alva on 2015/5/6.
 */
public class BcyAllWorkFragment extends BcyAbsChildFragment {
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        requestUrl = Constants.ALL_WORK_API_BCY;
        hasAvatar = false;
        super.onViewCreated(view, savedInstanceState);
    }
}
