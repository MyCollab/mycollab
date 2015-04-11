/*
 * jQuery Form Styler v1.3.5
 * http://dimox.name/jquery-form-styler/
 *
 * Copyright 2012-2013 Dimox (http://dimox.name/)
 * Released under the MIT license.
 *
 * Date: 2013.05.01
 *
 */

(function($) {
	$.fn.styler = function(opt) {

		var opt = $.extend({
			idSuffix: '-styler',
			browseText: 'Обзор...',
			selectVisibleOptions: 0,
			singleSelectzIndex: '100',
			selectSmartPositioning: true
		}, opt);

		return this.each(function() {
			var el = $(this);
			var id = '',
					cl = '',
					dataList = '';
			if (el.attr('id') !== undefined && el.attr('id') != '') id = ' id="' + el.attr('id') + opt.idSuffix + '"';
			if (el.attr('class') !== undefined && el.attr('class') != '') cl = ' ' + el.attr('class');
			var data = el.data();
			for (var i in data) {
				if (data[i] != '') dataList += ' data-' + i + '="' + data[i] + '"';
			}
			id += dataList;

			// checkbox
			if (el.is(':checkbox')) {
				el.css({position: 'absolute', left: -9999}).each(function() {
					if (el.next('span.jq-checkbox').length < 1) {
						var checkbox = $('<span' + id + ' class="jq-checkbox' + cl + '" style="display: inline-block"><span></span></span>');
						el.after(checkbox);
						if (el.is(':checked')) checkbox.addClass('checked');
						if (el.is(':disabled')) checkbox.addClass('disabled');
						// клик на псевдочекбокс
						checkbox.click(function() {
							if (!checkbox.is('.disabled')) {
								if (el.is(':checked')) {
									el.prop('checked', false);
									checkbox.removeClass('checked');
								} else {
									el.prop('checked', true);
									checkbox.addClass('checked');
								}
								el.change();
								return false;
							} else {
								return false;
							}
						});
						// клик на label
						el.parent('label').add('label[for="' + el.attr('id') + '"]').click(function(e) {
							checkbox.click();
							e.preventDefault();
						});
						// переключение по Space или Enter
						el.change(function() {
							if (el.is(':checked')) checkbox.addClass('checked');
							else checkbox.removeClass('checked');
						})
						// чтобы переключался чекбокс, который находится в теге label
						.keydown(function(e) {
							if (el.parent('label').length && (e.which == 13 || e.which == 32)) checkbox.click();
						})
						.focus(function() {
							if (!checkbox.is('.disabled')) checkbox.addClass('focused');
						})
						.blur(function() {
							checkbox.removeClass('focused');
						});
						// обновление при динамическом изменении
						el.on('refresh', function() {
							if (el.is(':checked')) checkbox.addClass('checked');
								else checkbox.removeClass('checked');
							if (el.is(':disabled')) checkbox.addClass('disabled');
								else checkbox.removeClass('disabled');
						});
					}
				});

			// radio
			} else if (el.is(':radio')) {
				el.css({position: 'absolute', left: -9999}).each(function() {
					if (el.next('span.jq-radio').length < 1) {
						var radio = $('<span' + id + ' class="jq-radio' + cl + '" style="display: inline-block"><span></span></span>');
						el.after(radio);
						if (el.is(':checked')) radio.addClass('checked');
						if (el.is(':disabled')) radio.addClass('disabled');
						// клик на псевдорадиокнопке
						radio.click(function() {
							if (!radio.is('.disabled')) {
								$('input[name="' + el.attr('name') + '"]').prop('checked', false).next().removeClass('checked');
								el.prop('checked', true).next().addClass('checked');
								el.change();
								return false;
							} else {
								return false;
							}
						});
						// клик на label
						el.parent('label').add('label[for="' + el.attr('id') + '"]').click(function(e) {
							radio.click();
							e.preventDefault();
						});
						// переключение стрелками
						el.change(function() {
							$('input[name="' + el.attr('name') + '"]').next().removeClass('checked');
							el.next().addClass('checked');
						})
						.focus(function() {
							if (!radio.is('.disabled')) radio.addClass('focused');
						})
						.blur(function() {
							radio.removeClass('focused');
						});
						// обновление при динамическом изменении
						el.on('refresh', function() {
							if (el.is(':checked')) {
								$('input[name="' + el.attr('name') + '"]').next().removeClass('checked');
								radio.addClass('checked');
							} else {
								radio.removeClass('checked');
							}
							if (el.is(':disabled')) radio.addClass('disabled');
								else radio.removeClass('disabled');
						});
					}
				});

			// file
			} else if (el.is(':file')) {
				el.css({position: 'absolute', top: '-50%', right: '-50%', fontSize: '200px', opacity: 0}).each(function() {
					if (el.parent('span.jq-file').length < 1) {
						var file = $('<span' + id + ' class="jq-file' + cl + '" style="display: inline-block; position: relative; overflow: hidden"></span>');
						var name = $('<div class="jq-file__name" style="float: left; white-space: nowrap"></div>').appendTo(file);
						var browse = $('<div class="jq-file__browse" style="float: left">' + opt.browseText + '</div>').appendTo(file);
						el.after(file);
						file.append(el);
						if (el.is(':disabled')) file.addClass('disabled');
						el.change(function() {
							name.text(el.val().replace(/.+[\\\/]/, ''));
						})
						.focus(function() {
							file.addClass('focused');
						})
						.blur(function() {
							file.removeClass('focused');
						})
						.click(function() {
							file.removeClass('focused');
						})
						// обновление при динамическом изменении
						.on('refresh', function() {
							if (el.is(':disabled')) file.addClass('disabled');
								else file.removeClass('disabled');
						})
					}
				});

			// select
			} else if (el.is('select')) {
				el.each(function() {
					if (el.next('span.jqselect').length < 1) {

						function selectbox() {

							// запрещаем прокрутку страницы при прокрутке селекта
							function preventScrolling(selector) {
								selector.unbind('mousewheel DOMMouseScroll').bind('mousewheel DOMMouseScroll', function(e) {
									var scrollTo = null;
									if (e.type == 'mousewheel') { scrollTo = (e.originalEvent.wheelDelta * -1); }
									else if (e.type == 'DOMMouseScroll') { scrollTo = 40 * e.originalEvent.detail; }
									if (scrollTo) { e.preventDefault(); $(this).scrollTop(scrollTo + $(this).scrollTop()); }
								});
							}

							var option = $('option', el);
							var list = '';
							// формируем список селекта
							function makeList() {
								for (i = 0, len = option.length; i < len; i++) {
									var li = '',
											liClass = '',
											optionClass = '',
											optgroupClass = '';
									var disabled = 'disabled';
									var selDis = 'selected sel disabled';
									if (option.eq(i).prop('selected')) liClass = 'selected sel';
									if (option.eq(i).is(':disabled')) liClass = disabled;
									if (option.eq(i).is(':selected:disabled')) liClass = selDis;
									if (option.eq(i).attr('class') !== undefined) optionClass = ' ' + option.eq(i).attr('class');
									li = '<li class="' + liClass + optionClass + '">'+ option.eq(i).text() +'</li>';
									// если есть optgroup
									if (option.eq(i).parent().is('optgroup')) {
										if (option.eq(i).parent().attr('class') !== undefined) optgroupClass = ' ' + option.eq(i).parent().attr('class');
										li = '<li class="' + liClass + optionClass + ' option' + optgroupClass + '">'+ option.eq(i).text() +'</li>';
										if (option.eq(i).is(':first-child')) {
											li = '<li class="optgroup' + optgroupClass + '">' + option.eq(i).parent().attr('label') + '</li>' + li;
										}
									}
									list += li;
								}
							} // end makeList()

							// одиночный селект
							function doSelect() {
								var selectbox =
									$('<span' + id + ' class="jq-selectbox jqselect' + cl + '" style="display: inline-block; position: relative; z-index:' + opt.singleSelectzIndex + '">'+
											'<div class="jq-selectbox__select" style="float: left"><div class="jq-selectbox__text"></div>'+
												'<div class="jq-selectbox__trigger"><div class="jq-selectbox__trigger-arrow"></div></div>'+
											'</div>'+
										'</span>');
								el.after(selectbox).css({position: 'absolute', left: -9999});
								var divSelect = $('div.jq-selectbox__select', selectbox);
								var divText = $('div.jq-selectbox__text', selectbox);
								var optionSelected = option.filter(':selected');

								// берем опцию по умолчанию
								if (optionSelected.length) {
									divText.text(optionSelected.text());
								} else {
									divText.text(option.first().text());
								}

								// если селект неактивный
								if (el.is(':disabled')) {
									selectbox.addClass('disabled');

								// если селект активный
								} else {
									makeList();
									var dropdown =
										$('<div class="jq-selectbox__dropdown" style="position: absolute; overflow: auto; overflow-x: hidden">'+
												'<ul style="list-style: none">' + list + '</ul>'+
											'</div>');
									selectbox.append(dropdown);
									var li = $('li', dropdown);
									if (li.filter('.selected').length < 1) li.first().addClass('selected sel');
									var selectHeight = selectbox.outerHeight();
									if (dropdown.css('left') == 'auto') dropdown.css({left: 0});
									if (dropdown.css('top') == 'auto') dropdown.css({top: selectHeight});
									var liHeight = li.outerHeight();
									var position = dropdown.css('top');
									dropdown.hide();

									// при клике на псевдоселекте
									divSelect.click(function() {
										el.focus();

										// умное позиционирование
										if (opt.selectSmartPositioning) {
											var win = $(window);
											var topOffset = selectbox.offset().top;
											var bottomOffset = win.height() - selectHeight - (topOffset - win.scrollTop());
											var visible = opt.selectVisibleOptions;
											var	minHeight = liHeight * 6;
											var	newHeight = liHeight * visible;
											if (visible > 0 && visible < 6) minHeight =  newHeight;
											// раскрытие вверх
											if (bottomOffset < 0 || bottomOffset < minHeight)	{
												dropdown.height('auto').css({top: 'auto', bottom: position});
												if (dropdown.outerHeight() > topOffset - win.scrollTop() - 20 ) {
													dropdown.height(Math.floor((topOffset - win.scrollTop() - 20) / liHeight) * liHeight);
													if (visible > 0 && visible < 6) {
														if (dropdown.height() > minHeight) dropdown.height(minHeight);
													} else if (visible > 6) {
														if (dropdown.height() > newHeight) dropdown.height(newHeight);
													}
												}
											// раскрытие вниз
											} else if (bottomOffset > minHeight) {
												dropdown.height('auto').css({bottom: 'auto', top: position});
												if (dropdown.outerHeight() > bottomOffset - 20 ) {
													dropdown.height(Math.floor((bottomOffset - 20) / liHeight) * liHeight);
													if (visible > 0 && visible < 6) {
														if (dropdown.height() > minHeight) dropdown.height(minHeight);
													} else if (visible > 6) {
														if (dropdown.height() > newHeight) dropdown.height(newHeight);
													}
												}
											}
										}

										$('span.jqselect').css({zIndex: (opt.singleSelectzIndex-1)}).removeClass('focused');
										selectbox.css({zIndex: opt.singleSelectzIndex});
										if (dropdown.is(':hidden')) {
											$('div.dropdown:visible').hide();
											dropdown.show();
											selectbox.addClass('opened');
										} else {
											dropdown.hide();
											selectbox.removeClass('opened');
										}

										// прокручиваем до выбранного пункта при открытии списка
										if (li.filter('.selected').length) {
											dropdown.scrollTop(dropdown.scrollTop() + li.filter('.selected').position().top - dropdown.innerHeight()/2 + liHeight/2);
										}

										preventScrolling(dropdown);
										return false;
									});

									// при наведении курсора на пункт списка
									li.hover(function() {
										$(this).siblings().removeClass('selected');
									});
									var selectedText = li.filter('.selected').text();

									// при клике на пункт списка
									li.filter(':not(.disabled):not(.optgroup)').click(function() {
										var t = $(this);
										var liText = t.text();
										if (selectedText != liText) {
											var index = t.index();
											if (t.is('.option')) index -= t.prevAll('.optgroup').length;
											t.addClass('selected sel').siblings().removeClass('selected sel');
											option.prop('selected', false).eq(index).prop('selected', true);
											selectedText = liText;
											divText.text(liText);
											el.change();
										}
										dropdown.hide();
									});
									dropdown.mouseout(function() {
										$('li.sel', dropdown).addClass('selected');
									});

									// изменение селекта
									el.change(function() {
										divText.text(option.filter(':selected').text());
										li.removeClass('selected sel').not('.optgroup').eq(el[0].selectedIndex).addClass('selected sel');
									})
									.focus(function() {
										selectbox.addClass('focused');
									})
									.blur(function() {
										selectbox.removeClass('focused');
									})
									// прокрутки списка с клавиатуры
									.bind('keydown keyup', function(e) {
										divText.text(option.filter(':selected').text());
										li.removeClass('selected sel').not('.optgroup').eq(el[0].selectedIndex).addClass('selected sel');
										// вверх, влево, PageUp
										if (e.which == 38 || e.which == 37 || e.which == 33) {
											dropdown.scrollTop(dropdown.scrollTop() + li.filter('.selected').position().top);
										}
										// вниз, вправо, PageDown
										if (e.which == 40 || e.which == 39 || e.which == 34) {
											dropdown.scrollTop(dropdown.scrollTop() + li.filter('.selected').position().top - dropdown.innerHeight() + liHeight);
										}
										if (e.which == 13) {
											dropdown.hide();
										}
									});

									// прячем выпадающий список при клике за пределами селекта
									$(document).on('click', function(e) {
										// e.target.nodeName != 'OPTION' - добавлено для обхода бага в Опере
										// (при изменении селекта с клавиатуры срабатывает событие onclick)
										if (!$(e.target).parents().hasClass('selectbox') && e.target.nodeName != 'OPTION') {
											dropdown.hide().find('li.sel').addClass('selected');
											selectbox.removeClass('focused opened');
										}
									});
								}
							} // end doSelect()

							// мультиселект
							function doMultipleSelect() {
								var selectbox = $('<span' + id + ' class="jq-select-multiple jqselect' + cl + '" style="display: inline-block"></span>');
								el.after(selectbox).css({position: 'absolute', left: -9999});
								makeList();
								selectbox.append('<ul style="position: relative">' + list + '</ul>');
								var ul = $('ul', selectbox);
								var li = $('li', selectbox).attr('unselectable', 'on').css({'-webkit-user-select': 'none', '-moz-user-select': 'none', '-ms-user-select': 'none', '-o-user-select': 'none', 'user-select': 'none'});
								var size = el.attr('size');
								var ulHeight = ul.outerHeight();
								var liHeight = li.outerHeight();
								if (size !== undefined && size > 0) {
									ul.css({'height': liHeight * size});
								} else {
									ul.css({'height': liHeight * 4});
								}
								if (ulHeight > selectbox.height()) {
									ul.css('overflowY', 'scroll');
									preventScrolling(ul);
									// прокручиваем до выбранного пункта
									if (li.filter('.selected').length) {
										ul.scrollTop(ul.scrollTop() + li.filter('.selected').position().top);
									}
								}
								if (el.is(':disabled')) {
									selectbox.addClass('disabled');
									option.each(function() {
										if ($(this).is(':selected')) li.eq($(this).index()).addClass('selected');
									});
								} else {

									// при клике на пункт списка
									li.filter(':not(.disabled):not(.optgroup)').click(function(e) {
										el.focus();
										selectbox.removeClass('focused');
										var clkd = $(this);
										if(!e.ctrlKey) clkd.addClass('selected');
										if(!e.shiftKey) clkd.addClass('first');
										if(!e.ctrlKey && !e.shiftKey) clkd.siblings().removeClass('selected first');

										// выделение пунктов при зажатом Ctrl
										if(e.ctrlKey) {
											if (clkd.is('.selected')) clkd.removeClass('selected first');
												else clkd.addClass('selected first');
											clkd.siblings().removeClass('first');
										}

										// выделение пунктов при зажатом Shift
										if(e.shiftKey) {
											var prev = false,
													next = false;
											clkd.siblings().removeClass('selected').siblings('.first').addClass('selected');
											clkd.prevAll().each(function() {
												if ($(this).is('.first')) prev = true;
											});
											clkd.nextAll().each(function() {
												if ($(this).is('.first')) next = true;
											});
											if (prev) {
												clkd.prevAll().each(function() {
													if ($(this).is('.selected')) return false;
														else $(this).not('.disabled, .optgroup').addClass('selected');
												});
											}
											if (next) {
												clkd.nextAll().each(function() {
													if ($(this).is('.selected')) return false;
														else $(this).not('.disabled, .optgroup').addClass('selected');
												});
											}
											if (li.filter('.selected').length == 1) clkd.addClass('first');
										}

										// отмечаем выбранные мышью
										option.prop('selected', false);
										li.filter('.selected').each(function() {
											var t = $(this);
											var index = t.index();
											if (t.is('.option')) index -= t.prevAll('.optgroup').length;
											option.eq(index).prop('selected', true);
										});
										el.change();

									});

									// отмечаем выбранные с клавиатуры
									option.each(function(i) {
										$(this).data('optionIndex', i);
									});
									el.change(function() {
										li.removeClass('selected');
										var arrIndexes = [];
										option.filter(':selected').each(function() {
											arrIndexes.push($(this).data('optionIndex'));
										});
										li.not('.optgroup').filter(function(i) {
											return $.inArray(i, arrIndexes) > -1;
										}).addClass('selected');
									})
									.focus(function() {
										selectbox.addClass('focused');
									})
									.blur(function() {
										selectbox.removeClass('focused');
									});

									// прокручиваем с клавиатуры
									if (ulHeight > selectbox.height()) {
										el.keydown(function(e) {
											// вверх, влево, PageUp
											if (e.which == 38 || e.which == 37 || e.which == 33) {
												ul.scrollTop(ul.scrollTop() + li.filter('.selected').position().top - liHeight);
											}
											// вниз, вправо, PageDown
											if (e.which == 40 || e.which == 39 || e.which == 34) {
												ul.scrollTop(ul.scrollTop() + li.filter('.selected:last').position().top - ul.innerHeight() + liHeight*2);
											}
										});
									}

								}
							} // end doMultipleSelect()
							if (el.is('[multiple]')) doMultipleSelect(); else doSelect();
						} // end selectbox()

						selectbox();

						// обновление при динамическом изменении
						el.on('refresh', function() {
							el.next().remove();
							selectbox();
						});
					}
				});
			}// end select

		});

	}
})(jQuery);