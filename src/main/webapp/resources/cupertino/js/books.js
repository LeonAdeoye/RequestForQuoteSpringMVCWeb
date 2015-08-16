function validityFormatter(row, cell, value, columnDef, dataContext)
{
	return '';
}

var columns = 
[
 	{id: "bookCode", name: "Book Code", field: "bookCode", sortable: true, toolTip: "Book Code"},
	{id: "entity", name: "Entity", field: "entity", sortable: true, toolTip: "Entity"},
	{id: "isValid", name: "Valid", field: "isValid", sortable: true, toolTip: "Validity", formatter: validityFormatter},
	{id: "lastUpdatedByUser", name: "Last Updated By", field: "lastUpdatedByUser", sortable: true, toolTip: "last updated by user"}
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
    topPanelHeight:230
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
		    url: contextPath + "/books", 
		    type: 'GET', 
		    dataType: 'json',  
		    contentType: 'application/json',
		    mimeType: 'application/json',
		    timeout: 5000,
		    cache: false,
		    success: function(books) 
		    {
		    	dataView.setItems(books, "bookCode");
		    	// TODO add sort
		    	loadingIndicator.fadeOut();
		    },
            error: function (xhr, textStatus, errorThrown) 
            {
            	console.log(xhr.responseText);
            	alert('Failed to retrieve book details because of a server error.');
                loadingIndicator.fadeOut();
            }
		});
	}
	
	showLoadIndicator();
	getListOfBooks();
	
});
