package com.happy.samuelalva.bcykari.support;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/4/16.
 */
public interface Constants {
    String BASE_API = "http://bcy.net";
    String ILLUST_API = BASE_API + "/illust";
    String ILLUST_TOP_POST_100 = ILLUST_API + "/toppost100";
    String ALL_ART_WORK = ILLUST_API + "/allartwork?&p=";

    String COSER_API = BASE_API + "/coser";
    String COSER_TOP_POST_100 = COSER_API + "/toppost100";
    String ALL_WORK = COSER_API + "/allwork?&p=";

    Pattern COVER_PATTERN = Pattern.compile("http://img[0-9].bcyimg.com/drawer/[0-9]+/cover/\\w+/\\w+(\\.jpg|\\.png|\\.jpeg|\\.gif|\\w)");
    Pattern HD_COVER_PATTERN = Pattern.compile("http://img[0-9].bcyimg.com/drawer/[0-9]+/post/\\w+/\\w+(\\.jpg|\\.gif|\\.jpeg|\\.png|\\w)");
    Pattern AUTHOR_PATTERN = Pattern.compile("<a class=\"work-thumbnail__author\" href=\"/u/\\d+\" target=\"_blank\">[\\s\\S]+?</a>");
    Pattern AVATAR_PATTERN = Pattern.compile("(http://user.bcyimg.com/Public/Upload/avatar/\\S+/middle(\\.jpg|\\.png|\\.jpeg|\\.gif)|/Public/Image/user_pic_middle.gif)");
    Pattern ILLUST_DETAIL_PATTERN = Pattern.compile("/illust/detail/\\d+/\\d+");
    Pattern TOTAL_PAGE_PATTERN = Pattern.compile("<span>共\\d+篇</span>");

    Pattern COSER_PATTERN = Pattern.compile("http://img[0-9].bcyimg.com/coser/[0-9]+/post/\\w+/\\w+(\\.jpg|\\.png|\\.jpeg|\\.gif|\\w)");
    Pattern HD_COSER_PATTERN = Pattern.compile("http://img[0-9].bcyimg.com/coser/[0-9]+/post/[0-9]\\w+/\\w+(\\.jpg|\\.png|\\.jpeg|\\.gif|\\w)/w650");
    Pattern COSER_DETAIL_PATTERN = Pattern.compile("/coser/detail/\\d+/\\d+");
}
