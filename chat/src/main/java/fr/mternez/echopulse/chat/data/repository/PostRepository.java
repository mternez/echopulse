package fr.mternez.echopulse.chat.data.repository;

import fr.mternez.echopulse.chat.data.model.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface PostRepository extends CrudRepository<Post, String>{
    Set<Post> findAllByChannelId(UUID channelId);

    void deleteAllByChannelId(UUID channelId);
}
