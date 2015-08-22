function validityFormatter(row, cell, value, columnDef, dataContext)
{
	return value ? "Valid" : "Invalid";
}

var columns = 
[
 	{id: "clientId", name: "Client ID", field: "clientId", sortable: true, toolTip: "Client's unique identifier"},
	{id: "name", name: "Name", field: "name", sortable: true, toolTip: "Client's name", width: 125},
	{id: "tier", name: "Tier", field: "tier", sortable: true, toolTip: "Client's tier"},
	{id: "isValid", name: "Validity", field: "isValid", sortable: true, toolTip: "Client's validity status", formatter: validityFormatter},
	{id: "lastUpdatedBy", name: "Last Updated By", field: "lastUpdatedBy", sortable: true, toolTip: "User to last update the client", width: 90}
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
	$("#new-client-add").removeAttr('disabled');
}

function disableAddButton()
{
	$("#new-client-add").attr('disabled', 'disabled');
}

function toggleAddButtonState()
{
	if(($("#new-client-name").val() != "") && ($("#new-client-name").val() != $("#new-client-name").attr("default_value")))
		enableAddButton();
	else
		disableAddButton();
}

function clearNewclientInputFields()
{
	$("#new-client-name").val($("#new-client-name").attr("default_value"));	
}

$(document).ready(function()
{
	var clientUpdateAjaxLock = false;
	var clientUpdateIntervalTime = 1000;
	var clientUpdateTimeout = 5000;
	
	var loadingIndicator = null;
	var groupItemMetadataProvider = new Slick.Data.GroupItemMetadataProvider();
	
	var dataView = new Slick.Data.DataView(
	{
	    groupItemMetadataProvider: groupItemMetadataProvider,
	    inlineFilters: true
	});
	
	var clientsGrid = new Slick.Grid("#clientsGrid", dataView, columns, options);
	
	clientsGrid.registerPlugin(groupItemMetadataProvider);	
	clientsGrid.setSelectionModel(new Slick.RowSelectionModel());
	clientsGrid.setTopPanelVisibility(false);
	
	$(".new-client-btn").button();
	disableAddButton();	
	$("#new-client-name").keyup(toggleAddButtonState);
	$("#new-client-name").focusout(toggleAddButtonState);
	
	$("#new-client-name").click(function()
	{
		if($(this).val() === $(this).attr("default_value"))
		{
			$(this).val("");
			disableAddButton();
		}
	});

	$("input.new-client-input-class").focusout(function()
	{
		if(trimSpaces($(this).val()) === "")
			$(this).val($(this).attr("default_value"));
	});	

	$("#new-client-clear").click(function()
	{
		clearNewclientInputFields();		
		disableAddButton();
	});
	
	$("#new-client-add").click(function()
	{
		var name = $("#new-client-name").val();
		var updatedByUser = "ladeoye";
		var tier = $("#new-client-tier").val();
		ajaxAddNewClient(name, tier, updatedByUser);				
		disableAddButton();
		clearNewclientInputFields();		
	});
	
	function flashNewClientRow(newClientId)
	{
		var columns = clientsGrid.getColumns();
		var size = columns.length;
		
		for(var index = 0; index < size; index++)
			clientsGrid.flashCell(dataView.getRowById(newClientId), clientsGrid.getColumnIndex(columns[index].id), 100);
	}
	
    function showLoadIndicator() 
    {
        if (!loadingIndicator) 
        {
        	loadingIndicator = $(".loading-indicator").appendTo(document.body);
        	var $g = $("#clientsGrid");
        	loadingIndicator
            	.css("position", "absolute")
            	.css("top", $g.position().top + $g.height() / 2 - loadingIndicator.height() / 2)
            	.css("left", $g.position().left + $g.width() / 2 - loadingIndicator.width() / 2);
        }
        loadingIndicator.show();
    }	
	
	function ajaxGetListOfclients() 
	{
		showLoadIndicator();
		
		$.ajax({
		    url: contextPath + "/clients/ajaxGetListOfAllClients", 
		    type: 'GET', 
		    dataType: 'json',  
		    contentType: 'application/json',
		    mimeType: 'application/json',
		    timeout: clientUpdateTimeout,
		    cache: false,
		    success: function(clients) 
		    {
		    	dataView.setItems(clients, "clientId");
		    	loadingIndicator.fadeOut();
		    },
            error: function (xhr, textStatus, errorThrown) 
            {
            	loadingIndicator.fadeOut();
            	console.log(xhr.responseText);
            	alert('Failed to retrieve list of clients because of a server error.');                
            }
		});
	}
	
	function ajaxUpdateClient(clientId, name, tier, validity, lastUpdatedBy) 
	{
		var clientDetails = { "clientId" : clientId, "name": name, "tier" : tier, 
			"isValid" : validity, "lastUpdatedBy" : lastUpdatedBy };
		
		$.ajax({
		    url: contextPath + "/clients/ajaxUpdateClient", 
		    type: 'POST', 
		    dataType: 'json',
		    data: JSON.stringify(clientDetails),
		    contentType: 'application/json',
		    mimeType: 'application/json',
		    timeout: clientUpdateTimeout,
		    cache: false,
		    success: function(result) 
		    {
		    	loadingIndicator.fadeOut();
	    		var item = dataView.getItemById(clientId);
	    		item["isValid"] = validity;
	    		dataView.updateItem(clientId, item);		    				    	
		    	clientsGrid.flashCell(dataView.getRowById(clientId), clientsGrid.getColumnIndex("isValid"), 100);
		    	
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
	function ajaxGetAnyNewClients()
	{
		$.ajax({
		    url: contextPath + "/clients/ajaxGetAnyNewClients", 
		    type: 'GET', 
		    dataType: 'json',
		    contentType: 'application/json',
		    mimeType: 'application/json',
		    timeout: clientUpdateTimeout,
		    cache: false,
		    success: function(newClients) 
		    {
		    	loadingIndicator.fadeOut();		    	
		    	
				if(newClients)
				{
				 	var length = newClients.length;				 	
					for(var index = 0; index < length; index++)
					{
						dataView.insertItem(0, {"clientId": newClients[index].clientId, "name": newClients[index].name , "tier": newClients[index].tier, 
							"isValid" : newClients[index].isValid, "lastUpdatedBy" : newClients[index].lastUpdatedBy });
					
						flashClientRow(newClients[index].clientId);
					}
				}
		    },
            error: function (xhr, textStatus, errorThrown) 
            {
            	loadingIndicator.fadeOut();
            	console.log(xhr.responseText);
            	alert('Failed to get an update of any new clients because of a server error.');                
            }
		});		
	}
	
	function ajaxAddNewClient(name, tier, lastUpdatedBy) 
	{
		var newClient = { "name" : name, "tier": tier, "isValid" : true, "lastUpdatedBy" : lastUpdatedBy };
		
		$.ajax({
		    url: contextPath + "/clients/ajaxAddNewClient", 
		    type: 'POST', 
		    dataType: 'json',
		    data: JSON.stringify(newClient),
		    contentType: 'application/json',
		    mimeType: 'application/json',
		    timeout: clientUpdateTimeout,
		    cache: false,
		    success: function(newClientId) 
		    {
		    	loadingIndicator.fadeOut();
		    	
				if(newClientId)
				{
					dataView.insertItem(0, {"clientId" : newClientId, "name" : name , "tier" : tier, 
						"isValid" : true, "lastUpdatedBy" : lastUpdatedBy });
					
					flashNewClientRow(newClientId);
				}
				else
					alert("Failed to insert the new client because of a server error.");
		    },
            error: function (xhr, textStatus, errorThrown) 
            {
            	loadingIndicator.fadeOut();
            	console.log(xhr.responseText);
            	alert('Failed to insert the new client because of a server error.');                
            }
		});
	}	
		
	ajaxGetListOfclients();
	
	clientsGrid.onSort.subscribe(function(e, args)
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
		clientsGrid.updateRowCount();
		clientsGrid.render();
	});

	dataView.onRowsChanged.subscribe(function (e, args) 
	{
		clientsGrid.invalidateRows(args.rows);
		clientsGrid.render();
	});
				
	dataView.refresh();
	clientsGrid.render();
	
	clientsGrid.onContextMenu.subscribe(function (e) 
	{
        var cell = clientsGrid.getCellFromEvent(e);
        var item = dataView.getItem(cell.row);
        e.preventDefault();
    	
    	$("#clientContextMenu li").each(function(index)
		{
    		if((item["Validity"] === "Valid") && ($(this).attr("data") === "VALIDATE") ||
    				(item["Validity"] === "Invalid") && ($(this).attr("data") === "INVALIDATE"))
        		$(this).hide();
    		else
    			$(this).show();
		});        	
    	
        $("#clientContextMenu")
            .data("row", cell.row)
            .css("top", e.pageY)
            .css("left", e.pageX)
            .show();
    
        $("body").one("click", function() 
        {
          $("#clientContextMenu").hide();
        });
    });
		
    $("#clientContextMenu").click(function (e) 
    {
    	if (!$(e.target).is("li")) 
    		return;
      
    	if (!clientsGrid.getEditorLock().commitCurrentEdit())
    		return;
      
    	var row = $(this).data("row");
    	var operation = $(e.target).attr("data");
    	var lastUpdatedBy = "ladeoye";
    	
		showLoadIndicator();
		ajaxUpdateClient(dataView.getItem(row).clientId, dataView.getItem(row).name, 
				dataView.getItem(row).tier, operation === "VALIDATE", lastUpdatedBy);	
    });	    	
});
