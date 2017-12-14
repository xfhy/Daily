package com.xfhy.daily.model.bean;

import java.util.List;

/**
 * author feiyang
 * create at 2017/9/27 16:41
 * description：往期日报
 */
public class PastNewsBean {


    /*
     * date : 20170918
     * stories : [{"images":["https://pic3.zhimg.com/v2-c0b7c8183ac78ac0fb602ddbe7b98f5e.jpg"],
     * "type":0,"id":9621716,"ga_prefix":"091822","title":"小事 · 一个县城大爷的「互联网」风暴"},
     * {"images":["https://pic4.zhimg.com/v2-42a9abdb0fba2e8002a2930ca95652b3.jpg"],"type":0,
     * "id":9620469,"ga_prefix":"091821","title":"《我爱我家》 vs 《武林外传》，我选《我爱我家》"},
     * {"images":["https://pic2.zhimg.com/v2-4bc3dd35946aed7c318f7ee0c70e108d.jpg"],"type":0,
     * "id":9621230,"ga_prefix":"091820","title":"一提饺子就想到韭菜鸡蛋和猪肉，是时候为口口爆汁的番茄馅打 call 了"},
     * {"images":["https://pic1.zhimg.com/v2-f0bd4da6d39ee6c07bba251adb80f69c.jpg"],"type":0,
     * "id":9621619,"ga_prefix":"091819","title":"人挤人、跑断腿、在大厅里迷路，这样的医院肯定输在了设计上"},
     * {"images":["https://pic4.zhimg.com/v2-76be599477e5e0b5bd459fa85484cb47.jpg"],"type":0,
     * "id":9615976,"ga_prefix":"091818","title":"一个剧组中，赚钱最多、第二多、第三多的人，都是谁？"},
     * {"images":["https://pic2.zhimg.com/v2-6611ac97fea76cc8e62d1c0c6a993fa9.jpg"],"type":0,
     * "id":9621221,"ga_prefix":"091817","title":"- 只有我觉得闺蜜很假吗？\r\n- 对，只有你"},
     * {"images":["https://pic3.zhimg.com/v2-b99a052298d1c5bf43542262cbab7376.jpg"],"type":0,
     * "id":9620697,"ga_prefix":"091816","title":"「63% 农村孩子没上过高中」，网友的炮火都被标题党带歪了"},
     * {"images":["https://pic2.zhimg.com/v2-962ec1d52e2315467d09fa6986b3b53d.jpg"],"type":0,
     * "id":9621541,"ga_prefix":"091815","title":"去年大火的《西部世界》落选了，这部令人头皮发麻的电视剧是最大赢家"},
     * {"images":["https://pic1.zhimg.com/v2-b0ead81f8b19980572808381624e6a74.jpg"],"type":0,
     * "id":9621255,"ga_prefix":"091814","title":"看着自己的作品被人议论，创作者们会想什么？"},
     * {"images":["https://pic3.zhimg.com/v2-c99c8b9c059caeb704fdff140113795e.jpg"],"type":0,
     * "id":9617782,"ga_prefix":"091813","title":"只知道它是建筑大师的杰作，走远了才发现竟然像是只\u2026\u2026熊"},
     * {"images":["https://pic3.zhimg.com/v2-c8bd77efde34f4e1389b9220ad072fbe.jpg"],"type":0,
     * "id":9612104,"ga_prefix":"091812","title":"大误 · 失落的植物简史"},{"images":["https://pic3.zhimg
     * .com/v2-85a95a1143ce41ae9567869c1e040e76.jpg"],"type":0,"id":9621239,"ga_prefix":"091811",
     * "title":"我发的微博属于我吗？想要给第三方使用新浪管得着吗？"},{"images":["https://pic1.zhimg
     * .com/v2-ffd073f5d205a8ca5bf00b9d86cadb84.jpg"],"type":0,"id":9620937,"ga_prefix":"091810",
     * "title":"生命的最后，它化作 15 亿公里外的一点微光"},{"images":["https://pic2.zhimg
     * .com/v2-0f9d7960d946b7f83a93921caee888e5.jpg"],"type":0,"id":9616037,"ga_prefix":"091809",
     * "title":"「你期望的薪资是多少？」如果 HR 问起，你怎么回？"},{"images":["https://pic2.zhimg
     * .com/v2-c685b99f69dc97a1fe160f7c64262e59.jpg"],"type":0,"id":9617425,"ga_prefix":"091808",
     * "title":"为什么天上的星星除了月球外，肉眼看大小都差不多？"},{"images":["https://pic2.zhimg
     * .com/v2-b39a819f760373dae8857e512fcecb25.jpg"],"type":0,"id":9614949,"ga_prefix":"091807",
     * "title":"查套餐，自动接电话，新夜间模式\u2026\u2026即将开放更新的 iOS 11，有史以来最贴心"},
     * {"images":["https://pic2.zhimg.com/v2-6b9ed2a911069afee7e174a693752071.jpg"],"type":0,
     * "id":9620744,"ga_prefix":"091807","title":"WePhone 创始人被逼自杀后，世纪佳缘们的新危机"},
     * {"images":["https://pic2.zhimg.com/v2-d9f81acc91b7bce6f59a3ea71887a5c5.jpg"],"type":0,
     * "id":9617927,"ga_prefix":"091807","title":"流传在表情包界的神图「黑人问号」，究竟「神」在哪？"},
     * {"images":["https://pic1.zhimg.com/v2-7fbd72986cffa1beb0049aa9562cf518.jpg"],"type":0,
     * "id":9620861,"ga_prefix":"091806","title":"瞎扯 · 如何正确地吐槽"}]
     */

    /**
     * 日报日期
     */
    private String date;
    /**
     * 对应日期的日报
     */
    private List<LatestDailyListBean.StoriesBean> stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<LatestDailyListBean.StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<LatestDailyListBean.StoriesBean> stories) {
        this.stories = stories;
    }

    @Override
    public String toString() {
        return "PastNewsBean{" +
                "date='" + date + '\'' +
                ", stories=" + stories +
                '}';
    }
}
