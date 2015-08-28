<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html;charset=ISO-8859-1">
		
		<link href="<c:url value="/resources/css/common.css" />" rel="stylesheet" type="text/css">
		<link href="<c:url value="/resources/css/underlyings.css" />" rel="stylesheet" type="text/css">
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
		<script type="text/javascript" src="<c:url value="/resources/js/underlyings.js" />"></script>		
				
		<script type="text/javascript">		
			var contextPath='<%=request.getContextPath()%>'
		
			if (typeof jQuery == 'undefined')				 
			    alert("WARNING: jQuery library is NOT loaded!");
		</script>		
		<title><spring:message code="underlyings.maintenance.label"/></title>
	</head>
	<body>
		
		<div id="underlyings-new-underlying" class="new-underlying-class float-left">				
			<input id="new-underlying-ric" class="float-left new-underlying-input-class" path="ric" value="Enter RIC..." type="text" default_value="Enter RIC..." />
			<input id="new-underlying-description" class="float-left new-underlying-input-class" path="description" value="Enter description..." type="text" default_value="Enter description..." />
			<input id="new-underlying-spread" class="float-left new-underlying-input-class" path="spread" value="Enter spread..." type="text" default_value="Enter spread..." />
			<input id="new-underlying-referencePrice" class="float-left new-underlying-input-class" path="referencePrice" value="Enter reference price..." type="text" default_value="Enter reference price..." />
			<input id="new-underlying-simulationPriceVariance" class="float-left new-underlying-input-class" path="simulationPriceVariance" value="Enter simulation price variance..." type="text" default_value="Enter simulation price variance..." />
			<input id="new-underlying-dividendYield" class="float-left new-underlying-input-class" path="dividendYield" value="Enter dividend yield..." type="text" default_value="Enter dividend yield..." />
			<button id="new-underlying-add" class="new-underlying-btn new-underlying-input-class"><spring:message code="underlyings.add.button.label"/></button>
			<button id="new-underlying-clear" class="new-underlying-btn new-underlying-input-class float-right"><spring:message code="underlyings.clear.button.label"/></button>
			<button id="new-underlying-import" class="new-underlying-btn new-underlying-input-class float-right"><spring:message code="underlyings.import.button.label"/></button>
		</div>
			
		<span class='loading-indicator' style="display:none;"><label><spring:message code="loading.indicator.label"/></label></span>

		<ul id="underlyingContextMenu" class="contextMenu">
		  <b><spring:message code="contextMenu.operation.title.label"/></b>
		  <li data="VALIDATE"><spring:message code="contextMenu.operation.validate.menuitem"/></li>
		  <li data="INVALIDATE"><spring:message code="contextMenu.operation.invalidate.menuitem"/></li>
		  <li data="SAVE"><spring:message code="contextMenu.operation.save.menuitem"/></li>
		  <li data="SAVE_ALL"><spring:message code="contextMenu.operation.saveAll.menuitem"/></li>
		  <li data="REFRESH"><spring:message code="contextMenu.operation.refresh.menuitem"/></li>
		</ul>
		
		<div style="width:905px;">
    		<div id="underlyingsGrid" style="width:905px;height:595px;"></div>
		</div>
	</body>
</html>