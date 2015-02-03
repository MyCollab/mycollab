<?php
/* ------------------------------
 *      XMLSitemapFeed CLASS
 * ------------------------------ */

class XMLSitemapFeed {

	/**
	* Plugin variables
	*/
	
	// Pretty permalinks base name
	public $base_name = 'sitemap';

	// Pretty permalinks extension
	public $extension = 'xml';
	
	// Database options prefix
	private $prefix = 'xmlsf_';
	
	// Flushed flag
	private $yes_mother = false;

	private $defaults = array();
	private $disabled_post_types = array('attachment'); /* attachment post type is disabled... images are included via tags in the post and page sitemaps */
	private $disabled_taxonomies = array('post_format'); /* post format taxonomy is brute force disabled for now; might come back... */
	private $gn_genres = array( 
				'gn-pressrelease' => 'PressRelease', 
				'gn-satire' => 'Satire', 
				'gn-blog' => 'Blog', 
				'gn-oped' => 'OpEd', 
				'gn-opinion' => 'Opinion', 
				'gn-usergenerated' => 'UserGenerated' 
				);
	
	// Global values used for priority and changefreq calculation
	private $domain;
	private $firstdate;
	private $lastmodified;
	private $postmodified = array();
	private $termmodified = array();
	private $blogpage;
	private $images = array();
						
	// make some private parts public ;)
	
	public function prefix() 
	{
		return $this->prefix;
	}

	public function gn_genres() 
	{
		return $this->gn_genres;
	}

	public function domain() 
	{
		// allowed domain
		if (empty($this->domain)) {
			$url_parsed = parse_url(home_url()); // second parameter PHP_URL_HOST for only PHP5 + ... 
			$this->domain = str_replace("www.","",$url_parsed['host']);
		}
		
		return $this->domain;
	}

	// default options
	private function build_defaults() 
	{
		// sitemaps
		if ( '1' == get_option('blog_public') )
			$this->defaults['sitemaps'] = array(
					'sitemap' => XMLSF_NAME
					);
		else
			$this->defaults['sitemaps'] = array();

		// post_types
		$this->defaults['post_types'] = array();
		foreach ( get_post_types(array('public'=>true),'names') as $name ) { // want 'publicly_queryable' but that excludes pages for some weird reason
			// skip unallowed post types
			if (in_array($name,$this->disabled_post_types))
				continue;

			$this->defaults['post_types'][$name] = array(
								'name' => $name,
								'active' => '',
								'archive' => '',
								'priority' => '0.5',
								'dynamic_priority' => '',
								'tags' => array('image' => 'attached'/*,'video' => ''*/)
								);
		}		

		if ( defined('XMLSF_POST_TYPE') && XMLSF_POST_TYPE != 'any' )
			$active_arr = array_map('trim',explode(',',XMLSF_POST_TYPE));
		else 
			$active_arr = array('post','page');
			
		foreach ( $active_arr as $name )
			if ( isset($this->defaults['post_types'][$name]) )
				$this->defaults['post_types'][$name]['active'] = '1';
		
		if ( isset($this->defaults['post_types']['post']) ) {
			if (wp_count_posts('post')->publish > 500)
				$this->defaults['post_types']['post']['archive'] = 'yearly';
			$this->defaults['post_types']['post']['priority'] = '0.7';
			$this->defaults['post_types']['post']['dynamic_priority'] = '1';
		}

		if ( isset($this->defaults['post_types']['page']) ) {
			unset($this->defaults['post_types']['page']['archive']);
			$this->defaults['post_types']['page']['priority'] = '0.3';
		}

		// taxonomies
		$this->defaults['taxonomies'] = array(); // by default do not include any taxonomies
		
		// news sitemap settings
		$this->defaults['news_sitemap'] = array();

		// ping search engines
		$this->defaults['ping'] = array(
					'google' => array (
						'active' => '1',
						'uri' => 'http://www.google.com/webmasters/tools/ping?sitemap=',
						'type' => 'GET'
						),
					'bing' => array (
						'active' => '1',
						'uri' => 'http://www.bing.com/ping?sitemap=',
						'type' => 'GET'
						),
					'yandex' => array (
						'active' => '',
						'uri' => 'http://ping.blogs.yandex.ru/RPC2',
						'type' => 'RPC'
						),
					'baidu' => array (
						'active' => '',
						'uri' => 'http://ping.baidu.com/ping/RPC2',
						'type' => 'RPC'
						),
					'others' => array (
						'active' => '1',
						'uri' => 'http://rpc.pingomatic.com/',
						'type' => 'RPC'
						),
					);

		$this->defaults['pong'] = array(); // for storing last ping timestamps and status

		// robots
		$this->defaults['robots'] = "Disallow: */xmlrpc.php\nDisallow: */wp-*.php\nDisallow: */trackback/\nDisallow: *?wptheme=\nDisallow: *?comments=\nDisallow: *?replytocom\nDisallow: */comment-page-\nDisallow: *?s=\nDisallow: */wp-content/\nAllow: */wp-content/uploads/\n";
		
		// additional urls
		$this->defaults['urls'] = array();

		// additional allowed domains
		$this->defaults['domains'] = array();
		
		// news sitemap tags settings
		$this->defaults['news_tags'] = array( 
						'name' => '', 
						'image' => 'featured',
						'access' => array( 
							'default' => '', 
							'private' => 'Registration', 
							'password' => 'Subscription' 
							), 
						'genres' => array( 
							'active' => '1', 
							'default' => '' 
							),
						'keywords' => array( 
							'from' => 'category', 
							'default' => '' 
							),
						'locations' => array( 
							'active' => '1', 
							'default' => '' 
							) 
						);
		
		
	}

