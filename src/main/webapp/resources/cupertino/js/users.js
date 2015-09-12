function validityFormatter(row, cell, value, columnDef, dataContext)
{
	return value ? "Valid" : "Invalid";
}

var columns = 
[
 	{id: "userId", name: "User ID", field: "userId", sortable: true, toolTip: "User's unique identifier", width : 100},
	{id: "firstName", name: "First name", field: "firstName", sortable: true, toolTip: "User's first name", width : 110, editor: Slick.Editors.Text},
	{id: "lastName", name: "Last name", field: "lastName", sortable: true, toolTip: "User's last name", width : 110, editor: Slick.Editors.Text},
	{id: "emailAddress", name: "Email address", field: "emailAddress", sortable: true, toolTip: "User's email address", width: 170, editor: Slick.Editors.Text},
	{id: "isValid", name: "Validity", field: "isValid", sortable: true, toolTip: "User's validity status", formatter: validityFormatter},
	{id: "lastUpdatedBy", name: "Last updated by", field: "lastUpdatedBy", sortable: true, toolTip: "User to last update this user's details"}
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
	$("#new-user-add").removeAttr('disabled');
}

function disableAddButton()
{
	$("#new-user-add").attr('disabled', 'disabled');
}

function toggleAddButtonState()
{
	if((($("#new-user-firstName").val() != "") 
			&& ($("#new-user-firstName").val() != $("#new-user-firstName").attr("default_value")))
			&& (($("#new-user-userId").val() != "") 
					&& ($("#new-user-userId").val() != $("#new-user-userId").attr("default_value"))))
		enableAddButton();
	else
		disableAddButton();
}

function clearNewUserInputFields()
{
	$("#new-user-firstName").val($("#new-user-firstName").attr("default_value"));
	$("#new-user-userId").val($("#new-user-userId").attr("default_value"));
	$("#new-user-lastName").val($("#new-user-lastName").attr("default_value"));
	$("#new-user-emailAddress").val($("#new-user-emailAddress").attr("default_value"));
}

