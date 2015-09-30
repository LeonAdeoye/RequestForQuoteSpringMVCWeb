<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html;charset=ISO-8859-1">
		
		<link href="<c:url value="/resources/css/common.css" />" rel="stylesheet" type="text/css">
		<link href="<c:url value="/resources/css/chats.css" />" rel="stylesheet" type="text/css">
		<link href="<c:url value="/resources/css/jquery-ui.min.css" />" rel="stylesheet" type="text/css">

		<script type="text/javascript" src="<c:url value="/resources/js/jquery-2.1.3.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/js/jquery-ui.min.js" />"></script>				
		<script type="text/javascript" src="<c:url value="/resources/js/jquery.browser.min.js" />"></script>
										
		<script type="text/javascript" src="<c:url value="/resources/js/utilities.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/js/chats.js" />"></script>		
				
		<title>Chatroom</title>
	</head>
	<body>
		<div>
			<div id="chatroom-left-pane"></div>
			<div id="chatroom-right-pane"></div>
		</div>			
		<span class='loading-indicator' style="display:none;"><label><spring:message code="loading.indicator.label"/></label></span>

		<ul id="chatContextMenu" class="contextMenu">
		</ul>		
	</body>
</html>