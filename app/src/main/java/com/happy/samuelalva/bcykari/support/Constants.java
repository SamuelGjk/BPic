package com.happy.samuelalva.bcykari.support;

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
