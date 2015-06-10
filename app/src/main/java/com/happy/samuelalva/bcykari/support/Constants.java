package com.happy.samuelalva.bcykari.support;

/**
 * Created by Samuel.Alva on 2015/4/16.
 */
public interface Constants {
    String HOST_TYPE = "HOST_TYPE";

    String BASE_API_BCY = "http://bcy.net";
    String ILLUST_API = BASE_API_BCY + "/illust";
    String ILLUST_TOP_POST_100 = ILLUST_API + "/toppost100";
    String ALL_ART_WORK = ILLUST_API + "/allartwork?&p=";

    String COSER_API = BASE_API_BCY + "/coser";
    String COSER_TOP_POST_100 = COSER_API + "/toppost100";
    String ALL_WORK = COSER_API + "/allwork?&p=";

    String BASE_API_PIXIV = "http://www.pixiv.net";
    String BASE_RANKING_API_PIXIV = BASE_API_PIXIV + "/ranking.php";
    String DAILY_ILLUST_RANKING_PIXIV = BASE_RANKING_API_PIXIV + "?mode=daily&content=illust&page=";

}
