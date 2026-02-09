/*                                                                             
 * Copyright (C) 2019 Rison Han                                     
 *                                                                             
 * Licensed under the Apache License, Version 2.0 (the "License");           
 * you may not use this file except in compliance with the License.            
 * You may obtain a copy of the License at                                     
 *                                                                             
 *      http://www.apache.org/licenses/LICENSE-2.0                             
 *                                                                             
 * Unless required by applicable law or agreed to in writing, software         
 * distributed under the License is distributed on an "AS IS" BASIS,         
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.    
 * See the License for the specific language governing permissions and         
 * limitations under the License.                                              
 */                                                                            
package com.amituofo.common.kit.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.DeflaterInputStream;

public class GZIPCompressedInputStream extends DeflaterInputStream {

	/**
	 * This static class is used to hijack the InputStream read(b, off, len) function to be able to
	 * compute the CRC32 checksum of the content as it is read.
	 */
	static private class CRCWrappedInputStream extends InputStream {
		private InputStream inputStream;

		/**
		 * CRC32 of uncompressed data.
		 */
		protected CRC32 crc = new CRC32();

		/**
		 * Construct the object with the InputStream provided.
		 * 
		 * @param pInputStream
		 *            - Any class derived from InputStream class.
		 */
		public CRCWrappedInputStream(InputStream pInputStream) {
			inputStream = pInputStream;

			crc.reset(); // Reset the CRC value.
		}

		/**
		 * Methods in this group are the InputStream equivalent methods that just call the method on the
		 * InputStream provided during construction.
		 */
		public int available() throws IOException {
			return inputStream.available();
		};

		public void close() throws IOException {
			inputStream.close();
		};

		public void mark(int readlimit) {
			inputStream.mark(readlimit);
		};

		public boolean markSupported() {
			return inputStream.markSupported();
		};

		public int read() throws IOException {
			return inputStream.read();
		};

		public int read(byte[] b) throws IOException {
			return inputStream.read(b);
		};

		public void reset() throws IOException {
			inputStream.reset();
		};

		public long skip(long n) throws IOException {
			return inputStream.skip(n);
		};

		/*
		 * This function intercepts all read requests in order to calculate the CRC value that is stored in
		 * this object.
		 */
		public int read(byte b[], int off, int len) throws IOException {
			// Do the actual read from the input stream.
			int retval = inputStream.read(b, off, len);

			// If we successfully read something, compute the CRC value
			// of it.
			if (0 <= retval) {
				crc.update(b, off, retval);
			}

			// All done with the intercept. Return the value.
			return retval;
		};

		/*
		 * Function to retrieve the CRC value computed thus far while the stream was processed.
		 */
		public long getCRCValue() {
			return crc.getValue();
		};
	} // End class CRCWrappedInputStream.

//	private InputStream orginalInputStream;

	/**
	 * Create a new input stream with the default buffer size of 512 bytes.
	 * 
	 * @param pInputStream
	 *            - InputStream to read content for compression.
	 * @throws IOException
	 *             if an I/O error has occurred.
	 */
	public GZIPCompressedInputStream(InputStream pInputStream) {
		this(pInputStream, 512);
	}

	/**
	 * Create a new input stream with the specified buffer size.
	 * 
	 * @param pInputStream
	 *            - InputStream to read content for compression.
	 * @param size
	 *            - The output buffer size.
	 * @exception -
	 *                IOException if an I/O error has occurred.
	 */
	public GZIPCompressedInputStream(InputStream pInputStream, int size) {
		super(new CRCWrappedInputStream(pInputStream), new Deflater(Deflater.DEFAULT_COMPRESSION, true), size);

//		this.orginalInputStream = pInputStream;
		mCRCInputStream = (CRCWrappedInputStream) super.in;
	}

	// Indicator for if EOF has been reached for this stream.
	private boolean mReachedEOF = false;

	// Holder for the hijacked InputStream that computes the
	// CRC32 value.
	private CRCWrappedInputStream mCRCInputStream;

	/*
	 * GZIP header structure and positional variable.
	 */
	private final static int GZIP_MAGIC = 0x8b1f;

	private final static byte[] mHeader = { (byte) GZIP_MAGIC, // Magic number (short)
			(byte) (GZIP_MAGIC >> 8), // Magic number (short)
			Deflater.DEFLATED, // Compression method (CM)
			0, // Flags (FLG)
			0, // Modification time MTIME (int)
			0, // Modification time MTIME (int)
			0, // Modification time MTIME (int)
			0, // Modification time MTIME (int)
			0, // Extra flags (XFLG)
			0 // Operating system (OS) FYI. UNIX/Linux OS is 3
	};

	private int mHeaderPos = 0; // Keeps track of how much of the
								// header has already been read.

	/*
	 * GZIP trailer structure and positional indicator.
	 *
	 * Trailer consists of 2 integers: CRC32 value and original file size.
	 */
	private final static int TRAILER_SIZE = 8;
	private byte mTrailer[] = null;
	private int mTrailerPos = 0;

	/***
	 * Overridden functions against the DeflatorInputStream.
	 */

