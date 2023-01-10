package ru.lesson1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class Main {
    public static final String URL = "https://raw.githubusercontent.com/" +
            "netology-code/jd-homeworks/master/http/task1/cats";

    public static void main(String[] args) throws IOException {

        System.out.println("Запуск программы");
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        HttpGet request = new HttpGet(URL);
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
        CloseableHttpResponse response = httpClient.execute(request);
        String body = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);

        //наковыряем инфу с помощью Gson, ибо он понятный и привычный :-))

        Gson gsonCats = new Gson();
        List<Cats> anyCats = Arrays.asList(gsonCats.fromJson(body, Cats[].class));
        List<Cats> rightCatsList = anyCats.stream().filter(cats -> cats.getUpvotes() > 0)
                .collect(Collectors.toList());
        System.out.println("-------------------Gson parsing:---------------------");
        System.out.println(rightCatsList.toString());

        //наковыряем инфу с помощью Jakson, ибо так в задании

        ObjectMapper mapper = new ObjectMapper();
        Cats[] catsJksn = mapper.readValue(body, Cats[].class);
        List<Cats> jksonCatsRight = Arrays.stream(catsJksn).filter(cats -> cats.getUpvotes() > 0)
                .collect(Collectors.toList());
        System.out.println("-------------------Jakson parsing:---------------------");
        System.out.println(jksonCatsRight.toString());
    }

}
