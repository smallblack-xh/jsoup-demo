package com.linkk.jsoupdemo.client;

import com.linkk.jsoupdemo.utils.Link;
import com.linkk.jsoupdemo.utils.PrintUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 不同网站的小说的api接口
 * @Author: Administrator
 * @Date: 2019/5/11
 * @Package: com.linkk.jsoupdemo.client
 * @Name: NovelClient
 **/
public class NovelsClient {
    public final static String userAgent = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.75 Safari/537.36";

    public static List<Link> oldBiquge() throws IOException {
        /*旧版笔趣阁小说爬取方式 旧的笔趣阁没有可以搜索的方法 so使用时除非是特定的URL，否则基本没用途*/
        String url = "http://www.xbiquge.la/13/13959/";
        String baseUrl = "http://www.xbiquge.la";
        Document document = Jsoup.connect(url).userAgent(userAgent).timeout(500000).get();
        String bookName = document.getElementById("info").select("h1").text();
        System.out.println(bookName);
        Elements select = document.select("#list dd a");
        List<Link> links = new ArrayList<>();
        for(Element element:select){
            String href = baseUrl+element.attr("href");
            String text = element.text();
            Link link = new Link(text,href);
            links.add(link);
        }
        PrintUtils.printLinks(links);
        return links;
    }

    public static List<Link> zongheng(String keyword) throws IOException {
        /*纵横小说网查询爬取小说方式 可搜索*/
        List<Link> links = new ArrayList<>();
        keyword = URLEncoder.encode(keyword,"UTF-8");
        System.out.println(keyword);
        /*%E5%89%91%E6%9D%A5*/
        String baseUrl = "http://book.zongheng.com/showchapter/";
        String url = "http://search.zongheng.com/s?keyword="+keyword;
        Document document = Jsoup.connect(url).userAgent(userAgent).timeout(500000).get();
        Elements select = document.select("div.fl.se-result-infos .tit a");
        String novelUrl = null;
        for(Element element:select){
            String text = element.text();
            if(URLDecoder.decode(keyword,"UTF-8").equals(text)){
                novelUrl = element.attr("href");
            }
        }
        if(novelUrl==null){
                System.out.println("@@@@@@@@没有找到对应的小说");
                return links;
        }
        if (novelUrl != null) {
            novelUrl = novelUrl.substring(novelUrl.lastIndexOf("/", novelUrl.length()));
            novelUrl = baseUrl+novelUrl;
        }
        Document novelIndex = Jsoup.connect(novelUrl).userAgent(userAgent).timeout(500000).get();
        Elements novelLinks = novelIndex.select(".chapter-list li a");
        System.out.println(novelLinks);
        for (Element element:novelLinks){
            links.add(new Link(element.text(),element.attr("href")));
        }
//        Document content = Jsoup.connect(novelLinks.get(1).attr("href")).userAgent(userAgent).timeout(500000).get();
//        System.out.println(content.select(".content p"));
        PrintUtils.printLinks(links);

        return links;
    }

    public static List<Link> newBiquge(String keyword) throws IOException {
        /*新笔趣阁小说查询爬取方式*/
        List<Link> links = new ArrayList<>();
        String searchUrl = "https://www.xbiquge6.com/search.php?keyword=";
        String baseUrl = "https://www.xbiquge6.com";
        /*新笔趣阁字段查询不需要编码加密*/
        searchUrl = searchUrl+keyword;
        System.out.println(searchUrl);
        Document document = Jsoup.connect(searchUrl).userAgent(userAgent).timeout(500000).get();
        Elements select = document.select(".result-game-item-pic a");
        String novelUrl = select.attr("href");
        Document chapters = Jsoup.connect(novelUrl).userAgent(userAgent).get();
        Elements elements = chapters.select("#list dd a");
        for(Element element:elements){
            links.add(new Link(element.text(),baseUrl+element.attr("href")));
        }
        PrintUtils.printLinks(links);

        return links;
    }

    public static List<Link> qidian(String keyword) throws IOException {
        /*起点中文网小说爬取方式 目前不可用，涉及隐藏域问题*/
        List<Link> links = new ArrayList<>();
        String searchUrl = "https://www.qidian.com/search?kw=";
//        keyword = URLEncoder.encode(keyword,"UTF-8");
        Document document = Jsoup.connect(searchUrl + keyword).userAgent(userAgent).timeout(500000).get();
        Elements select = document.select(".book-mid-info a");
        String novelUrl = null;
        for (Element e:select){
            if(e.text().equals(keyword)){
                novelUrl = "https:"+e.attr("href");
            }
        }
        Document document1 = Jsoup.connect(novelUrl).userAgent(userAgent).timeout(500000).get();
        document1.getElementById("j-catalogWrap");
        System.out.println(document1);
        Elements select1 = document1.select(".cf li a[data-cid]");
        System.out.println(select1);
        return links;
    }
}
