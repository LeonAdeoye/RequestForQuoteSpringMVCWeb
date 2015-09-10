<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html;charset=ISO-8859-1">
		
		<link href="<c:url value="/resources/css/common.css" />" rel="stylesheet" type="text/css">
		<link href="<c:url value="/resources/css/users.css" />" rel="stylesheet" type="text/css">
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
		<script type="text/javascript" src="<c:url value="/resources/js/users.js" />"></script>		
				
		<script type="text/javascript">		
			var contextPath='<%=request.getContextPath()%>'
		
			if (typeof jQuery == 'undefined')				 
			    alert("WARNING: jQuery library is NOT loaded!");
		</script>		
				<title><spring:message code="users.maintenance.label"/></title>
	</head>
	<body>
		
		<div id="users-new-user" class="new-user-class float-left">
			<input id="new-user-userId" class="float-left new-user-input-class" path="userId" value="Enter user ID..." type="text" default_value="Enter user ID..." />
			<input id="new-user-firstName" class="float-left new-user-input-class" path="firstName" value="Enter first name..." type="text" default_value="Enter first name..." />
			<input id="new-user-lastName" class="float-left new-user-input-class" path="lastName" value="Enter last name..." type="text" default_value="Enter last name..." />
			<input id="new-user-emailAddress" class="float-left new-user-input-class" path="emailAddress" value="Enter email address..." type="text" default_value="Enter email address..." />
			<button id="new-user-add" class="new-user-btn new-user-input-class"><spring:message code="users.add.button.label"/></button>
			<button id="new-user-clear" class="new-user-btn new-user-input-class float-right"><spring:message code="users.clear.button.label"/></button>
			<button id="new-user-import-btn" class="new-user-btn new-user-input-class float-right"><spring:message code="users.import.button.label"/></button>
		</div>
			
		<span class='loading-indicator' style="display:none;"><label><spring:message code="loading.indicator.label"/></label></span>

		<ul id="userContextMenu" class="contextMenu">
		  <b><spring:message code="contextMenu.operation.title.label"/></b>
		  <li data="VALIDATE"><spring:message code="contextMenu.operation.validate.menuitem"/></li>
		  <li data="INVALIDATE"><spring:message code="contextMenu.operation.invalidate.menuitem"/></li>
		  <li data="SAVE"><spring:message code="contextMenu.operation.save.menuitem"/></li>
		  <li data="SAVE_ALL"><spring:message code="contextMenu.operation.saveAll.menuitem"/></li>
		  <li data="REFRESH"><spring:message code="contextMenu.operation.refresh.menuitem"/></li>
		</ul>
		
		<div style="width:650px;">
    		<div id="usersGrid" style="width:650px;height:595px;"></div>
		</div>
	</body>
</html>