	/**
	* QUERY FUNCTIONS
	*/
	
	public function defaults($key = false) 
	{
		if (empty($this->defaults))
			$this->build_defaults();

		if ($key) {
			$return = ( isset($this->defaults[$key]) ) ? $this->defaults[$key] : '';
		} else {
			$return = $this->defaults;
		}

		return apply_filters( 'xmlsf_defaults', $return, $key );
	}
	
	public function get_option($option) 
	{
		return get_option($this->prefix.$option, $this->defaults($option));
	}
	
	public function get_sitemaps() 
	{
		$return = $this->get_option('sitemaps');
		
		// make sure it's an array we are returning
		return (!empty($return)) ? (array)$return : array();
	}
	
	public function get_ping() 
	{		
		$return = $this->get_option('ping');
		
		// make sure it's an array we are returning
		return (!empty($return)) ? (array)$return : array();
	}
	
	public function get_pong() 
	{		
		$return = $this->get_option('pong');
		
		// make sure it's an array we are returning
		return (!empty($return)) ? (array)$return : array();
	}
	
	public function disabled_post_types() 
	{		
		return $this->disabled_post_types;

	}
	
	public function disabled_taxonomies() 
	{		
		return $this->disabled_taxonomies;

	}
	
	public function get_post_types() 
	{		
		$return = $this->get_option('post_types');

		// make sure it's an array we are returning
		return (!empty($return)) ? (array)$return : array();
	}

	public function have_post_types() 
	{		
		$post_types = $this->get_option('post_types');
		$return = array();

		foreach ( $post_types as $type => $values ) {
			if(!empty($values['active'])) {
				$count = wp_count_posts( $values['name'] );
				if ($count->publish > 0) {
					$values['count'] = $count->publish;
					$return[$type] = $values;
				}
			}
		}

		// make sure it's an array we are returning
		return (!empty($return)) ? (array)$return : array();
	}
		
	public function get_taxonomies() 
	{
		$return = $this->get_option('taxonomies');

		// make sure it's an array we are returning
		return (!empty($return)) ? (array)$return : array();
	}
		
	public function get_urls() 
	{
		$return = $this->get_option('urls');

		// make sure it's an array we are returning
		if(!empty($return)) {
			if(is_array($return))
				return $return;
			else
				return explode("\n",$return);
		} else {
			return array();
		}	
	}

	public function get_domains() 
	{
		$return = array_merge( array( $this->domain() ), (array)$this->get_option('domains') );
		
		// make sure it's an array we are returning
		return (!empty($return)) ? (array)$return : array();
	}

	public function get_archives($post_type = 'post', $type = '') 
	{
		global $wpdb;
		$return = array();
		if ( 'monthly' == $type ) {
			$query = "SELECT YEAR(post_date) AS `year`, LPAD(MONTH(post_date),2,'0') AS `month`, count(ID) as posts FROM $wpdb->posts WHERE post_type = '$post_type' AND post_status = 'publish' GROUP BY YEAR(post_date), MONTH(post_date) ORDER BY post_date DESC";			
			$key = md5($query);
			$cache = wp_cache_get( 'xmlsf_get_archives' , 'general');
			if ( !isset( $cache[ $key ] ) ) {
				$arcresults = $wpdb->get_results($query);
				$cache[ $key ] = $arcresults;
				wp_cache_set( 'xmlsf_get_archives', $cache, 'general' );
			} else {
				$arcresults = $cache[ $key ];
			}
			if ( $arcresults ) {
				foreach ( (array) $arcresults as $arcresult ) {
					$return[$arcresult->year.$arcresult->month] = $this->get_index_url( 'posttype', $post_type, $arcresult->year . $arcresult->month );
				}
			}
		} elseif ('yearly' == $type) {
			$query = "SELECT YEAR(post_date) AS `year`, count(ID) as posts FROM $wpdb->posts WHERE post_type = '$post_type' AND post_status = 'publish' GROUP BY YEAR(post_date) ORDER BY post_date DESC";
			$key = md5($query);
			$cache = wp_cache_get( 'xmlsf_get_archives' , 'general');
			if ( !isset( $cache[ $key ] ) ) {
				$arcresults = $wpdb->get_results($query);
				$cache[ $key ] = $arcresults;
				wp_cache_set( 'xmlsf_get_archives', $cache, 'general' );
			} else {
				$arcresults = $cache[ $key ];
			}
			if ($arcresults) {
				foreach ( (array) $arcresults as $arcresult) {
					$return[$arcresult->year] = $this->get_index_url( 'posttype', $post_type, $arcresult->year );
				}
			}
		} else {
			$return[0] = $this->get_index_url('posttype', $post_type); // $sitemap = 'home', $type = false, $param = false
		}
		return $return;
	}

