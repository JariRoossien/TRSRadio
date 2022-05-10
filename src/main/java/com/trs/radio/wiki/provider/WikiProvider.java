package com.trs.radio.wiki.provider;

import com.google.gson.Gson;
import com.trs.radio.core.provider.EmbedProvider;
import net.dv8tion.jda.api.EmbedBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class WikiProvider {

    HttpClient client;

    public WikiProvider() {
        client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build();
    }

    public Article getRandomWikiPage() {
        Article article = new Article();
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("https://en.wikipedia.org/api/rest_v1/page/random/summary")
        ).GET().build();
        try {
            HttpResponse<String> stringHttpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            article = new Gson().fromJson(stringHttpResponse.body(), Article.class);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return article;
    }

    public Article getWikiPageFrom(String query, String lang) {
        Article article = new Article();
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("https://" + lang + ".wikipedia.org/api/rest_v1/page/summary/" + query)
        ).GET().build();
        try {
            HttpResponse<String> stringHttpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            article = new Gson().fromJson(stringHttpResponse.body(), Article.class);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return article;

    }

    public Article getWikiPageFrom(String query) {
        return getWikiPageFrom(query, "en");
    }

    public static class Article {
        public String title;
        public String displaytitle;

        public ContentUrl content_urls;
        public Image thumbnail;
        public Image originalimage;
        public String extract;

        @Override
        public String toString() {
            return title + " - " + extract;
        }

        public EmbedBuilder toDiscordtoEmbed() {
            EmbedBuilder builder = EmbedProvider.getDefaultBuilder();
            builder.setTitle(title);

            if (extract == null) {
                builder.addField("Not found", "Page or Revision not found.", false);
                return builder;
            }

            builder.addField(title,
                    extract.substring(0, Math.min(1023, extract.length())), false);
            for (int i = 1023; i < extract.length(); i += 1024) {
                builder.addField("", extract.substring(i,
                        Math.min(i + 1023, extract.length())
                ), false);
            }
            if (thumbnail != null) {
                builder.setThumbnail(originalimage.source);
            }
            builder.addField("", "[Go to wikipedia](" + content_urls.desktop.page + ")", false);
            return builder;
        }
    }

    public static class ContentUrl {
        public Desktop desktop;

        public static class Desktop {
            public String page;
        }
    }

    public static class Image {
        public String source;
        public int width;
        public int height;
    }
}
