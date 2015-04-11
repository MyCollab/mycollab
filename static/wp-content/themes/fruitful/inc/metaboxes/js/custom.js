jQuery(document).ready(function() {
	jQuery('.cmb_option').iCheck({
			  checkboxClass: 'icheckbox_minimal-grey',
			  radioClass:    'iradio_minimal-grey'
	});
	
	jQuery('.cmb_select, .cmb-type-select_timezone td select').each (function() {
		jQuery(this).select2({});
	});
	
});