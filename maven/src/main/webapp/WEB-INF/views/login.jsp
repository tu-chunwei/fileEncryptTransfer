<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/head.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>登录</title>
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
	
<div class="innercontentNo innercontentpd0">

字符串：<input type="text"   id="str1"  /> <input type="button"   onclick="toJm()" value="加密">  
<br>加密后: <div style="border:1px solid red;width:300px;height:30px;" id="toJm"></div>
<br>
<br>
<input type="button"   onclick="deJm()" value="解密"> <br>

解密后: <div style="border:1px solid green;width:300px;height:30px;" id="deJm"> &nbsp;</div>

   
</div>
</body>

<script type="text/javascript">
	$(function(){
		$.postJSON("${pageContext.request.contextPath }/security/key",{},function(e){
			debugger
			if(e != null){
				var random = guid();
				/* //模
				var modulus = e.pubKey.split(';')[0];
				//公钥指数
				var public_exponent = e.pubKey.split(';')[1];
				//通过模和公钥参数获取公钥
				var key = new RSAUtils.getKeyPair(public_exponent, "", modulus);
				//对密码进行加密传输 
				var encrypedPwd = RSAUtils.encryptedString(key,a);
				$.postJSON("${pageContext.request.contextPath }/security/getAESkey",{"encrypedPwd":encrypedPwd},function(e){
					debugger
				}); */
				var encrypt = new JSEncrypt();
				encrypt.setPublicKey(e.pubKey);
				//加密
				var encrypedPwd = encrypt.encrypt(random);
				$.postJSON("${pageContext.request.contextPath }/security/postAESkey",{"encrypedPwd":encrypedPwd},function(e){});
				alert(encrypedPwd);
				var text = Encrypt("123",random);
				$.postJSON("${pageContext.request.contextPath }/security/AESTest",{"text":text},function(e){
					debugger
				});
			}
		});
	});
	function Encrypt(word,AESkey){  
		debugger
	    var key = CryptoJS.enc.Utf8.parse(AESkey);
	    var srcs = CryptoJS.enc.Utf8.parse(word);  
	    var encrypted = CryptoJS.AES.encrypt(srcs, key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});  
	    return encrypted.toString();  
	}  
	function Decrypt(word){  
	    var key = CryptoJS.enc.Utf8.parse("&i do not know !");   

	    var decrypt = CryptoJS.AES.decrypt(word, key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});  
	    return CryptoJS.enc.Utf8.stringify(decrypt).toString();  
	} 
	//生成随机码
	function guid() {
	    function S4() {
	       return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
	    }
	    return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4());
	}
</script>
<script type="text/javascript">
 	
	function Encrypt(word){  
	    var key = CryptoJS.enc.Utf8.parse("&i do not know !");   

	    var srcs = CryptoJS.enc.Utf8.parse(word);  
	    var encrypted = CryptoJS.AES.encrypt(srcs, key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});  
	    return encrypted.toString();  
	}  
	function Decrypt(word){  
	    var key = CryptoJS.enc.Utf8.parse("&i do not know !");   

	    var decrypt = CryptoJS.AES.decrypt(word, key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});  
	    return CryptoJS.enc.Utf8.stringify(decrypt).toString();  
	} 
	function toJm(){
		$("#toJm").html(Encrypt($("#str1").val()));
	}

   function deJm(){
	   var rlt=$("#toJm").html();
	   if(rlt==""){
		   alert("请先加密，确保红框处有加密后的值！");
	   }else{
		}
		$("#deJm").html(Decrypt(rlt));
	}
</script>
</html>