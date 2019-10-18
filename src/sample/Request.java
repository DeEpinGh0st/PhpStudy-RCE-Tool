package sample;


import javafx.scene.control.Alert;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Request {
    /**
     * 1.返回代理对象
     * @param proxyIp
     * @param proxyPort
     * @return
     */
    public Proxy setProxy(String proxyIp , int proxyPort ){
        try{
            InetSocketAddress socketAddress
                    = new InetSocketAddress(proxyIp , proxyPort );
            Proxy proxy = new Proxy(Proxy.Type.HTTP , socketAddress );
            return proxy;
        }catch(Exception e ){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 3.发送POST请求
     * @param url
     * @param params
     * @param formData
     * @return
     * @throws Exception
     */
    public String sendPost(String url , String params  , String formData) throws Exception{

        StringBuilder builder = new StringBuilder();

        if(!(params == null || params.length() == 0) ){
            url += ("?" + params );
        }

        URL Url = new URL(url );
        URLConnection conn = Url.openConnection();

        //如果设置代理 , 和发送GET一样.
        conn.setRequestProperty("accept", "*/*" );
        conn.setRequestProperty("Connection", "Keep-Alive" );
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.86 Safari/537.36");

        //设置之后就可以发送POST请求了
        conn.setDoInput(true );
        conn.setDoOutput(true );


        //获取它的输出流 , 直接写入post请求
        PrintWriter writer = new PrintWriter(conn.getOutputStream() );
        writer.print(formData );
        writer.flush();


        //获取浏览器的返回数据
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream() ) );
        String line = reader.readLine();
        line = new String(line.getBytes() , "gbk" );  //解决乱码的问题
        while(line != null ){
            builder.append(line + "\r\n" );
            line = reader.readLine();
        }
        reader.close();
        writer.close();


        return builder.toString();
    }

    /**
     * 2.发送Get请求
     * @param url
     * @param params
     * 表示链接后面的一些参数 如name=ghoset&pass=ghoset
     * @return
     */
    public String sendGet(String url , String params , Proxy proxy, String payload) throws Exception {
        StringBuilder builder = new StringBuilder();

        if(params != null || params.length() != 0  ){
            url = url + "?" + params;   //重新构造URL链接
        }
        URL Url  = new URL(url );
        URLConnection conn =  Url.openConnection();
        // 设置代理
        //URLConnection conn = Url.openConnection(setProxy(proxyHost, proxyPort));
        // 如果需要设置代理账号密码则添加下面一行
        //conn.setRequestProperty("Proxy-Authorization", "Basic "+Base64.encode("account:password".getBytes()));
        //发送数据包(可以直接抓取浏览器数据包然后复制)
        conn.setRequestProperty("accept", "*/*" );
        conn.setRequestProperty("Connection", "Keep-Alive" );
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.86 Safari/537.36");
        conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
        conn.setRequestProperty("Accept-Charset", payload);
        conn.connect();

        //接收响应的数据包
        /*Map<String , List<String >> map = conn.getHeaderFields();
        Set<String > set = map.keySet();
        for(String k : set ){
            String v = conn.getHeaderField(k );
            System.out.println(k + ":" + v  );
        }*/


        //返回浏览器的输出信息
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream() ));
        String line = reader.readLine();
        line = new String(line.getBytes() , "gbk" );
        //实现将字符串转成gbk类型显示.
        while(line != null ){
            builder.append(line +"\r\n" );
            line = reader.readLine();
        }

        //释放资源
        reader.close();
        if (builder.indexOf("<!DOCTYPE") == -1){
            return builder.toString();
        }
        return builder.substring(0, builder.indexOf("<!DOCTYPE"));
    }

}
