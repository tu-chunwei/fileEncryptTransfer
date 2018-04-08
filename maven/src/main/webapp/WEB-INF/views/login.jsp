<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/head.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!-- <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width"> -->
<title>登录</title>
<%-- <link rel="stylesheet" href="${pageContext.request.contextPath }/css/mdui.css" />
<script type="text/javascript" src="${pageContext.request.contextPath }/js/mdui.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-3.2.1.min.js"></script> --%>
</head>
<body style="background: url(../img/bcl3.jpg); background-size: cover;">
	<div class="mdui-container">
		<div class="mudi-row" style="height: 100px;"></div>
		<div
			class="mdui-card mudi-row mdui-col-md-4 mdui-col-offset-md-4 mdui-col-sm-8 mdui-col-offset-sm-2 mdui-col-xs-12 ">

			<div class="mdui-card-media"
				style="width: 150px; margin: 0 auto; margin-top: 40px;">
				<!-- <img src="img/head.png" /> -->
			</div>
			<div class="mdui-card-media mdui-text-center"
				style="color: #0084ff; font-size: 22px; margin-top: 15px;">登录</div>
			<div
				class="mdui-row mdui-textfield mdui-textfield-floating-label mdui-col-md-8 mdui-col-offset-md-2 mdui-col-xs-8 mdui-col-offset-xs-2"
				style="margin-top: 10px;">
				<label class="mdui-textfield-label" style="color: dimgrey;">用户名</label>
				<input class="mdui-textfield-input"
					style="border-bottom-color: #222222;" type="text" required />
				<div class="mdui-textfield-error" style="color: #666;">用户名不能为空</div>
			</div>
			<div
				class="mdui-row mdui-textfield mdui-textfield-floating-label mdui-col-md-8 mdui-col-offset-md-2 mdui-col-xs-8 mdui-col-offset-xs-2">
				<label class="mdui-textfield-label" style="color: dimgrey;">密码</label>
				<input class="mdui-textfield-input"
					style="border-bottom-color: #222222;" type="password"
					pattern="^.*(?=.{6,})(?=.*[a-z])(?=.*[A-Z]).*$" required />
				<div class="mdui-textfield-error" style="color: #666;">密码至少 6
					位，且包含大小写字母</div>
				<!-- <div class="mdui-textfield-helper">请输入至少 6 位，且包含大小写字母的密码</div> -->
			</div>
			<div
				class="mudi-row mdui-col-md-8 mdui-col-offset-md-2 mdui-col-xs-8 mdui-col-offset-xs-2"
				style="margin-top: 30px;">
				<button class="mdui-btn mdui-btn-raised mdui-ripple mdui-color-gray"
					style="background: rgba(0, 132, 255, .7); color: #ffffff; width: 100%;">
					<i class="mdui-icon mdui-icon-center material-icons"></i>登录
				</button>
			</div>
			<!--<div  class="mudi-row mdui-col-md-8 mdui-col-offset-md-2 mdui-col-xs-8 mdui-col-offset-xs-2" style="margin-top: 10px;" > 
			    <button class="mdui-btn mdui-btn-raised mdui-ripple mdui-color-gray mudi-xs-12 mudi-sm-12 mudi-md-12 mudi-lg-12" style="background: rgba(0,132,255,.7);color: #ffffff;"><i class="mdui-icon mdui-icon-center material-icons"></i>重置</button>
			    </div>-->
			<div
				class="mdui-row  mdui-col-md-8 mdui-col-offset-md-2 mdui-col-xs-8 mdui-col-offset-xs-2"
				style="height: 40px;"></div>
		</div>
	</div>
</body>

<script type="text/javascript">
	
	$(function(){
		debugger
		var a = guid();
	});
	
	
	function guid() {
	    function S4() {
	       return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
	    }
	    return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
	}
	
	$.postJSON = function(url, data, callback) {
	    return jQuery.ajax({
	        'type' : 'POST',
	        'url' : url,
	        'contentType' : 'application/json;charset=UTF-8',
	        'data' : JSON.stringify(data),
	        'dataType' : 'json',
	        'success' : callback
	    });
	};

	</script>
</html>