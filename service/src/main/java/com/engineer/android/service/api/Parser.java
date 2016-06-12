package com.engineer.android.service.api;

import java.util.List;

/**
 *
 * Created by L.J on 2016/6/10.
 */
public interface Parser<T> {
    T parseInstance(String content);
    List<T> parseList(String content);
}
