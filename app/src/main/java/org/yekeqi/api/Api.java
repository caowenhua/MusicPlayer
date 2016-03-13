package org.yekeqi.api;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.yekeqi.http.HttpClient;
import org.yekeqi.http.HttpException;
import org.yekeqi.http.Response;
import org.yekeqi.model.KeySearchResponse;
import org.yekeqi.model.SongSearchResponse;
import org.yekeqi.util.JsonUtils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yekeqi on 2015/9/28.
 */
public class Api {

    private HttpClient client;
    private Context context;

    public Api(Context context) {
        client = new HttpClient();
        this.context = context;
    }

    public static enum Method {
        post, get
    }

    private <T extends Object> T request(Method method, String url,
                                 ArrayList<BasicNameValuePair> params, Class<?> clz) {
        T response = null;
        try {
            response = (T) clz.newInstance();
            Response s = null;
            System.out.println(url);
            if (Method.post == method) {
                Map<String, String> map = new HashMap<String, String>();
                for (int i = 0; i < params.size(); i++) {
                    map.put(params.get(i).getName(), params.get(i).getValue());
                }
//                String sign = SecureUtil.getSign(map);
//                params.add(new BasicNameValuePair("sign", sign));
                s = client.post(url, params); // http网络连接
            } else { // RequeseMethod.get == method
                s = client.get(url, null); // http网络连接
            }
            System.out.println(s.asString());
            if (JsonUtils.isGoodJson(s.asString())) {
                // JSONObject json = new JSONObject(s.asString());//转换为json格式
                response = (T) JsonUtils.toBean(s.asString(), clz);
            } else {
//                Looper.prepare();
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "服务器数据匹配失败", Toast.LENGTH_SHORT).show();
                    }
                });

            }

        } catch (IllegalStateException e) {
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "服务器错误", Toast.LENGTH_SHORT).show();
                }
            });
//            Looper.prepare();
//            Toast.makeText(context, "服务器错误", Toast.LENGTH_SHORT).show();
        } catch (HttpException e) {
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "连接超时", Toast.LENGTH_SHORT).show();
                }
            });
//            Looper.prepare();
//            Toast.makeText(context, "连接超时", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "未知错误", Toast.LENGTH_SHORT).show();
                }
            });
//            Looper.prepare();
//            Toast.makeText(context, "未知错误", Toast.LENGTH_SHORT).show();
        }
        return response;
    }

    public KeySearchResponse keySearch(String key){
        String url = "http://so.ard.iyyin.com/sug/sugAll?uid=860173012017731" +
                "&f=f508&v=v8.2.0.2015090716" +
                "&app=ttpod" +
                "&utdid=Up%2FR%2BDhBy0oDAH64ghXa%2Fp8%2B" +
                "&hid=5574435026331845&s=s200"+
                "&alf=alf700145" +
                "&imsi=&tid=0&net=2" +
                "&q=" + URLEncoder.encode(key);
        //UrlUtil.Utf8URLencode(key);

//        ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
//        params.add(new BasicNameValuePair("uid", "860173012017731"));
//        params.add(new BasicNameValuePair("f", "f508"));
//        params.add(new BasicNameValuePair("v", "v8.2.0.2015090716"));
//        params.add(new BasicNameValuePair("app", "ttpod"));
//        params.add(new BasicNameValuePair("utdid", "Up/R+DhBy0oDAH64ghXa/p8+"));
//        params.add(new BasicNameValuePair("hid", "5574435026331845"));
//        params.add(new BasicNameValuePair("s", "s200"));
//        params.add(new BasicNameValuePair("alf", "alf700145"));
//        params.add(new BasicNameValuePair("tid", "0"));
//        params.add(new BasicNameValuePair("net", "2"));
//        params.add(new BasicNameValuePair("imsi", ""));

        KeySearchResponse response = request(Method.get, url, null, KeySearchResponse.class);
        return response;
    }

    public SongSearchResponse songSearch(int page, String key){
        String url = "http://api.dongting.com/misc/search/song?uid=860173012017731&hid=5574435026331845&from=android&resolution=480x854" +
                "&net=2&api_version=1.0&utdid=Up%2FR%2BDhBy0oDAH64ghXa%2Fp8%2B" +
                "&longitude=113.344666" +
                "&user_id=0&splus=4.1.2%252F16" +
                "&tid=0&client_id=5a77fe38cad828662295a19329dfe0cd&f=f508" +
                "&os=4.1.2&app=ttpod" +
                "&rom=Xiaomi%252Fmione_plus%252Fmione_plus%253A4.1.2%252FJZO54K%252F4.4.25%253Auser%252Frelease-keys" +
                "&alf=alf700145&imei=860173012017731&cpu=msm8660&agent=none&size=50&v=v8.2.0.2015090716" +
                "&s=s200&address=%E5%B9%BF%E4%B8%9C%E7%9C%81%E5%B9%BF%E5%B7%9E%E5%B8%82%E5%A4%A9%E6%B2%B3%E5%8C%BA%E8%A1%A1%E5%B1%B1%E8%B7%AF" +
                "&ram=768108+kB&active=0&latitude=23.157738&language=zh&mid=MI-ONE%2BPlus&cpu_model=MIONE" +
                "&page=" + page +
                "&q=" + URLEncoder.encode(key);
        //+ UrlUtil.Utf8URLencode(key);

        SongSearchResponse response = request(Method.get, url, null, SongSearchResponse.class);
        return response;
    }
}
