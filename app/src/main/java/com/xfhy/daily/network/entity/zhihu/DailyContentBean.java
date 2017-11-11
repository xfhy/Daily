package com.xfhy.daily.network.entity.zhihu;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * author feiyang
 * create at 2017/9/27 15:43
 * description：日报内容
 */
public class DailyContentBean {


    /*
     * body : <div class="main-wrap content-wrap">
     <div class="headline">

     <div class="img-place-holder"></div>



     </div>

     <div class="content-inner">




     <div class="question">
     <h2 class="question-title">为什么想单独控制无名指时，附近的手指也会被控制？</h2>

     <div class="answer">

     <div class="meta">
     <img class="avatar" src="http://pic2.zhimg.com/v2-4efdade4145ab5910048611fbbdd4c0d_is.jpg">
     <span class="author">牛传欣，</span><span class="bio">脑运动控制探秘，神经假肢与仿生控制，XMind 软件早期天使投资人</span>
     </div>

     <div class="content">
     <p>无名指和小指的纠结比较明显。以我个人为例，如果我握住小指，无名指能够独立弯曲；但如果握住无名指，则小指完全无法弯曲。据说这个现象因人而异，但大体上每个人的无名指和小指都存在一定程度的纠结。</p>
     <p>这种现象和肌肉肌腱的解剖结构有关，也和神经支配有关。</p>
     <p>先看肌腱的解剖。下面是网上找来的手部伸肌腱解剖图片，可以看到驱动无名指和小指的肌腱是有分叉连接的，这样在动一个指头的时候，分叉的肌腱会带动所连的手指。也就是说，你的无名指和小指存在机械上的&ldquo;串台&rdquo;。</p>
     <p><img class="content-image" src="http://pic4.zhimg
     .com/70/v2-94a1fcda13472558d31bdc9c46511563_b.jpg" alt="" /></p>
     <p>不过人手上只是伸肌的肌腱串连较为明显，屈肌的肌腱基本上都是分离的。所以上面讲到的手指纠结问题我认为主要原因不是肌腱的串连，而是神经支配纠结了。</p>
     <p>下面看看感觉神经连接，</p>
     <p><img class="content-image" src="http://pic1.zhimg
     .com/70/v2-881fd868ccb3ad91daee0b8f0442d42c_b.jpg" alt="" /></p>
     <p>可以看到有一条黄色的来自尺神经（Ulnar nerve）的分叉，既连着无名指又连着小指。这说明无名指和小指也存在神经信号上的&ldquo;串台&rdquo;
     。不过这个图上画的是感觉神经，我认为仍然不是纠结的最核心。</p>
     <p>除了感觉神经以外还有运动神经，主要是负责弯曲无名指和小指的&ldquo;指深屈肌&rdquo;
     存在共同的支配。指深屈肌位置在手臂上，暂时没有找到特别清楚的讲指深屈肌的运动神经&ldquo;串台&rdquo;的图，用下面的代替：</p>
     <p><img class="content-image" src="http://pic1.zhimg
     .com/70/v2-42cf40b7283346211da802676369324c_b.jpg" alt="" /></p>
     <p>至于你问的有没有可能在大脑里面也有混淆或者&ldquo;串台&rdquo;？这个应该说正常人是没有的。从经典的&ldquo;小矮人（homunculus）&rdquo;
     图谱看过去，五个指头在感觉运动皮层的分化是很明确的。但脑疾病的患者就可能会有这个串台问题了，也就是一个肌肉的指令串到另一个肌肉去。这个在临床及神经运动控制中叫做&ldquo;
     运动溢出（motor overflow）&rdquo;。</p>
     </div>
     </div>


     <div class="view-more"><a href="http://www.zhihu.com/question/65797393">查看知乎讨论<span
     class="js-question-holder"></span></a></div>

     </div>


     </div>
     </div>
     * image_source : Public Domain
     * title : 握住无名指试试，这时你的小指能弯曲吗？
     * image : https://pic4.zhimg.com/v2-4715ed42c9881d8270c85d30ff0f103b.jpg
     * share_url : http://daily.zhihu.com/story/9632777
     * js : []
     * ga_prefix : 092714
     * images : ["https://pic4.zhimg.com/v2-5a3b8a84adb07df3c4d15637813c8217.jpg"]
     * type : 0
     * id : 9632777
     * css : ["http://news-at.zhihu.com/css/news_qa.auto.css?v=4b3e3"]
     */

    /**
     * HTML 格式的日报内容
     */
    private String body;
    /**
     * 图片的内容提供方。为了避免被起诉非法使用图片，在显示图片时最好附上其版权信息。
     */
    @JSONField(name = "image_source")
    private String imageSource;
    /**
     * 日报标题
     */
    private String title;
    /**
     * 获得的图片同 `最新消息` 获得的图片分辨率不同。这里获得的是在文章浏览界面中使用的大图。
     */
    private String image;
    /**
     * 供在线查看内容与分享至 SNS 用的 URL
     */
    @JSONField(name = "share_url")
    private String shareUrl;
    /**
     * 供 Google Analytics 使用
     */
    @JSONField(name = "ga_prefix")
    private String gaPrefix;
    /**
     * 新闻的类型
     */
    private int type;
    /**
     * 新闻的 id
     */
    private int id;
    /**
     * js一般都是空的,不知道里面放的什么类型的
     * 供手机端的 WebView(UIWebView) 使用
     */
    private List<String> js;
    /**
     * 缩略图
     */
    private List<String> images;
    /**
     * 供手机端的 WebView(UIWebView) 使用
     * 可知，知乎日报的文章浏览界面利用 WebView(UIWebView) 实现
     */
    private List<String> css;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getGaPrefix() {
        return gaPrefix;
    }

    public void setGaPrefix(String gaPrefix) {
        this.gaPrefix = gaPrefix;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getJs() {
        return js;
    }

    public void setJs(List<String> js) {
        this.js = js;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getCss() {
        return css;
    }

    public void setCss(List<String> css) {
        this.css = css;
    }

    @Override
    public String toString() {
        return "DailyContentBean{" +
                "body='" + body + '\'' +
                ", imageSource='" + imageSource + '\'' +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", shareUrl='" + shareUrl + '\'' +
                ", gaPrefix='" + gaPrefix + '\'' +
                ", type=" + type +
                ", id=" + id +
                ", js=" + js +
                ", images=" + images +
                ", css=" + css +
                '}';
    }
}
