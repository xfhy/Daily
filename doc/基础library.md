# 基础library

- Application

## 1.activity

- BaseActivity
	- 销毁时取消网络访问
	- 封装fragment返回栈
- AppManager 管理所有的Activity
- BaseMvpActivity  MVP界面的
	- 泛型:BasePresenter
	- 继承:BaseActivity
	- 实现:BaseView

## 2.fragment

- BaseFragment
- BaseMVPFragment 
	- 泛型:BasePresenter
	- 继承:BaseFragment
	- 实现:BaseView

## 2.presenter

- BasePresenter (接口)
	- 泛型:V extends View
- BaseView 
	- void showErrorMsg(String msg);
	- void showErrorView(String msg);
