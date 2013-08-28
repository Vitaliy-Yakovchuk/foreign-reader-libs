		function showSel() {
     var sel = document.selection, range;
    var x = 0, y = 0;
    if (sel) {
        if (sel.type != "Control") {
            range = sel.createRange();
            range.collapse(true);
            x = range.boundingLeft;
            y = range.boundingTop;
        }
    } else if (window.getSelection) {
        sel = window.getSelection();
        if (sel.rangeCount) {
            range = sel.getRangeAt(0).cloneRange();
            if (range.getClientRects) {
                range.collapse(true);
                var rect = range.getClientRects()[0];
                x = rect.left;
                y = rect.top;
            }
        }
    }
	
	var t = sel.toString().toLowerCase().trim();
	
	var txt = t + " " + getStatus(t);
	
	$("<div />").attr("id", "dialog-confirm").append(
        $("<p />").text(txt).css("text-align", "center")
    ).dialog({
	 modal:true,
	 position: [x, y],
	 
	 buttons: { "Known": function() {setKnown(t); $(this).dialog("close"); }, 
	 "Unknown": function() { setUnknown(t); $(this).dialog("close"); }} 
	 
	 
	});
}

	$(document).dblclick(function(e) {
    showSel();
	
    //alert(t);
});
