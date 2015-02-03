(function ($) {
    $.fn.mobileMenu = function (options) {

        var defaults = {
            defaultText: 'Navigate to...',
            className: 'select-menu',
            subMenuClass: 'sub-menu',
            subMenuDash: '&ndash;'
        },
            settings = $.extend(defaults, options),
            el = $(this);
        i = 0;
        this.each(function () {
            // ad class to submenu list
            if ($(this).parents('aside').length > 0) return;
            i++;
            el.find('ul.children').addClass(settings.subMenuClass);
            $(this).attr('id', 'main-menu-' + i);
            // Create base menu
            $('<select />', {
                'id': 'mobile-menu-' + i,
                'class': settings.className
            }).insertAfter($('#main-menu-' + i));

            // Create default option
            $('<option />', {
                "value": '#',
                "text": settings.defaultText
            }).appendTo('#' + 'mobile-menu-' + i);

            // Create select option from menu
            $('#main-menu-' + i).find('a').each(function () {
                var $this = '',
                    optText = '',
                    optSub = '',
                    len = '',
                    dash = '';

                $this = $(this), optText = '&nbsp;' + $this.text(), optSub = $this.parents('.' + settings.subMenuClass), len = optSub.length;


                // if menu has sub menu
                if ($this.parents('ul').hasClass(settings.subMenuClass)) {
                    dash = Array(len + 1).join(settings.subMenuDash);
                    optText = dash + optText;
                }

                // Now build menu and append it
                $('<option />', {
                    "value": this.href,
                    "html": optText,
                    "selected": (this.href == window.location.href)
                }).appendTo('#' + 'mobile-menu-' + i);

            }); // End el.find('a').each
            // Change event on select element
            $('.' + settings.className).change(function () {
                var locations = $(this).val();
                if (locations !== '#') {
                    window.location.href = $(this).val();
                };
            });

        });
        return this;
    };
})(jQuery);