$(document).ready(function()
{
	var userUpdateAjaxLock = false;
	var userUpdateIntervalTime = 1000;
	var userUpdateTimeout = 5000;
	
	var loadingIndicator = null;
	var groupItemMetadataProvider = new Slick.Data.GroupItemMetadataProvider();
	
	var dataView = new Slick.Data.DataView(
	{
	    groupItemMetadataProvider: groupItemMetadataProvider,
	    inlineFilters: true
	});
	
	var usersGrid = new Slick.Grid("#usersGrid", dataView, columns, options);
	
	usersGrid.registerPlugin(groupItemMetadataProvider);	
	usersGrid.setSelectionModel(new Slick.RowSelectionModel());
	usersGrid.setTopPanelVisibility(false);
	
	$('#new-user-import-btn').inputFileText({
	    text : "Import",
	    buttonClass : "import-btn-class"
	});		
	
	$(".new-user-btn").button();
	$(".import-btn-class").button();
	
	disableAddButton();
	$("#new-user-userId").keyup(toggleAddButtonState);
	$("#new-user-userId").focusout(toggleAddButtonState);	
	$("#new-user-firstName").keyup(toggleAddButtonState);
	$("#new-user-firstName").focusout(toggleAddButtonState);
	$("#new-user-lastName").keyup(toggleAddButtonState);
	$("#new-user-lastName").focusout(toggleAddButtonState);
	$("#new-user-emailAddress").keyup(toggleAddButtonState);
	$("#new-user-emailAddress").focusout(toggleAddButtonState);
	
	$("input.new-user-input-class").click(function()
	{
		if($(this).val() === $(this).attr("default_value"))
		{
			$(this).val("");
			disableAddButton();
		}
	});

	$("input.new-user-input-class").focusout(function()
	{
		if(trimSpaces($(this).val()) === "")
			$(this).val($(this).attr("default_value"));
	});	

	$("#new-user-clear").click(function()
	{
		clearNewUserInputFields();		
		disableAddButton();
	});
	
	$("#new-user-add").click(function()
	{
		var userId = $("#new-user-userId").val();
		var firstName = $("#new-user-firstName").val();
		var lastName = $("#new-user-lastName").val();
		var emailAddress = $("#new-user-emailAddress").val();
		var lastUpdatedBy = "ladeoye";
		
		ajaxAddNewUser(userId, firstName, lastName, emailAddress, lastUpdatedBy);				
	});
	
	function flashRow(userId)
	{
		var columns = usersGrid.getColumns();
		var size = columns.length;
		
		for(var index = 0; index < size; index++)
			usersGrid.flashCell(dataView.getRowById(userId), usersGrid.getColumnIndex(columns[index].id), 100);
	}
	
    function showLoadIndicator() 
    {
        if (!loadingIndicator) 
        {
        	loadingIndicator = $(".loading-indicator").appendTo(document.body);
        	var $g = $("#usersGrid");
        	loadingIndicator
            	.css("position", "absolute")
            	.css("top", $g.position().top + $g.height() / 2 - loadingIndicator.height() / 2)
            	.css("left", $g.position().left + $g.width() / 2 - loadingIndicator.width() / 2);
        }
        loadingIndicator.show();
    }	
	
	function ajaxGetListOfUsers() 
	{
		showLoadIndicator();
		
		$.ajax({
		    url: contextPath + "/users/ajaxGetListOfAllUsers", 
		    type: 'GET', 
		    dataType: 'json',  
		    contentType: 'application/json',
		    mimeType: 'application/json',
		    timeout: userUpdateTimeout,
		    cache: false,
		    success: function(users) 
		    {
		    	usersGrid.invalidateAllRows();
		    	dataView.setItems(users, "userId");
		    	usersGrid.render();
		    	loadingIndicator.fadeOut();
		    },
            error: function (xhr, textStatus, errorThrown) 
            {
            	loadingIndicator.fadeOut();
            	console.log(xhr.responseText);
            	alert('Failed to retrieve list of users because of a server error.');                
            }
		});
	}
	
	var modifiedCells = {};
	
    usersGrid.onCellChange.subscribe(function (e, args) 
    {
        if (!modifiedCells[args.row]) 
            modifiedCells[args.row] = {};
        
        modifiedCells[args.row][this.getColumns()[args.cell].id] = "slick-cell-modified";
        this.removeCellCssStyles("modified");
        this.setCellCssStyles("modified", modifiedCells);
    });	
	
	function ajaxUpdateUser(userId, firstName, lastName, emailAddress, isValid, lastUpdatedBy, flashRowFlag) 
	{
		showLoadIndicator();
		
		var userDetails = { 
			"userId" : userId, 
			"firstName" : firstName, 
			"lastName" : lastName, 
			"emailAddress" : emailAddress,
			"locationName" : "HONG_KONG", // TODO
			"groupName" : "CLSA_SALES",
			"isValid" : isValid, 
			"lastUpdatedBy" : lastUpdatedBy 
		};	    
		
		$.ajax({
		    url: contextPath + "/users/ajaxUpdateUser", 
		    type: 'POST', 
		    dataType: 'json',
		    data: JSON.stringify(userDetails),
		    contentType: 'application/json',
		    mimeType: 'application/json',
		    timeout: userUpdateTimeout,
		    cache: false,
		    success: function(result) 
		    {
		    	loadingIndicator.fadeOut();
		    	
		    	if(result)
		    	{
		    		var item = dataView.getItemById(userId);
		    		item["isValid"] = isValid;
		    		dataView.updateItem(userId, item);
		    		
		    		if(flashRowFlag)
		    			flashRow(userId);
			    	
		    		usersGrid.removeCellCssStyles("modified");
			    	delete modifiedCells[dataView.getRowById(userId)];
			    	usersGrid.setCellCssStyles("modified", modifiedCells);
		    	}
		    	else
		    		alert('Failed to update the user details because of a server error.');		    	
		    },
            error: function (xhr, textStatus, errorThrown) 
            {
            	loadingIndicator.fadeOut();            	
            	console.log(xhr.responseText);
            	alert('Failed to update the user details because of a server error.');                
            }
		});
	}
	
	// TODO
	function ajaxGetAnyNewUsers()
	{
		showLoadIndicator();
		
		$.ajax({
		    url: contextPath + "/users/ajaxGetAnyNewUsers", 
		    type: 'GET', 
		    dataType: 'json',
		    contentType: 'application/json',
		    mimeType: 'application/json',
		    timeout: userUpdateTimeout,
		    cache: false,
		    success: function(newUsers) 
		    {
		    	loadingIndicator.fadeOut();		    	
		    	
				if(newUsers)
				{
				 	var length = newUsers.length;				 	
					for(var index = 0; index < length; index++)
					{
						dataView.insertItem(0, {"userId" : newUsers[index].userId, 
							"firstName" : newUsers[index].firstName, 
							"lastName" : newUsers[index].lastName, 
							"emailAddress" : newUsers[index].emailAddress, 
							"isValid" : newUsers[index].isValid, 
							"lastUpdatedBy" : newUsers[index].lastUpdatedBy });
					
						flashRow(newUsers[index].userId);
					}
				}
		    },
            error: function (xhr, textStatus, errorThrown) 
            {
            	loadingIndicator.fadeOut();
            	console.log(xhr.responseText);
            	alert('Failed to get an update of any new users because of a server error.');                
            }
		});		
	}
	
	function ajaxAddNewUser(userId, firstName, lastName, emailAddress, lastUpdatedBy) 
	{
		showLoadIndicator();
		
		var newUser = { 
			"userId" : userId, 
			"firstName" : firstName, 
			"lastName" : lastName, 
			"emailAddress" : emailAddress,
			"locationName" : "HONG_KONG", //TODO
			"groupName" : "CLSA_SALES",			
			"isValid" : true, 
			"lastUpdatedBy" : lastUpdatedBy 
		};
		
		$.ajax({
		    url: contextPath + "/users/ajaxAddNewUser", 
		    type: 'POST', 
		    dataType: 'json',
		    data: JSON.stringify(newUser),
		    contentType: 'application/json',
		    mimeType: 'application/json',
		    timeout: userUpdateTimeout,
		    cache: false,
		    success: function(result) 
		    {
		    	loadingIndicator.fadeOut();
		    	
				if(result)
				{
					dataView.insertItem(0, {
						"userId" : userId, 
						"firstName" : firstName, 
						"lastName" : lastName, 
						"emailAddress" : emailAddress, 
						"isValid" : true,
						"lastUpdatedBy" : lastUpdatedBy 
					});
					
					flashRow(userId);
					disableAddButton();
					clearNewUserInputFields();					
				}
				else
					alert("Failed to insert the new user because of a server error.");
		    },
            error: function (xhr, textStatus, errorThrown) 
            {
            	loadingIndicator.fadeOut();
            	console.log(xhr.responseText);
            	alert('Failed to insert the new user because of a server error.');                
            }
		});
	}	
		
	ajaxGetListOfUsers();
	
	usersGrid.onSort.subscribe(function(e, args)
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
		usersGrid.updateRowCount();
		usersGrid.render();
	});

	dataView.onRowsChanged.subscribe(function (e, args) 
	{
		usersGrid.invalidateRows(args.rows);
		usersGrid.render();
	});
				
	dataView.refresh();
	usersGrid.render();
	
	usersGrid.onContextMenu.subscribe(function (e) 
	{
        var cell = usersGrid.getCellFromEvent(e);
        var item = dataView.getItem(cell.row);
        e.preventDefault();
    	
    	$("#userContextMenu li").each(function(index)
		{
    		if((item["Validity"] === "Valid") && ($(this).attr("data") === "VALIDATE") ||
    				(item["Validity"] === "Invalid") && ($(this).attr("data") === "INVALIDATE"))
        		$(this).hide();
    		else
    			$(this).show();
		});        	
    	
        $("#userContextMenu")
            .data("row", cell.row)
            .css("top", e.pageY)
            .css("left", e.pageX)
            .show();
    
        $("body").one("click", function() 
        {
          $("#userContextMenu").hide();
        });
    });
		
    $("#userContextMenu").click(function (e) 
    {
    	if (!$(e.target).is("li")) 
    		return;
      
    	if (!usersGrid.getEditorLock().commitCurrentEdit())
    		return;
      
    	var row = $(this).data("row");
    	var operation = $(e.target).attr("data");
    	var lastUpdatedBy = "ladeoye";
		
    	switch (operation) 
    	{
        	case "VALIDATE":
        		ajaxUpdateUser(dataView.getItem(row).userId, dataView.getItem(row).firstName, 
        			dataView.getItem(row).lastName, dataView.getItem(row).emailAddress, 
        			true, lastUpdatedBy, true);	
        		break;
        	case "INVALIDATE":
        		ajaxUpdateUser(dataView.getItem(row).userId, dataView.getItem(row).firstName, 
        			dataView.getItem(row).lastName, dataView.getItem(row).emailAddress,
        			false, lastUpdatedBy, true);
        		break;
        	case "SAVE":
        		ajaxUpdateUser(dataView.getItem(row).userId, dataView.getItem(row).firstName, 
        			dataView.getItem(row).lastName, dataView.getItem(row).emailAddress, 
        			dataView.getItem(row).isValid, lastUpdatedBy, true);
        		break;
        	case "SAVE_ALL":
        		var answer = confirm("Are you sure you want to save all changes?");
        		if(answer)
    			{
            		for (var key in modifiedCells)
            		{
                		ajaxUpdateUser(dataView.getItem(key).userId, dataView.getItem(key).firstName, 
                			dataView.getItem(key).lastName, dataView.getItem(key).emailAddress,
                			dataView.getItem(key).isValid, lastUpdatedBy, false);
            		}    			
    			}        		
        		break;
        	case "REFRESH":
        		ajaxGetListOfUsers();
        		break;
        	default: 
        		alert("Sorry, this operation is yet to be supported!");
    	}				
    });	    	
});
