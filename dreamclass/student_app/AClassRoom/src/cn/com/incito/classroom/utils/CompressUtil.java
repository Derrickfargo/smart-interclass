package cn.com.incito.classroom.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class CompressUtil {

	/**
	 * Answer a byte array compressed in the Zip format from bytes.
	 * 
	 * @param bytes
	 *            a byte array
	 * @param aName
	 *            a String the represents a file name
	 * @return byte[] compressed bytes
	 * @throws IOException
	 */
	public static byte[] zipBytes(byte[] bytes) throws IOException {
		ByteArrayOutputStream tempOStream = null;
		BufferedOutputStream tempBOStream = null;
		ZipOutputStream tempZStream = null;
		ZipEntry tempEntry = null;
		byte[] tempBytes = null;

		tempOStream = new ByteArrayOutputStream(bytes.length);
		tempBOStream = new BufferedOutputStream(tempOStream);
		tempZStream = new ZipOutputStream(tempBOStream);
		tempEntry = new ZipEntry(String.valueOf(bytes.length));
		tempEntry.setMethod(ZipEntry.DEFLATED);
		tempEntry.setSize((long) bytes.length);

		tempZStream.putNextEntry(tempEntry);
		tempZStream.write(bytes, 0, bytes.length);
		tempZStream.flush();
		tempBOStream.flush();
		tempOStream.flush();
		tempZStream.close();
		tempBytes = tempOStream.toByteArray();
		tempOStream.close();
		tempBOStream.close();
		return tempBytes;
	}

	/**
	 * Answer a byte array that has been decompressed from the Zip format.
	 * 
	 * @param bytes
	 *            a byte array of compressed bytes
	 * @return
	 * @return byte[] uncompressed bytes
	 * @throws IOException
	 */
	public static byte[] unzipBytes(byte[] bytes) {
		OutputStream os =null;
		ByteArrayInputStream tempIStream = null;
		BufferedInputStream tempBIStream = null;
		ZipInputStream tempZIStream = null;
		ZipEntry tempEntry = null;
		long tempDecompressedSize = -1;
		byte[] tempUncompressedBuf = null;
		try {
			tempIStream = new ByteArrayInputStream(bytes, 0, bytes.length);
			tempBIStream = new BufferedInputStream(tempIStream);
			tempZIStream = new ZipInputStream(tempBIStream);
			tempEntry = tempZIStream.getNextEntry();
			if (tempEntry != null) {
				tempDecompressedSize = tempEntry.getCompressedSize();
				if (tempDecompressedSize < 0) {
					tempDecompressedSize = Long.parseLong(tempEntry.getName());
				}

				int size = (int) tempDecompressedSize;
				tempUncompressedBuf = new byte[size];
				int num = 0, count = 0;
				while (true) {
					count = tempZIStream.read(tempUncompressedBuf, 0, size - num);
					num += count;
					os.write(tempUncompressedBuf, 0, count);
					os.flush();
					if (num >= size)
						break;
				}
			}
			tempZIStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tempUncompressedBuf;
	}
	
	/***
	  * 压缩GZip
	  * 
	  * @param data
	  * @return
	  */
	 public static byte[] gZip(byte[] data) {
	  byte[] b = null;
	  try {
	   ByteArrayOutputStream bos = new ByteArrayOutputStream();
	   GZIPOutputStream gzip = new GZIPOutputStream(bos);
	   gzip.write(data);
	   gzip.finish();
	   gzip.close();
	   b = bos.toByteArray();
	   bos.close();
	  } catch (Exception ex) {
	   ex.printStackTrace();
	  }
	  return b;
	 }
	 /***
	  * 解压GZip
	  * 
	  * @param data
	  * @return
	  */
	 public static byte[] unGZip(byte[] data) {
	  byte[] b = null;
	  try {
	   ByteArrayInputStream bis = new ByteArrayInputStream(data);
	   GZIPInputStream gzip = new GZIPInputStream(bis);
	   byte[] buf = new byte[1024];
	   int num = -1;
	   ByteArrayOutputStream baos = new ByteArrayOutputStream();
	   while ((num = gzip.read(buf, 0, buf.length)) != -1) {
	    baos.write(buf, 0, num);
	   }
	   b = baos.toByteArray();
	   baos.flush();
	   baos.close();
	   gzip.close();
	   bis.close();
	  } catch (Exception ex) {
	   ex.printStackTrace();
	  }
	  return b;
	 }
	
}
