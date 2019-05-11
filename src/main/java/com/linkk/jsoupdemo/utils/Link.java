package com.linkk.jsoupdemo.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * @Description: 用于存储URL以及URL文本的工具
 * @Author: Administrator
 * @Date: 2019/5/11
 * @Package: com.linkk.jsoupdemo.utils
 * @Name: Link
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Link {
    private String text;
    private String url;
}
