package com.example.helloworld.controller

import com.example.helloworld.service.S3Service
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.slf4j.LoggerFactory

@RestController
@RequestMapping("/api/s3")
class S3Controller(private val s3Service: S3Service) {

    private val logger = LoggerFactory.getLogger(S3Controller::class.java)

    @PostMapping("/upload")
    fun uploadFile(@RequestParam("file") file: MultipartFile): ResponseEntity<String> {
        return try {
            logger.info("Received file: ${file.originalFilename}")
            val fileUrl = s3Service.uploadFile(file)
            logger.info("File uploaded successfully: $fileUrl")
            ResponseEntity.ok(fileUrl)
        } catch (e: Exception) {
            logger.error("File upload failed", e)
            ResponseEntity.internalServerError().body("Failed to upload file: ${e.message}")
        }
    }
}
