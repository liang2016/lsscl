<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>
<div class="borderDiv marB marR">
  <table>
    <tr><td colspan="3">
      <span class="smallTitle"><fmt:message key="pointEdit.text.props"/></span>
      <tag:help id="textRenderers"/>
    </td></tr>
    
    <tr>
      <td class="formLabelRequired"><fmt:message key="pointEdit.text.type"/></td>
      <td class="formField">
        <sst:select id="textRendererSelect" onchange="textRendererEditor.change();"
                value="${form.textRenderer.typeName}">
          <c:forEach items="${textRenderers}" var="trdef">
            <sst:option value="${trdef.name}"><fmt:message key="${trdef.nameKey}"/></sst:option>
          </c:forEach>
        </sst:select>
      </td>
    </tr>
    
    <tbody id="textRendererAnalog" style="display:none;">
      <tr>
        <td class="formLabelRequired"><fmt:message key="pointEdit.text.format"/></td>
        <td class="formField">
          <input id="textRendererAnalogFormat" type="text"/>
          <tag:help id="numberFormats"/>
        </td>
      </tr>
      <tr>
        <td class="formLabel"><fmt:message key="pointEdit.text.suffix"/></td>
        <td class="formField"><input id="textRendererAnalogSuffix" type="text"/></td>
      </tr>
    </tbody>
    <tbody id="textRendererBinary" style="display:none;">
      <tr>
        <td class="formLabelRequired"><fmt:message key="pointEdit.text.zero"/></td>
        <td class="formField">
          <table cellspacing="0" cellpadding="0">
            <tr>
              <td valign="top"><input id="textRendererBinaryZero" type="text"/></td>
              <td width="10"></td>
              <td valign="top" align="center">
                <div dojoType="ColorPalette" palette="3x4" id="textRendererBinaryZeroColour"></div>
                <a href="#" onclick="textRendererEditor.handlerBinaryZeroColour(null); return false;">(<fmt:message key="pointEdit.text.default"/>)</a>
              </td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td class="formLabelRequired"><fmt:message key="pointEdit.text.one"/></td>
        <td class="formField">
          <table cellspacing="0" cellpadding="0">
            <tr>
              <td valign="top"><input id="textRendererBinaryOne" type="text"/></td>
              <td width="10"></td>
              <td valign="top" align="center">
                <div dojoType="ColorPalette" palette="3x4" id="textRendererBinaryOneColour"></div>
                <a href="#" onclick="textRendererEditor.handlerBinaryOneColour(null); return false;">(<fmt:message key="pointEdit.text.default"/>)</a>
              </td>
            </tr>
          </table>
        </td>
      </tr>
    </tbody>
    <tbody id="textRendererMultistate" style="display:none;">
      <tr>
        <td colspan="2">
          <table>
            <tr>
              <th><fmt:message key="pointEdit.text.key"/></th>
              <th><fmt:message key="pointEdit.text.text"/></th>
              <th><fmt:message key="pointEdit.text.colour"/></th>
              <td></td>
            </tr>
            <tr>
              <td valign="top"><input type="text" id="textRendererMultistateKey" value="" class="formVeryShort"/></td>
              <td valign="top"><input type="text" id="textRendererMultistateText" value="" class="formShort"/></td>
              <td valign="top" align="center">
                <div dojoType="ColorPalette" palette="3x4" id="textRendererMultistateColour"></div>
                <a href="#" onclick="textRendererEditor.handlerMultistateColour(null); return false;">(<fmt:message key="pointEdit.text.default"/>)</a>
              </td>
              <td valign="top">
                <tag:img png="add" title="common.add" onclick="return textRendererEditor.addMultistateValue();"/>
              </td>
            </tr>
            <tbody id="textRendererMultistateTable"></tbody>
          </table>
        </td>
      </tr>
    </tbody>
    <tbody id="textRendererNone" style="display:none;">
    </tbody>
    <tbody id="textRendererPlain" style="display:none;">
      <tr>
        <td class="formLabel"><fmt:message key="pointEdit.text.suffix"/></td>
        <td class="formField"><input id="textRendererPlainSuffix" type="text"/></td>
      </tr>
    </tbody>
    <tbody id="textRendererRange" style="display:none;">
      <tr>
        <td class="formLabelRequired"><fmt:message key="pointEdit.text.format"/></td>
        <td class="formField"><input id="textRendererRangeFormat" type="text"/></td>
      </tr>
      <tr>
        <td colspan="2">
          <table>
            <tr>
              <th><fmt:message key="pointEdit.text.from"/></th>
              <th><fmt:message key="pointEdit.text.to"/></th>
              <th><fmt:message key="pointEdit.text.text"/></th>
              <th><fmt:message key="pointEdit.text.colour"/></th>
              <td></td>
            </tr>
            <tr>
              <td valign="top"><input type="text" id="textRendererRangeFrom" value="" class="formVeryShort"/></td>
              <td valign="top"><input type="text" id="textRendererRangeTo" value="" class="formVeryShort"/></td>
              <td valign="top"><input type="text" id="textRendererRangeText" value=""/></td>
              <td valign="top" align="center">
                <div dojoType="ColorPalette" palette="3x4" id="textRendererRangeColour"></div>
                <a href="#" onclick="textRendererEditor.handlerRangeColour(null); return false;">(<fmt:message key="pointEdit.text.default"/>)</a>
              </td>
              <td valign="top">
                <tag:img png="add" title="common.add" onclick="return textRendererEditor.addRangeValue();"/>
              </td>
            </tr>
            <tbody id="textRendererRangeTable"></tbody>
          </table>
        </td>
      </tr>
    </tbody>
    <tbody id="textRendererTime" style="display:none;">
      <tr>
        <td class="formLabelRequired"><fmt:message key="pointEdit.text.format"/></td>
        <td class="formField">
          <input id="textRendererTimeFormat" type="text"/>
          <tag:help id="datetimeFormats"/>
        </td>
      </tr>
      <tr>
        <td class="formLabel"><fmt:message key="pointEdit.text.conversionExponent"/></td>
        <td class="formField"><input id="textRendererTimeConversionExponent" type="text"/></td>
      </tr>
    </tbody>
  </table>