	public function get_robots() 
	{
		return ( $robots = $this->get_option('robots') ) ? $robots : '';
	}

	public function do_tags( $type = 'post' ) 
	{
		$return = $this->get_option('post_types');

		// make sure it's an array we are returning
		return ( isset($return[$type]) && !empty($return[$type]['tags']) ) ? (array)$return[$type]['tags'] : array();
	}
	
	public function is_home($id) {
		
			if ( empty($this->blogpage) ) {
				$blogpage = get_option('page_for_posts');
				
				if ( !empty($blogpage) ) {
					global $polylang;
					if ( isset($polylang) )
						$this->blogpage = $polylang->get_translations('post', $blogpage);
					else
						$this->blogpage = array($blogpage);
				} else {
					$this->blogpage = array('-1');
				}
			}

			return in_array($id,$this->blogpage);
			
	}
		
	/**
	* TEMPLATE FUNCTIONS
	*/
	
	public function modified($sitemap = 'post_type', $term = '') 
	{
		if ('post_type' == $sitemap) :

			global $post;

			// if blog page look for last post date
			if ( $post->post_type == 'page' && $this->is_home($post->ID) )
				return get_lastmodified('GMT','post');

			if ( empty($this->postmodified[$post->ID]) ) {
				$postmodified = get_post_modified_time( 'Y-m-d H:i:s', true, $post->ID );
				$options = $this->get_option('post_types');

				if( !empty($options[$post->post_type]['update_lastmod_on_comments']) )
					$lastcomment = get_comments( array(
								'status' => 'approve',
								'number' => 1,
								'post_id' => $post->ID,
								) );

				if ( isset($lastcomment[0]->comment_date_gmt) )
					if ( mysql2date( 'U', $lastcomment[0]->comment_date_gmt ) > mysql2date( 'U', $postmodified ) )
						$postmodified = $lastcomment[0]->comment_date_gmt;
		
				$this->postmodified[$post->ID] = $postmodified;
			}
		
			return $this->postmodified[$post->ID];

		elseif ( !empty($term) ) :

			if ( is_object($term) ) { 
				if ( !isset($this->termmodified[$term->term_id]) ) {
				// get the latest post in this taxonomy item, to use its post_date as lastmod
					$posts = get_posts ( array(
						'post_type' => 'any',
					 	'numberposts' => 1, 
						'no_found_rows' => true, 
						'update_post_meta_cache' => false, 
						'update_post_term_cache' => false, 
						'update_cache' => false,
						'tax_query' => array(
								array(
									'taxonomy' => $term->taxonomy,
									'field' => 'slug',
									'terms' => $term->slug
								)
							)
						)
					);
					$this->termmodified[$term->term_id] = isset($posts[0]->post_date_gmt) ? $posts[0]->post_date_gmt : '';
				}
				return $this->termmodified[$term->term_id];
			} else {
				$obj = get_taxonomy($term);
				return get_lastdate( 'gmt', $obj->object_type );
				// uses get_lastdate() function defined in xml-sitemap/hacks.php !
				// which is a shortcut: returns last post date, not last modified date... 
				// TODO find the long way around (take tax type, get all terms, 
				// do tax_query with all terms for one post and get its lastmod date)
			}

		else :

			return '0000-00-00 00:00:00';

		endif;
	}

	public function get_images($sitemap = '') 
	{
		global $post;
		if ( empty($this->images[$post->ID]) ) {
			if ('news' == $sitemap) {
				$options = $this->get_option('news_tags');
				$which = isset($options['image']) ? $options['image'] : '';
			} else {
				$options = $this->get_option('post_types');
				$which = isset($options[$post->post_type]['tags']['image']) ? $options[$post->post_type]['tags']['image'] : '';
			}
			if('attached' == $which) {
				$args = array( 'post_type' => 'attachment', 'post_mime_type' => 'image', 'numberposts' => -1, 'post_status' =>'inherit', 'post_parent' => $post->ID );
				$attachments = get_posts($args);
				if ($attachments) {
					foreach ( $attachments as $attachment ) {
						$url = wp_get_attachment_image_src( $attachment->ID, 'full' );
						$this->images[$post->ID][] = array( 
										'loc' => esc_url( $url[0] ),
										'title' => apply_filters( 'the_title_xmlsitemap', $attachment->post_title ),
										'caption' => apply_filters( 'the_title_xmlsitemap', $attachment->post_excerpt )
										);
					}
				}
			} elseif ('featured' == $which) {
				if (has_post_thumbnail( $post->ID ) ) {
					$attachment = get_post(get_post_thumbnail_id( $post->ID ));
					$url = wp_get_attachment_image_src( $attachment->ID, 'full' );
					$this->images[$post->ID][] =  array( 
										'loc' => esc_url( $url[0] ),
										'title' => apply_filters( 'the_title_xmlsitemap', $attachment->post_title ),
										'caption' => apply_filters( 'the_title_xmlsitemap', $attachment->post_excerpt )
										);
				}
			}
		}
		return ( isset($this->images[$post->ID]) ) ? $this->images[$post->ID] : false;
	}
	
