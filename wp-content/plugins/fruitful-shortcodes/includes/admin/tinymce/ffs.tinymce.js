/*-----------------------------------------------------------------------------------*/
/*	ffs TinyMCE "Tabs" Button
/*-----------------------------------------------------------------------------------*/
(function() {  
    tinymce.create('tinymce.plugins.fruitful_horizontal_tabs', {  
        init : function(ed, url) {
            ed.addButton('fruitful_horizontal_tabs', {  
                title : 'Add horizontal tabs',  
                image : url+'/tabs.png',    
                onclick : function() {  
                     ed.selection.setContent('[fruitful_tabs type="default" width="100%" fit="false"] <br /> [fruitful_tab title="Title 1"] Tab 1 content place [/fruitful_tab] <br /> [fruitful_tab title="Title 2"] Tab 2 content place [/fruitful_tab] <br /> [fruitful_tab title="Title 3"] Tab 3 content place [/fruitful_tab] <br /> [/fruitful_tabs]');  
            
                }  
            });  
        },  
        createControl : function(n, cm) {  
            return null;  
        },  
    });  
    tinymce.PluginManager.add('fruitful_horizontal_tabs', tinymce.plugins.fruitful_horizontal_tabs);  
})();

(function() {  
    tinymce.create('tinymce.plugins.fruitful_vertical_tabs', {  
        init : function(ed, url) {  
            ed.addButton('fruitful_vertical_tabs', {  
                title : 'Add vertical tabs',  
                image : url+'/tabsvertical.png',  
                onclick : function() {  
                     ed.selection.setContent('[fruitful_tabs type="vertical" width="100%" fit="false"] <br /> [fruitful_tab title="Title 1"] Tab 1 content place [/fruitful_tab] <br /> [fruitful_tab title="Title 2"] Tab 2 content place [/fruitful_tab] <br /> [fruitful_tab title="Title 3"] Tab 3 content place [/fruitful_tab] <br /> [/fruitful_tabs]');  
            
                }  
            });  
        },  
        createControl : function(n, cm) {  
            return null;  
        },  
    });  
    tinymce.PluginManager.add('fruitful_vertical_tabs', tinymce.plugins.fruitful_vertical_tabs);  
})();


(function() {  
    tinymce.create('tinymce.plugins.fruitful_accordion_tabs', {  
        init : function(ed, url) {  
            ed.addButton('fruitful_accordion_tabs', {  
                title : 'Add accordion tabs',  
                image : url+'/accordion.png',  
                onclick : function() {  
                     ed.selection.setContent('[fruitful_tabs type="accordion" width="100%" fit="false"] <br /> [fruitful_tab title="Title 1"] Tab 1 content place [/fruitful_tab] <br /> [fruitful_tab title="Title 2"] Tab 2 content place [/fruitful_tab] <br /> [fruitful_tab title="Title 3"] Tab 3 content place [/fruitful_tab] <br /> [/fruitful_tabs]');  
            
                }  
            });  
        },  
        createControl : function(n, cm) {  
            return null;  
        },  
    });  
    tinymce.PluginManager.add('fruitful_accordion_tabs', tinymce.plugins.fruitful_accordion_tabs);  
})();

/*-----------------------------------------------------------------------------------*/
/*	ffs TinyMCE "Description Box" Button
/*-----------------------------------------------------------------------------------*/
(function() {  
    tinymce.create('tinymce.plugins.fruitful_dbox', {  
        init : function(ed, url) {  
            ed.addButton('fruitful_dbox', {  
                title : 'Add promo text',  
                image : url+'/promotext.png',  
                onclick : function() {  
                     ed.selection.setContent('[fruitful_dbox] Hello, world! This is Fruitful Shortcodes plugin. [/fruitful_dbox]');  
											 
                }  
            });  
        },  
        createControl : function(n, cm) {  
            return null;  
        },  
    });  
    tinymce.PluginManager.add('fruitful_dbox', tinymce.plugins.fruitful_dbox);  
})();


