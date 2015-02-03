<?php
/**
 * XML Sitemap Feed Template for displaying an XML Sitemap feed.
 *
 * @package XML Sitemap Feed plugin for WordPress
 */

status_header('200'); // force header('HTTP/1.1 200 OK') even for sites without posts
header('Content-Type: text/xml; charset=' . get_bloginfo('charset'), true);

echo '<?xml version="1.0" encoding="' . get_bloginfo('charset') . '"?>
<?xml-stylesheet type="text/xsl" href="' . plugins_url('xsl/sitemap.xsl.php',__FILE__) . '?ver=' . XMLSF_VERSION . '"?>
<!-- generated-on="' . date('Y-m-d\TH:i:s+00:00') . '" -->
<!-- generator="XML & Google News Sitemap Feed plugin for WordPress" -->
<!-- generator-url="http://status301.net/wordpress-plugins/xml-sitemap-feed/" -->
<!-- generator-version="' . XMLSF_VERSION . '" -->
<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9" ';

global $xmlsf;
$post_type = get_query_var('post_type');

foreach ( $xmlsf->do_tags($post_type) as $tag => $setting )
	$$tag = $setting;

echo !empty($image) ? '
	xmlns:image="http://www.google.com/schemas/sitemap-image/1.1" ' : '';
echo '
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xsi:schemaLocation="http://www.sitemaps.org/schemas/sitemap/0.9 
		http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd';
echo !empty($image) ? '
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

// any ID's we need to exclude?
$excluded = $xmlsf->get_excluded($post_type);

// loop away!
if ( have_posts() ) :
    while ( have_posts() ) : 
	the_post();
	
	// check if page is in the exclusion list (like front page)
	// or if we are not dealing with an external URL :: Thanks to Francois Deschenes :)
	// or if post meta says "exclude me please"
	$exclude = get_post_meta( $post->ID, '_xmlsf_exclude', true );
	if ( !empty($exclude) || !$xmlsf->is_allowed_domain(get_permalink()) || in_array($post->ID, $excluded) )
		continue;

	// TODO more image tags & video tags
	?>
	<url>
		<loc><?php echo esc_url( get_permalink() ); ?></loc>
		<?php echo $xmlsf->get_lastmod(); ?> 
		<changefreq><?php echo $xmlsf->get_changefreq(); ?></changefreq>
	 	<priority><?php echo $xmlsf->get_priority(); ?></priority>
<?php
	if ( !empty($image) && $xmlsf->get_images() ) : 
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
endif; 
?></urlset>
<?php $xmlsf->_e_usage(); ?>
