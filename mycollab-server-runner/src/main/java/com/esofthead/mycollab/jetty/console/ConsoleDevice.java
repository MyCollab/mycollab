/**
 * This file is part of mycollab-server-runner.
 *
 * mycollab-server-runner is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-server-runner is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-server-runner.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
Copyright (c) 2010 McDowell

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 */

package com.esofthead.mycollab.jetty.console;

import java.io.Console;
import java.io.PrintWriter;
import java.io.Reader;

/**
 * {@link TextDevice} implementation wrapping a {@link Console}.
 * 
 * @author McDowell
 */
class ConsoleDevice extends TextDevice {
	private final Console console;

	public ConsoleDevice(Console console) {
		this.console = console;
	}

	@Override
	public TextDevice printf(String fmt, Object... params)
			throws ConsoleException {
		console.format(fmt, params);
		return this;
	}

	@Override
	public Reader reader() throws ConsoleException {
		return console.reader();
	}

	@Override
	public String readLine() throws ConsoleException {
		return console.readLine();
	}

	@Override
	public char[] readPassword() throws ConsoleException {
		return console.readPassword();
	}

	@Override
	public PrintWriter writer() throws ConsoleException {
		return console.writer();
	}
}