package com.xfhy.androidbasiclibs.net.converter;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * author feiyang
 * create at 2017年9月27日10:39:12
 * description：
 * <p>
 * 参考自:https://github.com/BaronZ88/Retrofit2-FastJson-Converter  感谢原作者
 */
final class FastJsonResponseBodyConvert<T> implements Converter<ResponseBody, T> {

    private Type type;

    public FastJsonResponseBodyConvert(Type type) {
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        return JSON.parseObject(value.string(), type);
    }
}