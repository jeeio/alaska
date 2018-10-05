package io.jee.alaska.resumable.js;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * by fanxu
 */
@Component
public class ResumableInfoStorage {
	
	@Resource
	private RedisTemplate<String, ResumableInfo> redisTemplate;

    //resumableIdentifier --  ResumableInfo
//    private HashMap<String, ResumableInfo> mMap = new HashMap<String, ResumableInfo>();

    /**
     * Get ResumableInfo from mMap or Create a new one.
     * @param resumableChunkSize
     * @param resumableTotalSize
     * @param resumableIdentifier
     * @param resumableFilename
     * @param resumableRelativePath
     * @param resumableFilePath
     * @return
     */
    public synchronized ResumableInfo get(int resumableChunkSize, long resumableTotalSize,
                             String resumableIdentifier, String resumableFilename,
                             String resumableRelativePath, String resumableFilePath) {
        ResumableInfo info = redisTemplate.boundValueOps(resumableIdentifier).get();
        if (info == null) {
            info = new ResumableInfo();

            info.resumableChunkSize     = resumableChunkSize;
            info.resumableTotalSize     = resumableTotalSize;
            info.resumableIdentifier    = resumableIdentifier;
            info.resumableFilename      = resumableFilename;
            info.resumableRelativePath  = resumableRelativePath;
            info.resumableFilePath      = resumableFilePath;

            BoundValueOperations<String, ResumableInfo> operations = redisTemplate.boundValueOps(resumableIdentifier);
            operations.set(info);
            operations.expire(6, TimeUnit.HOURS);
        }
        return redisTemplate.boundValueOps(resumableIdentifier).get();
    }

    /**
     * ɾ��ResumableInfo
     * @param info
     */
    public void remove(ResumableInfo info) {
    	redisTemplate.opsForValue().getOperations().delete(info.resumableIdentifier);
    }
}
