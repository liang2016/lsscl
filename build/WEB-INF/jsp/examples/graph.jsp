<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>   
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<tag:page dwr="DynamicAndHistoryPointDwr" js="DynamicAndHistoryPointDwr" onload="">
<html>
	<head>
		<meta http-equiv="X-UA-Compatible"
		content="IE=EmulateIE7; IE=EmulateIE9">
		<title>isZoomedIgnoreProgrammaticZoom Flag</title>
		<script type="text/javascript" src="../plugins/excanvas.js"></script>
	<script type="text/javascript" src="../strftime/strftime-min.js"></script> 
	<script type="text/javascript" src="../plugins/dygraph-layout.js"></script>
	<script type="text/javascript" src="../plugins/dygraph-canvas.js"></script>
	<script type="text/javascript" src="../plugins/dygraph.js"></script>
	<script type="text/javascript" src="../plugins/dygraph-utils.js"></script>
    <script type="text/javascript" src="../plugins/dygraph-interaction-model.js"></script>
	<script type="text/javascript" src="../plugins/dygraph-options-reference.js"></script>
	 <script type="text/javascript">function watchListChanged(){var divPoints=$("pointsD");divPoints.innerHTML="";loadCheckedBox();}function loadCheckedBox(){var selectWatchList=$("watchListSelect").value;DynamicAndHistoryPointDwr.getPoints(selectWatchList,function(data){for(var i=0;i<data.length;i++){$("pointsD").innerHTML+="<input type='checkbox' checked='checked'  title='"+i+"'  id='"+data[i].id+"' name='"+data[i].name+"' value='"+data[i].name+"'title='title' onClick='change(this)'/>"+data[i].name+"<br>"}});}</script><script type="text/javascript">function getPointIdArray(){var inputIds=document.getElementsByTagName("input");var pointIdArrayCB=[];for(var i=0;i<inputIds.length;i++){var obj=inputIds[i];if(obj.type=='checkbox'){pointIdArrayCB.push(obj.id);}}return pointIdArrayCB;}function getPointNameArray(){var inputNames=document.getElementsByTagName("input");var pointNameArrayCB=[];pointNameArrayCB.push("时间");for(var i=0;i<inputNames.length;i++){var obj=inputNames[i];if(obj.type=='checkbox'){pointNameArrayCB.push(obj.value);}}return pointNameArrayCB;}function dwrLoading(imageSrc){var loadingImage;if(imageSrc)loadingImage=imageSrc;else loadingImage="images/loading.gif";dwr.engine.setPreHook(function(){var disabledImageZone=$('disabledImageZone');if(!disabledImageZone){disabledImageZone=document.createElement('div');disabledImageZone.setAttribute('id','disabledImageZone');disabledImageZone.style.position="absolute";disabledImageZone.style.zIndex="1000";disabledImageZone.style.left="0px";disabledImageZone.style.top="0px";disabledImageZone.style.width="100%";disabledImageZone.style.height="100%";var imageZone=document.createElement('img');imageZone.setAttribute('id','imageZone');imageZone.setAttribute('src',imageSrc);imageZone.style.position="absolute";imageZone.style.top="0px";imageZone.style.right="0px";disabledImageZone.appendChild(imageZone);document.body.appendChild(disabledImageZone);}else{$('imageZone').src=imageSrc;disabledImageZone.style.display='block';}});DWREngine.setPostHook(function(){$('disabledImageZone').style.display='none';});}</script><script type="text/javascript">var one=0;var two=50000;var intervalID=0;var allData=new Array();function getSize(){intervalID=window.setInterval("getDateBack()",5000);}function getDateBack(){if(two>150000){window.clearInterval(intervalID);}var idxArray=[];var compareArray=[];var pointArray=[];var lineNum;var maxPoint;var graphDataArray=[];idxArray=getPointIdArray();lineNum=idxArray.length;if(lineNum<=0)return;getInputDate();dwrLoading('images/loading.gif');DynamicAndHistoryPointDwr.getPointHistoryData(idxArray,fromYear,fromMonth,fromDay,fromHour,fromMinute,fromSecond,toYear,toMonth,toDay,toHour,toMinute,toSecond,one,two,callBack);function callBack(data){allData+=data;$("time").innerHTML+="<br>时间':"+new Date().getTime()+"";one+=50000;two+=50000;}}</script><script type="text/javascript">function init(){setNowTime();}function setNowTime(){now=new Date();now.setTime(now.getTime());$("fromYear").value=now.getFullYear();$("fromMonth").value=now.getMonth()+1;$("fromDay").value=now.getDate()-1;$("fromHour").value=now.getHours();$("fromMinute").value=now.getMinutes();$("fromSecond").value=now.getSeconds();$("toYear").value=now.getFullYear();$("toMonth").value=now.getMonth()+1;$("toDay").value=now.getDate();$("toHour").value=now.getHours();$("toMinute").value=now.getMinutes();$("toSecond").value=now.getSeconds();}var fromYear,fromMonth,fromDay,fromHour,fromMinute,fromSecond,toYear,toMonth,toDay,toHour,toMinute,toSecond;function getInputDate(){fromYear=$("fromYear").value;fromMonth=$("fromMonth").value;fromDay=$("fromDay").value;fromHour=$("fromHour").value;fromMinute=$("fromMinute").value;fromSecond=$("fromSecond").value;toYear=$("toYear").value;toMonth=$("toMonth").value;toDay=$("toDay").value;toHour=$("toHour").value;toMinute=$("toMinute").value;toSecond=$("toSecond").value;}function getIdx(idx,pArray){var idxA=pArray.length;for(var i=0;i<pArray.length;i++){if(idx==pArray[i]){idxA=i;break;}}return idxA;}var startTime=0;function getLocTime(nS){return new Date(parseInt(nS)*1000);}var g;function getPoint(){var idxArray=[];var compareArray=[];var pointArray=[];var lineNum;var maxPoint;var graphDataArray=[];idxArray=getPointIdArray();lineNum=idxArray.length;if(lineNum<=0)return;getInputDate();dwrLoading('images/loading.gif');DynamicAndHistoryPointDwr.getPointHistoryData(idxArray,fromYear,fromMonth,fromDay,fromHour,fromMinute,fromSecond,toYear,toMonth,toDay,toHour,toMinute,toSecond,0,200000,callBack);function callBack(points){$("time").innerHTML+="<br>数据加载完毕:"+new Date().getSeconds()+"";if(points.length<1){alert("查询无数据,请重新输入查询日期");return;}$("div_g").style.display='none';$("time").innerHTML+="<br>开始'将数据规整到数组1':"+new Date().getSeconds()+"";for(var i=0;i<idxArray.length;i++){pointArray[i]=new Array();}$("time").innerHTML+="<br>开始'将数据规整到数组2':"+new Date().getSeconds()+"";for(var i=0;i<points.length;i++){pointArray[getIdx(points[i].id,idxArray)].push([points[i].time,points[i].value]);}$("time").innerHTML+="<br>开始'将个数和偏移写入compareArray':"+new Date().getSeconds()+"";for(var i=0;i<idxArray.length;i++){compareArray.push([pointArray[i].length,0]);}$("time").innerHTML+="<br>开始整合数组:"+new Date().getSeconds()+"";while(1){startTime=0xFFFFFFFFFFFFFFFF;for(var i=0;i<compareArray.length;i++){if(compareArray[i][0]==compareArray[i][1])continue;if(startTime>pointArray[i][compareArray[i][1]][0])startTime=pointArray[i][compareArray[i][1]][0];}if(startTime==0xFFFFFFFFFFFFFFFF)break;var midArray=[];midArray.push(getLocTime(startTime/1000));for(var i=0;i<compareArray.length;i++){if(compareArray[i][0]==compareArray[i][1]){midArray.push(null);continue;}if(pointArray[i][compareArray[i][1]][0]==startTime){midArray.push(pointArray[i][compareArray[i][1]][1]);compareArray[i][1]++;}else midArray.push(null);}graphDataArray.push(midArray);}alert(graphDataArray);$("time").innerHTML+="<br>开始画图:"+new Date().getSeconds()+"";g=new Dygraph(document.getElementById("div_g"),graphDataArray,{title:'点设备历史记录',legend:'always',connectSeparatedPoints:true,drawPoints:false,xAxisLabelWidth:150,showRoller:true,valueRange:[-100,1000],labels:getPointNameArray(),labelsDiv:document.getElementById("labels"),labelsDivStyles:{'text-align':'right','background':'none'},labelsSeparateLines:true,visibility:[true,true,true]});$("div_g").style.display='block';$("time").innerHTML+="<br>处理完毕:"+new Date().getSeconds()+"";graphDataArray=null;}$("time").innerHTML+="<br>开始查询:"+new Date().getSeconds()+"";}function change(el){g.setVisibility(parseInt(el.title),el.checked);}</script>
	</head>
 
  <body onload="init()">
        <div id="disabledImageZone" style="display: none">
	         <img alt="" src="" id="imageZone">
	     </div>
	    <div id="div_g" class="image" style="width:1024px; height:400px; display: none; " >
		</div>
		 <div id="labels" style="float: left;  width: 240px;height: 400px;" >
		</div>
		
      <div>
         <fieldset >
		 <legend>选择时间</legend>
		     从<input type="text" id="fromYear" value="" style="width: 40px;">年 
		  <select id="fromMonth">
			  <option value="1">一月 </option>
			  <option value="2">二月 </option>
			  <option value="3">三月 </option>
			  <option value="4">四月 </option>
			  <option value="5">五月 </option>
			  <option value="6">六月 </option>
			  <option value="7">七月 </option>
			  <option value="8">八月 </option>
			  <option value="9">九月 </option>
			  <option value="10">十月</option>
			  <option value="11">十一月</option>       
			  <option value="12">十二月</option>
		  </select>    
		  <select id="fromDay">
		          <c:forEach var="dayF" begin="1" end="31">
		            <option value="${dayF }"><c:if test="${dayF<10}">0</c:if>${dayF}</option>&
		          </c:forEach>
		  </select>
		      日,
		  <select id="fromHour">
			      <c:forEach var="hourF" begin="0" end="23">
			       <option value="${hourF}"><c:if test="${hourF<10}">0</c:if>${hourF}</option>
			      </c:forEach>
		  </select>
		  :
		  <select id="fromMinute">
		         <c:forEach var="minuteF" begin="0" end="59">
		           <option value="${minuteF}"><c:if test="${minuteF<10}">0</c:if>${minuteF}</option>
		         </c:forEach>
		  </select>
          :	
           <select id="fromSecond">
		         <c:forEach var="secondF" begin="0" end="59">
		           <option value="${secondF}"><c:if test="${secondF<10}">0</c:if>${secondF}</option>
		         </c:forEach>
		  </select>
		  <br/>	  
		      至<input type="text" id="toYear" value="2011" style="width: 40px;">年
		  <select id="toMonth">
		  <option value="1">一月 </option>
		  <option value="2">二月 </option>
		  <option value="3">三月 </option>
		  <option value="4">四月 </option>
		  <option value="5">五月 </option>
		  <option value="6">六月 </option>
		  <option value="7">七月 </option>
		  <option value="8">八月 </option>
		  <option value="9">九月 </option>
		  <option value="10">十月</option>
		  <option value="11">十一月</option>       
		  <option value="12">十二月</option>
		  </select>     
		  <select id="toDay">
		          <c:forEach var="dayT" begin="1" end="31">
		            <option value="${dayT}"><c:if test="${dayT<10}">0</c:if>${dayT}</option>
		          </c:forEach>
		  </select>
		  日,
		  <select id="toHour">
			      <c:forEach var="hourT" begin="0" end="23">
			       <option value="${hourT}"><c:if test="${hourT<10}">0</c:if>${hourT}</option>
			      </c:forEach>
		  </select>
		  :
		  <select id="toMinute">
		         <c:forEach var="minuteT" begin="0" end="59">
		           <option value="${minuteT}"><c:if test="${minuteT<10}">0</c:if>${minuteT}</option>
		         </c:forEach>
		  </select>
	      :
	      <select id="toSecond">
		         <c:forEach var="secondT" begin="0" end="59">
		           <option value="${secondT}"><c:if test="${secondT<10}">0</c:if>${secondT}</option>
		         </c:forEach>
		  </select>
		  <input id="getGraphBUT" type="button" value="获取图表" title="获取图表" onclick="getPoint()" >
		  </fieldset>		
		<!-- 加载观察列表 -->
		<fieldset>
		
		<legend>观察列表</legend>
		 <select id="watchListSelect" value="${selectedWatchList}" onchange="watchListChanged()"
                        onmouseover="">
                  <c:forEach items="${watchListNames}" var="wl">
                    <option <c:if test="${wl.key==watchListId}">selected="selected"</c:if> value="${wl.key}">${sst:escapeLessThan(wl.value)}</option>
                  </c:forEach>
        </select>
		<!-- 动态加载点设备 -->   
		<legend>点设备</legend>
		<div id="pointsD" style="float: left;">
           <c:forEach items="${pointList}" var="pl" varStatus="index">
           <div style="width: 220px;">
           <input type="checkbox" id="${pl.id}" name="${pl.name }" value="${pl.name}" title="${index.index}"  checked="checked" onClick="change(this)" />${pl.name }  
          </div>
          </c:forEach>
        </div>
       </fieldset>
     </div>
     <div id="time"><input type="button" value="定时器" onclick="getSize()"/> </div>
  </body>
</html>
</tag:page>
