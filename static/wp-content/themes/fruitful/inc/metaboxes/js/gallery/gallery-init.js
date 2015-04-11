var file_frame = '';
jQuery(document).ready(function($) {		
 jQuery('.add_gallery_items_button').live('click', function( event ) {			
	event.preventDefault();			
	if (file_frame) {
		file_frame.open();				
		return;			
	}			
	file_frame = wp.media.editor.send.attachment  = wp.media({
	editing:   true,
	multiple:   true
	}); 			
	file_frame.on( 'select', function() {				
		var selection = file_frame.state().get('selection');					
			selection.map( function( attachment ) {						
			attachment = attachment.toJSON();						
			var image_url = attachment.url,							
				image_id  = attachment.id;						
				var data = {	
					action:  'anaglyph_add_new_element_action',										
					type:    'add_new_images',										
					image_url: image_url,										
					image_id : image_id,										
					image_cnt: $("ul.sortable-admin-gallery li.img_status").length,
					anaglyph_ajax_nonce : anaglyph_vars_ajax.ajax_nonce,																			
					};													
					$.post(anaglyph_vars_ajax.ajaxurl, data, function(response) {							
					if ($("ul.sortable-admin-gallery li.img_status").length > 0) {
						$("ul.sortable-admin-gallery li.img_status").last().after(response);							
					} else {
						$("ul.sortable-admin-gallery").append(response);							
					}				  				  						
					});			  					
			});			
	});			
	file_frame.open();			
	return false;		
});		

	$( "#sortable" ).disableSelection();	
	$( "#sortable" ).sortable({placeholder:'ui-SortPlaceHolder'});			
});