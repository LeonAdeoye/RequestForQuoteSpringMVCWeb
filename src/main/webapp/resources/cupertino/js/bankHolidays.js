function validityFormatter(row, cell, value, columnDef, dataContext)
{
	return value ? "Valid" : "Invalid";
}

var columns = 
[
 	{id: "identifier", name: "Identifier", field: "identifier", sortable: true, toolTip: "Bank holiday's unique identifier", width: 110},
	{id: "location", name: "Location", field: "location", sortable: true, toolTip: "Bank holiday's location", width: 110},
	{id: "bankHolidayDate", name: "Bank holiday date", field: "bankHolidayDateString", sortable: true, toolTip: "Bank Holiday's date" , width: 110,  formatter: dateFormatter},
	{id: "isValid", name: "Validity", field: "isValid", sortable: true, toolTip: "BankHoliday's validity status", formatter: validityFormatter},
	{id: "lastUpdatedBy", name: "Last updated by", field: "lastUpdatedBy", sortable: true, toolTip: "User to last update the bank holiday" , width: 95}
];

var options = 
{
	enableCellNavigation: true,
	enableColumnReorder: true,		
    editable: true,
    enableAddRow: false,
    asyncEditorLoading: true,
    autoEdit: false,
    cellFlashingCssClass: "cellFlash",
    forceFitColumns: false
};

function enableAddButton()
{
	$("#new-bankHoliday-add").removeAttr('disabled');
}

function disableAddButton()
{
	$("#new-bankHoliday-add").attr('disabled', 'disabled');
}

function toggleAddButtonState()
{
	if($("#new-bankHoliday-location").val() != "" && $("#new-bankHoliday-location").val() != $("#new-bankHoliday-location").attr("default_value")
			&& ($("#new-bankHoliday-date").val() != "") && $("#new-bankHoliday-date").val() != $("#new-bankHoliday-date").attr("default_value"))
		enableAddButton();
	else
		disableAddButton();
}

function clearNewbankHolidayInputFields()
{
	$("#new-bankHoliday-location").val($("#new-bankHoliday-location").attr("default_value"));
	$("#new-bankHoliday-date").val($("#new-bankHoliday-date").attr("default_value"));	
}

