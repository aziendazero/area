(function($) {
  "use strict";
  $.fn.multifilter = function(options) {
    var settings = $.extend( {
      'target'        : $('table'),
      'method'    : 'thead', // This can be thead or class or thclass (individua le colonne tramite class nel head)
      'inputColInfoStored'    : 'data-col' // This can be data-col or dijit (occorre selezionare gli input tramite id ed il valore viene ricavato tramite dijit)
    }, options);

    jQuery.expr[":"].Equals = function(a, i, m) {
      return (a.textContent || a.innerText || "").toUpperCase() === m[3].toUpperCase();
    };

    this.each(function() {
      var $this = $(this);
      if (settings.inputColInfoStored === 'dijit')
    	  $this = dijit.byId($this.attr('id'));
      var container = settings.target;
      var row_tag = 'tr';
      var item_tag = 'td';
      var rows = container.find($(row_tag));
      var dataCol = undefined;
      if (settings.inputColInfoStored === 'dijit') {
    	  dataCol = $this.attr('id');
      } else {
    	  dataCol = $this.data('col');
      }
	  console.log('dataCol: ' + dataCol);

      if (settings.method === 'thead') {
        // Match the data-col attribute to the text in the thead
        var col = container.find('th:Equals(' + dataCol + ')');
        var col_index = container.find($('thead th')).index(col);
      };

      if (settings.method === 'thclass') {
          // Match the data-col attribute to the class of thead cols
          var col = container.find('th.' + dataCol);
          var col_index = container.find($('thead th')).index(col);
      };

      if (settings.method === 'class') {
        // Match the data-col attribute to the class on each column
        var col = rows.first().find('td.' + dataCol);
        var col_index = rows.first().find('td').index(col);
      };

      if (settings.inputColInfoStored === 'dijit') {
	      dojo.connect($this, 'onChange', function(){
	        var filter;
	        if (settings.inputColInfoStored === 'dijit')
	        	filter = dijit.byId($this.attr('id')).attr('value');
	        else
	        	filter = $this.val();
	        //alert('$this.val(): ' + $this.val());
	        console.log('filter: ' + filter);
	        rows.each(function() {
	          var row = $(this);
	          var cell = $(row.children(item_tag)[col_index]);
	          if (filter) {
	            if (cell.text().toLowerCase().indexOf(filter.toLowerCase()) !== -1) {
	              cell.attr('data-filtered', 'positive');
	            } else {
	              cell.attr('data-filtered', 'negative');
	            }
	            if (row.find(item_tag + "[data-filtered=negative]").size() > 0) {
	               row.hide();
	            } else {
	              if (row.find(item_tag + "[data-filtered=positive]").size() > 0) {
	                row.show();
	              }
	            }
	          } else {
	            cell.attr('data-filtered', 'positive');
	            if (row.find(item_tag + "[data-filtered=negative]").size() > 0) {
	              row.hide();
	            } else {
	              if (row.find(item_tag + "[data-filtered=positive]").size() > 0) {
	                row.show();
	              }
	            }
	          }
	        });
	        return false;
	      });
	      //$this.keyup(function() {
	      //dojo.connect($this, 'onkeyup', function(event){
	      dojo.connect($this,'onKeyUp',function(){
		    console.log('onKeyUp');
	        //$this.change();
	        //$this.onchange();
	        $this.onChange();
	      });
      } else {
    	$this.change(function() {
	        var filter;
	        if (settings.inputColInfoStored === 'dijit')
	        	filter = dijit.byId($this.attr('id')).attr('value');
	        else
	        	filter = $this.val();
	        //alert('$this.val(): ' + $this.val());
	        console.log('filter: ' + filter);
	        rows.each(function() {
	          var row = $(this);
	          var cell = $(row.children(item_tag)[col_index]);
	          if (filter) {
	            if (cell.text().toLowerCase().indexOf(filter.toLowerCase()) !== -1) {
	              cell.attr('data-filtered', 'positive');
	            } else {
	              cell.attr('data-filtered', 'negative');
	            }
	            if (row.find(item_tag + "[data-filtered=negative]").size() > 0) {
	               row.hide();
	            } else {
	              if (row.find(item_tag + "[data-filtered=positive]").size() > 0) {
	                row.show();
	              }
	            }
	          } else {
	            cell.attr('data-filtered', 'positive');
	            if (row.find(item_tag + "[data-filtered=negative]").size() > 0) {
	              row.hide();
	            } else {
	              if (row.find(item_tag + "[data-filtered=positive]").size() > 0) {
	                row.show();
	              }
	            }
	          }
	        });
	        return false;
	      }).keyup(function() {
	        $this.change();
	      });
      }
    });
  };
})(jQuery);