/*-----------------------------------------------------------------------------------*/
/*	ffs TinyMCE "Info Box Area with columns" Button
/*-----------------------------------------------------------------------------------*/
(function() {  
    tinymce.create('tinymce.plugins.fruitful_one_half_column', {  
        init : function(ed, url) {  
            ed.addButton('fruitful_one_half_column', {  
                title : 'Add 1/2 columns',  
                image : url+'/one_half.png',  
                onclick : function() {  
                     ed.selection.setContent('[fruitful_ibox_row] <br /> [fruitful_ibox column="ffs-two-one" title="Title 1"]Lorem ipsum dolor sit amet.[/fruitful_ibox] <br /> [fruitful_ibox column="ffs-two-one" title="Title 2" last="true"]Lorem ipsum dolor sit amet.[/fruitful_ibox][/fruitful_ibox_row]');  
                }  
            });  
        },  
        createControl : function(n, cm) {  
            return null;  
        },  
    });  
    tinymce.PluginManager.add('fruitful_one_half_column', tinymce.plugins.fruitful_one_half_column);  
	
	tinymce.create('tinymce.plugins.fruitful_one_third_column', {  
        init : function(ed, url) {  
            ed.addButton('fruitful_one_third_column', {  
                title : 'Add 1/3 columns',  
                image : url+'/one_third.png',  
                onclick : function() {  
                     ed.selection.setContent('[fruitful_ibox_row] <br /> [fruitful_ibox column="ffs-three-one" title="Title 1"]Lorem ipsum dolor sit amet.[/fruitful_ibox] <br /> [fruitful_ibox column="ffs-three-one" title="Title 2"]Lorem ipsum dolor sit amet.[/fruitful_ibox] <br /> [fruitful_ibox column="ffs-three-one" title="Title 3" last="true"]Lorem ipsum dolor sit amet.[/fruitful_ibox] <br /> [/fruitful_ibox_row]');  
											 
                }  
            });  
        },  
        createControl : function(n, cm) {  
            return null;  
        },  
    });  
    tinymce.PluginManager.add('fruitful_one_third_column', tinymce.plugins.fruitful_one_third_column);  
	
	tinymce.create('tinymce.plugins.fruitful_two_third_column', {  
        init : function(ed, url) {  
            ed.addButton('fruitful_two_third_column', {  
                title : 'Add 2/3 columns',  
                image : url+'/two_third.png',  
                onclick : function() {  
                     ed.selection.setContent('[fruitful_ibox_row] <br /> [fruitful_ibox column="ffs-three-two" title="Title 1"]Lorem ipsum dolor sit amet.[/fruitful_ibox] <br /> [fruitful_ibox column="ffs-three-one" title="Title 2" last="true"]Lorem ipsum dolor sit amet.[/fruitful_ibox] <br /> [/fruitful_ibox_row]');  
                }  
            });  
        },  
        createControl : function(n, cm) {  
            return null;  
        },  
    });  
    tinymce.PluginManager.add('fruitful_two_third_column', tinymce.plugins.fruitful_two_third_column);  
	
	tinymce.create('tinymce.plugins.fruitful_one_fourth_column', {  
        init : function(ed, url) {  
            ed.addButton('fruitful_one_fourth_column', {  
                title : 'Add 1/4 columns',  
                image : url+'/one_fourth.png',  
                onclick : function() {  
                     ed.selection.setContent('[fruitful_ibox_row] <br /> [fruitful_ibox column="ffs-four-one" title="Title 1"]Lorem ipsum dolor sit amet.[/fruitful_ibox] <br /> [fruitful_ibox column="ffs-four-one" title="Title 2"]Lorem ipsum dolor sit amet.[/fruitful_ibox] <br /> [fruitful_ibox column="ffs-four-one" title="Title 3"]Lorem ipsum dolor sit amet.[/fruitful_ibox] <br /> [fruitful_ibox column="ffs-four-one" title="Title 4" last="true"]Lorem ipsum dolor sit amet.[/fruitful_ibox] <br /> [/fruitful_ibox_row]');  
                }  
            });  
        },  
        createControl : function(n, cm) {  
            return null;  
        },  
    });  
    tinymce.PluginManager.add('fruitful_one_fourth_column', tinymce.plugins.fruitful_one_fourth_column);  
	
	tinymce.create('tinymce.plugins.fruitful_three_fourth_column', {  
        init : function(ed, url) {  
            ed.addButton('fruitful_three_fourth_column', {  
                title : 'Add 3/4 columns',  
                image : url+'/three_fourth.png',  
                onclick : function() {  
                     ed.selection.setContent('[fruitful_ibox_row] <br /> [fruitful_ibox column="ffs-four-three" title="Title 1"]Lorem ipsum dolor sit amet.[/fruitful_ibox] <br /> [fruitful_ibox column="ffs-four-one" title="Title 2" last="true"]Lorem ipsum dolor sit amet.[/fruitful_ibox] <br /> [/fruitful_ibox_row]');  
                }  
            });  
        },  
        createControl : function(n, cm) {  
            return null;  
        },  
    });  
    tinymce.PluginManager.add('fruitful_three_fourth_column', tinymce.plugins.fruitful_three_fourth_column);  
	
	tinymce.create('tinymce.plugins.fruitful_one_fifth_column', {  
        init : function(ed, url) {  
            ed.addButton('fruitful_one_fifth_column', {  
                title : 'Add 1/5 columns',  
                image : url+'/one_fifth.png',  
                onclick : function() {  
                     ed.selection.setContent('[fruitful_ibox_row] <br /> [fruitful_ibox column="ffs-five-one" title="Title 1"]Lorem ipsum dolor sit amet.[/fruitful_ibox] <br /> [fruitful_ibox column="ffs-five-one" title="Title 2"]Lorem ipsum dolor sit amet.[/fruitful_ibox] <br /> [fruitful_ibox column="ffs-five-one" title="Title 3"]Lorem ipsum dolor sit amet.[/fruitful_ibox] <br /> [fruitful_ibox column="ffs-five-one" title="Title 4"]Lorem ipsum dolor sit amet.[/fruitful_ibox] <br /> [fruitful_ibox column="ffs-five-one" title="Title 5" last="true"]Lorem ipsum dolor sit amet.[/fruitful_ibox] <br /> [/fruitful_ibox_row]');  
                }  
            });  
        },  
        createControl : function(n, cm) {  
            return null;  
        },  
    });  
    tinymce.PluginManager.add('fruitful_one_fifth_column', tinymce.plugins.fruitful_one_fifth_column);  
})();

