package com.xfhy.androidbasiclibs.common.converter;

import okhttp3.RequestBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;

import java.io.IOException;

import okhttp3.MediaType;
import retrofit2.Converter;

/**
 * author feiyang
 * create at 2017年9月27日10:39:12
 * description：
 * <p>
 * 参考自:https://github.com/BaronZ88/Retrofit2-FastJson-Converter  感谢原作者
 */
final class FastJsonRequestBodyConverter<T> implements Converter<T, RequestBody> {

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

    private SerializeConfig serializeConfig;

    public FastJsonRequestBodyConverter(SerializeConfig serializeConfig) {
        this.serializeConfig = serializeConfig;
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        return RequestBody.create(MEDIA_TYPE, JSON.toJSONBytes(value, serializeConfig));
    }
}
