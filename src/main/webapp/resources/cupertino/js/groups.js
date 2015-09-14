function validityFormatter(row, cell, value, columnDef, dataContext)
{
	return value ? "Valid" : "Invalid";
}

var columns = 
[
 	{id: "name", name: "Group Name", field: "name", sortable: true, toolTip: "Unique group name", width: 140},
	{id: "description", name: "Group Description", field: "description", sortable: true, toolTip: "Group's description", editor: Slick.Editors.Text, width: 240},
	{id: "isValid", name: "Validity", field: "isValid", sortable: true, toolTip: "Groups's validity status", formatter: validityFormatter},
	{id: "lastUpdatedBy", name: "Last Updated By", field: "lastUpdatedBy", sortable: true, toolTip: "User to last update the group" , width: 110}
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
	$("#new-group-add").removeAttr('disabled');
}

function disableAddButton()
{
	$("#new-group-add").attr('disabled', 'disabled');
}

function toggleAddButtonState()
{
	if(($("#new-group-name").val() != "" && $("#new-group-name").val() != $("#new-group-name").attr("default_value"))
			&& ($("#new-group-description").val() != "" && $("#new-group-description").val() != $("#new-group-description").attr("default_value")))
		enableAddButton();
	else
		disableAddButton();
}

function clearNewGroupInputFields()
{
	$("#new-group-name").val($("#new-group-name").attr("default_value"));
	$("#new-group-description").val($("#new-group-description").attr("default_value"));	
}

