<#--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
-->
<#if evt.alarmLevel gt 0>
  <tr>
    <td valign="top"><img src="cid:<@img src="exclamation.png"/>" alt="<@fmt key="ftl.note"/>" title="<@fmt key="ftl.note"/>"/></td>
    <td colspan="2">
      <#if evt.eventType.getEventSourceId()==6>
        <@fmt key="ftl.rtn"/>
      <#else>
        <@fmt key="ftl.manual"/>
      </#if>
      &nbsp;&nbsp;<@fmt key="evt.remind"/>
    </td>
  </tr>
</#if>
