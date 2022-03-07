package com.evc.toolkit.http;

import com.evc.toolkit.http.adapter.NullStringToEmptyAdapterFactory;
import com.evc.toolkit.util.HttpUrlConstant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Android Studio.
 * User: evan
 * Date: 2020/11/2
 * Time: 5:01 PM
 */
public class HttpServiceGenerator {
    //读超时长，单位：毫秒
    public static final int READ_TIME_OUT =10 * 1000;
    //连接时长，单位：毫秒
    public static final int CONNECT_TIME_OUT = 10 * 1000;
    //写入超时，单位：毫秒
    public static final int WRITE_TIME_OUT = 10 * 1000;
    public Retrofit retrofit;
    public HashMap<Class, Object> serviceCache = new HashMap<>();
    private OkHttpClient okHttpClient;

    private volatile static HttpServiceGenerator retrofitManager;

    /*************************缓存设置*********************/

    /*
    1. noCache 不使用缓存，全部走网络

    2. noStore 不使用缓存，也不存储缓存

    3. onlyIfCached 只使用缓存

    4. maxAge 设置最大失效时间，失效则不使用 需要服务器配合

    5. maxStale 设置最大失效时间，失效则不使用 需要服务器配合 感觉这两个类似 还没怎么弄清楚，清楚的同学欢迎留言

    6. minFresh 设置有效时间，依旧如上

    7. FORCE_NETWORK 只走网络

    8. FORCE_CACHE 只走缓存*/

    /**
     * 设缓存有效期为两天
     */
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;
    /**
     * 查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
     * max-stale 指示客户机可以接收超出超时期间的响应消息。如果指定max-stale消息的值，那么客户机可接收超出超时期指定值之内的响应消息。
     */
    private static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    /**
     * 查询网络的Cache-Control设置，头部Cache-Control设为max-age=0
     * (假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)时则不会使用缓存而请求服务器
     */
    private static final String CACHE_CONTROL_AGE = "max-age=0";

    //私有构造方法
    private HttpServiceGenerator(){

        //缓存
//        File cacheFile = new File(AppInfoManager.getInstance().getContext().getCacheDir(), "cache");
//        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb

        //日志打印
        LoggingInterceptor loggingInterceptor = new LoggingInterceptor.Builder().setLevel(Level.BODY).build();


   /*     Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request ;
                String accesstoken = SPUtils.getString(AppInfoConstant.ACCESSTOKEN, "");
                Log.i("proguarddd",accesstoken);
                Log.i("proguarddd","asd:"+AppInfoConstant.ACCESSTOKEN);

                //ip
                String ip = NetWorkUtil.getLocalIpAddress();
                //警号
                String policeId = SPUtils.getString(AppInfoConstant.ACCOUNT, "");
                //身份证
                String cardNo = SPUtils.getString(AppInfoConstant.IDCARD, "");
                //imei
                String deviceId = DeviceIdUtils.getDeviceId(AppInfoManager.getInstance().getContext());
                Request.Builder builder = original.newBuilder()
                        .header("Content-Type", "application/json")
                        .header("kx_appCode","1");

                if (!accesstoken.isEmpty()){
                    builder = builder.header("Authorization", AppInfoConstant.BEARER+" "+accesstoken);
                }
                //终端Ip号
                if (null != ip && !ip.isEmpty()) {
                    builder = builder.header("kx_terminalIp", ip);
                }
                //policeId
                if (null != policeId && !policeId.isEmpty()) {
                    builder = builder.header("kx_policeId", policeId);
                }
                //cardNo
                if (null != cardNo && !cardNo.isEmpty()) {
                    builder = builder.header("kx_cardNo", cardNo);
                }
                //deviceId
                if (null != cardNo && !cardNo.isEmpty()) {
                    builder = builder.header("kx_imei", deviceId);
                }
                Log.i("Header"+"imei:",deviceId);
                Log.i("Header"+"policeId:",policeId);
                Log.i("Header"+"cardNo:",cardNo);
                Log.i("Header"+"terminalIp:",ip);
                Log.i("Header"+"Authorization:",accesstoken);

                request = builder.method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            }
        };
*/
        //自定义超时时间
     /*   Interceptor timeoutInterceptor = new Interceptor() {

            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                final Invocation tag = request.tag(Invocation.class);
                final Method method = tag != null ? tag.method() : null;
                final ConnectionTime timeout = method != null ? method.getAnnotation(ConnectionTime.class) : null;
                if(timeout !=null && timeout.connectionTime() > 0){
                    Response proceed = chain.withConnectTimeout(timeout.connectionTime(), TimeUnit.SECONDS)
                            .withReadTimeout(timeout.connectionTime(), TimeUnit.SECONDS)
                            .withWriteTimeout(timeout.connectionTime(), TimeUnit.SECONDS)
                            .proceed(request);
                    return proceed;
                }

                return chain.proceed(request);
            }

        };

*/
        OkHttpClient.Builder builder = new OkHttpClient.Builder().readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.MILLISECONDS)
//                .addInterceptor(mRewriteCacheControlInterceptor)
//                .addInterceptor(timeoutInterceptor)
                .addInterceptor(loggingInterceptor);
//                .addInterceptor(headerInterceptor)
//                .cache(cache);

        //用于使用chrome调试
//        builder.addNetworkInterceptor(new com.facebook.stetho.okhttp3.StethoInterceptor());

        okHttpClient = builder.build();

        String baseApiHost = HttpUrlConstant.getAPPURL();

//        if (BuildConfig.DEBUG) {
//            baseApiHost = SPUtils.getString(HttpUrlConstant.KEY_CURRENT_ENVIRONMENT, HttpUrlConstant.getAPPURL());
//        }
        Logger.e("APPURL:"+baseApiHost);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .callFactory(new CallFactoryProxy(okHttpClient))
                .baseUrl(baseApiHost)
                .build();
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public static HttpServiceGenerator create() {
        if (retrofitManager == null) {

            synchronized (HttpServiceGenerator.class) {
                if (retrofitManager == null) {
                    retrofitManager = new HttpServiceGenerator();
                }
            }
        }
        return retrofitManager;
    }

    /**
     * 获取Service
     *
     * @return Service
     */
    public static <T> T create(final Class<T> service) {
        create();
        T cache = (T) retrofitManager.serviceCache.get(service);
        if (cache == null) {
            cache = retrofitManager.retrofit.create(service);
            retrofitManager.serviceCache.put(service, cache);
        }
        return cache;
    }

    /**
     * 云端响应头拦截器，用来配置缓存策略
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
   /* private final Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetWorkUtil.isConnected(AppInfoManager.getInstance().getContext())) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response originalResponse = chain.proceed(request);
            if (NetWorkUtil.isConnected(AppInfoManager.getInstance().getContext())) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        //.header("Cache-Control", "max-age=3600")
                        .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_SEC)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };*/


}
