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
	                    <ul id="chatroom-rooms-list">
						</ul>
	                </div>
	                <div id="chatroom-send-message-pane">
	                    <textarea id="new-chatroom-message" class="new-chatroom-message-textarea-class" cols="20" wrap="hard" autofocus path="chatMessage" default_value="Enter message here..." ><spring:message code="chatroom.message.default-value"/></textarea>
	                </div>
	            </div>			
			</div>
			<div id="chatroom-right-pane">
				<ul id="chatroom-message-list">
				</ul>
			</div>
		</div>			
		<span class='loading-indicator' style="display:none;"><label><spring:message code="loading.indicator.label"/></label></span>

		<ul id="chatroomContextMenu" class="contextMenu">
		</ul>		
	</body>
</html>