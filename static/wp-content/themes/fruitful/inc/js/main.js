jQuery(document).ready(function() {  
	jQuery('input[type=checkbox]').ezMark();  
	if (document.body.clientWidth > 580){
		jQuery('select').styler();    
	}
	
	/*Font Family Preview*/  
	jQuery(".select-fonts").each(function() { 
		var vTextFontSample = jQuery(this).prev(); 		
			vTextFontSample.css({"font-family" : jQuery(this).val()});   
	});  	  
	
	jQuery(".select-fonts").change(function(){ 		
		var vTextFontSample = jQuery(this).prev(); 		
		vTextFontSample.css({"font-family" : jQuery("option:selected",this).val()}); 		
		return false;  
	});      
		
	var vSelectSlider = jQuery(".select-slider");  
	var vSelectSliderVal = vSelectSlider.val();  
	if (vSelectSliderVal == 0) { jQuery('.no-slider-select').show();  } 
	else if  (vSelectSliderVal == 1) {	  jQuery('.flex-slider').show();  } 
	else if  (vSelectSliderVal == 2) {	  jQuery('.nivo-slider').show();  }							  
		
	jQuery(".select-slider").change(function(){ 	
		var vSliderId = jQuery('.select-slider option:selected').val();		
		if (vSliderId == 0) {		
			jQuery('.no-slider-select').show();		
			jQuery('.flex-slider').hide();		
			jQuery('.nivo-slider').hide();	
		} else if  (vSliderId == 1) {		
			jQuery('.flex-slider').show();		
			jQuery('.no-slider-select').hide();		
			jQuery('.nivo-slider').hide();	
		} else if  (vSliderId == 2) {		
			jQuery('.nivo-slider').show();		
			jQuery('.no-slider-select').hide();		
			jQuery('.flex-slider').hide();	
		}	
		return false;  
	});		    
		
	jQuery('#settings-section-0').fadeIn("slow");  
	jQuery('.form-admin-fruitful .content .menu-options ul li').click(function() {  	
		jQuery('.form-admin-fruitful .content .menu-options ul li').removeClass("current");		
		jQuery(this).addClass("current");		
		jQuery(this).css({'border-top':'1px solid #E5E5E5'});		
		jQuery(this).css({'border-bottom':'1px solid #E5E5E5'});		
		jQuery(this).prev().css({'border-bottom':'0'});		
		jQuery(this).next().css({'border-top':'0'});			
		jQuery('.form-admin-fruitful .content .settings-section').hide();	
		var index_a = jQuery(this).find('a').attr("id");		
			index_a = index_a.substr(index_a.indexOf('_') + 1);		
			jQuery('#settings-section-' + index_a).fadeIn("slow");  
	});		
	
	jQuery("#upload_bg_button").click(function() { 
		jQuery("#background_img").click(); 
	});		
	
	
	/*Color init*/
	var vColorPickerOptions = {
    	defaultColor: false,
    	change:		  function(event, ui){},
    	clear: 		  function() {},
		hide: 		  true,
		palettes: 	  true
	};
	jQuery('.colorPicker').wpColorPicker(vColorPickerOptions);
	/*End Color init*/
	
	
	jQuery('#btn_idc').live('click', function() {	       
		vCurrElem = jQuery('#btn_idc');
		var data = { action: 'run_import_dummy_data', type: 'add', data: '' };			
		jQuery.post(ajaxurl, data, function(is_import) { 
			if (is_import) {
				show_message_import();	               
				t = setTimeout('fade_message()', 1000);	            
				vCurrElem.prop('disabled', true);
			}
		});	
	return false;	
	});				
	
	jQuery('#form-admin-fruitful').submit(function() {	       
		var data = jQuery(this).serialize();	       
		jQuery.post(ajaxurl, data, function(response) {				
			var vRes = parseInt(jQuery.trim(response));				   				
			 if(vRes == 1) {	               
				show_message(1);	               
				t = setTimeout('fade_message()', 2000);	            
			 } else {
				show_message(2);	               
				t = setTimeout('fade_message()', 2000);	            
			 }	        
		});	        
	return false;	
	});				
	
	jQuery('#view_all_options').live("click", function() {			
		var vElemSlideOpt = jQuery('#slider_main_options');			
			vElemSlideOpt.fadeIn('slow'); 			
			jQuery(this).remove();		
	});  		
	
	jQuery(".content-close-slide").live("click", function() {			
		var vElem = jQuery(this).parent().next();			 
		if (vElem.css('display') == "none" ) { 
			vElem.fadeIn('slow'); 
		} else { 
			vElem.fadeOut('slow'); 
		}			
	});				
	
	jQuery('input[name="reset"]').live("click", function(){		
		
		jQuery.prompt("All theme options will be reset to default. <br /> This changes can’t be returned. Be careful.", {
			title: "Reset options",
			buttons: { "Reset": true, "Cancel": false },
			focus: -1,
			opacity: 0.2,
			submit: function(e,v,m,f){
				if (v) {
					var data = {										
						action: 	'fruitful_reset_btn',										
						type:   	'reset',										
						data: 		''									
						};														
						
						jQuery.post(ajaxurl, data, function(response) { });					
						jQuery.prompt.close();
						setTimeout(function(){	
							location.reload(true);						
						}, 1000);
				}
			}
		});
		
	});							
	
	
	if (jQuery("ul.slides li").size() >2) { 
		jQuery("ul.slides li .slide-content").hide("slow"); 
	}				
		jQuery(".expand_all").live("click", function()	{ 
			jQuery("ul.slides li .slide-content").show("slow"); 
		});		
		
		jQuery(".collapse_all").live("click", function()	{ 
			jQuery("ul.slides li .slide-content").hide("slow"); 
		});		
		
		if (jQuery('body').hasClass('rtl')) {
			jQuery("#save_options").center_rtl();		
		} else {
			jQuery("#save_options").center();		
		}

	jQuery(window).bind('resize', function() { 
		if (jQuery('body').hasClass('rtl')) {
			jQuery("#save_options").center_rtl();		
		} else {
			jQuery("#save_options").center();		
		}
	});
	
	if (jQuery('.menu-type-responsive').length > 0){
		var responsiveMenuOptionBox = jQuery('.menu-type-responsive').parents('.settings-form-row');
		if (!jQuery('#responsive_ch').prop('checked')) {
			responsiveMenuOptionBox.hide();
		}
		jQuery('#responsive_ch').change(function(){
			if (responsiveMenuOptionBox.css('display') == 'none'){
				responsiveMenuOptionBox.show();
			} else {
				responsiveMenuOptionBox.hide();
			}
		});
	}
	
});

