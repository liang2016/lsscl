<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<tag:page dwr="WatchListDwr"  onload="getPoint">
<html>
	<head>
		<meta http-equiv="X-UA-Compatible"
			content="IE=EmulateIE7; IE=EmulateIE9">
		<title>isZoomedIgnoreProgrammaticZoom Flag</title>
		<script type="text/javascript" src="../plugins/excanvas.min.js"></script>
		<script type="text/javascript" src="../plugins/dygraph-dev.js"></script>
		<script type="text/javascript" src="../strftime/strftime-min.js"></script>
		<script type="text/javascript" src="../plugins/dygraph-layout.js"></script>
		<script type="text/javascript" src="../plugins/dygraph-canvas.js"></script>
		<script type="text/javascript" src="../plugins/dygraph.js"></script>
		<script type="text/javascript" src="../plugins/dygraph-utils.js"></script>
     	<script type="text/javascript" src="../plugins/dygraph-interaction-model.js"></script>
         <script type="text/javascript" src="../plugins/dygraph-options-reference.js"></script>
	     <style type="text/css">
	    #div_g {
	      border: 1px solid #ff9933;
	    }
	    legend {
		 border: 1px solid #ff9933;
	     }
	     fieldset {
		 border: 1px solid #ff9933;
		 width: 600px;
		  }
		  #pointsD {
			font-size: 10px;
			text-align: right;
			border-collapse: collapse;/* 边框合并属性  */
			 width:600px;
		  }
  		#pointsD tr{
		  border: 1px solid #FF9933;
			}
		#pointsD tr td{
		border: 1px solid #FF9933;
			}
    </style>
	  
	</head>
  
  <body >
     <div id="div_g"  style=" padding:15px; margin15px; width:800px; height:400px; float: left;"></div>
     <div id="labels" style="float: left; padding:15px; margin15px;">
     </div>
    
    <div style="clear:both"></div>
    <div style="float:left ; padding:15px; margin15px;">
    <fieldset>
    <legend><fmt:message key="realtimeData.time.range"></fmt:message></legend>
    <input type="hidden" id="serverDate" value="${serverDate }">
    <FONT><fmt:message key="realtimeData.time.faiveMin"></fmt:message></FONT><input type="radio" value="300000" name="timeRadio" CHECKED onclick="zoomGraphX()">&nbsp;&nbsp;
    <FONT><fmt:message key="realtimeData.time.tenMin"></fmt:message></FONT><input type="radio" value="600000" name="timeRadio"  onclick="zoomGraphX()">&nbsp;&nbsp;
    <FONT><fmt:message key="realtimeData.time.thirtyMin"></fmt:message></FONT><input type="radio" value="1800000" name="timeRadio"  onclick="zoomGraphX()">&nbsp;&nbsp;
    <FONT><fmt:message key="realtimeData.time.oneHours"></fmt:message></FONT><input type="radio" value="3600000" name="timeRadio"  onclick="zoomGraphX()">&nbsp;&nbsp;
    <br><br>
    <br>
    </fieldset>
    </div>
        <div style="clear:both"></div>
     <div style="width: 800px; float: left; padding:15px; margin15px;">
     <!--加载观察列表-->
		<fieldset>
		<legend><fmt:message key="dox.watchList"></fmt:message> </legend><br/>
        <br/>
		 <select id="watchListSelect" value="${selectedWatchList}" onchange="watchListChanged()"
                        onmouseover="">
                  <c:forEach items="${watchListNames}" var="wl">
                    <option <c:if test="${wl.key==watchListId}">selected="selected"</c:if> value="${wl.key}">${sst:escapeLessThan(wl.value)}</option>
                  </c:forEach>
        </select>
        <br/>
        <br/>
		<b style="font-size: 14px;"><fmt:message key="realtimeData.points"></fmt:message></b><br>
		<br>
		<table id="pointsD" style="float: left;" border="1" bordercolor="#ff9933">
		<tr>
		<td><b><fmt:message key="realtimeData.points.name"></fmt:message> </b> </td><td><b><fmt:message key="realtimeData.value.newValue"></fmt:message></b></td><td><b><fmt:message key="realtimeData.value.maxValue"></fmt:message></b></td><td><b><fmt:message key="realtimeData.value.minValue"></fmt:message></b></td>
		</tr>
           <c:forEach items="${pointList}" var="pl" varStatus="index">
           <tr>
             <td>${pl.name }&nbsp;:&nbsp;<input type="checkbox" id="${pl.id}" name="isHidden" value="${pl.name}" title="${index.index}"  checked="checked" onClick="change(this)" /></td>
             <td><label id="l${pl.id}" ></label></td>   
            <td><span id="max${pl.id}">0</<span></td> 
            <td><span id="min${pl.id}">0</<span></td> 
           </tr>  
          </c:forEach>
        </table>
       </fieldset></div>
       <div id="info"></div>
  </body>
  <script type="text/javascript">
    
    //清空复选框
		  function watchListChanged(){
		   var divPoints= $("pointsD");
		   divPoints.innerHTML="";
		   loadCheckedBox();
		  }
		  function loadCheckedBox(){
		    var selectWatchList=$("watchListSelect").value;
		    WatchListDwr.getPoints(selectWatchList,function(data){
		      for(var i=0;i<data.length;i++){
		         $("pointsD").innerHTML+="<tr><td> "+data[i].name+"<input type='checkbox' checked='checked'  title='"+i+"'  id='"+data[i].id+"' name='"+data[i].name+"' value='"+data[i].name+"'title='title' onClick='change(this)'/></td><td><lable id='l"+data[i].id+"'></lable> </td>  "+"<td><span id='max"+data[i].id+"'>0</<span></td>" 
		         +"<td><span id='min"+data[i].id+"'>0</<span></td></tr>"
					if(i==data.length-1){
					    getPoint();
					}
			       }
		    });
		   window.location.reload(); 
		  }
 
		//获得点设备名称集合(用于图像lable显示)
	      	 //获得checkbox上点设备的名称
		 function getPointNameArray(){
			   //获得页面上所有input标签对象
			   var inputNames = document.getElementsByName("isHidden");
			   //初始化一个空数组,存放所有点设备(checkbox)
			   var pointNameArrayCB =[];
			   pointNameArrayCB.push("<fmt:message key="realtimeData.time"></fmt:message>");
			   for(var i=0;i<inputNames.length;i++){
			     var obj = inputNames[i];
			     if(obj.type=='checkbox'){
			   	       pointNameArrayCB.push(obj.value);  
			     }
			    }
			     return pointNameArrayCB;
				 }
		  //画布
	       var g 
	       //定义数据组
	        var pointData = [];
	       //x轴时间范围/秒
	       var xAxisRang=300;
	       //获得点设备id集合
	       var maxDate=new Date();
	       //定义y轴范围
	       var yMinRange=-1;
	       var yMaxRange=10;
    	function getPointIdArray(){
    		//获得页面上所有input标签对象
		   var inputIds = document.getElementsByTagName("input");
		   //初始化一个空数组,存放所有点设备(checkbox)
		   var pointIdArrayCB =[];
		   for(var i=0;i<inputIds.length;i++){
		     
		     if(inputIds[i].type=='checkbox'){
		      pointIdArrayCB.push(inputIds[i].id);
		     }
		    }
		     return pointIdArrayCB;
		 }
    	 //设置第一个数据
      function setFirstData(){
           pointData.length=0;
           var newData=[];
           newData.push(new Date());
           var pointIds=getPointIdArray();
           for(var j=0;j<pointIds.length;j++){
           newData[j+1]=null;
        }
         pointData.push(newData);
      }

      //dwr获取动态数据
      function getDataInfo(){
    	  //刷新时间
       	    var time=new Date();
	    var serverDate=$("serverDate").value; 
        WatchListDwr.getPointData(callBack);
	    function callBack(data){
	       var newData=[];
	       //循环对比显示动态数据
	       
	     
	        time.setYear(serverDate.substring(0,4));
	        time.setMonth(serverDate.substring(5,7)-1);
	        time.setDate(serverDate.substring(8,10));
	        for(var k=0;k<data.length;k++){
	          if(data[k].time!=null){
		            time.setHours(data[k].time.substring(0,2));
		            time.setMinutes(data[k].time.substring(3,5));
		            time.setSeconds(data[k].time.substring(6,8));
		            //同步x轴时间
		            maxDate=time;
		            newData.push(time);
		            isTime=0;
		            break;
	            }
	           else if(k==data.length-1&&data[k].time==null){
		              setTimeout("getDataInfo",1500);
		              return ;
	            }  
	            else{
		            continue;
		            }
	          
	          }       
	       
	       for(var n=0;n<data.length;n++){
	         var lableHTML="l"+data[n].id;
	         var lableMAX="max"+data[n].id;
	         var lableMIN="min"+data[n].id;
	         setRange(); 
	         if(data[n].value==null){
	           continue ;
	           }
	          else{
	          dojo.byId(lableHTML).innerHTML=data[n].value;
              if(parseFloat(dojo.byId(lableHTML).innerHTML)>parseFloat(dojo.byId(lableMAX).innerHTML)){
            	 dojo.byId(lableMAX).innerHTML=dojo.byId(lableHTML).innerHTML;
                 }   
              else if(parseFloat(dojo.byId(lableHTML).innerHTML)<parseFloat(dojo.byId(lableMIN).innerHTML)){
            	  dojo.byId(lableMIN).innerHTML=dojo.byId(lableHTML).innerHTML;
                  }
               else{
                continue;
               } 
	         
	         }
	       }  
	      
	        //如果数据没取到 重新取
	        if(newData[0]==null){
	         setTimeout("getDataInfo",1500);
	         return false;
	        }
	        
	        var  lables=document.getElementsByTagName("label"); 
	        //循环对比画图数组
            for(var m=0;m<lables.length;m++){
                 newData[m+1]=lables[m].innerHTML;
             }
	       if(newData.length<1){
            setTimeout("getDataInfo",1500);
            return false;
             }
            else{
            pointData.push(newData);  
            zoomGraphX();                 
            } 
	      if(pointData.length>=xAxisRang*lables.length/5){
	        pointData.shift();
	        }
        }
      }

      //加载是完成一系列操作
      function getPoint(){
         setFirstData();
         getDataInfo();
         painting();
     }
      //画图
       var options ;
     function painting(){
      options={title:'<fmt:message key="realtimeData.realtimeLine"></fmt:message>',
            connectSeparatedPoints: true,
            drawPoints: false,
            ylabel: '<fmt:message key="realtimeData.value"></fmt:message>',
            xlabel: '<fmt:message key="realtimeData.time"></fmt:message>',  
            xAxisLabelWidth: 150,
            showRoller: true,
            valueRange: [yMinRange, yMaxRange],
            labels: getPointNameArray(),
            labelsDiv: document.getElementById("labels"),
            labelsDivStyles: { border: '1px solid black'},
            labelsSeparateLines: true,           
            dateWindow: [maxDate-60000, maxDate]
          };	 
        g = new Dygraph(document.getElementById("div_g"), pointData,options);
              }
      function change(el) {
	        g.setVisibility(parseInt(el.title), el.checked);
	      }
 //设置y轴最大范围和最小范围
      function setRange(){
    	  var pointsArray=getPointIdArray();
    	  yMaxRange=0;
    	  yMinRange=-1;
    	  for(var i=0;i<pointsArray.length;i++){
          	if($(pointsArray[i]).checked){
               if(parseFloat($("max"+pointsArray[i]).innerHTML)>yMaxRange){
            	   yMaxRange=parseInt($("max"+pointsArray[i]).innerHTML)+5;
            	   continue;
                    }
                else if(parseFloat($("min"+pointsArray[i]).innerHTML)<yMinRange){
            	   yMinRange=parseInt($("min"+pointsArray[i]).innerHTML)-5;
                   }
              	} 
             }
    	  zoomGraphY();
          }
     //定时获取数据,更新图像
      setInterval("Refurbishing()",2000);
      	function Refurbishing() {
         getDataInfo();
         g.updateOptions({
             'file':pointData
             });
      	}

    /*
           更新x轴时间
    */
      function zoomGraphX() {
        var minDate=maxDate-checkRadio();
        g.updateOptions({
          dateWindow: [minDate, maxDate]
        });
      }
    //时间段分析
    function checkRadio(){
     var radios = document.getElementsByName("timeRadio");
      for(var i=0;i<radios.length ;i++){
       if(radios[i].checked){
        return radios[i].value;
       } 
      }
    }
    //设置y轴范围
     function zoomGraphY() {
        g.updateOptions({
          valueRange: [yMinRange, yMaxRange]
        });
      }

    </script>
</html>
</tag:page> 