	public function get_lastmod($sitemap = 'post_type', $term = '') 
	{
		$return = trim(mysql2date('Y-m-d\TH:i:s+00:00', $this->modified($sitemap,$term), false));
		return !empty($return) ? '<lastmod>'.$return.'</lastmod>' : '';
	}

	public function get_changefreq($sitemap = 'post_type', $term = '') 
	{
		$modified = trim($this->modified($sitemap,$term));

		if (empty($modified))
			return 'weekly';
		
		$lastactivityage = ( gmdate('U') - mysql2date( 'U', $modified ) ); // post age
	 	
	 	if ( ($lastactivityage/86400) < 1 ) { // last activity less than 1 day old 
	 		$changefreq = 'hourly';
	 	} else if ( ($lastactivityage/86400) < 7 ) { // last activity less than 1 week old 
	 		$changefreq = 'daily';
	 	} else if ( ($lastactivityage/86400) < 30 ) { // last activity less than one month old 
	 		$changefreq = 'weekly';
	 	} else if ( ($lastactivityage/86400) < 365 ) { // last activity less than 1 year old 
	 		$changefreq = 'monthly';
	 	} else {
	 		$changefreq = 'yearly'; // over a year old...
	 	} 

	 	return $changefreq;
	}

	public function get_priority($sitemap = 'post_type', $term = '') 
	{
		if ( 'post_type' == $sitemap ) :
			global $post;
			$options = $this->get_option('post_types');
			$defaults = $this->defaults('post_types');
			$priority_meta = get_metadata('post', $post->ID, '_xmlsf_priority' , true);
		
			if ( !empty($priority_meta) || $priority_meta == '0' ) {
		
				$priority = $priority_meta;
			
			} elseif ( !empty($options[$post->post_type]['dynamic_priority']) ) {

				$post_modified = mysql2date('U',$post->post_modified_gmt);
		
				if ( empty($this->lastmodified) )
					$this->lastmodified = mysql2date('U',get_lastmodified('GMT',$post->post_type)); 
					// last posts or page modified date in Unix seconds 
					// uses get_lastmodified() function defined in xml-sitemap/hacks.php !
			
				if ( empty($this->firstdate) )
					$this->firstdate = mysql2date('U',get_firstdate('GMT',$post->post_type)); 
					// uses get_firstdate() function defined in xml-sitemap/hacks.php !
			
				if ( isset($options[$post->post_type]['priority']) )
					$priority_value = $options[$post->post_type]['priority'];
				else
					$priority_value = $defaults[$post->post_type]['priority'];
		
				// reduce by age
				// NOTE : home/blog page gets same treatment as sticky post
				if ( is_sticky($post->ID) || $this->is_home($post->ID) )
					$priority = $priority_value;
				else
					$priority = ( $this->lastmodified > $this->firstdate ) ? $priority_value - $priority_value * ( $this->lastmodified - $post_modified ) / ( $this->lastmodified - $this->firstdate ) : $priority_value;

				if ( $post->comment_count > 0 )
					$priority = $priority + 0.1 + ( 0.9 - $priority ) * $post->comment_count / wp_count_comments($post->post_type)->approved;

				// and a final trim for cases where we end up above 1 (sticky posts with many comments)
				if ($priority > 1) 
					$priority = 1;

			} else {

				$priority = ( isset($options[$post->post_type]['priority']) && is_numeric($options[$post->post_type]['priority']) ) ? $options[$post->post_type]['priority'] : $defaults[$post->post_type]['priority'];
		
			}

		elseif ( ! empty($term) ) :

			$max_priority = 0.4;
			$min_priority = 0.0;
			// TODO make these values optional

			$tax_obj = get_taxonomy($term->taxonomy);
			$postcount = 0;
			foreach ($tax_obj->object_type as $post_type) {
				$_post_count = wp_count_posts($post_type);
				$postcount += $_post_count->publish;
			}

			$priority = ( $postcount > 0 ) ? $min_priority + ( $max_priority * $term->count / $postcount ) : $min_priority;

		else :

			$priority = 0.5;

		endif;
		
		return number_format($priority,1);
	}

	public function get_home_urls() 
	{
		$urls = array();
		
		global $polylang,$q_config;

		if ( isset($polylang) )			
			foreach ($polylang->get_languages_list() as $term)
		    		$urls[] = $polylang->get_home_url($term);
		else
			$urls[] = home_url();
		
		return $urls;
	}

	public function get_excluded($post_type) 
	{
		$exclude = array();
		
		if ( $post_type == 'page' && $id = get_option('page_on_front') ) {
			global $polylang;
			if ( isset($polylang) )
				$exclude += $polylang->get_translations('post', $id);
			else 
				$exclude[] = $id;
		}
		
		return $exclude;
	}

