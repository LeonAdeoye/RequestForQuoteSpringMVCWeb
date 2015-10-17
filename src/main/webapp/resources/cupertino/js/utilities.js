String.prototype.toPascalCase = function()
{
    return this.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
};

String.prototype.padZero = function(len, paddingChar)
{
    var result = this, paddingChar = paddingChar || '0';
    
    while(result.length< len) 
    	result = paddingChar + result;
    
    return result;
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

function yearFormatter(row, cell, value, columnDef, dataContext)
{
    if (value !== null)
    {	
    	var theDate = new Date(value);
    	if(theDate !== NaN)
    		return $.datepicker.formatDate("yy", theDate);    		
    }
    return "";
}

function convertToDateTime(dateObject)
{
	return dateObject.dayOfMonth + " " + dateObject.month + " " + dateObject.year + ", " + 
		dateObject.hour + ":" + dateObject.minute + ":" + dateObject.second;
}

function convertToTime(dateObject)
{
	return dateObject.hour + ":" + dateObject.minute.toString().padZero(2,'0') + ":" + dateObject.second.toString().padZero(2,'0');
}