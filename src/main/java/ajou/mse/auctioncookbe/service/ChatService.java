package ajou.mse.auctioncookbe.service;

import ajou.mse.auctioncookbe.entity.User;
import ajou.mse.auctioncookbe.entity.WaitingRoom;
import ajou.mse.auctioncookbe.repository.UserRepository;
import ajou.mse.auctioncookbe.repository.WaitingRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {

    private List<String> chatList = new ArrayList<>();

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WaitingRoomRepository waitingRoomRepository;

    public String postChat(String roomID, String userID, String chat) {
        // Room Check and User Check
        Optional<User> userOptional = userRepository.findById(userID);
        if (userOptional.isEmpty()) {
            return "FAILED: USER";
        }

        Optional<WaitingRoom> waitingRoomOptional = waitingRoomRepository.findById(roomID);
        if (waitingRoomOptional.isEmpty()) {
            return "FAILED: ROOM";
        }

        User user = userOptional.get();
        WaitingRoom waitingRoom = waitingRoomOptional.get();

        chatList.add(user.getName() + "@" + waitingRoom.getRoomCode() + ": " + chat);

        return "SUCCESS";
    }

    public List<String> fetchChat() {
        return chatList;
    }
}
