<#--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
-->
<#--
<#if evt.alarmLevel gt 0>


 <@fmt key="evt.remind"/>,
<@fmt key="ftl.note"/>: <#if evt.eventType.getEventSourceId()==6><@fmt key="ftl.rtn"/><#else><@fmt key="ftl.manual"/></#if>



</#if>
-->
