(function($){

	$(function(){
		if(typeof scrollBackToTop.autoFontSize !== 'undefined' && scrollBackToTop.autoFontSize){
			$('.scroll-back-to-top-wrapper').textfill({ maxFontPixels: 36 });
		}

		$('.scroll-back-to-top-wrapper').on('click', function(){
			if(typeof scrollBackToTop.scrollDuration !== 'undefined'){
				scrollToElement("body", scrollBackToTop.scrollDuration, 0);
			}
		});

		$(document).on( 'scroll', function(){

			if ($(window).scrollTop() > 100) {
				$('.scroll-back-to-top-wrapper').addClass('show');
			} else {
				$('.scroll-back-to-top-wrapper').removeClass('show');
			}
		});

		if(typeof scrollBackToTop.visibilityDuration !== 'undefined' && scrollBackToTop.visibilityDuration){
			$(window).on('scroll', function() {
				clearTimeout($.data(this, 'sbttScrollTimer'));
				$.data(this, 'sbttScrollTimer', setTimeout(function() {
					$('.scroll-back-to-top-wrapper').removeClass('show');
				}, scrollBackToTop.visibilityDuration));
			});
		}
	});

	function scrollToElement(selector, time, verticalOffset) {

		time = typeof(time) != 'undefined' ? time : 1000;
		verticalOffset = typeof(verticalOffset) != 'undefined' ? verticalOffset : 0;
		element = $(selector);
		offset = element.offset();
		offsetTop = offset.top + verticalOffset;
		$('html, body').animate({scrollTop: 0}, parseInt(time), 'linear');
	}

	function isFullyVisible(el) {

		if ( ! el.length ) {
			return false;
		}

		if ( el instanceof jQuery ) {
			el = el[0];
		}

		var top = el.offsetTop;
		var left = el.offsetLeft;
		var width = el.offsetWidth;
		var height = el.offsetHeight;

		while(el.offsetParent) {
			el = el.offsetParent;
			top += el.offsetTop;
			left += el.offsetLeft;
		}

		return (
			top >= window.pageYOffset &&
				left >= window.pageXOffset &&
				(top + height) <= (window.pageYOffset + window.innerHeight) &&
				(left + width) <= (window.pageXOffset + window.innerWidth)
			);
	}

	function isPartiallyVisible(el) {

		if ( ! el.length ) {
			return false;
		}

		if ( el instanceof jQuery ) {
			el = el[0];
		}

		var top = el.offsetTop;
		var left = el.offsetLeft;
		var width = el.offsetWidth;
		var height = el.offsetHeight;

		while(el.offsetParent) {
			el = el.offsetParent;
			top += el.offsetTop;
			left += el.offsetLeft;
		}

		return (
			top < (window.pageYOffset + window.innerHeight) &&
				left < (window.pageXOffset + window.innerWidth) &&
				(top + height) > window.pageYOffset &&
				(left + width) > window.pageXOffset
			);
	}
})(jQuery);