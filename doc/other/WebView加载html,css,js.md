# WebView踩坑


## 1.设置WebView的配置

```java
 WebSettings settings = wvDetailContent.getSettings();
if (mPresenter.getNoImageState()) {
    //设置为无图模式
    settings.setBlockNetworkImage(true);
}

//判断用户是否设置了自动缓存
if (mPresenter.getAutoCacheState()) {
    //设置是否应该启用应用程序缓存API。 默认值是false
    settings.setAppCacheEnabled(true);
    //设置是否启用DOM存储API。 默认值是false。
    settings.setDomStorageEnabled(true);
    //设置是否启用数据库存储API。 默认值是false。
    settings.setDatabaseEnabled(true);
    if (SystemUtil.isNetworkConnected()) {
        //默认缓存使用模式。
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
    } else {
        //不要使用网络，从缓存中加载。
        settings.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
    }
}

//是否启用JS
settings.setJavaScriptEnabled(true);
//设置自适应屏幕  缩小宽度以适合屏幕的内容
settings.setLoadWithOverviewMode(true);
//设置布局算法,将所有内容移动到视图宽度的一列中。
settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//设置是否允许进行缩放
settings.setSupportZoom(true);
```

## 2.在html中引入外部css,js文件   常规拼接顺序css->html->js

```java
public class HtmlUtil
{

    //css样式,隐藏header
    private static final String HIDE_HEADER_STYLE = "<style>div.headline{display:none;}</style>";

    //css style tag,需要格式化
    private static final String NEEDED_FORMAT_CSS_TAG = "<link rel=\"stylesheet\" type=\"text/css\" href=\"%s\"/>";

    // js script tag,需要格式化
    private static final String NEEDED_FORMAT_JS_TAG = "<script src=\"%s\"></script>";

    public static final String MIME_TYPE = "text/html; charset=utf-8";

    public static final String ENCODING = "utf-8";

    private HtmlUtil()
    {

    }

    /**
     * 根据css链接生成Link标签
     *
     * @param url String
     * @return String
     */
    public static String createCssTag(String url)
    {

        return String.format(NEEDED_FORMAT_CSS_TAG, url);
    }

    /**
     * 根据多个css链接生成Link标签
     *
     * @param urls List<String>
     * @return String
     */
    public static String createCssTag(List<String> urls)
    {

        final StringBuilder sb = new StringBuilder();
        for (String url : urls)
        {
            sb.append(createCssTag(url));
        }
        return sb.toString();
    }

    /**
     * 根据js链接生成Script标签
     *
     * @param url String
     * @return String
     */
    public static String createJsTag(String url)
    {

        return String.format(NEEDED_FORMAT_JS_TAG, url);
    }

    /**
     * 根据多个js链接生成Script标签
     *
     * @param urls List<String>
     * @return String
     */
    public static String createJsTag(List<String> urls)
    {

        final StringBuilder sb = new StringBuilder();
        for (String url : urls)
        {
            sb.append(createJsTag(url));
        }
        return sb.toString();
    }

    /**
     * 根据样式标签,html字符串,js标签
     * 生成完整的HTML文档
     */

    public static String createHtmlData(String html, List<String> cssList, List<String> jsList)
    {
        final String css = HtmlUtil.createCssTag(cssList);
        final String js = HtmlUtil.createJsTag(jsList);
        return css.concat(HIDE_HEADER_STYLE).concat(html).concat(js);
    }
}

```

## 3.开始加载

```java
String htmlData = HtmlUtil.createHtmlData(zhihuDetailBean.getBody(),zhihuDetailBean.getCss(),zhihuDetailBean.getJs());
wvDetailContent.loadData(htmlData, HtmlUtil.MIME_TYPE, HtmlUtil.ENCODING);
```

## 4.回退处理

```java
public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wvDetailContent.canGoBack()) {
            wvDetailContent.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
```
