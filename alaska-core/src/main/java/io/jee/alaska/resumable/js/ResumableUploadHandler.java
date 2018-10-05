package io.jee.alaska.resumable.js;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import io.jee.alaska.resumable.js.ResumableInfo.ResumableChunkNumber;

/**
 *
 * using Resumable.js to upload files.
 *
 */
@Component
public class ResumableUploadHandler {
	
	private String uploadDir = "upload_dir";
	@Resource
	private ResumableInfoStorage resumableInfoStorage;
	@Resource
	private RedisTemplate<String, ResumableChunkNumber> redisTemplate;
	
    public String getUploadDir() {
		return uploadDir;
	}

	public void setUploadDir(String uploadDir) {
		this.uploadDir = uploadDir;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int resumableChunkNumber        = getResumableChunkNumber(request);

        ResumableInfo info = getResumableInfo(request);

        RandomAccessFile raf = new RandomAccessFile(info.resumableFilePath, "rw");

        //Seek to position
        raf.seek((resumableChunkNumber - 1) * (long)info.resumableChunkSize);

        //Save to file
        InputStream is = request.getInputStream();
        long readed = 0;
        long content_length = request.getContentLength();
        byte[] bytes = new byte[1024 * 128];
        while(readed < content_length) {
            int r = is.read(bytes);
            if (r < 0)  {
                break;
            }
            raf.write(bytes, 0, r);
            readed += r;
        }
        raf.close();

        BoundSetOperations<String, ResumableChunkNumber> boundSetOperations = redisTemplate.boundSetOps(info.resumableFilename);
        boundSetOperations.expire(6, TimeUnit.HOURS);
        //Mark as uploaded.
        boundSetOperations.add(new ResumableInfo.ResumableChunkNumber(resumableChunkNumber));
        Set<ResumableChunkNumber> numbers = redisTemplate.boundSetOps(info.resumableFilename).members();
        if (info.checkIfUploadFinished(numbers)) { //Check if all chunks uploaded, and change filename
        	resumableInfoStorage.remove(info);
            response.getWriter().print("All finished.");
        } else {
            response.getWriter().print("Upload");
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int resumableChunkNumber        = getResumableChunkNumber(request);

        ResumableInfo info = getResumableInfo(request);
        Set<ResumableChunkNumber> numbers = redisTemplate.boundSetOps(info.resumableFilename).members();
        if (numbers.contains(new ResumableInfo.ResumableChunkNumber(resumableChunkNumber))) {
            response.getWriter().print("Uploaded."); //This Chunk has been Uploaded.
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private int getResumableChunkNumber(HttpServletRequest request) {
        return HttpUtils.toInt(request.getParameter("resumableChunkNumber"), -1);
    }

    private ResumableInfo getResumableInfo(HttpServletRequest request) throws ServletException {

        int resumableChunkSize          = HttpUtils.toInt(request.getParameter("resumableChunkSize"), -1);
        long resumableTotalSize         = HttpUtils.toLong(request.getParameter("resumableTotalSize"), -1);
        String resumableIdentifier      = request.getParameter("resumableIdentifier");
        String resumableFilename        = request.getParameter("resumableFilename");
        String resumableRelativePath    = request.getParameter("resumableRelativePath");
        //Here we add a ".temp" to every upload file to indicate NON-FINISHED
        File uploadDirFile = new File(uploadDir);
        if(!uploadDirFile.exists()) uploadDirFile.mkdirs();
        String resumableFilePath        = new File(uploadDir, resumableFilename).getAbsolutePath() + ".temp";


        ResumableInfo info = resumableInfoStorage.get(resumableChunkSize, resumableTotalSize,
                resumableIdentifier, resumableFilename, resumableRelativePath, resumableFilePath);
        if (!info.vaild())         {
        	resumableInfoStorage.remove(info);
            throw new ServletException("Invalid request params.");
        }
        return info;
    }
}
