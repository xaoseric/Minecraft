package com.fadelands.array.geolocation;

import com.fadelands.array.Array;
import com.fadelands.array.request.Request;
import com.fadelands.array.request.RequestBuilder;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class GeoManager {

    private Array array;
    private String geoUrl = "http://ip-api.com/json/";

    public GeoManager(Array array) {
        this.array = array;
    }

    public String getCountry(String ip) {
        System.out.println(ip);
        List<String> name = new ArrayList<>();
        new RequestBuilder(array.getOkHttpClient()).setRequestType(Request.RequestType.GET).setURL(geoUrl + ip).build()
                .asJSON(success -> {
                    if (success.getBody().getString("status").equals("fail")) {
                        name.add("IP check failed.");
                        return;
                    }
                    System.out.println(success.getBody());

                    name.add(success.getBody().getString("country"));

                    System.out.println(name);

                });
        return name.toString();
    }

    public String getCountryCode(String ip) {
        AtomicReference<String> code = new AtomicReference<>(null);
        new RequestBuilder(array.getOkHttpClient()).setRequestType(Request.RequestType.GET).setURL(geoUrl + ip).build()
                .asJSON(success -> code.set(success.getBody().getString("countryCode")));
        return code.get();
    }

    public String getCity(String ip) {
        AtomicReference<String> city = new AtomicReference<>(null);
        new RequestBuilder(array.getOkHttpClient()).setRequestType(Request.RequestType.GET).setURL(geoUrl + ip).build()
                .asJSON(success -> city.set(success.getBody().getString("city")));
        return city.get();
    }
}
