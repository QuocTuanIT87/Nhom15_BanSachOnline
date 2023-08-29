package com.tuan1611pupu.appbansach.Api;

public class APIService {
    private static String base_url = "https://bookstoremini.azurewebsites.net/";
    public static DataService getService(){
        return APIRetrofitClient.getClient(base_url).create(DataService.class);
    }

}
