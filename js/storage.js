known = "known";

unknown = "unknown";

 

function setKnown(word){
	var storage = window['localStorage'],
	storage.setItem(word, known);
}

function setUnknown(word){
	var storage = window['localStorage'],
	storage.setItem(word, unknown);
}

function getStatus(word){
	var storage = window['localStorage'],
	storage.getItem(word);
}
