var SNIPPET_REGEX = /^([+-]?[1-9]*[C|c|P|p]{1}){1}([-+]{1}[1-9]*[C|c|P|p]{1})* ([\d]+){1}(,{1}[\d]+)* [\d]{1,2}(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)20[\d]{2}(,{1}[\d]{1,2}(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)20[\d]{2})* (\w){4,7}\.[A-Z]{1,2}$/;

function enableAddButton()
{
	$("#requests_add_button").removeAttr('disabled');
}

function enableClearButton()
{
	$("#requests_clear_button").removeAttr('disabled');
}

function disableAddButton()
{
	$("#requests_add_button").attr('disabled', 'disabled');
}

function disableClearButton()
{
	$("#requests_clear_button").attr('disabled', 'disabled');
}

function clearNewRequestInputFields()
{
	$("#requests_snippet").val($("#requests_snippet").attr("default_value"));
	$("#requests_client").val($("#requests_client").attr("default_value"));
	$("#requests_bookCode").val($("#requests_bookCode").attr("default_value"));	
}

function snippetMatches(snippet)
{
	return SNIPPET_REGEX.test(snippet);
}

function toggleAddButtonState()
{
	if(snippetMatches($("#requests_snippet").val()))
		enableAddButton();
	else
		disableAddButton();
}

function RequestDate(dayOfMonth, month, year) 
{
	  this.dayOfMonth = dayOfMonth;
	  this.month = month;
	  this.year = year;
}

RequestDate.prototype.toString = function dateToString() 
{
	var displayValue = new String(this.dayOfMonth);
	displayValue = displayValue.concat(((this.month).substr(0,3)).toPascalCase());
	displayValue = displayValue.concat(this.year);
	return displayValue;
}

function dateFormatter(row, cell, value, columnDef, dataContext)
{
    if (value != null)
    {	
    	var theRequestDate = new RequestDate(value.dayOfMonth, value.month, value.year);
    	
    	return theRequestDate;
    }
    else
        return "";
}

function pascalCaseFormatter(row, cell, value, columnDef, dataContext)
{
    if (value == null)
        return "";
    else
    	return value.toPascalCase();
}

function decimalFormatter(row, cell, value, columnDef, dataContext)
{
    if (value == null)
        return "0.000";
    else
    	return value.toFixed(3);     
}

var columns = 
[
 	{id: "requestId", name: "Request ID", field: "identifier", sortable: true, toolTip: "Request unique identifier"},
	{id: "snippet", name: "Snippet", field: "request", cssClass: "cell-title", minWidth: 220, validator: requiredFieldValidator, toolTip: "Request snippet"},
	{id: "status", name: "Status", field: "status", sortable: true, toolTip: "Current status of request.", formatter: pascalCaseFormatter},
	{id: "pickedUpBy", name: "Picked Up By", field: "pickedUpBy", sortable: true, toolTip: "Request picked up by user:"},
	{id: "clientId", name: "Client ID", field: "clientId", sortable: true, toolTip: "Client this requests applies to"},	
	{id: "tradeDate", name: "Trade Date", field: "tradeDate", formatter: dateFormatter, editor: Slick.Editors.Date, sortable: true, toolTip: "Trade date"},
	{id: "premiumAmount", name: "Theoretical Value", field: "premiumAmount", formatter: decimalFormatter, toolTip: "Theoretical value"},
	{id: "timeValue", name: "Time Value", field: "timeValue", formatter: decimalFormatter, toolTip: "Time value"},
	{id: "intrinsicValue", name: "Intrinsic Value", field: "intrinsicValue", formatter: decimalFormatter, toolTip: "Intrinsic value"},
	{id: "underlyingPrice", name: "Spot", field: "underlyingPrice", toolTip: "Underlying Spot price"},
	{id: "delta", name: "Delta", field: "delta", formatter: decimalFormatter, toolTip: "Delta"},
	{id: "gamma", name: "Gamma", field: "gamma", formatter: decimalFormatter, toolTip: "Gamma"},
	{id: "vega", name: "Vega", field: "vega", formatter: decimalFormatter, toolTip: "Vega"},
	{id: "theta", name: "Theta", field: "theta", formatter: decimalFormatter, toolTip: "Theta"},
	{id: "rho", name: "Rho", field: "rho", formatter: decimalFormatter, toolTip: "Rho"},
	{id: "bookCode", name: "Book Code", field: "bookCode", sortable: true, toolTip: "Book code"},	
	{id: "underlyingRIC", name: "Underlying RIC", field: "underlyingRIC", toolTip: "Underlying RIC"},
	{id: "salesComment", name: "Sales Comment", field: "salesComment", editor: Slick.Editors.LongText, toolTip: "Comments made by sales."},
	{id: "traderComment", name: "Trader Comment", field: "traderComment", editor: Slick.Editors.LongText, toolTip: "Comments made by the traders."},
	{id: "clientComment", name: "Client Comment", field: "clientComment", editor: Slick.Editors.LongText, toolTip: "Comments made by the client."}		
];

