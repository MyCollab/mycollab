<?php
/**
 * Google News Sitemap Feed Template
 *
 * @package XML Sitemap Feed plugin for WordPress
 */

status_header('200'); // force header('HTTP/1.1 200 OK') for sites without posts
header('Content-Type: text/xml; charset=' . get_bloginfo('charset'), true);

echo '<?xml version="1.0" encoding="'.get_bloginfo('charset').'"?><?xml-stylesheet type="text/xsl" href="' . plugins_url('xsl/sitemap-index.xsl.php',__FILE__) . '?ver=' . XMLSF_VERSION . '"?>
<!-- generated-on="'.date('Y-m-d\TH:i:s+00:00').'" -->
<!-- generator="XML & Google News Sitemap Feed plugin for WordPress" -->
<!-- generator-url="http://status301.net/wordpress-plugins/xml-sitemap-feed/" -->
<!-- generator-version="'.XMLSF_VERSION.'" -->
<sitemapindex xmlns="http://www.sitemaps.org/schemas/sitemap/0.9"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.sitemaps.org/schemas/sitemap/0.9 
		http://www.sitemaps.org/schemas/sitemap/0.9/siteindex.xsd">
';

global $xmlsf;
?>
	<sitemap>
		<loc><?php echo $xmlsf->get_index_url('home'); ?></loc>
		<lastmod><?php echo mysql2date('Y-m-d\TH:i:s+00:00', get_lastdate( 'gmt' ), false); ?></lastmod>
	</sitemap>
<?php
// add rules for custom public post types
foreach ( $xmlsf->have_post_types() as $post_type ) :

	if (!empty($post_type['archive'])) 
		$archive = $post_type['archive']; 
	else 
		$archive = '';
	foreach ( $xmlsf->get_archives($post_type['name'],$archive) as $m => $url ) {
?>
	<sitemap>
		<loc><?php echo $url; ?></loc>
		<lastmod><?php echo mysql2date('Y-m-d\TH:i:s+00:00', get_lastmodified( 'gmt', $post_type['name'], $m ), false); ?></lastmod>
	</sitemap>
<?php 
	}
endforeach;

	// add rules for custom public post taxonomies
foreach ( $xmlsf->get_taxonomies() as $taxonomy ) :

	if ( wp_count_terms( $taxonomy, array('hide_empty'=>true) ) > 0 ) {
?>
	<sitemap>
		<loc><?php echo $xmlsf->get_index_url('taxonomy',$taxonomy); ?></loc>
		<?php echo $xmlsf->get_lastmod('taxonomy',$taxonomy); ?>
	</sitemap>
<?php 
	}
endforeach;

// custom URLs sitemap
$urls = $xmlsf->get_urls();
if ( !empty($urls) ) :
?>
	<sitemap>
		<loc><?php echo $xmlsf->get_index_url('custom'); ?></loc>
	</sitemap>
<?php 
endif;
?></sitemapindex>
<?php $xmlsf->_e_usage(); ?>
