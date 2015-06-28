package com.happy.samuelalva.bcykari.support;

/**
 * Created by Samuel.Alva on 2015/4/16.
 */
public class Constants {

    public static final String BASE_API_BCY = "http://bcy.net";
    public static final String ILLUST_API = BASE_API_BCY + "/illust";
    public static final String ILLUST_TOP_POST_100 = ILLUST_API + "/toppost100";
    public static final String ALL_ART_WORK = ILLUST_API + "/allartwork?&p=";

    public static final String COSER_API = BASE_API_BCY + "/coser";
    public static final String COSER_TOP_POST_100 = COSER_API + "/toppost100";
    public static final String ALL_WORK = COSER_API + "/allwork?&p=";

    public static final String BASE_API_PIXIV = "http://www.pixiv.net/";
    public static final String BASE_RANKING_API_PIXIV = BASE_API_PIXIV + "ranking.php";
    public static final String DAILY_ILLUST_RANKING_PIXIV = BASE_RANKING_API_PIXIV + "?mode=daily&content=illust";

    public static String getPixivDatlyIllustRankingApi(String date) {
        return DAILY_ILLUST_RANKING_PIXIV + "&date=" + date + "&p=";
    }

}
