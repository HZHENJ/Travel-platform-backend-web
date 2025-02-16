package com.example.backendweb.Controller.Media;

import com.example.backendweb.Services.Meida.MediaService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @ClassName MediaProxyController
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2025/2/2
 * @Version 1.0
 */

@RestController
@RequestMapping("/api/proxy/media")
public class MediaProxyController {

    private final MediaService mediaService;

    @Autowired
    public MediaProxyController(MediaService mediaService){
        this.mediaService = mediaService;
    }

    @GetMapping("/{uuid}")
    public void getMedia(
            @PathVariable("uuid") String uuid,
            @RequestParam(name = "fileType", required = false) String fileType,
            HttpServletResponse response
    ) throws IOException {
        ResponseEntity<byte[]> externalResponse = mediaService.getMediaFile(uuid, fileType);
        if (externalResponse.getStatusCode().is2xxSuccessful() && externalResponse.getBody() != null) {
            // Set the Content-Type of the response (e.g. image/jpg)
            MediaType contentType = externalResponse.getHeaders().getContentType();
            response.setContentType(contentType != null ? contentType.toString() : "application/octet-stream");
            // Write the acquired image binary data to the response output stream
            StreamUtils.copy(externalResponse.getBody(), response.getOutputStream());
        } else {
            // Returns an error status code when the request fails
            response.sendError(externalResponse.getStatusCodeValue(), "Can not get Media File!");
        }
    }
}