var options = 
{
	enableCellNavigation: true,
	enableColumnReorder: true,		
    editable: true,
    enableAddRow: false,
    asyncEditorLoading: false,
    autoEdit: false,
    forceFitColumns: false,
    cellHighlightCssClass: "priceUpdateIncrease",
    cellFlashingCssClass: "cellFlash",
    topPanelHeight:250	    
};

function requiredFieldValidator(value) 
{
	if (value == null || value == undefined || !value.length) 
	{
		return {valid: false, msg: "This is a required field"};
	}
	else 
	{
		return {valid: true, msg: null};
	}
}

$(document).ready(function()
{
	var priceUpdatesAjaxLock = false;
	var calculationUpdatesAjaxLock = false;
	var statusUpdatesAjaxLock = false;

	var priceUpdateIntervalTime = 1000;
	var calculationUpdateIntervalTime = 1000;
	var statusUpdateIntervalTime = 1000;

	var calculationUpdateTimeout = 5000;
	var priceUpdateTimeout = 5000;
	var statusUpdateTimeout = 5000;

	var dataView = new Slick.Data.DataView();	
	var requestsGrid = new Slick.Grid("#requestsGrid", dataView, columns, options);
	var columnpicker = new Slick.Controls.ColumnPicker(columns, requestsGrid, options);
	
	requestsGrid.setSelectionModel(new Slick.RowSelectionModel());
	requestsGrid.setTopPanelVisibility(false);
	getRequestsFromTodayOnly();

	function processNewlyCreatedRequest(newlyCreatedrequest)
	{
		if(newlyCreatedrequest)
			dataView.insertItem(0, newlyCreatedrequest);
	}
	
	
	function processStatusUpdates(status)
	{
		alert("Not yet implemented: " + statuses);
	}
	
	function processCalculationUpdates(calculations)
	{
			alert("Not yet implemented: " + calculations);
	}	
	
	function processPriceUpdates(prices)
	{
		dataView.beginUpdate();
		var changes = {};
		var increaseInPrice = false;
		
		for(var i = 0, size = dataView.getLength(); i < size; i++)
		{
			var item = dataView.getItemByIdx(i);
			if(prices[item["underlyingRIC"]] !== undefined)
			{
				if(item["underlyingPrice"] != prices[item["underlyingRIC"]].lastPrice)
				{
					if(prices[item["underlyingRIC"]].lastPrice > item["underlyingPrice"])
						increaseInPrice = true;
												
					item["underlyingPrice"] = prices[item["underlyingRIC"]].lastPrice; 
					dataView.updateItem(item["identifier"], item);
					
					if (!changes[i])
						changes[i] = { underlyingPrice: (increaseInPrice ? "priceUpdateIncrease" : "priceUpdateDecrease") };
					
					requestsGrid.setCellCssStyles("highlight", changes);
					
					setTimeout(function() { 
						requestsGrid.setCellCssStyles("highlight", {});
					}, 300);
				}
			}
		}
		
		dataView.endUpdate();
		requestsGrid.render();
	}
	
	$("#requests_add_button").click(function(event)
	{
		disableAddButton();
		disableClearButton();
		
		var snippet = $('#requests_snippet').val();
	    var bookCode = $('#requests_bookCode').val();
	    var client = $('#requests_client').val();
	    var lastUpdatedBy = "ladeoye"; // TODO
	    var json = { "request" : snippet, "bookCode" : bookCode, "clientId": client , "lastUpdatedBy" : lastUpdatedBy};
	    
	    clearNewRequestInputFields();
		
		$.ajax({
		    url: contextPath + "/requests/createNewRequest", 
		    type: 'POST',
		    data: JSON.stringify(json),
		    dataType: 'json',  
		    contentType: 'application/json', 
		    mimeType: 'application/json',
		    timeout: 5000,
		    cache: false,
		    success: function(newlyCreatedrequest) 
		    {
		    	processNewlyCreatedRequest(newlyCreatedrequest);		    	
		    },
	        error: function (xhr, textStatus, errorThrown) 
	        {
	        	if(textStatus == "timeout")
	        		alert('Response to newly created RFQ timed-out after five seconds');
	        	else
	            	alert('Error: ' + xhr.responseText);                	
	        }
		});		
	});
	
	function getPriceUpdates() 
	{
		if(!priceUpdatesAjaxLock)
		{
			priceUpdatesAjaxLock = true;
			
			$.ajax({
			    url: contextPath + "/requests/priceUpdates", 
			    type: 'GET', 
			    dataType: 'json',  
			    contentType: 'application/json', 
			    mimeType: 'application/json',
			    timeout: priceUpdateTimeout,
			    cache: false,
			    success: function(prices) 
			    {
			    	processPriceUpdates(prices);		    	
			    	priceUpdatesAjaxLock = false;
			    },
	            error: function (xhr, textStatus, errorThrown) 
	            {
	            	if(textStatus == "timeout")
	            		alert('Price update timed-out after three seconds');
	            	else
	                	alert('Error: ' + xhr.responseText);                	
	            	
	            	priceUpdatesAjaxLock = false;
	            }
			});			
		}		
	}	
	
	function getCalculationUpdates() 
	{
		if(!calculationUpdatesAjaxLock)
		{
			calculationUpdatesAjaxLock = true;
			
			$.ajax({
			    url: contextPath + "/requests/calculationUpdates", 
			    type: 'GET', 
			    dataType: 'json',  
			    contentType: 'application/json', 
			    mimeType: 'application/json',
			    timeout: calculationUpdateTimeout,
			    cache: false,
			    success: function(calculations) 
			    {
			    	processCalculationUpdates(calculations);		    	
			    	calculationUpdatesAjaxLock = false;
			    },
	            error: function (xhr, textStatus, errorThrown) 
	            {
	            	if(textStatus == "timeout")
	            		alert('Calculation update timed-out after ' + calculationUpdateTimeout + ' milliseconds');
	            	else
	                	alert('Error: ' + xhr.responseText);                	
	                
	            	calculationUpdatesAjaxLock = false;            		
	 
	            }
			});			
		}		
	}
	
	function getStatusUpdates() 
	{
		if(!statusUpdatesAjaxLock)
		{
			statusUpdatesAjaxLock = true;
			
			$.ajax({
			    url: contextPath + "/requests/statusUpdates", 
			    type: 'GET', 
			    dataType: 'json',  
			    contentType: 'application/json', 
			    mimeType: 'application/json',
			    timeout: statusUpdateTimeout,
			    cache: false,
			    success: function(statuses) 
			    {
			    	processStatusUpdates(statuses);		    	
			    	statusUpdatesAjaxLock = false;
			    },
	            error: function (xhr, textStatus, errorThrown) 
	            {
	            	if(textStatus == "timeout")
	            		alert('Status update timed-out after ' + statusUpdateTimeout + ' milliseconds');
	            	else
	                	alert('Error: ' + xhr.responseText);                	
	                
	            	statusUpdatesAjaxLock = false;
	            }
			});			
		}		
	}	
	
	requestsGrid.onSort.subscribe(function(e, args)
	{
	    sortColumn = args.sortCol.field;
	    
		function lexographicComparer(a, b)
		{
			var x = a[sortColumn], y = b[sortColumn];
			return (x == y ? 0 : (x > y ? 1 : -1));
		}
		
		function dateComparer(a, b)
		{
			var x = a[sortColumn], y = b[sortColumn];
			
			if(x.year == y.year)
			{
				if(x.monthValue == y.monthValue)
					return x.dayOfMonth - y.dayOfMonth; 
				else
					return x.monthValue - y.monthValue;
			}
			else
				return x.year - y.year;
		}		

		dataView.sort((sortColumn == "tradeDate" ? dateComparer : lexographicComparer), args.sortAsc);
	});
	
	dataView.onRowCountChanged.subscribe(function (e, args)
	{
		requestsGrid.updateRowCount();
		requestsGrid.render();
	});

	dataView.onRowsChanged.subscribe(function (e, args) 
	{
		requestsGrid.invalidateRows(args.rows);
		requestsGrid.render();
	});
	
	requestsGrid.render();
	
/*	$("#requestsGroupByRadio").buttonset();
	$("#requestsRealTimeUpdatesCheckbox").buttonset()*/;
	
	$("#priceFrequencySliderValue").html(" = 1000 ms");
	
	$("#priceFrequencySlider").slider(
	{
		"range": "min",
		"min" : 200,
		"max" : 5000,
		"step" : 200,
		"value" : 1000,
	    "stop": function (event, ui)
	    {
	    	if (priceUpdateIntervalTime != ui.value)
	    	{
	    		if(ui.value <= priceUpdateTimeout - 500)
	    			priceUpdateIntervalTime = ui.value;
	    		else
	    			priceUpdateIntervalTime = priceUpdateTimeout - 500;
	    		
	    		$("#priceFrequencySliderValue").html(" = " + priceUpdateIntervalTime + " ms");
	    	}	    	
	    }
	});
	
	$("#calculationFrequencySliderValue").html(" = 1000 ms");
	
	$("#calculationFrequencySlider").slider(
	{
		"range": "min",
		"min" : 200,
		"max" : 5000,
		"step" : 200,
		"value" : 1000,
	    "stop": function (event, ui)
	    {
	    	if (calculationUpdateIntervalTime != ui.value)
	    	{
	    		if(ui.value <= calculationUpdateTimeout - 500)
	    			calculationUpdateIntervalTime = ui.value;
	    		else
	    			calculationUpdateIntervalTime = calculationUpdateTimeout - 500;
	    		
	    		$("#calculationFrequencySliderValue").html(" = " + calculationUpdateIntervalTime + " ms");
	    	}
	    }
	});
	
	$("#statusFrequencySliderValue").html(" = 1000 ms");
	
	$("#statusFrequencySlider").slider(
	{
		"range": "min",
		"min" : 200,
		"max" : 5000,
		"step" : 200,
		"value" : 1000,
	    "stop": function (event, ui)
	    {
	    	if (statusUpdateIntervalTime != ui.value)
	    	{
	    		if(ui.value <= statusUpdateTimeout - 500)
	    			statusUpdateIntervalTime = ui.value;
	    		else
	    			statusUpdateIntervalTime = statusUpdateTimeout - 500;
	    		
	    		$("#statusFrequencySliderValue").html(" = " + statusUpdateIntervalTime + " ms");
	    	}
	    }
	});
	
	$("#priceTimeoutSliderValue").html(" = 5000 ms");
	
	$("#priceTimeoutSlider").slider(
	{
		"range": "min",
		"min" : priceUpdateIntervalTime + 1000,
		"max" : 10000,
		"step" : 200,
		"value" : 5000,
	    "stop": function (event, ui)
	    {
	    	if (priceUpdateTimeout != ui.value)
			{
	    		if(ui.value >= (priceUpdateIntervalTime + 1000))
	    			priceUpdateTimeout = ui.value;
	    		else
	    			priceUpdateTimeout = priceUpdateIntervalTime + 1000;
	    		
	    		$("#priceTimeoutSliderValue").html(" = " + priceUpdateTimeout + " ms");
			}
	    }
	});
	
	$("#statusTimeoutSliderValue").html(" = 5000 ms");
	
	$("#statusTimeoutSlider").slider(
	{
		"range": "min",
		"min" : statusUpdateIntervalTime + 1000,
		"max" : 10000,
		"step" : 200,
		"value" : 5000,
	    "stop": function (event, ui)
	    {
	    	if (statusUpdateTimeout != ui.value)
			{
	    		if(ui.value >= (statusUpdateIntervalTime + 1000))
	    			statusUpdateTimeout = ui.value;
	    		else
	    			statusUpdateTimeout = statusUpdateIntervalTime + 1000;
	    		
	    		$("#statusTimeoutSliderValue").html(" = " + statusUpdateTimeout + " ms");
			}
	    }
	});
	
	$("#calculationTimeoutSliderValue").html(" = 5000 ms");

	$("#calculationTimeoutSlider").slider(
	{
		"range": "min",
		"min" : calculationUpdateIntervalTime + 1000,
		"max" : 10000,
		"step" : 200,
		"value" : 5000,
	    "stop": function (event, ui)
	    {
	    	if (calculationUpdateTimeout != ui.value)
			{
	    		if(ui.value >= (calculationUpdateIntervalTime + 1000))
	    			calculationUpdateTimeout = ui.value;
	    		else
	    			calculationUpdateTimeout = calculationUpdateIntervalTime + 1000;
	    		
	    		$("#calculationTimeoutSliderValue").html(" = " + calculationUpdateTimeout + " ms");
			}
	    }
	});
	
	function appendToTopPanel()
	{
		$("#requestsInlineConfigurePanel").appendTo(requestsGrid.getTopPanel());
		$("#requestsInlineGroupByPanel").appendTo(requestsGrid.getTopPanel());
	}
	
	appendToTopPanel();		
	
	$('.toggleTopPanel').bind('click', function(event) 
	{
		$(".inlinePanel").hide();
    	switch ($(this).attr("data")) 
    	{
        	case "configure":
        		$("#requestsInlineConfigurePanel").show();
        		break;
        	case "group":
        		$("#requestsInlineGroupByPanel").show();
        		break;        		
        	case "search":
        		requestsGrid.setTopPanelVisibility(false);
        		alert("Sorry, search operation has not yet been implemented!");
        		return;
        	case "filter":
        		requestsGrid.setTopPanelVisibility(false);
        		alert("Sorry, filter operation has not yet been implemented!");
        		return;
        	case "chart":
        		requestsGrid.setTopPanelVisibility(false);
        		alert("Sorry, charting operation has not yet been implemented!");
        		return;        		
    	}
    	
    	requestsGrid.setTopPanelVisibility(true);
	});
	
	$('.hideTopPanel').bind('click', function(event) 
	{
		requestsGrid.setTopPanelVisibility(false);
	});
	
	var priceUpdateInterval;
	var statusUpdateInterval;
	var calculationUpdateInterval;
	
	$('#turnOnPriceUpdates').bind('click', function(event) 
	{
		if(jQuery('#turnOnPriceUpdates').is(':checked'))
			priceUpdateInterval = setInterval(getPriceUpdates, priceUpdateIntervalTime);
		else
			window.clearInterval(priceUpdateInterval);
	});
	
	$('#turnOnCalculationUpdates').bind('click', function(event) 
	{
		if(jQuery('#turnOnCalculationUpdates').is(':checked'))
			calculationUpdateInterval = setInterval(getCalculationUpdates, calculationUpdateIntervalTime);
		else		
			window.clearInterval(calculationUpdateInterval);
	});
	
	$('#turnOnStatusUpdates').bind('click', function(event) 
	{
		if(jQuery('#turnOnStatusUpdates').is(':checked'))
			statusUpdateInterval = setInterval(getStatusUpdates, statusUpdateIntervalTime);
		else		
			window.clearInterval(statusUpdateInterval);
	});	
			
	requestsGrid.onContextMenu.subscribe(function (e) 
	{
        var cell = requestsGrid.getCellFromEvent(e);
        
        if (requestsGrid.getColumns()[cell.cell].id == "status")
        {
        	e.preventDefault();
        	
        	$("#requestContextMenu").hide();
        	
	        $("#statusContextMenu")
	            .data("row", cell.row)
	            .css("top", e.pageY)
	            .css("left", e.pageX)
	            .show();
        
	        $("body").one("click", function() 
	        {
	          $("#statusContextMenu").hide();
	        });
        }
        else
    	{
        	e.preventDefault();
        	
        	$("#statusContextMenu").hide();
        	
	        $("#requestContextMenu")
	            .data("row", cell.row)
	            .css("top", e.pageY)
	            .css("left", e.pageX)
	            .show();
        
	        $("body").one("click", function() 
	        {
	          $("#requestContextMenu").hide();
	        });
    	}
    });	
	
	function getRequestsFromTodayOnly() 
	{		
		$.ajax({
		    url: contextPath + "/requests/today", 
		    type: 'GET', 
		    dataType: 'json',  
		    contentType: 'application/json',
		    mimeType: 'application/json',
		    timeout: 5000,
		    cache: false,
		    success: function(requests) 
		    {
		    	dataView.setItems(requests, "identifier");
		    },
            error: function (xhr, textStatus, errorThrown) 
            {
                alert('Error: ' + xhr.responseText);
            }
		});
	}	
	
	getRequestsFromTodayOnly();
		
	$("#requests_bookCode").autocomplete(
	{
		minLength:3,
        source: function (request, response) 
        {
            $.ajax(
            {
                type: "GET",
                url: contextPath + "/requests/matchingBookTags?pattern=" + request.term,
                dataType: "json",
                data: 
                {
                    term: request.termCode
                },
                error: function (xhr, textStatus, errorThrown) 
                {
                    alert('Error: ' + xhr.responseText);
                },
                success: function (data) 
                {
                    response($.map(data, function (item) 
                    {
                        return {
                            		label: item.label,
                            		value: item.value
                        		}
                    }));
                }
            });
        }
    });
	
	$("#requests_client").autocomplete(
	{
		minLength:2,
        source: function (request, response) 
        {
            $.ajax(
            {
                type: "GET",
                url: contextPath + "/requests/matchingClientTags?pattern=" + request.term,
                dataType: "json",
                data: 
                {
                    term: request.termCode
                },
                error: function (xhr, textStatus, errorThrown) 
                {
                    alert('Error: ' + xhr.responseText);
                },
                success: function (data) 
                {
                    response($.map(data, function (item) 
                    {
                        return {
                            		label: item.label,
                            		value: item.value
                        		}
                    }));
                }
            });
        }
    });
	
	function updateStatusInGrid(updatedRequest, row, oldStatus, newStatus)
	{	
    	var isPickedUpByChanging = ((oldStatus != "PICKED_UP") && (newStatus == "PICKED_UP")) 
			|| ((oldStatus != "PENDING") && (newStatus == "PENDING"));
    		
    	dataView.getItem(row).status = newStatus;
    	
    	if(isPickedUpByChanging && dataView.getItem(row).pickedUpBy != updatedRequest.pickedUpBy)
    		dataView.getItem(row).pickedUpBy = updatedRequest.pickedUpBy;			    		
    	else
    		isPickedUpByChanging = false;
    	
    	requestsGrid.updateRow(row);
    	
    	requestsGrid.flashCell(row, requestsGrid.getColumnIndex("status"), 100);
    	
    	if(isPickedUpByChanging)
    		requestsGrid.flashCell(row, requestsGrid.getColumnIndex("pickedUpBy"), 100);
	}
	
	function ajaxSendStatusUpdate(row, newStatus)
	{
		var identifier = dataView.getItem(row).identifier;
		var oldStatus = dataView.getItem(row).status;
		var lastUpdatedBy = "ladeoye"; // TODO
		var json = { "identifier" : identifier, "status" : newStatus, "lastUpdatedBy" : lastUpdatedBy};
		
		$.ajax({
		    url: contextPath + "/requests/updateStatus", 
		    type: 'POST',
		    data: JSON.stringify(json),
		    dataType: 'json',  
		    contentType: 'application/json', 
		    mimeType: 'application/json',
		    timeout: 5000,
		    cache: false,
		    success: function(updatedRequest) 
		    {
		    	if(updatedRequest)
		    		updateStatusInGrid(updatedRequest, row, oldStatus, newStatus);
		    	else
		    		alert("Server failed to update the status of request: "  + identifier + " from: " + oldStatus + " to: " + newStatus);
		    },		    
	        error: function (xhr, textStatus, errorThrown) 
	        {
	        	if(textStatus == "timeout")
	        		alert("Failed to update the status of request: "  + identifier + " from: " + oldStatus + " to: " + newStatus + ". Status update timed-out after five seconds.");
	        	else
	            	alert("Failed to update the status of request: "  + identifier + " from: " + oldStatus + " to: " + newStatus + ". Error: " + xhr.responseText);                	
	        }
		});
	}
	
    $("#statusContextMenu").click(function (e) 
    {
    	if (!$(e.target).is("li")) 
    		return;
      
    	if (!requestsGrid.getEditorLock().commitCurrentEdit())
    		return;
    	
		ajaxSendStatusUpdate( $(this).data("row"), $(e.target).attr("data"));		
    });
    
    $("#requestContextMenu").click(function (e) 
    {
    	if (!$(e.target).is("li")) 
    		return;
      
    	if (!requestsGrid.getEditorLock().commitCurrentEdit())
    		return;
      
    	var row = $(this).data("row");
    	var operation = $(e.target).attr("data");
    	
    	switch (operation) 
    	{
        	case "PICK_UP":
        		ajaxSendStatusUpdate(row, "PICKED_UP");	
        		break; 
        	default: 
        		alert("Sorry, this operation is yet to be supported!");
    	}    	

    });	    
	
	$(".btn").button(); // TODO disable does not work yet. find disabled attribute and add it.
	
	disableAddButton();	
	$("#requests_snippet").keyup(toggleAddButtonState);
	$("#requests_snippet").focusout(toggleAddButtonState);

	$(".new_request").click(function()
	{
		if($(this).val() == $(this).attr("default_value"))
		{
			$(this).val("");
			
			if($(this).is("#requests_snippet"))
				disableAddButton();
		}
	});

	$(".new_request").focusout(function()
	{
		if(trimSpaces($(this).val()) == "")
			$(this).val($(this).attr("default_value"));
	});

	$("#requests_clear_button").click(function()
	{
		clearNewRequestInputFields();		
		disableAddButton();		
	});	
});
