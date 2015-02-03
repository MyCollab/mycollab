<?php
/*
	Thnaks WP Inline Comment Errors
	Author: "aviarts"
*/
if (!class_exists('fruitfulcFormInlineErrors')){
    class fruitfulcFormInlineErrors {
        public function __construct() { add_action('init', array($this, 'init')); }
        public function init() {
            session_start();
            add_filter('wp_die_handler', 			 	array($this, 'getWpDieHandler'));
            add_action('comment_form_before_fields', 	array($this, 'fruitfulOutInlineErrors'));
            add_action('comment_form_logged_in_after', 	array($this, 'fruitfulOutInlineErrors'));
            add_filter('comment_form_default_fields',	array($this, 'fruitfulCFormDefVal'));
            add_filter('comment_form_field_comment',	array($this, 'fruitfulformCommentDefault'));
        }

		function getWpDieHandler($handler){ return array($this, 'fruitfulhandleWpCommentError'); }
        function fruitfulhandleWpCommentError($message, $title='', $args=array())
        {
            if(!is_admin() && !empty($_POST['comment_post_ID']) && is_numeric($_POST['comment_post_ID'])){
                $_SESSION['formError'] = $message;
                $denied = array('submit', 'comment_post_ID', 'comment_parent');
                foreach($_POST as $key => $value){
                    if(!in_array($key, $denied)){
                        $_SESSION['formFields'][$key] = stripslashes($value);
                    }
                }
                session_write_close();
                wp_safe_redirect(get_permalink($_POST['comment_post_ID']) . '#fruitfulCommentError', 302);
                exit;
            } else {
                _default_wp_die_handler($message, $title, $args);   
            }
        }

        public function fruitfulOutInlineErrors() {
            $formError = '';
			if (!empty($_SESSION['formError'])) {
				$formError = $_SESSION['formError'];
                unset($_SESSION['formError']);
				echo '<div id="fruitfulCommentError" class="commentsErrorBox">';
					echo '<ul><li>'.$formError.'</li></ul>';
				echo '</div>';
			}
		}

        function fruitfulCFormDefVal($fields) {
            if (!empty($_SESSION['formFields'])) {
				$formFields = $_SESSION['formFields'];
				foreach($fields as $key => $field){
                if($this->stringContains('input', $field)){
						if($this->stringContains('type="text"', $field)){
							$fields[$key] = str_replace('value=""', 'value="'. stripslashes($formFields[$key]) .'"', $field);
						}
					} elseif ($this->stringContains('</textarea>', $field)){
						$fields[$key] = str_replace('</textarea>', stripslashes($formFields[$key]) .'</textarea>', $field);
					}
				}
			}
            return $fields;
        }

        function fruitfulformCommentDefault($comment_field) {
            if (!empty($_SESSION['formFields'])) {
				$formFields = $_SESSION['formFields'];
				unset($_SESSION['formFields']);
				return str_replace('</textarea>', $formFields['comment'] . '</textarea>', $comment_field);
			} else {
				return $comment_field;
			}
        }

        public function stringContains($needle, $haystack){ return strpos($haystack, $needle) !== FALSE; }

    }

}

new fruitfulcFormInlineErrors();