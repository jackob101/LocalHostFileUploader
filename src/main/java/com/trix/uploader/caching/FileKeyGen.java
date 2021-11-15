package com.trix.uploader.caching;

import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

public class FileKeyGen implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {

        String path = (String) params[0];
        int indexOf = path.lastIndexOf("/");

        if (path.length() > 0) {
            if (indexOf > 0) {
                path = path.substring(0, indexOf);
            } else {
                path = "";
            }
        }

        return target.getClass().getSimpleName() + "_" + path;
    }
}