function show_message(n) {	
	if(n == 1) { jQuery('.save-options').html('<div class="icon-sc"></div><div class="message-text">Options saved</div>').show();  }  
	else 	   { jQuery('.save-options').html('<div class="icon-al"></div><div class="message-text">Nothing new to save</div>').show();  }	
}

function fade_message() {	
	jQuery('.save-options').fadeOut(1000);	
	clearTimeout(t);
}			

function show_message_import() {	
	jQuery('.save-options').html('<div class="icon-sc"></div><div class="message-text">homepage installed <br /> visit your home page</div>').show(); 
}
	
jQuery.fn.center = function () {    
		var heightRatio = (
			jQuery('#form-admin-fruitful').height() != 0)  ? this.outerHeight() / jQuery('#form-admin-fruitful').height() : 1;    
			var widthRatio 	= (jQuery('#form-admin-fruitful').width() != 0)   ? this.outerWidth() / jQuery('#form-admin-fruitful').width() : 1;    
			this.css({
				position: 'fixed',        
				margin: 0,        
				top:  (50*(1-heightRatio))  + "%",        
				left: (50*(1-widthRatio))  + "%"    });    
			return this;
	}
jQuery.fn.center_rtl = function () {    
		var heightRatio = (
			jQuery('#form-admin-fruitful').height() != 0)  ? this.outerHeight() / jQuery('#form-admin-fruitful').height() : 1;    
			var widthRatio 	= (jQuery('#form-admin-fruitful').width() != 0)   ? this.outerWidth() / jQuery('#form-admin-fruitful').width() : 1;    
			this.css({
				position: 'fixed',        
				margin: 0,        
				top:  (50*(1-heightRatio))  + "%",        
				right: (50*(1-widthRatio))  + "%"    });    
			return this;
	}