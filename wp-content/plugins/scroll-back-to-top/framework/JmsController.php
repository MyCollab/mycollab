<?php

/**
 * Controller
 * This is an abstract base controller that adds some handy properties and methods
 *
 * @author Joe Sexton <joe@josephmsexton.com>
 * @package WordPress
 * @subpackage JMS Plugin Framework
 * @version 1.2
 */
if ( !class_exists( 'JmsController' ) ){
	abstract class JmsController {

    /**
     * @var float
     */
    protected $version = 1.2;

		/**
		 * @var string
		 */
		protected $rootPath;

		/**
		 * @var string
		 */
		protected $fwPath;

		/**
		 * @var string
		 */
		protected $controllerPath;

		/**
		 * @var string
		 */
		protected $modelPath;

		/**
		 * @var string
		 */
		protected $viewPath;

		/**
		 * @var string
		 */
		protected $assetPath;

		/**
		 * @var string
		 */
		protected $pluginName;

		/**
		 * @var string
		 */
		protected $pluginSlug;

		/**
		 * @var string
		 */
		protected $pluginFile;

		/**
		 * constructor
		 *
		 * @param string $pluginFile
		 * @param string $pluginName
		 */
		public function __construct( $pluginFile, $pluginName = ''  ) {

			$rootPath = plugin_dir_path( $pluginFile );
			$rootUrl  = plugin_dir_url( $pluginFile );

			$this->rootPath = $rootPath;

			$this->fwPath         = $this->rootPath . 'framework/';
			$this->controllerPath = $this->rootPath . 'controller/';
			$this->modelPath      = $this->rootPath . 'model/';
			$this->viewPath       = $this->rootPath . 'view/';
			$this->assetPath      = $rootUrl . 'assets/';

			$this->pluginFile = $pluginFile;
			$this->pluginName = $pluginName;
      $this->pluginBase = plugin_basename($pluginFile);

			$pathParts        = explode( '/', $pluginFile );
			$filename         = array_pop( $pathParts );
			$filenameParts    = explode( '.', $filename );
			$this->pluginSlug = array_shift(  $filenameParts );

			add_action( 'wp_enqueue_scripts', array( $this, 'enqueueScripts' ) );
			add_action( 'wp_enqueue_scripts', array( $this, 'enqueueStyles' ) );
			add_action( 'admin_enqueue_scripts', array( $this, 'enqueueAdminScripts' ) );
			add_action( 'admin_enqueue_scripts', array( $this, 'enqueueAdminStyles' ) );

			register_activation_hook( $this->pluginFile, array( $this, 'onActivation' ) );
			register_deactivation_hook( $this->pluginFile, array( $this, 'onDeactivation' ) );

			$this->_init();
		}

		/**
		 * enqueue script
		 * enqueue script using : separators for dirs in assets directory
		 * ie. render('acme:test') will render /assets/js/acme/test.js
		 *
		 * @param string $handle
		 * @param string $script
		 * @param array $deps
		 * @param string $ver
		 * @param boolean $footer
		 * @param array $localizedVars
		 * @return boolean
		 */
		public function enqueueScript(
			$handle,
			$script,
			array $deps = array( 'jquery' ),
			$ver = null,
			$footer = true,
			array $localizedVars = array()
		) {

			$script = str_replace( ':', '/', $script );
			$file = $this->assetPath . 'js/' . $script . '.js';

			wp_register_script( $handle, $file, $deps, $ver, $footer );
			wp_enqueue_script( $handle );

			foreach ( $localizedVars as $name => $vars ) {

				wp_localize_script( $handle, $name, $vars );
			}

			return true;
		}

		/**
		 * enqueue style
		 * enqueue style using : separators for dirs in assets directory
		 * ie. render('acme:test') will render /assets/css/acme/test.css
		 *
		 * @param string $handle
		 * @param string $style
		 * @param array $deps
		 * @param string $ver
		 * @param string $media
		 * @return boolean
		 */
		public function enqueueStyle(
			$handle,
			$style,
			array $deps = array(),
			$ver = null,
			$media = 'screen'
		) {

			$style = str_replace( ':', '/', $style );
			$file = $this->assetPath . 'css/' . $style . '.css';

			wp_register_style( $handle, $file, $deps, $ver, $media );
			wp_enqueue_style( $handle );

			return true;
		}

		/**
		 * enqueue script
		 * enqueue script using a public web path
		 *
		 * @param string $handle
		 * @param string $path
		 * @param array $deps
		 * @param string $ver
		 * @param boolean $footer
		 * @param array $localizedVars
		 * @return boolean
		 */
		public function enqueueCdnScript(
			$handle,
			$path,
			array $deps = array( 'jquery' ),
			$ver = null,
			$footer = true,
			array $localizedVars = array()
		) {
			wp_register_script( $handle, $path, $deps, $ver, $footer );
			wp_enqueue_script( $handle );

			foreach ( $localizedVars as $name => $vars ) {

				wp_localize_script( $handle, $name, $vars );
			}

			return true;
		}

		/**
		 * enqueue style
		 * enqueue style using a public web path
		 *
		 * @param string $handle
		 * @param string $path
		 * @param array $deps
		 * @param string $ver
		 * @param string $media
		 * @return boolean
		 */
		public function enqueueCdnStyle(
			$handle,
			$path,
			array $deps = array(),
			$ver = null,
			$media = 'screen'
		) {
			wp_register_style( $handle, $path, $deps, $ver, $media );
			wp_enqueue_style( $handle );

			return true;
		}

		/**
		 * render view
		 * call view using : separators for dirs in view directory
		 * ie. render('acme:test') will render /view/acme/test.php
		 *
		 * @param string $view view template
		 * @param array $args arguments used in the view
		 * @param boolean $echo true=print false=return
		 * @return string|boolean
		 */
		function render( $view, array $args = array(), $echo = true ){

			$view      = str_replace( ':', '/', $view );
			$file      = $this->viewPath . $view . '.php';
			$assetPath = $this->assetPath;

			if( !empty( $args ) )
				extract( $args );

			if ( !file_exists( $file ) )
				return false;

			if ( $echo ) {
				include $file;

				return true;

			} else {
				ob_start();
				include $file;
				$output = ob_get_contents();
				ob_end_clean();

				return $output;
			}
		}

		/**
		 * render shortcode
		 * call view using : separators for dirs in view directory
		 * ie. renderShortcode('acme:test', $atts) will render /view/acme/test.php
		 *
		 * @param string $view view template
		 * @param array $atts attributes passed
		 * @param array $pairs supported/default attributes
		 * @param string $shortcode shortcode name
		 * @return string
		 */
		public function renderShortcode( $view, $atts, array $pairs = array(), $shortcode = '' ) {

			$view      = str_replace( ':', '/', $view );
			$file      = $this->viewPath . $view . '.php';
			$assetPath = $this->assetPath;

			extract( shortcode_atts( $pairs, $atts, $shortcode ) );

			if ( !file_exists( $file ) )
				return false;

			ob_start();
			include $file;
			$output = ob_get_contents();
			ob_end_clean();

			return $output;
		}

		/**
		 * add actions
		 */
		protected function _init() {}

		/**
		 * enqueue admin scripts
		 */
		public function enqueueScripts() {}

		/**
		 * enqueue admin styles
		 */
		public function enqueueStyles() {}

		/**
		 * enqueue admin scripts
		 */
		public function enqueueAdminScripts() {}

		/**
		 * enqueue admin styles
		 */
		public function enqueueAdminStyles() {}

		/**
		 * on activation
		 */
		public function onActivation() {}

		/**
		 * on deactivation
		 */
		public function onDeactivation() {}

		/**
		 * root path
		 *
		 * @return string
		 */
		public function rootPath(){

			return $this->rootPath;
		}

		/**
		 * fw path
		 *
		 * @return string
		 */
		public function fwPath(){

			return $this->fwPath;
		}

		/**
		 * controller path
		 *
		 * @return string
		 */
		public function controllerPath(){

			return $this->controllerPath;
		}

		/**
		 * model path
		 *
		 * @return string
		 */
		public function modelPath(){

			return $this->modelPath;
		}

		/**
		 * view path
		 *
		 * @return string
		 */
		public function viewPath(){

			return $this->viewPath;
		}

		/**
		 * asset path
		 *
		 * @return string
		 */
		public function assetPath(){

			return $this->assetPath;
		}

		/**
		 * plugin name
		 *
		 * @return string
		 */
		public function pluginName(){

			return $this->pluginName;
		}

		/**
		 * plugin slug
		 *
		 * @return string
		 */
		public function pluginSlug(){

			return $this->pluginSlug;
		}

		/**
		 * plugin file
		 *
		 * @return string
		 */
		public function pluginFile(){

			return $this->pluginFile;
		}

		/**
		 * text domain
		 *
		 * @return string
		 */
		public function textDomain(){

			return $this->pluginSlug;
		}

    /**
     * Get version
     *
     * @return float
     */
    public function getVersion() {
      return $this->version;
    }
	}
}