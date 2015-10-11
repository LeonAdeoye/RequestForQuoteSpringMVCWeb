<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html;charset=ISO-8859-1">
		
		<link href="<c:url value="/resources/css/common.css" />" rel="stylesheet" type="text/css">
		<link href="<c:url value="/resources/css/chatroom.css" />" rel="stylesheet" type="text/css">
		<link href="<c:url value="/resources/css/jquery-ui.min.css" />" rel="stylesheet" type="text/css">
		<link href="<c:url value="/resources/css/jqx.base.css" />" rel="stylesheet" type="text/css">
		<link href="<c:url value="/resources/css/jqx.ui-smoothness.css" />" rel="stylesheet" type="text/css">

		<script type="text/javascript" src="<c:url value="/resources/js/jquery-2.1.3.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/js/jquery-ui.min.js" />"></script>				
		<script type="text/javascript" src="<c:url value="/resources/js/jquery.browser.min.js" />"></script>
				
		<script type="text/javascript" src="<c:url value="/resources/js/jqxcore.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/js/jqxbuttons.js" />"></script>		
		<script type="text/javascript" src="<c:url value="/resources/js/jqxsplitter.js" />"></script>
												
		<script type="text/javascript" src="<c:url value="/resources/js/utilities.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/js/chatroom.js" />"></script>		
				
		<title><spring:message code="chatroom.title.label"/></title>
	</head>
	<body>
		<div id="chatroom-all-panes">
			<div>
				<div id="chatroom-left-pane">
					<div id="chatroom-rooms-pane">
	                    See chat rooms here
	                </div>
	                <div id="chatroom-send-message-pane">
	                    Send a message here
	                </div>
	            </div>			
			</div>
			<div id="chatroom-right-pane">See chat messages for a selected room here</div>
		</div>			
		<span class='loading-indicator' style="display:none;"><label><spring:message code="loading.indicator.label"/></label></span>

		<ul id="chatroomContextMenu" class="contextMenu">
		</ul>		
	</body>
</html>