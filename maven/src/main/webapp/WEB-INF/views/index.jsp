<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-3.2.1.min.js"></script>
<script src="${pageContext.request.contextPath }/js/ajaxfileupload.js"></script>
<title>Test</title>
</head>
<body>
<form action="${pageContext.request.contextPath }/file/upload" method="post" enctype="multipart/form-data">
   <h2>文件上传</h2>
                文件1:<input type="file" name="uploadFile"/><br/>
                文件2：<input type="file" name="uploadFile"/><br/>
                文件3：<input type="file" name="uploadFile"/><br/>
      <input type="submit" value="上传"/>
</form>
<hr>
<c:forEach items="${user }" var="users">
${users.name }
</c:forEach>
<input onclick="myfunction()" type="button" value="测试">
<div id="show"></div>
姓名：<input type="text" id="name"><br>
密码：<input type="text" id="password"><br>
年龄：<input type="text" id="age"><br>
性别：<input type="text" id="male"><br>
<input type="button" value="注册" onclick="register()">
<hr>
id：<input type="text" id="id">
<input value="删除" type="button" onclick="deleteUser()">
<hr>
<input type="button" value="修改" onclick="updateUser()">
</body>
<!-- **************************************       js        *************************************** -->
<script type="text/javascript">
$(function(){
	debugger
	$.ajax({
		url:"${pageContext.request.contextPath }/user/randomShock",
		type:"POST",
		data:JSON.stringify({"id":"600"}),
		contentType : 'application/json;charset=UTF-8',
		datatype:"json",
		success:function (data){
			debugger
		},
		error:function(e){
			debugger
		}
	});
	/* $.postJSON("${pageContext.request.contextPath }/user/randomShock",{"id":"1","name":"2"},function(e){
		debugger
		if(e){
			for(var i=0;i<e.length;i++){
				html+="name"+e[i].name+"id"+e[i].id;
			}alert(html);
			$("#show").html(html);
		}
	}); */
});


function updateUser(){
	var id = $("#id").val();
	var name = $("#name").val();
	$.postJSON("<%=request.getContextPath() %>/user/update",{"id":id,"name":name},function(e){});
}
function deleteUser(){
	var id = $("#id").val();
	$.postJSON("<%=request.getContextPath() %>/user/deleteuser",{"id":id},function(e){});
}
function register(){
	debugger
	var param = {
		"name":$("#name").val(),
		"password":$("#password").val(),
		"age":$("#age").val(),
		"male":$("#male").val(),
	}
	$.postJSON("<%=request.getContextPath() %>/user/register",param,function(e){
		debugegr
		if(e){
			alert(id);
		}
	});
}
function myfunction(){
	var html = "";
	$.postJSON("<%=request.getContextPath()%>/user/index/load",{"id":1},function(e){
		if(e){
			for(var i=0;i<e.length;i++){
				html+="name"+e[i].name+"id"+e[i].id;
			}alert(html);
			$("#show").html(html);
		}
	});
}

$.postJSON = function(url, data, callback) {
	return jQuery.ajax( {
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