$(document).ready(function()
{
	var groupUpdateAjaxLock = false;
	var groupUpdateIntervalTime = 1000;
	var groupUpdateTimeout = 5000;
	
	var loadingIndicator = null;
	var groupItemMetadataProvider = new Slick.Data.GroupItemMetadataProvider();
	
	var dataView = new Slick.Data.DataView(
	{
	    groupItemMetadataProvider: groupItemMetadataProvider,
	    inlineFilters: true
	});
	
	var groupsGrid = new Slick.Grid("#groupsGrid", dataView, columns, options);
	
	groupsGrid.registerPlugin(groupItemMetadataProvider);	
	groupsGrid.setSelectionModel(new Slick.RowSelectionModel());
	groupsGrid.setTopPanelVisibility(false);
	
	$(".new-group-btn").button();
	disableAddButton();	
	$("#new-group-name").keyup(toggleAddButtonState);
	$("#new-group-name").focusout(toggleAddButtonState);
	$("#new-group-description").keyup(toggleAddButtonState);
	$("#new-group-description").focusout(toggleAddButtonState);
	
	$("input.new-group-input-class").click(function()
	{
		if($(this).val() === $(this).attr("default_value"))
		{
			$(this).val("");
			
			if($(this).is("#new-group-name"))
				disableAddButton();
		}
	});

	$("input.new-group-input-class").focusout(function()
	{
		if(trimSpaces($(this).val()) === "")
			$(this).val($(this).attr("default_value"));
	});	

	$("#new-group-clear").click(function()
	{
		clearNewGroupInputFields();		
		disableAddButton();
	});
	
	$("#new-group-add").click(function()
	{
		var name = $("#new-group-name").val();
		var description = $("#new-group-description").val();
		var updatedByUser = "ladeoye";
		
		ajaxAddNewGroup(name.toUpperCase(), description, updatedByUser);
		disableAddButton();
		clearNewGroupInputFields();		
	});		
	
    function showLoadIndicator() 
    {
        if (!loadingIndicator) 
        {
        	loadingIndicator = $(".loading-indicator").appendTo(document.body);
        	var $g = $("#groupsGrid");
        	loadingIndicator
            	.css("position", "absolute")
            	.css("top", $g.position().top + $g.height() / 2 - loadingIndicator.height() / 2)
            	.css("left", $g.position().left + $g.width() / 2 - loadingIndicator.width() / 2);
        }
        loadingIndicator.show();
    }	
	
	function ajaxGetListOfGroups() 
	{
		showLoadIndicator();
		
		$.ajax({
		    url: contextPath + "/groups/ajaxGetListOfAllGroups", 
		    type: 'GET', 
		    dataType: 'json',  
		    contentType: 'application/json',
		    mimeType: 'application/json',
		    timeout: groupUpdateTimeout,
		    cache: false,
		    success: function(groups) 
		    {
		    	dataView.setItems(groups, "name");
		    	loadingIndicator.fadeOut();
		    },
            error: function (xhr, textStatus, errorThrown) 
            {
            	loadingIndicator.fadeOut();
            	console.log(xhr.responseText);
            	alert('Failed to retrieve list of groups because of a server error.');                
            }
		});
	}
	
	function ajaxUpdateGroup(name, description, isValid, lastUpdatedBy) 
	{
		var groupToUpdate = { 
			"name" : name,
			"description" : description,
			"isValid" : isValid, 
			"lastUpdatedBy" : lastUpdatedBy 
		};
		
		$.ajax({
		    url: contextPath + "/groups/ajaxUpdateGroup", 
		    type: 'POST', 
		    dataType: 'json',
		    data: JSON.stringify(groupToUpdate),
		    contentType: 'application/json',
		    mimeType: 'application/json',
		    timeout: groupUpdateTimeout,
		    cache: false,
		    success: function(result) 
		    {
		    	loadingIndicator.fadeOut();
		    	
		    	if(result)
		    	{
		    		var item = dataView.getItemById(name);
		    		item["isValid"] = isValid;
		    		dataView.updateItem(name, item);		    		
		    		flashRow(name);
					
		    		groupsGrid.removeCellCssStyles("modified");
			    	delete modifiedCells[dataView.getRowById(name)];				
		    	}
				else
		    		alert("Failed to update the group details.");		    	
		    },
            error: function (xhr, textStatus, errorThrown) 
            {
            	loadingIndicator.fadeOut();
            	console.log(xhr.responseText);
            	alert('Failed to update the group details because of a server error.');                
            }
		});
	}
	
	function flashRow(name)
	{
		var columns = groupsGrid.getColumns();
		var size = columns.length;
		
		for(var index = 0; index < size; index++)
			groupsGrid.flashCell(dataView.getRowById(name), groupsGrid.getColumnIndex(columns[index].id), 100);
	}	
	
	function ajaxAddNewGroup(name, description, lastUpdatedBy) 
	{
		var newGroup = { 
			"name" : name, 
			"description" : description,
			"isValid" : true,
			"lastUpdatedBy" : lastUpdatedBy 
		};
		
		$.ajax({
		    url: contextPath + "/groups/ajaxAddNewGroup", 
		    type: 'POST', 
		    dataType: 'json',
		    data: JSON.stringify(newGroup),
		    contentType: 'application/json',
		    mimeType: 'application/json',
		    timeout: groupUpdateTimeout,
		    cache: false,
		    success: function(result) 
		    {
		    	loadingIndicator.fadeOut();
				if(result)
				{
					dataView.insertItem(0, { 
						"name" : name, 
						"description" : description, 
						"isValid" : true, 
						"lastUpdatedBy" : lastUpdatedBy 
					});
					
					flashRow(name);
					disableAddButton();
					clearNewGroupInputFields();					
				}
				else
		    		alert("Failed to insert the new group because of a server error.");		    	
		    },
            error: function (xhr, textStatus, errorThrown) 
            {
            	loadingIndicator.fadeOut();
            	console.log(xhr.responseText);
            	alert('Failed to insert the new group because of a server error.');                
            }
		});
	}	
		
	ajaxGetListOfGroups();
	
	groupsGrid.onSort.subscribe(function(e, args)
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
		groupsGrid.updateRowCount();
		groupsGrid.render();
	});

	dataView.onRowsChanged.subscribe(function (e, args) 
	{
		groupsGrid.invalidateRows(args.rows);
		groupsGrid.render();
	});
	
	var modifiedCells = {};
	
    groupsGrid.onCellChange.subscribe(function (e, args) 
    {
        if (!modifiedCells[args.row]) 
            modifiedCells[args.row] = {};
        
        modifiedCells[args.row][this.getColumns()[args.cell].id] = "slick-cell-modified";
        this.removeCellCssStyles("modified");
        this.setCellCssStyles("modified", modifiedCells);
    });	
				
	dataView.refresh();
	groupsGrid.render();
	
	groupsGrid.onContextMenu.subscribe(function (e) 
	{
        var cell = groupsGrid.getCellFromEvent(e);
        var item = dataView.getItem(cell.row);
        e.preventDefault();
    	
    	$("#groupContextMenu li").each(function(index)
		{
    		if((item["Validity"] === "Valid") && ($(this).attr("data") === "VALIDATE") ||
    			(item["Validity"] === "Invalid") && ($(this).attr("data") === "INVALIDATE"))
        		$(this).hide();
    		else
    			$(this).show();
		});        	
    	
        $("#groupContextMenu")
            .data("row", cell.row)
            .css("top", e.pageY)
            .css("left", e.pageX)
            .show();
    
        $("body").one("click", function() 
        {
          $("#groupContextMenu").hide();
        });
    });
		
    $("#groupContextMenu").click(function (e) 
    {
    	if (!$(e.target).is("li")) 
    		return;
      
    	if (!groupsGrid.getEditorLock().commitCurrentEdit())
    		return;
      
    	var row = $(this).data("row");
    	var operation = $(e.target).attr("data");
    	var updatedByUser = "ladeoye";
    	
    	
    	switch (operation) 
    	{
        	case "VALIDATE":
        	case "INVALIDATE":
        		showLoadIndicator();
        		ajaxUpdateGroup(dataView.getItem(row).name, dataView.getItem(row).description, 
        				operation === "VALIDATE", updatedByUser);
        		break;
        	case "SAVE":
        		showLoadIndicator();
        		ajaxUpdateGroup(dataView.getItem(row).name, dataView.getItem(row).description, 
        				dataView.getItem(row).isValid, updatedByUser);
        		break;
        	case "SAVE_ALL":
        		var answer = confirm("Are you sure you want to save all changes?");
        		if(answer)
    			{
        			showLoadIndicator();
            		for (var key in modifiedCells)
            		{
                		ajaxUpdateGroup(dataView.getItem(row).name, dataView.getItem(row).description, 
                				dataView.getItem(row).isValid, updatedByUser);
            		}    			
    			}        		
        		break;
        	case "REFRESH":
        		showLoadIndicator();
        		ajaxGetListOfGroups();
        		break;
        	default: 
        		alert("Sorry, this operation is yet to be supported!");
    	}					
    });	    	
});
