function validityFormatter(row, cell, value, columnDef, dataContext)
{
	return value ? "Valid" : "Invalid";
}

var columns = 
[
 	{id: "bookCode", name: "Book Code", field: "bookCode", sortable: true, toolTip: "Unique book code"},
	{id: "entity", name: "Entity", field: "entity", sortable: true, toolTip: "Book's entity"},
	{id: "isValid", name: "Validity", field: "isValid", sortable: true, toolTip: "Books's validity status", formatter: validityFormatter},
	{id: "lastUpdatedBy", name: "Last Updated By", field: "lastUpdatedBy", sortable: true, toolTip: "User to last update the book", width: 90}
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
	$("#new-book-add").removeAttr('disabled');
}

function disableAddButton()
{
	$("#new-book-add").attr('disabled', 'disabled');
}

function toggleAddButtonState()
{
	if(($("#new-book-bookCode").val() != "" && $("#new-book-bookCode").val() != $("#new-book-bookCode").attr("default_value"))
			&& ($("#new-book-entity").val() != "" && $("#new-book-entity").val() != $("#new-book-entity").attr("default_value")))
		enableAddButton();
	else
		disableAddButton();
}

function clearNewBookInputFields()
{
	$("#new-book-bookCode").val($("#new-book-bookCode").attr("default_value"));
	$("#new-book-entity").val($("#new-book-entity").attr("default_value"));	
}