	public function is_allowed_domain($url) 
	{
		$domains = $this->get_domains();
		$return = false;
		$parsed_url = parse_url($url);

		if (isset($parsed_url['host'])) { 
			foreach( $domains as $domain ) {
				if( $parsed_url['host'] == $domain || strpos($parsed_url['host'],".".$domain) !== false ) {
					$return = true;
					break;
				}
			}
		}
		
		return apply_filters( 'xmlsf_allowed_domain', $return );
	}

	public function get_index_url( $sitemap = 'home', $type = false, $param = false ) 
	{
		$root =  esc_url( trailingslashit(home_url()) );		
		$name = $this->base_name.'-'.$sitemap;
				
		if ( $type )
			$name .= '-'.$type;			

		if ( '' == get_option('permalink_structure') || '1' != get_option('blog_public')) {
			$name = '?feed='.$name;
			$name .= $param ? '&m='.$param : '';
		} else {
			$name .= $param ? '.'.$param : '';
			$name .= '.'.$this->extension;
		}
		
		return $root . $name;
	}
	

	/**
	* ROBOTSTXT 
	*/

	// add sitemap location in robots.txt generated by WP
	public function robots($output) 
	{		
		echo "\n# XML Sitemap & Google News Feeds version ".XMLSF_VERSION." - http://status301.net/wordpress-plugins/xml-sitemap-feed/";

		if ( '1' != get_option('blog_public') ) {
			echo "\n# XML Sitemaps are disabled. Please see Site Visibility on Settings > Reading.";
		} else {
			foreach ( $this->get_sitemaps() as $pretty ) 
				echo "\nSitemap: " . trailingslashit(get_bloginfo('url')) . $pretty;

			if ( empty($pretty) )
				echo "\n# No XML Sitemaps are enabled. Please see XML Sitemaps on Settings > Reading.";
		}
		echo "\n\n";
	}
	
	// add robots.txt rules
	public function robots_txt($output) 
	{
		return $output . $this->get_option('robots') ;
	}
	
	/**
	* REWRITES
	*/

	/**
	 * Remove the trailing slash from permalinks that have an extension,
	 * such as /sitemap.xml (thanks to Permalink Editor plugin for WordPress)
	 *
	 * @param string $request
	 */
	 
	public function trailingslash($request) 
	{
		if (pathinfo($request, PATHINFO_EXTENSION)) {
			return untrailingslashit($request);
		}
		return $request; // trailingslashit($request);
	}

	/**
	 * Add sitemap rewrite rules
	 *
	 * @param string $wp_rewrite
	 */
	 
	public function rewrite_rules($wp_rewrite) 
	{
		$xmlsf_rules = array();
		$sitemaps = $this->get_sitemaps();

		foreach ( $sitemaps as $name => $pretty )
			$xmlsf_rules[ preg_quote($pretty) . '$' ] = $wp_rewrite->index . '?feed=' . $name;

		if (!empty($sitemaps['sitemap'])) {
			// home urls
			$xmlsf_rules[ $this->base_name . '-home\.' . $this->extension . '$' ] = $wp_rewrite->index . '?feed=sitemap-home';
		
			// add rules for post types (can be split by month or year)
			foreach ( $this->get_post_types() as $post_type ) {
				if ( isset($post_type['active']) && '1' == $post_type['active'] )
					$xmlsf_rules[ $this->base_name . '-posttype-' . $post_type['name'] . '\.([0-9]+)?\.?' . $this->extension . '$' ] = $wp_rewrite->index . '?feed=sitemap-posttype-' . $post_type['name'] . '&m=$matches[1]';
			}

			// add rules for taxonomies
			foreach ( $this->get_taxonomies() as $taxonomy ) {
				$xmlsf_rules[ $this->base_name . '-taxonomy-' . $taxonomy . '\.' . $this->extension . '$' ] = $wp_rewrite->index . '?feed=sitemap-taxonomy-' . $taxonomy;
			}

			$urls = $this->get_urls();
			if(!empty($urls))
				$xmlsf_rules[ $this->base_name . '-custom\.' . $this->extension . '$' ] = $wp_rewrite->index . '?feed=sitemap-custom';

		}
		
		$wp_rewrite->rules = $xmlsf_rules + $wp_rewrite->rules;
	}
	
	/**
	* REQUEST FILTER
	*/
	public function template( $theme ) {

		if ( isset($request['feed']) && strpos($request['feed'],'sitemap') == 0 )
			// clear get_template response to prevent themes functions.php (another source of blank line problems) from loading
			return '';
		else
			return $theme;
	}

