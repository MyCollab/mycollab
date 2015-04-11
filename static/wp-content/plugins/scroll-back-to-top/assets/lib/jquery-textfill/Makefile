all: jquery.textfill.min.js

jquery.textfill.min.js: jquery.textfill.js
	curl --data output_info=compiled_code --data-urlencode js_code@jquery.textfill.js http://closure-compiler.appspot.com/compile > jquery.textfill.min.js

clean:
	rm -f jquery.textfill.min.js

.PHONY: clean
