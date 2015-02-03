<?php
/*
Plugin Name: Google Analytics
Plugin URI: http://wordpress.org/extend/plugins/googleanalytics/
Description: Enables <a href="http://www.google.com/analytics/">Google Analytics</a> on all pages.
Version: 1.0.6
Author: Kevin Sylvestre
Author URI: http://ksylvest.com/
*/

if (!defined('WP_CONTENT_URL'))
      define('WP_CONTENT_URL', get_option('siteurl').'/wp-content');
if (!defined('WP_CONTENT_DIR'))
      define('WP_CONTENT_DIR', ABSPATH.'wp-content');
if (!defined('WP_PLUGIN_URL'))
      define('WP_PLUGIN_URL', WP_CONTENT_URL.'/plugins');
if (!defined('WP_PLUGIN_DIR'))
      define('WP_PLUGIN_DIR', WP_CONTENT_DIR.'/plugins');

function activate_googleanalytics() {
  add_option('web_property_id', 'UA-0000000-0');
}

function deactive_googleanalytics() {
  delete_option('web_property_id');
}

function admin_init_googleanalytics() {
  register_setting('googleanalytics', 'web_property_id');
}

function admin_menu_googleanalytics() {
  add_options_page('Google Analytics', 'Google Analytics', 'manage_options', 'googleanalytics', 'options_page_googleanalytics');
}

function options_page_googleanalytics() {
  include(WP_PLUGIN_DIR.'/googleanalytics/options.php');  
}

function googleanalytics() {
  $web_property_id = get_option('web_property_id');
?>
<script type="text/javascript">
var _gaq = _gaq || [];
_gaq.push(['_setAccount', '<?php echo $web_property_id ?>']);
_gaq.push(['_trackPageview']);
(function() {
var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
})();
</script>
<?php
}

register_activation_hook(__FILE__, 'activate_googleanalytics');
register_deactivation_hook(__FILE__, 'deactive_googleanalytics');

if (is_admin()) {
  add_action('admin_init', 'admin_init_googleanalytics');
  add_action('admin_menu', 'admin_menu_googleanalytics');
}

if (!is_admin()) {
  add_action('wp_head', 'googleanalytics');
}

?>