	public function filter_request( $request ) 
	{
		if ( isset($request['feed']) && strpos($request['feed'],'sitemap') == 0 ) {

			if ( $request['feed'] == 'sitemap' ) {
				
				// setup actions and filters
				add_action('do_feed_sitemap', array($this, 'load_template_index'), 10, 1);

				return $request;
			}

			if ( $request['feed'] == 'sitemap-news' ) {
				// disable caching
				define( 'DONOTCACHEPAGE', 1 ); // wp super cache -- or does super cache always clear feeds after new posts??
				// TODO w3tc
				
				// setup actions and filters
				add_action('do_feed_sitemap-news', array($this, 'load_template_news'), 10, 1);
				add_filter('post_limits', array($this, 'filter_news_limits') );
				add_filter('posts_where', array($this, 'filter_news_where'), 10, 1);

				// modify request parameters
				$types_arr = explode(',',XMLSF_NEWS_POST_TYPE);
				$request['post_type'] = (in_array('any',$types_arr)) ? 'any' : $types_arr;
				
				// include post status private at some point?
				// $request['post_status'] = array( 'publish', 'private' );
				// for now only publish:
				$request['post_status'] = 'publish';
				
				$request['no_found_rows'] = true;
				$request['update_post_meta_cache'] = false;
				//$request['update_post_term_cache'] = false; // << TODO test: can we disable or do we need this for terms?

				return $request;
			}

			if ( $request['feed'] == 'sitemap-home' ) {
				// setup actions and filters
				add_action('do_feed_sitemap-home', array($this, 'load_template_base'), 10, 1);

				return $request;
			}

			if ( strpos($request['feed'],'sitemap-posttype') == 0 ) {
				foreach ( $this->get_post_types() as $post_type ) {
					if ( $request['feed'] == 'sitemap-posttype-'.$post_type['name'] ) {
						// setup actions and filters
						add_action('do_feed_sitemap-posttype-'.$post_type['name'], array($this, 'load_template'), 10, 1);
						add_filter( 'post_limits', array($this, 'filter_limits') );

						// modify request parameters
						$request['post_type'] = $post_type['name'];
						$request['post_status'] = 'publish';
						$request['orderby'] = 'modified';
						$request['lang'] = '';
						$request['no_found_rows'] = true;
						$request['update_post_meta_cache'] = false;
						$request['update_post_term_cache'] = false;
						/*if ('attachment' == $post_type['name']) {
							$request['post_status'] = 'inherit';
							$request['post_mime_type'] = 'image,audio'; // ,video,audio
						}*/

						return $request;
					}
				}
			}

			if ( strpos($request['feed'],'sitemap-taxonomy') == 0 ) {
				foreach ( $this->get_taxonomies() as $taxonomy ) {
					if ( $request['feed'] == 'sitemap-taxonomy-'.$taxonomy ) {
						// setup actions and filters
						add_action('do_feed_sitemap-taxonomy-'.$taxonomy, array($this, 'load_template_taxonomy'), 10, 1);

						// modify request parameters
						$request['taxonomy'] = $taxonomy;
						$request['lang'] = '';
						$request['no_found_rows'] = true;
						$request['update_post_meta_cache'] = false;
						$request['update_post_term_cache'] = false;
						$request['post_status'] = 'publish';

						return $request;
					}
				}
			}

			if ( strpos($request['feed'],'sitemap-custom') == 0 ) {
				// setup actions and filters
				add_action('do_feed_sitemap-custom', array($this, 'load_template_custom'), 10, 1);

				return $request;
			}

		}

		return $request;
	}

	/**
	* FEED TEMPLATES
	*/

	// set up the sitemap index template
	public function load_template_index() 
	{
		load_template( XMLSF_PLUGIN_DIR . '/includes/feed-sitemap.php' );
	}

	// set up the sitemap home page(s) template
	public function load_template_base() 
	{
		load_template( XMLSF_PLUGIN_DIR . '/includes/feed-sitemap-home.php' );
	}

	// set up the post types sitemap template
	public function load_template() 
	{
		load_template( XMLSF_PLUGIN_DIR . '/includes/feed-sitemap-post_type.php' );
	}

	// set up the taxonomy sitemap template
	public function load_template_taxonomy() 
	{
		load_template( XMLSF_PLUGIN_DIR . '/includes/feed-sitemap-taxonomy.php' );
	}

	// set up the news sitemap template
	public function load_template_news() 
	{
		load_template( XMLSF_PLUGIN_DIR . '/includes/feed-sitemap-news.php' );
	}

	// set up the news sitemap template
	public function load_template_custom() 
	{
		load_template( XMLSF_PLUGIN_DIR . '/includes/feed-sitemap-custom.php' );
	}

	/**
	* LIMITS
	*/

	// override default feed limit
	public function filter_limits( $limits ) 
	{
		return 'LIMIT 0, 50000';
	}

	// override default feed limit for taxonomy sitemaps
	public function filter_limits_taxonomy( $limits ) 
	{
		return 'LIMIT 0, 1';
	}

	// override default feed limit for GN
	public function filter_news_limits( $limits ) 
	{
		return 'LIMIT 0, 1000';
	}

	// Create a new filtering function that will add a where clause to the query,
	// used for the Google News Sitemap
	public function filter_news_where( $where = '' ) 
	{
		// only posts from the last 2 days
		return $where . " AND post_date > '" . date('Y-m-d H:i:s', strtotime('-49 hours')) . "'";
	}
		

