String.prototype.toPascalCase = function()
{
    return this.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
};

function trimSpaces(x)
{
	return x.replace(/^\s+|\s+$/gm,'');
}

function dateFormatter(row, cell, value, columnDef, dataContext)
{
    if (value !== null)
    {	
    	var theDate = new Date(value);
    	if(theDate !== NaN)
    		return $.datepicker.formatDate("yy-mm-dd", theDate);    		
    }
    return "";
}