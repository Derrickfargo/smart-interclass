/***
 * 首页js代码
 */
function GpsMap(tmp) {
	$('#mapModal').modal('show');
}

/**
 * 页面初始化后执行
 */
$(function(){
	// 公告 点击更多弹出框
	$('.notice .moreInfo').click(function(){
		$('#moreNoticeModal').modal('show');
	});
	
	//自动登录
	//autoLogin();
});

/**
 * 收藏与取消收藏车源
 */
function collectcars(obj){
   var carid= obj.id;
   if(obj.src.indexOf("cancel") > 0){//已经收藏，可以取消。
	   $.ajax({  
            url : _path+"/mycars/removeFirstPageCollectMyCar",  
            type : "POST",  
            datatype:"json",  
            data : {"carid":carid,"agentid":"agentid1"},  
            success : function(data) {  
              if(data.status==0){                    
            	  obj.src="images/store.png";
            	  obj.title="收藏";
                //cancelSuc();
              }else{
                alert(data.msg);
              }
            },  
            error : function(data) {  
                alert(data.msg);  
            }
        });  
   }else{
   //收藏
   $.ajax({  
        url : _path+"/mycars/collectCar",  
        type : "POST",  
        datatype:"json",  
        data : {"carid":carid,"agentid":"agentid1"},  
        success : function(data) {  
          if(data.status==0){
            obj.src="images/cancel.png";
            obj.title="取消收藏";
          }else{
            alert(data.msg);
            obj.src="images/cancel.png";
          }
        },  
        error : function(data) {  
            //alert(data.msg);  
        }  
    });  
   }
}

/**
 * 收藏与取消收藏货源
 */
function collectgoods(obj){
   if(obj.src.indexOf("cancel") > 0){//已经收藏，可以取消。
    $.ajax({  
        url : _path+"/mygoods/cancelGoods",  
        type : "POST",  
        datatype:"json",  
        data : {"goodsid":obj.id,"agentid":"agentid1","type":"1"},  
        success : function(data) {  
          if(data.status==0){                    
            obj.src = "images/store.png";
            obj.title="收藏";
          }else{
            alert(data.msg);
          }
        },  
        error : function(data) {  
            alert(data.msg);  
        }  
    });  
   }else{
   //收藏
   $.ajax({  
        url : _path+"/mygoods/collectGoods",  
        type : "POST",  
        datatype:"json",  
        data : {"goodsid":obj.id ,"agentid":"agentid1","type":"1"},  
        success : function(data) {  
          if(data.status==0){
            obj.src = "images/cancel.png";
            obj.title="取消收藏";
          }else{
        	alert(data.msg);
        	obj.src = "images/cancel.png";
          }
        },  
        error : function(data) {  
            alert(data.msg);  
        }  
    });  
   }
}

/**
 * 首页左上角 tab页 找车源
 * */
function findCars(){
	// 添加验证
	if($('#localcity').val() == "" && $('#targetcity').val() == ""){
		$('#targetcity').next().removeClass('hidden');
		$('#Fcar').css('margin-top','-10px');
	}else{
		$("#findCarsForm").submit();
		$('#targetcity').next().addClass('hidden');
		$('#Fcar').css('margin-top','0px');
	}
	
}

/**
 * 首页左上角 tab页 物流跟踪
 * */
function findOrder(){
	if(/^\d{0,14}$/gi.test($("#orderno").val())){
		$("#findOrderForm").submit();
		$('#orderno').next().addClass('hidden');
	}else{
		$('#orderno').next().removeClass('hidden');
	}
}

/**
 * 首页右侧 公告滚动
 * @param win
 */
(function(win) {
	var callboarTimer;
	var callboard = $('#callboard');
	var callboardUl = callboard.find('ul');
	var callboardLi = callboard.find('li');
	var liLen = callboard.find('li').length;
	var initHeight = callboardLi.first().outerHeight(true);
	win.autoAnimation = function() {
		if (liLen <= 1)
			return;
		var self = arguments.callee;
		var callboardLiFirst = callboard.find('li').first();
		callboardLiFirst.animate({
			marginTop : -initHeight
		}, 500, function() {
			clearTimeout(callboarTimer);
			callboardLiFirst.appendTo(callboardUl).css({
				marginTop : 0
			});
			callboarTimer = setTimeout(self, 5000);
		});
	}
	callboard.mouseenter(function() {
		clearTimeout(callboarTimer);
	}).mouseleave(function() {
		callboarTimer = setTimeout(win.autoAnimation, 5000);
	});
}(window));
setTimeout(window.autoAnimation, 5000);


