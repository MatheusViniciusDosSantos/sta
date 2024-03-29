package br.com.sta.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender javaMailSender; 
	
	@Value("${spring.mail.username}")
	private String remetente;

	public String enviarEmailTexto(String destinatario, String titulo, String messagem) {
		try {
			SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
			simpleMailMessage.setFrom(remetente);
			simpleMailMessage.setTo(destinatario);
			simpleMailMessage.setSubject(titulo);
			simpleMailMessage.setText(messagem);
			javaMailSender.send(simpleMailMessage);
			return "Email enviado";
		} catch(Exception e) {
			return "Erro ao enviar e-mail";
		}
	}
}
