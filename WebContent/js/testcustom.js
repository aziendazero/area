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

function updateUdoUoCheckbox(obj, isudo, uoid, ind) {
	//alert('uoid: ' + uoid);
	//alert('ind: ' + ind);
	var curDij = dijit.byId(obj.id);
	isChecked = curDij.get('checked');
	if(isudo) {
		if(isChecked == true) {
			//sto ceccando la udo devo ceccare la relativa UO 
			//alert('isudo and checked ');
			//alert('curDij.get(\'checked\'): ' + curDij.get('checked'));
			//Cerco il checkbox contenuto nella tr con isudo=n and uoid=uoid 
			var search = 'tr[uoid="' + uoid + '"][isudo="n"] input[type="checkbox"]';
			//alert('search: ' + search);
			//alert('length: ' + dojo.query(search).length);
			dojo.query(search).forEach(function(item){
				//alert('trovato node.isUdo: ' + dojo.attr(node, "isUdo"));
				//node.;
				var widget = dijit.getEnclosingWidget(item);
				//alert(widget);
				widget.set('checked', true);
			});
		}		
	} else {
		//Devo dececcare tutte le Udo se sto dececcando la OU
		if(isChecked == false) {
			//alert('isUO and dechecked');
			var search = 'tr[uoid="' + uoid + '"][isudo="y"] input[type="checkbox"]';
			//alert('search: ' + search);
			//alert('length: ' + dojo.query(search).length);
			dojo.query(search).forEach(function(item){
				//alert('trovato node.isUdo: ' + dojo.attr(node, "isUdo"));
				//node.;
				var widget = dijit.getEnclosingWidget(item);
				//alert(widget);
				widget.set('checked', false);
			});
		}
	}
	Spring.remoting.submitForm('','home_form',{_eventId:'setActiveRowIndex',index:ind,changeCheck:true,checked:isChecked});
}

function getAllProperties(obj) {
	  var properties = '';
	  for (property in obj) {
	    properties += '\n' + property;
	  }
	  alert('Properties of object:' + properties);
}

//Per uno dei vari bug IE8
function getTagName(event) {
	var event = event || window.event;
	return (event.target || event.srcElement).tagName;
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

function changeTypeDocsShowT(chklistname, tableid) {
	//alert('numero checkbox: ' + $("input[name='" + chklistname + "']").size());
	var selShow = '';
	var selHide = '';
	var selShowWrite = false;
	var selHideWrite = false;
	$("input[name='" + chklistname + "']").each( function () {
		//alert('this.id: ' + this.id);
		//alert('this.value: ' + this.value);//OK
		//alert('this.prop(checked): ' + $(this).prop('checked'));//OK
		//alert('this.is(:checked): ' + $(this).is(':checked'));

		//alert('this.val(): ' + $(this).val());
		
		//alert($(this).val() + ' checked: ' + $(this).is(':checked'));//OK
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
		
		//var curDij = dijit.byId(this.id);
		//alert(dijit.getEnclosingWidget(this).value);//OK
		//alert('value: ' + curDij.get('value'));//NO
		//alert('isChecked:' + curDij.get('checked'));//NO in IE non risulta checcato nel onchange
		
	});
	//alert('selShow: ' + selShow);
	//alert('selHide: ' + selHide);
	
	//alert('numero tr show: ' + $('#' + tableid + ' tr').size());
	//alert('numero tr show: ' + $('#' + tableid + ' tr.Planimetrie').size());
	//alert('numero tr show: ' + $('#' + tableid + ' tr.PianoDiAdeguamento').size());
	//alert('numero tr show: ' + $('tr.PianoDiAdeguamento, tr.Planimetrie').size());
	//alert('numero tr show: ' + $('#' + tableid + ' tr').size());
	//alert('numero tr show: ' + $('#' + tableid + ' tr').filter('.PianoDiAdeguamento, .Planimetrie').size());
	
	alert('numero tr show: ' + $('#' + tableid + ' tr').filter(selShow).size());
	alert('numero tr hide: ' + $('#' + tableid + ' tr').filter(selHide).size());

	$('#' + tableid + ' tr').filter(selShow).show();
	$('#' + tableid + ' tr').filter(selHide).hide();
	//alert('numero tr show: ' + $("#" + tableid + ' .' + selShow).size());
	//alert('numero tr hide: ' + $("#" + tableid + ' .' + selHide).size());
}
