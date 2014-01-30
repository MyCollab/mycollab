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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;

/**
 * Abstraction representing a text input/output device.
 * 
 * @author McDowell
 */
public abstract class TextDevice {
	private static TextDevice DEFAULT;

	static {
		if (System.console() == null) {
			System.out
					.println("Can not detect system console. Will use standard input/output stream");
			DEFAULT = streamDevice(System.in, System.out);
		} else {
			DEFAULT = new ConsoleDevice(System.console());
		}
	}

	/**
	 * The default system text I/O device.
	 * 
	 * @return the default device
	 */
	public static TextDevice defaultTextDevice() {
		return DEFAULT;
	}

	/**
	 * Returns a text I/O device wrapping the given streams. The default system
	 * encoding is used to decode/encode data.
	 * 
	 * @param in
	 *            an input source
	 * @param out
	 *            an output target
	 * @return a new device
	 */
	public static TextDevice streamDevice(InputStream in, OutputStream out) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		PrintWriter writer = new PrintWriter(out, true);
		return new CharacterDevice(reader, writer);
	}

	public abstract TextDevice printf(String fmt, Object... params)
			throws ConsoleException;

	public abstract String readLine() throws ConsoleException;

	public abstract char[] readPassword() throws ConsoleException;

	public abstract Reader reader() throws ConsoleException;

	public abstract PrintWriter writer() throws ConsoleException;
}