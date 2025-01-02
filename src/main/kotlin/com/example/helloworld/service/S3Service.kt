package com.example.helloworld.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.model.S3Exception
import software.amazon.awssdk.core.sync.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Service
class S3Service(private val s3Client: S3Client) {

    @Value("\${aws.s3.bucket-name}")
    private lateinit var bucketName: String

    fun uploadFile(file: MultipartFile): String {
        val fileName = System.currentTimeMillis().toString() + "_" + file.originalFilename
        val tempFile = convertMultiPartToFile(file)

        try {
            val putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build()

            s3Client.putObject(
                putObjectRequest,
                RequestBody.fromFile(tempFile) // 파일을 읽어 RequestBody로 변환
            )
        } catch (e: S3Exception) {
            throw RuntimeException("Failed to upload file to S3: ${e.message}", e)
        } finally {
            tempFile.delete() // 임시 파일 삭제
        }

        // 업로드한 파일의 S3 URL 반환
        return "https://${bucketName}.s3.amazonaws.com/${fileName}"
    }

    private fun convertMultiPartToFile(file: MultipartFile): File {
        val convertedFile = File(file.originalFilename!!)
        FileOutputStream(convertedFile).use { fos -> fos.write(file.bytes) }
        return convertedFile
    }
}
