package av.entrance.host.host.controller;

import av.entrance.host.host.model.Test;
import com.google.firebase.database.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @PostMapping("/create")
    public ResponseEntity<String> createTest(@RequestBody Test test) {

        DatabaseReference ref =
                FirebaseDatabase.getInstance()
                        .getReference("tests")
                        .push();

        ref.setValueAsync(test);

        return ResponseEntity.ok("Test created with key: " + ref.getKey());
    }

    @GetMapping("/get_tests")
    public CompletableFuture<List<Test>> getAllTests() {

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("tests");

        CompletableFuture<List<Test>> future = new CompletableFuture<>();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Test> list = new ArrayList<>();

                for (DataSnapshot child : snapshot.getChildren()) {
                    Test test = child.getValue(Test.class);
                    list.add(test);
                }

                future.complete(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });

        return future;
    }

}