$(document).ready(function()
{
	var bookUpdateAjaxLock = false;
	var bookUpdateIntervalTime = 1000;
	var bookUpdateTimeout = 5000;
	
	var loadingIndicator = null;
	var groupItemMetadataProvider = new Slick.Data.GroupItemMetadataProvider();
	
	var dataView = new Slick.Data.DataView(
	{
	    groupItemMetadataProvider: groupItemMetadataProvider,
	    inlineFilters: true
	});
	
	var booksGrid = new Slick.Grid("#booksGrid", dataView, columns, options);
	
	booksGrid.registerPlugin(groupItemMetadataProvider);	
	booksGrid.setSelectionModel(new Slick.RowSelectionModel());
	booksGrid.setTopPanelVisibility(false);
	
	$(".new-book-btn").button();
	disableAddButton();	
	$("#new-book-bookCode").keyup(toggleAddButtonState);
	$("#new-book-bookCode").focusout(toggleAddButtonState);
	$("#new-book-entity").keyup(toggleAddButtonState);
	$("#new-book-entity").focusout(toggleAddButtonState);
	
	$("input.new-book-input-class").click(function()
	{
		if($(this).val() === $(this).attr("default_value"))
		{
			$(this).val("");
			
			if($(this).is("#new-book-bookCode"))
				disableAddButton();
		}
	});

	$("input.new-book-input-class").focusout(function()
	{
		if(trimSpaces($(this).val()) === "")
			$(this).val($(this).attr("default_value"));
	});	

	$("#new-book-clear").click(function()
	{
		clearNewBookInputFields();		
		disableAddButton();
	});
	
	$("#new-book-add").click(function()
	{
		var bookCode = $("#new-book-bookCode").val();
		var entity = $("#new-book-entity").val();
		var updatedByUser = "ladeoye";
		
		ajaxAddNewBook(bookCode.toUpperCase(), entity, updatedByUser);
		disableAddButton();
		clearNewBookInputFields();		
	});		
	
    function showLoadIndicator() 
    {
        if (!loadingIndicator) 
        {
        	loadingIndicator = $(".loading-indicator").appendTo(document.body);
        	var $g = $("#booksGrid");
        	loadingIndicator
            	.css("position", "absolute")
            	.css("top", $g.position().top + $g.height() / 2 - loadingIndicator.height() / 2)
            	.css("left", $g.position().left + $g.width() / 2 - loadingIndicator.width() / 2);
        }
        loadingIndicator.show();
    }	
	
	function ajaxGetListOfBooks() 
	{
		showLoadIndicator();
		
		$.ajax({
		    url: contextPath + "/books/ajaxGetListOfAllBooks", 
		    type: 'GET', 
		    dataType: 'json',  
		    contentType: 'application/json',
		    mimeType: 'application/json',
		    timeout: bookUpdateTimeout,
		    cache: false,
		    success: function(books) 
		    {
		    	dataView.setItems(books, "bookCode");
		    	loadingIndicator.fadeOut();
		    },
            error: function (xhr, textStatus, errorThrown) 
            {
            	loadingIndicator.fadeOut();
            	console.log(xhr.responseText);
            	alert('Failed to retrieve list of books because of a server error.');                
            }
		});
	}
	
	function ajaxUpdateBookValidity(bookCode, validity, updatedByUser) 
	{
		var bookDetails = { "bookCode" : bookCode, "isValid" : validity, "updatedByUser" : updatedByUser };
		
		$.ajax({
		    url: contextPath + "/books/ajaxUpdateValidity", 
		    type: 'GET', 
		    dataType: 'json',
		    data: JSON.stringify(bookDetails),
		    contentType: 'application/json',
		    mimeType: 'application/json',
		    timeout: bookUpdateTimeout,
		    cache: false,
		    success: function(result) 
		    {
		    	loadingIndicator.fadeOut();
		    	
		    	if(result)
					booksGrid.flashCell(0, booksGrid.getColumnIndex("isValid"), 100);
				else
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
	
	function flashNewBookRow(bookCode)
	{
		var columns = booksGrid.getColumns();
		var size = columns.length;
		
		for(var index = 0; index < size; index++)
			booksGrid.flashCell(dataView.getRowById(bookCode), booksGrid.getColumnIndex(columns[index].name), 100);
	}	
	
	function ajaxAddNewBook(bookCode, entity, updatedByUser) 
	{
		var bookDetails = { "bookCode" : bookCode, "entity" : entity, "updatedByUser" : updatedByUser };
		
		$.ajax({
		    url: contextPath + "/books/ajaxAddNewBook", 
		    type: 'GET', 
		    dataType: 'json',
		    data: JSON.stringify(bookDetails),
		    contentType: 'application/json',
		    mimeType: 'application/json',
		    timeout: bookUpdateTimeout,
		    cache: false,
		    success: function(result) 
		    {
		    	loadingIndicator.fadeOut();
				if(result)
				{
					dataView.insertItem(0, { "bookCode": bookCode, "entity": entity , 
						"isValid" : true, "updatedByUser" : updatedByUser });
					
					flashNewBookRow(bookCode);
				}
				else
		    		alert("Failed to insert the new book because of a server error.");		    	
		    },
            error: function (xhr, textStatus, errorThrown) 
            {
            	loadingIndicator.fadeOut();
            	console.log(xhr.responseText);
            	alert('Failed to insert the new book because of a server error.');                
            }
		});
	}	
		
	ajaxGetListOfBooks();
	
	booksGrid.onSort.subscribe(function(e, args)
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
		booksGrid.updateRowCount();
		booksGrid.render();
	});

	dataView.onRowsChanged.subscribe(function (e, args) 
	{
		booksGrid.invalidateRows(args.rows);
		booksGrid.render();
	});
				
	dataView.refresh();
	booksGrid.render();
	
	booksGrid.onContextMenu.subscribe(function (e) 
	{
        var cell = booksGrid.getCellFromEvent(e);
        var item = dataView.getItem(cell.row);
        e.preventDefault();
    	
    	$("#bookContextMenu li").each(function(index)
		{
    		if((item["Validity"] === "Valid") && ($(this).attr("data") === "VALIDATE") ||
    				(item["Validity"] === "Invalid") && ($(this).attr("data") === "INVALIDATE"))
        		$(this).hide();
    		else
    			$(this).show();
		});        	
    	
        $("#bookContextMenu")
            .data("row", cell.row)
            .css("top", e.pageY)
            .css("left", e.pageX)
            .show();
    
        $("body").one("click", function() 
        {
          $("#bookContextMenu").hide();
        });
    });
		
    $("#bookContextMenu").click(function (e) 
    {
    	if (!$(e.target).is("li")) 
    		return;
      
    	if (!booksGrid.getEditorLock().commitCurrentEdit())
    		return;
      
    	var row = $(this).data("row");
    	var operation = $(e.target).attr("data");
    	var updatedByUser = "ladeoye";
    	
    	switch (operation) 
    	{
        	case "VALIDATE":        		
        		showLoadIndicator();
        		ajaxUpdateBookValidity(dataView.getItem(row).bookCode, true, updatedByUser);	
        		break;
        	case "INVALIDATE":
        		showLoadIndicator();
        		ajaxUpdateBookValidity(dataView.getItem(row).bookCode, false, updatedByUser);        		
        		break;
        	default: 
        		alert("Sorry, this operation is yet to be supported!");
    	}    	
    });	    	
});
