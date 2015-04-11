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
			var vLinkElem   = jQuery(this);
			var customData  = vLinkElem.data('imagetype');
			var customClass = '',
				customId	= '';
			
			var file_frame = uploads_multimedia_init('Upload Image', 'Select Image', true, false);
			file_frame.on( 'select', function() {
				var selection = file_frame.state().get('selection');
					selection.map( function( attachment ) {
						attachment = attachment.toJSON();
						var image_url = attachment.url,
							image_id  = attachment.id;
							
							if 		(customData  == 'slide')   { customClass = 'custom-slide';} 
							else if (customData  == 'logo')    { customClass = 'logo'; } 
							else if (customData  == 'favicon') { customClass = 'favicon'; } 
							else if (customData  == 'headerbackground') { customClass = 'headerbackground'; customId = 'headerbackground'; }  
							else 	{ customClass = ''; }
							
							vLinkElem.parent().parent().find('.img-container').remove();
							if (customClass == '') {
								vLinkElem.parent().parent().prepend('<div class="img-container"><img src="' + image_url + '" alt="" /></div>');
							}
							else if (customClass == 'headerbackground') {
								vLinkElem.parent().parent().prepend('<div class="img-container"><img id="headerimgbackground" src="' + image_url + '" alt="" /></div>');
							}
							else {
								console.log(vLinkElem.parent().parent().find('.img-container img'));
								vLinkElem.parent().parent().prepend('<div class="img-container ' + customClass + '"><img src="' + image_url + '" alt="" /></div>');
							}							
							vLinkElem.parent().parent().find('input[type="hidden"]').val(image_id);
					});

			});

			file_frame.open();
			return false;
		});
	
		jQuery('.reset_btn').live('click', function( event ) {
			event.preventDefault();
				var vLinkElem = jQuery(this);
					vLinkElem.parent().parent().find('.img-container').remove(); 
					vLinkElem.parent().parent().find('input').val('');
					vLinkElem.remove();
			return false;
		});	
		
		jQuery(".remove-slide").live("click", function() {
			var vElemRemove = jQuery(this).parent().parent();
			if (jQuery(this).parent().next().find('.reset_slide_btn').length > 0) {
			    jQuery(this).parent().next().find('.reset_slide_btn').click();
			}
			vElemRemove.remove();
					
		});
		
	jQuery(".add_new_btn").bind("click", function() {
			var 	vIndex,     
					vMaxIndex = 0;
			
			jQuery('.slides').children().each(function() {
				var vStr = jQuery(this).attr('id');
				vIndex = parseInt(vStr.substr(vStr.indexOf("-")+7,2));
					if ( vMaxIndex < vIndex) {
						 vMaxIndex = vIndex;
					}
			});
			
			var data = {
						action:  'fruitful_add_new_slide_action',
						type:    'add_new_slide',
						data:     vMaxIndex + 1
					};	
		 
			jQuery.post(ajaxurl, data, function(response) {
				if (jQuery("ul.slides li.slide").length > 0) {
					jQuery("ul.slides li.slide").last().after(response);
				} else {
					jQuery("ul.slides").append(response);
				}
				jQuery('input[type=checkbox]').ezMark();  
	          });
	          return false;
		});
		
		/*Sortable slide*/
		jQuery("ul.slides").sortable({ 
			 opacity: 0.9,
			 cursor: 'move',
 			 revert: true,
			 beforeStop: function(event, ui) {},
			 change :function(event, ui) {}
		});
}); 