$(document).ready(function()
{
	var bankHolidayUpdateAjaxLock = false;
	var bankHolidayUpdateIntervalTime = 1000;
	var bankHolidayUpdateTimeout = 5000;
	
	var loadingIndicator = null;
	var groupItemMetadataProvider = new Slick.Data.GroupItemMetadataProvider();
	
	var dataView = new Slick.Data.DataView(
	{
	    groupItemMetadataProvider : groupItemMetadataProvider,
	    inlineFilters : true
	});
	
	var bankHolidaysGrid = new Slick.Grid("#bankHolidaysGrid", dataView, columns, options);
	
	bankHolidaysGrid.registerPlugin(groupItemMetadataProvider);	
	bankHolidaysGrid.setSelectionModel(new Slick.RowSelectionModel());
	bankHolidaysGrid.setTopPanelVisibility(false);
	
	function datepickerOnClose(theDate, instance)
	{
		try
		{			
			if(theDate !== "" && ($(this).val() !== $(this).attr("default_value")))
			{
				$.datepicker.parseDate("yy-mm-dd", theDate);
				toggleAddButtonState();
			}
		}
		catch(err)
		{
			alert('Invalid date! "YYYY-DD-MM" date format expected. For example: 2012-12-23.');
			$(this).val($(this).attr("default_value"));
		}		
	}	
	
	$(".new-bankHoliday-btn").button();
	$.datepicker.setDefaults({dateFormat : "yy-mm-dd", onClose : datepickerOnClose });
	$("#new-bankHoliday-date").datepicker();	
	
	disableAddButton();	
	$("#new-bankHoliday-location").keyup(toggleAddButtonState);
	$("#new-bankHoliday-location").focusout(toggleAddButtonState);
	$("#new-bankHoliday-date").keyup(toggleAddButtonState);
	$("#new-bankHoliday-date").focusout(toggleAddButtonState);
	
	
	$("input.new-bankHoliday-input-class").click(function()
	{
		if($(this).val() === $(this).attr("default_value"))
		{
			$(this).val("");
			disableAddButton();
		}
	});	
	
	$('#new-bankHoliday-import-btn').inputFileText({
	    text : "Import",
	    buttonClass : "import-btn-class"
	});
	
	$('.import-btn-class').button();

	$("input.new-bankHoliday-input-class").focusout(function()
	{
		if(trimSpaces($(this).val()) === "")
			$(this).val($(this).attr("default_value"));
	});	

	$("#new-bankHoliday-clear").click(function()
	{
		clearNewbankHolidayInputFields();		
		disableAddButton();
	});
	
	$("#new-bankHoliday-add").click(function()
	{
		var location = $("#new-bankHoliday-location").val();
		var updatedByUser = "ladeoye";
		var bankHolidayDate = $("#new-bankHoliday-date").val();
		showLoadIndicator();
		ajaxAddNewBankHoliday(location, bankHolidayDate, updatedByUser);				
	});
	
	$("input#new-bankHoliday-location").autocomplete(
	{
		minLength:2,
        source: ajaxLocationAutocomplete
    });	
	
	function flashNewBankHolidayRow(newBankHolidayId)
	{
		var columns = bankHolidaysGrid.getColumns();
		var size = columns.length;
		
		for(var index = 0; index < size; index++)
			bankHolidaysGrid.flashCell(dataView.getRowById(newBankHolidayId), bankHolidaysGrid.getColumnIndex(columns[index].id), 100);
	}
	
    function showLoadIndicator() 
    {
        if (!loadingIndicator) 
        {
        	loadingIndicator = $(".loading-indicator").appendTo(document.body);
        	var $g = $("#bankHolidaysGrid");
        	loadingIndicator
            	.css("position", "absolute")
            	.css("top", $g.position().top + $g.height() / 2 - loadingIndicator.height() / 2)
            	.css("left", $g.position().left + $g.width() / 2 - loadingIndicator.width() / 2);
        }
        loadingIndicator.show();
    }
    
	function ajaxLocationAutocomplete(request, response) 
    {
        $.ajax(
        {
            type: "GET",
            url: contextPath + "/bankHolidays/matchingLocationTags?pattern=" + request.term,
            dataType: "json", 
            data: 
            {
                term : request.termCode
            },
            error: function (xhr, textStatus, errorThrown) 
            {
            	console.log(xhr.responseText);
                alert('Failed to retrieve location autocomplete data because of a server error.');
            },
            success: function (locations) 
            {
                response($.map(locations, function (location) 
                {
                    return 
                    {	
                    	label : location.label,
                        value : location.value
                    }
                }));
            }
        });
    }    
	
	function ajaxGetListOfBankHolidays() 
	{
		showLoadIndicator();
		
		$.ajax({
		    url: contextPath + "/bankHolidays/ajaxGetListOfAllBankHolidays", 
		    type: 'GET', 
		    dataType: 'json',  
		    contentType: 'application/json',
		    mimeType: 'application/json',
		    timeout: bankHolidayUpdateTimeout,
		    cache: false,
		    success: function(bankHolidays) 
		    {
		    	dataView.setItems(bankHolidays, "identifier");
		    	loadingIndicator.fadeOut();
		    },
            error: function (xhr, textStatus, errorThrown) 
            {
            	loadingIndicator.fadeOut();
            	console.log(xhr.responseText);
            	alert('Failed to retrieve list of bankHolidays because of a server error.');                
            }
		});
	}
	
	function ajaxUpdateBankHoliday(bankHolidayId, location, bankHolidayDate, isValid, lastUpdatedBy) 
	{
		var bankHolidayDetails = 
		{ 
			"identifier" : bankHolidayId, 
			"location" : location,
			"bankHolidayDate" : bankHolidayDate, 
			"isValid" : isValid,
			"lastUpdatedBy" : lastUpdatedBy 
		};
		
		$.ajax({
		    url: contextPath + "/bankHolidays/ajaxUpdateBankHoliday", 
		    type: 'POST', 
		    dataType: 'json',
		    data: JSON.stringify(bankHolidayDetails),
		    contentType: 'application/json',
		    mimeType: 'application/json',
		    timeout: bankHolidayUpdateTimeout,
		    cache: false,
		    success: function(result) 
		    {
		    	loadingIndicator.fadeOut();
	    		var item = dataView.getItemById(bankHolidayId);
	    		item["isValid"] = isValid;
	    		dataView.updateItem(bankHolidayId, item);		    				    	
		    	bankHolidaysGrid.flashCell(dataView.getRowById(bankHolidayId), bankHolidaysGrid.getColumnIndex("isValid"), 100);
		    	
		    	if(!result)
		    		alert("Failed to update the validity.");		    	
		    },
            error: function (xhr, textStatus, errorThrown) 
            {
            	loadingIndicator.fadeOut();
            	console.log(xhr.responseText);
            	alert('Failed to update the validity details because of a server error.');                
            }
		});
	}
	
	// TODO
	function ajaxGetAnyNewBankHolidays()
	{
		$.ajax({
		    url: contextPath + "/bankHolidays/ajaxGetAnyNewBankHolidays", 
		    type: 'GET', 
		    dataType: 'json',
		    contentType: 'application/json',
		    mimeType: 'application/json',
		    timeout: bankHolidayUpdateTimeout,
		    cache: false,
		    success: function(newBankHolidays) 
		    {
		    	loadingIndicator.fadeOut();		    	
		    	
				if(newBankHolidays)
				{
				 	var length = newBankHolidays.length;				 	
					for(var index = 0; index < length; index++)
					{
						dataView.insertItem(0, 
						{
							"identifier" : newBankHolidays[index].identifier, 
							"location" : newBankHolidays[index].location , 
							"bankHolidayDate" : newBankHolidays[index].bankHolidayDate, 
							"isValid" : newBankHolidays[index].isValid, 
							"lastUpdatedBy" : newBankHolidays[index].lastUpdatedBy 
						});
					
						flashBankHolidayRow(newBankHolidays[index].identifier);
					}
				}
		    },
            error: function (xhr, textStatus, errorThrown) 
            {
            	loadingIndicator.fadeOut();
            	console.log(xhr.responseText);
            	alert('Failed to get an update of any new bankHolidays because of a server error.');                
            }
		});		
	}
	
	function ajaxAddNewBankHoliday(location, bankHolidayDate, lastUpdatedBy) 
	{
		var newBankHoliday = 
		{
			"identifier" : -1,
			"location" : location, 
			"bankHolidayDate" : bankHolidayDate, 
			"isValid" : true, 
			"lastUpdatedBy" : lastUpdatedBy 
		};
		
		$.ajax({
		    url: contextPath + "/bankHolidays/ajaxAddNewBankHoliday", 
		    type: 'POST', 
		    dataType: 'json',
		    data: JSON.stringify(newBankHoliday),
		    contentType: 'application/json',
		    mimeType: 'application/json',
		    timeout: bankHolidayUpdateTimeout,
		    cache: false,
		    success: function(newBankHolidayId) 
		    {
		    	loadingIndicator.fadeOut();
		    	
				if(newBankHolidayId > 0)
				{
					dataView.insertItem(0, 
					{
						"identifier" : newBankHolidayId, 
						"location" : location, 
						"bankHolidayDate" : bankHolidayDate, 
						"isValid" : true, 
						"lastUpdatedBy" : lastUpdatedBy 
					});
					
					flashNewBankHolidayRow(newBankHolidayId);
					disableAddButton();					
					clearNewbankHolidayInputFields();
				}
				else
					alert("Failed to insert the new bank holiday because of a server error.");
		    },
            error: function (xhr, textStatus, errorThrown) 
            {
            	loadingIndicator.fadeOut();
            	console.log(xhr.responseText);
            	alert('Failed to insert the new bank holiday because of a server error.');                
            }
		});
	}	
		
	ajaxGetListOfBankHolidays();
	
	bankHolidaysGrid.onSort.subscribe(function(e, args)
	{
	    sortColumn = args.sortCol.field;
	    
		function lexographicComparer(a, b)
		{
			var x = a[sortColumn], y = b[sortColumn];
			return (x === y ? 0 : (x > y ? 1 : -1));
		}
		
		dataView.sort(lexographicComparer, args.sortAsc);
	});
	
	dataView.onRowCountChanged.subscribe(function (e, args)
	{
		bankHolidaysGrid.updateRowCount();
		bankHolidaysGrid.render();
	});

	dataView.onRowsChanged.subscribe(function (e, args) 
	{
		bankHolidaysGrid.invalidateRows(args.rows);
		bankHolidaysGrid.render();
	});
				
	dataView.refresh();
	bankHolidaysGrid.render();
	
	bankHolidaysGrid.onContextMenu.subscribe(function (e) 
	{
        var cell = bankHolidaysGrid.getCellFromEvent(e);
        var item = dataView.getItem(cell.row);
        e.preventDefault();
    	
    	$("#bankHolidayContextMenu li").each(function(index)
		{
    		if((item["Validity"] === "Valid") && ($(this).attr("data") === "VALIDATE") ||
    		(item["Validity"] === "Invalid") && ($(this).attr("data") === "INVALIDATE"))
        		$(this).hide();
    		else
    			$(this).show();
		});        	
    	
        $("#bankHolidayContextMenu")
            .data("row", cell.row)
            .css("top", e.pageY)
            .css("left", e.pageX)
            .show();
    
        $("body").one("click", function() 
        {
        	$("#bankHolidayContextMenu").hide();
        });
    });
		
    $("#bankHolidayContextMenu").click(function (e) 
    {
    	if (!$(e.target).is("li")) 
    		return;
      
    	if (!bankHolidaysGrid.getEditorLock().commitCurrentEdit())
    		return;
      
    	var row = $(this).data("row");
    	var operation = $(e.target).attr("data");
    	var lastUpdatedBy = "ladeoye";
    	
		showLoadIndicator();
		ajaxUpdateBankHoliday(dataView.getItem(row).identifier, dataView.getItem(row).location, 
				dataView.getItem(row).bankHolidayDate, operation === "VALIDATE", lastUpdatedBy);	
    });	    	
});