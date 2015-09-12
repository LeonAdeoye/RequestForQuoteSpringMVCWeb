<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html;charset=ISO-8859-1">
		
		<link href="<c:url value="/resources/css/common.css" />" rel="stylesheet" type="text/css">
		<link href="<c:url value="/resources/css/groups.css" />" rel="stylesheet" type="text/css">
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
		<script type="text/javascript" src="<c:url value="/resources/js/groups.js" />"></script>		
				
		<script type="text/javascript">		
			var contextPath='<%=request.getContextPath()%>'
		
			if (typeof jQuery == 'undefined')				 
			    alert("WARNING: jQuery library is NOT loaded!");
		</script>		
		<title><spring:message code="groups.maintenance.label"/></title>
	</head>
	<body>
		
		<div id="groups-new-group" class="new-group-class float-left">				
			<input id="new-group-name" class="float-left new-group-input-class" path="name" value="Enter name..." type="text" default_value="Enter name..." />
			<input id="new-group-description" class="float-left new-group-input-class" path="description" value="Enter description..." type="text" default_value="Enter description..." />
			<button id="new-group-add" class="new-group-btn new-group-input-class"><spring:message code="groups.add.button.label"/></button>
			<button id="new-group-clear" class="new-group-btn new-group-input-class float-right"><spring:message code="groups.clear.button.label"/></button>
			<button id="new-group-import" class="new-group-btn new-group-input-class float-right"><spring:message code="groups.import.button.label"/></button>
		</div>
			
		<span class='loading-indicator' style="display:none;"><label><spring:message code="loading.indicator.label"/></label></span>

		<ul id="groupContextMenu" class="contextMenu">
		  <b><spring:message code="contextMenu.operation.title.label"/></b>
		  <li data="VALIDATE"><spring:message code="contextMenu.operation.validate.menuitem"/></li>
		  <li data="INVALIDATE"><spring:message code="contextMenu.operation.invalidate.menuitem"/></li>
		</ul>
		
		<div style="width:570px;">
    		<div id="groupsGrid" style="width:570px;height:595px;"></div>
		</div>
	</body>
</html>