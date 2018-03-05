/**
 * 待改
 * 
 * @param url
 * @param params
 * @param callback
 * @returns {*}
 */
function loadPage(url){
	$('.loader').show();
	$('.alert-fade-hide').show();
	$("#mainDiv").load(url,function(response,status,xhr){
		$('.loader').hide();
		$('.alert-fade-hide').hide();
		if(status=="success"){
			if(response){
				try{
					var result = jQuery.parseJSON(response);
					if(result.code==100){ 
						$("#mainDiv").html("");
					}
				}catch(e){
					return response;
				}
			}
		}
	});
}
 
/**
 * Load a url into a page
 */
var _old_load = jQuery.fn.load;
jQuery.fn.load = function( url, params, callback ) {
	//update for HANZO, 2016/12/22
	if (typeof url !== "string" && _old_load) {
		return _old_load.apply( this, arguments );
	}

	var selector, type, response,
		self = this,
		off = url.indexOf( " " );
	if ( off > -1 ) {
		selector = jQuery.trim( url.slice( off ) );
		url = url.slice( 0, off );
	}
	if ( jQuery.isFunction( params ) ) {
		callback = params;
		params = undefined;
	} else if ( params && typeof params === "object" ) {
		type = "POST";
	}
	if ( self.length > 0 ) {
		jQuery.ajax( {
			url: url,
			beforeSend: function( xhr ) {  
				    xhr.setRequestHeader('X-Requested-With', {toString: function(){ return ''; }});  
			},  
			type: type || "GET", 
			dataType: "html",
			data: params
		} ).done( function( responseText ) {
			response = arguments;
			self.html( selector ?
				jQuery( "<div>" ).append( jQuery.parseHTML( responseText ) ).find( selector ) :
				responseText );
		} ).always( callback && function( jqXHR, status ) {
			self.each( function() {
				callback.apply( this, response || [ jqXHR.responseText, status, jqXHR ] );
			} );
		} );
	}

	return this;
};



function ajaxPost(url, params, callback) {
	var result = null;
    var headers={};
    headers['CSRFToken']=$("#csrftoken").val();
    
	$.ajax({
		type : 'post',
		async : false,
		url : url,
		data : params,
		dataType : 'json',
		headers:headers,
		success : function(data, status) {
			if (callback) { 
				callback.call(this, data, status);
			}
		},
		error : function(err, err1, err2) {
		    if(err && err.readyState && err.readyState == '4'){
                var responseBody = err.responseText;
                if(responseBody){   
                	 responseBody = "{'retData':"+responseBody;
                     var resJson = eval('(' + responseBody + ')');
                     $("#csrftoken").val(resJson.csrf.CSRFToken);
                     this.success(resJson.retData, 200);
                }
                return ;
            }
		}
	});

	return result;
}

/**
 * 收缩左边栏时，触发markdown编辑的resize
 */
$("[data-toggle='offcanvas']").click(function(){
	if(editor){
		setTimeout(function(){editor.resize()},500);
	}
});

var HtmlUtil = {
	/*1.用浏览器内部转换器实现html转码*/
	htmlEncode:function (html){
		//1.首先动态创建一个容器标签元素，如DIV
		var temp = document.createElement ("div");
		//2.然后将要转换的字符串设置为这个元素的innerText(ie支持)或者textContent(火狐，google支持)
		(temp.textContent != undefined ) ? (temp.textContent = html) : (temp.innerText = html);
		//3.最后返回这个元素的innerHTML，即得到经过HTML编码转换的字符串了
		var output = temp.innerHTML;
		temp = null;
		return output;
	},
	/*2.用浏览器内部转换器实现html解码*/
	htmlDecode:function (text){
		//1.首先动态创建一个容器标签元素，如DIV
		var temp = document.createElement("div");
		//2.然后将要转换的字符串设置为这个元素的innerHTML(ie，火狐，google都支持)
		temp.innerHTML = text;
		//3.最后返回这个元素的innerText(ie支持)或者textContent(火狐，google支持)，即得到经过HTML解码的字符串了。
		var output = temp.innerText || temp.textContent;
		temp = null;
		return output;
	}
};

String.prototype.startWith=function(s){
	if(s==null||s==""||this.length==0||s.length>this.length)
		return false;
	if(this.substr(0,s.length)==s)
		return true;
	else
		return false;
	return true;
}

function ajaxApi(url, data, successCallback, failureCallback) {
	$('.loader').show();
	$('.alert-fade-hide').show();
	alert(data);
	$.ajax({
		type : "POST",
		url : url,
		data : data,
		dataType : "html",
		success : function(d) {
			$('.loader').hide();
			$('.alert-fade-hide').hide();
			successCallback && successCallback($.parseJSON(d));
		},
		error : function(msg) {
			failureCallback && failureCallback(msg);
		}
	});
}

function ajaxP2P(url, data, successCallback, failureCallback) {
	$('.loader').show();
	$('.alert-fade-hide').show();
	$.ajax({
		type : "POST",
		url : url,
		data : JSON.stringify(data),
		dataType : "json",
		contentType : "application/json; charset=utf-8",
		success : function(d) {
			$('.loader').hide();
			$('.alert-fade-hide').hide();
			successCallback && successCallback(d);
		},
		error : function(msg) {
			failureCallback && failureCallback(msg);
		}
	});
}

function isNumberBy500(ssn) {
	var re = /^[0-9]*[0-9]$/i; // 校验是否为数字
	if (re.test(ssn) && ssn % 500 === 0) {
		return true;
	} else {
		return false;
	}
}

function isNumberBy50(ssn) {
	var re = /^[0-9]*[0-9]$/i; // 校验是否为数字
	if (re.test(ssn) && ssn % 50 === 0) {
		return true;
	} else {
		return false;
	}
}