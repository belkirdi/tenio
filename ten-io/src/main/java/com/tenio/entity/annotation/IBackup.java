/*
The MIT License

Copyright (c) 2016-2020 kong <congcoi123@gmail.com>

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
package com.tenio.entity.annotation;

/**
 * This interface is used for backing up and restoring an object. The object is
 * serialized to a JSON string and can be saved somewhere. Otherwise, a JSON
 * object can be established to the corresponding one.
 *
 * @param <T> the template class
 * 
 * @author kong
 * 
 */
public interface IBackup<T> {

	/**
	 * Convert an object data to a JSON data
	 * 
	 * @return <b>true</b> if it's success, <b>false</b> otherwise
	 */
	boolean backup();

	/**
	 * Convert a JSON data to an object data
	 * 
	 * @return an object or <b>null</b> in failed cases
	 */
	T restore();

}
