package com.example.streaming_ai_chat;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.StreamingChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.vaadin.firitin.components.messagelist.MarkdownMessage;

import java.util.ArrayList;

@PageTitle("🤖 AI Assistant Chat, can help you to solve a problem")
@Route("")
public class MainView extends VerticalLayout {

    private final ArrayList<Message> chatHistory = new ArrayList<>();

    VerticalLayout messageList = new VerticalLayout();
    Scroller messageScroller = new Scroller(messageList);
    MessageInput messageInput = new MessageInput();

    public MainView(StreamingChatModel chatClient) {
        add(messageScroller, messageInput);
        setSizeFull();
        setMargin(false);
        messageScroller.setSizeFull();
        messageInput.setWidthFull();

        chatHistory.add(new SystemMessage("""
                You are a knowledgeable and friendly AI assistant named Donald.
                Your role is to help users by answering their questions, providing information,
                and offering guidance to the best of your abilities.
                When responding, use a warm and professional tone, and break down complex topics into easy-to-understand explanations.
                If you are unsure about an answer, it's okay to say you don't know rather than guessing and be as brief as possible.
                """));
        messageInput.addSubmitListener(event -> {

            chatHistory.add(new UserMessage(event.getValue()));
            messageList.add(new MarkdownMessage(event.getValue(), "You:"));


            MarkdownMessage reply = new MarkdownMessage("Assistant:");
            messageList.add(reply);

            Prompt prompt = new Prompt(chatHistory);
            chatClient.stream(prompt)
                    .doOnComplete(() -> chatHistory.add(new AssistantMessage(reply.getMarkdown())))
                    .subscribe(c -> reply.appendMarkdownAsync(c.getResult().getOutput().getContent()));
            reply.scrollIntoView();
        });
    }

}
