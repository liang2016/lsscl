<#--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
-->
<#include "include/eventHeader.ftl">
<table>
  <tr><td colspan="3" class="smallTitle">${instanceDescription} - <@fmt key="ftl.eventInactive"/></td></tr>
  <tr><td colspan="3">${evt.prettyRtnTimestamp} - <b><@fmt message=evt.rtnMessage/></b></td></tr>
  <tr><td colspan="3"></td></tr>
  <tr><td colspan="3" class="smallTitle"><@fmt key="ftl.originalInformation"/></td></tr>
  <#include "include/eventData.ftl">
</table>
<#include "include/eventFooter.ftl">