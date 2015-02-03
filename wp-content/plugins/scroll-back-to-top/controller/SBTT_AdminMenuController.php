<?php
/**
 * SBTT Admin Menu Controller class
 *
 * @author Joe Sexton <joe@josephmsexton.com>
 * @package WordPress
 * @subpackage scroll-back-to-top
 * @version 1.1
 * @uses JmsController
 * @uses JmsAdminSettingsPage
 * @uses JmsUserOptionsCollection
 */
if ( !class_exists( 'SBTT_AdminMenuController' ) ){
class SBTT_AdminMenuController extends JmsAdminSettingsPage {

  /**
   * @var float
   */
  protected $version = 1.1;

	/**
	 * register Wordpress actions and filters
	 */
	protected function _init() {
    $this->_upgrade();

		$options = new SBTT_Options();
		$this->addOptionsPage(
			__( 'Scroll Back to Top Settings', $this->textDomain() ),
			__( 'Scroll Back to Top', $this->textDomain() ),
			$options
		);

    // activate plugin page extra links
    add_filter("plugin_action_links_{$this->pluginBase}", array( $this, 'activatePluginPageLinksNameSection' ) );
    add_filter('plugin_row_meta', array($this, 'activatePluginPageLinksDescriptionSection'),10,2);
  }

  /**
   * Plugin upgrader
   */
  protected function _upgrade() {
    $options = new SBTT_Options();
    $options->initOptions();
    $defaults = $options->defaultOptions();

    $key       = $options->optionsKey();
    $wp_option = get_option( $key, array() );

    // v1.1 adds a few new options to the settings menu, init the default values.
    if (
      ( isset($wp_option['version'] ) && $wp_option['version'] < 1.1 ) ||
      !isset( $wp_option['version'] )
    ) {

      $wp_option['version'] = $options->getVersion();

      if ( !isset( $wp_option['min_resolution'] ) ) {
        $wp_option['min_resolution'] = isset( $defaults['min_resolution'] ) ? $defaults['min_resolution'] : 0;
      }
      if ( !isset( $wp_option['max_resolution'] ) ) {
        $wp_option['max_resolution'] = isset( $defaults['max_resolution'] ) ? $defaults['max_resolution'] : 9999;
      }
      if ( !isset( $wp_option['visibility_duration'] ) ) {
        $wp_option['visibility_duration'] = isset( $defaults['visibility_duration'] ) ? $defaults['visibility_duration'] : 0;
      }
      if ( !isset( $wp_option['color_foreground_hover'] ) && isset( $wp_option['color_foreground'] ) ) {
        $wp_option['color_foreground_hover'] = $wp_option['color_foreground'];
      } elseif ( !isset( $wp_option['color_foreground_hover'] ) ) {
        $wp_option['color_foreground_hover'] = isset( $defaults['color_foreground_hover'] ) ? $defaults['color_foreground_hover'] : '#eeeeee';
      }

      update_option($key, $wp_option);
    }
  }

	/**
	 * enqueue admin scripts
	 */
	public function enqueueAdminScripts() {

		$this->enqueueScript( 'sbtt-admin', 'admin' );
	}

	/**
	 * enqueue admin styles
	 */
	public function enqueueAdminStyles() {
		$this->enqueueCdnStyle( 'font-awesome', '//netdna.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.css' );
	}

	/**
	 * on plugin activation init default options
	 */
	public function onActivation() {

		$this->options->initOptions();
	}

  /**
   * Add links to plugin page plugin area
   *
   * @param array $links
   *   Links.
   *
   * @return array
   */
  public function activatePluginPageLinksNameSection( $links ) {

    $settings_links = array(
      "<a href='options-general.php?page={$this->pluginSlug}'>" . __( 'Settings', $this->textDomain() ) . "</a>"
    );
    $links = array_merge($links, $settings_links);

    return $links;
  }

  /**
   * Add links to plugin page description area
   *
   * @param array $links
   * @param string $file
   * @return array
   */
  public function activatePluginPageLinksDescriptionSection( $links, $file ) {

    if ( $file == $this->pluginBase ) {
      $links[] = '<a href="http://www.webtipblog.com/scroll-back-top-wordpress-plugin-button-designs/" target="_blank">' . __( 'Design Inspiration', $this->textDomain() ) . '</a>';
      $links[] = '<a href="https://wordpress.org/plugins/scroll-back-to-top/" target="_blank">' . __( 'Wordpress Plugin Page', $this->textDomain() ) . '</a>';
    }

    return $links;
  }

  /**
   * Render contextual help menu for an admin page
   *
   * @param WP_Screen $screen
   * @return string
   */
  protected function _renderContextualHelp(WP_Screen $screen) {

    $screen->add_help_tab( array(
      'id'      => 'overview',
      'title'   => __( 'Overview', $this->textDomain() ),
      'content' => $this->render('help:overview', array(), false),
    ) );

    $screen->add_help_tab( array(
      'id'      => 'visibility',
      'title'   => __( 'Visibility', $this->textDomain() ),
      'content' => $this->render('help:visibility', array(), false),
    ) );

    $screen->add_help_tab( array(
      'id'      => 'appearance',
      'title'   => __( 'Button Appearance', $this->textDomain() ),
      'content' => $this->render('help:appearance', array(), false),
    ) );

    $screen->add_help_tab( array(
      'id'      => 'location',
      'title'   => __( 'Button Location', $this->textDomain() ),
      'content' => $this->render('help:location', array(), false),
    ) );

    $screen->add_help_tab( array(
      'id'      => 'label',
      'title'   => __( 'Button Label', $this->textDomain() ),
      'content' => $this->render('help:label', array(), false),
    ) );

    $screen->add_help_tab( array(
      'id'      => 'animation',
      'title'   => __( 'Animation Options', $this->textDomain() ),
      'content' => $this->render('help:animation', array(), false),
    ) );

    $screen->add_help_tab( array(
      'id'      => 'advanced',
      'title'   => __( 'Advanced Options', $this->textDomain() ),
      'content' => $this->render('help:advanced', array(), false),
    ) );

    $screen->add_help_tab( array(
      'id'      => 'troubleshooting',
      'title'   => __( 'Troubleshooting', $this->textDomain() ),
      'content' => $this->render('help:troubleshooting', array(), false),
    ) );

    return '';
  }
}
}