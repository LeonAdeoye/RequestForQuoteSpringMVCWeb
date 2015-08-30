<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html;charset=ISO-8859-1">
		
		<link href="<c:url value="/resources/css/common.css" />" rel="stylesheet" type="text/css">
		<link href="<c:url value="/resources/css/bankHolidays.css" />" rel="stylesheet" type="text/css">
		<link href="<c:url value="/resources/css/jquery-ui.min.css" />" rel="stylesheet" type="text/css">
		<link href="<c:url value="/resources/slickGrid/examples.css" />" rel="stylesheet" type="text/css">
		<link href="<c:url value="/resources/slickGrid/slick.grid.css" />" rel="stylesheet" type="text/css">
		<link href="<c:url value="/resources/slickGrid/css/smoothness/jquery-ui-1.8.16.custom.css" />" rel="stylesheet" type="text/css">
		<link href="<c:url value="/resources/slickGrid/controls/slick.columnpicker.css" />" rel="stylesheet" type="text/css">

		<script type="text/javascript" src="<c:url value="/resources/js/jquery-2.1.3.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/js/jquery-ui.min.js" />"></script>				
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/lib/jquery.event.drag-2.2.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/js/jquery.browser.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/js/jquery-input-file-text.js" />"></script>
								
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/slick.core.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/slick.formatters.js" />"></script>	
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/slick.editors.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/slick.groupitemmetadataprovider.js" />"></script>			
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/slick.grid.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/slick.dataview.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/plugins/slick.rowselectionmodel.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/controls/slick.columnpicker.js" />"></script>
		
		<script type="text/javascript" src="<c:url value="/resources/js/utilities.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/js/bankHolidays.js" />"></script>		
				
		<script type="text/javascript">		
			var contextPath='<%=request.getContextPath()%>'
		
			if (typeof jQuery == 'undefined')				 
			    alert("WARNING: jQuery library is NOT loaded!");		
		</script>		
		<title><spring:message code="bankHolidays.maintenance.label"/></title>
	</head>
	<body>
		
		<div id="bankHolidays-new-bankHoliday" class="new-bankHoliday-class float-left">				
			<input id="new-bankHoliday-location" class="float-left new-bankHoliday-input-class" path="location" value="Enter location..." type="text" default_value="Enter location..." />
			<input id="new-bankHoliday-date" class="float-left new-bankHoliday-input-class" path="bankHolidayDate" value="Enter date..." type="text" default_value="Enter date..." />
			<input type="file" class="float-right" id="import-btn-class new-bankHoliday-import-btn" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" />			
		</div>
			
		<span class='loading-indicator' style="display:none;"><label><spring:message code="loading.indicator.label"/></label></span>

		<ul id="bankHolidayContextMenu" class="contextMenu">
		  <b><spring:message code="contextMenu.operation.title.label"/></b>
		  <li data="VALIDATE"><spring:message code="contextMenu.operation.validate.menuitem"/></li>
		  <li data="INVALIDATE"><spring:message code="contextMenu.operation.invalidate.menuitem"/></li>
		  <li data="REFRESH"><spring:message code="contextMenu.operation.refresh.menuitem"/></li>
		</ul>
		
		<div style="width:505px;">
    		<div id="bankHolidaysGrid" style="width:505px;height:595px;"></div>
		</div>
	</body>
</html>