	/**
	* PINGING
	*/

	public function ping($uri, $timeout = 3) 
	{
		$options = array();
		$options['timeout'] = $timeout;

		$response = wp_remote_request( $uri, $options );

		if ( '200' == wp_remote_retrieve_response_code($response) )
			$succes = true;
		else
			$succes = false;	

		return $succes;
	}

	public function do_pings($new_status, $old_status, $post) 
	{
		// first check if we've got a post type that is included in our sitemap
		foreach($this->get_option('post_types') as $post_type)
			if( $post->post_type == $post_type['name'] ) {
				$active = true; // got a live one, green light is on.
				break;
			}
		if ( !isset($active) )
			return;
		
		if ( $old_status != 'publish' && $new_status == 'publish' ) {
			// Post is published from any other status
			$sitemaps = $this->get_sitemaps();
			foreach ($this->get_ping() as $se => $data)
				if( !empty($data['active']) && '1' == $data['active'])
					foreach ( $sitemaps as $pretty )
						if ( $this->ping( $data['uri'].urlencode(trailingslashit(get_bloginfo('url')) . $pretty) ) ) {
							$pong = $this->get_pong();
							$pong[$se][$pretty] = mysql2date('Y-m-d H:i:s', 'now', false);
							update_option($this->prefix.'pong',$pong);
						}
		}
		/*
		if ( $old_status == 'publish' && $new_status == 'publish' ) {
			// Post is updated
			// TODO make pinging in this case optional ... later, maybe
		}
		*/	
		// see more on http://codex.wordpress.org/Post_Status_Transitions
	}

	/**
	* DE-ACTIVATION
	*/

	public function clear_settings() 
	{
		delete_option('xmlsf_version');
		foreach ( $this->defaults() as $option => $settings ) {
			delete_option('xmlsf_'.$option);
		}
		
		if(!term_exists('gn-genre') || !term_exists('gn-location-1') || !term_exists('gn-location-2') || !term_exists('gn-location-3'))
			$this->register_gn_taxonomies();

		$terms = get_terms('gn-genre',array('hide_empty' => false));
		foreach ( $terms as $term ) {
			wp_delete_term(	$term->term_id, 'gn-genre' );
		}
		$terms = get_terms('gn-location-1',array('hide_empty' => false));
		foreach ( $terms as $term ) {
			wp_delete_term(	$term->term_id, 'gn-genre' );
		}
		$terms = get_terms('gn-location-2',array('hide_empty' => false));
		foreach ( $terms as $term ) {
			wp_delete_term(	$term->term_id, 'gn-genre' );
		}
		$terms = get_terms('gn-location-3',array('hide_empty' => false));
		foreach ( $terms as $term ) {
			wp_delete_term(	$term->term_id, 'gn-genre' );
		}

		remove_action('generate_rewrite_rules', array($this, 'rewrite_rules') );
		global $wp_rewrite;
		$wp_rewrite->flush_rules();
	}

	/**
	* INITIALISATION
	*/

	public function plugins_loaded() 
	{
		// TEXT DOMAIN
		if ( is_admin() ) // text domain needed on admin only
			load_plugin_textdomain('xml-sitemap-feed', false, dirname(dirname(plugin_basename( __FILE__ ))) . '/languages' );

		// UPGRADE
		if (get_option('xmlsf_version') != XMLSF_VERSION) {
			// rewrite rules not available on plugins_loaded 
			// and don't flush rules from init as Polylang chokes on that
			// just remove the rules and let WP renew them when ready...
			delete_option('rewrite_rules');

			// upgrade from ping to pong
			$pings = get_option($this->prefix.'pings');
			if (!empty($pings))
				update_option($this->prefix.'pong',$pings);

			$this->yes_mother = true; // did you flush and wash your hands?		

			update_option('xmlsf_version', XMLSF_VERSION);
		}
		
	}

	private function flush_rules($hard = false) 
	{		
		if ($this->yes_mother) // did you flush?
			return; // yes, mother!

		global $wp_rewrite;
		// don't need hard flush by default
		$wp_rewrite->flush_rules($hard); 

		$this->yes_mother = true;
	}
	
