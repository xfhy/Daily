金山词霸每日一句  免费开放文档地址:http://open.iciba.com/?c=wiki


传入参数：
file	//数据格式，默认（json），可选xml
date	//标准化日期格式 如：2013-05-06， 如：http://open.iciba.com/dsapi/?date=2013-05-03
如果 date为空 则默认取当日的，当日为空 取前一日的
type(可选)	// last 和 next 你懂的，以date日期为准的，last返回前一天的，next返回后一天的

JSON 字段解释
{
'sid':'' #每日一句ID
'tts': '' #音频地址
'content':'' #英文内容
'note': '' #中文内容
'love': '' #每日一句喜欢个数
'translation':'' #词霸小编
'picture': '' #图片地址
'picture2': '' #大图片地址
'caption':'' #标题
'dateline':'' #时间
's_pv':'' #浏览数
'sp_pv':'' #语音评测浏览数
'tags':'' #相关标签
'fenxiang_img':'' #合成图片，建议分享微博用的
}

返回示例(有一些是Unicode,需要转换成中文):
```json
{
    "sid": "2788",
    "tts": "http:\/\/news.iciba.com\/admin\/tts\/2017-11-20-day",
    "content": "For every beauty there is an eye to see it. For every truth there is an ear to hear it. For every love there is a heart to receive it.",
    "note": "\u6bcf\u4e00\u79cd\u7f8e\uff0c\u90fd\u4f1a\u6709\u4e00\u53cc\u773c\u775b\u80fd\u770b\u5230\uff1b\u6bcf\u4e00\u4e2a\u771f\u76f8\uff0c\u90fd\u6709\u4e00\u53cc\u8033\u6735\u4f1a\u542c\u5230\uff1b\u6bcf\u4e00\u4efd\u7231\uff0c\u603b\u4f1a\u6709\u4e00\u9897\u5fc3\u4f1a\u611f\u53d7\u5230\u3002",
    "love": "986",
    "translation": "\u8bcd\u9738\u5c0f\u7f16\uff1a\u5b66\u95ee\u4e4b\u7f8e\uff0c\u5728\u4e8e\u4f7f\u4eba\u4e00\u5934\u96fe\u6c34\uff1b \u8bd7\u6b4c\u4e4b\u7f8e\uff0c\u5728\u4e8e\u717d\u52a8\u7537\u5973\u51fa\u8f68\uff1b \u5973\u4eba\u4e4b\u7f8e\uff0c\u5728\u4e8e\u8822\u5f97\u65e0\u6028\u65e0\u6094\uff1b \u7537\u4eba\u4e4b\u7f8e\uff0c\u5728\u4e8e\u8bf4\u8c0e\u8bf4\u5f97\u767d\u65e5\u89c1\u9b3c\u3002\u2014\u2014\u5468\u7acb\u6ce2",
    "picture": "http:\/\/cdn.iciba.com\/news\/word\/20171120.jpg",
    "picture2": "http:\/\/cdn.iciba.com\/news\/word\/big_20171120b.jpg",
    "caption": "\u8bcd\u9738\u6bcf\u65e5\u4e00\u53e5",
    "dateline": "2017-11-20",
    "s_pv": "0",
    "sp_pv": "0",
    "tags": [
        {
            "id": null,
            "name": null
        },
        {
            "id": null,
            "name": null
        }
    ],
    "fenxiang_img": "http:\/\/cdn.iciba.com\/web\/news\/longweibo\/imag\/2017-11-20.jpg"
}
```

