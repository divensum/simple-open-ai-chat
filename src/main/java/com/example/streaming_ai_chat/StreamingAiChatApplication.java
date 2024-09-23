package com.example.streaming_ai_chat;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Push
public class StreamingAiChatApplication implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(StreamingAiChatApplication.class, args);
	}

}
