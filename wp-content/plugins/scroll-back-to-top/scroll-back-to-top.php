<?php
/*
Plugin Name: Scroll Back to Top
Description: Adds a scroll to top button
Plugin URI: http://www.webtipblog.com/scroll-back-top-wordpress-plugin/
Text Domain: scroll-back-to-top
Version: 1.1.3
Author: Joe Sexton
Author URI: http://www.josephmsexton.com
*/

require_once 'framework/JmsBootstrap.php';
$wpClassLoader = new JmsBootstrap( __FILE__, __( 'Scroll Back to Top', 'scroll-back-to-top') );