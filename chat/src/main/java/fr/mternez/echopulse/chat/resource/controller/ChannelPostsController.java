package fr.mternez.echopulse.chat.resource.controller;

import fr.mternez.echopulse.chat.data.model.Post;
import fr.mternez.echopulse.chat.data.repository.PostRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
public class ChannelPostsController {

    private final PostRepository postRepository;

    ChannelPostsController(
            final PostRepository postRepository
    ) {
        this.postRepository = postRepository;
    }

    @GetMapping("{channelId}/posts")
    public Set<Post> getLatest(@PathVariable UUID channelId) {
        return this.postRepository.findAllByChannelId(channelId);
    }
}
