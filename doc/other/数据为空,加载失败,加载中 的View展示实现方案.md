
# 方案1

### 首先定义2种布局

- 加载错误布局
- 加载中布局

### 在BaseFragment和BaseActivity中加载上面的布局

- 定义3种状态:
    - 加载中
    - error
    - 正常情况

子类里面切换状态,切换状态时显示或者隐藏其他的布局

# 方案2

### 自定义一个ViewGroup

自定义一个多布局xml.

将需要显示错误布局的界面在写xml时包含在该ViewGroup中,然后需要显示空布局时将包含的内容布局隐藏,然后显示自己的多样式布局的其中一种即可.

详情见:https://github.com/gturedi/StatefulLayout

源码的大概意思如上