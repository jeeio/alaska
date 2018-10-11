package io.jee.alaska.util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.WebRequest;

public class WebFileReaderUtils {
	
	private static final int BUFFER_LENGTH = 1024 * 64;
	private static final long EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;
	private static final Pattern RANGE_PATTERN = Pattern.compile("bytes=(?<start>\\d*)-(?<end>\\d*)");
	
	public static void partial(Path path, WebRequest request, HttpServletResponse response) throws IOException {
	    long length = Files.size(path);
	    long start = 0;
	    long end = length - 1;

	    String range = request.getHeader("Range");
	    range=range==null?"":range;
	    Matcher matcher = RANGE_PATTERN.matcher(range);

	    if (matcher.matches()) {
			String startGroup = matcher.group("start");
			start = startGroup.isEmpty() ? start : Long.valueOf(startGroup);
			start = start < 0 ? 0 : start;
			
			String endGroup = matcher.group("end");
			end = endGroup.isEmpty() ? end : Long.valueOf(endGroup);
			end = end > length - 1 ? length - 1 : end;
	    }

	    long contentLength = end - start + 1;
	    
	    response.reset();
	    response.setBufferSize(BUFFER_LENGTH);
	    response.setHeader("Accept-Ranges", "bytes");
	    response.setDateHeader("Last-Modified", Files.getLastModifiedTime(path).toMillis());
	    response.setDateHeader("Expires", System.currentTimeMillis() + EXPIRE_TIME);
//	    response.setContentType(Files.probeContentType(path));
	    response.setContentType(new MimetypesFileTypeMap().getContentType(path.toFile()));
	    response.setContentLengthLong(contentLength);
	    if(StringUtils.hasText(range)) {
	    	response.setHeader("Content-Range", String.format("bytes %s-%s/%s", start, end, length));
	    	response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
	    }

	    int bytesRead;
	    long bytesLeft = contentLength;
	    ByteBuffer buffer = ByteBuffer.allocate(BUFFER_LENGTH);

	    SeekableByteChannel input = Files.newByteChannel(path, StandardOpenOption.READ);
	    OutputStream output = response.getOutputStream();
	    try {

	    	input.position(start);

	    	while ((bytesRead = input.read(buffer)) != -1 && bytesLeft > 0) {
		        buffer.clear();
		        output.write(buffer.array(), 0, bytesLeft < bytesRead ? (int) bytesLeft : bytesRead);
		        bytesLeft -= bytesRead;
	    	}
//			while ((bytesRead = input.read(buffer)) != -1) {
//				buffer.clear();
//				output.write(buffer.array(), 0, bytesRead);
//			}
	    }catch (IOException e) {
		}finally {
			input.close();
			output.close();
		}
	}
	
	public static void partialDownload(Path path, String name, WebRequest request, HttpServletResponse response) throws IOException {
	    long length = Files.size(path);
	    long start = 0;
	    long end = length - 1;

	    String range = request.getHeader("Range");
	    range=range==null?"":range;
	    Matcher matcher = RANGE_PATTERN.matcher(range);

	    if (matcher.matches()) {
		    String startGroup = matcher.group("start");
		    start = startGroup.isEmpty() ? start : Long.valueOf(startGroup);
		    start = start < 0 ? 0 : start;
	
		    String endGroup = matcher.group("end");
		    end = endGroup.isEmpty() ? end : Long.valueOf(endGroup);
		    end = end > length - 1 ? length - 1 : end;
	    }

	    long contentLength = end - start + 1;

	    response.reset();
	    response.setBufferSize(BUFFER_LENGTH);
		String userAgent = request.getHeader("User-Agent").toLowerCase();
		if (userAgent.indexOf("firefox") > -1) {
			name = new String(name.getBytes("UTF-8"), "iso-8859-1");
		}else {
			name = URLEncoder.encode(name, "UTF-8");
		}
		response.setHeader("Content-Disposition", String.format("attachment;filename=\"%s\"", name));
	    response.setHeader("Accept-Ranges", "bytes");
	    response.setDateHeader("Last-Modified", Files.getLastModifiedTime(path).toMillis());
	    response.setDateHeader("Expires", System.currentTimeMillis() + EXPIRE_TIME);
//	    response.setContentType(Files.probeContentType(path));
	    response.setContentType(new MimetypesFileTypeMap().getContentType(path.toFile()));
	    response.setContentLengthLong(contentLength);
	    if(StringUtils.hasText(range)) {
	    	response.setHeader("Content-Range", String.format("bytes %s-%s/%s", start, end, length));
	    	response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
	    }

	    int bytesRead;
	    long bytesLeft = contentLength;
	    ByteBuffer buffer = ByteBuffer.allocate(BUFFER_LENGTH);

	    SeekableByteChannel input = Files.newByteChannel(path, StandardOpenOption.READ);
	    OutputStream output = response.getOutputStream();
	    try {

	    	input.position(start);

	    	while ((bytesRead = input.read(buffer)) != -1 && bytesLeft > 0) {
		        buffer.clear();
		        output.write(buffer.array(), 0, bytesLeft < bytesRead ? (int) bytesLeft : bytesRead);
		        bytesLeft -= bytesRead;
		    }
	    }catch (IOException e) {
		}finally {
			input.close();
			output.close();
		}
	}
	
	public static void checkNotModified(Path path, WebRequest request, HttpServletResponse response) throws IOException {
		if(request.checkNotModified(Files.getLastModifiedTime(path).toMillis())){
			return;
		}
		
		response.reset();
	    response.setBufferSize(BUFFER_LENGTH);
	    response.setHeader("Cathe-Control", "public, max-age=" + (EXPIRE_TIME/1000));
	    response.setDateHeader("Last-Modified", Files.getLastModifiedTime(path).toMillis());
	    response.setDateHeader("Expires", System.currentTimeMillis() + EXPIRE_TIME);
//	    response.setContentType(Files.probeContentType(path));
	    response.setContentType(new MimetypesFileTypeMap().getContentType(path.toFile()));
	    response.setContentLengthLong(Files.size(path));

	    int bytesRead;
	    ByteBuffer buffer = ByteBuffer.allocate(BUFFER_LENGTH);

	    SeekableByteChannel input = Files.newByteChannel(path, StandardOpenOption.READ);
		OutputStream output = response.getOutputStream();
	    try {
			while ((bytesRead = input.read(buffer)) != -1) {
				buffer.clear();
				output.write(buffer.array(), 0, bytesRead);
			}
	    }catch (IOException e) {
		}finally {
			input.close();
			output.close();
		}
	}

}
