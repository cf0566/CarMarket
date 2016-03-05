package com.cpic.carmarket.utils;

public class UrlUtils {
	//请求头
	public static final String postUrl="http://jk1.cpioc.com/index.php?m=Admin&c=merchantApi&a=";
	//登录接口:
	public static final String path_login="login";
	//获取验证码
	public static final String path_getCode="getCode";
	//忘记密码接口
	public static final String path_updatePwd="updatePwd";
	//获取客服电话
	public static final String path_getServiceTel="getServiceTel";
	//问答列表接口:
	public static final String path_answerList="answerList";
	//问答详情接口
	public static final String path_answerDetail="answerDetail";
	//回复接口
	public static final String path_answer="answer";
	//消息列表接口
	public static final String path_messageList="messageList";
	//消息详情接口
	public static final String path_messageDetail="messageDetail";
	//订单列表
	public static final String path_orderList="orderList";
	//订单状态
	public static final String STATUS_WAIT = "1";
	public static final String STATUS_ON = "2";
	public static final String STATUS_AFTER = "3";
	public static final String STATUS_QUERY = "4";
	public static final String STATUS_CLOSE = "5";
	
	//订单详情接口
	public static final String path_orderDetail="orderDetail";
	//订单状态变更接口
	public static final String path_orderAction="orderAction";
	//添加洗车服务
	public static final String path_washAdd="washAdd";
	//删除洗车服务
	public static final String path_washDel="washDel";
	//洗车服务列表
	public static final String path_washList="washList";
	//交易记录
	public static final String path_tradeList="tradeList";
	//保证金缴纳
	public static final String path_marginSubmit="marginSubmit";
	//提现记录
	public static final String path_deposit="deposit";
	//意见反馈接口
	public static final String path_advise="advise";
	//根据环信用户账号获取用户信息
	public static final String path_getUserImg="getUserImg";
	//上传商家logo:
	public static final String path_uploadImage="uploadImage";
	//上传店面图片
	public static final String path_uploadStoreImg="uploadStoreImg";
	//商家信息
	public static final String path_merchantInfo="merchantInfo";
	//更改商家信息
	public static final String path_modifyInfo="modifyInfo";
	
	//获取数据失败Toast
	public static final String loading_failure = "数据获取失败,请检查网络状况！";
	
}
