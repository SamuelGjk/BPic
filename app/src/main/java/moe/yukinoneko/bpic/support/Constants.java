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

package moe.yukinoneko.bpic.support;

/**
 * Created by Samuel.Alva on 2015/4/16.
 */
public class Constants {
    public static final String BASE_API_BCY = "http://bcy.net";
    public static final String ILLUST_API_BCY = BASE_API_BCY + "/illust";
    public static final String ILLUST_TOP_POST_100_API_BCY = ILLUST_API_BCY + "/toppost100";
    public static final String ALL_ART_WORK_API_BCY = ILLUST_API_BCY + "/allartwork?&p=";
    public static final String ALL_FANART_API_BCY = ILLUST_API_BCY + "/allfanart?&p=";

    public static final String COSER_API_BCY = BASE_API_BCY + "/coser";
    public static final String COSER_TOP_POST_100_API_BCY = COSER_API_BCY + "/toppost100";
    public static final String ALL_WORK_API_BCY = COSER_API_BCY + "/allwork?&p=";

    public static final String BASE_API_PIXIV = "http://www.pixiv.net/";
    public static final String BASE_RANKING_API_PIXIV = BASE_API_PIXIV + "ranking.php";
    public static final String DAILY_ILLUST_RANKING_API_PIXIV = BASE_RANKING_API_PIXIV + "?mode=daily&content=illust";
    public static final String MEMBER_ILLUST_API_PIXIV = "member_illust.php?mode=medium&illust_id=";

    public static String getPixivDatlyIllustRankingApi(String date) {
        return DAILY_ILLUST_RANKING_API_PIXIV + "&date=" + date + "&p=";
    }

}
