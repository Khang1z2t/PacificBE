package com.pacific.pacificbe.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Component;

@Component
public class HtmlSanitizerUtil {
    private final Safelist BLOG_SAFE_LIST = Safelist.relaxed()
            .addTags("iframe", "video", "source", "figure", "figcaption", "u", "s", "span", "img")
            .addAttributes("iframe", "src", "width", "height", "allowfullscreen", "frameborder")
            .addAttributes("video", "controls", "width", "height")
            .addAttributes("source", "src", "type")
            .addAttributes("img", "src", "alt", "width", "height")
            .addAttributes(":all", "class", "style")
            .addProtocols("iframe", "src", "http", "https")
            .addProtocols("video", "src", "http", "https")
            .addProtocols("source", "src", "http", "https")
            .addProtocols("img", "src", "http", "https", "data");

    public String sanitize(String html) {
        if (html == null || html.isBlank()) return "";
        return Jsoup.clean(html, "", BLOG_SAFE_LIST, new Document.OutputSettings().prettyPrint(false));
    }
}
