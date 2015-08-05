var SNIPPET_REGEX = /^([+-]?[1-9]*[C|c|P|p]{1}){1}([-+]{1}[1-9]*[C|c|P|p]{1})* ([\d]+){1}(,{1}[\d]+)* [\d]{1,2}(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)20[\d]{2}(,{1}[\d]{1,2}(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)20[\d]{2})* (\w){4,7}\.[A-Z]{1,2}$/;

function enableAddButton()
{
	$("#requests_add_button").removeAttr('disabled');
}

function disableAddButton()
{
	$("#requests_add_button").attr('disabled', 'disabled');
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

var statusHashIndexedByStatusEnum = {}, statusHashIndexedByDesc = {}, clientHashIndexedByDesc = {}, clientHashIndexedById = {};

function dateFormatter(row, cell, value, columnDef, dataContext)
{
    if (value != null)
    {	
    	var theDate = new Date(value);
    	if(theDate !== NaN)
    		return $.datepicker.formatDate("yy-mm-dd", theDate);    		
    }
    return "";
}

function clientFormatter(row, cell, value, columnDef, dataContext)
{
    if (value == null)
        return "";
    else
    	return clientHashIndexedById[value];
}

function statusFormatter(row, cell, value, columnDef, dataContext)
{
    if (value == null)
        return "";
    else
    	return statusHashIndexedByStatusEnum[value];
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

function sumTotalsFormatter(totals, columnDef)
{
	var val = totals.sum && totals.sum[columnDef.field];
	
	if (val != null)
		return "total: " + ((Math.round(parseFloat(val)*100)/100));
	
	return "";
}

function waitingFormatter(value)
{
	return "Wait...";
}

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

function renderSparkline(cellNode, row, dataContext, colDef)
{
    $(cellNode).empty().sparkline(dataContext["profitAndLossPoints"], {width: "100%"});
}

var columns = 
[
 	{id: "requestId", name: "Request ID", field: "identifier", sortable: true, toolTip: "Request unique identifier"},
	{id: "snippet", name: "Snippet", field: "request", cssClass: "cell-title", minWidth: 220, validator: requiredFieldValidator, toolTip: "Request snippet"},
	{id: "status", name: "Status", field: "status", sortable: true, toolTip: "Current status of request.", formatter: statusFormatter},
	{id: "pickedUpBy", name: "Picked Up By", field: "pickedUpBy", sortable: true, toolTip: "Request picked up by user:"},
	{id: "clientId", name: "Client", field: "clientId", sortable: true, toolTip: "Client this requests applies to", formatter: clientFormatter},	
	{id: "tradeDate", name: "Trade Date", field: "tradeDateString", formatter: dateFormatter, sortable: true, toolTip: "Trade date"},
	{id: "premiumAmount", name: "Theoretical Value", field: "premiumAmount", formatter: decimalFormatter, toolTip: "Theoretical value"},
	{id: "timeValue", name: "Time Value", field: "timeValue", formatter: decimalFormatter, toolTip: "Time value"},
	{id: "intrinsicValue", name: "Intrinsic Value", field: "intrinsicValue", formatter: decimalFormatter, toolTip: "Intrinsic value"},
	{id: "underlyingPrice", name: "Spot", field: "underlyingPrice", toolTip: "Underlying Spot price"},
	{id: "profitAndLossPoints", name: "P&L Chart", sortable: false, formatter: waitingFormatter, asyncPostRender: renderSparkline, toolTip: "Profit and loss chart"},
	{id: "delta", name: "Delta", field: "delta", formatter: decimalFormatter, toolTip: "Delta", groupTotalsFormatter: sumTotalsFormatter},
	{id: "gamma", name: "Gamma", field: "gamma", formatter: decimalFormatter, toolTip: "Gamma", groupTotalsFormatter: sumTotalsFormatter},
	{id: "vega", name: "Vega", field: "vega", formatter: decimalFormatter, toolTip: "Vega", groupTotalsFormatter: sumTotalsFormatter},
	{id: "theta", name: "Theta", field: "theta", formatter: decimalFormatter, toolTip: "Theta", groupTotalsFormatter: sumTotalsFormatter},
	{id: "rho", name: "Rho", field: "rho", formatter: decimalFormatter, toolTip: "Rho", groupTotalsFormatter: sumTotalsFormatter},
	{id: "bookCode", name: "Book Code", field: "bookCode", sortable: true, toolTip: "Book code"},	
	{id: "underlyingRIC", name: "Underlying RIC", field: "underlyingRIC", toolTip: "Underlying RIC"},
	{id: "salesComment", name: "Sales Comment", field: "salesComment", editor: Slick.Editors.LongText, toolTip: "Comments made by sales."},
	{id: "traderComment", name: "Trader Comment", field: "traderComment", editor: Slick.Editors.LongText, toolTip: "Comments made by the traders."},
	{id: "clientComment", name: "Client Comment", field: "clientComment", editor: Slick.Editors.LongText, toolTip: "Comments made by the client."}		
];

var allColumns = 
[
 	{id: "requestId", name: "Request ID", field: "identifier", sortable: true, toolTip: "Request unique identifier"},
	{id: "snippet", name: "Snippet", field: "request", cssClass: "cell-title", minWidth: 220, validator: requiredFieldValidator, toolTip: "Request snippet"},
	{id: "status", name: "Status", field: "status", sortable: true, toolTip: "Current status of request", formatter: statusFormatter},
	{id: "pickedUpBy", name: "Picked Up By", field: "pickedUpBy", sortable: true, toolTip: "User who picked up the request"},
	{id: "clientId", name: "Client", field: "clientId", sortable: true, toolTip: "Client this requests applies to", formatter: clientFormatter},	
	{id: "tradeDate", name: "Trade Date", field: "tradeDate", formatter: dateFormatter, sortable: true, toolTip: "Trade date"},
	{id: "expiryDate", name: "Maturity Date", field: "expiryDateString", toolTip: "Expiry date", formatter: dateFormatter},
	
	{id: "premiumAmount", name: "Theoretical Value", field: "premiumAmount", formatter: decimalFormatter, toolTip: "Theoretical value"},
	{id: "premiumPercentage", name: "Premium Percentage", field: "premiumPercentage", toolTip: "Premium percentage"},
	{id: "timeValue", name: "Time Value", field: "timeValue", formatter: decimalFormatter, toolTip: "Time value"},
	{id: "intrinsicValue", name: "Intrinsic Value", field: "intrinsicValue", formatter: decimalFormatter, toolTip: "Intrinsic value"},
	{id: "underlyingPrice", name: "Spot", field: "underlyingPrice", toolTip: "Underlying Spot price"},
	{id: "impliedVol", name: "Implied Volatility", field: "impliedVol", toolTip: "Implied volatility"},
	{id: "profitAndLossPoints", name: "Profit And Loss Graph", sortable: false, formatter: waitingFormatter, asyncPostRender: renderSparkline, toolTip: "Profit and loss points"},
					
	{id: "delta", name: "Delta", field: "delta", formatter: decimalFormatter, toolTip: "Delta", groupTotalsFormatter: sumTotalsFormatter},
	{id: "gamma", name: "Gamma", field: "gamma", formatter: decimalFormatter, toolTip: "Gamma", groupTotalsFormatter: sumTotalsFormatter},
	{id: "vega", name: "Vega", field: "vega", formatter: decimalFormatter, toolTip: "Vega", groupTotalsFormatter: sumTotalsFormatter},
	{id: "theta", name: "Theta", field: "theta", formatter: decimalFormatter, toolTip: "Theta", groupTotalsFormatter: sumTotalsFormatter},
	{id: "rho", name: "Rho", field: "rho", formatter: decimalFormatter, toolTip: "Rho", groupTotalsFormatter: sumTotalsFormatter},
			
	{id: "notionalMillions", name: "Notional Millions", field: "notionalMillions", toolTip: "Notional in Millions"},
	{id: "notionalFXRate", name: "Notional FX Rate", field: "notionalFXRate", toolTip: "Notional FX Rate"},
	{id: "notionalCurrency", name: "Notional Currency", field: "notionalCurrency", toolTip: "Notional Currency"},
	
	{id: "quantity", name: "Quantity", field: "quantity", toolTip: "Quantity"},
	{id: "contracts", name: "Contracts", field: "contracts", toolTip: "Number of contracts"},
	{id: "lotSize", name: "Lot Size", field: "lotSize", toolTip: "Lot Size"},
	{id: "multiplier", name: "Multiplier", field: "multiplier", toolTip: "Multiplier"},		

	{id: "deltaNotional", name: "Delta Notional", field: "deltaNotional", toolTip: "Delta Notional"},
	{id: "gammaNotional", name: "Gamma Notional", field: "gammaNotional", toolTip: "Gamma Notional"},
	{id: "vegaNotional", name: "Vega Notional", field: "vegaNotional", toolTip: "Vega Notional"},
	{id: "thetaNotional", name: "Theta Notional", field: "thetaNotional", toolTip: "Theta Notional"},
	{id: "rhoNotional", name: "Rho Notional", field: "rhoNotional", toolTip: "Rho Notional"},
	
	{id: "deltaShares", name: "Delta Shares", field: "deltaShares", toolTip: "Delta Shares"},
	{id: "gammaShares", name: "Gamma Shares", field: "gammaShares", toolTip: "Gamma Shares"},
	{id: "vegaShares", name: "Vega Shares", field: "vegaShares", toolTip: "Vega Shares"},
	{id: "thetaShares", name: "Theta Shares", field: "thetaShares", toolTip: "Theta Shares"},
	{id: "rhoShares", name: "Rho Shares", field: "rhoShares", toolTip: "Rho Shares"},
	
	{id: "bookCode", name: "Book Code", field: "bookCode", sortable: true, toolTip: "Book code"},	
	{id: "underlyingRIC", name: "Underlying RIC", field: "underlyingRIC", toolTip: "Underlying RIC"},
	
	{id: "salesComment", name: "Sales Comment", field: "salesComment", editor: Slick.Editors.LongText, toolTip: "Comments made by sales"},
	{id: "traderComment", name: "Trader Comment", field: "traderComment", editor: Slick.Editors.LongText, toolTip: "Comments made by the traders"},
	{id: "clientComment", name: "Client Comment", field: "clientComment", editor: Slick.Editors.LongText, toolTip: "Comments made by the client"},		
	
	{id: "salesCreditAmount", name: "Sales Credit Amount", field: "salesCreditAmount", toolTip: "Sales Credit Amount"},
	{id: "salesCreditPercentage", name: "Sales Credit Percentage", field: "salesCreditPercentage", toolTip: "Sales Credit Percentage"},
	{id: "salesCreditFXRate", name: "Sales Credit FX Rate", field: "salesCreditFXRate", toolTip: "Sales Credit FX Rate"},
	{id: "salesCreditCurrency", name: "Sales Credit Currency", field: "salesCreditCurrency", toolTip: "Sales Credit Currency"},
	
	{id: "premiumSettlementDate", name: "Premium Settlement Date", field: "premiumSettlementDate", toolTip: "Premium Settlement Date"},
	{id: "premiumSettlementDaysOverride", name: "Premium Settlement Days Override", field: "premiumSettlementDaysOverride", toolTip: "Premium Settlement Days Override"},
	{id: "premiumSettlementCurrency", name: "Premium Settlement Currency", field: "premiumSettlementCurrency", toolTip: "Premium Settlement Currency"},
	{id: "premiumSettlementFXRate", name: "Premium Settlement FX Rate", field: "premiumSettlementFXRate", toolTip: "Premium Settlement FX Rate"},		
	
	{id: "lastUpdatedBy", name: "Last Updated By", field: "lastUpdatedBy", toolTip: "User who last updated this request"},
	{id: "lastUpdate", name: "Last Updated At", field: "lastUpdate", toolTip: "Date of last update"}		
];

var options = 
{
	enableCellNavigation: true,
	enableColumnReorder: true,		
    editable: true,
    enableAddRow: false,
    asyncEditorLoading: true,
    autoEdit: false,
    forceFitColumns: false,
    cellHighlightCssClass: "priceUpdateIncrease",
    cellFlashingCssClass: "cellFlash",
    topPanelHeight:230,
    enableAsyncPostRender: true
};

function validateAgainstBothDates(startDate, endDate)
{
	return endDate - startDate >= 0;
}

function validateAgainstStartDate(startDate, gridDate)
{		
	return gridDate - startDate >= 0;
}

function validateAgainstEndDate(endDate, gridDate)
{
	return endDate - gridDate >= 0;
}

function convertToDate(dateToConvert)
{
	return new Date(dateToConvert.dayOfMonth + " " + dateToConvert.month + " " + dateToConvert.year);;
}

function requestsFilter(item, args)
{		
	if (args.startTradeDate != "" && !validateAgainstStartDate(new Date(args.startTradeDate), convertToDate(item["tradeDate"])))
		return false;
	
	if (args.endTradeDate != "" && !validateAgainstEndDate(new Date(args.endTradeDate),	convertToDate(item["tradeDate"])))
		return false;
	
	if (args.startTradeDate != "" && args.endTradeDate != "" && !validateAgainstBothDates(new Date(args.startTradeDate), 
			new Date(args.endTradeDate)))
		return false;
	
	if (args.startMaturityDate != "" && !validateAgainstStartDate(new Date(args.startMaturityDate), convertToDate(item["expiryDate"])))
		return false;
	
	if (args.endMaturityDate != "" && !validateAgainstEndDate(new Date(args.endMaturityDate),	convertToDate(item["expiryDate"])))
		return false;
	
	if (args.startMaturityDate != "" && args.endMaturityDate != "" && !validateAgainstBothDates(new Date(args.startMaturityDate), 
			new Date(args.endMaturityDate)))
		return false;	
	
	if (args.bookCode != "" && item["bookCode"].indexOf(args.bookCode) == -1)
	    return false;		
	
	if (args.status != "" && statusHashIndexedByStatusEnum && (statusHashIndexedByStatusEnum[item["status"]].toUpperCase()).indexOf(args.status.toUpperCase()) == -1)
	    return false;
	
	if (args.clientId != "" && clientHashIndexedById && (clientHashIndexedById[item["clientId"]].toUpperCase()).indexOf(args.clientId.toUpperCase()) == -1)
		return false;
	
	if (args.underlyingRIC != "" && item["underlyingRIC"].indexOf(args.underlyingRIC) == -1)
	    return false;		
	
	return true;
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
	
	var loadingIndicator = null;
	var groupItemMetadataProvider = new Slick.Data.GroupItemMetadataProvider();
	
	var dataView = new Slick.Data.DataView(
	{
	    groupItemMetadataProvider: groupItemMetadataProvider,
	    inlineFilters: true
	});
	
	var requestsGrid = new Slick.Grid("#requestsGrid", dataView, columns, options);
	requestsGrid.registerPlugin(groupItemMetadataProvider);
	
	var columnpicker = new Slick.Controls.ColumnPicker(allColumns, requestsGrid, options);
	
	requestsGrid.setSelectionModel(new Slick.RowSelectionModel());
	requestsGrid.setTopPanelVisibility(false);
	
	function datepickerOnClose(theDate, instance)
	{
		try
		{			
			if(theDate !== "" && ($(this).val() !== $(this).attr("default_value")))
			{
				$.datepicker.parseDate("yy-mm-dd", theDate);
				Slick.GlobalEditorLock.cancelCurrentEdit();
				filterHash[$(this).attr("criterion_name")] = $(this).val();
				updateFilter();
			}
		}
		catch(err)
		{
			alert('Invalid date! "YYYY-DD-MM" date format expected. For example: 2012-12-23.');
			$(this).val($(this).attr("default_value"));
		}		
	}
	
	$.datepicker.setDefaults({dateFormat : "yy-mm-dd", onClose : datepickerOnClose });
	$(".dateTxtBox").datepicker();
	
	$("#requests_search_save_btn").click(function()
	{
		$(".save-search-dialog").removeClass("dialog-hide");
		
		$("#save-search-dialog-parent").dialog(
		{	
			modal : true,
		    resizable: false,
		    width: 270,
			beforeClose: function()
			{
			},		    
			buttons: 
			{ 
				Save: function()
				{
					$(this).dialog("close");
					$(".save-search-dialog").addClass("dialog-hide");					
				},
				Cancel: function()
				{
					$(this).dialog("close");
					$(".save-search-dialog").addClass("dialog-hide");
				}				
			}			
		});
	});	
	
	var snippet = "";
		
	function constructSnippet()
	{
		var tempSnippet = "", strikeSnippet = "", callPutSnippet = "", maturityDateSnippet = "", underlyingSnippet = ""
		
		if($("#new-request-dialog-maturity-date").val() != $("#new-request-dialog-maturity-date").attr("default_value"))
			maturityDateSnippet = $("#new-request-dialog-maturity-date").val();
		
		if($("#new-request-dialog-underlying-ric").val() != $("#new-request-dialog-underlying-ric").attr("default_value"))
			underlyingSnippet = $("#new-request-dialog-underlying-ric").val();
		
		$("div.new-request-dialog-snippet-breakdown-class").each(function()
		{
			$(this).children("select.new-request-dialog-type-class:first").each(function()
			{
				tempSnippet = $(this).val();
			});
			
			$(this).children("input.new-request-dialog-qty-class:first").each(function()
			{
				if($(this).val() != "1" && ($(this).val() != $(this).attr("default_value")))
					tempSnippet = $(this).val() + tempSnippet;
			});
			
			$(this).children("select.new-request-dialog-side-class:first").each(function()
			{
				if($(this).val() == "Sell")
					tempSnippet = "-" + tempSnippet;
				else
					tempSnippet = "+" + tempSnippet;
			});
			
			$(this).children("input.new-request-dialog-strike-class:first").each(function()
			{
				if($(this).val() != $(this).attr("default_value"))
				{
					if(strikeSnippet == "" || strikeSnippet == $(this).val())
						strikeSnippet = $(this).val();
					else
						strikeSnippet = strikeSnippet + "," + $(this).val();					
				}
			});
			
			callPutSnippet = callPutSnippet + tempSnippet; 
		});
		
		if($("#new-request-dialog-expiry-type").val() == "A")
			callPutSnippet = callPutSnippet.toUpperCase();
		else
			callPutSnippet = callPutSnippet.toLowerCase();
		
		snippet = callPutSnippet + " " + strikeSnippet + " " + maturityDateSnippet + " " + underlyingSnippet;
		
		var snippetSet = $("#new-request-dialog-constructed-snippet").html(snippet);
		
		if(snippetMatches(snippet))
		{
			snippetSet.addClass("new-request-dialog-constructed-snippet-correct");
			snippetSet.removeClass("new-request-dialog-constructed-snippet-incorrect");
		}
		else
		{
			snippetSet.addClass("new-request-dialog-constructed-snippet-incorrect");
			snippetSet.removeClass("new-request-dialog-constructed-snippet-correct");		
		}
	}
		
	function pasteRequestSnippetFromDialog()
	{
		if(snippetMatches(snippet))
		{
			$("#requests_snippet").val(snippet);
			$("#new-request-dialog-parent").dialog("close");
			$(".new-requests-dialog").addClass("new-requests-dialog-hide");		
		}
		else
			alert("The snippet is incomplete and invalid. Pls set the other properties in order to paste it.");
	}
	
	function clearAddNewRequestDialog()
	{
		$("#new-request-dialog-constructed-snippet").html("");		
		$(".cloned-snippet").remove();
		$("input.new-requests-dialog").each(function() 
		{
			$(this).val($(this).attr("default_value"));
		});
	}
	
	$("#requests_add_more_button").click(function()
	{	
		$(".new-requests-dialog").removeClass("dialog-hide");
		
		$("#new-request-dialog-parent").dialog(
		{	
			modal : true,
		    resizable: false,
		    width: 395,
			beforeClose: function()
			{
				clearAddNewRequestDialog();
			},		    
			buttons: 
			{ 
				Paste: pasteRequestSnippetFromDialog,
				Cancel: function()
				{
					$(this).dialog("close");
					$(".new-requests-dialog").addClass("dialog-hide");
				},
				Clear: clearAddNewRequestDialog					
			}			
		});
		
		$("div.new-request-dialog-snippet-breakdown-class input.new-requests-dialog").change(constructSnippet); 
		$("div.new-request-dialog-snippet-breakdown-class select.new-requests-dialog").change(constructSnippet);
		$("#new-request-dialog-expiry-type").change(constructSnippet);
				
		// Initialize after dialog creation because autocomplete menu's z-index is incorrect.
		$("input.dialog_underlying_autocomplete").autocomplete(
		{
			minLength:1,
	        source: ajaxUnderlyingAutocomplete,
	        change: constructSnippet
	    });
		
		$("#new-request-dialog-maturity-date").datepicker(
		{
			dateFormat : "ddMyy", 
			onClose : constructSnippet 
		});
	});
	
	var legCount = 0;
	$("button.clone-snippet-add").click(function()
	{
		constructSnippet();
		
		var newLeg = "new-request-dialog-snippet-breakdown" + legCount++;
		$("#new-request-dialog-snippet-breakdown")
			.clone(true).attr("id", newLeg)
			.removeClass("clone-this-snippet")
			.removeClass("dialog-hide")
			.addClass("cloned-snippet")
			.insertBefore("#new-request-dialog-expiry-type");
		
		// After cloning set values back to default. 
		var qty_default = $("#" + newLeg + " " + "input.new-request-dialog-qty-class").attr("default_value");
		$("#" + newLeg + " " + "input.new-request-dialog-qty-class").val(qty_default);
		
		var strike_default = $("#" + newLeg + " " + "input.new-request-dialog-strike-class").attr("default_value");
		$("#" + newLeg + " " + "input.new-request-dialog-strike-class").val(strike_default);
	});
	
	$("button.clone-snippet-remove").click(function()
	{
		$(this).parent("div.new-request-dialog-snippet-breakdown-class.cloned-snippet").remove();
		constructSnippet();
	})	
	
	var chartDialogOptions = 
	{
		minWidth : 700, 
		minHeight : 600,
	    resizable: false			
	};
	
	var dynamicChartDialogOptions = 
	{
		minWidth : 700, 
		minHeight : 600,
	    resizable: false,
		beforeClose: function()
		{
			$(this).remove();
		}			
	};
	
	var chartCount = 0;
	
    function createChartDialog(requestId)
    {    	
    	if(requestId !== undefined)
		{
    		showLoadIndicator();
    		
    		var dynamicChartId = "dynamic-chart-" + chartCount++;
    		$(".chart-to-clone").clone().attr("id", dynamicChartId)
    			.removeClass("chart-to-clone")
    			.attr("title", "Charts for request for quote: " + requestId)
    			.dialog(dynamicChartDialogOptions);
    		
    		$("#" + dynamicChartId).children(".chart-content").tabs({heightStyle: "fill"});
    		
    		ajaxGetChartData(requestId, dynamicChartId);
		}		
    }	
		
	$("#requests_chart_chart_btn").click(function()
	{
		createChartDialog();
	});	
	
    function showLoadIndicator() 
    {
        if (!loadingIndicator) 
        {
        	loadingIndicator = $(".loading-indicator").appendTo(document.body);
        	var $g = $("#requestsGrid");
        	loadingIndicator
            	.css("position", "absolute")
            	.css("top", $g.position().top + $g.height() / 2 - loadingIndicator.height() / 2)
            	.css("left", $g.position().left + $g.width() / 2 - loadingIndicator.width() / 2);
        }
        loadingIndicator.show();
    }   
    
    var filterHash = {};
        
	showLoadIndicator();
	getStatusList();
	getUnderlyingList();
	getBookList();
	getClientList();	
	getRequestsFromTodayOnly();
	clearFilter();

	function updateFilter()
	{
		dataView.setFilterArgs(
		{	      
			bookCode : filterHash["bookCode"],
			status : filterHash["status"],
			underlyingRIC : filterHash["underlyingRIC"],
			clientId : filterHash["clientId"],
			startTradeDate : filterHash["startTradeDate"],
			endTradeDate : filterHash["endTradeDate"],
			startMaturityDate : filterHash["startMaturityDate"],
			endMaturityDate : filterHash["endMaturityDate"]		
		});
		
		dataView.refresh();
	}
	
	function clearFilter()
	{
		filterHash["bookCode"] = "";
		filterHash["clientId"] = "";
		filterHash["status"] = "";
		filterHash["underlyingRIC"] = "";
	    filterHash["startTradeDate"] = "";
	    filterHash["endTradeDate"] = "";
	    filterHash["startMaturityDate"] = "";
	    filterHash["endMaturityDate"] = "";
		updateFilter();
	}		

	dataView.beginUpdate();
	updateFilter();		
	dataView.setFilter(requestsFilter);
	dataView.endUpdate();
	
    $(".filter_textbox").keyup(function (e)
    {
    	Slick.GlobalEditorLock.cancelCurrentEdit();
    	filterHash[$(this).attr("criterion_name")] = $(this).val();
	    updateFilter();
    });
    
	function processNewlyCreatedRequest(newlyCreatedrequest)
	{
		if(newlyCreatedrequest)
			dataView.insertItem(0, newlyCreatedrequest);
	}
		
	function processStatusUpdates(setOfRequests)
	{
		if(!$("#groupByNothing").is(":checked") || !$('#turnOnStatusUpdates').is(':checked') || setOfRequests.length == 0)
			return;
		
		dataView.beginUpdate();
				
		for(var i = 0, size = setOfRequests.length; i < size; i++)
		{
			var request = setOfRequests[i];			
			var gridItem = dataView.getItemById(request["identifier"]);
			
			if((gridItem !== undefined) && (request !== undefined) && (gridItem["status"] != request["status"]))
			{
				gridItem["status"] = request["status"];
				gridItem["pickedUpBy"] = request["pickedUpBy"];
				dataView.updateItem(gridItem["identifier"], gridItem);			    	
				requestsGrid.flashCell(dataView.getRowById(request["identifier"]), requestsGrid.getColumnIndex("status"), 100);					
			}
		}
		
		dataView.endUpdate();
		requestsGrid.render();
	}
	
	function processCalculationUpdates(calculations)
	{
		if(!$("#groupByNothing").is(":checked") || !$('#turnOnCalculationUpdates').is(':checked') || calculations.length == 0)
			return;
		
		alert("Not yet implemented: " + calculations);
	}	
	
	function processPriceUpdates(prices)
	{
		if(!$("#groupByNothing").is(":checked") || (prices.length == 0) || !$('#turnOnPriceUpdates').is(':checked'))
			return;
	
		dataView.beginUpdate();
		var changes = {};
		var increaseInPrice = false;
				
		for(var i = 0, size = dataView.getLength(); i < size; i++)
		{
			var item = dataView.getItemByIdx(i);
			if((item !== undefined) && (prices[item["underlyingRIC"]] !== undefined))
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
	    
	var underlyingPriceChartOptions = 
	{
		chart: 
		{
			title: 'Underlying Price Charts'
		},
        hAxis: 
        {
            title: 'Underlying Price'
        },
        titlePosition: 'in',
        chartArea:
        {
        	left:200,
        	top:200,
        	width:'100%',
        	height:'100%'
        },
        height: 450,
        width: 550,
        legend:
        { 
        	position: 'bottom' 
        },
		backgroundColor: '#80800',
		tooltip: 
		{
			trigger: focus,
			showColorCode: true
		}
	};
	
	var volatilityChartOptions = 
	{
		chart: 
		{
			title: 'Volatility Charts'
		},
        hAxis: 
        {
            title: 'Volatility'
        },
        titlePosition: 'in',
        chartArea:
        {
        	left:200,
        	top:200,
        	width:'100%',
        	height:'100%'
        },
        height: 450,
        width: 550,
        legend:
        { 
        	position: 'bottom' 
        },
		backgroundColor: '#80800',
		tooltip: 
		{
			trigger: focus,
			showColorCode: true
		}
	};
	
	var timeToExpiryChartOptions = 
	{
		chart: 
		{
			title: 'Time To Expiry Charts'
		},
        hAxis: 
        {
            title: 'Time to expiry'
        },
        titlePosition: 'in',
        chartArea:
        {
        	left:200,
        	top:200,
        	width:'100%',
        	height:'100%'
        },
        height: 450,
        width: 550,
        legend:
        { 
        	position: 'bottom' 
        },
		backgroundColor: '#80800',
		tooltip: 
		{
			trigger: focus,
			showColorCode: true
		}
	};
	
	var theoreticalValueChartOptions = 
	{
		chart: 
		{
			title: 'Theoretical Value'
		},
        hAxis: 
        {
            title: 'Underlying Price'
        },
        titlePosition: 'in',
        chartArea:
        {
        	left:200,
        	top:200,
        	width:'100%',
        	height:'100%'
        },
        height: 500,
        width: 600,
        legend:
        { 
        	position: 'none' 
        },
		backgroundColor: '#80800',
		tooltip: 
		{
			trigger: focus
		}
	};	

    function drawGreekCharts(chartData, dataID, chartContainerId, horizontalAxisLabel, chartOptions)
    {
    	var data = new google.visualization.DataTable();
    	
		var columnData = chartData[dataID];
		
		var firstColumn = columnData[dataID];
		var deltaColumn = columnData["DELTA"];
		var gammaColumn = columnData["GAMMA"];
		var vegaColumn = columnData["VEGA"];
		var thetaColumn = columnData["THETA"];
		var rhoColumn = columnData["RHO"];
		
		var chartLineNames = [horizontalAxisLabel, "Delta", "Gamma", "Vega", "Theta", "Rho"];
    	
		for (var i = 0; i < chartLineNames.length; i++)
    		data.addColumn('number', chartLineNames[i]);
    	
		var chartLineData = [];
		for(var i = 0; i < firstColumn.length; i++)
		{
			chartLineData[i] = new Array(firstColumn[i], deltaColumn[i], gammaColumn[i], 
					vegaColumn[i], thetaColumn[i], rhoColumn[i]);			
		}

    	data.addRows(chartLineData);
    	
    	var view = new google.visualization.DataView(data);	    	

    	var chart = new google.charts.Line($("#" + chartContainerId).get(0));
    	chart.draw(view, chartOptions);
    	
        $('input.chart-line-display').change(function() 
	    {
	    	var colsToHide = $('input.chart-line-display:not(:checked)').map(function()
	    	{
	    		return parseInt($(this).attr("column_index"));
	    	}).get();
	    	
	    	view.hideColumns(colsToHide);
	    	chart.draw(view, chartOptions);
	    });
    }
    
    function drawTheoreticalValueChart(chartData, dataID, chartContainerId, horizontalAxisLabel, chartOptions)
    {
    	var data = new google.visualization.DataTable();
		var columnData = chartData[dataID];
		var firstColumn = columnData[dataID];
		
		var theoColumn = columnData["THEORETICAL_VALUE"];
		var chartLineNames = [horizontalAxisLabel, "Theoretical Value"];
    	
		data.addColumn('number', horizontalAxisLabel);
		data.addColumn('number', "Theoretical Value");
    	
		var chartLineData = [];
		for(var i = 0; i < firstColumn.length; i++)
			chartLineData[i] = new Array(firstColumn[i], theoColumn[i]);

    	data.addRows(chartLineData);   	

    	var chart = new google.charts.Line($("#" + chartContainerId).get(0));
    	chart.draw(data, chartOptions);
    }    
    
	function ajaxGetChartData(requestId, chartContainerId)
	{
		$.ajax(
		{
		    url: contextPath + "/requests/ajaxGetChartData", 
		    type: 'POST',
		    data: JSON.stringify(requestId),
		    dataType: 'json',  
		    contentType: 'application/json', 
		    mimeType: 'application/json',
		    timeout: 300000,
		    cache: false,
		    success: function(data) 
		    {
		    	drawGreekCharts(data, "UNDERLYING_PRICE", chartContainerId + " #underlying-price-charts-content",
		    			"Underlying Price", underlyingPriceChartOptions);
		    	
		    	drawGreekCharts(data, "VOLATILITY", chartContainerId + " #volatility-charts-content",
		    			"Volatility", volatilityChartOptions);
		    	
		    	drawGreekCharts(data, "TIME_TO_EXPIRY", chartContainerId + " #time-to-expiry-charts-content",
		    			"Time To Expiry", timeToExpiryChartOptions);
		    	
		    	drawTheoreticalValueChart(data, "UNDERLYING_PRICE", chartContainerId + " #theoretical-value-charts-content",
		    			"Underlying Price", theoreticalValueChartOptions);
		    	
		    	loadingIndicator.fadeOut();
		    },
	        error: function (xhr, textStatus, errorThrown) 
	        {
	        	if(textStatus != "timeout")
        		{
	        		if(xhr.status == 404) 
	        			alert('Failed to get chart data because the server is no longer available. Please try to reload the page.');
	        		else
	        			alert('Failed to get chart data for request: ' + requestId + ' because of a server error.');   
        		}
	        	else
	        		alert('Failed to get chart data because of a timeout after five seconds');
	        	
	        	loadingIndicator.fadeOut();
	        }
		});		
		
	}
	
	function ajaxSendNewRequest(snippet, bookCode, client, lastUpdatedBy)
	{
	    var json = { "request" : snippet, "bookCode" : bookCode, "clientId": client , "lastUpdatedBy" : lastUpdatedBy};
	    
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
		    	if(newlyCreatedrequest)
		    		processNewlyCreatedRequest(newlyCreatedrequest);
		    },
	        error: function (xhr, textStatus, errorThrown) 
	        {
	        	if(textStatus != "timeout")
        		{
	        		if(xhr.status == 404)
	        			alert('Failed to add request because the server is no longer available. Please try to reload the page.');
	        		else
	        			alert('Failed to add request [' + snippet + '] because of a server error.');   
        		}
	        	else
	        		alert('Failed to add new request because of timeout after five seconds');
	        }
		});		
	}
	
	$("#requests_add_button").click(function(event)
	{
		disableAddButton();
	    
		var snippet = $('#requests_snippet').val();
	    var bookCode = $('#requests_bookCode').val();
	    var client = clientHashIndexedByDesc[$('#requests_client').val()];
	    var lastUpdatedBy = "ladeoye"; // TODO
	    
	    clearNewRequestInputFields();	    
		
	    ajaxSendNewRequest(snippet, bookCode, client, lastUpdatedBy);
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
			    	priceUpdateInterval = setTimeout(getPriceUpdates, priceUpdateIntervalTime);
			    },
	            error: function (xhr, textStatus, errorThrown) 
	            {
	            	$("#turnOnPriceUpdates").prop('checked', false);
	            	window.clearInterval(priceUpdateInterval);
	            	
	            	if(textStatus != "timeout")
	        		{
		        		if(xhr.status == 404)
		        			alert('Price updates failed because the server is no longer available. Please try to reload the page.');
		        		else
		        			alert('Price updates failed because of a server error.');   		        			
	        		}
	            	else
	            		alert("Price updates timed-out after " + priceUpdateTimeout + " milliseconds. Retrigger manually.");
	            	
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
			    	calculationUpdateInterval = setTimeout(getCalculationUpdates, calculationUpdateIntervalTime);
			    },
	            error: function (xhr, textStatus, errorThrown) 
	            {
	            	$("#turnOnCalculationUpdates").prop('checked', false);
	            	window.clearInterval(calculationUpdateInterval);
	            	
	            	if(textStatus != "timeout")
	        		{
		        		if(xhr.status == 404)
		        			alert('Calculation updates failed because the server is no longer available. Please try to reload the page.');
		        		else
		        			alert('Calculation updates failed because of a server error.');   		        			
	        		}
	            	else
	            		alert('Calculation updates timed-out after ' + calculationUpdateTimeout + ' milliseconds. Retriggger manually.');
	                
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
			    	statusUpdateInterval = setTimeout(getStatusUpdates, statusUpdateIntervalTime);
			    },
	            error: function (xhr, textStatus, errorThrown) 
	            {
	            	$("#turnOnStatusUpdates").prop('checked', false);
	            	window.clearInterval(statusUpdateInterval);
	            	
	            	if(textStatus != "timeout")
	        		{
		        		if(xhr.status == 404)
		        			alert('Status updates failed because the server is no longer available. Please try to reload the page.');
		        		else
		        			alert('Status updates failed because of a server error.');   		        			
	        		}
	            	else
	            		alert('Status updates timed-out after ' + statusUpdateTimeout + ' milliseconds. Retrigger manually.');
	                
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

		dataView.sort((sortColumn === "tradeDate" ? dateComparer : lexographicComparer), args.sortAsc);
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
	
	$("#priceFrequencySliderValue").html(" = 1000 ms");
	
	$("#priceFrequencySlider").slider(
	{
		"range": "min",
		"min" : 200,
		"max" : 60000,
		"step" : 200,
		"value" : 1000,
	    "slide": function (event, ui)
	    {
	    	if (priceUpdateIntervalTime != ui.value)
	    	{
	    		priceUpdateIntervalTime = ui.value;
	    		$("#priceFrequencySliderValue").html(" = " + priceUpdateIntervalTime + " ms");
	    	}	    	
	    }
	});
	
	$("#calculationFrequencySliderValue").html(" = 1000 ms");
	
	$("#calculationFrequencySlider").slider(
	{
		"range": "min",
		"min" : 200,
		"max" : 60000,
		"step" : 200,
		"value" : 1000,
	    "slide": function (event, ui)
	    {
	    	if (calculationUpdateIntervalTime != ui.value)
	    	{
	    		calculationUpdateIntervalTime = ui.value;
	    		$("#calculationFrequencySliderValue").html(" = " + calculationUpdateIntervalTime + " ms");
	    	}
	    }
	});
	
	$("#statusFrequencySliderValue").html(" = 1000 ms");
	
	$("#statusFrequencySlider").slider(
	{
		"range": "min",
		"min" : 200,
		"max" : 60000,
		"step" : 200,
		"value" : 1000,
	    "slide": function (event, ui)
	    {
	    	if (statusUpdateIntervalTime != ui.value)
	    	{
	    		statusUpdateIntervalTime = ui.value;
	    		$("#statusFrequencySliderValue").html(" = " + statusUpdateIntervalTime + " ms");
	    	}
	    }
	});
	
	$("#priceTimeoutSliderValue").html(" = 5000 ms");
	
	$("#priceTimeoutSlider").slider(
	{
		"range": "min",
		"min" : 3000,
		"max" : 60000,
		"step" : 1000,
		"value" : 5000,
	    "slide": function (event, ui)
	    {
	    	if (priceUpdateTimeout != ui.value)
			{
	    		priceUpdateTimeout = ui.value;
	    		$("#priceTimeoutSliderValue").html(" = " + priceUpdateTimeout + " ms");
			}
	    }
	});
	
	$("#statusTimeoutSliderValue").html(" = 5000 ms");
	
	$("#statusTimeoutSlider").slider(
	{
		"range": "min",
		"min" : 3000,
		"max" : 60000,
		"step" : 1000,
		"value" : 5000,
	    "slide": function (event, ui)
	    {
	    	if (statusUpdateTimeout != ui.value)
			{
	    		statusUpdateTimeout = ui.value;
	    		$("#statusTimeoutSliderValue").html(" = " + statusUpdateTimeout + " ms");
			}
	    }
	});
	
	$("#calculationTimeoutSliderValue").html(" = 5000 ms");

	$("#calculationTimeoutSlider").slider(
	{
		"range": "min",
		"min" : 3000,
		"max" : 60000,
		"step" : 1000,
		"value" : 5000,
	    "slide": function (event, ui)
	    {
	    	if (calculationUpdateTimeout != ui.value)
			{
	    		calculationUpdateTimeout = ui.value;
	    		$("#calculationTimeoutSliderValue").html(" = " + calculationUpdateTimeout + " ms");
			}
	    }
	});
	
	function appendToTopPanel()
	{
		$("#requestsInlineConfigurePanel").appendTo(requestsGrid.getTopPanel());
		$("#requestsInlineGroupByPanel").appendTo(requestsGrid.getTopPanel());
		$("#requestsInlineSearchPanel").appendTo(requestsGrid.getTopPanel());
		$("#requestsInlineFilterPanel").appendTo(requestsGrid.getTopPanel());
		$("#requestsInlineChartPanel").appendTo(requestsGrid.getTopPanel());
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
        		// Does not clear previous search criteria because they may still be active.        		
        		$("#requestsInlineSearchPanel").show();
        		break;
        	case "filter":
        		// Does not clear previous filters because they may still be active.
        		$("#requestsInlineFilterPanel").show();
        		break;
        	case "chart":
        		$("#requestsInlineChartPanel").show();
        		break;        		
    	}
    	
    	requestsGrid.setTopPanelVisibility(true);
    	requestsGrid.render();
	});
	
	$('.hideTopPanel').bind('click', function(event) 
	{
		requestsGrid.setTopPanelVisibility(false);
		if($(this).is(".filter_search_close_btn"))
		{			
			$(".filter_textBox").each(function(index)
			{
				if($(this).val() != $(this).attr("default_value"))
					$("#requests_filter_button").addClass("filter_on");					
			})
			
			$(".search_textBox").each(function(index)
			{
				if($(this).val() != $(this).attr("default_value"))
					$("#requests_search_button").addClass("filter_on");					
			})			
		}
	});
	
	var priceUpdateInterval;
	var statusUpdateInterval;
	var calculationUpdateInterval;
	
	$('#turnOnPriceUpdates').bind('click', function(event) 
	{
		if($('#turnOnPriceUpdates').is(':checked'))
			getPriceUpdates();
		else
			window.clearTimeout(priceUpdateInterval);
	});
	
	$('#turnOnCalculationUpdates').bind('click', function(event) 
	{
		if($('#turnOnCalculationUpdates').is(':checked'))
			getCalculationUpdates();
		else		
			window.clearTimeout(calculationUpdateInterval);
	});
	
	$('#turnOnStatusUpdates').bind('click', function(event) 
	{
		if($('#turnOnStatusUpdates').is(':checked'))
			getStatusUpdates();
		else		
			window.clearInterval(statusUpdateInterval);
	});	
			
	requestsGrid.onContextMenu.subscribe(function (e) 
	{
        var cell = requestsGrid.getCellFromEvent(e);
        var item = dataView.getItem(cell.row);
        e.preventDefault();
        
        if (requestsGrid.getColumns()[cell.cell].id == "status")
        {
        	$("#requestContextMenu").hide();
        	        	
        	$("#statusContextMenu li").each(function(index)
			{
        		if(item["status"] === $(this).attr("data"))
            		$(this).hide();
        		else
        			$(this).show();
			});
        	
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
        	$("#statusContextMenu").hide();
        	
        	$("#requestContextMenu li").each(function(index)
			{
        		if(item["status"] === $(this).attr("data"))
            		$(this).hide();
        		else
        			$(this).show();
			});        	
        	
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
	
	function getStatusList()
	{
		$.ajax({
		    url: contextPath + "/requests/statuses", 
		    type: 'GET', 
		    dataType: 'json',  
		    contentType: 'application/json',
		    mimeType: 'application/json',
		    timeout: 5000,
		    cache: false,
		    success: function(statuses) 
		    {
		    	statusHashIndexedByStatusEnum = statuses;
		    	
		    	for(var status in statuses)
		    		statusHashIndexedByDesc[statuses[status]] = status;
		    }, 
            error: function (xhr, textStatus, errorThrown) 
            {
                alert('Failed to get list of statuses because of a server error.');
            }
		});	
	}
	
	// TODO - decide if this is needed
	function getUnderlyingList()
	{
		$.ajax({
		    url: contextPath + "/underlyings/all", 
		    type: 'GET', 
		    dataType: 'json',  
		    contentType: 'application/json',
		    mimeType: 'application/json',
		    timeout: 5000,
		    cache: false,
		    success: function(underlyings) 
		    {
		    },
            error: function (xhr, textStatus, errorThrown) 
            {
                alert('Failed to get list of underlyings because of a server error.');
            }
		});	
	}
	
	// TODO - decide if this is needed	
	function getBookList()
	{
		$.ajax({
		    url: contextPath + "/books/all", 
		    type: 'GET', 
		    dataType: 'json',  
		    contentType: 'application/json',
		    mimeType: 'application/json',
		    timeout: 5000,
		    cache: false,
		    success: function(books) 
		    {
		    },
            error: function (xhr, textStatus, errorThrown) 
            {
                alert('Failed to get list of books because of a server error.');
                loadingIndicator.fadeOut();
            }
		});	
	}
		
	function getClientList()
	{
		$.ajax({
		    url: contextPath + "/clients/all", 
		    type: 'GET', 
		    dataType: 'json',  
		    contentType: 'application/json',
		    mimeType: 'application/json',
		    timeout: 5000,
		    cache: false,
		    success: function(clients) 
		    {
                $.map(clients, function (client) 
                {
                	clientHashIndexedByDesc[client.name] = client.clientId;
                	clientHashIndexedById[client.clientId] = client.name;                	
                });		    	
		    },
            error: function (xhr, textStatus, errorThrown) 
            {
                alert('Failed to get list of Clients because of a server error.');
                loadingIndicator.fadeOut();
            }
		});	
	}
	
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
		    	$('.slick-header-columns').children().eq(0).trigger('click');
		    	loadingIndicator.fadeOut();
		    },
            error: function (xhr, textStatus, errorThrown) 
            {
                alert('Error: ' + xhr.responseText);
                loadingIndicator.fadeOut();
            }
		});
	}
		
	$(".requests_book_autocomplete").autocomplete(
	{
		minLength:3,
        source: function (request, response) 
        {
            $.ajax(
            {
                type: "GET",
                url: contextPath + "/books/matchingBookTags?pattern=" + request.term,
                dataType: "json",
                data: 
                {
                    term: request.termCode
                },
                error: function (xhr, textStatus, errorThrown) 
                {
                    alert('Failed to retrieve book autocomplete data because of a server error.');
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
	
	$(".requests_client_autocomplete").autocomplete(
	{
		minLength:2,
        source: function (request, response) 
        {
            $.ajax(
            {
                type: "GET",
                url: contextPath + "/clients/matchingClientTags?pattern=" + request.term,
                dataType: "json",
                data: 
                {
                    term: request.termCode
                },
                error: function (xhr, textStatus, errorThrown) 
                {
                    alert('Failed to retrieve client autocomplete data because of a server error.');
                },              
                success: function (clients)
                {
                    response($.map(clients, function (client) 
                    {
                        return {
                            		label: client.label,
                            		value: client.value
                        		}
                    }));
                }
            });
        }
    });
		
	function displayLabel(event, ui)
	{
        event.preventDefault();
        $(event.currentTarget).val(ui.item.label);
	}	
	
	$(".requests_client_autocomplete").on("autocompleteselect", displayLabel);
	$(".requests_client_autocomplete").on("autocompletefocus", displayLabel);	
	
	$(".requests_status_autocomplete").on("autocompleteselect", displayLabel);
	$(".requests_status_autocomplete").on("autocompletefocus", displayLabel);	
	
	function ajaxUnderlyingAutocomplete(request, response) 
    {
        $.ajax(
        {
            type: "GET",
            url: contextPath + "/underlyings/matchingUnderlyingTags?pattern=" + request.term,
            dataType: "json", 
            data: 
            {
                term: request.termCode
            },
            error: function (xhr, textStatus, errorThrown) 
            {
                alert('Failed to retrieve underlying autocomplete data because of a server error.');
            },
            success: function (underlyings) 
            {
                response($.map(underlyings, function (underlying) 
                {
                    return {
                        		label: underlying.value + "  (" + underlying.label + ")",
                        		value: underlying.value
                    		}
                }));
            }
        });
    }	
		
	$(".requests_underlying_autocomplete").autocomplete(
	{
		minLength:1,
        source: ajaxUnderlyingAutocomplete
    });
	
	function ajaxStatusAutocomplete(request, response) 
    {
        $.ajax(
        {
            type: "GET",
            url: contextPath + "/requests/matchingStatusTags?pattern=" + request.term,
            dataType: "json", 
            data: 
            {
                term: request.termCode
            },
            error: function (xhr, textStatus, errorThrown) 
            {
                alert('Failed to retrieve status autocomplete data because of a server error.');
            },
            success: function (statuses) 
            {
                response($.map(statuses, function (status) 
                {
                    return {
                        		label: status.label,
                        		value: status.value
                    		}
                }));
            }
        });
    }	
	
	$(".requests_status_autocomplete").autocomplete(
	{
		minLength:1,
        source: ajaxStatusAutocomplete
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
	            	alert("Failed to update the status of request: "  + identifier + " from: " + oldStatus + " to: " + newStatus + " because of a server error.");                	
	        }
		});
	}	
	
	function saveSearch()
	{
		$.ajax({
		    url: contextPath + "/searches/ajaxInsert", 
		    type: 'POST',
		    data: JSON.stringify(json),
		    dataType: 'json',  
		    contentType: 'application/json', 
		    mimeType: 'application/json',
		    timeout: 5000,
		    cache: false,
		    success: function(savedSearches) 
		    {
		    	if(savedSearches)
	    		{
	    		
	    		}
		    },		    
	        error: function (xhr, textStatus, errorThrown) 
	        {
	        	if(textStatus == "timeout")
	        		alert("Failed to save the searches. Timed-out after five seconds.");
	        	else
	            	alert("Failed to save the searches because of a server error.");                	
	        }
		});		
	}
	
	function addCriterionToCriteria(criterionOwner, criterionName, criterionValue)
	{
		var criterion = {};
		criterion["owner"] = criterionOwner;
		criterion["name"] = criterionName;
		criterion["value"] = criterionValue;		
		return criterion;
	}
	
	function collateCriteria()
	{
		var criteria = [];
		
		$(".requests_search_collate").each(function()
		{
			if($(this).val() !== $(this).attr("default_value"))
				criteria.push(addCriterionToCriteria("NO_OWNER", $(this).attr("criterion_name"), $(this).val()));
		});
		
		// Transformation from client description to client ID is required....
		if($("#requests_search_client").val() !== $("#requests_search_client").attr("default_value"))
		{
			criteria.push(addCriterionToCriteria("NO_OWNER", $("#requests_search_client").attr("criterion_name"), 
					clientHashIndexedByDesc[$("#requests_search_client").val()]));
		}
		
		// Transformation from status description to status enum is required....		
		if($("#requests_search_status").val() !== $("#requests_search_status").attr("default_value"))
		{			
			criteria.push(addCriterionToCriteria("NO_OWNER", $("#requests_search_status").attr("criterion_name"), 
					statusHashIndexedByDesc[$("#requests_search_status").val()]));
		}
				
		return criteria;
	}
	
	function performSearch()
	{
		showLoadIndicator();

		$.ajax({
		    url: contextPath + "/requests/ajaxSearch", 
		    type: 'POST',
		    data: JSON.stringify(collateCriteria()),
		    dataType: 'json',  
		    contentType: 'application/json', 
		    mimeType: 'application/json',
		    timeout: 60000,
		    cache: false,
		    success: function(searchResults) 
		    {
		    	if(searchResults)
	    		{
			    	dataView.setItems(searchResults, "identifier");
			    	loadingIndicator.fadeOut();		    		
	    		}
		    	else
	    		{
	    			loadingIndicator.fadeOut();
	    			alert("No results were returned from the server. Pls enhance search criteria.");
	    		}		    			    	
		    },		    
	        error: function (xhr, textStatus, errorThrown) 
	        {
	        	loadingIndicator.fadeOut();
	        	
	        	if(textStatus == "timeout")
	        		alert("Failed to retrieve result of search. Timed-out after 1 minute.");
	        	else
	            	alert("Failed to retrieve search results because of a server error.");                	
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
        	case "PICKED_UP":
        		ajaxSendStatusUpdate(row, "PICKED_UP");	
        		break;
        	case "CHART":
        		createChartDialog(dataView.getItem(row).identifier);
        		break;
        	case "CLONE":
        		var snippet = dataView.getItem(row).request;
        		var bookCode = dataView.getItem(row).bookCode;
        		var client = dataView.getItem(row).clientId;
        		var lastUpdatedBy = "ladeoye" // TODO
        		ajaxSendNewRequest(snippet, bookCode, client, lastUpdatedBy);
        		break;
        	case "CUT_AND_PASTE":
        		$("#requests_snippet").val(dataView.getItem(row).request);
        		$("#requests_bookCode").val(dataView.getItem(row).bookCode);
        		$("#requests_client").val(clientHashIndexedById[dataView.getItem(row).clientId]);
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
		clearFilterInputFields();
		clearSearchInputFields();
		clearFilter();
		clearNewRequestInputFields();		
		disableAddButton();
		$("#requests_filter_button").removeClass("filter_on");
		$("#requests_search_button").removeClass("filter_on");
		getRequestsFromTodayOnly();
	});
	
	$("#requests_search_search_btn").click(function()
	{		
		performSearch();
	});	
	
	$(".filter_search_textBox").click(function()
	{
		if($(this).val() == $(this).attr("default_value"))
		{
			$(this).val("");			
		}
	});

	$(".filter_search_textBox").focusout(function()
	{
		if(trimSpaces($(this).val()) == "")
			$(this).val($(this).attr("default_value"));
	});
	
	function clearFilterInputFields()
	{				
		$("div#requestsFilter input.filter_search_textBox").each(function(index)
		{
			$(this).val($(this).attr("default_value"));
		});		
	}
	
	function clearSearchInputFields()
	{				
		$("div#requestsSearch input.filter_search_textBox").each(function(index)
		{
			$(this).val($(this).attr("default_value"));
		});		
	}	
	
	$(".requests_filter_search_clear_btn").click(function()
	{
		if($(this).is("#requests_filter_clear_btn"))
		{
			clearFilterInputFields();
			$("#requests_filter_button").removeClass("filter_on");
			clearFilter();
		}

		if($(this).is("#requests_search_clear_btn"))
		{
			clearSearchInputFields();
			$("#requests_search_button").removeClass("filter_on");
		}
		
		getRequestsFromTodayOnly();
	});
		
	function groupByBook() 
	{
		dataView.setGrouping(
		{
			getter: "bookCode",
			formatter: function (g)
			{
				return "Book code: " + g.value + "  <span style='color:green'>(" + g.count + " items)</span>";
			},
			aggregateCollapsed: false,
			lazyTotalsCalculation: true
		});
	}
	
	function groupByClient() 
	{
		dataView.setGrouping(
		{
			getter: "clientId",
			formatter: function (g)
			{
				return "Client: " + g.value + "  <span style='color:green'>(" + g.count + " items)</span>";
			},
			aggregateCollapsed: false,
			lazyTotalsCalculation: true
		});
	}
	
	function groupByTradeDate() 
	{
		dataView.setGrouping(
		{
			getter: "tradeDate",
			formatter: function (g)
			{
				return "Trade date: " + g.value + "  <span style='color:green'>(" + g.count + " items)</span>";
			},
			aggregateCollapsed: false,
			lazyTotalsCalculation: true
		});
	}	

	function groupByStatus() 
	{
		dataView.setGrouping(
		{
			getter: "status",
			formatter: function (g)
			{
				return "Status: " + g.value + "  <span style='color:green'>(" + g.count + " items)</span>";
			},
			aggregateCollapsed: false,
			lazyTotalsCalculation: true
		});
	}	
	
	function groupByUnderlying() 
	{
		dataView.setGrouping(
		{
			getter: "underlyingRIC",
			formatter: function (g)
			{
				return "Underlying: " + g.value + "  <span style='color:green'>(" + g.count + " items)</span>";
			},
			aggregators:
			[ 
			  new Slick.Data.Aggregators.Sum("delta"),
			  new Slick.Data.Aggregators.Sum("gamma"),
			  new Slick.Data.Aggregators.Sum("vega"),
			  new Slick.Data.Aggregators.Sum("theta"),
			  new Slick.Data.Aggregators.Sum("rho")			  
			],
			aggregateCollapsed: false,
			lazyTotalsCalculation: true
		});
	}
	
	$("#groupByNothing").click(function()
	{
		if($("#groupByNothing").is(":checked"))
			dataView.setGrouping([]);
	});

	$("#groupByClient").click(function()
	{
		if($("#groupByClient").is(":checked"))
			groupByClient();
	});
		
	$("#groupByBook").click(function()
	{
		if($("#groupByBook").is(":checked"))
			groupByBook();
	});
	
	$("#groupByStatus").click(function()
	{
		if($("#groupByStatus").is(":checked"))
			groupByStatus();
	});
	
	$("#groupByTradeDate").click(function()
	{
		if($("#groupByTradeDate").is(":checked"))
			groupByTradeDate();
	});
	
	$("#groupByUnderlying").click(function()
	{
		if($("#groupByUnderlying").is(":checked"))
			groupByUnderlying();
	});
});
