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
		
		<script type="text/javascript">
			if (typeof jQuery == 'undefined')				 
			    alert("WARNING: jQuery library is NOT loaded!");
		</script>			
	</head>
	<body>
		<div id="rfq-main-tabset">
			<ul>
				<li><a href="/requestForQuote/requests">Requests</a></li>
				<li><a href="/requestForQuote/clients">Clients</a></li>
				<li><a href="/requestForQuote/underlyings">Underlyings</a></li>
				<li><a href="/requestForQuote/books">Books</a></li>
				<li><a href="/requestForQuote/chat">Chat</a></li>
				<li><a href="/requestForQuote/users">Users</a></li>
				<li><a href="/requestForQuote/groups">Groups</a></li>
				<li><a href="/requestForQuote/bankHolidays">Holidays</a></li>
			</ul>
		</div>
		<script type="text/javascript">
			$("#rfq-main-tabset").tabs({
				"selected" : 0
			});
		</script>		
	</body>
</html>