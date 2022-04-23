package cn.objectspace.webssh.controller;


import com.google.common.util.concurrent.RateLimiter;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/upload")
public class UploadController {


    final double permitsPerSecond = 1.0;        //每秒生成5个令牌
    final long warmupPeriod = 1;                //在warmupPeriod时间内RateLimiter会增加它的速率，在抵达它的稳定速率或者最大速率之前
    final TimeUnit timeUnit = TimeUnit.SECONDS; //参数warmupPeriod 的时间单位
    /*
     * 创建一个稳定输出令牌的RateLimiter，保证了平均每秒不超过qps个请求
     * 当请求到来的速度超过了qps，保证每秒只处理qps个请求
     * 当这个RateLimiter使用不足(即请求到来速度小于qps)，会囤积最多qps个请求
     *
     * 创建的是SmoothBursty 实例 平滑稳定
     */
    RateLimiter rateLimiter = RateLimiter.create(permitsPerSecond);

    /**
     * 根据指定的稳定吞吐率和预热期来创建RateLimiter，
     * 这里的吞吐率是指每秒多少许可数（通常是指QPS，每秒多少查询），
     * 在这段预热时间内，RateLimiter每秒分配的许可数会平稳地增长直到预热期结束时达到其最大速率（只要存在足够请求数来使其饱和）。
     * 同样地，如果RateLimiter 在warmupPeriod时间内闲置不用，它将会逐步地返回冷却状态。
     * 也就是说，它会像它第一次被创建般经历同样的预热期。
     * 返回的RateLimiter 主要用于那些需要预热期的资源，这些资源实际上满足了请求（比如一个远程服务），
     * 而不是在稳定（最大）的速率下可以立即被访问的资源。
     * 返回的RateLimiter 在冷却状态下启动（即预热期将会紧跟着发生），并且如果被长期闲置不用，它将回到冷却状态
     * <p>
     * 创建的是SmoothWarmingUp实例    平滑预热
     */
    RateLimiter rl = RateLimiter.create(permitsPerSecond, warmupPeriod, timeUnit);

    //统计
    int countSuccess,countFail = 0;


    @RequestMapping("/files")
    public ResponseEntity upload1(@RequestParam("files") MultipartFile[] file, HttpServletResponse response) throws Exception {

        List<String> urlList = new ArrayList<>();
        String path = System.getProperty("user.home") + "/upload/";
        if (!Files.exists(Paths.get(path))) {
            Files.createDirectory(Paths.get(path));
        }
        // 限流
        boolean flag = rateLimiter.tryAcquire();
        if (flag) {
            for (MultipartFile multipartFile : file) {
                byte[] bytes = multipartFile.getBytes();
                String originalFilename = multipartFile.getOriginalFilename();
                String name = path + originalFilename;
                IOUtils.write(bytes, new FileOutputStream(name));
                urlList.add(Base64Utils.encodeToString(name.getBytes(StandardCharsets.UTF_8)));
            }
            countSuccess++;//并发时，统计不准确!!!

        } else {
            countFail++;
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(urlList);
    }
}