	public function register_gn_taxonomies() 
	{
			register_taxonomy( 'gn-genre', 'post', array(
				'hierarchical' => true,
				'labels' => array(
						'name' => __('Google News Genres','xml-sitemap-feed'),
						'singular_name' => __('Google News Genre','xml-sitemap-feed'),
						//'menu_name' => __('GN Genres','xml-sitemap-feed'),
					),
				'public' => false,
				'show_ui' => true,
				'show_tagcloud' => false,
				'query_var' => false,
				'capabilities' => array( // prevent creation / deletion
						'manage_terms' => 'nobody',
						'edit_terms' => 'nobody',
						'delete_terms' => 'nobody',
						'assign_terms' => 'edit_posts'
					)
			));

			register_taxonomy( 'gn-location-3', 'post', array(
				'hierarchical' => false,
				'labels' => array(
						'name' => __('Google News Country','xml-sitemap-feed'),
						//'menu_name' => __('GN Genres','xml-sitemap-feed'),
						'separate_items_with_commas' => __('Only one allowed. Must be consistent with other Google News location entities (if set).','xml-sitemap-feed'),
					),
				'public' => false,
				'show_ui' => true,
				'show_tagcloud' => false,
				'query_var' => false,
				'capabilities' => array( // prevent creation / deletion
						'manage_terms' => 'nobody',
						'edit_terms' => 'nobody',
						'delete_terms' => 'nobody',
						'assign_terms' => 'edit_posts'
					)
			));

			register_taxonomy( 'gn-location-2', 'post', array(
				'hierarchical' => false,
				'labels' => array(
						'name' => __('Google News State/Province','xml-sitemap-feed'),
						//'menu_name' => __('GN Genres','xml-sitemap-feed'),
						'separate_items_with_commas' => __('Only one allowed. Must be consistent with other Google News location entities (if set).','xml-sitemap-feed'),
					),
				'public' => false,
				'show_ui' => true,
				'show_tagcloud' => false,
				'query_var' => false,
				'capabilities' => array( // prevent creation / deletion
						'manage_terms' => 'nobody',
						'edit_terms' => 'nobody',
						'delete_terms' => 'nobody',
						'assign_terms' => 'edit_posts'
					)
			));

			register_taxonomy( 'gn-location-1', 'post', array(
				'hierarchical' => false,
				'labels' => array(
						'name' => __('Google News City','xml-sitemap-feed'),
						//'menu_name' => __('GN Genres','xml-sitemap-feed'),
						'separate_items_with_commas' => __('Only one allowed. Must be consistent with other Google News location entities (if set).','xml-sitemap-feed'),
					),
				'public' => false,
				'show_ui' => true,
				'show_tagcloud' => false,
				'query_var' => false,
				'capabilities' => array( // prevent creation / deletion
						'manage_terms' => 'nobody',
						'edit_terms' => 'nobody',
						'delete_terms' => 'nobody',
						'assign_terms' => 'edit_posts'
					)
			));

	}
	
	public function register_news_taxonomy() 
	{
		$sitemaps = $this->get_sitemaps();
		
		if (isset($sitemaps['sitemap-news'])) {
			
			// register the taxonomies
			$this->register_gn_taxonomies();

			// create terms
			if (delete_transient('xmlsf_create_genres')) {
				foreach ($this->gn_genres as $slug => $name) {
					wp_insert_term(	$name, 'gn-genre', array(
						'slug' => $slug,
					) );
				}
			}
		}
	}
	
	public function admin_init() 
	{
		// CATCH TRANSIENT for reset
		if (delete_transient('xmlsf_clear_settings'))
			$this->clear_settings();
		
		// CATCH TRANSIENT for flushing rewrite rules after the sitemaps setting has changed
		if (delete_transient('xmlsf_flush_rewrite_rules'))
			$this->flush_rules();
		
		// Include the admin class file
		include_once( XMLSF_PLUGIN_DIR . '/includes/admin.php' );

	}

	// for debugging
	public function _e_usage() 
	{
		if (defined('WP_DEBUG') && WP_DEBUG == true) {
			echo '<!-- Queries executed '.get_num_queries();
			if(function_exists('memory_get_peak_usage'))
				echo ' | Peak memory usage '.round(memory_get_peak_usage()/1024/1024,2).'M';
			echo ' -->';
		}
	}

	/**
	* CONSTRUCTOR
	*/

	function __construct() 
	{
		// sitemap element filters
		add_filter('the_title_xmlsitemap', 'strip_tags');
		add_filter('the_title_xmlsitemap', 'ent2ncr', 8);
		add_filter('the_title_xmlsitemap', 'esc_html');
		add_filter('bloginfo_xmlsitemap', 'ent2ncr', 8);
		
		// TEMPLATE
		add_filter('template', array($this, 'template'), 0); //create_function ( string $args , string $code )
		
		// REQUEST main filtering function
		add_filter('request', array($this, 'filter_request'), 1 );
		
		// TEXT DOMAIN, LANGUAGE PLUGIN FILTERS ...
		add_action('plugins_loaded', array($this,'plugins_loaded'), 11 );

		// REWRITES
		add_action('generate_rewrite_rules', array($this, 'rewrite_rules') );
		add_filter('user_trailingslashit', array($this, 'trailingslash') );
		
		// TAXONOMY
		add_action('init', array($this,'register_news_taxonomy'), 0 );
		
		// REGISTER SETTINGS, SETTINGS FIELDS, UPGRADE checks...
		add_action('admin_init', array($this,'admin_init'));
		
		// ROBOTSTXT
		add_action('do_robotstxt', array($this, 'robots'), 0 );
		add_filter('robots_txt', array($this, 'robots_txt'), 0 );
		
		// PINGING
		add_action('transition_post_status', array($this, 'do_pings'), 10, 3); 

		// DE-ACTIVATION
		register_deactivation_hook( XMLSF_PLUGIN_DIR . '/xml-sitemap.php', array($this, 'clear_settings') );
	}

}
