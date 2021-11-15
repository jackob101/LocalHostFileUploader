package com.trix.uploader.caching;

import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

public class ParentKeyGen implements KeyGenerator {
    @Override
    public Object generate(Object target, Method method, Object... params) {

        return target.getClass().getSimpleName() + "_" +
                params[0];
    }
}
