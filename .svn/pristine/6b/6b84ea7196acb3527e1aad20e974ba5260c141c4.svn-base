<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%><%@include file="/WEB-INF/tags/decl.tagf"%><%--
--%><%@tag body-content="empty"%><%--
--%><%@attribute name="username" required="true"%>
<script type="text/javascript">var djConfig = { isDebug: true };</script>
<!-- script type="text/javascript" src="http://o.aolcdn.com/dojo/0.4.2/dojo.js"></script -->
<script type="text/javascript" src="resources/dojo/dojo.js"></script>
<script type="text/javascript" src="dwr/engine.js"></script>
<script type="text/javascript" src="dwr/util.js"></script>
<script type="text/javascript" src="resources/common.js"></script>
<script type="text/javascript" src="dwr/interface/MiscDwr.js"></script>
<script type="text/javascript" src="dwr/interface/CustomViewDwr.js"></script>
<script type="text/javascript" src="resources/view.js"></script>
<mango:viewInit username="${username}"/>
<script type="text/javascript">
  dwr.util.setEscapeHtml(false);
  mango.view.initCustomView();
  dojo.addOnLoad(mango.longPoll.start);
  
  function setPoint(xid, value, callback) {
      mango.view.setPoint(xid, value, callback);
  }
</script>
