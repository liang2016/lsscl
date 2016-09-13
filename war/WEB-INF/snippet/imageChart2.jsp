<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%-- The snippet used for image charts in data point detail pages. Does not require the existence 
of an image chart renderer object for the point --%>
<img src="chart/${imageChartTime}_${imageChartDuration}_${imageChartPointId}.png?w=${imageChartWidth}&h=${imageChartHeight}" 
        width="${imageChartWidth}" height="${imageChartHeight}" alt="<fmt:message key="common.genChart"/>"/>