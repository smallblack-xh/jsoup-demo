package com.linkk.jsoupdemo.client;

import com.linkk.jsoupdemo.utils.Link;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

/**
 * @author: linkk
 */
public class JsoupDemo1 {

    public final static String userAgent = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.75 Safari/537.36";



    public static void main(String[] args) throws IOException {
        List<Link> links = ImageClient.nvshens("杨晨晨");
        List<Link> linkList = ImageClient.albumDetail(links.get(0));
            for (int j = 0;j < linkList.size(); j++) {
                Link link = linkList.get(j);
                Utils.downImages(null,link.getUrl());
            }

    }
}
