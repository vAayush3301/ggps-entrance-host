package av.entrance.host.host.controller;

import av.entrance.host.host.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/image")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public CompletableFuture<ResponseEntity<String>> upload(@RequestParam("file") MultipartFile file) {
        return imageService.uploadImage(file).thenApply(ResponseEntity::ok);
    }

    @GetMapping
    public ResponseEntity<String> getImageUrl(@RequestParam String key) {
        String signedUrl = imageService.generateSignedUrl(key);
        return ResponseEntity.ok(signedUrl);
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam String key) {
        System.out.println("Incoming delete call. Key = " + key);
        imageService.deleteImage(key);
        return ResponseEntity.ok("Image Deleted");
    }
}
