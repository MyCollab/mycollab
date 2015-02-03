<h2>Troubleshooting</h2>

It is possible for conflicts to be caused if your theme uses a version of font-awesome that is different
from the version enqueued by the plugin.  There are 2 solutions to this problem.

The first solution is to set the plugin to use "Custom Text" instead of an icon as the label.  This may not
be the most ideal solution, but if no icons are used then the plugin will not try to include the font-awesome
library and will not cause a conflict.

A second solution is a little more code intensive.  The plugin offers a filter for the HTML output of the
button.  The idea is to set the plugin to use "Custom Text" but then set the "Custom Label Text" text box
to contain no text at all.  This will not load the plugin's version of font-awesome and will render an empty
button.  The next step is to apply the filter in your theme's functions.php file to add the font-awesome icon
manually.  Here is an example of a code snippet you could include in functions.php to insert an icon into the
button's "Custom Label" area.  This will use your theme's version of font-awesome and cause no conflicts.


<pre class="lang:php decode:true " >add_filter('sbtt_button_markup', 'my_scroll_back_to_top_filter');
function my_scroll_back_to_top_filter($text) {
  $text = str_replace(
    '&lt;span class="scroll-back-to-top-inner"&gt;',
    '&lt;span class="scroll-back-to-top-inner"&gt;&lt;i class="fa fa-arrow-circle-up"&gt;&lt;/i&gt;',
    $text
  );

  return $text;
}</pre>


The '&lt;i class="fa fa-arrow-circle-up"&gt;&lt;/i&gt;' is the icon for font-awesome.  To see more options,
including the icons I included in the plugin, go to <a href="http://fortawesome.github.io/Font-Awesome/icons/" target="_blank">http://fortawesome.github.io/Font-Awesome/icons/</a>.  I should
also note that this is the recommended method to add your own icons from any icon library if needed to match a theme.