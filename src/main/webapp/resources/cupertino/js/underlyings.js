function validityFormatter(row, cell, value, columnDef, dataContext)
{
	return value ? "Valid" : "Invalid";
}

var columns = 
[
 	{id: "ric", name: "Underlying RIC", field: "ric", sortable: true, toolTip: "Underlying's unique identifier, the RIC", width : 100},
	{id: "description", name: "Description", field: "description", sortable: true, toolTip: "Underlying's description", width : 200, editor: Slick.Editors.LongText},
	{id: "spread", name: "Spread", field: "spread", sortable: true, toolTip: "Underlying's spread", editor: Slick.Editors.Text},
	{id: "referencePrice", name: "Reference price", field: "referencePrice", sortable: true, toolTip: "Underlying's reference price", width: 125, editor: Slick.Editors.Text},
	{id: "simulationPriceVariance", name: "Simulation price variance", field: "simulationPriceVariance", sortable: true, toolTip: "Underlying's simulation price variance", width: 150, editor: Slick.Editors.Text},
	{id: "dividendYield", name: "Dividend yield", field: "dividendYield", sortable: true, toolTip: "Underlying's dividend yield", editor: Slick.Editors.Text},
	{id: "isValid", name: "Validity", field: "isValid", sortable: true, toolTip: "Underlying's validity status", formatter: validityFormatter},
	{id: "lastUpdatedBy", name: "Last updated by", field: "lastUpdatedBy", sortable: true, toolTip: "User to last update the underlying", width: 90}
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
	$("#new-underlying-add").removeAttr('disabled');
}

function disableAddButton()
{
	$("#new-underlying-add").attr('disabled', 'disabled');
}

function toggleAddButtonState()
{
	if((($("#new-underlying-description").val() != "") 
			&& ($("#new-underlying-description").val() != $("#new-underlying-description").attr("default_value")))
			&& (($("#new-underlying-ric").val() != "") 
					&& ($("#new-underlying-ric").val() != $("#new-underlying-ric").attr("default_value"))))
		enableAddButton();
	else
		disableAddButton();
}

function clearNewunderlyingInputFields()
{
	$("#new-underlying-description").val($("#new-underlying-description").attr("default_value"));
	$("#new-underlying-ric").val($("#new-underlying-ric").attr("default_value"));
	$("#new-underlying-spread").val($("#new-underlying-spread").attr("default_value"));
	$("#new-underlying-referencePrice").val($("#new-underlying-referencePrice").attr("default_value"));
	$("#new-underlying-simulationPriceVariance").val($("#new-underlying-simulationPriceVariance").attr("default_value"));
	$("#new-underlying-dividendYield").val($("#new-underlying-dividendYield").attr("default_value"));
}