	/*
	 * Function to indicate whether there is any content available to read. It is overridden because
	 * there are the GZIP header and trailer to think about.
	 */
	public int available() throws IOException {
		return (mReachedEOF ? 0 : 1);
	}

	/*
	 * This read function is the meat of the class. It handles passing back the GZIP header, GZIP
	 * content, and GZIP trailer in that order to the caller.
	 */
	public int read(byte[] outBuffer, int offset, int maxLength) throws IOException, IndexOutOfBoundsException {

		int retval = 0; // Contains the number of bytes read into
						// outBuffer and will be the return value of
						// the function.
		int bIndex = offset; // Used as current index into outBuffer.
		int dataBytesCount = 0; // Used to indicate how many data bytes
								// are in the outBuffer array.

		// Make sure we have a buffer.
		if (null == outBuffer) {
			throw new NullPointerException("Null buffer for read");
		}

		// Make sure offset is valid.
		if (0 > offset || offset >= outBuffer.length) {
			throw new IndexOutOfBoundsException("Invalid offset parameter value passed into function");
		}

		// Make sure the maxLength is valid.
		if (0 > maxLength || outBuffer.length - offset < maxLength)
			throw new IndexOutOfBoundsException("Invalid maxLength parameter value passed into function");

		// Asked for nothing; you get nothing.
		if (0 == maxLength)
			return retval;

		/**
		 * Put any GZIP header in the buffer if we haven't already returned it from previous calls.
		 */
		if (mHeaderPos < mHeader.length) {
			// Get how much will fit.
			retval = Math.min(mHeader.length - mHeaderPos, maxLength);

			// Put it there.
			for (int i = retval; i > 0; i--) {
				outBuffer[bIndex++] = mHeader[mHeaderPos++];
			}

			// Return the number of bytes copied if we exhausted the
			// maxLength specified.
			// NOTE: Should never be >, but...
			if (retval >= maxLength) {
				return retval;
			}
		}

		/**
		 * At this point, the header has all been read or put into the buffer.
		 *
		 * Time to add some GZIP compressed data, if there is still some left.
		 */
		if (0 != super.available()) {

			// Get some data bytes from the DeflaterInputStream.
			dataBytesCount = super.read(outBuffer, offset + retval, maxLength - retval);

			// As long as we didn't get EOF (-1), update the buffer index and
			// retval.
			if (0 <= dataBytesCount) {
				bIndex += dataBytesCount;
				retval += dataBytesCount;
			}

			// Return the number of bytes copied during this call if we
			// exhausted the maxLength requested.
			// NOTE: Should never be >, but...
			if (retval == maxLength) {
				return retval;
			}

			// If we got here, we should have read all that can be read from
			// the input stream, so make sure the input stream is at EOF just
			// in case someone tries to read it outside this class.
			byte[] junk = new byte[1];
			if (-1 != super.read(junk, 0, junk.length)) {
				// Should never happen, but...
				throw new IOException("Unexpected content read from input stream when EOF expected");
			}
		}

		/**
		 * Got this far; time to write out the GZIP trailer.
		 */

		// Have we already set up the GZIP trailer in a previous
		// invocation?
		if (null == mTrailer) {
			// Time to prepare the trailer.
			mTrailer = new byte[TRAILER_SIZE];

			// Put the content in it.
			writeTrailer(mTrailer, 0);
		}

		// If there are still GZIP trailer bytes to be returned to the
		// caller, do as much as will fit in the outBuffer.
		if (mTrailerPos < mTrailer.length) {

			// Get the number of bytes that will fit in the outBuffer.
			int trailerSize = Math.min(mTrailer.length - mTrailerPos, maxLength - bIndex);

			// Move them in.
			for (int i = trailerSize; i > 0; i--) {
				outBuffer[bIndex++] = mTrailer[mTrailerPos++];
			}

			// Return the total number of bytes written during this call.
			return retval + trailerSize;
		}

		/**
		 * If we got this far, we have already been asked to read all content that is available.
		 *
		 * So we are at EOF.
		 */
		mReachedEOF = true;
		return -1;
	}

	/***
	 * Helper functions to construct the trailer.
	 */

	/*
	 * Write GZIP member trailer to a byte array, starting at a given offset.
	 */
	private void writeTrailer(byte[] buf, int offset) throws IOException {
		writeInt((int) mCRCInputStream.getCRCValue(), buf, offset);
		// CRC32 of uncompr. data
		writeInt(def.getTotalIn(), buf, offset + 4);
		// Number of uncompr. bytes
	}

	/*
	 * Write integer in Intel byte order to a byte array, starting at a given offset.
	 */
	private void writeInt(int i, byte[] buf, int offset) throws IOException {
		writeShort(i & 0xffff, buf, offset);
		writeShort((i >> 16) & 0xffff, buf, offset + 2);
	}

	/*
	 * Write short integer in Intel byte order to a byte array, starting at a given offset
	 */
	private void writeShort(int s, byte[] buf, int offset) throws IOException {
		buf[offset] = (byte) (s & 0xff);
		buf[offset + 1] = (byte) ((s >> 8) & 0xff);
	}
}