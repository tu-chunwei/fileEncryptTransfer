/**
 * post ajax请求
 */
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