$(document).ready(function()
{
	var underlyingUpdateAjaxLock = false;
	var underlyingUpdateIntervalTime = 1000;
	var underlyingUpdateTimeout = 5000;
	
	var loadingIndicator = null;
	var groupItemMetadataProvider = new Slick.Data.GroupItemMetadataProvider();
	
	var dataView = new Slick.Data.DataView(
	{
	    groupItemMetadataProvider: groupItemMetadataProvider,
	    inlineFilters: true
	});
	
	var underlyingsGrid = new Slick.Grid("#underlyingsGrid", dataView, columns, options);
	
	underlyingsGrid.registerPlugin(groupItemMetadataProvider);	
	underlyingsGrid.setSelectionModel(new Slick.RowSelectionModel());
	underlyingsGrid.setTopPanelVisibility(false);
	
	$(".new-underlying-btn").button();
	disableAddButton();
	$("#new-underlying-ric").keyup(toggleAddButtonState);
	$("#new-underlying-ric").focusout(toggleAddButtonState);	
	$("#new-underlying-description").keyup(toggleAddButtonState);
	$("#new-underlying-description").focusout(toggleAddButtonState);
	$("#new-underlying-spread").keyup(toggleAddButtonState);
	$("#new-underlying-spread").focusout(toggleAddButtonState);
	$("#new-underlying-referencePrice").keyup(toggleAddButtonState);
	$("#new-underlying-referencePrice").focusout(toggleAddButtonState);
	$("#new-underlying-simulationPriceVariance").keyup(toggleAddButtonState);
	$("#new-underlying-simulationPriceVariance").focusout(toggleAddButtonState);
	$("#new-underlying-dividendYield").keyup(toggleAddButtonState);
	$("#new-underlying-dividendYield").focusout(toggleAddButtonState);	
	
	$("input.new-underlying-input-class").click(function()
	{
		if($(this).val() === $(this).attr("default_value"))
		{
			$(this).val("");
			disableAddButton();
		}
	});

	$("input.new-underlying-input-class").focusout(function()
	{
		if(trimSpaces($(this).val()) === "")
			$(this).val($(this).attr("default_value"));
	});	

	$("#new-underlying-clear").click(function()
	{
		clearNewunderlyingInputFields();		
		disableAddButton();
	});
	
	$("#new-underlying-add").click(function()
	{
		var ric = $("#new-underlying-ric").val();
		var description = $("#new-underlying-description").val();
		var spread = $("#new-underlying-spread").val();
		var referencePrice = $("#new-underlying-referencePrice").val();
		var simulationPriceVariance = $("#new-underlying-simulationPriceVariance").val();
		var dividendYield = $("#new-underlying-dividendYield").val();
		var lastUpdatedBy = "ladeoye";
		
		ajaxAddNewUnderlying(ric, description, spread, referencePrice, simulationPriceVariance, dividendYield, lastUpdatedBy);				
		disableAddButton();
		clearNewunderlyingInputFields();		
	});
	
	function flashRow(ric)
	{
		var columns = underlyingsGrid.getColumns();
		var size = columns.length;
		
		for(var index = 0; index < size; index++)
			underlyingsGrid.flashCell(dataView.getRowById(ric), underlyingsGrid.getColumnIndex(columns[index].id), 100);
	}
	
    function showLoadIndicator() 
    {
        if (!loadingIndicator) 
        {
        	loadingIndicator = $(".loading-indicator").appendTo(document.body);
        	var $g = $("#underlyingsGrid");
        	loadingIndicator
            	.css("position", "absolute")
            	.css("top", $g.position().top + $g.height() / 2 - loadingIndicator.height() / 2)
            	.css("left", $g.position().left + $g.width() / 2 - loadingIndicator.width() / 2);
        }
        loadingIndicator.show();
    }	
	
	function ajaxGetListOfUnderlyings() 
	{
		showLoadIndicator();
		
		$.ajax({
		    url: contextPath + "/underlyings/ajaxGetListOfAllUnderlyings", 
		    type: 'GET', 
		    dataType: 'json',  
		    contentType: 'application/json',
		    mimeType: 'application/json',
		    timeout: underlyingUpdateTimeout,
		    cache: false,
		    success: function(underlyings) 
		    {
		    	underlyingsGrid.invalidateAllRows();
		    	dataView.setItems(underlyings, "ric");
		    	underlyingsGrid.render();
		    	loadingIndicator.fadeOut();
		    },
            error: function (xhr, textStatus, errorThrown) 
            {
            	loadingIndicator.fadeOut();
            	console.log(xhr.responseText);
            	alert('Failed to retrieve list of underlyings because of a server error.');                
            }
		});
	}
	
	var modifiedCells = {};
	
    underlyingsGrid.onCellChange.subscribe(function (e, args) 
    {
        if (!modifiedCells[args.row]) 
            modifiedCells[args.row] = {};
        
        modifiedCells[args.row][this.getColumns()[args.cell].id] = "slick-cell-modified";
        this.removeCellCssStyles("modified");
        this.setCellCssStyles("modified", modifiedCells);
    });	
	
	function ajaxUpdateUnderlying(ric, description, spread, referencePrice, 
			simulationPriceVariance, dividendYield, isValid, lastUpdatedBy, flashRowFlag) 
	{
		showLoadIndicator();
		
		var underlyingDetails = { 
			"ric" : ric, 
			"description" : description, 
			"spread" : spread, 
			"referencePrice" : referencePrice, 
			"simulationPriceVariance" : simulationPriceVariance, 
			"dividendYield" : dividendYield,	
			"isValid" : isValid, 
			"lastUpdatedBy" : lastUpdatedBy 
		};	    
		
		$.ajax({
		    url: contextPath + "/underlyings/ajaxUpdateUnderlying", 
		    type: 'POST', 
		    dataType: 'json',
		    data: JSON.stringify(underlyingDetails),
		    contentType: 'application/json',
		    mimeType: 'application/json',
		    timeout: underlyingUpdateTimeout,
		    cache: false,
		    success: function(result) 
		    {
		    	loadingIndicator.fadeOut();
		    	
		    	if(result)
		    	{
		    		var item = dataView.getItemById(ric);
		    		item["isValid"] = isValid;
		    		dataView.updateItem(ric, item);
		    		
		    		if(flashRowFlag)
		    			flashRow(ric);
			    	
		    		underlyingsGrid.removeCellCssStyles("modified");
			    	delete modifiedCells[dataView.getRowById(ric)];
			    	underlyingsGrid.setCellCssStyles("modified", modifiedCells);
		    	}
		    	else
		    		alert('Failed to update the underlying details because of a server error.');		    	
		    },
            error: function (xhr, textStatus, errorThrown) 
            {
            	loadingIndicator.fadeOut();            	
            	console.log(xhr.responseText);
            	alert('Failed to update the underlying details because of a server error.');                
            }
		});
	}
	
	// TODO
	function ajaxGetAnyNewUnderlyings()
	{
		showLoadIndicator();
		
		$.ajax({
		    url: contextPath + "/underlyings/ajaxGetAnyNewUnderlyings", 
		    type: 'GET', 
		    dataType: 'json',
		    contentType: 'application/json',
		    mimeType: 'application/json',
		    timeout: underlyingUpdateTimeout,
		    cache: false,
		    success: function(newUnderlyings) 
		    {
		    	loadingIndicator.fadeOut();		    	
		    	
				if(newUnderlyings)
				{
				 	var length = newUnderlyings.length;				 	
					for(var index = 0; index < length; index++)
					{
						dataView.insertItem(0, {"ric" : newUnderlyings[index].ric, 
							"description" : newUnderlyings[index].description, 
							"spread" : newUnderlyings[index].spread, 
							"referencePrice" : newUnderlyings[index].referencePrice, 
							"simulationPriceVariance" : newUnderlyings[index].simulationPriceVariance, 
							"dividendYield" : newUnderlyings[index].dividendYield, 
							"isValid" : newUnderlyings[index].isValid, 
							"lastUpdatedBy" : newUnderlyings[index].lastUpdatedBy });
					
						flashRow(newUnderlyings[index].ric);
					}
				}
		    },
            error: function (xhr, textStatus, errorThrown) 
            {
            	loadingIndicator.fadeOut();
            	console.log(xhr.responseText);
            	alert('Failed to get an update of any new underlyings because of a server error.');                
            }
		});		
	}
	
	function ajaxAddNewUnderlying(ric, description, spread, referencePrice, 
			simulationPriceVariance, dividendYield, lastUpdatedBy) 
	{
		showLoadIndicator();
		
		var newUnderlying = { 
			"ric" : ric, 
			"description" : description, 
			"spread" : spread, 
			"referencePrice" : referencePrice, 
			"simulationPriceVariance" : simulationPriceVariance, 
			"dividendYield" : dividendYield,	
			"isValid" : true, 
			"lastUpdatedBy" : lastUpdatedBy 
		};
		
		$.ajax({
		    url: contextPath + "/underlyings/ajaxAddNewUnderlying", 
		    type: 'POST', 
		    dataType: 'json',
		    data: JSON.stringify(newUnderlying),
		    contentType: 'application/json',
		    mimeType: 'application/json',
		    timeout: underlyingUpdateTimeout,
		    cache: false,
		    success: function(result) 
		    {
		    	loadingIndicator.fadeOut();
		    	
				if(result)
				{
					dataView.insertItem(0, {
						"ric" : ric, 
						"description" : description, 
						"spread" : spread, 
						"referencePrice" : referencePrice, 
						"simulationPriceVariance" : simulationPriceVariance, 
						"dividendYield" : dividendYield,	
						"isValid" : true, 
						"lastUpdatedBy" : lastUpdatedBy 
					});
					
					flashRow(ric);
				}
				else
					alert("Failed to insert the new underlying because of a server error.");
		    },
            error: function (xhr, textStatus, errorThrown) 
            {
            	loadingIndicator.fadeOut();
            	console.log(xhr.responseText);
            	alert('Failed to insert the new underlying because of a server error.');                
            }
		});
	}	
		
	ajaxGetListOfUnderlyings();
	
	underlyingsGrid.onSort.subscribe(function(e, args)
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
		underlyingsGrid.updateRowCount();
		underlyingsGrid.render();
	});

	dataView.onRowsChanged.subscribe(function (e, args) 
	{
		underlyingsGrid.invalidateRows(args.rows);
		underlyingsGrid.render();
	});
				
	dataView.refresh();
	underlyingsGrid.render();
	
	underlyingsGrid.onContextMenu.subscribe(function (e) 
	{
        var cell = underlyingsGrid.getCellFromEvent(e);
        var item = dataView.getItem(cell.row);
        e.preventDefault();
    	
    	$("#underlyingContextMenu li").each(function(index)
		{
    		if((item["Validity"] === "Valid") && ($(this).attr("data") === "VALIDATE") ||
    				(item["Validity"] === "Invalid") && ($(this).attr("data") === "INVALIDATE"))
        		$(this).hide();
    		else
    			$(this).show();
		});        	
    	
        $("#underlyingContextMenu")
            .data("row", cell.row)
            .css("top", e.pageY)
            .css("left", e.pageX)
            .show();
    
        $("body").one("click", function() 
        {
          $("#underlyingContextMenu").hide();
        });
    });
		
    $("#underlyingContextMenu").click(function (e) 
    {
    	if (!$(e.target).is("li")) 
    		return;
      
    	if (!underlyingsGrid.getEditorLock().commitCurrentEdit())
    		return;
      
    	var row = $(this).data("row");
    	var operation = $(e.target).attr("data");
    	var lastUpdatedBy = "ladeoye";
		
    	switch (operation) 
    	{
        	case "VALIDATE":
        		ajaxUpdateUnderlying(dataView.getItem(row).ric, dataView.getItem(row).description, 
        			dataView.getItem(row).spread, dataView.getItem(row).referencePrice, 
        			dataView.getItem(row).simulationPriceVariance, dataView.getItem(row).dividendYield,
        			true, lastUpdatedBy, true);	
        		break;
        	case "INVALIDATE":
        		ajaxUpdateUnderlying(dataView.getItem(row).ric, dataView.getItem(row).description, 
        			dataView.getItem(row).spread, dataView.getItem(row).referencePrice, 
        			dataView.getItem(row).simulationPriceVariance, dataView.getItem(row).dividendYield,
        			false, lastUpdatedBy, true);
        		break;
        	case "SAVE":
        		ajaxUpdateUnderlying(dataView.getItem(row).ric, dataView.getItem(row).description, 
        			dataView.getItem(row).spread, dataView.getItem(row).referencePrice, 
        			dataView.getItem(row).simulationPriceVariance, dataView.getItem(row).dividendYield,
        			dataView.getItem(row).isValid, lastUpdatedBy, true);
        		break;
        	case "SAVE_ALL":
        		var answer = confirm("Are you sure you want to save all changes?");
        		if(answer)
    			{
            		for (var key in modifiedCells)
            		{
                		ajaxUpdateUnderlying(dataView.getItem(key).ric, dataView.getItem(key).description, 
                			dataView.getItem(key).spread, dataView.getItem(key).referencePrice, 
                			dataView.getItem(key).simulationPriceVariance, dataView.getItem(key).dividendYield,
                			dataView.getItem(key).isValid, lastUpdatedBy, false);
            		}    			
    			}        		
        		break;
        	case "REFRESH":
        		ajaxGetListOfUnderlyings();
        		break;
        	default: 
        		alert("Sorry, this operation is yet to be supported!");
    	}				
    });	    	
});
