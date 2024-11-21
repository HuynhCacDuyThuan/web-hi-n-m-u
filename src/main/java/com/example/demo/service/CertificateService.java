package com.example.demo.service;

import com.example.demo.dto.VolunteerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import jakarta.mail.internet.MimeMessage;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.rendering.ImageType;

@Service
public class CertificateService {

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private JavaMailSender mailSender;

	// Generate HTML from Thymeleaf template
	public String generateCertificateHtml(VolunteerDTO volunteer) {
		Context context = new Context();
		context.setVariable("volunteer", volunteer);
		return templateEngine.process("certificate", context);
	}

	public byte[] generatePdfFromHtml(String html) throws Exception {
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			PdfRendererBuilder builder = new PdfRendererBuilder();

			builder.withHtmlContent(html, null);

			// Set page size to larger than A3 landscape to ensure the full width
			builder.useDefaultPageSize(20f, 11.7f, PdfRendererBuilder.PageSizeUnits.INCHES);

			builder.toStream(outputStream);
			builder.run();
			return outputStream.toByteArray();
		}
	}

	// Convert PDF to JPEG
	public byte[] convertPdfToJpeg(byte[] pdfBytes) throws Exception {
		try (PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfBytes))) {
			PDFRenderer pdfRenderer = new PDFRenderer(document);
			BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB); // Renders the first
																									// page at 300 DPI

			// Convert BufferedImage to JPEG byte array
			try (ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream()) {
				ImageIO.write(bufferedImage, "jpg", jpegOutputStream);
				return jpegOutputStream.toByteArray();
			}
		}
	}

	// Send an email with the JPEG certificate attached
	public void sendCertificateEmail(VolunteerDTO volunteer, byte[] jpegBytes, String recipientEmail) throws Exception {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		helper.setTo(recipientEmail);
		helper.setSubject("Blood Donation Certificate");
		helper.setText(
				"Dear " + volunteer.getFullName() + ",\n\nAttached is the blood donation certificate.\n\nThank you!");

		// Attach the JPEG with the name "BloodDonationCertificate.jpg"
		helper.addAttachment("BloodDonationCertificate.jpg", new ByteArrayResource(jpegBytes));

		// Send the email with the attachment
		mailSender.send(message);
	}

	// Issue certificate: Generate HTML, convert to JPEG, and send via email
	public void issueCertificate(VolunteerDTO volunteer, String email) throws Exception {
		// Generate HTML from the template
		String html = generateCertificateHtml(volunteer);

		// Convert HTML to PDF
		byte[] pdfBytes = generatePdfFromHtml(html);

		// Convert PDF to JPEG
		byte[] jpegBytes = convertPdfToJpeg(pdfBytes);

		// Send the email with the generated JPEG attachment
		sendCertificateEmail(volunteer, jpegBytes, email);
	}
}