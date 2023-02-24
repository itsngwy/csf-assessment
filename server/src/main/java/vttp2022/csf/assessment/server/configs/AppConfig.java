package vttp2022.csf.assessment.server.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AppConfig {

	// Digital Ocean for image/files
	// @Value("${spaces.secret.key}")
	@Value("${SPACES_SECRET_KEY}")
	private String spacesSecretKey;
	// @Value("${spaces.access.key}")
	@Value("${SPACES_ACCESS_KEY}")
	private String spacesAccessKey;

	// Digital Ocean endpoint
	@Value("${spaces.endpoint.url}")
	private String spacesEndpointUrl;
	@Value("${spaces.endpoint.region}")
	private String spacesRegion;
	
	// Digital Ocean
	@Bean
	public AmazonS3 createS3Client() {
		BasicAWSCredentials cred = new BasicAWSCredentials(spacesAccessKey, spacesSecretKey);
		EndpointConfiguration epConfig = new EndpointConfiguration(spacesEndpointUrl, spacesRegion);

		return AmazonS3ClientBuilder.standard()
				.withEndpointConfiguration(epConfig)
				.withCredentials(new AWSStaticCredentialsProvider(cred))
				.build();
	}

}
