/*
  textfill
 @name      jquery.textfill.js
 @author    Russ Painter
 @author    Yu-Jie Lin
 @version   0.4.0
 @date      2013-08-16
 @copyright (c) 2012-2013 Yu-Jie Lin
 @copyright (c) 2009 Russ Painter
 @license   MIT License
 @homepage  https://github.com/jquery-textfill/jquery-textfill
 @example   http://jquery-textfill.github.io/jquery-textfill/index.html
*/
(function(e){e.fn.textfill=function(q){function m(){a.debug&&("undefined"!=typeof console&&"undefined"!=typeof console.debug)&&console.debug.apply(console,arguments)}function r(){"undefined"!=typeof console&&"undefined"!=typeof console.warn&&console.warn.apply(console,arguments)}function n(a,b,d,h,f,k){function c(a,b){var c=" / ";a>b?c=" > ":a==b&&(c=" = ");return c}m(a+"font: "+b.css("font-size")+", H: "+b.height()+c(b.height(),d)+d+", W: "+b.width()+c(b.width(),h)+h+", minFontPixels: "+f+", maxFontPixels: "+
k)}function p(a,b,d,h,f,k,c,l){for(n(a+": ",b,f,k,c,l);c<l-1;){var e=Math.floor((c+l)/2);b.css("font-size",e);if(d.call(b)<=h){if(c=e,d.call(b)==h)break}else l=e;n(a+": ",b,f,k,c,l)}b.css("font-size",l);d.call(b)<=h&&(c=l,n(a+"* ",b,f,k,c,l));return c}var a=e.extend({debug:!1,maxFontPixels:40,minFontPixels:4,innerTag:"span",widthOnly:!1,success:null,callback:null,fail:null,complete:null,explicitWidth:null,explicitHeight:null},q);this.each(function(){var g=e(a.innerTag+":visible:first",this),b=a.explicitHeight||
e(this).height(),d=a.explicitWidth||e(this).width(),h=g.css("font-size");m("Opts: ",a);m("Vars: maxHeight: "+b+", maxWidth: "+d);var f=a.minFontPixels,k=0>=a.maxFontPixels?b:a.maxFontPixels,c=void 0;a.widthOnly||(c=p("H",g,e.fn.height,b,b,d,f,k));f=p("W",g,e.fn.width,d,b,d,f,k);a.widthOnly?g.css("font-size",f):g.css("font-size",Math.min(c,f));m("Final: "+g.css("font-size"));g.width()>d||g.height()>b&&!a.widthOnly?(g.css("font-size",h),a.fail&&a.fail(this)):a.success?a.success(this):a.callback&&(r("callback is deprecated, use success, instead"),
a.callback(this))});a.complete&&a.complete(this);return this}})(window.jQuery);
