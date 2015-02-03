<?php
/**
 * Google News Sitemap Feed Template
 *
 * @package XML Sitemap Feed plugin for WordPress
 */

global $xmlsf;
$options = $xmlsf->get_option('news_tags');

status_header('200'); // force header('HTTP/1.1 200 OK') for sites without posts
header('Content-Type: text/xml; charset=' . get_bloginfo('charset'), true);

echo '<?xml version="1.0" encoding="'.get_bloginfo('charset').'"?>
<?xml-stylesheet type="text/xsl" href="' . plugins_url('xsl/sitemap-news.xsl.php',__FILE__) . '?ver=' . XMLSF_VERSION . '"?>
<!-- generated-on="'.date('Y-m-d\TH:i:s+00:00').'" -->
<!-- generator="XML & Google News Sitemap Feed plugin for WordPress" -->
<!-- generator-url="http://status301.net/wordpress-plugins/xml-sitemap-feed/" -->
<!-- generator-version="'.XMLSF_VERSION.'" -->
<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9" 
	xmlns:news="http://www.google.com/schemas/sitemap-news/0.9" ';

echo !empty($options['image']) ? '
	xmlns:image="http://www.google.com/schemas/sitemap-image/1.1" ' : '';
echo '
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xsi:schemaLocation="http://www.sitemaps.org/schemas/sitemap/0.9 
		http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd 
		http://www.google.com/schemas/sitemap-news/0.9 
		http://www.google.com/schemas/sitemap-news/0.9/sitemap-news.xsd' ;
echo !empty($options['image']) ? '
		http://www.google.com/schemas/sitemap-image/1.1 
		http://www.google.com/schemas/sitemap-image/1.1/sitemap-image.xsd' : '';
echo '">
';

// get site language for default language
// bloginfo_rss('language') returns improper format so
// we explode on hyphen and use only first part. 
// TODO this workaround breaks (simplified) chinese :(
$language = reset(explode('-', convert_chars(strip_tags(get_bloginfo('language'))) ));	
if ( empty($language) )
	$language = 'en';

// loop away!
if ( have_posts() ) : 
    while ( have_posts() ) : 
	the_post();

	// check if we are not dealing with an external URL :: Thanks to Francois Deschenes :)
	// or if post meta says "exclude me please"
	$exclude = get_post_meta( $post->ID, '_xmlsf_exclude', true );
	if ( !empty($exclude) || !$xmlsf->is_allowed_domain(get_permalink()) )
		continue;

	?>
	<url>
		<loc><?php echo esc_url( get_permalink() ); ?></loc>
		<news:news>
			<news:publication>
				<news:name><?php 
					if(!empty($options['name']))
						echo apply_filters( 'the_title_xmlsitemap', $options['name'] );
					elseif(defined('XMLSF_GOOGLE_NEWS_NAME')) 
						echo apply_filters( 'the_title_xmlsitemap', XMLSF_GOOGLE_NEWS_NAME ); 
					else 
						echo apply_filters( 'the_title_xmlsitemap', get_bloginfo('name') ); ?></news:name>
				<news:language><?php 
					$lang = reset(get_the_terms($post->ID,'language'));
					echo (is_object($lang)) ? $lang->slug : $language;  ?></news:language>
			</news:publication>
			<news:publication_date><?php 
				echo mysql2date('Y-m-d\TH:i:s+00:00', $post->post_date_gmt, false); ?></news:publication_date>
			<news:title><?php echo apply_filters( 'the_title_xmlsitemap', get_the_title() ); ?></news:title>
