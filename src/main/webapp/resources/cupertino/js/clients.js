function validityFormatter(row, cell, value, columnDef, dataContext)
{
	return value ? "Valid" : "Invalid";
}

var columns = 
[
 	{id: "clientId", name: "client ID", field: "clientId", sortable: true, toolTip: "Client's unique identifier"},
	{id: "name", name: "name", field: "name", sortable: true, toolTip: "Client's name", width: 125},
	{id: "tier", name: "tier", field: "tier", sortable: true, toolTip: "Client's tier"},
	{id: "isValid", name: "Validity", field: "isValid", sortable: true, toolTip: "Client's validity status", formatter: validityFormatter},
	{id: "lastUpdatedBy", name: "Last Updated By", field: "lastUpdatedBy", sortable: true, toolTip: "Client was last updated by user", width: 90}
];

var options = 
{
	enableCellNavigation: true,
	enableColumnReorder: true,		
    editable: true,
    enableAddRow: false,
    asyncEditorLoading: true,
    autoEdit: false,
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
		ajaxAddNewclient(name, updatedByUser);				
		disableAddButton();
		clearNewclientInputFields();		
	});		
	
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
		    url: contextPath + "/clients/ajaxGetListOfClients", 
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
	
	function ajaxUpdateclientValidity(clientId, validity, updatedByUser) 
	{
		var clientDetails = { "clientId" : clientId, "isValid" : validity, "updatedByUser" : updatedByUser };
		
		$.ajax({
		    url: contextPath + "/clients/ajaxUpdateValidity", 
		    type: 'GET', 
		    dataType: 'json',
		    data: JSON.stringify(clientDetails),
		    contentType: 'application/json',
		    mimeType: 'application/json',
		    timeout: clientUpdateTimeout,
		    cache: false,
		    success: function(result) 
		    {
		    	loadingIndicator.fadeOut();
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
	
	function ajaxAddNewclient(name, updatedByUser) 
	{
		var clientDetails = { "name" : name, "updatedByUser" : updatedByUser };
		
		$.ajax({
		    url: contextPath + "/clients/ajaxAddNewClient", 
		    type: 'GET', 
		    dataType: 'json',
		    data: JSON.stringify(clientDetails),
		    contentType: 'application/json',
		    mimeType: 'application/json',
		    timeout: clientUpdateTimeout,
		    cache: false,
		    success: function(newClientId) 
		    {
		    	loadingIndicator.fadeOut();
		    	
				if(newClientId)
				{
					dataView.insertItem(0, {"clientId": newClientId, "name": name , "tier": tier, 
						"isValid" : true, "updatedByUser" : updatedByUser});		
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
    	var updatedByUser = "ladeoye";
    	
    	switch (operation) 
    	{
        	case "VALIDATE":        		
        		showLoadIndicator();
        		ajaxUpdateclientValidity(dataView.getItem(row).clientId, true, updatedByUser);	
        		break;
        	case "INVALIDATE":
        		showLoadIndicator();
        		ajaxUpdateclientValidity(dataView.getItem(row).clientId, false, updatedByUser);        		
        		break;
        	default: 
        		alert("Sorry, this operation is yet to be supported!");
    	}    	
    });	    	
});