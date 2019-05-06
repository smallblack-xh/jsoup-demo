package com.linkk.jsoupdemo.client;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author: linkk
 */
public class JsoupDemo1 {

    public final static String userAgent = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.75 Safari/537.36";

    public static void test1() throws IOException {
        /*旧版笔趣阁小说爬取方式*/
        String url = "http://www.xbiquge.la/13/13959/";
        Document document = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.75 Safari/537.36").timeout(500000).get();
        String bookName = document.getElementById("info").select("h1").text();
        System.out.println(bookName);
        Elements select = document.select("#list dd a");
        for(Element element:select){
            String href = element.attr("href");
            System.out.println("http://www.xbiquge.la"+href);
            String text = element.text();
            System.out.println(text);
        }
    }

    public static void test2() throws IOException {
        /*纵横小说网查询爬取小说方式*/
        String keyword = "圣墟";
        keyword = URLEncoder.encode(keyword,"UTF-8");
        System.out.println(keyword);
        /*%E5%89%91%E6%9D%A5*/
        String baseUrl = "http://book.zongheng.com/showchapter/";
        String url = "http://search.zongheng.com/s?keyword="+keyword;
        Document document = Jsoup.connect(url).userAgent(userAgent).timeout(500000).get();

        Elements select = document.select("div.fl.se-result-infos a");
        String novelUrl = null;
        for(Element element:select){
            String text = element.text();
            if(URLDecoder.decode(keyword,"UTF-8").equals(text)){
                novelUrl = element.attr("href");
            }else {
                System.out.println("@@@@@@@@没有找到对应的小说");
                return;
            }
        }
        if (novelUrl != null) {
            novelUrl = novelUrl.substring(novelUrl.lastIndexOf("/", novelUrl.length()));
            novelUrl = baseUrl+novelUrl;
        }
        Document novelIndex = Jsoup.connect(novelUrl).userAgent(userAgent).timeout(500000).get();
        Elements novelLinks = novelIndex.select(".chapter-list li a");
//        System.out.println(novelLinks);
//        for (Element element:novelLinks){
//            System.out.println(element.text()+" #### "+element.attr("href"));
//        }
        Document content = Jsoup.connect(novelLinks.get(1).attr("href")).userAgent(userAgent).timeout(500000).get();
        System.out.println(content.select(".content p"));
    }


    public static void test3() throws IOException {
        /*新笔趣阁小说查询爬取方式*/
        String searchUrl = "https://www.xbiquge6.com/search.php?keyword=";
        String baseUrl = "https://www.xbiquge6.com";
        String keyword = "仙宫";
        /*新笔趣阁字段查询不需要编码加密*/
//        keyword = URLEncoder.encode(keyword,"UTF-8");
        searchUrl = searchUrl+keyword;
        System.out.println(searchUrl);
        Document document = Jsoup.connect(searchUrl).userAgent(userAgent).timeout(500000).get();
        Elements select = document.select(".result-game-item-pic a");
        String novelUrl = select.attr("href");
        Document chapters = Jsoup.connect(novelUrl).userAgent(userAgent).get();
        Elements elements = chapters.select("#list dd a");
        for(Element element:elements){
            System.out.println(baseUrl+element.attr("href")+" #### "+element.text());
        }
    }

    public static void test4() throws IOException {
        /*起点中文网小说爬取方式*/
        String searchUrl = "https://www.qidian.com/search?kw=";
        String keyword = "仙宫";
//        keyword = URLEncoder.encode(keyword,"UTF-8");
        Document document = Jsoup.connect(searchUrl + keyword).userAgent(userAgent).timeout(500000).get();
        Elements select = document.select(".book-mid-info a");
        Document document1 = Jsoup.connect("https:"+select.get(0).attr("href")).userAgent(userAgent).timeout(500000).get();
        Elements select1 = document1.select(".cf li a[data-cid]");
        for(Element element:select1){
            if(element.attr("href").contains("vipreader")){
                System.out.println("VIP不可读");
                return;
            }else {
                System.out.println("https:"+element.attr("href")+" #### "+element.text());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        test3();
    }
}
