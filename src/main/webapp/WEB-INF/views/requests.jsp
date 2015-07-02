<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html;charset=ISO-8859-1">		

		<link href="<c:url value="/resources/css/requests.css" />" rel="stylesheet" type="text/css">
		<link href="<c:url value="/resources/css/jquery-ui.min.css" />" rel="stylesheet" type="text/css">
		<link href="<c:url value="/resources/slickGrid/slick.grid.css" />" rel="stylesheet" type="text/css">
		<link href="<c:url value="/resources/slickGrid/css/smoothness/jquery-ui-1.8.16.custom.css" />" rel="stylesheet" type="text/css">
		<link href="<c:url value="/resources/slickGrid/controls/slick.columnpicker.css" />" rel="stylesheet" type="text/css">

		<script type="text/javascript" src="<c:url value="/resources/js/jquery-2.1.3.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/js/jquery-ui.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/lib/jquery.event.drag-2.2.js" />"></script>
						
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/slick.core.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/slick.formatters.js" />"></script>	
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/slick.editors.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/plugins/slick.rowselectionmodel.js" />"></script>	
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/slick.grid.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/slick.dataview.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/controls/slick.columnpicker.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/js/utilities.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/js/requests.js" />"></script>		
				
		<script type="text/javascript">
			var contextPath='<%=request.getContextPath()%>' /* needed for all ajax calls */			
		</script>
					
	<title><spring:message code="requests.title.label"/></title>
	</head>
	<body>
		<ul id="statusContextMenu" class="contextMenu" style="display:none;position:absolute">
		  <b>Select new status:</b>
		  <li data="PENDING">Pending</li>
		  <li data="PICKED_UP">Picked Up</li>
		  <li data="PASSED">Passed</li>
		  <li data="TRADED_AWAY">Traded Away</li>
		  <li data="TRADED_AWAY_ASK">Traded Away Ask</li>
		  <li data="TRADED_AWAY_BID">Traded Away Bid</li>
		  <li data="FILLED">Filled</li>
		  <li data="FILLED_ASK">Filled Ask</li>
		  <li data="FILLED_BID">Filled Bid</li>		  
		  <li data="INVALID">Invalid</li>
		</ul>
		<ul id="requestContextMenu" class="contextMenu" style="display:none;position:absolute">
		  <b>Select operation to perform:</b>
		  <li data="VIEW_DETAILS">View details</li>
		  <li data="PICK_UP">Pick up</li>
		  <li data="SAVE">Save changes</li>
		  <li data="RECALCULATE">Recalculate</li>
		  <li data="FREEZE">Freeze underlying price</li>
		  <li data="CHART">Chart</li>
		  <li data="CLONE">Clone</li>
		  <li data="DELETE">Delete</li>
		</ul>
		<div id="requests_bar">
			<div id="requests_title">
				<p id="new_requests"><spring:message code="requests.addNewRequest.label"/></p>
			</div>
			<div class="addNew" id="requests_add_new">
				<input id="requests_snippet" class="new_request" path="request" value="Enter request snippet..." type="text" default_value="Enter request snippet..." />
				<input id="requests_client" class="new_request" path="clientId" value="Select client name..." type="text" default_value="Select client name..." />
				<input id="requests_bookCode" class="new_request" path="bookCode" value="Select book code..." type="text" default_value="Select book code..." />
				<button id="requests_add_button" class="btn"><spring:message code="requests.addRequest.button.label"/></Button>
				<button id="requests_clear_button" class="btn"><spring:message code="requests.clearRequest.button.label"/></Button>																		
			</div>
			<div class="pull-right" id="requests_language_link">
				<a href="?language=en">English</a>|<a href="?language=jp">Japanese</a>
			</div>								
		</div>
		
		<div style="width:1340px;">
		    <div class="grid-header" style="width:100%">
		      <label>Requests for quote:</label>
		      <span style="float:right" class="ui-icon ui-icon-search toggleSearchPanel" title="Toggle search panel"></span>
		    </div>
    		<div id="requestsGrid" style="width:1340px;height:700px;"></div>
		</div>

	</body>
</html>