/*-----------------------------------------------------------------------------------*/
/*	ffs TinyMCE "Separator" Button
/*-----------------------------------------------------------------------------------*/

(function() {  
    tinymce.create('tinymce.plugins.fruitful_sep', {  
        init : function(ed, url) {  
            ed.addButton('fruitful_sep', {  
                title : 'Add separator',  
                image : url+'/separator.png',  
                onclick : function() {  
                     ed.selection.setContent('[fruitful_sep]');  
											 
                }  
            });  
        },  
        createControl : function(n, cm) {  
            return null;  
        },  
    });  
    tinymce.PluginManager.add('fruitful_sep', tinymce.plugins.fruitful_sep);  
})();


/*-----------------------------------------------------------------------------------*/
/*	ffs TinyMCE "Alerts" Button
/*-----------------------------------------------------------------------------------*/

(function() {  
    tinymce.create('tinymce.plugins.fruitful_alerts', {  
        init : function(ed, url) {  
            ed.addButton('fruitful_alerts', {  
                title : 'Add alert',  
                image : url+'/alert.png',  
                onclick : function() {  
                     ed.selection.setContent('[fruitful_alert type="alert-success"]Oh snap! Change a few things up and try submitting again.[/fruitful_alert]');  
                }  
            });  
        },  
        createControl : function(n, cm) {  
            return null;  
        },  
    });  
    tinymce.PluginManager.add('fruitful_alerts', tinymce.plugins.fruitful_alerts);  
})();


/*-----------------------------------------------------------------------------------*/
/*	ffs TinyMCE "Progress bar" Button
/*-----------------------------------------------------------------------------------*/

(function() {  
    tinymce.create('tinymce.plugins.fruitful_pbar', {  
        init : function(ed, url) {  
            ed.addButton('fruitful_pbar', {  
                title :  'Add progress bar',  
                image : url+'/progressbar.png',  
                onclick : function() {  
                     ed.selection.setContent('[fruitful_pbar stripped="false"][fruitful_bar type="progress-bar-success" width="60%" stripped="false" active="false"][/fruitful_bar][/fruitful_pbar]');  
                }  
            });  
        },  
        createControl : function(n, cm) {  
            return null;  
        },  
    });  
    tinymce.PluginManager.add('fruitful_pbar', tinymce.plugins.fruitful_pbar);  
})();


/*-----------------------------------------------------------------------------------*/
/*	ffs TinyMCE "Bootstrap Button" Button
/*-----------------------------------------------------------------------------------*/
(function() {  
    tinymce.create('tinymce.plugins.fruitful_btn', {  
        init : function(ed, url) {  
            ed.addButton('fruitful_btn', {  
                title :  'Add button',  
                image : url+'/button.png',  
                onclick : function() {  
                     ed.selection.setContent('[fruitful_btn]Name "button"[/fruitful_btn]');  
                }  
            });  
        },  
        createControl : function(n, cm) {  
            return null;  
        },  
    });  
    tinymce.PluginManager.add('fruitful_btn', tinymce.plugins.fruitful_btn);  
})();