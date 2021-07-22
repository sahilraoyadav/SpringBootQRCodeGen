package com.myproject.springbootqrgenapp.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.myproject.springbootqrgenapp.generator.QRCodeGenerator;

import lombok.var;

@RestController
@RequestMapping("/")
public class QRCodeController {

	private static final String QR_CODE_IMAGE_PATH = "./src/main/resources/QRCode.png";

	@GetMapping(value = "/genrateAndDownloadQRCode/{codeText}/{width}/{height}")
	public void download(@PathVariable("codeText") String codeText, @PathVariable("width") Integer width,
			@PathVariable("height") Integer height) throws Exception {
		QRCodeGenerator.generateQRCodeImage(codeText, width, height, QR_CODE_IMAGE_PATH);
	}

	@GetMapping(value = "/genrateQRCode/{codeText}/{width}/{height}")
	public ResponseEntity<byte[]> generateQRCode(@PathVariable("codeText") String codeText,
			@PathVariable("width") Integer width, @PathVariable("height") Integer height) throws Exception {
		return ResponseEntity.status(HttpStatus.OK).body(QRCodeGenerator.getQRCodeImage(codeText, width, height));
	}

	@GetMapping(value = "/image", produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<InputStreamResource> getImagea() throws IOException {
		var imgFile = new ClassPathResource("QRCode.png");
		return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG)
				.body(new InputStreamResource(imgFile.getInputStream()));
	}

	@GetMapping(value = "/")
	public ModelAndView getStart() {
		String text = String.format(
				  "1. Welcome to the qr code genreator. "
				+ "2. Add /gen/'text to be included in the qr'/'width'/'height' to the url to generate the qr code. "
				+ "3. You will see a qr code with the text and width * height! ");
		ModelAndView mav = new ModelAndView("welcomePage","WelcomeMessage","Welcome!");
		
		return mav;
	}

	@GetMapping(value = "/gen/{codeText}/{width}/{height}", produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<BufferedImage> zxingQRCode(@PathVariable("codeText") String codeText,
			@PathVariable("width") Integer width, @PathVariable("height") Integer height) throws Exception {
		return successResponse(QRCodeGenerator.genQrCode(codeText, width, height));
	}

	private ResponseEntity<BufferedImage> successResponse(BufferedImage image) {
		return new ResponseEntity<>(image, HttpStatus.OK);
	}

	@Bean
	public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
		return new BufferedImageHttpMessageConverter();
	}
}
