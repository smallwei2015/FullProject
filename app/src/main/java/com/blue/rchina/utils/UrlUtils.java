package com.blue.rchina.utils;

/**
 * ============================================================
 * 
 * 描 述 ：管理url地址的工具类
 * 
 * ============================================================
 **/
public class UrlUtils {
	/**基础ip 融中国域名*/
	/*http://smartchina.blueapp.com.cn:8093/smartchina*/
	public final static String BASE="http://smartchina.blueapp.com.cn:8093/";


	public static final String BWEATHER="http://api.map.baidu.com/telematics/v3/weather?location=%s&output=json&ak=qQTjMuGGYt9VlOYnpE0EeqIENimTW0nS&mcode=2D:52:C3:2B:88:44:A4:1D:92:2B:43:C9:03:ED:A3:A4:B5:55:73:20;com.blue.rchina";


	public static final String BASEURL="http://longyan.blueapp.com.cn:8080/dwtt/frontAPI/";
	public static final String LIST_REPORT =BASEURL+"achieveReport" ;
	public static final String REPORT_HANDLE=BASEURL+"handleReport";

	public static final String BASEURL1="http://202.85.214.46:8090/blueapp/frontAPI/";

	public static final String joinShopCart=BASEURL1+"joinShopCart";
	public static final String getGoodsInfo=BASEURL1+"getGoodsInfo";
	public static final String emptyShopCart=BASEURL1+"emptyShopCart";
	public static final String updateShopCart=BASEURL1+"updateShopCart";
	public static final String getOrderId=BASEURL1+"getOrderId";
	public static final String getShopCartInfo=BASEURL1+"getShopCartInfo";

	 public static final String CHANNEL_LIST=BASEURL+"achieveNewsList";
	public static final String CHANEL_STRUCTURE=BASEURL+"achieveChannelData?channelId=%s&page=%s";
	public static final String READ_ARTICLE=BASEURL+"readArticle?appuserId=%s&dataId=%s";
	public static final String LIST_ARTICLE_COMMENT=BASEURL+"findArticleCommentList";
	public static final String USER_ARTICLE_STATE=BASEURL+"getStateAboutAppuserToArticle?dataId=%s&appuserId=%s";

	public static final String List_COMMENT_REPORT=BASEURL+"achieveCommentReport";
	public static final String REPORT_COMMENT = BASEURL + "commentReport";

	public static final String deleteOrder=BASEURL1+"deleteOrder";
	public static final String confirmReceive=BASEURL1+"confirmReceive";
	public static final String getOrderInfoByAppuser=BASEURL1+"getOrderInfoByAppuser";
	public static final String FEEDBACK = "";
	public static final String LIST_USER_COLLECT = "";
	public static final String pay=BASEURL1+"pay";

	/*上传头像*/
	public static final String UPDATE_HEAD=BASEURL1+"updatePersonalHeadIcon";
	/*修改用户名*/
	public static final String UPDATE_NAME=BASEURL1+"updatePersonalNickName";
	/*修改性别*/
	public static final String UPDATE_GENDER=BASEURL1+"updatePersonalSex?arg1=%s&appuserId=%s";
	/*修改手机号*/
	public static final String UPDATE_PHONE=BASEURL1+"updatePersonalPhone";

	public static final String VALIDATE_VERIFY=BASEURL1+"validateSMSCode";
	public static final String  UPDATE_PASSWORD =BASEURL1+"resetPassword";
	public static final String getCouponInfoByAppuser=BASEURL1+"getCouponInfoByAppuser";

	public static final String 	APP_URL="";
	public static final String achieveNoticeList="";




	//public static final String N_base="http://192.168.1.12:8088/smartchina/frontAPI/";

	public static final String N_base="http://smartcity.blueapp.com.cn:8088/smartchina/frontAPI/";

	public static final String N_achieveAreaStructure=N_base+"achieveAreaStructure";
	public static final String N_achieveApplicationStructure=N_base+"achieveApplicationStructure";
	public static final String N_achieveChannelData=N_base+"achieveChannelData";

