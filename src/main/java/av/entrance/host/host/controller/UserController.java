package av.entrance.host.host.controller;

import av.entrance.host.host.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @PostMapping("/createUser")
    public ResponseEntity<String> createTest(@RequestParam String clientId, @RequestBody User user) {
        System.out.println(clientId);
        DatabaseReference ref =
                FirebaseDatabase.getInstance()
                        .getReference(clientId)
                        .child("users")
                        .child(clientId)
                        .child(user.getUserId());

        ref.setValueAsync(user);

        return ResponseEntity.ok("User created with key: " + ref.getKey());
    }

    @PostMapping("/updateUser")
    public ResponseEntity<String> updateUser(@RequestParam String clientId, @RequestBody User user) {
        System.out.println(clientId);
        DatabaseReference ref =
                FirebaseDatabase.getInstance()
                        .getReference(clientId)
                        .child("users")
                        .child(clientId)
                        .child(user.getUserId());

        ref.setValueAsync(user);

        return ResponseEntity.ok("User updated with key: " + ref.getKey());
    }
}
