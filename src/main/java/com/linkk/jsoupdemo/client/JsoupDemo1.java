package com.linkk.jsoupdemo.client;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.UUID;

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
        String keyword = "圣墟";
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
    public static void test5() throws IOException {
        /*顶点小说爬取方式 编码方式不确定 暂时搁置，有用户爬取限制*/
        String searchUrl = "https://www.x23us.com/modules/article/search.php?searchtype=keywords&searchkey=";
        String keyword = "元尊";
        String submit = "搜索";
        searchUrl =searchUrl+URLEncoder.encode(keyword,"gb2312");
        searchUrl = searchUrl+"&action=login&submit="+URLEncoder.encode(submit,"gb2312");
        System.out.println(searchUrl);
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.131 Safari/537.36";
        Document document = Jsoup.connect(searchUrl).userAgent(userAgent).timeout(500000).method(Connection.Method.GET).followRedirects(true).maxBodySize(0).get();
        System.out.println(document);
//        String url = "https://www.x23us.com/book/65650";
//        Document document = Jsoup.connect(url).userAgent(userAgent).timeout(500000).get();
//        Elements select = document.select(".hst");
//        String novelUrl = select.attr("href");
//        Document document1 = Jsoup.connect(novelUrl).userAgent(userAgent).timeout(500000).get();
//        Elements elements = document1.select(".L a");
//        for(Element element:elements){
//            if(element.attr("href").contains("vipreader")){
//                System.out.println("VIP不可读");
//                return;
//            }else {
//                System.out.println(element.attr("href")+" #### "+element.text());
//            }
//        }
    }
    public static void test6() throws IOException {
        /*https://www.2717.com图片网站图片url抓取*/
        String baseUrl = "https://www.2717.com/ent/meinvtupian/2019/";
        String url = "https://www.2717.com/ent/meinvtupian/2019/315004.html";
        Document document = Jsoup.connect(url).userAgent(userAgent).get();
        Elements totalElement = document.select("#pageinfo");
        Elements a = totalElement.select("a");
        String s = a.text().replace("共", "").replace("页:","").replace(" ","");
        System.out.println(Integer.parseInt(s));
        for(int i=1;i<=Integer.parseInt(s);i++){
            String tempUrl = baseUrl + "315004_"+i+".html";
            Document document1 = Jsoup.connect(tempUrl).userAgent(userAgent).get();
            Elements select = document1.select("#picBody a");
            Elements img = select.select("img");
            System.out.println(img.attr("alt")+"===>"+img.attr("src"));
            Utils.downImages(null,img.attr("src"));
        }
    }

    public static void test7() throws IOException {
        /*https://www.2717.com图片网站图片url抓取*/
        String baseUrl = "http://www.mm131.com/qingchun/";
        Document document = Jsoup.connect(baseUrl).get();
        System.out.println(document);
        /*思路，每个版块的页面都由
        * http://www.mm131.com/mingxing/list_5_2.html
        * 类型组成
        * 其表单提交的时候，附带两个隐藏域imageField.x以及imageField.y
        * 方法：
        * 1、获取每个版块的页面
        * 2、把当前版块比如http://www.mm131.com/mingxing/下的所有图片集合拿出来
        * 3、拿到分页表里面的页面编号
        * 4、循环页面获取图片集合
        * */

    }

    public static void main(String[] args) throws IOException {
//        System.out.println(UUID.randomUUID().toString().replace("-",""));
        test7();
    }
}
