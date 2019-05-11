package com.linkk.jsoupdemo.client;

import com.linkk.jsoupdemo.utils.Link;
import com.linkk.jsoupdemo.utils.PrintUtils;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author: Administrator
 * @Date: 2019/5/11
 * @Package: com.linkk.jsoupdemo.client
 * @Name: ImageClient
 **/
public class ImageClient {

    /*
    * MM131以及2717图片都先暂时搁置
    * */

    private final static Pattern PATTERN = Pattern.compile("(https).*?(jpg)");


    public final static String userAgent = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.75 Safari/537.36";
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

    public static List<Link> nvshens(String keyword) throws IOException {
        /*
        宅男女神网带搜索功能
        * https://wap.nvshens.com/
        * */
        String searchUrl = "https://wap.nvshens.com/query.aspx?name=";
        String baseUrl = "https://wap.nvshens.com";
        searchUrl = searchUrl+keyword;
        System.out.println(searchUrl);
        /*获取查询耶URL*/
        Document document = Jsoup.connect(searchUrl).userAgent(userAgent).timeout(50000).get();
        Elements select = document.select(".ck-item");
        Elements albumUrlElement = select.select(".ck-link-mask");
        String albumUrl = baseUrl+albumUrlElement.attr("href");
        Elements albumTitle = select.select(".ck-title");
        /*判断当前页是否存在相册，存在相册则需要进入相册中方能获取所有的写真集*/
        Document albumPage = Jsoup.connect(albumUrl).userAgent(userAgent).get();
        Element hasAlbum = albumPage.getElementById("btnAlbum");
        if(hasAlbum!=null){
            albumUrl = albumUrl+"album";
        }

        albumPage = Jsoup.connect(albumUrl).userAgent(userAgent).get();
        Elements allPhotoPages = albumPage.select("#dphoto");
        Elements pagesDetail = allPhotoPages.select(".ck-initem");
        List<Link> links = new ArrayList<>();
        for(Element element : pagesDetail){
            links.add(new Link(element.select(".ck-gallery-title").text(),baseUrl+element.select(".ck-link-mask").attr("href")));
        }
////        PrintUtils.printLinks(links);
//        for (int i = 0; i < 5; i++) {
//            Link link = links.get(i);
//            System.out.println("============================================================================");
//            System.out.println(link.getText());
//            System.out.println("============================================================================");
//            albumDetail(link);
//        }
        return  links;
    }

    public static List<Link> albumDetail(Link link) throws IOException {
        /*获取相册页面*/
        Document album = Jsoup.connect(link.getUrl()).userAgent(userAgent).get();
        /*获取分页区域元素，解析得到总的页面数*/
        Elements select = album.select("#pagediv");
        Elements page = select.select(".page");
        String text = page.text();
        int pageNum = Integer.parseInt(text.substring(text.indexOf("/")+1, text.length()));
        String suffix = ".html";
        String searchUrl = null;
        String baseUrl = link.getUrl();
        List<Link> links = new ArrayList<>();
        for(int i=1;i<=pageNum;i++){
            searchUrl = baseUrl+i+suffix;
            Document document = Jsoup.connect(searchUrl).userAgent(userAgent).get();
            Elements currrentImages = document.select("#idiv img");
            for (Element element:currrentImages){
                String src = element.attr("src");
                if(src == null|| "".equals(src)){
                   src = element.attr("data-original");
                }
                Link imgLink = new Link(element.attr("alt"), src);
                links.add(imgLink);
            }
        }
        PrintUtils.printLinks(links);
        return links;
    }
}
