//package com.gray.Utils;
//
//@PostMapping("/upload")
//public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
//    
//    // ✅ (1) Empty check
//    if (file.isEmpty()) {
//        return ResponseEntity.badRequest().body("File cannot be empty");
//    }
//
//    // ✅ (2) Type check (MIME type)
//    String contentType = file.getContentType();
//    if (contentType == null || 
//        !(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
//        return ResponseEntity.badRequest().body("Only JPEG and PNG images are allowed");
//    }
//
//    // ✅ (3) Size check (e.g., 2 MB max)
//    long maxSize = 2 * 1024 * 1024; // 2 MB
//    if (file.getSize() > maxSize) {
//        return ResponseEntity.badRequest().body("File size exceeds 2 MB limit");
//    }
//
//    // ✅ (4) Optional: Dimension check
//    BufferedImage image = ImageIO.read(file.getInputStream());
//    if (image == null) {
//        return ResponseEntity.badRequest().body("Invalid image file");
//    }
//    int width = image.getWidth();
//    int height = image.getHeight();
//    if (width > 2000 || height > 2000) {
//        return ResponseEntity.badRequest().body("Image dimensions are too large");
//    }
//
//    // ✅ If all good
//    return ResponseEntity.ok("Image uploaded successfully");
//}
