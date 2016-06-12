package com.engineer.android.service.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by L.J on 2016/6/10.
 */
public class JsonParser<T> implements Parser {
    private static final Gson GSON = new Gson();

    @Override
    public T parseInstance(String json){
        return GSON.fromJson(json,new TypeToken<T>(){}.getType());
    }

    @Override
    public List<T> parseList(String json){
        return GSON.fromJson(json,new TypeToken<ArrayList<T>>(){}.getType());
    }
}
