jQuery(document).ready(function() {
	if (jQuery('.list-layouts').length > 0) {
		jQuery('.list-layouts li img').each(function() {
			jQuery(this).removeClass('selected');
		})
		
		var vCheck = jQuery('.list-layouts').find('input:checked');
			vCheck.parent().find('img').addClass('selected');
		
		jQuery('.list-layouts li img').live('click', function() {
			jQuery('.list-layouts li img').each(function() {
				jQuery(this).removeClass('selected');
				jQuery(this).parent().find('input').prop('checked', true);
			})
			
			jQuery(this).addClass('selected');
			jQuery(this).parent().find('input').prop('checked', true);
			
			return false;
		});
		
	}
});