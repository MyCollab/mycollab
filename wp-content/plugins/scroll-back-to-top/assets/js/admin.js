(function($){
	$(function(){
		sbttLoaded();
		$('[name="scroll-back-to-top[label_type]"]').on('change', sbttLoaded);
	});

	function sbttLoaded(){
		if($('[name="scroll-back-to-top[label_type]"]:checked').val() == 'text'){
			$('[name="scroll-back-to-top[icon_size]"]').prop('disabled', true);
			$('[name="scroll-back-to-top[label_text]"]').prop('disabled', false);
			$('[name="scroll-back-to-top[font_size]"]').prop('disabled', false);
			if(!$('[name="scroll-back-to-top[font_size]"]').val()){
				$('[name="scroll-back-to-top[font_size]"]').val(12);
			}
		} else {
			$('[name="scroll-back-to-top[icon_size]"]').prop('disabled', false);
			$('[name="scroll-back-to-top[label_text]"]').prop('disabled', true);
			$('[name="scroll-back-to-top[font_size]"]').prop('disabled', true);
			if(!$('[name="scroll-back-to-top[icon_size]"]:checked').val()){
				$('[name="scroll-back-to-top[icon_size]"][value="fa-2x"]').prop('checked', true);
			}
		}
	}
})(jQuery);