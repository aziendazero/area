function setRowsClass(row) {
	var els = row.parentNode.getElementsByTagName("TR");
	for (var i=1; i<els.length;i++)
		els[i].setAttribute('class','');
	row.setAttribute('class','active');
}

function displayOpenwaitmsg() {
	if (document.getElementById("waitmsgdiv") != null) {
		document.getElementById("waitmsgdiv").style.display = 'block';
		document.body.style.cursor = 'wait';
		document.getElementById("openwaitmsggif").style.display='block';
	}
}

function hideOpenwaitmsg() {
	if (document.getElementById("waitmsgdiv") != null) {
		document.getElementById("waitmsgdiv").style.display = 'none';
		document.body.style.cursor = 'auto';
		document.getElementById("openwaitmsggif").style.display='none';
	}
}

function onCustomFormSubmit(theEvent) {
	if (theEvent.keyCode == 13 && theEvent.target.tagName!='TEXTAREA')
		return false;
	return true;
}

function esitoChange(obj, dom) {
	//alert('funzione esitoChange - typeof(obj): ' + typeof(obj));
	//alert('funzione esitoChange - obj: ' + obj.value);
	if(typeof obj.value !== "undefined") {
		var valEsito = obj.value.toLowerCase();
		//Non Autorizzata all'Esercizio
		if(valEsito == 'non ammessa al procedimento' || valEsito == 'non accreditata' || valEsito == 'non autorizzata') {
			//alert('bb' + dom.byId("esitoDataInizio").value);
			$('#trDate').hide();
			dom.byId("esitoDataInizio").value = '';
			dom.byId("esitoScadenza").value = '';
		} else {
			$('#trDate').show();
		}
	}
}

function updateUdoUoCheckbox(obj, isudo, uoid, ind) {
	var isChecked = null;
	if(typeof obj.checked !== "undefined")
		isChecked = obj.checked;
	if(isChecked === null) {
		var curDij = dijit.byId(obj.id);
		isChecked = curDij.get('checked');
		//isChecked = curDij.checked;
	}
	if(isudo) {
		if(isChecked == true) {
			//sto ceccando la udo devo ceccare la relativa UO 
			//Cerco il checkbox contenuto nella tr con isudo=n and uoid=uoid 
			var search = 'tr[uoid="' + uoid + '"][isudo="n"] input[type="checkbox"]';
			dojo.query(search).forEach(function(item){
				var widget = dijit.getEnclosingWidget(item);
				widget.set('checked', true);
			});
		}		
	} else {
		//Devo dececcare tutte le Udo se sto dececcando la OU
		if(isChecked == false) {
			var search = 'tr[uoid="' + uoid + '"][isudo="y"] input[type="checkbox"]';
			dojo.query(search).forEach(function(item){
				var widget = dijit.getEnclosingWidget(item);
				widget.set('checked', false);
			});
		}
	}
	Spring.remoting.submitForm('','edit_folder_form',{_eventId:'setActiveRowIndex',index:ind,changeCheck:true,checked:isChecked});
}

function updateUdoUoInstCheckbox(obj, isuo, uoid, ind) {
	var isChecked = null;
	if(typeof obj.checked !== "undefined")
		isChecked = obj.checked;
	if(isChecked === null) {
		var curDij = dijit.byId(obj.id);
		isChecked = curDij.get('checked');
		//isChecked = curDij.checked;
	}
	//abilitare se si vuole la gestione javascript della selezione udo selezionando la uo
	if(isuo) {
		//Devo ceccare tutte le Udo se sto ceccando la OU
		if(isChecked == true) {
			var search = 'tr[uoid="' + uoid + '"][isudo="y"] input[type="checkbox"]';
			dojo.query(search).forEach(function(item){
				var widget = dijit.getEnclosingWidget(item);
				widget.set('checked', true);
			});
		}
	}
	Spring.remoting.submitForm('','form',{_eventId:'setActiveRowIndex',index:ind,changeCheck:true,checked:isChecked});
}

function setClass(elId, isEmptyField) {
	var el = document.getElementById(elId);
	if (el != null) {
		el.setAttribute("class", isEmptyField?"anchor_hidden": "anchor");
	}
	return true;
}

function setBonitaPopupSize() {
	//height="450" width="698px" 
	//alert('setBonitaPopupSize - w: ' + $(window).width() + '; h: ' + $(window).height());
	var btaskw = $(window).width() - 200;
	var btaskh = $(window).height() - 200;
	if(btaskw < 700) {
		btaskw = 700;
	}
	if(btaskh < 450) {
		btaskh = 450;
	}
	//alert('btaskh: ' + btaskh);
	//alert('btaskw: ' + btaskw);
	$('#bonitaheader').width(btaskw);
	$('#bonitaiframe').width(btaskw-2);
	$('#bonitaiframe').height(btaskh);
}

function setBonitaWorkflowImageSize() {
	//height="450" width="698px" 
	//alert('setBonitaWorkflowImageSize - w: ' + $(window).width() + '; h: ' + $(window).height());
	var btaskw = $(window).width() - 20;
	var btaskh = $(window).height() - 35;
	//alert('btaskh: ' + btaskh);
	//alert('btaskw: ' + btaskw);
	$('#bonitaheader').width(btaskw);
	$('#bonitaiframe').width(btaskw-2);
	$('#bonitaiframe').height(btaskh);
}

/**
 * apertura di un popup fullscreen
 */
function openFullscreenPopup(url) {
	var width = screen.width - 100;
	var height = screen.height - 150;
	//alert('openFullscreenPopup - width: ' + width + '; height: ' + height);

	wLeft = window.screenLeft ? window.screenLeft : window.screenX;
    wTop = window.screenTop ? window.screenTop : window.screenY;

    var left = wLeft + (window.innerWidth / 2) - (width / 2);
    var top = wTop + (window.innerHeight / 2) - (height / 2);
	
	var targetWin = window.open(url, '_blank', 'scrollbars=yes,resizable=yes, width='+width+', height='+height+', top='+top+', left='+left);
	targetWin.focus();
	
	if (url != '' && (url.startsWith('https://') || url.startsWith('http://')))
		return false;
}

function changeTypeDocsShow(chklistname, tableid) {
	//alert('changeTypeDocsShow');
	var selShow = '';
	var selHide = '';
	var selShowWrite = false;
	var selHideWrite = false;
	$("input[name='" + chklistname + "']").each( function () {
		if($(this).is(':checked') == true) {
			if(selShowWrite)
				selShow += ', ';
			selShow += '.' + $(this).val();
			selShowWrite = true;
		} else {
			if(selHideWrite)
				selHide += ', ';
			selHide += '.' + $(this).val();	
			selHideWrite = true;
		}
	});
	$('#' + tableid + ' tr').filter(selShow).show();
	$('#' + tableid + ' tr').filter(selHide).hide();
}