	public static final String N_login=N_base+"login";
	public static final String N_thirdPartyLogin=N_base+"thirdPartyLogin";
	public static final String N_regiWithText=N_base+"regiWithText";
	public static final String N_achieveSMSCode=N_base+"achieveSMSCode";
	public static final String N_validateSMSCode=N_base+"validateSMSCode";
	public static final String N_resetPassword= N_base+"resetPassword";
	public static final String N_updatePersonalHeadIcon=N_base+"updatePersonalHeadIcon";
	public static final String N_updatePersonalNickName=N_base+"updatePersonalNickName";
	public static final String N_updatePersonalSex=N_base+"updatePersonalSex";
	public static final String N_updatePersonalPhone=N_base+"updatePersonalPhone";



	public static final String N_achievePersonalScore=N_base+"achievePersonalScore";
	public static final String N_findAppuserArticleCollectList=N_base+"findAppuserArticleCollectList";
	public static final String N_findAppuserArticleCommentList=N_base+"findAppuserArticleCommentList";
	public static final String N_feedBack=N_base+"feedBack";
	public static final String N_openAPP=N_base+"openAPP";
	public static final String N_inviteFriend=N_base+"inviteFriend";
	public static final String N_readArticle=N_base+"readArticle";
	public static final String N_shareArticle=N_base+"shareArticle";
	public static final String N_commentArticle=N_base+"commentArticle";
	public static final String N_findArticleCommentList=N_base+"findArticleCommentList";
	public static final String N_collectArticle=N_base+"collectArticle";
	public static final String N_cancelCollectArticle=N_base+"cancelCollectArticle";


	public static final String N_report=N_base+"report";
	public static final String N_achieveReport=N_base+"achieveReport";
	public static final String N_achieveDynamicData=N_base+"achieveDynamicData";
	public static final String N_commentReport=N_base+"commentReport";
	public static final String N_achieveCommentReport=N_base+"achieveCommentReport";
	public static final String N_handleReport=N_base+"handleReport";
	public static final String N_achieveAreaByLocation=N_base+"achieveAreaByLocation";
	public static final String N_achieveAppuserReport=N_base+"achieveAppuserReport";
	public static final String N_delReport=N_base+"delReport";
	public static final String N_achieveChannelSort=N_base+"achieveChannelSort";
	public static final String N_sortChannel=N_base+"sortChannel";

	public static final String N_getReceiveInfo=N_base+"getReceiveInfo";
	public static final String N_addReceiveInfo=N_base+"addReceiveInfo";
	public static final String N_updateReceiveInfo=N_base+"updateReceiveInfo";
	public static final String N_setDefaultReceiveInfo=N_base+"setDefaultReceiveInfo";
	public static final String N_getGoodsInfo= N_base+"getGoodsInfo";
	public static final String N_getAreaGoodsInfo=N_base+"getAreaGoodsInfo";
	public static final String N_getRechargeOrder=N_base+"getRechargeOrder";
	public static final String N_recharge=N_base+"recharge";
	public static final String N_getRechargeInfo=N_base+"getRechargeInfo";

	public static final String N_joinShopCart=N_base+"joinShopCart";
	public static final String N_getShopCartInfo=N_base+"getShopCartInfo";
	public static final String N_emptyShopCart=N_base+"emptyShopCart";
	public static final String N_updateShopCart=N_base+"updateShopCart";
	public static final String N_getOrderId=N_base+"getOrderId";
	public static final String N_getCouponInfoByAppuser=N_base+"getCouponInfoByAppuser";
	public static final String N_confirmReceive=N_base+"confirmReceive";
	public static final String N_deleteOrder=N_base+"deleteOrder";
	public static final String N_getOrderInfoByAppuser=N_base+"getOrderInfoByAppuser";
	public static final String N_pay=N_base+"pay";
	public static final String N_getWeather="http://www.sojson.com/open/api/weather/json.shtml?";
	public static final String N_achieveFranchiseeStructure=N_base+"achieveFranchiseeStructure";
	public static final String N_DOWNLOAD = "http://a.app.qq.com/o/simple.jsp?pkgname=com.blue.rchina";
	public static final String N_achieveCommunity=N_base+"achieveCommunity";
	public static final String N_selectCommunity=N_base+"selectCommunity";
	public static final String N_achieveAppuserCommunity=N_base+"achieveAppuserCommunity";

	public static final String N_SHARE="http://smartcity.blueapp.com.cn:8088/smartchina/plug-in-ui/project/download/index.html";


}