package org.ada.datapi.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.NullOutputStream;

public class FileUtils {

	// **************************************************************

	static public void setContentFile(String pathFile, String contents) {

		pathFile = pathFile.replace("\\", "/");
		try {
			new File(pathFile.substring(0, pathFile.lastIndexOf("/"))).mkdirs();
		} catch (Exception e) {
		}
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(pathFile));
			out.write(contents);
			out.close();
		} catch (Exception e) {
			//SimpleLogger.setError("No se puede escribir en el archivo [" + pathFile + "]", e);
		}
	}

	static public StringBuffer getContentFile(File aFile) {

		StringBuffer contents = new StringBuffer();

		BufferedReader input = null;
		try {
			input = new BufferedReader(new FileReader(aFile));
			String line = null;
			while ((line = input.readLine()) != null) {
				contents.append(line + "\n");
			}
		} catch (FileNotFoundException ex) {
			return null;
		} catch (IOException ex) {
			return null;
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException ex) {
			}
		}
		return contents;
	}

	static public StringBuffer getContentFile(String pathFile) {

		File aFile = new File(pathFile);

		return getContentFile(aFile);
	}

	static public List<String> getContentFileList(String pathFile) {

		File aFile = new File(pathFile);
		List<String> contents = new ArrayList<String>();

		BufferedReader input = null;
		try {
			input = new BufferedReader(new FileReader(aFile));
			String line = null;
			while ((line = input.readLine()) != null) {
				contents.add(line);
			}
		} catch (FileNotFoundException ex) {
			return null;
		} catch (IOException ex) {
			return null;
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException ex) {
			}
		}
		return contents;
	}

	static public String[] getContentFileToArray(String pathFile) {

		StringBuffer contents = getContentFile(pathFile);
		return contents.toString().split("\n");
	}

	public static boolean copyURLToFile(URL in_url, File out_file) {

		try {
			InputStream in = in_url.openStream();

			OutputStream out = new FileOutputStream(out_file);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();

			return true;

		} catch (Throwable e) {
			//SimpleLogger.setError("copyURLToFile: " + in_url + ", " + out_file, e);

		}

		return false;

	}

	@SuppressWarnings("deprecation")
	public static Checksum checksum(File file, Checksum checksum) throws IOException {
		if (file.isDirectory()) {
			throw new IllegalArgumentException("Checksums can't be computed on directories");
		}
		InputStream in = null;
		try {
			in = new CheckedInputStream(new FileInputStream(file), checksum);
			IOUtils.copy(in, new NullOutputStream());
		} finally {
			IOUtils.closeQuietly(in);
		}
		return checksum;
	}

	public static long checksumCRC32(File file) throws IOException {
		CRC32 crc = new CRC32();
		checksum(file, crc);
		return crc.getValue();
	}
	
	/**
	 * Copy file <code>src</code> to <code>dst</code>. If <code>dst</code> file
	 * doesn't exists, it's created
	 * 
	 * @param src source file
	 * @param dst destination file
	 * @throws IOException
	 */
	public static void copy(File src, File dst) throws IOException{
		if (src.isDirectory() || dst.isDirectory()) {
			throw new IllegalArgumentException("Can't copy directories");
		}
		
		InputStream in = new FileInputStream(src);
	    OutputStream out = new FileOutputStream(dst);

	    // Transfer bytes from in to out
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) {
	        out.write(buf, 0, len);
	    }
	    in.close();
	    out.close();
	}

}
