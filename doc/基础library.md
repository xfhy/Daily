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
	- 泛型:P extends BasePresenter
	- 继承:BaseFragment
	- 实现:BaseView

## 3.presenter

- BasePresenter (接口)
	- 泛型:V extends View
- BaseView 
	- void showErrorMsg(String msg);
	- void showErrorView(String msg);

-----------

MVP简单使用如下:

CenterFragmentContract里面有presenter和view的接口

public class CenterFragment extends BaseMvpFragment<CenterFragmentContract.Presenter>
        implements CenterFragmentContract.View

--------------
## 4.widget

自定义控件

## 5.db
数据库操作

## 6.util

工具类

## 7.net
网络相关

## 8.adapter

一个封装好了的adapter,方便使用
