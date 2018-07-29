package com.fadelands.core.utils;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.URL;

public class CountryLookup {


    public static String getCountryCode(InetSocketAddress ip) throws Exception {
        HttpResponse<JsonNode> data = Unirest.get("http://ip-api.com/json/" + ip.getAddress().getHostAddress()).asJson();
        return data.getBody().getObject().getString("countryCode");
    }


    public static String getCountryName(InetSocketAddress ip) throws Exception {
        URL url = new URL("http://ip-api.com/json/" + ip.getAddress().getHostAddress());
        BufferedReader stream = new BufferedReader(new InputStreamReader(
                url.openStream()));
        StringBuilder entirePage = new StringBuilder();
        String inputLine;
        while ((inputLine = stream.readLine()) != null)
            entirePage.append(inputLine);
        stream.close();
        return entirePage.toString().split("\"country\":\"")[1].split("\",")[0];
    }

}
