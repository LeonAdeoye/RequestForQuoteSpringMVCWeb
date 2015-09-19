<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
	<head>
		<title>Requests</title>
		
		<link href="<c:url value="/resources/css/common.css" />" rel="stylesheet" type="text/css">
		<link href="<c:url value="/resources/css/main.css" />" rel="stylesheet" type="text/css">
		<link href="<c:url value="/resources/css/jquery-ui.min.css" />" rel="stylesheet" type="text/css">
		<link href="<c:url value="/resources/slickGrid/examples.css" />" rel="stylesheet" type="text/css">
		<link href="<c:url value="/resources/slickGrid/slick.grid.css" />" rel="stylesheet" type="text/css">
		<link href="<c:url value="/resources/slickGrid/css/smoothness/jquery-ui-1.8.16.custom.css" />" rel="stylesheet" type="text/css">
		<link href="<c:url value="/resources/slickGrid/controls/slick.columnpicker.css" />" rel="stylesheet" type="text/css">

		<script type="text/javascript" src="<c:url value="/resources/js/jquery-2.1.3.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/js/jquery-ui.min.js" />"></script>				
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/lib/jquery.event.drag-2.2.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/js/jquery.browser.min.js" />"></script>
								
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/slick.core.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/slick.formatters.js" />"></script>	
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/slick.editors.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/slick.groupitemmetadataprovider.js" />"></script>			
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/slick.grid.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/slick.dataview.js" />"></script>
		
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/lib/jquery.sparkline.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/plugins/slick.rowselectionmodel.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/controls/slick.columnpicker.js" />"></script>	
		
		<script type="text/javascript" src="<c:url value="/resources/js/utilities.js" />"></script>
 		<script type="text/javascript" src="https://www.google.com/jsapi"></script>					
	</head>
	<body>
		<div id="rfq-main-tabset">
			<ul>
				<li><a href="/requestForQuote/requests"><spring:message code="main.tabset.requests.label"/></a></li>
				<li><a href="/requestForQuote/clients"><spring:message code="main.tabset.clients.label"/></a></li>
				<li><a href="/requestForQuote/underlyings"><spring:message code="main.tabset.underlyings.label"/></a></li>
				<li><a href="/requestForQuote/books"><spring:message code="main.tabset.books.label"/></a></li>
				<li><a href="/requestForQuote/chat"><spring:message code="main.tabset.chat.label"/></a></li>
				<li><a href="/requestForQuote/users"><spring:message code="main.tabset.users.label"/></a></li>
				<li><a href="/requestForQuote/groups"><spring:message code="main.tabset.groups.label"/></a></li>
				<li><a href="/requestForQuote/bankHolidays"><spring:message code="main.tabset.holidays.label"/></a></li>
			</ul>
		</div>
		<script type="text/javascript">			
			$(document).ready(function()
			{
				$("#rfq-main-tabset").tabs();			
			});
		</script>					
	</body>
</html>