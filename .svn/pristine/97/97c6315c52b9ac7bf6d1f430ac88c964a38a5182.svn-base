<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">  
    <META HTTP-EQUIV="Pragma" CONTENT="no-cache">  
    <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">  
    <META HTTP-EQUIV="Expires" CONTENT="0">    
  <title>Simple Test</title>
 < script language="javascript" type="text/javascript" src="../plugins/excanvas.js"></script>
    <script type='text/javascript' src='/dwr/interface/WatchListDwr.js'></script>
  <script type='text/javascript' src='/dwr/engine.js'></script>
    <script type='text/javascript' src='/dwr/util.js'></script>
  <link rel="stylesheet" type="text/css" href="../plugins/jquery.jqplot.css" />
  <link rel="stylesheet" type="text/css" href="examples.css" />
  
  <!-- BEGIN: load jquery -->
  <script language="javascript" type="text/javascript" src="../plugins/jquery-1.4.4.min.js"></script>
  <!-- END: load jquery -->
  
  <!-- BEGIN: load jqplot -->
  <script language="javascript" type="text/javascript" src="../plugins/jquery.jqplot.js"></script>
  <script language="javascript" type="text/javascript" src="../plugins/jqplot.logAxisRenderer.js"></script>
  <script language="javascript" type="text/javascript" src="../plugins/jqplot.dateAxisRenderer.js"></script>
  <script language="javascript" type="text/javascript" src="../plugins/jqplot.cursor.js"></script>
  <script language="javascript" type="text/javascript" src="../plugins/jqplot.highlighter.js"></script>

  <!-- END: load jqplot -->
  <style type="text/css" media="screen">
    .jqplot-axis {
      font-size: 0.85em;
    }
    .jqplot-axis{
      font-size: 0.75em;
    }
  </style>
  <script type="text/javascript">
  	var str="";
  	function showtime() {
		now = new Date();
		now.setTime(now.getTime()+1800000);
	   var temp = now.getHours();
		if (temp < 10)
			str += "0";
			str += temp + ":";	 
		temp = now.getMinutes();
		if (temp < 10){
			str += "0";}	
		str += temp + ":";
		 temp = now.getSeconds();
		if (temp < 10)
			str += "0";
		str += temp;
		document.getElementById("Head1Right_Time").innerHTML = str;
		//ctroltime = setTimeout("showtime()", 1000);
	}
  
  </script>
  <script type="text/javascript" language="javascript">
  //数组
   var goog=[["13:28:00"],0] ;
   var googName="";
   var plot_1;
   var del_head=0;
   var colors=['#8F006D','#5BBD2B','#F9F400','#489620'];
   
   function getData(){
     WatchListDwr.getPointData(callBack);
     function callBack(data){
     googName=data[0].id;
      if(data==null){
        getData();
      }
      if(data[0].time==null||data[0].value==null){
         getData();
      }
      if(data[0].time!=null)
      {
      	if(del_head==0)
      	{
      		del_head=1;
      		goog.pop();
      	}
      	goog.push([data[0].time , parseInt(data[0].value)]);      	
      }
    }
  }
  function load(){
      showtime();
      getData();
      $.jqplot.config.enablePlugins = true;
      plot_1 = $.jqplot('chart', [goog], {
      legend:{show:true},
       seriesDefaults: {
                   //线宽
                	lineWidth: 1,
                	//阴影效果
                    shadow: false,
                	//显示折点
                	showMarker: false,
                	markerOptions: { 
                	// 是否在图中显示数据点 
                	size:2,
                	show: true                  	
                },
                	//是否可拖动
                	isDragable: false
                },
      grid:{
          // gridLineColor: '#ccff99',
           background: '#99cccc'
         //  borderColor:'#cc6633',
          // borderWidth: 3.0,
              
      },
      series:[
            //第一根折线,属性
              {
              //名称
               label:googName,
               //指定颜色
               color:colors[0]
               }
                ],
              //坐标点显示层
        cursor:{tooltipLocation:'nw', zoom:true, showTooltipGridPosition:true},           
        title: '测试',
         //采用不同颜色
      axesDefaults:{useSeriesColor: true},
      axes: {
        xaxis: {
          renderer:$.jqplot.DateAxisRenderer,
           min:goog[0][0],
           max:str,
           tickInterval: "10 minutes",
          //日期显示格式
        	tickOptions:{formatString:"%H:%M:%S"}
        },
     yaxis: {
        //渲染方式
            renderer: $.jqplot.LogAxisRenderer,
            //最小值/最大值
            min:10,
            max:1000,
           tickOptions:{formatString:'A%.2f'}
            
            }
       }
      }
    );
   change();
}
 
  function change(){

   getData();
   //plot_1.axes.xaxis.max = '6/05/2011';
   
   plot_1.axes.xaxis.min = goog[0][0];
   plot_1.axes.xaxis.max = goog[goog.length-1][0];
   plot_1.series[0].data = goog;
   plot_1.replot();
   setTimeout("change()", 2000); 
    
  }


</script>
  </head>
  <body onload="load()">
<?php include "nav.inc"; ?>
    <div id="chart" style="margin-top:20px; margin-left:20px; width:1000px; height:400px;" ></div>
    <div style="padding-top:20px"><div id="Head1Right_Time"></div> 
    <button value="reset" type="button" onclick="plot.resetZoom();">Zoom Out </button>
    </div>
 <input type="button" onclick="showtime()">
  </body>
</html>