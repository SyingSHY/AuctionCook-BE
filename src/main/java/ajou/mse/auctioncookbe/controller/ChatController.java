package ajou.mse.auctioncookbe.controller;

import ajou.mse.auctioncookbe.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/chat")
    public List<String> fetchChat() {
        return chatService.fetchChat();
    }

    @PostMapping("/chat")
    public String postChat(@RequestParam String roomID, @RequestParam String userUUID, @RequestBody String chat) {
        return chatService.postChat(roomID, userUUID, chat);
    }
}
