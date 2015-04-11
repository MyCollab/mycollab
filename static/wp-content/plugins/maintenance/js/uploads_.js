function uploads_multimedia_init (title, btnName, editing, multiple) {
		var outArray = [];
		var file_frame;
		if (file_frame) {		
			file_frame.open();
			return;
		}
		
		file_frame = wp.media.editor.send.attachment  = wp.media({
			 title: title,
		     button: {
						text: btnName
					 },
			 editing:    editing,
			 multiple:   multiple,
		});
		return file_frame;
}

jQuery(document).ready(function() {
   jQuery('.upload_btn').live('click', function( event ) {
			event.preventDefault();
			var vLinkElem = jQuery(this);
			var customData = vLinkElem.data('imagetype');
			var customClass = '';
			
			var file_frame = uploads_multimedia_init('Upload Image', 'Select Image', true, false);
			file_frame.on( 'select', function() {
				var selection = file_frame.state().get('selection');
					selection.map( function( attachment ) {
						attachment = attachment.toJSON();
						var image_url = attachment.url,
							image_id  = attachment.id;
							
							vLinkElem.parent().find('.boxes').css('background-image', 'url(' + image_url + ')');
							vLinkElem.parent().find('.boxes').append('<input class="button delete-img remove" type="submit" name="remove_bg" value="x" />');
							vLinkElem.parent().parent().find('input[type="hidden"]').val(image_id);
					});

			});

			file_frame.open();
			return false;
		});
	
		jQuery('.delete-img').live('click', function( event ) {
			event.preventDefault();
				var vLinkElem = jQuery(this);
					vLinkElem.parent().css('background-image', 'none'); 
					vLinkElem.parent().parent().parent().find('input[type="hidden"]').val('');
					vLinkElem.remove();
			return false;
		});	
	
}); 