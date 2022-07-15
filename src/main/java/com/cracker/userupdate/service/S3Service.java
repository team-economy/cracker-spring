package com.cracker.userupdate.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@NoArgsConstructor
public class S3Service {
    private AmazonS3 s3Client;

    @Value("${cloud.aws.credentials.accesskey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretkey}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    public String upload(MultipartFile multipartFile, String fileName) {
        ObjectMetadata objectMetaData = new ObjectMetadata();
        objectMetaData.setContentLength(multipartFile.getSize());
        objectMetaData.setContentType(multipartFile.getContentType());
        try (InputStream inputStream = multipartFile.getInputStream()){
            return putS3(inputStream, objectMetaData, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "fail";
    }
    private String putS3(InputStream inputStream, ObjectMetadata objectMetaData, String fileName) {
        s3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetaData).withCannedAcl(CannedAccessControlList.PublicRead));
        return s3Client.getUrl(bucket, fileName).toString();
    }

}

