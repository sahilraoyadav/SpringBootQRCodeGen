package com.myproject.springbootqrgenapp.controller;

import java.io.IOException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.myproject.springbootqrgenapp.generator.QRCodeGenerator;

import lombok.var;

@RestController
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
	public String getStart()  {
		return "hello world";
	}
}