</div>

<script type="text/javascript">
  dojo.require("dojo.widget.ColorPalette");
  
  function TextRendererEditor() {
      var currentTextRenderer;
      var multistateValues = new Array();
      var rangeValues = new Array();
      
      this.init = function() {
          // Colour handler events
          dojo.event.connect(dojo.widget.byId("textRendererRangeColour"), 'onColorSelect', this,
                  'handlerRangeColour');
          dojo.event.connect(dojo.widget.byId("textRendererMultistateColour"), 'onColorSelect', this,
                  'handlerMultistateColour');
          dojo.event.connect(dojo.widget.byId("textRendererBinaryZeroColour"), 'onColorSelect', this,
                  'handlerBinaryZeroColour');
          dojo.event.connect(dojo.widget.byId("textRendererBinaryOneColour"), 'onColorSelect', this,
                  'handlerBinaryOneColour');
          
          // Figure out which fields to populate with data.
          <c:choose>
            <c:when test='${form.textRenderer.typeName == "textRendererAnalog"}'>
              $set("textRendererAnalogFormat", "${sst:dquotEncode(form.textRenderer.format)}");
              $set("textRendererAnalogSuffix", "${sst:dquotEncode(form.textRenderer.suffix)}");
            </c:when>
            <c:when test='${form.textRenderer.typeName == "textRendererBinary"}'>
              $set("textRendererBinaryZero", "${sst:dquotEncode(form.textRenderer.zeroLabel)}");
              textRendererEditor.handlerBinaryZeroColour("${sst:dquotEncode(form.textRenderer.zeroColour)}");
              $set("textRendererBinaryOne", "${sst:dquotEncode(form.textRenderer.oneLabel)}");
              textRendererEditor.handlerBinaryOneColour("${sst:dquotEncode(form.textRenderer.oneColour)}");
            </c:when>
            <c:when test='${form.textRenderer.typeName == "textRendererMultistate"}'>
              <c:forEach items="${form.textRenderer.multistateValues}" var="msValue">
                textRendererEditor.addMultistateValue("${sst:dquotEncode(msValue.key)}",
                		"${sst:dquotEncode(msValue.text)}", "${sst:dquotEncode(msValue.colour)}");
              </c:forEach>
            </c:when>
            <c:when test='${form.textRenderer.typeName == "textRendererNone"}'>
            </c:when>
            <c:when test='${form.textRenderer.typeName == "textRendererPlain"}'>
              $set("textRendererPlainSuffix", "${sst:dquotEncode(form.textRenderer.suffix)}");
            </c:when>
            <c:when test='${form.textRenderer.typeName == "textRendererRange"}'>
              $set("textRendererRangeFormat", "${sst:dquotEncode(form.textRenderer.format)}");
              <c:forEach items="${form.textRenderer.rangeValues}" var="rgValue">
                textRendererEditor.addRangeValue("${rgValue.from}", "${rgValue.to}", "${sst:dquotEncode(rgValue.text)}",
                        "${sst:dquotEncode(rgValue.colour)}");
              </c:forEach>
            </c:when>
            <c:when test='${form.textRenderer.typeName == "textRendererTime"}'>
              $set("textRendererTimeFormat", "${sst:dquotEncode(form.textRenderer.format)}");
              $set("textRendererTimeConversionExponent", "${sst:dquotEncode(form.textRenderer.conversionExponent)}");
            </c:when>
            <c:otherwise>
              dojo.debug("Unknown text renderer: ${form.textRenderer.typeName}");
            </c:otherwise>
          </c:choose>
          
          textRendererEditor.change();
      }
  
      this.change = function() {
          if (currentTextRenderer)
              dojo.html.hide($(currentTextRenderer));
          currentTextRenderer = $("textRendererSelect").value
          dojo.html.show($(currentTextRenderer));
      };
      
      this.save = function(callback) {
          var typeName = $get("textRendererSelect");
          if (typeName == "textRendererAnalog")
              TextRenderDwr.setAnalogTextRenderer($get("textRendererAnalogFormat"),
                      $get("textRendererAnalogSuffix"), callback);
          else if (typeName == "textRendererBinary")
              TextRenderDwr.setBinaryTextRenderer($get("textRendererBinaryZero"), 
                      dojo.widget.byId("textRendererBinaryZeroColour").selectedColour, $get("textRendererBinaryOne"),
                      dojo.widget.byId("textRendererBinaryOneColour").selectedColour, callback);
          else if (typeName == "textRendererMultistate")
              TextRenderDwr.setMultistateRenderer(multistateValues, callback);
          else if (typeName == "textRendererNone")
              TextRenderDwr.setNoneRenderer(callback);
          else if (typeName == "textRendererPlain")
              TextRenderDwr.setPlainRenderer($get("textRendererPlainSuffix"), callback);
          else if (typeName == "textRendererRange")
              TextRenderDwr.setRangeRenderer($get("textRendererRangeFormat"), rangeValues, callback);
          else if (typeName == "textRendererTime")
              TextRenderDwr.setTimeTextRenderer($get("textRendererTimeFormat"),
                      $get("textRendererTimeConversionExponent"), callback);
          else
              callback();
      };
      
      //
      // List objects
      this.MultistateValue = function() {
          this.key;
          this.text;
          this.colour;
      };
      
      this.RangeValue = function() {
          this.from;
          this.to;
          this.text;
          this.colour;
      };
      
      //
      // Multistate list manipulation
      this.addMultistateValue = function(theKey, text, colour) {
          if (!theKey)
              theKey = $get("textRendererMultistateKey");
          var theNumericKey = parseInt(theKey);
          if (isNaN(theNumericKey)) {
              alert("<fmt:message key="pointEdit.text.errorParsingKey"/>");
              return false;
          }
          for (var i=multistateValues.length-1; i>=0; i--) {
              if (multistateValues[i].key == theNumericKey) {
                  //alert("<fmt:message key="pointEdit.text.listContainsKey"/> "+ theNumericKey);
                  return false;
              }
          }
          
          var theValue = new this.MultistateValue();
          theValue.key = theNumericKey;
          if (text)
              theValue.text = text;
          else
              theValue.text = $get("textRendererMultistateText");
          if (colour)
              theValue.colour = colour;
          else
              theValue.colour = dojo.widget.byId("textRendererMultistateColour").selectedColour;
          multistateValues[multistateValues.length] = theValue;
          this.sortMultistateValues();
          this.refreshMultistateList();
          $set("textRendererMultistateKey", theNumericKey+1);
          
          return false;
      };
      
      this.removeMultistateValue = function(theValue) {
          for (var i=multistateValues.length-1; i>=0; i--) {
              if (multistateValues[i].key == theValue)
                  multistateValues.splice(i, 1);
          }
          this.refreshMultistateList();
          return false;
      };
      
      this.sortMultistateValues = function() {
          multistateValues.sort( function(a,b) { return a.key-b.key; } );
      };
      
      this.refreshMultistateList = function() {
          dwr.util.removeAllRows("textRendererMultistateTable");
          dwr.util.addRows("textRendererMultistateTable", multistateValues, [
                  function(data) { return data.key; },
                  function(data) { 
                      if (data.colour)
                          return "<span style='color:"+ data.colour +"'>"+ data.text +"</span>";
                      return data.text;
                  },
                  function(data) {
                      return "<a href='#' onclick='return textRendererEditor.removeMultistateValue("+ data.key +
                             ");'><img src='images/bullet_delete.png' width='16' height='16' border='0' "+
                             "title='<fmt:message key="common.delete"/>'/><\/a>";
                  }
                  ], null);
      };
      
      //
      // Range list manipulation
      this.addRangeValue = function(theFrom, theTo, text, colour) {
          if (!theFrom&&theFrom!=0)
              theFrom = parseFloat($get("textRendererRangeFrom"));
          if (isNaN(theFrom)) {
              alert("<fmt:message key="pointEdit.text.errorParsingFrom"/>");
              return false;
          }
          
          if (!theTo&&theTo!=0)
              theTo = parseFloat($get("textRendererRangeTo"));
          if (isNaN(theTo)) {
              alert("<fmt:message key="pointEdit.text.errorParsingTo"/>");
              return false;
          }
          
          if (isNaN(theTo >= theFrom)) {
              alert("<fmt:message key="pointEdit.text.toGreaterThanFrom"/>");
              return false;
          }
          
          for (var i=0; i<rangeValues.length; i++) {
              if (rangeValues[i].from == theFrom && rangeValues[i].to == theTo) {
                  alert("<fmt:message key="pointEdit.text.listContainsRange"/> "+ theFrom +" - "+ theTo);
                  return false;
              }
          }
          
          var theValue = new this.RangeValue();
          theValue.from = theFrom;
          theValue.to = theTo;
          if (text)
              theValue.text = text;
          else
              theValue.text = $get("textRendererRangeText");
          if (colour)
              theValue.colour = colour;
          else
              theValue.colour = dojo.widget.byId("textRendererRangeColour").selectedColour;
          rangeValues[rangeValues.length] = theValue;
          this.sortRangeValues();
          this.refreshRangeList();
          $set("textRendererRangeFrom", theTo);
          $set("textRendererRangeTo", theTo + (theTo - theFrom));
          return false;
      };
      
      this.removeRangeValue = function(theFrom, theTo) {
          for (var i=rangeValues.length-1; i>=0; i--) {
              if (rangeValues[i].from == theFrom && rangeValues[i].to == theTo)
                  rangeValues.splice(i, 1);
          }
          this.refreshRangeList();
          return false;
      };
      
      this.sortRangeValues = function() {
          rangeValues.sort( function(a,b) {
              if (a.from == b.from)
                  return a.to-b.to;
              return a.from-b.from;
          });
      };
      
      this.refreshRangeList = function() {
          dwr.util.removeAllRows("textRendererRangeTable");
          dwr.util.addRows("textRendererRangeTable", rangeValues, [
                  function(data) { return data.from; },
                  function(data) { return data.to; },
                  function(data) { 
                      if (data.colour)
                          return "<span style='color:"+ data.colour +"'>"+ data.text +"</span>";
                      return data.text;
                  },
                  function(data) {
                      return "<a href='#' onclick='return textRendererEditor.removeRangeValue("+
                             data.from +","+ data.to +");'><img src='images/bullet_delete.png' width='16' "+
                             "height='16' border='0' title='<fmt:message key="common.delete"/>'/><\/a>";
                  }
                  ], null);
      };
      
      //
      // Color handling
      this.handlerRangeColour = function(colour) {
          dojo.widget.byId("textRendererRangeColour").selectedColour = colour;
          $("textRendererRangeText").style.color = colour;
      };
      this.handlerMultistateColour = function(colour) {
          dojo.widget.byId("textRendererMultistateColour").selectedColour = colour;
          $("textRendererMultistateText").style.color = colour;
      };
      this.handlerBinaryZeroColour = function(colour) {
          dojo.widget.byId("textRendererBinaryZeroColour").selectedColour = colour;
          $("textRendererBinaryZero").style.color = colour;
      };
      this.handlerBinaryOneColour = function(colour) {
          dojo.widget.byId("textRendererBinaryOneColour").selectedColour = colour;
          $("textRendererBinaryOne").style.color = colour;
      };
  }
  var textRendererEditor = new TextRendererEditor();
  dojo.addOnLoad(textRendererEditor, "init");
  
  
	function showTextRendererCB(response){
		clearTextRenderer();
		$j("#textRendererSelect").html("");
		var content = "";
		for(var i =0;i<response.data.definitionList.length;i++){
			var data = response.data.definitionList;
			var textRenderer = response.data.textRenderer;
			var temp = data[i].nameKey;
			var name = data[i].name;
			if(temp=="textRenderer.analog"){
				content += "<option value='"+data[i].name+"'><fmt:message key='textRenderer.analog'/></option>";
			}else if(temp=="textRenderer.binary"){
				content += "<option value='"+data[i].name+"'><fmt:message key='textRenderer.binary'/></option>";
			}else if(temp=="textRenderer.multistate"){
				content += "<option value='"+data[i].name+"'><fmt:message key='textRenderer.multistate'/></option>";
			}else if(temp=="textRenderer.none"){
				content += "<option value='"+data[i].name+"'><fmt:message key='textRenderer.none'/></option>";
			}else if(temp=="textRenderer.plain"){
				content += "<option value='"+data[i].name+"'><fmt:message key='textRenderer.plain'/></option>";
			}else if(temp=="textRenderer.range"){
				content += "<option value='"+data[i].name+"'><fmt:message key='textRenderer.range'/></option>";
			}else if(temp=="textRenderer.time"){
				content += "<option value='"+data[i].name+"'><fmt:message key='textRenderer.time'/></option>";
			} 
		}
		$j("#textRendererSelect").html(content);
		if(textRenderer==null){
        	currentTextRenderer = $("textRendererSelect").value;
        	if(currentTextRenderer=="")
        		return;
			textRendererEditor.change();
			$set("textRendererAnalogFormat", "0.00");
			return;
		}
		var typename = textRenderer.typeName;
		$j("#textRendererSelect").val(typename);
        if(typename=="textRendererAnalog"){
            $set("textRendererAnalogFormat", textRenderer.format);
        	$set("textRendererAnalogSuffix", textRenderer.suffix);
        }else if(typename=="textRendererBinary"){
            $set("textRendererBinaryZero", textRenderer.zeroLabel);
            textRendererEditor.handlerBinaryZeroColour(textRenderer.zeroColour);
            $set("textRendererBinaryOne", textRenderer.oneLabel);
            textRendererEditor.handlerBinaryOneColour(textRenderer.oneColour);
        }else if(typename=="textRendererMultistate"){
            var valueList = textRenderer.multistateValues
            for(var i =0;i<valueList.length;i++){
        	  	var msValue = valueList[i];
              	textRendererEditor.addMultistateValue(msValue.key,msValue.text, msValue.colour);
            }
        }else if(typename=="textRendererNone"){
        }else if(typename=="textRendererPlain"){
         	$set("textRendererPlainSuffix", textRenderer.suffix);
        }else if(typename=="textRendererRange"){
         	$set("textRendererRangeFormat", textRenderer.format);
         	var valueList = textRenderer.rangeValues;
         	for(var i =0;i<valueList.length;i++){
         		var rgValue = valueList[i];
         		textRendererEditor.addRangeValue(rgValue.from,rgValue.to,rgValue.text,rgValue.colour);
         	}
        }else if(typename=="textRendererTime"){
            $set("textRendererTimeFormat", textRenderer.format);
            $set("textRendererTimeConversionExponent", textRenderer.conversionExponent);
        }else{
            dojo.debug("Unknown text renderer: "+textRenderer.typeName);
        }
        currentTextRenderer = typename;
		textRendererEditor.change();
	}
    
    	
	function clearTextRenderer(){
		$set("textRendererAnalogFormat", "");
        $set("textRendererAnalogSuffix", "");
		$set("textRendererBinaryZero", "");
		$set("textRendererBinaryOne", "");
		$set("textRendererPlainSuffix", "");
		$set("textRendererRangeFormat", "");
		$set("textRendererTimeFormat", "");
        $set("textRendererTimeConversionExponent", "");
        $set("textRendererRangeFrom","");
        $set("textRendererRangeTo","");
        $set("textRendererRangeText","");
	}

</script>