/**
 * 公共车源
 */
function carList(){
	$.ajax({  
        url : _path+"/carList",  
        type : "POST", 
        datatype:"json",
        success : function(data) {
          if(!(data == "" || data.items.length==0)){
        	  var html = '';
        	  for ( var i in data.items) {
  				var car = data.items[i];
  				// 拼接div
  				html += '<div class="col-xs-6 objectInfo" onmouseover="fn_goodMouseover(this)" onmouseout="fn_goodMouseout(this)">';
  				html += '<div class="col-xs-10">';
  				html += '<p class=""><b>'+car.localcity+'&nbsp;至&nbsp;'+car.targetcity+'</b></p>';
  				html += '<p>';
  				html += '<span class="">车长：<font class="lenFont">'+car.carlength+'</font>米</span><span class="">载重：<font class="lenFont">'+car.totalload+'</font>吨</span>';
  				html += '<span	class="">车型：<b>'+car.cartype+'</b></span><span class="">货运状态：<b>'+isNull(car.carstatus==0?"空车":"满载")+'</b></span>';
  				html += '</p>';
  				html += '<p>';
  				html += '<span class="">司机：'+isNull(car.name)+'</span>';
  				html += '<span class="rateit" data-rateit-value="'+car.starImg+'" data-rateit-ispreset="true" data-rateit-readonly="true"></span>';
  				//html += '<span class="">共成交：'+car.creditcount+'笔</span>';
  				html += '</p>';
  				html += '</div>';
  				html += '<div class="col-xs-1">';
  				html += '<img id="'+car.id+'" style="display:none;" title="收藏" onclick="collectcars(this)" src="images/store.png"> ';
  				html += '</div>';
  				html += '<div class="col-xs-1">';
  				html += '<img alt="" style="display:none;" title="定位" src="images/maploca.png" onclick="javascript:GpsMap('+car.currentlocation+')">';
  				html += '</div>';
  				html += '</div>';
  			}
			$("#carList").html(html);
			$('.rateit').rateit();
          }else{
        	  // 暂无数据
          }
        },  
        error : function(data) {  
            alert("error");  
        }  
    }); 
}

/**
 * 公共货源
 */
function goodsList(){
	$.ajax({  
        url : _path+"/goodsList",  
        type : "POST", 
        datatype:"json",
        success : function(data) {
          if(!(data == "" || data.items.length==0)){
        	  var html = '';
        	  for ( var i in data.items) {
  				var goods = data.items[i];
  				// 拼接div
  				html += '<div class="col-xs-4 ng-scope objectInfo" onmouseover="fn_goodMouseover(this)" onmouseout="fn_goodMouseout(this)">';
				html += '<div class="col-xs-10">';
				html += '<p class=""><b>'+isNull(goods.originalcity)+'&nbsp;至&nbsp;'+isNull(goods.receiptcity)+'</b></p>';
				html += '<p>';
				html += '<span class=""> <font class="lenFont">'+goods.weight+'</font>吨</span><span class=""><font class="lenFont">'+isNull(goods.carlength)+'</font>米</span>';
				html += '<span class="">'+goods.goodsname+'</span>';
				html += '</p>';
				html += '<p class=""><b>说明</b>：'+goods.memo+'</p>';
				html += '</div>';
				html += '<div class="col-xs-2">';
				html += '<img id="'+goods.id+'"  style="display:none;" title="收藏" onclick="collectgoods(this)" src="images/store.png"> ';
				html += '</div>';
				html += '</div>';
  			}
			$("#goodsList").html(html);
          }else{
        	  // 暂无数据
          }
        },  
        error : function(data) {  
            alert(data.msg);  
        }  
    }); 
}