<?php
	// access tag
	$access = '';
	if (!empty($options['access'])) {
//		if ( get_post_status() == 'private' ) {
//			if (!empty($options['access']['private'])) $access = $options['access']['private'];
//		} else
		if ( post_password_required() ) {
			if (!empty($options['access']['password'])) $access = $options['access']['password'];
		} else {
			if (!empty($options['access']['default'])) $access = $options['access']['default'];
		}
	}
	
	if (!empty($access)) {
	?>
			<news:access><?php echo $access; ?></news:access>
<?php
	}

	// genres tag
	$genres = '';
	$terms = get_the_terms($post->ID,'gn-genre');
	if ( is_array($terms) ) { 
		$sep = ''; 
		foreach($terms as $obj) { 
			if (!empty($obj->name)) {
				$genres .= $sep . $obj->name;
				$sep = ', ';
			}
		} 			
	} 
	
	$genres = trim(apply_filters('the_title_xmlsitemap', $genres));
	
	if ( empty($genres) && !empty($options['genres']) && !empty($options['genres']['default']) ) { 
		$genres = trim(apply_filters('the_title_xmlsitemap', $options['genres']['default']));
	}

	if ( !empty($genres) ) {
	?>
			<news:genres><?php echo $genres; ?></news:genres>
<?php
	}

	// keywords tag
	$keywords = '';
	if( !empty($options['keywords']) ) {
		if ( !empty($options['keywords']['from']) ) {
			$terms = get_the_terms( $post->ID, $options['keywords']['from'] );
			if ( is_array($terms) ) {
				$sep = '';
				foreach($terms as $obj) { 
					if (!empty($obj->name)) {
						$keywords .= $sep . $obj->name;
						$sep = ', ';
					}
				} 
			}
		} 
		
		$keywords = trim(apply_filters('the_title_xmlsitemap', $keywords));
		
		if ( empty($keywords) && !empty($options['keywords']['default']) ) {
			$keywords = trim(apply_filters('the_title_xmlsitemap', $options['keywords']['default']));
		}
		
	}
	
	if ( !empty($keywords) ) {
	?>
			<news:keywords><?php echo $keywords; ?></news:keywords>
<?php
	}
	
	// locations tag
	$locations = '';
	$sep = '';
	$locs = array('gn-location-1','gn-location-2','gn-location-3');
	foreach ($locs as $tax) {
		$terms = get_the_terms($post->ID,$tax);
		if ( is_array($terms) ) {
			$obj = array_shift($terms);
			$term = is_object($obj) ? trim($obj->name) : '';
			if ( !empty($term) ) { 
				$locations .= $sep . $term; 
				$sep = ', ';			
			}
		}
	}
	
	$locations = trim(apply_filters('the_title_xmlsitemap', $locations));
	
	if ( empty($locations) && isset($options['locations']) && !empty($options['locations']['default']) ) { 
		$locations = trim(apply_filters('the_title_xmlsitemap', $options['locations']['default']));
	}

	if ( !empty($locations) ) {
	?>
			<news:geo_locations><?php echo $locations; ?></news:geo_locations>
<?php
	}
	
	?>
		</news:news>
<?php
	if ( !empty($options['image']) && $xmlsf->get_images('news') ) : 
		foreach ( $xmlsf->get_images() as $image ) { 
			if ( empty($image['loc']) )
				continue;
	?>
		<image:image>
			<image:loc><?php echo $image['loc']; ?></image:loc>
<?php 
		if ( !empty($image['title']) )
			echo "\t\t\t<image:title>{$image['title']}</image:title>\n";

		if ( !empty($image['caption']) )
			echo "\t\t\t<image:caption>{$image['caption']}</image:caption>\n";
	?>
		</image:image>
<?php 
		}
	endif;
	?>
	</url>
<?php 
    endwhile;
else :
// TODO replace link to home with the last post even if it's older than 2 days?

	$lastmodified_gmt = get_lastmodified('GMT'); // last posts or page modified date
?>
	<url>
		<loc><?php 
			// hook for filter 'xml_sitemap_url' provides a string here and MUST get a string returned
			$url = apply_filters( 'xml_sitemap_url', trailingslashit(home_url()) );
			if ( is_string($url) ) 
				echo esc_url( $url ); 
			else 
				echo esc_url( trailingslashit(home_url()) ); ?></loc>
		<lastmod><?php echo mysql2date('Y-m-d\TH:i:s+00:00', $lastmodified_gmt, false); ?></lastmod>
		<changefreq>daily</changefreq>
		<priority>1.0</priority>
	</url>
<?php
endif; 

?></urlset>
<?php $xmlsf->_e_usage(); ?>
