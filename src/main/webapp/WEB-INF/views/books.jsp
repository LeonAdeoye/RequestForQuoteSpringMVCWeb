<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html;charset=ISO-8859-1">
		
		<link href="<c:url value="/resources/css/common.css" />" rel="stylesheet" type="text/css">
		<link href="<c:url value="/resources/css/books.css" />" rel="stylesheet" type="text/css">
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
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/plugins/slick.rowselectionmodel.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/controls/slick.columnpicker.js" />"></script>
		
		<script type="text/javascript" src="<c:url value="/resources/js/utilities.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/js/books.js" />"></script>		
				
		<script type="text/javascript">		
			var contextPath='<%=request.getContextPath()%>'
		
			if (typeof jQuery == 'undefined')				 
			    alert("WARNING: jQuery library is NOT loaded!");
		</script>		
		<title>Book Maintenance</title>
	</head>
	<body>
		
		<div id="books-new-book" class="new-book-class float-left">				
			<input id="new-book-bookCode" class="float-left new-book-input-class" path="bookCode" value="Enter book code..." type="text" default_value="Enter book code..." />
			<input id="new-book-entity" class="float-left new-book-input-class" path="entity" value="Enter entity..." type="text" default_value="Enter entity..." />
			<button id="new-book-add" class="new-book-btn new-book-input-class"><spring:message code="books.add.button.label"/></button>
			<button id="new-book-clear" class="new-book-btn new-book-input-class float-right"><spring:message code="books.clear.button.label"/></button>
			<button id="new-book-import" class="new-book-btn new-book-input-class float-right"><spring:message code="books.import.button.label"/></button>
			<!-- <input type="file" name="xlfile" id="xlf"/> -->
		</div>
			
		<span class='loading-indicator' style="display:none;"><label><spring:message code="loading.indicator.label"/></label></span>

		<ul id="bookContextMenu" class="contextMenu">
		  <b><spring:message code="contextMenu.operation.title.label"/></b>
		  <li data="VALIDATE"><spring:message code="contextMenu.operation.validate.menuitem"/></li>
		  <li data="INVALIDATE"><spring:message code="contextMenu.operation.invalidate.menuitem"/></li>
		</ul>
		
		<div style="width:330px;">
    		<div id="booksGrid" style="width:330px;height:595px;"></div>
		</div>
	</body>
</html>