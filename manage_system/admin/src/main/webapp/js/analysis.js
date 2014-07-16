
// 列表/图表模式切换
$(document).on("click", ".incito-chart", function() {
	if ($(".incito-chart").html() == "列表模式") {
		$(".incito-chart").html("图表模式");
		$(".analysis-chart").attr("style", "display:none");
		$(".analysis-table").removeAttr("style");
		$(".analysis-page").removeAttr("style");
	} else {
		$(".incito-chart").html("列表模式");
		$(".analysis-table").attr("style", "display:none");
		$(".analysis-chart").removeAttr("style");
		$(".analysis-page").attr("style", "display:none");
	}
});

//统计时段与时间选择控件联动
$(".intico-dateSelect").change(function() {
	var changeVal = $(".intico-dateSelect").val();
	var startTime;
	var curr = new Date();
	var currMonth = curr.getMonth() + 1;
	var endTime = curr.getFullYear() + "-" + currMonth;
	if (changeVal == "0") {// 本月
		startTime = curr.getFullYear() + "-" + currMonth;
	} else if (changeVal == "1") {// 近三个月
		if ((currMonth - 2) > 0) {
			startTime = curr.getFullYear()+"-"+(currMonth - 2);
		} else {
			startTime = (curr.getFullYear() - 1)+"-"+(currMonth + 10);
		}
	} else if (changeVal == "2") {// 本年度
		startTime = curr.getFullYear() + "-1";
	} else {
		endTime=null;
	};
	if ($("[name='type']").val() == "1" && startTime != null && endTime != null) {
		startTime = startTime + "-1";
		endTime = endTime + "-" + (curr.getDay() + 1);
	}
	$("[name='startTime']").val(startTime);
	$("[name='endTime']").val(endTime);
});

