package av.entrance.host.host.controller;

import av.entrance.host.host.service.UploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/image")
public class ImageController {
    private final UploadService uploadService;

    public ImageController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping("/upload")
    public CompletableFuture<ResponseEntity<String>> upload(@RequestParam("file") MultipartFile file) {
        return uploadService.uploadImage(file).thenApply(ResponseEntity::ok);
    }

    @GetMapping
    public ResponseEntity<String> getImageUrl(@RequestParam String key) {
        String signedUrl = uploadService.generateSignedUrl(key);
        return ResponseEntity.ok(signedUrl);
    }
}
