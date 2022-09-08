package com.afb.dsi.numarch;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afb.dsi.numarch.services.IManageFileService;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@EnableScheduling
@EnableAutoConfiguration
@EnableSwagger2
//
@RestController
@SpringBootApplication
public class NumarchApplication implements CommandLineRunner {
	
	@Resource
	IManageFileService storageService;
	
//	@Autowired
//	CloseableHttpClient httpClient;
	
	@Value("${api.host.baseurl}")
	private String apiHost;
	
	public static void main(String[] args) {
		SpringApplication.run(NumarchApplication.class, args);
	}
	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.afb.dsi.numarch"))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(apiInfo());
	}
	
//   @Bean
//   public WebMvcConfigurer corsConfigurer() {
//      return new WebMvcConfigurer() {
//         @Override
//         public void addCorsMappings(CorsRegistry registry) {
//            registry.addMapping("/**").allowedOrigins("http://localhost:8080");
//	          registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");
//         }
//      };
//   }
	
//	@Bean
//	public RestTemplate restTemplate() {
//	    RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
//	    restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(apiHost));
//	    return restTemplate;
//	}
//	 
//	@Bean
//	@ConditionalOnMissingBean
//	public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
//	    HttpComponentsClientHttpRequestFactory clientHttpRequestFactory 
//	                        = new HttpComponentsClientHttpRequestFactory();
//	    clientHttpRequestFactory.setHttpClient(httpClient);
//	    return clientHttpRequestFactory;
//	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Numerisation et archivage des documents Spring Boot REST API Documentation")
				.description("REST APIs ")
				.contact(new Contact("Rodrigue T", "https://afrilandfirstbank.com/", "rodrigue_toukam@afrilandfirstbank.com"))
				.version("1.0")
				.build();
	}


	@RequestMapping(value = "/hello")
	public String helloWorld() {
		return "HELLO NUMARCH !";
	}

	@Override
	public void run(String... args) throws Exception {
		storageService.deleteAll();
	    storageService.init();
	}
	
}
