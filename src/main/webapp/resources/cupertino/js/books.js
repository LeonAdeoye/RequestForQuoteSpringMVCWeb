function validityFormatter(row, cell, value, columnDef, dataContext)
{
	return value ? "Valid" : "Invalid";
}

var columns = 
[
 	{id: "bookCode", name: "Book Code", field: "bookCode", sortable: true, toolTip: "Book Code"},
	{id: "entity", name: "Entity", field: "entity", sortable: true, toolTip: "Entity"},
	{id: "isValid", name: "Validity", field: "isValid", sortable: true, toolTip: "Validity", formatter: validityFormatter},
	{id: "lastUpdatedBy", name: "Last Updated By", field: "lastUpdatedBy", sortable: true, toolTip: "last updated by user"}
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
	
	function getListOfBooks() 
	{		
		$.ajax({
		    url: contextPath + "/books/ajaxUpdateValidity", 
		    type: 'GET', 
		    dataType: 'json',  
		    contentType: 'application/json',
		    mimeType: 'application/json',
		    timeout: bookUpdateTimeout,
		    cache: false,
		    success: function(result) 
		    {
		    	loadingIndicator.fadeOut();
		    },
            error: function (xhr, textStatus, errorThrown) 
            {
            	console.log(xhr.responseText);
            	alert('Failed to update the validity details because of a server error.');
                loadingIndicator.fadeOut();
            }
		});
	}
	
	function updateBookValidity(bookCode, validity, updatedByUser) 
	{		
		$.ajax({
		    url: contextPath + "/books/ajaxUpdateValidity", 
		    type: 'GET', 
		    dataType: 'json',  
		    contentType: 'application/json',
		    mimeType: 'application/json',
		    timeout: bookUpdateTimeout,
		    cache: false,
		    success: function(books) 
		    {
		    	loadingIndicator.fadeOut();
		    },
            error: function (xhr, textStatus, errorThrown) 
            {
            	console.log(xhr.responseText);
            	alert('Failed to update the validity details because of a server error.');
                loadingIndicator.fadeOut();
            }
		});
	}	
	
	showLoadIndicator();
	getListOfBooks();
	
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
	booksGrid.render()
	
    $("#bookContextMenu").click(function (e) 
    {
    	if (!$(e.target).is("li")) 
    		return;
      
    	if (!requestsGrid.getEditorLock().commitCurrentEdit())
    		return;
      
    	var row = $(this).data("row");
    	var operation = $(e.target).attr("data");
    	
    	switch (operation) 
    	{
        	case "VALIDATE":
        		alert("Sorry, this operation is yet to be supported!");	
        		break;
        	case "INVALIDATE":
        		alert("Sorry, this operation is yet to be supported!");
        		break;
        	case "CUT_AND_PASTE":
        		alert("Sorry, this operation is yet to be supported!");
        		break;          		
        	default: 
        		alert("Sorry, this operation is yet to be supported!");
    	}    	

    });	    
	
});
