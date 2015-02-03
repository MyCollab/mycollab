<?php
/**
 * Bootstrap
 * This class autoloads all classes in the plugin directory
 * This class will instantiate each controller class automatically
 *
 * @author Joe Sexton <joe@josephmsexton.com>
 * @package WordPress
 * @subpackage JMS Plugin Framework
 * @version 1.2
 */
if ( !class_exists( 'JmsBootstrap' ) ){
	class JmsBootstrap {

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
		 * @var array
		 */
		protected $objects = array();

		/**
		 * @var string
		 */
		protected $pluginName;

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
		public function __construct( $pluginFile, $pluginName = '' ) {

			$rootPath = plugin_dir_path( $pluginFile );

			if ( !$rootPath )
				$rootPath = dirname( __FILE__ );
			$this->rootPath = trailingslashit( $rootPath );

			$this->fwPath         = $this->rootPath . 'framework/';
			$this->controllerPath = $this->rootPath . 'controller/';
			$this->modelPath      = $this->rootPath . 'model/';

			$this->pluginFile = $pluginFile;
			$this->pluginName = $pluginName;
      $this->pluginBase = plugin_basename($pluginFile);

			spl_autoload_register( array( $this, '_autoload' ) );

			$this->initControllers();
		}

		/**
		 * autoload
		 *
		 * @param $class
		 */
		protected function _autoload( $class ){

			$this->_loadClassFile( $class );
		}

		/**
		 * load class file
		 * recursively searches all directories/subdirectories for class
		 * if class file is found, include()
		 *
		 * @param string $class
		 * @param string $dir
		 * @return bool
		 */
		protected function _loadClassFile( $class, $dir = null ) {

			if ( is_null( $dir ) )
				$dir = $this->rootPath;

			foreach ( scandir( $dir ) as $file ) {

				// directory?
				if ( is_dir( $dir . $file ) && substr( $file, 0, 1 ) !== '.' )
					$this->_loadClassFile( $class, $dir . $file . '/' );

				// php file and filename matches class?
				if (
					substr( $file, 0, 1 ) !== '.' &&
					preg_match( "/.php$/i" , $file ) &&
					(str_replace( '.php', '', $file ) == $class || str_replace( '.class.php', '', $file ) == $class )
				) {
					include $dir . $file;
				}
			}
		}

		/**
		 * init controllers
		 * recursively instantiates all controllers in the controllers directory/subdirectory
		 *
		 * @param string $dir
		 * @return boolean
		 */
		public function initControllers( $dir = null ) {

			if ( is_null( $dir ) )
				$dir = $this->controllerPath;

			if ( !is_dir( $dir ) )
				return false;

			foreach ( scandir( $dir ) as $file ) {

				// directory?
				if ( is_dir( $dir . $file ) && substr( $file, 0, 1 ) !== '.' )
					$this->initControllers( $dir . $file . '/' );

				// php file?
				if( substr( $file, 0, 1 ) !== '.' && preg_match( "/.php$/i" , $file ) ) {

					$class = str_replace( '.class.php', '', $file );
					$class = str_replace( '.php', '', $class );
					if ( class_exists( $class ) ) {
						$this->objects[$class] = new $class( $this->pluginFile, $this->pluginName );
					}
				}
			}

			return true;
		}

		/**
		 * get objects
		 *
		 * @return array
		 */
		public function getObjects() {

			return $this->objects;
		}

		/**
		 * get object
		 *
		 * @param string $class
		 * @return object|null
		 */
		public function getObject( $class ) {

			if ( !empty( $this->objects[$class] ) ) {

				return $this->objects[$class];
			}

			return null;
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